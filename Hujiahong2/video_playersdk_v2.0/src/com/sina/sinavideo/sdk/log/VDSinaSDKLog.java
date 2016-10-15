
package com.sina.sinavideo.sdk.log;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.coreplayer.util.VDResolutionData;
import com.sina.sinavideo.sdk.data.VDPlayerInfo;
import com.sina.sinavideo.sdk.data.VDSDKLogData;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDLog;
import com.sina.sinavideo.sdk.utils.VDSDKLogManager;
import com.sina.sinavideo.sdk.utils.VDSDKLogManager.VDSdkLog;

public class VDSinaSDKLog implements VDSdkLog {

    private final static String TAG = "VDSinaSDKLog";
    private static final int LIVE_HEART_BEAT = 5;

    private LogPushManager mLogPushManager;

    private long mPauseTime = 0;
    private long mPauseTimeDuration = 0;
    private String mVideoBufferPosition = "0";
    private VDTimeOutHandler mTimeOutHandler = null;
    private String mEnterID = null;
    private String eventID = "";

    private static class VDTimeOutHandler extends Handler {

        private WeakReference<VDSinaSDKLog> mLog = null;

        public VDTimeOutHandler(VDSinaSDKLog log) {
            super(Looper.getMainLooper());
            mLog = new WeakReference<VDSinaSDKLog>(log);
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            if (mLog == null) {
                return;
            }
            switch (msg.what) {
                case LIVE_HEART_BEAT:
                    VDLog.d(TAG, "LIVE_HEART_BEAT");
                    if (mLog.get().mLogPushManager != null && VDSDKLogData.getInstance().mLogVideoInfoList != null) {
                        mLog.get().mLogPushManager.writeToFile(StatisticUtil
                                .generateLiveVideoHeartBeatData(VDSDKLogData.getInstance().mLogVideoInfoList
                                        .getCurrInfo().mVideoId));
                    }
                    mLog.get().mTimeOutHandler.sendEmptyMessageDelayed(LIVE_HEART_BEAT, 5 * 60 * 1000L);
                    break;
            }
        };
    };

    public void startLiveHeartBeat() {
        mTimeOutHandler.sendEmptyMessage(LIVE_HEART_BEAT);
    }

    public VDSinaSDKLog() {
        super();
    }

    public void init(Context context) {
        // /this context is application context, not context point to activity
        VDLog.d(TAG, "context ctt=" + context);
        mLogPushManager = new LogPushManager(context.getApplicationContext());
        // @sunxiao1 modify，修改了VDSDKLog接口，增加了网络传递改变的方法，从controller里面直接传递出来
        // 可能会出现的问题，在controller已经被销毁，但网络改变后，就没办法在SDK中得到相应的调用，可能出现问题
        // TODO 后面还是修改为在LOG中注册application的receiver，使用全局方式来处理比较好
        // try {
        // 先注释了 lyh
        // VDVideoViewController.getInstance().mReciever.addListener(mLogPushManager);
        // } catch (IllegalStateException ex) {

        // }
        mTimeOutHandler = new VDTimeOutHandler(this);
        StatisticUtil.init(context, LogProperty.mWeiboID);
    }

    @Override
    public void release(int index) {
        VDLog.d(TAG, "release");
        if (mLogPushManager != null) {
            mLogPushManager.destory();
        }
        mLogPushManager = null;
    }

