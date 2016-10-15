package com.sina.sinavideo.sdk.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.sina.sinavideo.sdk.log.VDSinaSDKLog;

/**
 * 日志封装类[日志部分，都由此类做分发]<br/>
 * NOTE index都是infolist中的实际下标地址<br/>
 * NOTE 不能混淆
 * 
 * @author sunxiao
 */
public class VDSDKLogManager {

    /**
     * 更改日志部分，修改此处 新浪专用
     */
    // private final String mPath = "com.sina.sinavideo.sdk.log.VDSinaSDKLog";

    /**
     * 更改日志部分，修改此处 这是一个DEMO
     */
    // private final String mPath =
    // "com.sina.sinavideo.sdk.log_demo.VDDemoSDKLog";

    private static final String TAG = "VDSDKLogManager";

    // ------------状态部分--------------------//
    public final static int VDLOG_STATUS_SET = 1; // 设置播放流数据的时候
    public final static int VDLOG_STATUS_START = 2; // 执行play的时候
    public final static int VDLOG_STATUS_STOP = 3; // 播放完毕的时候
    public final static int VDLOG_STATUS_PAUSE = 4; // 点击暂停按钮
    public final static int VDLOG_STATUS_RESUME = 5; // 点击开始按钮
    public final static int VDLOG_STATUS_SEEK = 6; // 拖动
    public final static int VDLOG_STATUS_SOUNDCHANGE = 10; // 调节声音
    public final static int VDLOG_STATUS_BRIGHTCHANGE = 11; // 调节亮度
    public final static int VDLOG_STATUS_TO_FULLSCREEN = 12; // 转到全屏
    public final static int VDLOG_STATUS_FULLSCREEN_TO = 13; // 转到小屏
    public final static int VDLOG_STATUS_BACKENTERFRONT = 13; // 后台到前台
    public final static int VDLOG_STATUS_FRONTENTERBACK = 14; // 前台到后台
    public final static int VDLOG_STATUS_PREPARED = 100; // 状态改变部分：onPrepared
    public final static int VDLOG_STATUS_BUFFER_START = 101; // 状态改变部分：缓冲区空
    public final static int VDLOG_STATUS_BUFFER_READY = 102; // 状态改变部分：缓冲区满
    public final static int VDLOG_STATUS_FIRSTFRAME = 103; // 状态改变部分：首帧加载结束，马上开始播放了
    public final static int VDLOG_STATUS_LIVEHEARTBEAT = 104; // 心跳部分，按照config.properties配置中的时间进行

    // ------------状态部分--------------------sina专用
    public final static int VDLOG_STATUS_URL_M3U8 = 10000;
    public final static int VDLOG_STATUS_URL_PARSER = 10100;
    public final static int VDLOG_STATUS_URL_VID = 10200;
    public final static int VDLOG_STATUS_URL_ADV = 10300;

    // ------------信息部分--------------------//
    public final static int VDLOG_INFO_KADUN_BEGIN = 1; // 卡顿开始-网络或者系统原因导致
    public final static int VDLOG_INFO_KADUN_END = 2; // 卡顿结束
    public final static int VDLOG_INFO_FIRSTFRAME_DELAY = 3; // 首帧加载超时

    // ------------错误部分--------------------//
    public final static int VDLOG_ERROR_UNKNOWN = 1;
    public final static int VDLOG_ERROR_TIMEOUT = 2; // 超时
    public final static int VDLOG_ERROR_M3U8_PARSE_ERR = 3;
    public final static int VDLOG_ERROR_M3U8_NOCONTENT_ERR = 4;
    public final static int VDLOG_ERROR_MP4_PARSE_ERR = 5;
    public final static int VDLOG_ERROR_ADV_PARSE_ERR = 6;
    public final static int VDLOG_ERROR_ADV_NETWORK_ERR = 7;
    public final static int VDLOG_ERROR_ADV_JSON_PARSE_ERR = 8;

    // -------------网络改变的部分----------------//
    public final static int VDLOG_NETWORK_CHANGED_WIFI = 1;
    public final static int VDLOG_NETWORK_CHANGED_MOBILE = 2;
    public final static int VDLOG_NETWORK_CHANGED_NOTHING = 3;
    public final static int VDLOG_NETWORK_CHANGE_TO_MOBILE_2G = 4;
    public final static int VDLOG_NETWORK_CHANGE_TO_MOBILE_3G = 5;
    public final static int VDLOG_NETWORK_CHANGE_TO_MOBILE_4G = 6;

    /**
     * 统一的日志接口类
     * 
     * @author sunxiao
     */
    public static interface VDSdkLog {

