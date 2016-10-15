package com.sina.sinavideo.sdk.log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;

import com.sina.sinavideo.sdk.utils.VDLog;
import com.sina.sinavideo.sdk.utils.VDApplication;
import com.sina.sinavideo.sdk.utils.VDFileUtil;
import com.sina.sinavideo.sdk.utils.VDUtility;

/**
 * 日志上报工具类，可完成日志的实时上传和断网重连的日志上传工作
 * 
 * @author jiantian1
 * @author hongchao
 * 
 *         原理： 1.初始化时候把所有日志按行读出来（根据配置文件读相应的数据行），并放到handler中放送。
 *         2.当sdk没写一行日志，根据当前状态，符合的话，发一个消息到handler。
 *         3.当发送失败或者断网的时候，把所有消息移除掉并重新初始化；
 * 
 */
public class LogPushManager implements Runnable {

    private static final String TAG = LogPushManager.class.getSimpleName();

    private static final long FILES_MAX_SIZE = 10 * 1024 * 1024;// 10M
    private static final long FILE_MAX_SIZE = 1 * 1024 * 1024;// 1M
    private static final String PUSH_LOG_CONTENT_TYPE = "image/gif";// push上报返回的type

    /** 日志后缀 **/
    private static final String FILE_SUFFIX = ".sta";
    /** 日志存放目录 **/
    // private static final File LOG_FILE_PARENT = new
    // File(DiskLruCache.getRootFile(), "stat");
    private final File fParent;
    /** 日志文件过滤器 **/
    private static final FilenameFilter FILTER = new FilenameFilter() {

        @Override
        public boolean accept(File dir, String filename) {
            return filename.endsWith(FILE_SUFFIX);
        }
    };
    /** 待上传的日志文件 **/
    private List<File> mPushFiles = new ArrayList<File>();

    private static final String LOGPUSH_NAME = "log_push";
    private static final String LOGPUSH_FILE = "log_push_file";// 记录上传成功的文件名
    private static final String LOGPUSH_LINE = "log_push_line";// 记录上传成功的文件行
    private static final String LOGPUSH_SPITE = Statistic.TAG_OR;// 文件行的分割符号,分割上传信息和本地信息
    private static final String LOGPUSH_WAIT_LAST_FILE = "log_push_wait_last_file";// 记录最近等待上传的文件名
    private static final String LOGPUSH_WAIT_LAST_LINE = "log_push_wait_last_line";// 记录最近等待上传的文件行

    private static final String LOGPUSH_WRITE_FILE_INDEX = "log_push_write_file";// 记录正在的文件名标号，如何1，2，3

    private static final int CONNECT_COUNT = 3;// 请求次数
    private static final long RETRY_TIME = 200000;// 重试时间间隔

    private static final int MSG_REQUEST = 1;// 发送请求
    private static final int MSG_STOP = 2; // 停止
    private static final int MSG_RECONNECTION = 3;// 断线重连
    private static final int MSG_RETRY = 4;// 重试
    private LogPushHandler mPushHandler;
    private final Context fContext;
    private SharedPreferences mSharedPreferences;// 记录上传成功
    private boolean mHasNetwork = true;
    private boolean mRetry;// 发送重试
    private boolean mSupply;// 用于记录是否补发
    private boolean mInit;// 是否正在获取所以日志

    private ThreadPoolExecutor mExecutor;

    /**
     * 构造函数
     * 
     * @param context
     */
    public LogPushManager(Context context) {
        fContext = context;
        fParent = new File(context.getFilesDir(), "stat");

        mExecutor = new ThreadPoolExecutor(0, 1, 5, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {

                    private final AtomicInteger mCount = new AtomicInteger(1);

                    @Override
                    public Thread newThread(Runnable r) {
                        return new Thread(r, TAG + " #" + mCount.getAndIncrement());
                    }
                }, new ThreadPoolExecutor.DiscardOldestPolicy());

        mSharedPreferences = fContext.getSharedPreferences(LOGPUSH_NAME, Context.MODE_PRIVATE);
        HandlerThread imageLoader = new HandlerThread(TAG);
        imageLoader.start();
        mPushHandler = new LogPushHandler(imageLoader.getLooper());
        new Thread(this).start();

    }

