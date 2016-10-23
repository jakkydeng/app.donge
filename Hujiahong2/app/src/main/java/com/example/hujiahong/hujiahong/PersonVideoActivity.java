package com.example.hujiahong.hujiahong;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoExtListeners;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.VDVideoViewController;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.log.StatisticUtil;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;

import java.util.List;

import VO.VDVideoInfoVO;
import nf.framework.core.exception.LogUtil;
import utils.ImageManger;

/**
 * Created by hujiahong on 16/10/15.
 */

public class PersonVideoActivity extends MyBaseActivity implements VDVideoExtListeners.OnVDVideoFrameADListener, VDVideoExtListeners.OnVDVideoInsertADListener,
        VDVideoExtListeners.OnVDVideoPlaylistListener {

    public static final String INTENT_VideoList ="videoList";
    private VDVideoView mVDVideoView = null;
    private final static String TAG = "HorizonVideoActivity";
    private VDVideoPlayListView listView;
    private RelativeLayout video_back;
    private ImageView video_bg;
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
        prepareListener(infoList);
    }
    public void prepareListener(final VDVideoListInfo infoList) {
        VDVideoViewController controller = VDVideoViewController.getInstance(this);
        if (controller != null) {
            controller.getExtListener().setOnVDVideoCompletionListener(new VDVideoExtListeners.OnVDVideoCompletionListener() {
                @Override
                public void onVDVideoCompletion(VDVideoInfo info, int status) {
                    int index = infoList.getVideoInfoKey(info);
                    index++;
                    if (infoList.getRealVideoInfo(index) != null) {
                        mVDVideoView.play(index);
                    } else {
                        Toast.makeText(PersonVideoActivity.this,"重新播放",Toast.LENGTH_SHORT).show();
//                        mOnPlayListener.onPlayFinish();//结束播放
                        mVDVideoView.open(PersonVideoActivity.this, infoList);
                        //   vDVideoInfoVO.setDuration((int) attach.getViedoTime() * 1000);
                        mVDVideoView.play(0);
                    }
                }
            });
        }
    }
    private void initData() {
        setContentView(R.layout.video_layout);
        video_bg = (ImageView) findViewById(R.id.video_bg);
        ImageManger.asyncLoadImage(video_bg,"http://182.254.130.173/app/upload/1.png");
        mVDVideoView = (VDVideoView) findViewById(R.id.vv1);
        video_back = (RelativeLayout) findViewById(R.id.back_video);
        video_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
