package com.example.hujiahong.hujiahong;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import nf.framework.core.util.android.ExitDoubleClick;
import utils.IntentUtils;

/**
 * 主菜单
 */
public class MainActivity extends MyBaseActivity implements View.OnClickListener {
    private ImageView upButton;
    private ImageView leftButton;
    private ImageView rightButton;
    private ImageView bottomButton;
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
       upButton = (ImageView) mainView.findViewById(R.id.up_button);
        leftButton = (ImageView) mainView.findViewById(R.id.left_button);
        rightButton = (ImageView) mainView.findViewById(R.id.right_button);
        bottomButton = (ImageView) mainView.findViewById(R.id.bottom_button);
        upButton.setOnClickListener(this);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);
        bottomButton.setOnClickListener(this);
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.equals(upButton)){
            IntentUtils.intentToVideoPlayerAct(this,"","",5000);
        }else if (view.equals(leftButton)){

        }
        else if (view.equals(rightButton)){

        }
        else if (view.equals(bottomButton)){

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
