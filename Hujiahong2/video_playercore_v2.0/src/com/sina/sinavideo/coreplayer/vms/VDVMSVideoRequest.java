package com.sina.sinavideo.coreplayer.vms;

import android.content.Context;
import android.text.TextUtils;

/**
 * 新版视频管理系统管理类
 * 
 * @author GengHongchao
 */
public class VDVMSVideoRequest {

    /**
     * VMS回调接口，对外
     * 
     * @author GengHongchao
     */
    public interface OnVMSRequestCallback {

        /**
         * VMS处理成功
         * 
         * @param info
         */
        public void onComplete(VMSVideoInfo info);

        /**
         * VMS处理失败
         * 
         * @param error_msg
         * @see #ERROR_JSON_ERROR
         * @see #ERROR_NETWORK_DISABLED
         * @see #ERROR_REQUEST_ERROR
         * @see #ERROR_RESPONSE_ERROR
         */
        public void onError(int error_msg);
    }

    private static final String TAG = VDVMSVideoRequest.class.getSimpleName();

    /** 网络错误 */
    public static final int ERROR_NETWORK_DISABLED = 1;
    /** 网络请求错误 */
    public static final int ERROR_REQUEST_ERROR = 2;
    /** 服务器返回结果错误 */
    public static final int ERROR_RESPONSE_ERROR = 3;
    /** 解析错误 */
    public static final int ERROR_JSON_ERROR = 4;

    /** VMS请求访问地址 */
    private static String STR_VMS_URL = "http://s.video.sina.com.cn";

    private static String STR_TYPE_APP = "app";

    private VDVMSVideoParser mParser;

    /**
     * 构造函数
     * 
     * @param context
     * @param callback
     */
    public VDVMSVideoRequest(Context context, OnVMSRequestCallback callback) {
        mParser = new VDVMSVideoParser(context, callback);
    }

    /**
     * 通过videoID请求视频相关信息
     * 
     * @param VMSID
     *            视频ID
     */
    public void requestVideoDefinition(String VMSID) {
        if (!TextUtils.isEmpty(VMSID)) {
            String url = VDVMSVideoParser.makeUri(getVMSUrl(), STR_TYPE_APP, VMSID);
            mParser.startRequest(url);
        }
    }

    /**
     * 取消VMS网络请求
     */
    public void cancelRequest() {
        mParser.cancalRequest();
    }

    /**
     * VMS地址，需要进行格式处理，补充对应数值
     * 
     * @return
     */
    private String getVMSUrl() {
        return STR_VMS_URL + "/video/play?player=%1$s&video_id=%2$s";
    }

}