    @Override
    public void onStatusChange(int index, int status) {
        VDVideoInfo videoInfo = VDSDKLogData.getInstance().mLogVideoInfoList.getVideoInfo(index);
        VDVideoListInfo videoList = VDSDKLogData.getInstance().mLogVideoInfoList;
        VDPlayerInfo playerInfo = VDSDKLogData.getInstance().mLogPlayInfo;

        if (VDSDKLogManager.VDLOG_STATUS_STOP == status && videoInfo == null) {
            VDSDKLogData.getInstance().mLogVideoInfoList = null;
            VDSDKLogData.getInstance().mLogPlayInfo = null;
            Log.i(TAG, "onStatusChange VDLOG_STATUS_STOP mLogVideoInfo");
        }
        if (videoInfo == null || playerInfo == null) {
            VDLog.d(TAG, "videoInfo == null || playerInfo == null");
            return;
        }
        if (videoInfo.mIsInsertAD) {
            VDLog.d(TAG, "mIsInsertAD");
            // 广告视频，不上报日志
            return;
        }
        if (status == VDSDKLogManager.VDLOG_STATUS_START) {
            // 开始
            try {
                if (mLogPushManager != null) {
                    VDSDKLogData logData = VDSDKLogData.getInstance();
                    mLogPushManager.writeToFile(StatisticUtil.generatePlayVideoOperationData(videoInfo.mVideoId,
                            videoInfo.mTitle, videoInfo.mPlayUrl, logData.mVideoID, logData.mVDef, logData.mVIAsk,
                            logData.mVLive));
                } else {
                    VDLog.d(TAG, "mLogPushManager is null");
                }
            } catch (Exception ex) {
                VDLog.d(TAG, ex.getMessage());
                ex.printStackTrace();
            }
        } else if (status == VDSDKLogManager.VDLOG_STATUS_STOP) {
            VDLog.d(TAG, "VDLOG_STATUS_STOP");
            // 结束
            try {
                if (playerInfo != null && mLogPushManager != null && playerInfo.mDuration > 0) {
                    if (playerInfo != null) {
                        if (videoInfo.mIsLive) {
                            mLogPushManager.writeToFile(StatisticUtil.generatePlayEndData(videoInfo.mVideoId,
                                    Statistic.CLOSE_TYPE_USERCLOSE));
                            if (mTimeOutHandler != null) {
                                mTimeOutHandler.removeMessages(LIVE_HEART_BEAT);
                            }
                        } else {
                            boolean f = playerInfo.mCurrent < playerInfo.mDuration;
                            VDSDKLogData.getInstance().mLogStopType = f
                                    ? Statistic.CLOSE_TYPE_USERCLOSE
                                    : Statistic.CLOSE_TYPE_TIMEOVER;
                            mLogPushManager.writeToFile(StatisticUtil.generatePlayEndData(videoInfo.mVideoId,
                                    VDSDKLogData.getInstance().mLogStopType));
                        }
                    }
                } else if (mLogPushManager == null) {
                    VDLog.d(TAG, "mLogPushManager is null");
                }
            } catch (Exception ex) {
                VDLog.d(TAG, ex.getMessage());
                // 日志而已，出错就不搞了
                ex.printStackTrace();
            }
        } else if (status == VDSDKLogManager.VDLOG_STATUS_PAUSE) {
            VDLog.d(TAG, "VDLOG_STATUS_PAUSE");
            // 暂停
            try {
                if (mLogPushManager != null) {
                    String position = "";
                    if (videoInfo.mIsLive) {
                        // 直播
                        position = generateTime(System.currentTimeMillis(), false);
                        mPauseTimeDuration = System.currentTimeMillis();
                    } else {
                        // 点播
                        position = videoInfo.mVideoPosition / 1000L + "";
                        mPauseTimeDuration = videoInfo.mVideoPosition;
                    }
                    mPauseTime = System.currentTimeMillis();

                    mLogPushManager.writeToFile(StatisticUtil.generatePauseData(videoInfo.mVideoId, position));
                } else {
                    VDLog.d(TAG, "mLogPushManager is null");
                }
            } catch (Exception ex) {
                VDLog.d(TAG, ex.getMessage());
                // 日志而已，出错就不搞了
                ex.printStackTrace();
            }
        } else if (status == VDSDKLogManager.VDLOG_STATUS_RESUME) {
            VDLog.d(TAG, "VDLOG_STATUS_RESUME");
            // 恢复
            try {
                if (mPauseTime == 0) {
                    // 没有暂停就恢复？估计时序有错误了。
                    LogS.e(TAG, "没有暂停就恢复？估计时序有错误了。");
                    return;
                }
                String remainTime = String.valueOf((System.currentTimeMillis() - mPauseTime) / 1000);

                if (mLogPushManager != null) {
                    mLogPushManager.writeToFile(StatisticUtil.generateResumeData(videoInfo.mVideoId, mPauseTimeDuration
                            / 1000L + "", remainTime));
                } else {
                    VDLog.d(TAG, "mLogPushManager is null");
                }
            } catch (Exception ex) {
                VDLog.d(TAG, ex.getMessage());
                // 日志而已，出错就不搞了
                ex.printStackTrace();
            }
        } else if (status == VDSDKLogManager.VDLOG_STATUS_BUFFER_READY) {
            VDLog.d(TAG, "VDLOG_STATUS_READY");
            // 加载结束
            try {
                StatisticUtil.generateSessionTime();// 产生sessionid
                if (mLogPushManager != null) {
                    VDSDKLogData logData = VDSDKLogData.getInstance();
                    String nst = videoInfo.mIsLive ? "live" : "nolive";
                    mLogPushManager.writeToFile(StatisticUtil.generateBaseInfoData(videoInfo.mVideoId, nst));
                    // 记录播放模块启动数据并上报
                    // 先注释了 @远欢
                    // @sunxiao1 modify
                    mLogPushManager.writeToFile(StatisticUtil.generateStartPlayerModuleData(videoInfo.mVideoId,
                            videoList.getRealVideoListSize() + "") + "");
                    // 记录播放视频操作数据并上报
                    mLogPushManager.writeToFile(StatisticUtil.generatePlayVideoOperationData(videoInfo.mVideoId,
                            videoInfo.mTitle, videoInfo.mPlayUrl, logData.mVideoID, logData.mVDef, logData.mVIAsk,
                            logData.mVLive));
                } else {
                    VDLog.d(TAG, "mLogPushManager is null");
                }
            } catch (Exception ex) {
                VDLog.d(TAG, ex.getMessage());
                // 日志而已，出错就不搞了
                ex.printStackTrace();
            }
        } else if (status == VDSDKLogManager.VDLOG_STATUS_FIRSTFRAME) {
            VDLog.d(TAG, "VDLOG_STATUS_FIRSTFRAME");
            // 加载结束
            try {
                StatisticUtil.generateSessionTime();// 产生sessionid
                if (mLogPushManager != null) {
                    // 首帧加载成功
                    if (mLogPushManager != null) {
                        String currTime = videoInfo.mIsLive ? 0 + "" : videoInfo.mVideoDuration / 1000 + "";
                        mLogPushManager.writeToFile(StatisticUtil.generateFirstFrameData(videoInfo.mVideoId, currTime,
                                VDSDKLogData.getInstance().mLogFirstFrameVideoOnPreparedTime,
                                VDSDKLogData.getInstance().mLogFirstFrameVideoBufferStartTime,
                                VDSDKLogData.getInstance().mLogFirstFrameVideoBufferEndTime));
                    }
                } else {
                    VDLog.d(TAG, "mLogPushManager is null");
                }
            } catch (Exception ex) {
                VDLog.d(TAG, ex.getMessage());
                // 日志而已，出错就不搞了
                ex.printStackTrace();
            }
        } else if (status == VDSDKLogManager.VDLOG_STATUS_SEEK) {
            VDLog.d(TAG, "VDLOG_STATUS_SEEK");
            // 拖动进度
            if (mLogPushManager != null) {
                String seekFrom = VDSDKLogData.getInstance().mSeekFrom / 1000L + "";
                // TODO 谁能告诉我，这个是什么算法？？？
                String seekTo = VDSDKLogData.getInstance().mSeekTo / 1000L + "";
                mLogPushManager.writeToFile(StatisticUtil.generateSeekData(videoInfo.mVideoId, seekFrom, seekTo));
            } else {
                VDLog.d(TAG, "mLogPushManager is null");
            }
        } else if (status == VDSDKLogManager.VDLOG_STATUS_BUFFER_START) {
            // 缓冲开始
        } else if (status == VDSDKLogManager.VDLOG_STATUS_BRIGHTCHANGE) {
            // 调节亮度
        } else if (status == VDSDKLogManager.VDLOG_STATUS_FULLSCREEN_TO) {
            // 全屏
        } else if (status == VDSDKLogManager.VDLOG_STATUS_SOUNDCHANGE) {
            // 声音调节
        } else if (status == VDSDKLogManager.VDLOG_STATUS_BACKENTERFRONT) {
            VDLog.d(TAG, "VDLOG_STATUS_BACKENTERFRONT");
            // 后台激活到前台
            if (mLogPushManager != null) {
                if (!TextUtils.isEmpty(mEnterID)) {
                    mLogPushManager.writeToFile(StatisticUtil.generatePlayerBackOrFrontStatus(videoInfo.mVideoId, 0,
                            mEnterID));
                    mEnterID = null;
                }
            } else {
                VDLog.d(TAG, "mLogPushManager is null");
            }
        } else if (status == VDSDKLogManager.VDLOG_STATUS_FRONTENTERBACK) {
            VDLog.d(TAG, "VDLOG_STATUS_FRONTENTERBACK");
            // 前台进入后台
            if (mLogPushManager != null) {
                mEnterID = String.valueOf(System.currentTimeMillis());
                mLogPushManager.writeToFile(StatisticUtil.generatePlayerBackOrFrontStatus(videoInfo.mVideoId, 1,
                        mEnterID));
            } else {
                VDLog.d(TAG, "mLogPushManager is null");
            }
        } else if (status == VDSDKLogManager.VDLOG_STATUS_LIVEHEARTBEAT) {
            // 直播时候才会发送心跳数据
            mTimeOutHandler.removeMessages(LIVE_HEART_BEAT);
            if (videoList.getCurrInfo() != null && videoList.getCurrInfo().mIsLive) {
                startLiveHeartBeat();
            }
        }
    }

