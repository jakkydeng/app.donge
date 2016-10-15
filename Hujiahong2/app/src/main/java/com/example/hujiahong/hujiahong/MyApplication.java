package com.example.hujiahong.hujiahong;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.sina.sinavideo.coreplayer.splayer.SPlayer;
import com.sina.sinavideo.sdk.utils.VDApplication;
import nf.framework.app.UILApplication;
import nf.framework.core.exception.LogUtil;

public class MyApplication extends UILApplication {
	
	private static  Context mcontext=null;
	private static String currentTopic;


	@Override
	public void onCreate() {
		super.onCreate();
		mcontext =this;
		LogUtil.init(this);
		
	    VDApplication.getInstance().setContext(this);
		SPlayer.initialize(this);

	}
	
	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}

	public static Context getInstance(){
		return mcontext;
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
	}

	
	public  static void setCurrentTopic(String topic){
		currentTopic=topic;
		LogUtil.w(getInstance(), "当前聊天主题为:::::"+ currentTopic );
	}
	
	public static String getCurrentTopic(){
		
		return currentTopic;
	}
	
	@Override
	public String getAppName() {
		return null;
	}

	@Override
	public String getCurrentUserId() {
		return null;
	}

	@Override
	public String getCurrentUserName() {
		return null;
	}

	@Override
	public boolean isOpenBug() {
		return true;
	}



}
