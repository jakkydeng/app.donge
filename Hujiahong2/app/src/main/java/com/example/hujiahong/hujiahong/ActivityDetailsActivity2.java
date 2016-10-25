package com.example.hujiahong.hujiahong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import utils.ImageManger;

/**
 * 活动详情描述
 * Created by hujiahong on 16/10/17.
 */

public class ActivityDetailsActivity2 extends MyBaseActivity {
    private ImageView Pic;
    private TextView title;
    private TextView content;
    private String top_text;
    private TextView normal_content2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        initView();
    }

    private void initView() {

        super.navigationBarLayout.setVisibility(View.VISIBLE);
        super.top_textview.setText(top_text);
        super.leftButton.setVisibility(View.VISIBLE);
        super.leftButton.setImageResource(R.drawable.play_ctrl_back_press_bg);
        super.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        View mainView = LayoutInflater.from(this).inflate(R.layout.activity_normal_layout, super.mainlayout, false);
        super.mainlayout.addView(mainView);
        Pic = (ImageView) mainView.findViewById(R.id.normal_Img);
        Pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ImageManger.asyncLoadImage(Pic, "http://58.57.165.71:5001/app/activity/2.jpg");
        title = (TextView) mainView.findViewById(R.id.normal_title);
        content = (TextView) mainView.findViewById(R.id.normal_content);
        title.setText("东阿县检察院开展案件质量评查活动\n" +
                "进一步规范司法行为");
        content.setText("根据聊城市检察机关规范司法行为专项整治对照检查阶段工作会议部署要求和《聊城市检察机关规范司法行为专项整治对照检查阶段工作实施方案》安排，东阿县检察院规范司法行为专项整治工作领导小组和案件管理中心于2015年6月10日下午共同组织开展了2015上半年案件质量评查活动，着力发挥案件质量评查在评估司法办案质量、发现和纠正不规范问题、促进司法规范化建设方面的重要作用。本次案件质量评查采取网上评查与案卷评查相结合的方法。网上评查重点评查案件办理程序、案卡信息填录、文书制作审批、办案流程操作等情况；案卷评查坚持问题导向，通过审阅案件卷宗及相关资料，评查证据采信、事实认定、法律适用、办案期限、文书制作和使用、诉讼权利保障等情况，同时将案卷材料与统一业务应用系统案卡、文书记录内容相对照，评查统一业务系统规范应用情况。遵照评查流程，逐案制作《案件质量评查表》，详细记录评查的内容和发现的问题，提出减分意见。本次评查活动前后进行近三小时，参评人员认真负责，共发现问题11个，并及时告知相关部门在第一时间进行整改。");
        normal_content2 = (TextView) mainView.findViewById(R.id.normal_content2);


    }

    public void getIntentData() {
        top_text = getIntent().getStringExtra("top_text");
    }
}
