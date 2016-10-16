package com.example.hujiahong.hujiahong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import utils.ImageManger;

/**
 * 楼层分布
 * Created by hujiahong on 16/10/16.
 */

public class FloordistributionActivity extends MyBaseActivity{
private ImageView floorPic;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.navigationBarLayout.setVisibility(View.GONE);
        initView();
    }
    private void  initView() {
        View mainView = LayoutInflater.from(this).inflate(R.layout.floor_distribution, super.mainlayout, false);
        super.mainlayout.addView(mainView);
        floorPic = (ImageView) mainView.findViewById(R.id.floor_pic);
        floorPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageManger.asyncLoadImage(floorPic,"https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1476623324&di=07e684acd765160ff974b74559e71d34&src=http://pic9.nipic.com/20100827/5608174_220855012122_2.jpg");
    }
}
