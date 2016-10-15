package com.sina.sinavideo.sdk.log;

import java.net.URLEncoder;
import java.text.DecimalFormat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.SystemClock;
import android.text.TextUtils;

import com.sina.sinavideo.sdk.utils.VDLog;
import com.sina.sinavideo.sdk.utils.VDApplication;
import com.sina.sinavideo.sdk.utils.VDUtility;

/**
 * Statistic string builders.
 * 
 * @author Sina Corporation. Seven
 *         caution:.append(SystemClock.elapsedRealtime()).append(Statistic.
 *         TAG_OR)
 */
@SuppressWarnings("deprecation")
public class StatisticUtil {

    private static Context mContext;
    private static String mPackageName;
    private static String mVersionName = "";
    private static String mDeviceID = null;
    private static String mUserID = "";
    public static String sessionTime;

    public static void setUserID(String userId) {
        mUserID = userId;
    }

    public static String getUserID() {
        if (mUserID == null) {

        }
        return mUserID;
    }

    /**
     * 初始化工具类,主要是设置context对象以便相应方法的使用.
     * 
     * @param context
     */
    public static void init(Context context, String weiboDeviceID) {
        mContext = context;
        mPackageName = context == null ? "" : context.getPackageName();
        mVersionName = VDApplication.getInstance().getAPPVersion();
        mDeviceID = weiboDeviceID;
        if (TextUtils.isEmpty(weiboDeviceID)) {
            VDLog.e("StatisticUtil", "请传入weibo api 中的 deviceId!");
            mDeviceID = "-1";
        }
        VDLog.w("StatisticUtil", "init == mPackageName = " + mPackageName + " --  mVersionName = " + mVersionName
                + " -- mDeviceID = " + mDeviceID);
    }

    public static void generateSessionTime() {
        sessionTime = VDUtility.generateTime(System.currentTimeMillis(), false);
    }

    private static String generateStatisticsSessionID(String vid) {
        if (sessionTime == null) {
            generateSessionTime();
        }
        return mDeviceID + ":" + getURLEncoderStr(vid) + ":" + sessionTime;
    }

