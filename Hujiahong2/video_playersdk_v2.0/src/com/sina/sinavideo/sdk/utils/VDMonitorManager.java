package com.sina.sinavideo.sdk.utils;

import android.content.Context;

/**
 * 插监控桩的地方，是否开启，由VDSDKConfig来决定<br/>
 * 1.加入数据:VDMonitor.getInstance().setXXX()<br/>
 * 2.插桩:VDMonitor.getInstance().pile()
 * 
 * @author sunxiao1@staff.sina.com.cn
 */
public class VDMonitorManager {

    // ---状态码部分--开始
    public static final int STATUS_INIT_PLAYLIST = 100001;
    public static final int STATUS_PLAY = 100002;
    public static final int STATUS_CHECK_IFPLAY = 100003;
    public static final int STATUS_M3U8_START = 100101;
    public static final int STATUS_M3U8_END = 100102;
    public static final int STATUS_URLPARSER_BEGIN = 100103;
    public static final int STATUS_URLPARSER_SKIP = 100104;
    public static final int STATUS_URLPARSER_END = 100105;
    public static final int STATUS_VMS_BEGIN = 100106;
    public static final int STATUS_VMS_END = 100107;
    public static final int STATUS_ADV_BEGIN = 100108;
    public static final int STATUS_ADV_END = 100109;

    // ---状态码部分--结束

    public static interface VDMonitorHandler {

        public void start();

        public void stop();

        public void setStringPair(String key, String value);

        public void setIntPair(String key, int value);

        public void pile();

        public void setStatus(int status);
    }

    private VDMonitorHandler mMonitorHandler = null;

    public VDMonitorManager() {
        super();

        Context context = VDApplication.getInstance().getContext();
        String path = VDSDKConfig.getInstance().getMonitorClassPath();
        mMonitorHandler = (VDMonitorHandler) VDUtility.loadClass(context, path);
    }

    private static class VDMonitorINSTANCE {

        private static VDMonitorManager instance = new VDMonitorManager();
    }

    public static VDMonitorManager getInstance() {
        return VDMonitorINSTANCE.instance;
    }

    /**
     * 监控开始
     */
    public void watch() {
        mMonitorHandler.stop();
        mMonitorHandler.start();
    }

    /**
     * 插桩
     */
    public void pile(int status) {
        mMonitorHandler.pile();
        mMonitorHandler.setStatus(status);
    }

    /**
     * 赋值，字符串
     * 
     * @param key
     * @param value
     */
    public void setString(String key, String value) {
        mMonitorHandler.setStringPair(key, value);
    }

    /**
     * 赋值，整形
     * 
     * @param key
     * @param value
     */
    public void setInteger(String key, int value) {
        mMonitorHandler.setIntPair(key, value);
    }
}