    /**
     * 处理日志的Handler继承类
     * 
     * @author hongchao
     * 
     */
    class LogPushHandler extends Handler {

        public LogPushHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg == null)
                return;
            switch (msg.what) {

                case MSG_REQUEST : // 收到请求
                    if (VDApplication.getInstance().isNetworkConnected()) {// 判断是否有网络
                        mHasNetwork = true;
                        boolean success = false;
                        URL url = null;
                        try {
                            if (VDApplication.getInstance().mDebug) {
                            	if(!TextUtils.isEmpty(LogProperty.PUSH_DEBUG_URL))
                                url = new URL(LogProperty.PUSH_DEBUG_URL);
                            } else {
                            	if(!TextUtils.isEmpty(LogProperty.PUSH_URL))
                                url = new URL(LogProperty.PUSH_URL);
                            }
                        } catch (MalformedURLException e1) {
                            e1.printStackTrace();
                        }
                        if (url != null) {
                            VDLog.d(TAG, "handleMessage MSG_REQUEST msg.obj.toString() == " + msg.obj.toString());
                            String[] logLine = msg.obj.toString().split(LOGPUSH_SPITE);// filename|systemtime|line
                            if (logLine.length < 3) {// 小于3的把日志清空// TODO
                                VDLog.e(TAG,
                                        " 清空错误日志 logLine.length  === " + logLine.length + " -- " + msg.obj.toString());
                                VDFileUtil.deleteFileDir(fParent);
                                clearSharedPreferences();
                                mRetry = false;
                                mSupply = false;
                                removeMessage();
                                break;
                            }
                            String fileName = logLine[0];
                            String pushLine = logLine[2];
                            for (int i = 0; i <= CONNECT_COUNT; i++) {
                                // 网络请求
                                HttpURLConnection httpURLConnection = null;
                                DataOutputStream out = null;
                                try {
                                    httpURLConnection = getHttpURLConnection(url);
                                    String deviceName = android.os.Build.MODEL;
                                    String androidVer = android.os.Build.VERSION.RELEASE;
                                    String uaPrefix = LogProperty.mAppName + "/"
                                            + VDApplication.getInstance().getAPPVersion();

                                    httpURLConnection.setRequestProperty("User-Agent", uaPrefix + " android/"
                                            + androidVer + "/" + deviceName);
                                    httpURLConnection.setRequestProperty("Content-Type",
                                            "application/x-www-form-urlencoded");

                                    httpURLConnection.connect();
                                    out = new DataOutputStream(httpURLConnection.getOutputStream());
                                    out.writeBytes("sendtime="
                                            + VDUtility.generateTime(System.currentTimeMillis(), true) + "&" + "try="
                                            + i + "&" + pushLine);// 加入发送时间和重试次数
                                    out.flush();
                                    int response = httpURLConnection.getResponseCode();
                                    String contentType = httpURLConnection.getContentType();
                                    VDLog.d(TAG, "response == " + response + " -- " + contentType);
                                    // 判断返回码是200 并且
                                    // content_type是image/gif才认为是成功的
                                    success = (response == HttpURLConnection.HTTP_OK && PUSH_LOG_CONTENT_TYPE
                                            .equals(contentType));
                                } catch (MalformedURLException e) {
                                    VDLog.d(TAG, "MalformedURLException");
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    VDLog.d(TAG, "IOException");
                                    e.printStackTrace();
                                } catch (Exception e) {
                                    VDLog.d(TAG, "Exception");
                                    e.printStackTrace();
                                } finally {
                                    if (out != null) {
                                        try {
                                            out.close();
                                            out = null;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (httpURLConnection != null) {
                                        httpURLConnection.disconnect();
                                        httpURLConnection = null;
                                    }
                                }

                                if (success) {// 成功
                                    VDLog.d(TAG, "success");
                                    mRetry = false;
                                    String timeLine = logLine[1] + LOGPUSH_SPITE + pushLine;
                                    String oldFileName = mSharedPreferences.getString(LOGPUSH_FILE, null);
                                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                                    editor.putString(LOGPUSH_FILE, fileName);
                                    editor.putString(LOGPUSH_LINE, timeLine);
                                    editor.commit();
                                    String waitLastFileName = mSharedPreferences
                                            .getString(LOGPUSH_WAIT_LAST_FILE, null);
                                    VDLog.d(TAG, "oldFileName = " + oldFileName + " , fileName = " + fileName
                                            + " , waitLastFileName = " + waitLastFileName);
                                    if (!TextUtils.isEmpty(oldFileName) && !oldFileName.equals(fileName)) {
                                        VDFileUtil.deleteFile(new File(fParent, oldFileName));
                                    } else if (mSupply && !TextUtils.isEmpty(waitLastFileName)
                                            && waitLastFileName.equals(fileName)) {// 当是补发的情况，并且文件名等于记录等待上传的文件名时候
                                        VDLog.d(TAG, "pushLine = " + timeLine + " -- wait_last_line = "
                                                + mSharedPreferences.getString(LOGPUSH_WAIT_LAST_LINE, null));
                                        if (timeLine.equals(mSharedPreferences.getString(LOGPUSH_WAIT_LAST_LINE, null))) {
                                            // 当是补发的情况，当前行等于记录等待上传的文件的最后一行时候，证明是补发完成继续补发。
                                            init();
                                        }

                                    }
                                    break;
                                } else if (!VDApplication.getInstance().isNetworkConnected()) {
                                    mHasNetwork = false;
                                    removeMessage();
                                    break;
                                }
                            }
                        }
                        if (!success) {// 重发
                            VDLog.d(TAG, "重发");
                            mRetry = true;
                            removeMessages(MSG_RETRY);
                            removeMessages(MSG_REQUEST);
                            sendEmptyMessageDelayed(MSG_RETRY, RETRY_TIME);
                        }

                    } else {
                        VDLog.d(TAG, "isNetworkConnected not");
                        mHasNetwork = false;
                        mRetry = false;
                        removeMessage();
                    }

                    break;

                case MSG_RETRY :// 收到重试
                case MSG_RECONNECTION : // 收到断线重连
                    init();
                    break;

                case MSG_STOP : // 收到终止指令
                    Looper.myLooper().quit();
                    break;

            }
        }
    }

