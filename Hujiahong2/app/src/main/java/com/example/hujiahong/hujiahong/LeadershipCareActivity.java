package com.example.hujiahong.hujiahong;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import utils.ImageManger;

/**
 * 领导关怀的滚播图
 * Created by hujiahong on 16/10/16.
 */

public class LeadershipCareActivity extends MyBaseActivity implements View.OnLayoutChangeListener {


    private LinearLayout layout;
    private ScrollView namescroll;
    private final Handler mHandler = new Handler();

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
        for (int i = 0; i < 10; i++) {
            searchResultShow();
            System.out.println(i + "结束");
        }
        Log.d("...........", "" + layout.getHeight());
        System.out.println(namescroll.getScrollY());
        System.out.println("真正结束");
    }

    private void searchResultShow() {

/**
 * 动态布局
 */
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );


        ImageView imageView = new ImageView(LeadershipCareActivity.this);
        imageView.setImageResource(R.drawable.ic_launcher);
        ImageManger.asyncLoadImage(imageView, "http://uploads.yjbys.com/allimg/201609/3958-1609101IJ4462.jpg");
        //增加一个ImageView到线性布局中
        layout.addView(imageView, p);
        //  namescroll.addOnLayoutChangeListener(this);

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
                    mHandler.postDelayed(this, 30);
//                    Thread.currentThread().interrupt();

                } else {
                    mHandler.postDelayed(this, 30);
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
