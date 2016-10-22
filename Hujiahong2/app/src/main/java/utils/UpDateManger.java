package utils;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.hujiahong.hujiahong.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import nf.framework.core.util.android.ClickUtil;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by 胡加宏 on 2016/10/23.
 */

public class UpDateManger {
    //downLoadApk
    private static  UpDateManger ydbUpdateHelper;
    private static Context mcontext;
    private int progress; // 定义进度值
    int handmsg = 1;//
    private NotificationManager nm = null;
    private Notification nn = null; // 引入通知
    private RemoteViews view = null; // 用来设置通知的View
    private String apkDownloadPath; // 应用下载的地址
    private String savePath; // APK下载之后保存的地址
    private String saveFileName; // APK的文件名
    private static final int DOWN_UPDATE = 0;// 下载中消息
    private static final int DOWN_OVER = 1;// 下载完成消息
    private AlertDialog dlg = null;
    String softVersion = "";
    private UpDateManger() {
    }

    public static UpDateManger getInstanse(Context context) {
        if (ydbUpdateHelper == null) {
            ydbUpdateHelper = new UpDateManger();
        }
        mcontext = context;
        return ydbUpdateHelper;
    }

    private void initDownLoad(String versionCode){
        nm = (NotificationManager)(mcontext. getSystemService(NOTIFICATION_SERVICE));//获取系统通知的服务
        nn = new Notification();//创建一个通知对象

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //安装包的路径
        apkDownloadPath = "";
        // 存放位置为手机默认目录下的NotificationDemo文件夹（如果没有会默认生成一个这样的文件夹，详见下载块）
        savePath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/zbsd.ydb"+versionCode;
        // 为了测试我们把下载的apk的文件名也明明为NotificationDemo
        saveFileName = savePath + "/zbsd.ydb.apk";
    }
    private void  downLoadApk(){
        ShowToast("开始在后台下载新版本", mcontext);
        view = new RemoteViews(mcontext.getPackageName(), R.layout.download_progress_state_view);
        nn.icon = R.drawable.ic_launcher;
        view.setImageViewResource(R.id.download_progress_img, R.drawable.ic_launcher);
        new Thread(mdownApkRunnable).start();
        //如需使用外部浏览器下载，注释掉上边的线程，解开此句即可
        //downloadByBrowser(apkDownloadPath);
    }


    public void onBtnClick(String versionCode,String DownloadPath) {
        initDownLoad(versionCode);
        apkDownloadPath  = DownloadPath;

        if (ClickUtil.isFastDoubleClick()) {
            return;
        }
        File file = new File(savePath);

        if (file.exists() && !softVersion.equals(versionCode)) { //如果最新安装包存在
            installApk();
            return;
        }


        if (softVersion.equals(versionCode)) {
            ShowToast("您已经是最新版本", mcontext);
        } else {

            if (TextUtils.isEmpty(apkDownloadPath)) {
                Toast.makeText(mcontext,"下载地址为空请联系客服",Toast.LENGTH_SHORT).show();
                return;
            }
            downLoadApk();


        }
        return;

    }


    // 下载APK的线程匿名类START
    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                if (TextUtils.isEmpty(apkDownloadPath)){
                    return;
                }
                URL url = new URL(apkDownloadPath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                Log.e("test", file.exists()+"");
                if (!file.exists()) {
                    Log.e("test1", file.exists()+"");
                    file.mkdir();
                    Log.e("test2", file.exists()+"");
                }
                String apkFile = saveFileName;
                Log.e("test3", apkFile);
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                int count = 0;
                byte buf[] = new byte[1024];
                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    if(handmsg < progress){
                        handmsg ++;
                        mHandler.sendEmptyMessage(DOWN_UPDATE);
                    }
                    // 更新进度
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (true);// 点击取消就停止下载.
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("test", e.getMessage());
            }
        }
    };
    // 下载APK的线程匿名类END

    // 处理下载进度的Handler Start
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    //ShowToast(progress, DownLoadApk.this);
                    view.setProgressBar(R.id.download_progressbar, 100, handmsg,false);
                    view.setTextViewText(R.id.download_progress_text, handmsg + "%");
                    nn.contentView = view;
                    nm.notify(0, nn);
                    super.handleMessage(msg);
                    break;
                case DOWN_OVER:
                    ShowToast("下载完成",mcontext);
                    installApk();
                    break;
                default:
                    break;
            }
        };
    };
    // 处理下载进度的Handler End

    private void showDig(){ //免流量安装的dialog，目前用不上
        dlg = new AlertDialog.Builder(mcontext)
                .setTitle("安装")
                .setMessage("下载完成是否安装")
                .setPositiveButton("安装",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        installApk();
                        //install.setVisibility(View.GONE);
                    }})
                .setNegativeButton("删除",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (deleteAllFilesOfDir(new File(savePath))) {
                            ShowToast("存放目录和APK已删除", mcontext);
                        } else {
                            ShowToast("删除失败，请检查路径，并手动删除", mcontext);
                        }
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dlg.dismiss();
                    }
                }).show();
    }
    //Toast方法（实在是懒得写一次Toast就写一次make写一次show。有时候show还忘了。。就这么干了，成学员要学着变懒才有优化代码的动力）
    private static void ShowToast(Object msg,Context context){
        Toast.makeText(context, msg+"", Toast.LENGTH_SHORT).show();
    }

    // 安装apk
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            ShowToast("要安装的文件不存在，请检查路径",mcontext);
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
                "application/vnd.android.package-archive");
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mcontext.startActivity(i);
    }

    // 删除APK
    /**
     * @param path  Apk存放的目录，是目录，不是APK文件的路径！否则只会删除APK 不会删除存放的目录
     * @return
     */
    public static boolean deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return false;
        if (path.isFile()) {
            path.delete();
            return true;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
        return true;
    }
}
