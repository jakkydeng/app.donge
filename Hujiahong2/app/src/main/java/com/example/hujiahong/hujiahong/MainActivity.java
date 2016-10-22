package com.example.hujiahong.hujiahong;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import nf.framework.core.util.android.ExitDoubleClick;
import nf.framework.core.util.android.PhoneInfoUtils;
import utils.IntentUtils;
import utils.UpDateManger;

/**
 * 主菜单
 */
public class MainActivity extends MyBaseActivity implements View.OnClickListener {
    private LinearLayout upButton;
    private LinearLayout leftButton;
    private LinearLayout rightButton;
    private LinearLayout bottomButton;
    private LinearLayout bottomButton2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.navigationBarLayout.setVisibility(View.GONE);
        super.top_textview.setText("医大帮Test");
        initView();
        //更新,好比要更新的版本号是1.1，现在是1.0
        if (!PhoneInfoUtils.getAppVersionNum(this).equals("1.1")) {

            UpDateManger.getInstanse(this).onBtnClick("1.1", "http://zb-files-1.oss-cn-beijing.aliyuncs.com/com.zbsd.ydb.apk");
        }
        }
    private void  initView(){
        View mainView = LayoutInflater.from(this).inflate(R.layout.main_layout, super.mainlayout, false);
        super.mainlayout.addView(mainView);
       upButton = (LinearLayout) mainView.findViewById(R.id.up_button);
        leftButton = (LinearLayout) mainView.findViewById(R.id.left_button);
        rightButton = (LinearLayout) mainView.findViewById(R.id.right_button);
        bottomButton = (LinearLayout) mainView.findViewById(R.id.bottom_button);
        bottomButton2 = (LinearLayout) mainView.findViewById(R.id.bottom_button2);
        upButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        bottomButton.setOnClickListener(this);
        bottomButton2.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.equals(upButton)){
            IntentUtils.intentToVideoPlayerAct(this,"http://cdn.course1.mservice.cn/079/all/index.m3u8","凌云鹏主任手术视频",1210000);
        }else if (view.equals(leftButton)){
            IntentUtils.intentToLeadershipCareActivity(this);
        }
        else if (view.equals(rightButton)){
            IntentUtils. intentToFloordistributionActivity(this);
        }
        else if (view.equals(bottomButton)){
            IntentUtils.intentToDepartmentResponsibilitiesActivity(this);
        }
        else if (view.equals(bottomButton2)){
            IntentUtils.intentToActivityShowActivity(this);
        }
    }

    /**
     * 覆盖back键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ExitDoubleClick.getInstance(this).doDoubleClick(2000, ""); // 调用双击退出函数
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
