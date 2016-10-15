package com.sina.sinavideo.sdk.monitor;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.zip.GZIPOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.HandlerThread;

import com.sina.sinavideo.sdk.utils.VDApplication;
import com.sina.sinavideo.sdk.utils.VDFileUtil;
import com.sina.sinavideo.sdk.utils.VDLog;
import com.sina.sinavideo.sdk.utils.VDUtility;
import com.sina.sinavideo.sdk.utils.VDMonitorManager.VDMonitorHandler;

/**
 * sina的elk系统，发送的监控日志
 * 
 * @author alexsun
 */
public class VDSinaELKMonitorHandler implements VDMonitorHandler {

    private HandlerThread mFileThread = null;
    private Handler mFileHandler = null;
    private final static int FILE_BUFFER_MAX_SIZE = 4096;
    private final static int SDCARD_REMAIN_SIZE = 4 * 1024 * 1024; // 单位为Byte

    private HandlerThread mNetThread = null;
    private Handler mNetHandler = null;
    private static final String prefixPath = "/dip/data.log";
    private static final String URL = "http://1001.log.dip.sina.com.cn/sinavideo.log";
    // 没有测试地址，这个dip那边也没有机器资源
    private static final String DEBUG_URL = "http://1001.log.dip.sina.com.cn/sinavideo.log";

    private static final String TAG = "VDSinaELKMonitorHandler";
    private String mLogPath = null;
    private File mLogFile = null;

    private static char JSON_HEADER_CHAR = '[';
    private static char JSON_END_CHAR = ']';
    private static char JSON_SPLIT_CHAR = ',';

    /**
     * 检查当前权限是否满足
     * 
     * @param context
     * @return
     */
    private boolean hasSDPermission(Context context) {
        String permission = "android.permission.WRITE_EXTERNAL_STORAGE";
        return (context.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    public VDSinaELKMonitorHandler() {
        super();

        mFileThread = new HandlerThread("VDSinaELKMonitorHandler.FileHandlerThread");
        mFileThread.start();
        mFileHandler = new Handler(mFileThread.getLooper());

        mNetThread = new HandlerThread("VDSinaELKMonitorHandler.NetHandlerThread");
        mNetThread.start();
        mNetHandler = new Handler(mNetThread.getLooper());

        boolean isCanCreateFile = true;
        Context context = VDApplication.getInstance().getContext();
        if (hasSDPermission(context)) {
            mLogPath = VDUtility.getSDCardDataPath(context) + prefixPath;
            if (!VDUtility.getSDCardRemainCanWrite(context, SDCARD_REMAIN_SIZE)) {
                isCanCreateFile = false;
            }
        } else {
            mLogPath = VDUtility.getDocumentPath(context) + prefixPath;
        }
        if (isCanCreateFile) {
            mLogFile = new File(mLogPath);
            initLogFile();
        }
    }

    private String appendLogFileEnd(String cont) {
        String ret = "";
        if (cont.equals(String.valueOf(JSON_HEADER_CHAR)) || cont.isEmpty()) {
            return ret;
        }
        if (cont.charAt(cont.length() - 1) == JSON_SPLIT_CHAR) {
            ret = cont.substring(0, cont.length() - 1);
            ret += JSON_END_CHAR;
        }
        return ret;
    }

    private void initLogFile() {
        if (mLogFile == null) {
            return;
        }
        if (!mLogFile.exists()) {
            // 新建文件
            VDFileUtil.createNewFileAndParentDir(mLogFile);
        }
        // 加入初始化的json前缀
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mLogFile, false);
            fos.write((int) JSON_HEADER_CHAR);
        } catch (IOException ex) {
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException ex) {

                }
            }
        }
    }

    private VDMonitorData mMonitorData = new VDMonitorData();

    private synchronized void send() {
        // TODO Auto-generated method stub
        mNetHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 先取得数据
                FileInputStream is = null;
                StringBuffer sb = new StringBuffer(FILE_BUFFER_MAX_SIZE);
                try {
                    is = new FileInputStream(mLogFile);
                    int logBuffer = 0;
                    while ((logBuffer = is.read()) != -1) {
                        sb.append((char) logBuffer);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }
                if (sb.length() == 0) {
                    return;
                }
                String buffer = appendLogFileEnd(sb.toString());
                if (buffer.isEmpty()) {
                    return;
                }
                // 发送到网络
                String url = URL;
                if (VDApplication.getInstance().mDebug) {
                    url = DEBUG_URL;
                }
                HttpPost post = new HttpPost(url);
                post.setHeader(HTTP.CONTENT_TYPE, "application/json");
                // 将得到的内容进行压缩
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bos.write(buffer.getBytes(Charset.forName("UTF-8")));

                    ByteArrayOutputStream gaos = new ByteArrayOutputStream();
                    GZIPOutputStream gos = new GZIPOutputStream(gaos);
                    bos.writeTo(gos);
                    gos.finish();

                    ByteArrayEntity bae = new ByteArrayEntity(gaos.toByteArray());
                    post.setEntity(bae);
                } catch (UnsupportedEncodingException ex) {
                    VDLog.e(TAG, ex.getMessage());
                } catch (IOException ex) {
                    VDLog.e(TAG, ex.getMessage());
                }
                DefaultHttpClient client = new DefaultHttpClient();
                HttpResponse response = null;
                try {
                    response = client.execute(post);
                    if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
                        VDLog.e(TAG, "request error,code:" + response.getStatusLine().getStatusCode());
                    }
                } catch (IOException ex) {
                    VDLog.e(TAG, ex.getMessage());
                }
                // 清理掉日志文件
                mLogFile.delete();
                VDFileUtil.createNewFileAndParentDir(mLogFile);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mLogFile, false);
                    fos.write(JSON_HEADER_CHAR);
                } catch (IOException ex) {

                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException ex) {
                            VDLog.e(TAG, ex.getMessage());
                        }
                    }
                }
            }
        });
    }

    @Override
    public void setStringPair(final String key, final String value) {
        // TODO Auto-generated method stub
        mFileHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mMonitorData.set(key, value);
            }
        });
    }

    @Override
    public synchronized void pile() {
        // TODO Auto-generated method stub
        mMonitorData.mDateTime = new Date().getTime();
        mFileHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                // 将数据序列化
                FileWriter fos = null;
                try {
                    if (!mLogFile.canWrite()) {
                        return;
                    }
                    fos = new FileWriter(mLogFile, true);

                    if (mMonitorData != null) {
                        String buffer = mMonitorData.toJson().toString() + JSON_SPLIT_CHAR;
                        if (buffer != null) {
                            try {
                                fos.write(buffer);
                            } catch (IOException ex) {
                                VDLog.e(TAG, ex.getMessage());
                            }
                        }
                    }
                } catch (IOException ex) {
                    VDLog.e(TAG, ex.getMessage());
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException ex) {
                            VDLog.e(TAG, ex.getMessage());
                        }
                    }
                }

            }
        });
    }

    @Override
    public void setIntPair(final String key, final int value) {
        // TODO Auto-generated method stub
        mFileHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mMonitorData.set(key, Integer.valueOf(value));
            }
        });
    }

    @Override
    public void start() {
        // TODO Auto-generated method stub
        // 每次都启动一个新的基础数据类
        mMonitorData = new VDMonitorData();
    }

    @Override
    public void stop() {
        // TODO Auto-generated method stub
        send();
    }

    @Override
    public void setStatus(final int status) {
        // TODO Auto-generated method stub
        mFileHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                mMonitorData.ticker();
                mMonitorData.mStatus = status;
            }
        });
    }

}
