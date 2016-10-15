/**
 * 获取手机信息
 */
package nf.framework.core.util.android;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Pattern;

import nf.framework.core.base64.Md5Encrypt;
import nf.framework.core.exception.LogUtil;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Parcelable;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.WindowManager;

/**
 * 手机信息获取
 * 
 * @author Administrator 常用字段，数据获取
 */
public class PhoneInfoUtils {

	/**
	 * 通过包名获取应用程序的名称。
	 * @param context
	 *            Context对象。
	 * @param packageName
	 *            包名。
	 * @return 返回包名所对应的应用程序的名称。
	 */
	public static String getProgramNameByPackageName(Context context,
			String packageName) {
		PackageManager pm = context.getPackageManager();
		String name = null;
		try {
			name = pm.getApplicationLabel(
					pm.getApplicationInfo(packageName,
							PackageManager.GET_META_DATA)).toString();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return name;
	}

	/**
	 * 获取当前应用的版本号
	 * versionName
	 * @param context
	 * @return
	 */
	public static String getAppVersionNum(Context context) {

		String currentversion = null;
		try {
			currentversion = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}// 获取本地版本号

		return currentversion;
	}

	/**
	 * 获取应用的的代号
	 * 
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {

		int currentversionCode = 1;
		try {
			currentversionCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}// 获取本地版本号

		return currentversionCode;
	}

	/**
	 * 获取手机的电话号码
	 * 
	 * @return
	 */
	public static String phoneNum(Context context) {
		// 获取手机号、手机串号信息
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = tm.getDeviceId();
		String phonenum = null;
		if (imei != null) {
			phonenum = tm.getLine1Number();
		}
		return phonenum;
	}

	/**
	 * 获取手机卡的序列号
	 * 
	 * @return
	 */
	public static String getImsi(Context context) {

		// 获取手机号、手机串号信息
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imsi = tm.getSimSerialNumber();

		return imsi;
	}

	/**
	 * 获取系统的版本号
	 * 
	 * @param 例如
	 *            ：2.2.2
	 * @return
	 */
	public static  String getOSVersionNum() {

		String currentversion = android.os.Build.VERSION.RELEASE;

		return currentversion;
	}

	/**
	 * 获取android操作系统版本
	 * 
	 * @return
	 */
	public static int getAndroidSDKVersion() {
		int version = 1;
		try {
			version = android.os.Build.VERSION.SDK_INT;
		} catch (NumberFormatException e) {
			e.getStackTrace();
		}
		return version;
	}

	/**
	 * 手机系统的相关信息
	 */
	public static void PhoneInfo() {

		String phoneInfo = "Product: " + android.os.Build.PRODUCT;
		phoneInfo += ", CPU_ABI: " + android.os.Build.CPU_ABI;
		phoneInfo += ", TAGS: " + android.os.Build.TAGS;
		phoneInfo += ", VERSION_CODES.BASE: "
				+ android.os.Build.VERSION_CODES.BASE;
		phoneInfo += ", MODEL: " + android.os.Build.MODEL;
		phoneInfo += ", SDK: " + android.os.Build.VERSION.SDK;
		phoneInfo += ", VERSION.RELEASE: " + android.os.Build.VERSION.RELEASE;
		phoneInfo += ", DEVICE: " + android.os.Build.DEVICE;
		phoneInfo += ", DISPLAY: " + android.os.Build.DISPLAY;
		phoneInfo += ", BRAND: " + android.os.Build.BRAND;
		phoneInfo += ", BOARD: " + android.os.Build.BOARD;
		phoneInfo += ", FINGERPRINT: " + android.os.Build.FINGERPRINT;
		phoneInfo += ", ID: " + android.os.Build.ID;
		phoneInfo += ", MANUFACTURER: " + android.os.Build.MANUFACTURER;
		phoneInfo += ", USER: " + android.os.Build.USER;

		Log.e("手机信息：：", phoneInfo);

	}

	 //取得用户及设备标识
    //标识设备 android=md5(imei+mac+manufacturer+model)；ios=openUDID（Unique Device IDentifier）】
    /** 由于在2013.12.19日新闻将行为日志的格式升级为3.0.1在2013.12.25日对行为日志设备id进行了调整，如果取md5之后是16位的不变，不低于32位的，则取中间的16位，对于目前的用户量完全满足而且可以节约存储*/
    public static String getDeviceId(Context context) {
        String str = String.format("%s_%s_%s_%s", 
    		getIMEI(context), getMacAddress(context), android.os.Build.MANUFACTURER, 
    		android.os.Build.MODEL);
        
        /**
         * MD5的全称是 Message-Digest Algorithm 5 (信息摘要算法)
         * 通过手工就可以实现32位到16位之间的转换，只需要去掉32位密码格式的前八位以及最后八位，经过这样删减位数即可得到16位的加密格式。
         * 		即：21232f297a57a5a743894a0e4a801fc3 -》7a57a5a743894a0e
         * 同样也会遇到40位的MD5，40位的计算公式
         * 40位MD5 = 16 位MD5 + 《32位MD5后8位》 + 《32位MD5后16位》
         * 		7a57a5a743894a0e4a801fc343894a0e4a801fc3
         */
        str = Md5Encrypt.md5(str); //取中间16位比较节省空间        
        if(str.length() >= 32) {
        	str = str.substring(8, 24);
        }
        return str;
    }
	
	/**
	 * 获取手机串号
	 * 
	 * @return
	 */
	public static String getIMEI(Context context) {
		String deviceId = null;
		try {
			// 获取手机号、手机串号信息 当获取不到设备号时，系统会提供一个自动的deviceId
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			deviceId = tm.getDeviceId();
		} catch (Exception e) {
			e.getStackTrace();
			deviceId = "999999999999999";
		}
		return deviceId;
	}
	
	public static String getMacAddress(Context context){
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);       
        WifiInfo info = wifi.getConnectionInfo();
        if(info != null){
            return info.getMacAddress();
        }
        else{
        	return null;
        }
    }
	/***
	 * 获取mac地址，只有在开启wifi开关的情况下才能获取到
	 * @return
	 */
	public String getMac() {
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return macSerial;
	}