        /**
         * 状态改变时候，[触发 开始、结束、暂停、恢复、加载完成]
         * 
         * @param info
         * @param logData
         */
        public void onStatusChange(int index, int status);

        /**
         * 一些非错误信息
         * 
         * @param info
         * @param logData
         */
        public void onInfoLog(int index, int status);

        /**
         * 一些错误信息
         * 
         * @param info
         * @param logData
         */
        public void onErrorLog(int index, int status);

        /**
         * 释放资源部分
         */
        public void release(int index);

        /**
         * 网络改变的部分
         * 
         * @param type
         */
        public void networkChanged(int index, int type);
    }

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private VDSdkLog mSDKLog = null;

    public VDSDKLogManager() {
        super();
        // mSDKLog = loadClass();
        Context context = VDApplication.getInstance().getContext();

        VDSinaSDKLog sdkLog = (VDSinaSDKLog) VDUtility.loadClass(context, VDSDKConfig.getInstance(context)
                .getLogClassPath());
        sdkLog.init(VDApplication.getInstance().getContext());
        mSDKLog = sdkLog;
    }

    private static class VDSDKLogManagerINSTANCE {

        private static VDSDKLogManager instance = new VDSDKLogManager();
    }

    public static VDSDKLogManager getInstance() {
        return VDSDKLogManagerINSTANCE.instance;
    }

    /**
     * 释放资源部分
     */
    public void release() {
        // mHandler.post(new Runnable() {
        //
        // @Override
        // public void run() {
        // // TODO Auto-generated method stub
        // // if (mSDKLog != null) {
        // // mSDKLog.release();
        // // }
        // }
        // });
    }

