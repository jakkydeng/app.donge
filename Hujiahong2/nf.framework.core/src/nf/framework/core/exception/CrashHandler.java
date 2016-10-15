/**
 * 
 */
package nf.framework.core.exception;

/**
 * @author niufei 
 *
 */
import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;
import java.util.TreeSet;

import nf.framework.core.util.android.CheckInternet;
import nf.framework.core.util.android.CloseActivityClass;
import nf.framework.core.util.io.FileUtils;
import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类 来接管程序,并记录 发送错误报告.
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	/** Debug Log tag */
	public static final String TAG = "CrashHandler";
	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/** CrashHandler实例 */
	private static CrashHandler INSTANCE;
	/** 程序的Context对象 */
	private Context mContext;
	private CrashMessageCallBack mCrashMessageCallBack;

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			// 如果用户没有处理则让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			// Sleep一会后结束程序
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				Log.e(TAG, "Error : ", e);
			}
			CloseActivityClass.exitClient(mContext);
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(10);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		// final String msg =ex.getCause().getMessage();
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();

				Toast.makeText(mContext,"应用异常，请重启应用", Toast.LENGTH_LONG).show();
				Looper.loop();
				// 关闭所有activity
				CloseActivityClass.exitClient(mContext);
			}

		}.start();

		try {
			CrashInfoProperty crashInfoProperty=new CrashInfoProperty();
			// 收集设备信息
			crashInfoProperty.collectCrashDeviceInfo(mContext);
			// 保存错误报告文件
			crashInfoProperty.saveCrashInfoToFile(mContext,ex);
			// 发送错误报告到服务器
			sendCrashReportsToServer(mContext);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getStackTrace();
		}

		return true;
	}

	/**
	 * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
	 * 
	 * @throws Exception
	 */
	public void sendPreviousReportsToServer(CrashMessageCallBack mCrashMessageCallBack) {
	
		this.mCrashMessageCallBack=mCrashMessageCallBack;
		sendCrashReportsToServer(mContext);
	}

	/**
	 * 已检测网络 把错误报告发送给服务器,包含新产生的和以前没发送的.
	 * 
	 * @param ctx
	 * @throws Exception
	 */
	private void sendCrashReportsToServer(final Context ctx) {
		if (!CheckInternet.checkInternet(ctx)) {
			return;
		}
		new Thread() {
			@Override
			public void run() {
				try {
					String[] crFiles = CrashInfoProperty.getCrashReportFiles(ctx);
					if (crFiles != null && crFiles.length > 0) {
						TreeSet<String> sortedFiles = new TreeSet<String>();
						sortedFiles.addAll(Arrays.asList(crFiles));

						for (String fileName : sortedFiles) {
							File cr = new File(ctx.getFilesDir(), fileName);
							postReport(cr);
							cr.delete();// 删除已发送的报告
						}
					}
				} catch (Exception e) {

				}
			}
		}.start();
	}

	private void postReport(File file) throws Exception {
		// TODO 使用HTTP Post 发送错误报告到服务器
		// 这里不再详述,开发者可以根据OPhoneSDN上的其他网络操作
		// 教程来提交错误报告
		String message = FileUtils.read(file);
		if (!TextUtils.isEmpty(message)) {
			LogUtil.d(mContext, message);
		
			if(mCrashMessageCallBack!=null){
				mCrashMessageCallBack.asyncPostCrashMessageInfo(message);
			}
		
		}
	}

	
	public interface CrashMessageCallBack{
		
		public void asyncPostCrashMessageInfo(String message);
	}
}