    /**
     * 发送日志上传请求
     * 
     * @param line
     * @see LogPushManager#sendRequest(String, long)
     */
    private void sendRequest(String line) {
        if (mHasNetwork && !mRetry && !mSupply) {
            VDLog.d(TAG, "sendRequest line = " + line);
            sendRequest(line, 0);
        }
    }

    /** 写文件的时候调用 **/
    private void sendRequestWhenWrite(String line) {
        if (!mInit) {// 非初始化时，写文件可以发送请求消息
            sendRequest(line);
        }
    }

    /**
     * 发送日志上传请求
     * 
     * @param line
     *            日志内容
     * @param delayMillis
     *            延时毫秒数
     */
    private void sendRequest(String line, long delayMillis) {
        if (mPushHandler != null) {
            Message newMsg = mPushHandler.obtainMessage();
            newMsg.what = MSG_REQUEST;
            newMsg.obj = line;
            mPushHandler.sendMessageDelayed(newMsg, delayMillis);
        }
    }

    /**
     * 获取日志文件list
     */
    private void getLogFileList() {
        mPushFiles.clear();
        if (!VDApplication.getInstance().isNetworkConnected()) {
            mHasNetwork = false;
            return;
        }
        mHasNetwork = true;
        File parent = fParent;
        File[] filterFiles = parent.listFiles(FILTER);
        if (filterFiles != null && filterFiles.length > 0) {
            String currentPushFile = mSharedPreferences.getString(LOGPUSH_FILE, "");// 当前正在上传的文件
            long length = 0;
            for (File file : filterFiles) {// 遍历文件，并按照顺序存放
                VDLog.d(TAG, " name = " + file.getName() + " -- " + file.isFile());
                if (file.isFile()) {
                    int compare = file.getName().compareTo(currentPushFile);// （用户没改文件名的情况下）
                    if (compare < 0) {// 文件比上传文件小，肯定是上传过的，删除
                        VDFileUtil.deleteFile(file);
                    } else if (compare == 0) {// 相等，放到mPushFiles的第一位
                        length += file.length();
                        if (checkSize(length)) {
                            mSupply = false;
                            VDFileUtil.deleteFileDir(parent);
                            return;
                        }
                        mPushFiles.add(0, file);
                    } else {// 大于0,顺序放
                        length += file.length();
                        if (checkSize(length)) {
                            mSupply = false;
                            VDFileUtil.deleteFileDir(parent);
                            return;
                        }
                        mPushFiles.add(file);
                    }
                }
            }
            int size = mPushFiles.size();
            if (size > 0) {
                String currentLine = mSharedPreferences.getString(LOGPUSH_LINE, null);
                boolean hadFind = currentLine == null;// 是否需要查找补发的行，
                File currentFile = null;
                for (int i = 0; i < size; i++) {
                    currentFile = mPushFiles.get(i);
                    VDLog.d(TAG, "getLogFileList for currentFile = " + currentFile + "== currentLine = " + currentLine);
                    if (i > 0 && !hadFind) {// 只要是第一个找不到，其他日志都删除
                        VDFileUtil.deleteFile(currentFile);
                    } else {
                        hadFind = readPushFile(currentFile, currentLine, hadFind);
                        if (!hadFind) {// 没找到，删除所以文件
                            VDFileUtil.deleteFile(currentFile);
                        }
                    }
                }
                mSupply = hadFind;
                String waitLastLine = mSharedPreferences.getString(LOGPUSH_WAIT_LAST_LINE, null);
                VDLog.d(TAG, "getLogFileList waitLastLine ==" + waitLastLine + "== mSupply = " + mSupply);
                if (TextUtils.isEmpty(waitLastLine)) {
                    mSupply = false;
                }
                mPushFiles.clear();
            } else {
                mSupply = false;
                clearSharedPreferences();
            }
        } else {
            mSupply = false;
            clearSharedPreferences();
        }
    }

