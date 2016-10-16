package com.example.hujiahong.hujiahong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import utils.IntentUtils;

/**
 * 活动展示
 * Created by hujiahong on 16/10/16.
 */

public class ActivityShowActivity extends MyBaseActivity implements View.OnClickListener{
    private Button up;
    private Button bellow;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }
    private void  initView() {
        super.navigationBarLayout.setVisibility(View.VISIBLE);
        super.top_textview.setText("活动展示");
        super.leftButton.setVisibility(View.VISIBLE);
        super.leftButton.setImageResource(R.drawable.play_ctrl_back_press_bg);
        super.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        View mainView = LayoutInflater.from(this).inflate(R.layout.activity_show_activity_list, super.mainlayout, false);
        super.mainlayout.addView(mainView);
        up = (Button) mainView.findViewById(R.id.activity_show_up);
        bellow = (Button) mainView.findViewById(R.id.activity_show_bellow);
        up.setOnClickListener(this);
        bellow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
   if (v.equals(up)){
       IntentUtils.intentToActivitydetailsActivity(this,"两学一做");
   }else if (v.equals(bellow)){
       IntentUtils.intentToActivitydetailsActivity(this,"规范司法行为专项整治工作");
   }

    }
}
