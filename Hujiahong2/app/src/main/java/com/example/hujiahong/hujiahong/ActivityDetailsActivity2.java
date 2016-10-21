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
        ImageManger.asyncLoadImage(Pic, "http://img2.imgtn.bdimg.com/it/u=889217175,808021007&fm=21&gp=0.jpg");
        title = (TextView) mainView.findViewById(R.id.normal_title);
        content = (TextView) mainView.findViewById(R.id.normal_content);
        title.setText("开展规范司法行为专项整治活动");
        content.setText("为进一步加强司法规范化建设，提高检察机关严格规范、公正文明司法的水平，今年年初以来，陕西省宁强县检察院按照高检、省市院部署，紧密结合实际，认真制定工作方案，深入开展规范司法行为专项整治活动。\n" +
                "    准确把握和领会高检院、省市院专项整治工作会议精神，深入进行动员部署。该院召开专题动员大全，安排部署相关工作，引导全体检察人员深刻认识规范司法行为专项整治活动的重要性和现实紧迫性。要求检察干警把思想和行动统一到高检院和院机关的部署要求上来，增强政治敏锐性，深刻认识规范司法行为对于加强检察机关自身建设、提升司法公信力的重要意义，积极适应形势发展，转变执法理念，坚持和发挥司法公正对社会公正的引领、保障作用，通过扎实开展专项整治活动，切实解决检察工作中存在的执法不规范问题，使检察工作达到中央依法治国的新要求。\n" +
                "    大力开展正确的司法理念教育，以树立正确司法理念为先导，把忠诚教育、理想信念教育和社会主义法治理念教育贯穿活动始终，坚定理想信念，强化法治思维，坚守职业良知，引导检察人员坚持党的事业、人民利益、宪法法律至上，始终做到惩治犯罪与保障人权并重、程序公正与实体公正并重、全面客观收集审查证据与坚决依法排除非法证据并重、司法公正与司法效率并重、强化法律监督与强化自身监督并重、严格公正廉洁司法与理性平和文明规范司法并重，严守职业道德和权力边界，夯实规范司法、公正司法的思想根基。\n" +
                "    加强执法规范化建设。以执法办案一线人员为重点，坚持问题导向，全面排查各个执法办案岗位、环节存在的执法不规范问题，综合人大代表审议意见、基层院建设抽样评估反馈意见、案件质量评查发现的问题和人民群众反映强烈的突出问题，从自身司法办案和履行监督职责两个方面入手，切实把自身司法不严格、不规范、不公正、不廉洁的问题找准。特别针对基层院建设抽样评估中发现的重点问题，进行坚决整治和重点防范，认真开展专项整治和持续治理工作，同时，切实加大办案力度，让人民群众看到实实在在的成效。\n" +
                "    抓好案件检查评查工作。该院把案件检查评查作为加强案件质量监管的重要手段和促进规范化建设的重要内容。在此次专项整治活动中，该院案件管理办公室组织各业务部门开展案件评查，从制定好案件评查方案入手，对评查结果进行书面通报，各部门针对评查中的问题落实整改。\n" +
                "    加强作风纪律建设。把“监督者更要接受监督”的理念落到实处，深化检务公开，加强检务监察，对违规违纪办案的检察人员，一经发现，坚决查处。\n" +
                "    强化职业素能建设。引导干警树立终身学习观念，通过学习解决能力不足的问题，把十八届四中全会、习近平总书记系列讲话、重要法律法规作为应知应会的内容，把修改后的刑事诉讼法、民事诉讼法、行政诉讼法作为规范司法行为的重点，严格依照法律规定的权限、程序履行职责、行使权力。专项整治活动中，将对干警进行素能测试。\n" +
                "    更加重视规范司法行为的制度建设和制度落实工作。该院要求各业务部门积极探索本部门规范司法的制度机制，用制度规范执法办案行为，狠抓规范司法的制度落实工作，用制度约束权力，规范权力运行，不折不扣地落实曹建明检察长在讲话中提出的“四条硬措施”，对重点工作和突出问题一定要问责问效，真正解决问题。（郑力铭）");



    }

    public void getIntentData() {
        top_text = getIntent().getStringExtra("top_text");
    }
}
