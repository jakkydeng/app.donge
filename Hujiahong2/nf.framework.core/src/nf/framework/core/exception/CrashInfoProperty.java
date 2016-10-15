/**
 * 
 */
package nf.framework.core.exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Properties;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

/**

 * @TODO 

 * @author niufei

 * @Time 2014-6-5 下午5:38:06

 *

 */
public class CrashInfoProperty {
	
	/** 使用Properties来保存设备的信息和错误堆栈信息 */
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	private static final String STACK_TRACE = "STACK_TRACE";
	/** 错误报告文件的扩展名 */
	private static final String CRASH_REPORTER_EXTENSION = ".cr";
	
	/**
	 * 获取错误报告文件名
	 * 
	 * @param ctx
	 * @return
	 */
	public static String[] getCrashReportFiles(Context ctx) {
		File filesDir = ctx.getFilesDir();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(CRASH_REPORTER_EXTENSION);
			}
		};
		return filesDir.list(filter);
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return
	 * @throws IOException
	 */
	public String saveCrashInfoToFile(Context context,Throwable ex) throws IOException {
		if(context==null){
			return null;
		}
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		String result = info.toString();
		// SaveInterfaceUrlToFile(result);
		printWriter.close();
		mDeviceCrashInfo.put(STACK_TRACE, result);
		mDeviceCrashInfo.setProperty(STACK_TRACE, result);
		long timestamp = System.currentTimeMillis();
		String fileName = "crash-" + timestamp + CRASH_REPORTER_EXTENSION;
		FileOutputStream trace = context.openFileOutput(fileName, Context.MODE_PRIVATE);
		mDeviceCrashInfo.store(trace, "");
		// trace.write(result.getBytes());
		trace.flush();
		trace.close();
		return fileName;
	}

	/**
	 * 收集程序崩溃的设备信息
	 * 
	 * @param ctx
	 * @throws NameNotFoundException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void collectCrashDeviceInfo(Context ctx) throws NameNotFoundException, IllegalArgumentException, IllegalAccessException {
		PackageManager pm = ctx.getPackageManager();
		PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
		if (pi != null) {
			mDeviceCrashInfo.put(VERSION_NAME, pi.versionName == null ? "not set" : pi.versionName);
			mDeviceCrashInfo.put(VERSION_CODE, String.valueOf(pi.versionCode));
		}
		// 使用反射来收集设备信息.在Build类中包含各种设备信息,
		// 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
		// 具体信息请参考后面的截图
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			mDeviceCrashInfo.put(field.getName(), String.valueOf(field.get(null)));
			LogUtil.d(ctx,field.getName() + " : " + field.get(null));
		}

	}

}
