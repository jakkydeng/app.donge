package com.sina.sinavideo.sdk.utils;

import java.util.ArrayList;
import java.util.List;

import com.sina.sinavideo.sdk.data.VDVideoInfo;

import android.content.Context;

/**
 * 一个广告部分数据获取的插件<br/>
 * 这个只能用于多流广告的时候，单流广告不适用<br/>
 * NOTE 不能混淆
 * 
 * @author alexsun
 * 
 */
public class VDSDKAdvManager {

    // private final String mReqPath =
    // "com.sina.sinavideo.sdk.plugin.sina_adv.VDAdvRequest";

    private VDSDKAdvRequest mAdvRequest = null;

    /**
     * 广告系统的回调接口
     * 
     * @author alexsun
     * 
     */
    public static interface VDAdvRequestListener {

        public void onADVReqComplete(VDSDKRespAdvData advRespData);

        public void onADVReqError(int errorCode);
    }

    /**
     * 广告请求部分的抽象类
     * 
     * @author alexsun
     * 
     */
    public static abstract class VDSDKAdvRequest {

        public abstract void setContext(Context context);

        public abstract void setListener(VDAdvRequestListener listener);

        public abstract void setDebug(boolean isDebug);

        public abstract void request();

        public abstract void cancel();
    }

    /**
     * 请求部分的封装，从VDVideoInfo类中进行构建
     * 
     * @author alexsun
     * 
     */
    public static abstract class VDSDKReqAdvData {

        public abstract void readFrom(VDVideoInfo info);
    }

    public static abstract class VDSDKRespAdvData {

        /**
         * 获取的广告的url列表
         */
        public List<String> mAdvUrl = new ArrayList<String>();
        /**
         * 对应广告的秒数
         */
        public List<Integer> mAdvDuraction = new ArrayList<Integer>();
    }

    public VDSDKAdvManager() {
        super();
    }

    public void request() {
        mAdvRequest.request();
    }

    public void cancel() {
        mAdvRequest.cancel();
    }

    // ------------------单例部分
    private static class VDSDKAdvManagerINSTANCE {

        private static VDSDKAdvManager instance = new VDSDKAdvManager();
    }

    public static VDSDKAdvManager getInstance(Context context, VDAdvRequestListener l) {
        VDSDKAdvManager instance = VDSDKAdvManagerINSTANCE.instance;
        String classPath = VDSDKConfig.getInstance().getAdvClassPath();
        instance.mAdvRequest = (VDSDKAdvRequest) VDUtility.loadClass(VDApplication.getInstance().getContext(),
                classPath);
        instance.mAdvRequest.setContext(context);
        instance.mAdvRequest.setListener(l);
        instance.mAdvRequest.setDebug(VDApplication.getInstance().mDebug);
        return instance;
    }
    // ------------------单例部分----end
}
