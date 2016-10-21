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

        initView();
    }

    private void initView() {

        super.navigationBarLayout.setVisibility(View.VISIBLE);
        super.top_textview.setText("部门职责");
        super.leftButton.setVisibility(View.VISIBLE);
        super.leftButton.setImageResource(R.drawable.play_ctrl_back_press_bg);
        super.leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
          IntentUtils.intentToShowResponsibilitiesActivity(this,"办公室","协助市院领导处理检察政务，组织协调市院重要工作部署、重大决策的贯彻实施，组织安排机关重要会议和重大活动；负责文件起草；管理秘书事务；处理检察信息，编发内部刊物；处理机要文电；负责人大代表联络工作和特约检察员的联系工作；负责领导同志批办事项的督查工作；负责机关日常值班工作；负责检察统计、档案管理、保密、外事、接待等工作；对下级人民检察院相关业务进行指导。");
        }
        else if(v.equals(zhengzhichu)){
            IntentUtils.intentToShowResponsibilitiesActivity(this,"政治处","协同市委主管部门和市辖市党委负责全市检察机关党的建设和队伍建设工作；开展具有检察特点的思想政治工作；协同市委主管部门和市辖市党委负责市辖市人民检察院领导班子的考核、配备及后备干部的考察工作，办理有关任免手续；协同市主管部门管理全市检察机关的机构编制；负责全市检察机关的检察官等级、司法警察警衔管理和专业技术职务资格评审以及表彰奖励工作；组织实施检察机关有关干部管理规定，指导全市检察机关的干部人事管理工作；负责院机关和事业单位的机构编制和人事管理工作；负责市人民检察院的宣传工作以及对全市检察机关宣传工作的指导；组织指导全市检察机关干部教育培训工作，规划和指导培训基地建设及师资队伍建设工作，指导市检察官培训学院的教学培训工作；负责市人民检察院检察官考评委员会的日常工作；负责拟订和落实全市检察机关基层基础建设规划和有关政策措施；组织协调指导全市检察机关基层基础建设工作；负责全市检察机关基层检察院和基层单位考核工作；指导全市检察机关的派驻基层检察室建设工作。 政治部内设人事处、宣传处、干部教育培训处、基层工作处。");
        }
        else if(v.equals(zhenchajianduke)){
            IntentUtils.intentToShowResponsibilitiesActivity(this,"侦查监督科","负责对全市刑事犯罪案件（包括人民检察院直接受理侦查的贪污贿赂、国家工作人员渎职等犯罪案件）犯罪嫌疑人的审查批捕、决定逮捕和立案监督、侦查活动监督工作以及其他相关工作的指导；承办应当由市人民检察院办理的审查批捕、决定逮捕案件和复议、复核案件；承担应当由市人民检察院负责的立案监督、侦查活动监督工作；审查批准延长侦查羁押期限案件；承办下级人民检察院侦查监督工作中重大疑难个案的请示；掌握全市社会治安动态情况，参与处置重大突发事件；研究制定全市侦查监督检察业务工作计划、规定和办法。");
        }
        else if(v.equals(gongsuke)){
            IntentUtils.intentToShowResponsibilitiesActivity(this,"公诉科","负责对全市刑事犯罪案件（包括人民检察院直接受理侦查的贪污贿赂、国家工作人员渎职等犯罪案件）的审查起诉、出庭公诉、不起诉和抗诉工作的指导；负责对全市检察机关公诉部门开展侦查活动监督和刑事审判活动监督工作的指导；承办应当由市人民检察院审查起诉的刑事案件；对下一级人民检察院提出抗诉的刑事案件或市高级人民法院开庭审理的第一审和第二审公诉案件，审查及出庭履行职务；对市高级人民法院确有错误的第一审的刑事判决和裁定，依法向最高人民法院提出抗诉；对下级人民法院已经发生法律效力、确有错误的刑事判决和裁定，依法向市高级人民法院提出抗诉；对市高级人民法院已经发生法律效力、确有错误的刑事判决和裁定，提请最高人民检察院依法提出抗诉；承办下级人民检察院公诉工作中重大疑难复杂案件和疑难问题的请示；研究制定全市公诉业务工作计划、规定和办法。");
        }
        else if(v.equals(fenfantanwuhuluju)){
            IntentUtils.intentToShowResponsibilitiesActivity(this,"分反贪污贿赂局","负责对全市检察机关查办贪污贿赂、挪用公款、巨额财产来源不明、隐瞒境外存款、私分国有资产、私分罚没财物等职务犯罪案件侦查工作的指导；参与重大贪污贿赂等职务犯罪案件的侦查；直接立案侦查全市性重大贪污贿赂等职务犯罪案件；组织、协调、指挥重大及特大贪污贿赂等职务犯罪案件的侦查；负责重大、特大贪污贿赂等职务犯罪案件的侦查协作；研究分析全市贪污贿赂等职务犯罪的特点、规律，提出惩治对策；承办下级人民检察院反贪污贿赂工作中重大疑难案件和疑难问题的请示；研究制定全市反贪污贿赂检察业务工作计划、规定和办法；负责职务犯罪案件的网上初查、分析研判和线索管理；负责全市侦查信息共享机制的建设和管理；承担职务犯罪舆情收集、调查和控制工作；指导全市职务犯罪侦查信息工作。 反贪污贿赂局内设综合指导处、侦查处和职务犯罪大要案侦查指挥中心办公室、侦查信息处");
        }
        else if(v.equals(fanduzhiqinquanju)){
            IntentUtils.intentToShowResponsibilitiesActivity(this,"反渎职侵权局","负责对全市检察机关查办国家机关工作人员渎职犯罪和国家机关工作人员利用职权实施的非法拘禁、刑讯逼供、报复陷害、非法搜查的侵犯公民人身权利的犯罪以及侵犯公民民主权利的犯罪等案件侦查工作的指导；直接立案侦查全市性重大渎职侵权犯罪案件和最高人民检察院指定管辖的案件以及本院决定受理的国家机关工作人员利用职权实施的其他重大犯罪案件；参与和组织、指挥重特大渎职侵权犯罪案件的侦查以及跨地区案件的协查工作；调查分析全市渎职侵权犯罪的特点和规律，提出惩治和防范对策，结合办案，做好个案预防工作；承办下级人民检察院查办渎职侵权犯罪案件工作中重大疑难问题的请示；研究制定全市渎职侵权检察工作计划、规定和办法。");
        }
        else if(v.equals(xingshizhixingjianchaju)){
            IntentUtils.intentToShowResponsibilitiesActivity(this,"刑事执行检察局","负责对全市法院、公安机关和监狱、看守所、社区矫正机构等执行机关执行刑罚活动，减刑、假释、暂予监外执行等变更执行活动是否合法的监督工作的指导；负责由市高级人民法院承担的死刑缓期执行和无期徒刑罪犯的减刑开庭审理和裁定的检察监督工作；负责由市监狱管理局决定的暂予监外执行罪犯的检察监督工作；负责对全市公安机关监管被刑事拘留、逮捕和指定监视居住的犯罪嫌疑人、被告人的活动是否合法的监督工作，以及对超期羁押监督、久押不决案件清理和羁押必要性审查工作的指导；负责对全市强制医疗执行活动是否合法的监督工作的指导；负责对全市刑事执行中的职务犯罪案件立案侦查工作、预防工作的指导；负责对全市刑罚执行中发生的罪犯又犯罪案件审查逮捕、审查起诉工作和对立案、侦查和审判活动是否合法实行监督的指导；受理辩护人、诉讼代理人对全市刑事执行中阻碍行使诉讼权利的申诉、控告以及被刑事执行人及其近亲属、法定代理人的控告、举报和申诉。");
        }
        else if(v.equals(konggaoshensujianchake)){
            IntentUtils.intentToShowResponsibilitiesActivity(this,"控告申诉检察科","指导全市检察机关控告、申诉、举报和刑事赔偿工作；受理公民的报案、举报、控告、申诉和犯罪嫌疑人的自首；统一管理、分流举报线索，对性质不明、难以归口处理的案件线索进行初查；交办、督办有关案件线索；保护举报人合法权益，奖励举报有功人员；依法办理应当由市人民检察院管辖的刑事申诉案件，督促检查有关部门查报结果；受理刑事赔偿申请，承办应当由市人民检察院办理的刑事赔偿事项；综合反映、研究分析控告、申诉、举报和刑事赔偿工作情况；承办、答复下级人民检察院控告申诉检察工作中重大疑难案件、疑难问题的请示；研究制定全市控告申诉检察业务工作计划、规定和办法。");
        }
        else if(v.equals(minshixingzhengke)){
            IntentUtils.intentToShowResponsibilitiesActivity(this,"民事行政科","负责对全市民事检察工作的指导；对下级人民法院已经发生法律效力，确有错误的民事判决和裁定，按照审判监督程序，向市高级人民法院提出抗诉；对市高级人民法院已经发生法律效力，确有错误的民事判决和裁定，提请最高人民检察院按照审判监督程序提出抗诉，或者通过向市高级人民法院提出检察建议，启动再审程序；对市高级人民法院开庭审理的民事抗诉案件，出庭履行职务；指导和协调全市在办理民事申诉案件中发现的司法机关工作人员职务犯罪线索的管理、移送工作；研究分析全市民事检察工作的规律和特点，提出工作对策；研究制定全市民事检察工作业务计划、规定和办法；承办下级人民检察院民事检察工作中重大疑难案件和疑难问题的请示；统一负责全市民事行政检察综合工作，召集民事行政检察工作会议。");
        }
        else if(v.equals(jianchajishuke)){
            IntentUtils.intentToShowResponsibilitiesActivity(this,"检察技术科","负责对全市检察机关检察技术工作的指导；研究制定并组织实施全市检察技术工作发展规划；承担市人民检察院和下级人民检察院委托的重特大案件和疑难案件的现场勘查、文证审查、检验鉴定和技术协助工作；负责全市检察技术科研、学术交流及专业技术培训工作；负责全市检察系统检验鉴定机构、检验鉴定人的登记管理工作；研究制定全市检察技术业务工作计划、规定和办法。");
        }
        else if(v.equals(fajingju)){
            IntentUtils.intentToShowResponsibilitiesActivity(this,"法警局","负责对全市检察机关司法警察工作的指导；组织落实全市检察机关司法警察队伍建设的规划和措施；制定全市检察机关司法警察管理的规章制度并组织实施；检查监督司法警察执行法律、法规的情况；组织司法警察的教育培训；协助政工部门做好司法警察警衔的评定、调整工作；管理司法警察的警用装备；协调、指挥跨区域的重大警务活动；负责市院直接查处大要案的警务保障；负责市院门卫、大厅值班和机关安全保卫工作；完成检察长交办的其他任务。");
        }
    }
}
