package com.sina.sinavideo.sdk.plugin.sina_adv;

import java.util.ArrayList;
import java.util.List;

import com.sina.sinavideo.sdk.utils.VDSDKAdvManager.VDSDKRespAdvData;

/**
 * 广告接口返回的数据结构，跟随VDVideoInfo，一个片子一个广告结构
 * 
 * @author alexsun
 * 
 */
public class VDAdvResponseData extends VDSDKRespAdvData {

    public static class VDAdvRespContentData {

        // 点击监测
        public List<String> mMonitor = new ArrayList<String>();
        // 广告ID
        public String mLineItemID = "";
        // 开始时间，从1970年1月1日0时0分0秒开始经过的秒数
        public int mBeginTime = 0;
        // 结束时间，从1970年1月1日0时0分0秒开始经过的秒数
        public int mEndTime = 0;
        // 最大展示次数
        public int mFreq = 0;
        // 关闭监测
        public List<String> mClose = new ArrayList<String>();
        // 曝光监测
        public List<String> mPV = new ArrayList<String>();
        // 素材内容[就这个有用，播放地址，其余不管]
        public List<String> mSrc = new ArrayList<String>();
        // 素材类型
        public List<String> mType = new ArrayList<String>();
        // 落地页
        public List<String> mLink = new ArrayList<String>();
    }

    public String mAdvId = "";
    public List<VDAdvRespContentData> mAD = new ArrayList<VDAdvResponseData.VDAdvRespContentData>();

}