    @Override
    public void onInfoLog(int index, int status) {
        // TODO Auto-generated method stub
        VDVideoInfo videoInfo = VDSDKLogData.getInstance().mLogVideoInfoList.getVideoInfo(index);
        VDPlayerInfo playerInfo = VDSDKLogData.getInstance().mLogPlayInfo;
        if (videoInfo == null || playerInfo == null) {
            VDLog.d(TAG, "videoInfo == null || playerInfo == null");
            // 有一个为null，那么就不记录了。
            return;
        }
        if (videoInfo.mIsInsertAD) {
            VDLog.d(TAG, "mIsInsertAD");
            // 如果是广告视频，那么就不再上报日志了
            return;
        }
        String logResolution = Statistic.ENT_RESOLUTION_SD;
        if (playerInfo.mCurResolution == VDResolutionData.TYPE_DEFINITION_HD) {
            logResolution = Statistic.ENT_RESOLUTION_HD;
        } else if (playerInfo.mCurResolution == VDResolutionData.TYPE_DEFINITION_FHD) {
            logResolution = Statistic.ENT_RESOLUTION_XHD;
        }
        try {
            if (status == VDSDKLogManager.VDLOG_INFO_KADUN_BEGIN) {
                VDLog.d(TAG, "VDLOG_INFO_KADUN_BEGIN");
                // 卡顿开始
                if (mLogPushManager != null) {
                    eventID = generateTime(System.currentTimeMillis(), true);
                    String currTimeTS = (System.currentTimeMillis() / 1000L + "");
                    VDLog.d(TAG, "currTimeTS:" + currTimeTS);
                    mVideoBufferPosition = videoInfo.mIsLive ? currTimeTS : videoInfo.mVideoPosition + "";

                    String totalTime = videoInfo.mIsLive ? 0 + "" : videoInfo.mVideoDuration / 1000 + "";

                    mLogPushManager.writeToFile(StatisticUtil.generateHighPingStartData(videoInfo.mVideoId,
                            mVideoBufferPosition, totalTime, logResolution, eventID));
                } else {
                    VDLog.d(TAG, "mLogPushManager is null");
                }
            } else if (status == VDSDKLogManager.VDLOG_INFO_KADUN_END) {
                VDLog.d(TAG, "VDLOG_INFO_KADUN_END");
                // 卡顿结束
                if (mLogPushManager != null) {
                    // TODO 这儿有问题
                    String totalTime = videoInfo.mIsLive ? 0 + "" : videoInfo.mVideoDuration / 1000 + "";
                    String bufferTime = String.valueOf(System.currentTimeMillis()
                            - VDSDKLogData.getInstance().mLogBufferStartTime);
                    mLogPushManager.writeToFile(StatisticUtil.generateHighPingEndData(videoInfo.mVideoId,
                            mVideoBufferPosition, totalTime, logResolution, eventID, bufferTime));
                } else {
                    VDLog.d(TAG, "mLogPushManager is null");
                }
            } else if (status == VDSDKLogManager.VDLOG_INFO_FIRSTFRAME_DELAY) {
                VDLog.d(TAG, "VDLOG_INFO_FIRSTFRAME_DELAY");
                // 预加载视频超时【首帧加载失败】
                if (mLogPushManager != null) {
                    String preTime = String
                            .valueOf((System.currentTimeMillis() - (double) VDSDKLogData.getInstance().mLogFirstBufferStartTime) / 1000);
                    mLogPushManager.writeToFile(StatisticUtil.generatePretimeData(videoInfo.mVideoId, preTime));
                } else {
                    VDLog.d(TAG, "mLogPushManager is null");
                }
            }
        } catch (Exception ex) {
            // 日志而已，出错就不搞了
            ex.printStackTrace();
        }
    }

