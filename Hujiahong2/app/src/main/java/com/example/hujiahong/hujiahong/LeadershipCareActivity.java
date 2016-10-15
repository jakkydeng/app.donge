package com.example.hujiahong.hujiahong;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoExtListeners;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.log.StatisticUtil;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;

import java.util.List;

import VO.VDVideoInfoVO;
import nf.framework.core.exception.LogUtil;

/**
 * Created by hujiahong on 16/10/15.
 */

public class LeadershipCareActivity extends MyBaseActivity implements VDVideoExtListeners.OnVDVideoFrameADListener, VDVideoExtListeners.OnVDVideoInsertADListener,
        VDVideoExtListeners.OnVDVideoPlaylistListener {

    public static final String INTENT_VideoList ="videoList";
    private VDVideoView mVDVideoView = null;
    private final static String TAG = "HorizonVideoActivity";
    private VDVideoPlayListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);

        StatisticUtil.init(this, null);
        initData();
        getIntentData();
    }

    public void getIntentData() {

        @SuppressWarnings("unchecked")
        List<VDVideoInfoVO> list	 = (List<VDVideoInfoVO>) getIntent().getSerializableExtra(INTENT_VideoList);
        getIntent().getSerializableExtra(INTENT_VideoList);


        if(list==null){
            return;
        }
        VDVideoListInfo infoList =new VDVideoListInfo();
        for(VDVideoInfoVO  videoInfovo :list){
            VDVideoInfo videoInfo =new VDVideoInfo();
            videoInfo.mPlayUrl =videoInfovo.getUrl();
            LogUtil.e("我是地址","horizonViedeoActivity 55==="+videoInfovo.getUrl());
            videoInfo.mTitle =videoInfovo.getTitle();
            LogUtil.e("我是地址","horizonViedeoActivity 58==="+videoInfovo.getTitle());

            LogUtil.e("我是视频时间1","horizonViedeoActivity 591==="+videoInfovo.getDuration());
            LogUtil.e("我是视频时间1","horizonViedeoActivity 592==="+videoInfo.mVideoDuration);
            // videoInfovo.setDuration(300000);
            videoInfo.mIsLive=videoInfovo.isLive();
            LogUtil.e("我是地址","horizonViedeoActivity 60==="+videoInfovo.isLive());
            videoInfo.mVideoDuration=videoInfovo.getDuration();
            LogUtil.e("我是视频时间1","horizonViedeoActivity 62==="+videoInfovo.getDuration());
            LogUtil.e("我是视频时间222","horizonViedeoActivity 63==="+videoInfo.mVideoDuration);


            infoList.addVideoInfo(videoInfo);

        }
//        if (listView != null) {
//            listView.onVideoList(infoList);
//        }
        mVDVideoView.open(this, infoList);
        //   vDVideoInfoVO.setDuration((int) attach.getViedoTime() * 1000);
        mVDVideoView.play(0);
    }

    private void initData() {
        setContentView(R.layout.video_layout);
        mVDVideoView = (VDVideoView) findViewById(R.id.vv1);
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView.getParent());
        // 简单方式处理的视频列表
       // listView = (VDVideoPlayListView) findViewById(R.id.play_list_view);

        registerListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVDVideoView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVDVideoView.onPause();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!mVDVideoView.onVDKeyDown(keyCode, event)) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    private void registerListener() {
        mVDVideoView.setFrameADListener(this);
        mVDVideoView.setInsertADListener(this);
        mVDVideoView.setPlaylistListener(this);
    }

    // private void openVideo(VDVideoInfo info, int p) {
    // mVDVideoView.stop();
    // mVDVideoView.release(true);
    // mVDVideoView.open(this, info);
    // mVDVideoView.play(p);
    // }

    @Override
    protected void onStop() {
        super.onStop();
        mVDVideoView.onStop();
    }

    @Override
    protected void onDestroy() {
        mVDVideoView.release(false);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mVDVideoView.setIsFullScreen(true);
            LogS.e(VDVideoFullModeController.TAG, "onConfigurationChanged---横屏");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mVDVideoView.setIsFullScreen(false);
            LogS.e(VDVideoFullModeController.TAG, "onConfigurationChanged---竖屏");
        }

    }

    @Override
    public void onPlaylistClick(VDVideoInfo info, int p) {
        if (info == null) {
            LogS.e(TAG, "info is null");
        }
        mVDVideoView.play(p);
    }

    @Override
    public void onInsertADClick(VDVideoInfo info) {
    }

    @Override
    public void onInsertADStepOutClick(VDVideoInfo info) {
    }

    @Override
    public void onFrameADPrepared(VDVideoInfo info) {
    }
}