    /**
     * 获取当前上传文件的行和文件名称，当找到以后将文件名称和对应的行内容保存到SharedPreferences里面
     * 
     * @param file
     *            日志文件
     * @param currentLine
     *            当前行对应的内容
     * @param hadFind
     *            是否已经找到对应的行
     * @return
     */
    private boolean readPushFile(File file, String currentLine, boolean hadFind) {
        // boolean hadFind = false;// 是否找到当前行
        BufferedReader br = null;
        FileReader fr = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = null;
            String name = file.getName();
            String lastLine = null;
            // while ((line = br.readLine()) != null) {
            while (!TextUtils.isEmpty((line = br.readLine()))) {
                if (hadFind) {// 发送行
                    sendRequest(name + LOGPUSH_SPITE + line);
                    lastLine = line;
                } else if (line.equals(currentLine)) {// 查找当前行
                    hadFind = true;
                }
            }
            if (!hadFind) {
                clearSharedPreferences();
            } else {// 找到，就记录当前发送的文件名，和该文件的当前发送的行
                SharedPreferences.Editor editor = mSharedPreferences.edit();
                editor.putString(LOGPUSH_WAIT_LAST_FILE, name);
                editor.putString(LOGPUSH_WAIT_LAST_LINE, lastLine);
                editor.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fr != null) {
                    fr.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return hadFind;
    }

    /**
     * 通过url创建一个{@link HttpURLConnection}对象
     * 
     * @param url
     *            请求的url
     * @return {@link HttpURLConnection}对象
     * @throws IOException
     */
    public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");// 提交模式
        httpURLConnection.setConnectTimeout(20000);// 连接超时 单位毫秒
        httpURLConnection.setReadTimeout(20000);// 读取超时 单位毫秒
        httpURLConnection.setDoOutput(true);// 是否输入参数
        httpURLConnection.setDoInput(true);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setInstanceFollowRedirects(true);
        return httpURLConnection;
    }

    /**
     * 清除数据
     */
    private void clearSharedPreferences() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    // @Override
    public void wifiConnected() {
        connected();
    }

    // @Override
    public void mobileConnected() {
        connected();
    }

    // @Override
    public void nothingConnected() {// 断网时候
        mHasNetwork = false;
        mRetry = false;
        removeMessage();
    }

