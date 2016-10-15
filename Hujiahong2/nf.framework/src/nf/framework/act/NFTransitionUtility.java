package nf.framework.act;

import nf.framework.R;
import android.app.Activity;
import android.content.Context;

public class NFTransitionUtility {

	
	/***
	 * 右侧推出效果 右往左推出效果
	 * 
	 * @param activity
	 */
	public static void RightPushInTrans(Context context) {
		if (context == null) {
			return;
		}
		if(context instanceof Activity){
		// 右往左推出效果
		((Activity)context).overridePendingTransition(R.anim.common_push_left_in,
				R.anim.common_push_left_out);
		
		}
	}

	/**
	 * 左侧推出 左向右推 finish
	 * 
	 * @param activity
	 */
	public static void LeftPushInTrans(Context context) {
		if (context == null) {
			return;
		}
		if(context instanceof Activity){
		// 左向右推
		((Activity)context).overridePendingTransition(R.anim.common_push_right_in,
				R.anim.common_push_right_out);
		}
	}
	
	/**
	 * 盒装收缩效果
	 * 
	 * @param activity
	 */
	public static void BoxZoomOutInTrans(Activity activity) {
		// 盒装收缩效果
		if (activity == null) {
			return;
		}
		activity.overridePendingTransition(R.anim.part_to_home_fade_in,
				R.anim.part_to_home_fade_out);

	}
	
	/**
	 * 下往上推出效果
	 * 
	 * @param activity
	 */
	public static void DownToTopInTrans(Activity activity) {
		// 下往上推出效果
		if (activity == null) {
			return;
		}
		activity.overridePendingTransition(R.anim.common_push_up_in,
				R.anim.common_push_up_out);
	}
	/**
	 * 上往下退出效果 finish
	 * @param activity
	 */
	public static void TopToDownInTrans(Activity activity) {
		if (activity == null) {
			return;
		}
		activity.overridePendingTransition(R.anim.common_push_down_in,
				R.anim.common_push_down_out);
	}
	
	/**
	 * '上下交错效果
	 * 
	 * @param activity
	 */
	public static void SlideUpInTrans(Activity activity) {
		// 上下交错效果
		if (activity == null) {
			return;
		}
		activity.overridePendingTransition(R.anim.common_slide_up_in,
				R.anim.common_slide_down_out);
		;
	}

}
