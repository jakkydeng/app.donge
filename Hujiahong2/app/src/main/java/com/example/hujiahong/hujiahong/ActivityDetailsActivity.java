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
        ImageManger.asyncLoadImage(Pic, "http://www.legaldaily.com.cn/zfzz/images/attachement/png/site4/20161018/d43d7ecb6d041970399f1d.png");
        title = (TextView) mainView.findViewById(R.id.normal_title);
        content = (TextView) mainView.findViewById(R.id.normal_content);
        title.setText("两学一做");
        content.setText("（通讯员   刘洋）为进一步深入贯彻落实“两学一做”学习教育，营造浓厚的活动氛围，激发全体干警爱党、爱国、爱检察事业的热情，增强检察干警的职业自豪感和社会责任感，使全体干警补足“精气之钙”，提升“信仰之修”，充分展示新时代检察官的风采。10月17日上午，陕西省宝鸡市凤县人民检察院举办了“两学一做”学习教育主题演讲活动。" +

                "来自全院各个科室的6名青年参赛选手，紧紧围绕“两学一做”主题，从自己的本职工作出发，以身边模范的人和事为素材，结合检察工作实际，从不同角度、不同岗位集中宣扬党规党章党纪和党员的先锋模范作用，以鲜明的立场，鲜活的事例为代表，作为检察机关开展“两学一做”活动扬帆起航。选手们以朴素的语言和真挚的情感，通过富有感染力的演讲，充分展示了凤县检察院青年检察干警朝气蓬勃、昂扬向上的精神风貌，赢得了评委和干警们的阵阵掌声，为大家奉献了一场精彩演讲。演讲结束后，6名评委对参赛选手的表现依次打分，评选出一二三等奖，并由院党领导为获奖选手颁发了表彰证书" +

                "最后，代检察长邓武同志最后发表总结讲话，对本次活动给予了高度评价，并表示以后要继续开展各类演讲、朗诵等活动，全院人人参与、领导率先垂范，真正把该院这只优秀的队伍培养好、锤炼好。");



    }

    public void getIntentData() {
        top_text = getIntent().getStringExtra("top_text");
    }
}
