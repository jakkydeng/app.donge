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

public class ActivityDetailsActivity extends MyBaseActivity {
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
        ImageManger.asyncLoadImage(Pic, "http://58.57.165.71:5001/app/activity/1.jpg");
        title = (TextView) mainView.findViewById(R.id.normal_title);
        content = (TextView) mainView.findViewById(R.id.normal_content);
        title.setText("两学一做");
        title.setVisibility(View.GONE);
        content.setText("按照我院“两学一做”学习教育实施方案的安排，6月28日上午，院党组书记、检察长孙吉祥为全院党员干警上了一堂专题党课。\n" +
                "党课讲授中，孙吉祥检察长首先结合自己对“两学一做”学习教育内涵和要求的认识，讲授了重点需要把握的几个方面。同时围绕党章党规和习近平总书记系列重要讲话精神，就落实党中央和习总书记关于全面从严治党和做合格党员的要求，畅谈了认识和体会，为全院党员干警上了一堂理论性、针对性和教育性兼备的党课，进一步提升了全体干警的思想认识。\n" +
                "孙吉祥检察长强调，希望每位党员干警都能通过这次专题党课学习，自觉联系自身思想实际和工作实际，细心领会，深入思考，与时俱进，融会贯通，学以致用。\n" +
                "通过专题党课学习，干警们纷纷表态，不仅会通过实际行动不断强化自身党性修养，更会把党员身份和检察干警身份所赋予的职责做得更好，履行得更为完善，为东阿检察发展贡献力量。");

        normal_content2 = (TextView) mainView.findViewById(R.id.normal_content2);
        normal_content2.setVisibility(View.GONE);

    }

    public void getIntentData() {
        top_text = getIntent().getStringExtra("top_text");
    }
}