    /**
     * 通知网络改变
     */
    private void connected() {
        synchronized (LogPushManager.class) {
            if (!mHasNetwork) {
                mHasNetwork = true;
                mRetry = false;
                removeMessage();
                mPushHandler.sendEmptyMessage(MSG_RECONNECTION);
            }
        }
    }

    /**
     * 检查文件大小是否越界，峰值{@link #FILES_MAX_SIZE}
     * 
     * @param length
     *            文件大小
     * @return
     */
    private boolean checkSize(long length) {
        return length >= FILES_MAX_SIZE;
    }

    /**
     * 销毁
     */
    public void destory() {
        mHasNetwork = false;
        removeMessage();
        mPushHandler.sendEmptyMessage(MSG_STOP);
    }

    @Override
    public void run() {
        // push filter file
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        init();
    }

    /**
     * 将日志填充到文件中
     * 
     * @param line
     *            日志内容
     * @see #writeStringToFile(File, String, boolean, int)
     */
    public void writeToFile(final String line) {
        VDLog.d("LogPushManagerSeenLog", line);
        mExecutor.execute(new Runnable() {

            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                // if (FileUtil.isSdcardReady()) {
                int fileIndex = mSharedPreferences.getInt(LOGPUSH_WRITE_FILE_INDEX, 1);
                File file = new File(fParent, "stat_" + fileIndex + FILE_SUFFIX);
                writeStringToFile(file, line, true, fileIndex);
                // }
            }
        });
    }

    /**
     * 写字符串到文件, 文件父目录如果不存在，会自动创建
     * 
     * @param file
     *            被写入的文件
     * @param content
     *            日志内容
     * @param append
     *            是否追加
     * @param fileIndex
     *            文件对应的索引
     * @return
     */
    private boolean writeStringToFile(File file, String content, boolean append, int fileIndex) {
        boolean isWriteOk = false;
        char[] buffer = null;
        int count = 0;
        BufferedReader br = null;
        BufferedWriter bw = null;
        try {
            if (!file.exists()) {
                File dir = file.getParentFile();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                if (checkSize(getDirSize(dir))) {
                    VDLog.d(TAG, "文件夹超过 " + FILES_MAX_SIZE + "M，清空文件夹！");
                    VDFileUtil.deleteFileDir(dir);
                    clearSharedPreferences();
                }
                file.createNewFile();
            }
            if (file.exists()) {
                br = new BufferedReader(new StringReader(content));
                bw = new BufferedWriter(new FileWriter(file, append));
                buffer = new char[1024];
                int len = 0;
                while ((len = br.read(buffer, 0, 1024)) != -1) {
                    bw.write(buffer, 0, len);
                    count += len;
                }
                bw.newLine();// 换行
                bw.flush();
                sendRequestWhenWrite(file.getName() + LOGPUSH_SPITE + content);// 发请求
                if (file.length() >= FILE_MAX_SIZE) {// 每写完一行再检测是否超过限制大小
                    SharedPreferences.Editor editor = mSharedPreferences.edit();
                    editor.putInt(LOGPUSH_WRITE_FILE_INDEX, fileIndex + 1);
                    editor.commit();
                }
            }
            isWriteOk = content.length() == count;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                    bw = null;
                }
                if (br != null) {
                    br.close();
                    br = null;
                }
                buffer = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isWriteOk;
    }

    /**
     * 获取文件夹根目录文件的大小
     * 
     * @param dir
     *            文件夹
     * @return
     */
    private long getDirSize(File dir) {
        File[] filterFiles = dir.listFiles(FILTER);
        long size = 0;
        if (filterFiles != null && filterFiles.length > 0) {
            for (File file : filterFiles) {
                if (file.isFile()) {
                    size += file.length();
                }
            }
        }
        return size;
    }

    /** 初始化handler队列和状态 **/
    private void init() {
        VDLog.d(TAG, "getLogFileList init =====");
        try {
            mInit = true;
            mRetry = false;
            mSupply = false;
            removeMessage();
            getLogFileList();
        } catch (Exception e) {
            e.printStackTrace();
            VDLog.e(TAG, "getLogFileList error!", e);
        } finally {
            mInit = false;
        }
    }

    /** 重置状态和移除所有消息 **/
    private void removeMessage() {
        mPushHandler.removeCallbacksAndMessages(null);
    }
}