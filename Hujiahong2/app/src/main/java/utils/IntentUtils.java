package utils;

import android.content.Context;
import android.content.Intent;

import com.example.hujiahong.hujiahong.ActivityDetailsActivity;
import com.example.hujiahong.hujiahong.ActivityDetailsActivity2;
import com.example.hujiahong.hujiahong.ActivityShowActivity;
import com.example.hujiahong.hujiahong.DepartmentResponsibilitiesActivity;
import com.example.hujiahong.hujiahong.FloordistributionActivity;
import com.example.hujiahong.hujiahong.LeadershipCareActivity;
import com.example.hujiahong.hujiahong.MainActivity;
import com.example.hujiahong.hujiahong.PersonVideoActivity;
import com.example.hujiahong.hujiahong.ShowResponsibilitiesActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import VO.VDVideoInfoVO;
import nf.framework.act.browser.InnerBrowserActivity;
import nf.framework.core.util.android.CheckInternet;

/**
 * Created by hujiahong on 16/10/15.
 */

public class IntentUtils {
    /***
     * 视频播放(个人风采)
     * @param activity
     * @param videoPath
     * @param title
     * @param duration
     */
    public static void intentToVideoPlayerAct(Context activity, String videoPath, String title, int duration){
        List<VDVideoInfoVO> videoList = new ArrayList<VDVideoInfoVO>();
        VDVideoInfoVO videoInfo = new VDVideoInfoVO();
        videoInfo.setTitle(title);
        videoInfo.setUrl(videoPath);
        videoInfo.setDuration(duration);
        videoInfo.setLive(false);
        videoList.add(videoInfo);
        Intent intent = new Intent();
        intent.putExtra(PersonVideoActivity.INTENT_VideoList, (Serializable) videoList);
        intent.setClass(activity, PersonVideoActivity.class);
        activity.startActivity(intent);

    }
    /***
     * 图片轮播(领导关怀)
     * @param activity

     */
    public static void intentToLeadershipCareActivity(Context activity){

        Intent intent = new Intent();
        intent.setClass(activity, LeadershipCareActivity.class);
        activity.startActivity(intent);

    }
    /***
     * 图片轮播(领导关怀)
     * @param activity

     */
    public static void intentToMainActivity(Context activity){

        Intent intent = new Intent();
        intent.setClass(activity, MainActivity.class);
        activity.startActivity(intent);

    }
    /***
     * 楼层分布
     * @param activity

     */
    public static void intentToFloordistributionActivity(Context activity){

        Intent intent = new Intent();
        intent.setClass(activity, FloordistributionActivity.class);
        activity.startActivity(intent);

    }
    /***
     * 部门列表
     * @param activity

     */
    public static void intentToDepartmentResponsibilitiesActivity(Context activity){

        Intent intent = new Intent();
        intent.setClass(activity,DepartmentResponsibilitiesActivity.class);
        activity.startActivity(intent);

    }
    /***
     * 职责详情
     * @param activity

     */
    public static void intentToShowResponsibilitiesActivity(Context activity,String key,String discrib){

        Intent intent = new Intent();
        intent.setClass(activity,ShowResponsibilitiesActivity.class);
        intent.putExtra("key",key);
        intent.putExtra("dis",discrib);
        activity.startActivity(intent);

    }
    /***
     * 活动展示
     * @param activity

     */
    public static void intentToActivityShowActivity(Context activity){

        Intent intent = new Intent();
        intent.setClass(activity,ActivityShowActivity.class);
        activity.startActivity(intent);

    }


    public static void intentToInnerBrowserAct(Context activity,String source,String title,String url,boolean isNeedToken){
        if(!CheckInternet.checkInternet(activity)){

            return;
        }

        intentToInnerBrowserActEx(activity, source, title, url);
        TransitionUtility.RightPushInTrans(activity);
    }
    public static void intentToInnerBrowserActEx(Context context,String source,String title,String url) {

        Intent intent=new Intent();
        intent.setClass(context, InnerBrowserActivity.class);
        intent.putExtra(InnerBrowserActivity.INTENT_SOURCE, source);
        intent.putExtra(InnerBrowserActivity.INTENT_TITLE,title);
        intent.putExtra(InnerBrowserActivity.INTENT_URL, url);
        context.startActivity(intent);

    }

    /***
     * 活动详情
     * @param activity

     */
    public static void intentToActivitydetailsActivity(Context activity,String string){

        Intent intent = new Intent();
        intent.setClass(activity,ActivityDetailsActivity.class);
        intent.putExtra("top_text",string);
        activity.startActivity(intent);

    }
    /***
     * 活动详情
     * @param activity

     */
    public static void intentToActivitydetailsActivity2(Context activity,String string){

        Intent intent = new Intent();
        intent.setClass(activity,ActivityDetailsActivity2.class);
        intent.putExtra("top_text",string);
        activity.startActivity(intent);

    }
}
