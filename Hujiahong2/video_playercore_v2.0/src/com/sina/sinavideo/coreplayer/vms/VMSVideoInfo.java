package com.sina.sinavideo.coreplayer.vms;

import java.util.HashMap;

import com.sina.sinavideo.coreplayer.util.VDResolutionData;

/**
 * 通过VMS系统返回的视频基础信息类
 * 
 * @author GengHongchao
 * 
 */
public class VMSVideoInfo {

    /** 视频ID */
    String videoID;
    /** 视频标题 */
    String title;
    /** 视频长度 */
    int duration;
    /** 视频来源 */
    String from;
    /** 媒体标识 */
    String mediaTag;
    /** 转码类型 */
    String transcodeSystem;
    /** 视频缩略图地址 */
    String thumbnailUrl;
    /** 频道路径 */
    String channelPath;
    /** 视频默认播放地址 */
    String defaultPlayUrl;
    /** 视频默认清晰度关键值 */
    String defaultDefKey;
    /** 清晰度信息集合 */
    // HashMap<String, String> definitionInfos = new HashMap<String, String>();
    VDResolutionData mResolutionData = new VDResolutionData();

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMediaTag() {
        return mediaTag;
    }

    public void setMediaTag(String mediaTag) {
        this.mediaTag = mediaTag;
    }

    public String getTranscodeSystem() {
        return transcodeSystem;
    }

    public void setTranscodeSystem(String transcodeSystem) {
        this.transcodeSystem = transcodeSystem;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getChannelPath() {
        return channelPath;
    }

    public void setChannelPath(String channelPath) {
        this.channelPath = channelPath;
    }

    // public HashMap<String, String> getDefinitionInfo() {
    // return definitionInfos;
    // }
    //
    // public void setDefinitionInfo(HashMap<String, String> definition) {
    // this.definitionInfos = definition;
    // }

    public VDResolutionData getDefinitionInfo() {
        return mResolutionData;
    }

    public void setDefinitionInfo(VDResolutionData definition) {
        this.mResolutionData = definition;
    }

    public String getDefaultPlayUrl() {
        return defaultPlayUrl;
    }

    public void setDefaultPlayUrl(String defaultPlayUrl) {
        this.defaultPlayUrl = defaultPlayUrl;
    }

    public String getDefaultDefKey() {
        return defaultDefKey;
    }

    public void setDefaultDefKey(String defaultDefKey) {
        this.defaultDefKey = defaultDefKey;
    }

}