    // -------------状态日志部分-----------------//
    /**
     * 暂停时候发送日志
     * 
     * @param currTime
     *            当前视频位置点
     */
    public void pause(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_PAUSE");
                if (mSDKLog != null) {
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_PAUSE);
                }
            }
        });
    }

    public void startLiveHeartBeat(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "liveHeartBeat");
                if (mSDKLog != null) {
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_LIVEHEARTBEAT);
                }
            }
        });
    }

    /**
     * 恢复时候发送日志
     */
    public void resume(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_RESUME");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_RESUME);
            }
        });
    }

    /**
     * 设置视频列表
     */
    public void setVideo() {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                VDLog.d(TAG, "VDLOG_STATUS_START");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(0, VDLOG_STATUS_SET);
            }
        });
    }

    /**
     * 开始时候发送日志
     */
    public void start(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_START");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_START);
            }
        });
    }

    /**
     * 结束时候发送日志
     */
    public void stop(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_STOP");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_STOP);
            }
        });
    }

    /**
     * 加载完成，准备播放回调时候发送日志，【首帧日志】
     */
    public void onBufferEnd(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_READY");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_BUFFER_READY);
            }
        });
    }

    /**
     * 加载完成，准备播放回调时候发送日志，【首帧日志】
     */
    public void onFirstFrame(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_FIRSTFRAME");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_FIRSTFRAME);
            }
        });
    }

    /**
     * 缓冲开始
     */
    public void onBufferStart(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_BUFFER");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_BUFFER_START);
            }
        });
    }

    /**
     * 进度变化时候，发送日志
     * 
     * @param info
     * @param logData
     */
    public void onSeek(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_SEEK");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_SEEK);
            }
        });
    }

    /**
     * 音量变化时候发送日志
     * 
     * @param info
     * @param logData
     */
    public void onSoundChange(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_SOUNDCHANGE");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_SOUNDCHANGE);
            }
        });
    }

    /**
     * 亮度变化时候发送日志
     * 
     * @param info
     * @param logData
     */
    public void onBrightChange(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_BRIGHTCHANGE");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_BRIGHTCHANGE);
            }
        });
    }

    /**
     * 竖-》横
     * 
     * @param index
     */
    public void onToFullScreen(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_FULLSCREEN");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_TO_FULLSCREEN);
            }
        });
    }

    /**
     * 横-》竖
     * 
     * @param index
     */
    public void onFullScreenTo(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_FULLSCREEN");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_FULLSCREEN_TO);
            }
        });
    }

    /**
     * 全屏转换时候发送日志
     * 
     * @param info
     * @param logData
     */
    // public void onFullScreenChange(final int index) {
    // mHandler.post(new Runnable() {
    //
    // @Override
    // public void run() {
    // VDLog.d(TAG, "VDLOG_STATUS_FULLSCREEN");
    // if (mSDKLog != null)
    // mSDKLog.onStatusChange(index, VDLOG_STATUS_FULLSCREEN_TO);
    // }
    // });
    // }

    /**
     * 从后台激活到前台
     */
    public void onBackEnterFront(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_BACKENTERFRONT");
                if (mSDKLog != null)

                    mSDKLog.onStatusChange(index, VDLOG_STATUS_BACKENTERFRONT);
            }
        });
    }

    /**
     * 从前台进入后台状态
     */
    public void onFrontEnterBack(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_STATUS_FRONTENTERBACK");
                if (mSDKLog != null)
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_FRONTENTERBACK);
            }
        });
    }

    /**
     * onPrepared的日志记录函数
     * 
     * @param index
     */
    public void onPrepared(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                VDLog.d(TAG, "");
                if (mSDKLog != null) {
                    mSDKLog.onStatusChange(index, VDLOG_STATUS_PREPARED);
                }
            }
        });
    }

    // -------------警告信息日志部分-----------------//
    /**
     * 卡顿发送日志
     */
    public void onStuckBegin(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_INFO_KADUN_BEGIN");
                if (mSDKLog != null)
                    mSDKLog.onInfoLog(index, VDLOG_INFO_KADUN_BEGIN);
            }
        });
    }

    /**
     * 卡顿恢复发送日志
     */
    public void onStuckEnd(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_INFO_KADUN_END");
                if (mSDKLog != null)
                    mSDKLog.onInfoLog(index, VDLOG_INFO_KADUN_END);
            }
        });
    }

    /**
     * 记录首帧卡顿，意思是加载超时
     * 
     * @param info
     *            视频信息
     * @param preTime
     *            超时时长
     */
    public void onFirstFrameDelay(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_INFO_FIRSTFRAME_DELAY");
                if (mSDKLog != null)
                    mSDKLog.onInfoLog(index, VDLOG_INFO_FIRSTFRAME_DELAY);
            }
        });
    }

    // -------------错误信息日志部分-----------------//
    /**
     * 未知错误日志
     * 
     * @param info
     * @param logData
     */
    public void onErrorUnknown(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_ERROR_UNKNOWN");
                if (mSDKLog != null)
                    mSDKLog.onErrorLog(index, VDLOG_ERROR_UNKNOWN);
            }
        });
    }

    /**
     * M3u8地址解析错误
     * 
     * @param info
     * @param logData
     */
    public void onErrorM3u8Parse(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_ERROR_M3U8_PARSE_ERR");
                if (mSDKLog != null)
                    mSDKLog.onErrorLog(index, VDLOG_ERROR_M3U8_PARSE_ERR);
            }
        });
    }

    /**
     * M3u8地址为空
     * 
     * @param info
     * @param logData
     */
    public void onErrorM3u8NoContent(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_ERROR_M3U8_NOCONTENT_ERR");
                if (mSDKLog != null)
                    mSDKLog.onErrorLog(index, VDLOG_ERROR_M3U8_NOCONTENT_ERR);
            }
        });
    }

    /**
     * MP4地址解析错误
     * 
     * @param info
     * @param logData
     */
    public void onErrorMp4Parse(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                VDLog.d(TAG, "VDLOG_ERROR_MP4_PARSE_ERR");
                if (mSDKLog != null)
                    mSDKLog.onErrorLog(index, VDLOG_ERROR_MP4_PARSE_ERR);
            }
        });
    }

    public void onWifiConnected(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mSDKLog != null) {
                    mSDKLog.networkChanged(index, VDLOG_NETWORK_CHANGED_WIFI);
                }
            }

        });
    }

    public void onMobileConnected(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mSDKLog != null) {
                    mSDKLog.networkChanged(index, VDLOG_NETWORK_CHANGED_MOBILE);
                }
            }

        });
    }

    public void onNothingConnected(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mSDKLog != null) {
                    mSDKLog.networkChanged(index, VDLOG_NETWORK_CHANGED_NOTHING);
                }
            }

        });
    }

    public void onToMobile2GConnected(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mSDKLog != null) {
                    mSDKLog.networkChanged(index, VDLOG_NETWORK_CHANGE_TO_MOBILE_2G);
                }
            }

        });
    }

    public void onToMobile3GConnected(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mSDKLog != null) {
                    mSDKLog.networkChanged(index, VDLOG_NETWORK_CHANGE_TO_MOBILE_3G);
                }
            }

        });
    }

    public void onToMobile4GConnected(final int index) {
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                if (mSDKLog != null) {
                    mSDKLog.networkChanged(index, VDLOG_NETWORK_CHANGE_TO_MOBILE_4G);
                }
            }

        });
    }

    public static String getSecond(long date) {
        String s1 = String.valueOf(date);
        String str = "";
        str = date / 1000 + "." + s1.substring(s1.length() - 3);
        return str;
    }
}
