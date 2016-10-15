package com.sina.sinavideo.sdk.data;

import java.util.ArrayList;
import java.util.List;

import com.sina.sinavideo.sdk.log.Statistic;

/**
 * 日志数据封装类
 * 
 * @author sunxiao
 */
public class VDSDKLogData {

    public volatile long mLogFirstBufferStartTime = 0;// 用于记录超时日志
    public volatile long mLogBufferStartTime = 0;// 用于记录卡结束日志
    // first frame
    public volatile String mLogFirstFrameVideoOnPreparedTime = "";
    public volatile String mLogFirstFrameVideoBufferStartTime = "";
    public volatile String mLogFirstFrameVideoBufferEndTime = "";
    public volatile String mLogStopType = Statistic.CLOSE_TYPE_TIMEOVER;
    public volatile long mSeekFrom = 0;
    public volatile long mSeekTo = 0;
    public volatile VDVideoListInfo mLogVideoInfoList = new VDVideoListInfo();
    public volatile VDPlayerInfo mLogPlayInfo = new VDPlayerInfo();
    public List<String> mRedirectUrls = new ArrayList<String>();
    // 增加的需求
    // 视频播放的videoid[vid->vms中的vid]
    public volatile String mVideoID = "";
    // 视频播放的清晰度[sd(标清), hd(⾼高清), fhd(超清)]
    public volatile String mVDef = "";
    // 点播接口端上报数据
    public volatile String mVIAsk = "";
    // 直播接口端上报数据
    public volatile String mVLive = "";

    private static class VDSDKLogDataInstance {

        public static VDSDKLogData instance = new VDSDKLogData();
    }

    /**
     * 获取{@link VDSDKLogData}单实例
     * 
     * @return {@link VDSDKLogData}单实例对象
     */
    public static VDSDKLogData getInstance() {
        return VDSDKLogDataInstance.instance;
    }

    /**
     * 清理当前的数据，并设立初始值，每次重新初始化播放器的时候，都得清理一次
     */
    public synchronized void clearVID() {
        // 增加的需求
        // 视频播放的videoid[vid->vms中的vid]
        mVideoID = "";
        // 视频播放的清晰度[sd(标清), hd(⾼高清), fhd(超清)]
        mVDef = "";
        // 点播接口端上报数据
        mVIAsk = "";
        // 直播接口端上报数据
        mVLive = "";
    }

}
