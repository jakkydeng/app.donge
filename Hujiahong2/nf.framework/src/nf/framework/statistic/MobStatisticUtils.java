package nf.framework.statistic;

import android.app.Activity;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;


import java.util.HashMap;

/***
 * 统计事件 razor
 * @author hujiahong
 *
 */
public class MobStatisticUtils {

	private Activity activity;
	public MobStatisticUtils(Activity activity){
		
		this.activity= activity;
	}
	
	public void onStatisticResume(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				MobclickAgent.onResume(activity);
			}
		}).start();
		//开始引入统计Act

	}
	
	public void onStatisticPause(){
		new Thread(new Runnable() {
			@Override
			public void run() {
				MobclickAgent.onPause(activity);
			}
		}).start();
		//结束引入统计


		
	}
	
	public void onStaFragmentResume(final String pageName){
		new Thread(new Runnable() {
			@Override
			public void run() {
				MobclickAgent.onPageStart(pageName);
			}
		}).start();
		//开始引入统计Fragment

	}
	
	public void onStaFragmentPause(final String pageName){
		new Thread(new Runnable() {
			@Override
			public void run() {	
				MobclickAgent.onPageEnd(pageName);
			}
		}).start();
		//结束引入统计

	}
	
	public static void onEvent(final Context context,final String value){
		new Thread(new Runnable() {
			@Override
			public void run() {	
				MobclickAgent.onEvent(context, value);
			}
		}).start();

	}
	
	public static void onEvent(final Context context,final String value,final String param){
		new Thread(new Runnable() {
			@Override
			public void run() {	
				MobclickAgent.onEvent(context, value, param);
			}
		}).start();

	}
	public static void onEvent(final Context context, final String value, final String param, final int parm_value) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MobclickAgent.onEvent(context, value, param, parm_value);
			}
		}).start();
	}

	public static void onEvent(final Context context, final String value, final String key, final String parm_value){
		new Thread(new Runnable() {
			@Override
			public void run() {
				HashMap<String ,String>map = new HashMap<String, String>();
				map.put(key,parm_value);
				MobclickAgent.onEvent(context, value, map);
			}
		}).start();


	}
}