    @Override
    public void onErrorLog(int index, int status) {
        // TODO Auto-generated method stub
        VDVideoInfo videoInfo = VDSDKLogData.getInstance().mLogVideoInfoList.getVideoInfo(index);
        VDPlayerInfo playerInfo = VDSDKLogData.getInstance().mLogPlayInfo;
        if (videoInfo == null || playerInfo == null) {
            VDLog.d(TAG, "videoInfo == null || playerInfo == null");
            // 有一个为null，那么就不记录了。
            return;
        }
        if (videoInfo.mIsInsertAD) {
            VDLog.d(TAG, "mIsInsertAD");
            // 如果是广告视频，那么就不再上报日志了
            return;
        }
        try {
            if (status == VDSDKLogManager.VDLOG_ERROR_UNKNOWN) {
                VDLog.d(TAG, "VDLOG_ERROR_UNKNOWN");
                // 未知错误
                if (mLogPushManager != null) {
                    mLogPushManager.writeToFile(StatisticUtil
                            .generatePlayFailData(videoInfo.mVideoId, null, null, null));
                }
            } else if (status == VDSDKLogManager.VDLOG_ERROR_M3U8_PARSE_ERR) {
                VDLog.d(TAG, "VDLOG_ERROR_M3U8_PARSE_ERR");
                // m3u8解析错误
                if (mLogPushManager != null) {
                    mLogPushManager.writeToFile(StatisticUtil.generateM3U8ParseErrorData(videoInfo.mVideoId));
                }
            } else if (status == VDSDKLogManager.VDLOG_ERROR_M3U8_NOCONTENT_ERR) {
                VDLog.d(TAG, "VDLOG_ERROR_M3U8_NOCONTENT_ERR");
                // m3u8内容为空
                if (mLogPushManager != null) {
                    mLogPushManager.writeToFile(StatisticUtil.generateM3U8ParseNoContentData(videoInfo.mVideoId));
                }
            } else if (status == VDSDKLogManager.VDLOG_ERROR_MP4_PARSE_ERR) {
                VDLog.d(TAG, "VDLOG_ERROR_MP4_PARSE_ERR");
                // mp4无法跳转
                if (mLogPushManager != null) {
                    StringBuilder sb = new StringBuilder();
                    for (String url : VDSDKLogData.getInstance().mRedirectUrls) {
                        sb.append(url).append("||");
                    }
                    if (sb.length() > 2) {
                        sb.setLength(sb.length() - 2);
                    }
                    mLogPushManager.writeToFile(StatisticUtil.generateRedirectParseErrorData(videoInfo.mVideoId,
                            sb.toString()));
                }
            }
        } catch (Exception ex) {
            // 日志而已，出错就不搞了
            ex.printStackTrace();
        }
    }

