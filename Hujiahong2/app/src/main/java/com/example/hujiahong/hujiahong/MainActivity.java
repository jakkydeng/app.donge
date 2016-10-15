package com.example.hujiahong.hujiahong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

/**
 * 主菜单
 */
public class MainActivity extends MyBaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.navigationBarLayout.setVisibility(View.VISIBLE);
        super.top_textview.setText("医大帮Test");
        initView();
    }
    private void  initView(){
        View mainView = LayoutInflater.from(this).inflate(R.layout.main_layout, super.mainlayout, false);
        super.mainlayout.addView(mainView);
    }
}
