package nf.framework.core.util.android;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * @author niufei
 * 
 * 
 *         判断联网
 * 
 */
public class CheckInternet {
	
	 /**
	  * 当前网络状态枚举
	  * 
	  * */
	  public enum NetAuthority {
		  unNetConnect,//无网连接
		  WifiConnect,//wifi网络
		  Net2GComnect,//2G网络
		  Net3GComnect; //3G网络
	  }

	/**
	 * 是否飞行模式
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isAirplaneModeOn(Context mcontext) {
		return Settings.System.getInt(mcontext.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, 0) != 0;
	}

	/**
	 * 检测是否已经连接网络。
	 * 
	 * @param context
	 * @return 当且仅当连上网络时返回true,否则返回false。
	 */
	public static boolean checkInternet(Context mcontext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mcontext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		}
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		return (info != null) && info.isAvailable();
	}

	/**
	 * 判断当前网络状态
	 */
	public static NetAuthority JudgeCurrentNetState(Context mcontext) {
		ConnectivityManager connectivityManager = (ConnectivityManager) mcontext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			TelephonyManager mTelephony = (TelephonyManager) mcontext
					.getSystemService(Context.TELEPHONY_SERVICE);
			int netType = info.getType();
			int netSubtype = info.getSubtype();
			if (netType == ConnectivityManager.TYPE_WIFI) {// WIFI
				return NetAuthority.WifiConnect;
			} else if (netType == ConnectivityManager.TYPE_MOBILE
					&& netSubtype == TelephonyManager.NETWORK_TYPE_UMTS
					&& !mTelephony.isNetworkRoaming()) {// 3G
				return NetAuthority.Net3GComnect;
			} else {
				return NetAuthority.Net2GComnect;
			}
		}
		return NetAuthority.unNetConnect;
	}

}
