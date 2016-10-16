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
        ImageManger.asyncLoadImage(Pic, "https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1476623324&di=07e684acd765160ff974b74559e71d34&src=http://pic9.nipic.com/20100827/5608174_220855012122_2.jpg");
        title = (TextView) mainView.findViewById(R.id.normal_title);
        content = (TextView) mainView.findViewById(R.id.normal_content);
        title.setText("我是一个标题啊啊啊啊啊啊");
        content.setText("负责对全省检察机关司法警察工作的指导；组织落实全省检察机关司法警察队伍建设的规划和措施；制定全省检察机关司法警察管理的规章制度并组织实施；检查监督司法警察执行法律、法规的情况；组织司法警察的教育培训；协助政工部门做好司法警察警衔的评定、调整工作；管理司法警察的警用装备；协调、指挥跨区域的重大警务活动；负责省院直接查处大要案的警务保障；负责省院门卫、大厅值班和机关安全保卫工作；完成检察长交办的其他任务。");



    }

    public void getIntentData() {
        top_text = getIntent().getStringExtra("top_text");
    }
}
