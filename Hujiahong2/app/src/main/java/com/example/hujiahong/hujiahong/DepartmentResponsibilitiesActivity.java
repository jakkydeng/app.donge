package com.example.hujiahong.hujiahong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import utils.IntentUtils;

/**
 * 部门列表
 * Created by hujiahong on 16/10/16.
 */

public class DepartmentResponsibilitiesActivity extends MyBaseActivity implements View.OnClickListener{
    private LinearLayout bangongshi;
    private LinearLayout zhengzhichu;
    private LinearLayout zhenchajianduke;
    private LinearLayout gongsuke;
    private LinearLayout fenfantanwuhuluju;
    private LinearLayout fanduzhiqinquanju;
    private LinearLayout xingshizhixingjianchaju;
    private LinearLayout konggaoshensujianchake;
    private LinearLayout minshixingzhengke;
    private LinearLayout jianchajishuke;
    private LinearLayout fajingju;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.navigationBarLayout.setVisibility(View.VISIBLE);
        super.top_textview.setText("部门列表");
        initView();
    }

    private void initView() {
        View mainView = LayoutInflater.from(this).inflate(R.layout.responsibilities, super.mainlayout, false);
        super.mainlayout.addView(mainView);
        bangongshi = (LinearLayout) mainView.findViewById(R.id.bangongshi);
        zhengzhichu = (LinearLayout) mainView.findViewById(R.id.zhengzhichu);
        zhenchajianduke = (LinearLayout) mainView.findViewById(R.id.zhenchajianduke);
        gongsuke = (LinearLayout) mainView.findViewById(R.id.gongsuke);
        fenfantanwuhuluju = (LinearLayout) mainView.findViewById(R.id.fenfantanwuhuluju);
        fanduzhiqinquanju = (LinearLayout) mainView.findViewById(R.id.fanduzhiqinquanju);
        xingshizhixingjianchaju = (LinearLayout) mainView.findViewById(R.id.xingshizhixingjianchaju);
        konggaoshensujianchake = (LinearLayout) mainView.findViewById(R.id.konggaoshensujianchake);
        minshixingzhengke = (LinearLayout) mainView.findViewById(R.id.minshixingzhengke);
        jianchajishuke = (LinearLayout) mainView.findViewById(R.id.jianchajishuke);
        fajingju = (LinearLayout) mainView.findViewById(R.id.fajingju);

        bangongshi.setOnClickListener(this);
        zhengzhichu.setOnClickListener(this);
        zhenchajianduke.setOnClickListener(this);
        gongsuke.setOnClickListener(this);
        fenfantanwuhuluju.setOnClickListener(this);
        fanduzhiqinquanju.setOnClickListener(this);
        xingshizhixingjianchaju.setOnClickListener(this);
        konggaoshensujianchake.setOnClickListener(this);
        minshixingzhengke.setOnClickListener(this);
        jianchajishuke.setOnClickListener(this);
        fajingju.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(bangongshi)){
          IntentUtils.intentToShowResponsibilitiesActivity(this,"bangongshi");
        }
        else if(v.equals(zhengzhichu)){

        }
    }
}
