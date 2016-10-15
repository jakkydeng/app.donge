package com.sina.sinavideo.sdk.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.video_playersdkv2.R;

import android.content.Context;

/**
 * 配置管理器，管理raw/config.properties的配置文件信息
 * 
 * @author alexsun
 * 
 */
public class VDSDKConfig {

    private Properties mProperties = null;
    private final static String TAG = "VDSDKConfig";

    private final static String CONFIG_RETRY_TIME = "retrytime";
    private final static String CONFIG_LOG_HEARTBEAT_TIMER = "logHeartBeatTime";
    private final static String CONFIG_LOG_PRETIME = "logPreTime";
    private final static String CONFIG_LOG_PRETIME_PERCENT = "logPreTimePercent";
    private final static String CONFIG_LOG_CLASSPATH = "logClassPath";
    private final static String CONFIG_ADV_CLASSPATH = "advClassPath";
    private final static String CONFIG_ADV_UNITID = "advUnitID";
    private final static String CONFIG_MONITOR_OPEN = "monitorOpen";
    private final static String CONFIG_MONITOR_QUEUE_COUNT = "monitorQueueCount";
    private final static String CONFIG_MONITOR_CLASSPATH = "monitorClassPath";

    public VDSDKConfig() {
    }

    public synchronized void init(Context context) {
        if (context == null || mProperties != null) {
            VDLog.e(TAG, "VDSDKConfig's init,context is null");
            return;
        }
        mProperties = new Properties();
        try {
            InputStream is = context.getResources().openRawResource(R.raw.config);
            mProperties.load(is);
        } catch (IOException ex) {
            LogS.e(TAG, ex.getMessage());
        }
    }

    private static class VDSDKConfigINSTANCE {

        private static VDSDKConfig instance = new VDSDKConfig();
    }

    public static VDSDKConfig getInstance() {
        return VDSDKConfigINSTANCE.instance;
    }

    public static VDSDKConfig getInstance(Context context) {
        VDSDKConfig config = VDSDKConfigINSTANCE.instance;
        config.init(context);
        return config;
    }

    public int getRetryTime() {
        return Integer.valueOf(mProperties.getProperty(CONFIG_RETRY_TIME));
    }

    public int getLogHeartBeatTimer() {
        return Integer.valueOf(mProperties.getProperty(CONFIG_LOG_HEARTBEAT_TIMER));
    }

    public int getLogPreTime() {
        return Integer.valueOf(mProperties.getProperty(CONFIG_LOG_PRETIME));
    }

    public int getlogPreTimePercent() {
        return Integer.valueOf(mProperties.getProperty(CONFIG_LOG_PRETIME_PERCENT));
    }

    public String getAdvClassPath() {
        return mProperties.getProperty(CONFIG_ADV_CLASSPATH);
    }

    public String getLogClassPath() {
        String ret = null;
        try {
            ret = mProperties.getProperty(CONFIG_LOG_CLASSPATH);
        } catch (Exception ex) {
            VDLog.e(TAG, ex.getMessage());
        }
        if (ret == null) {
            ret = "com.sina.sinavideo.sdk.log_demo.VDDemoSDKLog";
        }
        return ret;
    }

    public String[] getAdvUnitID() {
        String unitID = mProperties.getProperty(CONFIG_ADV_UNITID);
        return unitID.split("\\|");
    }

    public int getMonitorOpen() {
        return Integer.valueOf(mProperties.getProperty(CONFIG_MONITOR_OPEN));
    }

    public int getMonitorQueueCount() {
        return Integer.valueOf(mProperties.getProperty(CONFIG_MONITOR_QUEUE_COUNT));
    }

    public String getMonitorClassPath() {
        String ret = null;
        try {
            ret = mProperties.getProperty(CONFIG_MONITOR_CLASSPATH);
        } catch (Exception ex) {
            VDLog.e(TAG, ex.getMessage());
        }
        if (ret == null) {
            ret = "com.sina.sinavideo.sdk.monitor.VDDefaultMonitorHandler";
        }
        return ret;
    }
}
