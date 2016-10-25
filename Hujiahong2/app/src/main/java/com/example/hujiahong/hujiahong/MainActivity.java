package com.example.hujiahong.hujiahong;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;

import java.sql.SQLException;
import java.util.List;

import VO.VersionVO;
import nf.framework.core.util.android.ExitDoubleClick;
import nf.framework.core.util.android.PhoneInfoUtils;
import utils.ImageManger;
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
    private ImageView home_bg;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.navigationBarLayout.setVisibility(View.GONE);
        super.top_textview.setText("医大帮Test");
        initView();
        //更新,好比要更新的版本号是1.1，现在是1.0
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            DataInterface.connect();
            String select = DataInterface.select("select version, url from appversion where name='donge'");
            Gson gson=new Gson();
            List<VersionVO>    v=gson.fromJson(select, new TypeToken<List<VersionVO>>(){}.getType());
            if (!PhoneInfoUtils.getAppVersionNum(this).equals(v.get(0).getVersion())) {

                UpDateManger.getInstanse(this).onBtnClick(v.get(0).getVersion(), v.get(0).getUrl());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
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
        home_bg = (ImageView) mainView.findViewById(R.id.home_bg);
        ImageManger.asyncLoadImage(home_bg,"http://182.254.130.173/app/upload/1.png");
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        if (view.equals(upButton)){
            IntentUtils.intentToVideoPlayerAct(this,"http://182.254.130.173/app/upload/1.mp4","凌云鹏主任手术视频",1210000);
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
