
package com.sina.sinavideo.sdk.monitor;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.sina.sinavideo.sdk.utils.VDApplication;
import com.sina.sinavideo.sdk.utils.VDMobileUtil;
import com.sina.sinavideo.sdk.utils.VDMonitorManager;
import com.sina.sinavideo.sdk.utils.VDUtility;

public class VDMonitorData {

    public final static String DATE_TIME_KEY = "DateTime";
    public final static String SESSION_ID_KEY = "SessionID";
    public final static String DEVICE_ID_KEY = "DeviceID";
    public final static String DEVICEBRAND_KEY = "DeviceBrand";
    public final static String DEVICEMODEL_KEY = "DeviceModel";
    public final static String PLATFORM_KEY = "Platform";
    public final static String PLATFORMVERSION_KEY = "PlatformVersion";
    public final static String APP_KEY = "App";
    public final static String APP_VESRION_KEY = "AppVesion";
    public final static String SDK_VERSION_KEY = "SDKVersion";
    public final static String VIDEO_ID_KEY = "Videoid";
    public final static String VIDEO_TYPE_KEY = "VideoType";
    public final static String VIDEO_STREAMTYPE_KEY = "VideoStreamType";
    public final static String STATUS_KEY = "Status";
    public final static String VERSION_KEY = "Version";

    // 网络部分
    public final static String VMSID_KEY = "VMSID";
    public final static String FROMURL_KEY = "FromUrl";
    public final static String TOURL_KEY = "ToUrl";
    public final static String NETTYPE_KEY = "NetType";
    public final static String BANDWIDTHTYPE_KEY = "BandwidthType";
    public final static String TICKS_KEY = "Ticks";
    public final static String ERRTYPE_KEY = "Errtype";
    // 播放器部分

    public long mDateTime = 0;
    public String mSessionID = "";
    public String mDeviceID = "";
    public String mDeviceBrand = "";
    public String mDeviceModel = "";
    public int mPlatform = 0;
    public String mPlatformVersion = "";
    public String mApp = "";
    public String mAppVesion = "";
    public String mSDKVersion = "";
    public String mVideoid = "";
    public int mVideoType = 0;
    public int mVideoStreamType = 0;
    public int mStatus = 0;
    public int mVersion = 0;

    // 网络部分
    public String mFromUrl = "";
    public String mToUrl = "";
    public int mNetType = 0;
    public int mBandwidthType = 0;
    public int mTicks = 0;
    public int mErrtype = 0;
    public String mVmsID = "";

    // 播放器部分

    public VDMonitorData() {
        super();
        init();
    }

