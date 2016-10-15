package com.sina.sinavideo.sdk.plugin.sina_adv;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.utils.VDMobileUtil;
import com.sina.sinavideo.sdk.utils.VDSDKConfig;
import com.sina.sinavideo.sdk.utils.VDSDKAdvManager.VDSDKReqAdvData;

/**
 * 广告接口的数据结构，跟随VDVideoInfo，一个片子一个广告结构
 * 
 * @author alexsun
 * 
 */
public class VDAdvRequestData extends VDSDKReqAdvData {

    private final static String TAG = "VDAdvRequestData";

    // ------------------构建VDVideoInfo的时候，必须填写
    public final static String ADUNIT_ID_KEY = "adunit_id";
    public final static String SIZE_KEY = "size";
    public final static String TARGETING_KEY = "targeting";
    // ------------------构建VDVideoInfo的时候，结束
    // ------------------构建VMS的时候，必须填写，这个在targeting里面
    public final static String MEDIA_TAGS_KEY = "media_tags";
    public final static String V_LENGTH_KEY = "v_length";
    // ------------------构建VMS的时候，结束
    // 下面的由sdk取得
    public final static String ROTATE_COUNT_KEY = "rotate_count";
    public final static String CLIENT_KEY = "client";
    public final static String DEVICE_PLATFORM_KEY = "device_platform";
    // public final static String PLATFROM_VERSION_KEY = "platfrom_version";
    public final static String DEVICE_TYPE_KEY = "device_type";
    public final static String CARRIER_KEY = "carrier";
    public final static String IP_KEY = "ip";
    public final static String DEVICE_ID_KEY = "device_id";
    /**
     * adunit_id:广告位ID
     */
    public List<String> mAdunitId = new ArrayList<String>();
    /**
     * size:广告位尺寸，格式为宽*高，如果获取不到则为unknown，例如：950*90, unknown
     */
    public List<String> mSize = new ArrayList<String>();
    /**
     * rotate_count:轮播数
     */
    public int mRotateCount = 0;
    /**
     * client:客户端名称，例如：sportapp
     */
    public String mClient = "";
    /**
     * device_platform:操作系统 0:unknown 1:ios 2:android
     */
    public String mDevicePlatform = "2";
    /**
     * platfrom_version:操作系统版本，例如：7.1, 4.4.3
     */
    public String mPlatfromVersion = "";
    /**
     * device_type:设备类型 0:unknown 1:phone 2:tablet
     */
    public String mDeviceType = "";
    /**
     * carrier:网络连接类型 0:unknown 1:WIFI 2:cellular network-2G 3:cellular
     * network-3G 4:cellular network-4G 5:cellular network-unknown generation
     */
    public String mCarrier = "";
    /**
     * ip:用户IP
     */
    public String mIp = "";
    /**
     * device_id:设备ID，规则为：ios6:mac ios7:idfa android:imei，MD5加密
     */
    public String mDeviceID = "";
    /**
     * targeting，每个业务都不太一样。
     */
    public Map<String, String> mTargeting = null;
    public Context mContext = null;

    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        JSONObject objTarget = new JSONObject();
        try {
            JSONArray jsonArr = new JSONArray();
            for(String value : mAdunitId)
            {
                jsonArr.put(value);
            }
            obj.put("adunit_id", jsonArr);
            jsonArr = new JSONArray();
            for(String value : mSize)
            {
                jsonArr.put(value);
            }
            obj.put("size", jsonArr);
            obj.put("rotate_count", mRotateCount);
            obj.put("client", mClient);
            obj.put("device_platform", mDevicePlatform);
            obj.put("platfrom_version", mPlatfromVersion);
            obj.put("device_type", mDeviceType);
            obj.put("carrier", mCarrier);
            obj.put("ip", mIp);
            obj.put("device_id", mDeviceID);
            if (mTargeting != null) {
                Iterator<Map.Entry<String, String>> iter = mTargeting.entrySet().iterator();
                while (iter.hasNext()) {
                    try {
                        Map.Entry<String, String> currValue = iter.next();
                        String key = currValue.getKey();
                        String value = currValue.getValue();
                        objTarget.put(key, value);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                obj.put("targeting", objTarget);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
        return obj;
    }

    private String getAppName() {
        // return mContext.getApplicationInfo().packageName;
        return "sportapp";
    }

    @SuppressWarnings("unchecked")
    @Override
    public void readFrom(VDVideoInfo info) {
        // TODO Auto-generated method stub
        if (info != null && info.mAdvReqData != null) {
            Map<String, Object> advMap = info.mAdvReqData;
            try {
                // 配置文件里面读取
                String[] adunitIDList = VDSDKConfig.getInstance().getAdvUnitID();
                if (adunitIDList != null) {
                    for (int i = 0; i < adunitIDList.length; i++) {
                        this.mAdunitId.add(adunitIDList[i]);
                    }
                }
                // 外部app填写的
                if (advMap.containsKey(SIZE_KEY)) {
                    try {
                        this.mSize = (List<String>) advMap.get(SIZE_KEY);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (advMap.containsKey(TARGETING_KEY)) {
                    try {
                        this.mTargeting = (Map<String, String>) advMap.get(TARGETING_KEY);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                // vms获取的
                if (mTargeting != null) {
                    // 这两个是从VMS中拿到的
                    if (advMap.containsKey(MEDIA_TAGS_KEY)) {
                        mTargeting.put(MEDIA_TAGS_KEY, (String) advMap.get(MEDIA_TAGS_KEY));
                    }
                    if (advMap.containsKey(V_LENGTH_KEY)) {
                        mTargeting.put(V_LENGTH_KEY, String.valueOf(advMap.get(V_LENGTH_KEY)));
                    }
                }
                // 自己填
                Random random = new Random();
                this.mRotateCount = random.nextInt(1000000);
                this.mClient = getAppName();
                this.mDevicePlatform = "2";
                this.mPlatfromVersion = getOSVer();
                // TODO 得到当前是pad还是phone，因为android的设备特殊性，统一作为phone处理了
                this.mDeviceType = "1";
                this.mCarrier = getCarrier();
                this.mIp = getIP();
                this.mDeviceID = getDeviceID();
            } catch (ClassCastException ex) {
                ex.printStackTrace();
            }
        }
    }

    private String getOSVer() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 娘的，你一个广告，你要这些信息有什么用？？？
     * 
     * @return
     */
    private String getCarrier() {
        int type = VDMobileUtil.getMobileType(mContext);
        if (type == VDMobileUtil.MOBILE_2G) {
            return "2";
        } else if (type == VDMobileUtil.NONE) {
            return "0";
        } else if (type == VDMobileUtil.WIFI) {
            return "1";
        } else if (type == VDMobileUtil.MOBILE_3G) {
            return "3";
        } else if (type == VDMobileUtil.MOBILE_4G) {
            return "4";
        }
        return "5";

    }

    private String getIP() {
        return "";
    }

    private static MessageDigest mMessageDigest;

    static {
        try {
            mMessageDigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            LogS.e(TAG, "MessageDigest.init.Exception : " + e.getMessage());
        }
    }

    /**
     * 字符串的MD5码
     * 
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        if (str != null) {
            return getMD5(str.getBytes());
        }
        return null;
    }

    /**
     * 获取字节数组的MD5码
     * 
     * @param bytes
     * @return
     */
    private static String getMD5(byte[] bytes) {
        if (mMessageDigest != null && bytes != null) {
            synchronized (VDAdvRequestData.class) {
                mMessageDigest.reset();
                mMessageDigest.update(bytes);
                return bytesToHexString(mMessageDigest.digest());
            }
        }
        return null;
    }

    private static String bytesToHexString(byte[] bytes) {
        if (bytes != null) {
            StringBuffer stringBuffer = new StringBuffer();
            String hv;
            for (int i = 0; i < bytes.length; i++) {
                int v = bytes[i] & 0xFF;
                hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuffer.append(0);
                }
                stringBuffer.append(hv);
            }
            return stringBuffer.toString();
        }
        return null;
    }

    private static boolean isEmpty(String str) {
        return null == str || "".equals(str.trim());
    }

    private static String getIMEI(Context _ctx) {
        TelephonyManager tm = (TelephonyManager) _ctx.getSystemService(Context.TELEPHONY_SERVICE);
        String IMEI = tm.getDeviceId();

        if (VDAdvRequestData.isEmpty(IMEI) || "0".equals(IMEI)) {
            WifiManager wifiManager = (WifiManager) _ctx.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            if (null != wifiInfo) {
                String macAddress = wifiInfo.getMacAddress();
                if (!VDAdvRequestData.isEmpty(macAddress)) {
                    IMEI = macAddress;
                }
            }
        }

        return IMEI;
    }

    private String getDeviceID() {
        return getMD5(getIMEI(mContext));
    }

}