	/***
	 * 获取cpu信息 例 ARMv7 Processor rev 0 (v7l)
	 * @return
	 */
	public String getCPU() {
		String cpuSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);

			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					cpuSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return cpuSerial;
	}

	/***
	 * 获得系统当前时间 毫秒级
	 */
	public long getSysTimeByMillis() {
		long millis = System.currentTimeMillis();
		return millis;
	}

	/**
	 * 获得制定时间毫秒级
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public long getMillisTime(String date, String time) {

		String[] dateStrs = date.split(":");
		String[] timeStrs = time.split(":");
		Calendar cal = new GregorianCalendar();
		cal.set(Integer.valueOf(dateStrs[0]), Integer.valueOf(dateStrs[1]) - 1,
				Integer.valueOf(dateStrs[2]), Integer.valueOf(timeStrs[0]),
				Integer.valueOf(timeStrs[1]), Integer.valueOf(timeStrs[2]));

		long CurrentMillis = 0;
		CurrentMillis = cal.getTime().getTime();

		return CurrentMillis;
	}

	/**
	 * 当前时间获取日期 time 例如： System.currentTimeMillis() 格式："yyyy:MM:dd kk:mm:ss"
	 * return 格式为24小时制 2012:03:23
	 */
	public String getDateByFormat() {
		long millis = getSysTimeByMillis();
		String datetime = DateFormat.format("yyyy:MM:dd", millis).toString();
		return datetime;
	}

	/**
	 * 当前时间获取时间 time 例如： System.currentTimeMillis() 格式："kk:mm" return 格式为24小时制
	 * 12:08
	 */
	public String getHourByFormat() {
		long millis = getSysTimeByMillis();
		String datetime = DateFormat.format("kk:mm", millis).toString();
		return datetime;
	}

	/**
	 * 当前时间获取日期 time 例如： System.currentTimeMillis() 格式："yyyy:MM:dd kk:mm:ss"
	 * return 格式为24小时制 2012:03:23
	 */
	public String getDateByFormat(long millTime) {
		String datetime = DateFormat.format("yyyy:MM:dd", millTime).toString();
		return datetime;
	}

	/**
	 * 判断是否存在快捷方式
	 * 
	 * @param context
	 * @return
	 */
	public static boolean hasShortCut(Context context,String appName) {
		String url = "";
		if (getAndroidSDKVersion() < 8) {
			url = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			url = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		ContentResolver resolver = context.getContentResolver();
		Cursor cursor = resolver.query(Uri.parse(url), new String[] { "title",
				"iconResource" }, "title=?", new String[] { appName }, null);
		if (cursor != null && cursor.moveToFirst()) {
			cursor.close();
			return true;
		}
		return false;
	}

	/**
	 * 判断桌面是否已添加快捷方式
	 * 
	 * @param cx
	 * @param titleName
	 *            快捷方式名称
	 * @return
	 */
	public static boolean hasShortcut(Context cx, String appName) {
		boolean result = false;
		// 获取当前应用名称
		String title = appName;
		// try {
		// final PackageManager pm = cx.getPackageManager();
		// title = pm.getApplicationLabel(
		// pm.getApplicationInfo(cx.getPackageName(),
		// PackageManager.GET_META_DATA)).toString();
		// } catch (Exception e) {
		// }
		final String uriStr;
		if (android.os.Build.VERSION.SDK_INT < 8) {
			uriStr = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		final Uri CONTENT_URI = Uri.parse(uriStr);
		final Cursor c = cx.getContentResolver().query(CONTENT_URI, null,
				"title=?", new String[] { title }, null);
		if (c != null && c.getCount() > 0) {
			result = true;
		}
		return result;
	}

	/* 提示用户是否创建快捷方式Dialog */
	public static void showDialog(final Context context, final int icon,
			final String appName, final Class<?> cls) {
		// 定义一个AlertDialog.builder方法,用于退出程序提示
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("是否创建桌面快捷方式");
		// 设置标题为提示
		builder.setTitle("提示");
		// 设置一个取消按钮，设置监听，监听事件为如果点击了，就将这个Dialog关闭
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		// 设置一个确认按钮，给其设置监听，点击就关闭这个Activity，Dialog也关闭
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent addShortcut = new Intent(
								"com.android.launcher.action.INSTALL_SHORTCUT");
						Parcelable ShortcutIcon = Intent.ShortcutIconResource
								.fromContext(context, icon);
						addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME,
								appName);
						addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
								new Intent().setClass(context, cls));
						addShortcut.putExtra(
								Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
								ShortcutIcon);
						context.sendBroadcast(addShortcut);
					}
				});
		builder.create().show();
	}

	/**
	 * 添加快捷方式
	 * 
	 * @param icon
	 * @param appName
	 * @param cls
	 */
	public void addShortcut(Context context,int icon, String appName, Class<?> cls) {

		Intent intent = new Intent();
		intent.setClass(context, cls);
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.LAUNCHER");

		Intent addShortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		Parcelable ShortcutIcon = Intent.ShortcutIconResource.fromContext(
				context, icon);

		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);
		addShortcut.putExtra("duplicate", false); // //不允许重复创建
		addShortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, ShortcutIcon);
		context.sendBroadcast(addShortcut);
	}

	/****
	 * 删除程序的快捷方式
	 * 
	 * @param context
	 * @param appName
	 */
	public static void delShortcut(Context context, String appName) {
		Intent shortcut = new Intent(
				"com.android.launcher.action.UNINSTALL_SHORTCUT");
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
		// 指定当前的Activity为快捷方式启动的对象: 如 com.everest.video.VideoPlayer
		// 注意: ComponentName的第二个参数必须是完整的类名（包名+类名），否则无法删除快捷方式
		String appClass = context.getPackageName() + ".ActivityName";
		ComponentName comp = new ComponentName(context.getPackageName(),
				appClass);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new Intent(
				Intent.ACTION_MAIN).setComponent(comp));
		context.sendBroadcast(shortcut);
	}

	/**
	 * 判断一个应用是否被安装到本地
	 * 
	 * @param apkName
	 * @return
	 */
	public static boolean isInstall(Context context,String apkName) {

		PackageInfo packageInfo;
		boolean isInstall = false;
		try {
			packageInfo = context.getPackageManager()
					.getPackageInfo(apkName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if (packageInfo != null) {
			isInstall = true;
		}
		return isInstall;
	}

	/**
	 * 
	 * 获取手机CPU是单核还是多核 Gets the number of cores available in this device, across
	 * all processors. Requires: Ability to peruse the filesystem at
	 * "/sys/devices/system/cpu"
	 * 
	 * @return The number of cores, or 1 if failed to get result
	 */
	public int getNumCores() {
		// Private Class to display only CPU devices in the directory listing
		class CpuFilter implements FileFilter {
			@Override
			public boolean accept(File pathname) {
				// Check if filename is "cpu", followed by a single digit number
				if (Pattern.matches("cpu[0-9]", pathname.getName())) {
					return true;
				}
				return false;
			}
		}

		try {
			// Get directory containing CPU info
			File dir = new File("/sys/devices/system/cpu/");
			// Filter to only list the devices we care about
			File[] files = dir.listFiles(new CpuFilter());
			// Return the number of cores (virtual CPU devices)
			return files.length;
		} catch (Exception e) {
			// Default to return 1 core
			return 1;
		}
	}

	/**
	 * 当前时间获取时间 time 例如： System.currentTimeMillis() 格式："kk:mm" return 格式为24小时制
	 * 12:08:23
	 */
	public String getSecondsByFormat() {
		long millis = getSysTimeByMillis();
		String datetime = DateFormat.format("kk:mm:ss", millis).toString();
		return datetime;
	}
	
	/**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */ 
    public static boolean isOpenGPS(Context context) { 
        LocationManager locationManager  
                                 = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE); 
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快） 
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); 
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位） 
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); 
        if (gps || network) { 
            return true; 
        } 
        return false; 
    }
    /**
     * 程序是否在前台运行
     * 
     * @return
     */
    public static boolean isAppOnForeground(Context context) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(
                    Context.ACTIVITY_SERVICE);
            String packageName = context.getPackageName();
            List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
            if (appProcesses == null) {
                return false;
            }
            for (RunningAppProcessInfo appProcess : appProcesses) {
                // The name of the process that this object is associated with.
                if (appProcess.processName.equals(packageName)
                        && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e("", "isAppOnForeground exception!"+ e);
        }
        return false;
    }

    /**
     * 屏幕宽度
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity){
    	WindowManager wm = (WindowManager)activity.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();//屏幕宽度
    }
}