    private static String getNetWorkType() {
        if (mContext == null) {
            return Statistic.ENT_IOS_NOTREACHABLE;
        }

        String netWorkType = "";
        NetworkInfo netWorkInfo = getAvailableNetWorkInfo();

        if (netWorkInfo != null) {
            if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                netWorkType = Statistic.ENT_IOS_WIFIREACHABLE;
            } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                netWorkType = Statistic.ENT_IOS_MOBILEREACHABLE;
            } else {
                netWorkType = Statistic.ENT_IOS_NOTREACHABLE;
            }
        }

        return netWorkType;
    }

    private static NetworkInfo getAvailableNetWorkInfo() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetInfo != null && activeNetInfo.isAvailable()) {
                return activeNetInfo;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * generate common data
     */
    public static String generateCommonData(String vid, int logIndex) {
        if (logIndex <= 0) {
            logIndex = 0;
        }
        StringBuffer sb = new StringBuffer()
                .append(SystemClock.elapsedRealtime())
                .append(Statistic.TAG_OR)
                // for upload use
                .append(Statistic.TAG_LOGID).append(Statistic.TAG_EQ).append(logIndex).append(Statistic.TAG_AND)
                .append(Statistic.TAG_NETTYPE).append(Statistic.TAG_EQ).append(getURLEncoderStr(getNetWorkType()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_SESSIONID).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(generateStatisticsSessionID(vid))).append(Statistic.TAG_AND)
                .append(Statistic.TAG_TS).append(Statistic.TAG_EQ)
                .append(VDUtility.generateTime(System.currentTimeMillis(), true));

        return sb.toString();
    }

    /*
     * generate base data
     */
    public static String generateBaseInfoData(String vid, String netStreamType) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDBaseInfo.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_APP).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(mPackageName)).append(Statistic.TAG_AND).append(Statistic.TAG_APPVERSION)
                .append(Statistic.TAG_EQ).append(getURLEncoderStr(mVersionName)).append(Statistic.TAG_AND)
                .append(Statistic.TAG_NETSTREAMTYPE).append(Statistic.TAG_EQ).append(getURLEncoderStr(netStreamType))
                .append(Statistic.TAG_AND).append(Statistic.TAG_VIDEOID).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(vid)).append(Statistic.TAG_AND).append(Statistic.TAG_DEVICEID)
                .append(Statistic.TAG_EQ).append(getURLEncoderStr(VDUtility.getIMEI())).append(Statistic.TAG_AND)
                .append(Statistic.TAG_DEVICESYS).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(VDUtility.getOSVersionInfo())).append(Statistic.TAG_AND)
                .append(Statistic.TAG_DEVICETYPE).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(VDUtility.getMobileModel())).append(Statistic.TAG_AND)
                .append(Statistic.TAG_LOGSYSVERSION).append(Statistic.TAG_EQ).append(Statistic.TAG_LOGVERSION)
                .append(Statistic.TAG_AND).append(Statistic.TAG_USERID).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(getUserID())).append(Statistic.TAG_AND).append(Statistic.TAG_PLATFORM)
                .append(Statistic.TAG_EQ).append(Statistic.ENT_PLATFORM);

        return sb.toString();
    }

    /*
     * 首帧日志
     */
    public static String generateFirstFrameData(String vid, String currTime, String bufferStartTime,
            String bufferEndTime, String firstBufferFull) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoFirstFrame.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_FIRSTFRAMETSEC).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(currTime)).append(Statistic.TAG_AND).append(Statistic.TAG_PLOADSTREAMTIME)
                .append(Statistic.TAG_EQ).append(getURLEncoderStr(bufferStartTime)).append(Statistic.TAG_AND)
                .append(Statistic.TAG_PSTREAMREADYTIME).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(bufferEndTime)).append(Statistic.TAG_AND)
                .append(Statistic.TAG_PFIRSTBUFFERFULL).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(firstBufferFull));

        return sb.toString();
    }

    /*
     * 卡顿开始 eventID is a timestamp, value equals high ping end data
     */
    public static String generateHighPingStartData(String vid, String currTime, String totalTime, String quelity,
            String eventID) {
        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoBufferEmptyStart.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_HIGHPINGSTARTCSEC).append(Statistic.TAG_EQ)
                .append(currTime).append(Statistic.TAG_AND).append(Statistic.TAG_HIGHPINGSTARTTSEC)
                .append(Statistic.TAG_EQ).append(totalTime).append(Statistic.TAG_AND)
                .append(Statistic.TAG_HIGHPINGSTARTQUALITY).append(Statistic.TAG_EQ).append(quelity)
                .append(Statistic.TAG_AND).append(Statistic.TAG_HIGHPINGSTARTEVENTID).append(Statistic.TAG_EQ)
                .append(eventID).append(Statistic.TAG_AND).append(Statistic.TAG_HIGHPINGSTARTEVENT)
                .append(Statistic.TAG_EQ).append(Statistic.ENT_HIGHPINGSTART_EVENT);

        return sb.toString();
    }

    /*
     * 卡顿结束
     */
    public static String generateHighPingEndData(String vid, String currTime, String totalTime, String quelity,
            String eventID, String bufferTime) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoBufferEmptyEnd.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_HIGHPINGENDCSEC).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(currTime)).append(Statistic.TAG_AND).append(Statistic.TAG_HIGHPINGENDTSEC)
                .append(Statistic.TAG_EQ).append(getURLEncoderStr(totalTime)).append(Statistic.TAG_AND)
                .append(Statistic.TAG_HIGHPINGENDQUELITY).append(Statistic.TAG_EQ).append(getURLEncoderStr(quelity))
                .append(Statistic.TAG_AND).append(Statistic.TAG_HIGHPINGENDEVENTID)
                .append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(eventID))
                .append(Statistic.TAG_AND)
                //
                .append(Statistic.TAG_HIGHPINGENDBUFFERTIME).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(getSecondTime(bufferTime))).append(Statistic.TAG_AND)
                .append(Statistic.TAG_HIGHPINGENDEVENT).append(Statistic.TAG_EQ)
                .append(Statistic.ENT_HIGHPINGEND_EVENT);

        return sb.toString();
    }

    /*
     * 首帧加载失败
     */
    public static String generatePlayFailData(String vid, String errorType, String errorDomain, String errorInfo) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoFail.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_ERROR).append(Statistic.TAG_EQ).append(errorType)
                .append(Statistic.TAG_AND).append(Statistic.TAG_ERRORDOMAIN).append(Statistic.TAG_EQ)
                .append(errorDomain).append(Statistic.TAG_AND).append(Statistic.TAG_ERRORINFO).append(Statistic.TAG_EQ)
                .append(errorInfo);

        return sb.toString();
    }

    /*
     * m3u8解析错误
     */
    public static String generateM3U8ParseErrorData(String vid) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoFail.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_LOGSUBID).append(Statistic.TAG_EQ)
                .append(Statistic.SVPLogSubIDS.SVPLogIDVideoELiveParseM3u8.index());

        return sb.toString();
    }

    /*
     * m3u8跳转失败
     */
    public static String generateM3U8ParseNoContentData(String vid) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoFail.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_LOGSUBID).append(Statistic.TAG_EQ)
                .append(Statistic.SVPLogSubIDS.SVPLogIDVideoELiveParseNoContent.index());

        return sb.toString();
    }

    /*
     * generate 播放器点播URL重定向解析错误⽇志 data
     */
    public static String generateRedirectParseErrorData(String vid, String extend) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoFail.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_LOGSUBID).append(Statistic.TAG_EQ)
                .append(Statistic.SVPLogSubIDS.SVPLogIDVideoENoliveGetRealMp4.index()).append(Statistic.TAG_AND)
                .append(Statistic.TAG_ERROREXTEND).append(Statistic.TAG_EQ).append(extend);

        return sb.toString();
    }

    /*
     * 暂停
     */
    public static String generatePauseData(String vid, String currTime) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoPause.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_PAUSECSEC).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(currTime)).append(Statistic.TAG_AND).append(Statistic.TAG_PAUSEEVENT)
                .append(Statistic.TAG_EQ).append(Statistic.ENT_PAUSE);

        return sb.toString();
    }

    /*
     * 继续 pauseStartTime : time when pause action start pauseRamainTime : time
     * between pause action and resume action (seconds)
     */
    public static String generateResumeData(String vid, String pauseStartTime, String pauseRamainTime) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoResume.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_BEGINSEC).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(pauseStartTime)).append(Statistic.TAG_AND).append(Statistic.TAG_PAUSETIME)
                .append(Statistic.TAG_EQ).append(getURLEncoderStr(pauseRamainTime)).append(Statistic.TAG_AND)
                .append(Statistic.TAG_RESUMEEVENT).append(Statistic.TAG_EQ).append(Statistic.ENT_RESUME);

        return sb.toString();
    }

    /*
     * 拖动进度
     */
    public static String generateSeekData(String vid, String seekFrom, String seekTo) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoDrag.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_DRAGFROM).append(Statistic.TAG_EQ).append(seekFrom)
                .append(Statistic.TAG_AND).append(Statistic.TAG_DRAGTO).append(Statistic.TAG_EQ).append(seekTo);

        return sb.toString();
    }

    /*
     * 􏰠􏰤􏰐􏱮􏰝􏲤􏰲􏰳􏰠􏰤􏰐􏱮􏰝􏲤􏰲􏰳􏰠􏰤􏰐􏱮􏰝􏲤􏰲􏰳􏰠􏰤􏰐􏱮􏰝􏲤􏰲􏰳播放模块启动日志
     */
    public static String generateStartPlayerModuleData(String vid, String plistLength) {

        StringBuffer sb = new StringBuffer().append(SystemClock.elapsedRealtime()).append(Statistic.TAG_OR)
                .append(Statistic.TAG_LOGID).append(Statistic.TAG_EQ)
                .append(Statistic.SVPLogIDS.SVPLogIDModuleStart.index()).append(Statistic.TAG_AND)
                .append(Statistic.TAG_PLAYLISTCOUNT).append(Statistic.TAG_EQ).append(getURLEncoderStr(plistLength))
                .append(Statistic.TAG_AND).append(Statistic.TAG_APP).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(mPackageName)).append(Statistic.TAG_AND).append(Statistic.TAG_APPVERSION)
                .append(Statistic.TAG_EQ).append(getURLEncoderStr(mVersionName)).append(Statistic.TAG_AND)
                .append(Statistic.TAG_DEVICEID).append(Statistic.TAG_EQ).append(getURLEncoderStr(VDUtility.getIMEI()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_DEVICESYS).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(VDUtility.getOSVersionInfo())).append(Statistic.TAG_AND)
                .append(Statistic.TAG_DEVICETYPE).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(VDUtility.getMobileModel())).append(Statistic.TAG_AND)
                .append(Statistic.TAG_LOGSYSVERSION).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(Statistic.TAG_LOGVERSION)).append(Statistic.TAG_AND).append(Statistic.TAG_TS)
                .append(Statistic.TAG_EQ).append(VDUtility.generateTime(System.currentTimeMillis(), true));

        return sb.toString();
    }

    /*
     * 开始播放视频
     */
    public static String generatePlayVideoOperationData(String vid, String title, String playURL, String videoid,
            String vdef, String viask, String vlive) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoPlay.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_TITLE).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(title)).append(Statistic.TAG_AND).append(Statistic.TAG_URL)
                .append(Statistic.TAG_EQ).append(getURLEncoderStr(playURL)).append(Statistic.TAG_AND)
                .append(Statistic.TAG_NEW_VIDEOID).append(Statistic.TAG_EQ).append(getURLEncoderStr(videoid))
                .append(Statistic.TAG_AND).append(Statistic.TAG_VDEF).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(vdef)).append(Statistic.TAG_AND).append(Statistic.TAG_VIASK)
                .append(Statistic.TAG_EQ).append(getURLEncoderStr(viask)).append(Statistic.TAG_AND)
                .append(Statistic.TAG_VLIVE).append(Statistic.TAG_EQ).append(getURLEncoderStr(vlive));

        return sb.toString();
    }

    /*
     * 视频播放结束 stopType : timeover | userclose
     */
    public static String generatePlayEndData(String vid, String stopType) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoStop.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_CLOSETYPE).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(stopType));

        return sb.toString();
    }

    public static String generatePretimeData(String vid, String pretime) {
        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoPretime.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_PRETIME).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(pretime));

        return sb.toString();
    }

    /*
     * generate when the video bring to front time
     */
    public static String generatePlayerBackOrFrontStatus(String vid, int enterBack, String enterId) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoBackground.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_ENTERBACK).append(Statistic.TAG_EQ).append(enterBack)
                .append(Statistic.TAG_AND).append(Statistic.TAG_ENTERID).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(enterId));
        return sb.toString();
    }

    /*
     * generate 直播心跳 data
     */
    public static String generateLiveVideoHeartBeatData(String vid) {

        StringBuffer sb = new StringBuffer()
                .append(generateCommonData(vid, Statistic.SVPLogIDS.SVPLogIDVideoHeartBeat.index()))
                .append(Statistic.TAG_AND).append(Statistic.TAG_VIDEOID).append(Statistic.TAG_EQ)
                .append(getURLEncoderStr(vid));

        return sb.toString();
    }

    private static String getURLEncoderStr(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        try {
            return URLEncoder.encode(str);
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    private static String getSecondTime(String millisecondTime) {
        DecimalFormat mDecimalFormat = new DecimalFormat("##0.000");// 保持小数点后三位
        return mDecimalFormat.format(Long.parseLong(millisecondTime) / 1000.0);
    }

}