    private final String FORMAT_ALL_DATE = "yyyy-MM-dd HH:mm:ss.SSS";
    private final String FORMAT_TIME = "HH:mm:ss";

    private SimpleDateFormat sFORMAT = new SimpleDateFormat(FORMAT_ALL_DATE, Locale.CHINA);

    public String generateTime(long time, boolean isLong) {
        Date date = new Date(time);
        sFORMAT.applyPattern(isLong ? FORMAT_ALL_DATE : FORMAT_TIME);
        String LgTime = null;
        try {
            LgTime = sFORMAT.format(date);
        } catch (Exception e) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(isLong ? FORMAT_ALL_DATE : FORMAT_TIME, Locale.CHINA);
                LgTime = format.format(new Date());
                e.printStackTrace();
            } catch (Exception e1) {
                e1.printStackTrace();
                LgTime = "";
            }
        }
        return LgTime;
    }

    @Override
    public void networkChanged(int index, int type) {
        // TODO Auto-generated method stub
        // 因为当前为单例结构，在controller中可能已经被release掉了，需要判断一下
        if (mLogPushManager == null)
            return;
        switch (type) {
            case VDSDKLogManager.VDLOG_NETWORK_CHANGED_MOBILE:
                mLogPushManager.mobileConnected();
                break;
            case VDSDKLogManager.VDLOG_NETWORK_CHANGED_NOTHING:
                mLogPushManager.nothingConnected();
                break;
            case VDSDKLogManager.VDLOG_NETWORK_CHANGED_WIFI:
                mLogPushManager.wifiConnected();
                break;
        }
    }
}