    public void set(String key, Object value) {
        if (value == null) {
            return;
        }
        if (key.equals(VIDEO_ID_KEY)) {
            mVideoid = new String(String.valueOf(value));
        } else if (key.equals(VIDEO_TYPE_KEY)) {
            mVideoType = Integer.valueOf(String.valueOf(value));
        } else if (key.equals(VIDEO_STREAMTYPE_KEY)) {
            mVideoStreamType = Integer.valueOf(String.valueOf(value));
        } else if (key.equals(STATUS_KEY)) {
            mStatus = Integer.valueOf(String.valueOf(value));
        } else if (key.equals(VMSID_KEY)) {
            mVmsID = new String(String.valueOf(value));
        } else if (key.equals(FROMURL_KEY)) {
            mFromUrl = new String(String.valueOf(value));
        } else if (key.equals(TOURL_KEY)) {
            mToUrl = new String(String.valueOf(value));
        } else if (key.equals(ERRTYPE_KEY)) {
            mErrtype = Integer.valueOf(String.valueOf(value));
        }
    }

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        try {
            obj.put(DATE_TIME_KEY, mDateTime);
            obj.put(SESSION_ID_KEY, mSessionID);
            obj.put(DEVICE_ID_KEY, mDeviceID);
            obj.put(DEVICEBRAND_KEY, mDeviceBrand);
            obj.put(DEVICEMODEL_KEY, mDeviceModel);
            obj.put(PLATFORM_KEY, mPlatform);
            obj.put(PLATFORMVERSION_KEY, mPlatformVersion);
            obj.put(APP_KEY, mApp);
            obj.put(APP_VESRION_KEY, mAppVesion);
            obj.put(SDK_VERSION_KEY, mSDKVersion);
            obj.put(VIDEO_ID_KEY, mVideoid);
            obj.put(VIDEO_TYPE_KEY, mVideoType);
            obj.put(VIDEO_STREAMTYPE_KEY, mVideoStreamType);
            obj.put(STATUS_KEY, mStatus);
            obj.put(VERSION_KEY, mVersion);

            if (mStatus >= 100100 && mStatus < 100199) {
                if (mStatus != VDMonitorManager.STATUS_VMS_BEGIN || mStatus != VDMonitorManager.STATUS_VMS_END) {
                    obj.put(FROMURL_KEY, mFromUrl);
                }
                if (mStatus == VDMonitorManager.STATUS_URLPARSER_BEGIN || mStatus == VDMonitorManager.STATUS_URLPARSER_END
                        || mStatus == VDMonitorManager.STATUS_URLPARSER_SKIP) {
                    obj.put(TOURL_KEY, mToUrl);
                }
                if (mStatus == VDMonitorManager.STATUS_VMS_BEGIN || mStatus == VDMonitorManager.STATUS_VMS_END) {
                    obj.put(VMSID_KEY, mVmsID);
                }
                obj.put(NETTYPE_KEY, mNetType);
                obj.put(BANDWIDTHTYPE_KEY, mBandwidthType);
                obj.put(TICKS_KEY, mTicks);
                obj.put(ERRTYPE_KEY, mErrtype);
            }
        } catch (JSONException ex) {

        }
        return obj;
    }

    public void ticker() {
        mTicks++;
    }

    public void clean() {
        mDateTime = 0;
        mSessionID = "";
        mDeviceID = "";
        mDeviceBrand = "";
        mDeviceModel = "";
        mPlatform = 0;
        mPlatformVersion = "";
        mApp = "";
        mAppVesion = "";
        mSDKVersion = "";
        mVideoid = "";
        mVideoType = 0;
        mVideoStreamType = 0;
        mStatus = 0;
        mVersion = 0;
        mTicks = 0;
    }

    public void init() {
        clean();
        Context context = VDApplication.getInstance().getContext();
        mApp = context.getPackageName();
        mAppVesion = VDApplication.getInstance().getAPPVersion();
        mDeviceID = VDUtility.md5("android " + VDUtility.getIMEI());
        // 对于session来说，类一次实例化，只会有一个赋值
        mSessionID = VDUtility.md5(String.valueOf((int) (Math.random() * 10000)));
        mSDKVersion = VDUtility.getSDKVersion(context);
        mPlatform = 0;
        mDeviceBrand = VDUtility.getBrand();
        mDeviceModel = VDUtility.getModel();
        mPlatform = 0;
        mPlatformVersion = String.valueOf(VDUtility.getSDKInt());
        // 网络部分
        if (VDMobileUtil.getMobileType(context) == VDMobileUtil.MOBILE_2G) {
            mBandwidthType = 0;
        } else if (VDMobileUtil.getMobileType(context) == VDMobileUtil.MOBILE_3G) {
            mBandwidthType = 1;
        } else if (VDMobileUtil.getMobileType(context) == VDMobileUtil.MOBILE_4G) {
            mBandwidthType = 2;
        } else if (VDMobileUtil.getMobileType(context) == VDMobileUtil.WIFI) {
            mBandwidthType = 3;
        }
        if (VDMobileUtil.getProvider(context) == VDMobileUtil.CHINA_MOBILE)
        {
            mNetType = 0;
        } else if (VDMobileUtil.getProvider(context) == VDMobileUtil.CHINA_TELECOM) {
            mNetType = 1;
        } else if (VDMobileUtil.getProvider(context) == VDMobileUtil.CHINA_TELECOM) {
            mNetType = 2;
        } else if (VDMobileUtil.getMobileType(context) == VDMobileUtil.WIFI) {
            mNetType = 4;
        } else {
            mNetType = 5;
        }
    }
}
