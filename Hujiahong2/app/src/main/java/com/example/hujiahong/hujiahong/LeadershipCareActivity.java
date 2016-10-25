package com.example.hujiahong.hujiahong;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

import utils.ImageManger;
import utils.IntentUtils;

/**
 * 领导关怀的滚播图
 * Created by hujiahong on 16/10/16.
 */

public class LeadershipCareActivity extends MyBaseActivity implements View.OnLayoutChangeListener {


    private LinearLayout layout;
    private ScrollView namescroll;
    private final Handler mHandler = new Handler();
    private List<String>picList = new ArrayList<>();

    @SuppressLint("NewApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_care_main);
        layout = (LinearLayout) findViewById(R.id.layout);
        namescroll = (ScrollView) findViewById(R.id.nameScroll);
        Log.d("...........", "" + layout.getHeight());
        namescroll.addOnLayoutChangeListener(this);
        System.out.println("开始");
        initData();
        //// TODO: 16/10/16 填充数据
        for (int i = 0; i < 6; i++) {
            searchResultShow(i);
            System.out.println(i + "结束");
        }
        Log.d("...........", "" + layout.getHeight());
        System.out.println(namescroll.getScrollY());
        System.out.println("真正结束");
    }
private void  initData(){
    picList.add("http://58.57.165.71:5001/app/images/1.jpg");
    picList.add("http://58.57.165.71:5001/app/images/2.jpg");
    picList.add("http://58.57.165.71:5001/app/images/3.jpg");

    picList.add("http://58.57.165.71:5001/app/images/4.jpg");

    picList.add("http://58.57.165.71:5001/app/images/5.jpg");

    picList.add("http://58.57.165.71:5001/app/images/5.jpg");



}
    private void searchResultShow(int i) {

/**
 * 动态布局
 */
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        ImageView imageView = new ImageView(LeadershipCareActivity.this);
        imageView.setImageResource(R.drawable.ic_launcher);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtils.intentToMainActivity(LeadershipCareActivity.this);
            }
        });
        ImageManger.asyncLoadImage(imageView, picList.get(i));
        //增加一个ImageView到线性布局中
        layout.addView(imageView, p);
    }

    private Runnable ScrollRunnable = new Runnable() {
        @Override
        public void run() {
            int off = layout.getMeasuredHeight() - namescroll.getHeight();//判断高度
//            System.out.println(layout.getMeasuredHeight()+".........."+namescroll.getHeight() );
            if (off > 0) {
                namescroll.smoothScrollBy(0, 1);
                if (namescroll.getScrollY() == off) {
                    /**
                     * 到底回到最上边
                     */
                    namescroll.scrollTo(0, 0);
                    mHandler.postDelayed(this, 70);
//                    Thread.currentThread().interrupt();

                } else {
                    mHandler.postDelayed(this, 70);
                }
            }
        }
    };

    @Override
    public void onLayoutChange(View v, int left, int top, int right,
                               int bottom, int oldLeft, int oldTop, int oldRight,
                               int oldBottom) {
        mHandler.post(ScrollRunnable);
        System.out.println(bottom);
    }
}
