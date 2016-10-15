package nf.framework.core.exception;
import android.content.Context;
import android.util.Log;

import java.io.File;

import nf.framework.core.util.DateUtils;
import nf.framework.core.util.io.FileUtils;

/**
 * 错误日志统计
 * @author niufei
 *
 */
public class LogUtil {

	public static boolean OpenBug=false;
	
	private static Context mcontext;
	/**
	 * 初始化log管理器 
	 * @param context
	 */
	public static void init(Context context){
		
		mcontext=context;
	}
	
	public static void writeExceptionLog(Throwable ex){
		if(mcontext==null){
			return;
		}
		try {
			CrashInfoProperty crashInfoProperty=new CrashInfoProperty();
			// 收集设备信息
			crashInfoProperty.collectCrashDeviceInfo(mcontext);
			// 保存错误报告文件
			crashInfoProperty.saveCrashInfoToFile(mcontext,ex);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
		}
	}
	
//	/**
//	 * 保存错误信息到文件中
//	 * @param ex
//	 * @return
//	 */
//	public static  void writeExceptionLog(Context context,Throwable ex) {
//		if(mcontext==null){
//			return;
//		}
//		Writer info = new StringWriter();
//		PrintWriter printWriter = new PrintWriter(info);
//		ex.printStackTrace(printWriter);
//
//		Throwable cause = ex.getCause();
//		while (cause != null) {
//			cause.printStackTrace(printWriter);
//			cause = cause.getCause();
//		}
//
//		String result = info.toString();
//		printWriter.close();
//		try {
//			long timestamp = System.currentTimeMillis();
//			String fileName = "ex-" + timestamp;
//			String fileFolderPath=context.getCacheDir()+File.separator+"log";
//			if(!new File(fileFolderPath).exists()){
//				new File(fileFolderPath).mkdirs();
//			}
//			String filePath=fileFolderPath+File.separator+fileName;
//			FileUtils.getInstance().write(new File(filePath), result);
//		} catch (Exception e) {
//			Log.e("", "an error occured while writing report file...", e);
//		}
//	}
//	
//	
	
	public static void e(Context mcontext,String errorMsg){
		if(!OpenBug){
			return;
		}
		e(getTag(mcontext),""+errorMsg);	
	}
	
	public static void e(String tag,String errorMsg){
		if(!OpenBug){
			return;
		}
		Log.e(""+tag,""+errorMsg);	
		saveLogToFile("e",tag,errorMsg);
	}
	
	public static void d(Context mcontext,String errorMsg){
		if(!OpenBug){
			return;
		}
		d(getTag(mcontext),""+errorMsg);	
	}
	public static void d(String tag,String errorMsg){
		if(!OpenBug){
			return;
		}
		Log.d(""+tag,""+errorMsg);	
		saveLogToFile("d",tag,errorMsg);
	}
	
	public static void w(Context mcontext,String errorMsg){
		if(!OpenBug){
			return;
		}
		w(getTag(mcontext),""+errorMsg);	
	}
	public static void w(String tag,String errorMsg){
		if(!OpenBug){
			return;
		}
		Log.w(""+tag,""+errorMsg);	
		saveLogToFile("w",tag,errorMsg);
	}
	public static void w(String tag ,String errorMsg,Throwable throwable){
		
		writeExceptionLog(throwable);
		if(!OpenBug){
			return;
		}
		w(""+tag,""+errorMsg);
	}
	
	public static void i(Context mcontext,String errorMsg){
		if(!OpenBug){
			return;
		}
		i(getTag(mcontext),errorMsg);	
	}
	public static void i(String tag,String errorMsg){
		if(!OpenBug){
			return;
		}
		Log.i(""+tag,""+errorMsg);	
		saveLogToFile("i",tag,errorMsg);
	}
	public static void v(Context mcontext,String errorMsg){
		if(!OpenBug){
			return;
		}
		v(getTag(mcontext),""+errorMsg);	
	}
	public static void v(String tag,String errorMsg){
		if(!OpenBug){
			return;
		}
		Log.v(""+tag,""+errorMsg);	
		saveLogToFile("v",tag,errorMsg);
	}
	private static String getTag(Context mcontext){
		
		return mcontext!=null?mcontext.getClass().getSimpleName():LogUtil.class.getSimpleName();
	}

	/**
	 *
	 * @param type
	 * @param tag
	 * @param param
     */
	private static void saveLogToFile(String type,String tag ,String param) {
		if(mcontext==null){ return; }
		try {
			String path =mcontext.getExternalCacheDir()
					.getPath() + "/log_"+DateUtils.getNowTime("yyyy_MM_dd")+".txt";
			File file = new File(path);
			if (!file.exists()) {
				file.createNewFile();
			}
			StringBuffer sb2 = new StringBuffer();
			sb2.append("\r\n");
			sb2.append("time --"+DateUtils.getNowTime(DateUtils.DATE_TIME_STR)+"-----type="+type+":::::tag="+ tag +"====>"+param);
			FileUtils.appendFileData(path, sb2.toString());
		} catch (Exception e) {
		}
	}
	
}
