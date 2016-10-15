
package com.sina.sinavideo.sdk.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dalvik.system.PathClassLoader;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

public class VDUtility {

    public enum eAndroidOS {
        UNKNOWN, MIUI, EmotionUI, Flyme, NubiaUI, Nokia_X, ColorOS, HTC, ZTE, FuntouchOS,
    };

    public static final String FORMAT_ALL_DATE = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String FORMAT_TIME = "HH:mm:ss";
    public static final String FORMAT_DATE = "yyyy-MM-dd";
    public static final String FORMAT_SIMPLE_DATE = "yyyyMMdd";

    private final static SimpleDateFormat sFORMAT = new SimpleDateFormat(FORMAT_ALL_DATE, Locale.CHINA);

    private static String mSystemProperty = getSystemProperty();

    private final static String TAG = "VDUtility";

    public static String getAppVersion(Context context) {
        return "";
    }

    public static String getAppName(Context context) {
        return "";
    }

    public static String getDocumentPath(Context context) {
        return context.getFilesDir().getAbsolutePath();
    }

    public static String getSDCardDataPath(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static boolean getSDCardRemainCanWrite(Context context, long remainSize) {
        String path = getSDCardDataPath(context);
        StatFs statFS = new StatFs(path);
        long blockSize = 0L;
        if (getSDKInt() >= 18)
        {
            blockSize = statFS.getBlockCount();
        } else {
            blockSize = statFS.getBlockSize();
        }
        long availableBlock = 0L;
        if (getSDKInt() >= 18)
        {
            availableBlock = statFS.getAvailableBlocks();
        } else {
            availableBlock = statFS.getAvailableBlocks();
        }
        long size = blockSize * availableBlock;
        if (size > remainSize) {
            return true;
        }

        return false;
    }

    public static String getSDKVersion(Context context) {
        PackageManager pm = context.getPackageManager();
        String pkgName = context.getPackageName();
        PackageInfo pkgInfo = null;
        String ret = "";
        try {
            pkgInfo = pm.getPackageInfo(pkgName, PackageManager.GET_CONFIGURATIONS);
            ret = pkgInfo.versionName;
        } catch (NameNotFoundException ex) {

        }
        return ret;
    }

    public static String generatePlayTime(long time) {
        if (time % 1000 >= 500) {
            time += 1000;
        }
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format(Locale.CHINA, "%02d:%02d:%02d", hours, minutes, seconds) : String.format(
                Locale.CHINA, "%02d:%02d", minutes, seconds);
    }

    /**
     * ��ㄥ��灏���瑰�����杞界被
     * 
     * @param context
     * @param path
     * @return
     */
    public static Object loadClass(Context context, String path) {
        try {
            String dexPath = context.getApplicationInfo().sourceDir;
            PathClassLoader pathClassLoader = new PathClassLoader(dexPath, context.getClassLoader());
            Class<?> c = Class.forName(path, true, pathClassLoader);
            Object ret = c.newInstance();
            return ret;
        } catch (InstantiationException ex1) {
            ex1.printStackTrace();
        } catch (IllegalAccessException ex2) {
            ex2.printStackTrace();
        } catch (ClassNotFoundException ex3) {
            ex3.printStackTrace();
        }

        return null;
    }

    public static String generateTime(long time, boolean isLong) {
        Date date = new Date(time);
        sFORMAT.applyPattern(isLong ? FORMAT_ALL_DATE : FORMAT_TIME);
        String LgTime = null;
        try {
            LgTime = sFORMAT.format(date);
        } catch (Exception e) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(isLong ? FORMAT_ALL_DATE : FORMAT_TIME, Locale.CHINA);
                LgTime = format.format(new Date());
                e.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
                LgTime = "";
            }
        }
        return LgTime;
    }

    private static TelephonyManager getTelephonyManager() {
        return (TelephonyManager) VDApplication.getInstance().getContext().getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static boolean isEmpty(String str) {
        return null == str || "".equals(str) || "NULL".equals(str.toUpperCase(Locale.CHINA));
    }
    
    public static boolean isOnlyMobileType(Context context) {
        State wifiState = null;
        State mobileState = null;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        try {
            networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (networkInfo != null) {
            wifiState = networkInfo.getState();
        }
        try {
            networkInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (networkInfo != null) {
            mobileState = networkInfo.getState();
        }
        VDLog.d("zhang", "onReceive -- wifiState = " + wifiState + " -- mobileState = " + mobileState);
        if (wifiState != null && mobileState != null && State.CONNECTED != wifiState && State.CONNECTED == mobileState) {
            // �����虹��缁�杩���ユ�����
            VDLog.d("zhang", "onReceive -- �����虹��缁�杩���ユ�����");
            return true;
        }
        return false;
    }

    /**
     * md5
     * 
     * @param str
     * @return
     */
    public static String md5(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
            byte[] byteArray = messageDigest.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    sb.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    sb.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return str;
    }

    /**
     * Get phone IMEI. READ_PHONE_STATE permission must be declared in manifest
     * file.
     * 
     * @param _mContext
     * @return
     */
    public static String getIMEI() {
        String IMEI = getTelephonyManager().getDeviceId();

        if (isEmpty(IMEI) || "0".equals(IMEI)) {
            WifiManager wifiManager = (WifiManager) VDApplication.getInstance().getContext()
                    .getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (null != wifiInfo) {
                String macAddress = wifiInfo.getMacAddress();
                if (!isEmpty(macAddress)) {
                    IMEI = md5(macAddress);
                }
            }
        }

        return IMEI;
    }

    public static String getOSVersionInfo() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * Get mobile model, like GT-i9500 etc.
     * 
     * @return
     */
    public static String getMobileModel() {
        return android.os.Build.MODEL;
    }

    public static String getSystemProperty(String propName) {
        String line = "";
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

    private static String getSystemProperty() {
        String line = "";
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop");
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 2048);
            String ret = input.readLine();
            while (ret != null) {
                line += ret + "\n";
                ret = input.readLine();
            }
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop", ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }

    public static eAndroidOS filterOS() {
        String prop = mSystemProperty;
        if (prop.contains("miui")) {
            return eAndroidOS.MIUI;
        } else if (prop.contains("EmotionUI")) {
            return eAndroidOS.EmotionUI;
        } else if (prop.contains("flyme")) {
            return eAndroidOS.Flyme;
        } else if (prop.contains("[ro.build.user]: [nubia]")) {
            return eAndroidOS.NubiaUI;
        } else if (prop.contains("Nokia_X")) {
            return eAndroidOS.Nokia_X;
        } else if (prop.contains("[ro.build.soft.version]: [A.")) {
            return eAndroidOS.ColorOS;
        } else if (prop.contains("ro.htc.")) {
            return eAndroidOS.HTC;
        } else if (prop.contains("[ro.build.user]: [zte")) {
            return eAndroidOS.ZTE;
        } else if (prop.contains("[ro.product.brand]: [vivo")) {
            return eAndroidOS.FuntouchOS;
        }
        return eAndroidOS.UNKNOWN;
    }

    public static String getBrand() {
        return Build.BRAND;
    }

    public static String getModel() {
        return Build.MODEL;
    }

    public static String getCPU() {
        return Build.CPU_ABI;
    }

    public static String getSDKRelease() {
        return Build.VERSION.RELEASE;
    }

    public static int getSDKInt() {
        return Build.VERSION.SDK_INT;
    }

    public static eAndroidOS getOS() {
        return filterOS();
    }

    /**
     * 寰���板��骞�瀹介��
     * 
     * @return {瀹藉害,楂�搴�}
     */
    @SuppressLint({
            "NewAPI", "Deprecated"
    })
    public static int[] getScreenSize() {
        int[] size = {
                0, 0
        };
        WindowManager wm = (WindowManager) VDApplication.getInstance().getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        if (getSDKInt() >= 13) {
            Point point = new Point();
            wm.getDefaultDisplay().getSize(point);
            size[0] = point.x;
            size[1] = point.y;
        } else {
            DisplayMetrics metrics = new DisplayMetrics();
            wm.getDefaultDisplay().getMetrics(metrics);
            size[0] = metrics.widthPixels;
            size[1] = metrics.heightPixels;
        }
        return size;
    }

    public static boolean isMeizu() {
        String brand = getBrand();
        eAndroidOS os = getOS();
        if (brand.equals("Meizu") && os == eAndroidOS.Flyme) {
            return true;
        }

        return false;
    }

    public static boolean isSamsungNoteII() {
        String brand = getBrand();
        String model = getModel();

        if (brand.equals("samsung") && model.equals("GT-N7100")) {
            return true;
        }

        return false;
    }

    public static boolean isXiaomi3() {
        String brand = getBrand();
        String model = getModel();

        if (brand.equals("Xiaomi") && model.equals("MI 3")) {
            return true;
        }

        return false;
    }

    public static boolean isLocalUrl(String url) {
        if (url == null) {
            return false;
        }
        // File file = Environment.getExternalStorageDirectory();
        // if (VDUtility.isSdcardReady() && file != null &&
        // url.startsWith(file.getAbsolutePath())) {
        // return true;
        // }
        if (url.startsWith("http"))
            return false;
        // return false;
        return true;
    }

    public static boolean isSdcardReady() {
        try {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        } catch (Exception e) {
            VDLog.e(TAG, "isSdcardReady had exception!", e);
            return false;
        }
    }

}
