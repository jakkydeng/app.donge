package com.sina.sinavideo.sdk.utils;

import android.annotation.SuppressLint;
import android.content.Context;

import com.sina.sinavideo.sdk.VDVideoViewController;

public class VDPlayPauseHelper {

    @SuppressLint("unused")
    private Context mContext;

    public VDPlayPauseHelper(Context ctt) {
        mContext = ctt;
    }

    public void doClick() {
        VDVideoViewController controller = VDVideoViewController.getInstance(mContext);
        if (null == controller)
            return;
        // 先测试添加于此
        if (controller.mVDPlayerInfo.mIsPlaying) {
            controller.pause();
            if (controller.getVideoView() != null) {
                // 记录暂停数据并上报
                VDSDKLogManager.getInstance().pause(controller.mVDVideoListInfo.mIndex);
            }
        } else {
            controller.resume();
            controller.start();
            VDSDKLogManager.getInstance().resume(controller.mVDVideoListInfo.mIndex);
        }
    }

}
