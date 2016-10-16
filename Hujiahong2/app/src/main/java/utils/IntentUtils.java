package utils;

import android.content.Context;
import android.content.Intent;

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
    public static void intentToShowResponsibilitiesActivity(Context activity,String s){

        Intent intent = new Intent();
        intent.setClass(activity,ShowResponsibilitiesActivity.class);
        intent.putExtra("key",s);
        activity.startActivity(intent);

    }
}
