package nf.framework.core;

import nf.framework.core.exception.CrashHandler;
import nf.framework.core.exception.LogUtil;
import nf.framework.core.exception.CrashHandler.CrashMessageCallBack;
import android.app.Application;
import android.util.Log;

/**
 * 用来控制所有application中的activity
 * @author niufei
 *
 */
public abstract class AbsApplication extends Application { 
	
	@Override
	public void onCreate() {
		super.onCreate();
		LogUtil.init(getApplicationContext());
		if(!isOpenBug()){
			CrashHandler crashHandler = CrashHandler.getInstance();
			//注册crashHandler
			crashHandler.init(getApplicationContext());
			//发送以前没发送的报告(可选)
			crashHandler.sendPreviousReportsToServer(getCrashMessageCallBack());
		}
	}
	public abstract boolean isOpenBug();
	public abstract CrashMessageCallBack getCrashMessageCallBack();
	/**
	 * @return
	 *
	 * niufei
	 *
	 * 2014-6-5 下午4:48:30
	 */
	public abstract  String getAppName();
	
	public abstract String getCurrentUserId();
	
	public abstract String getCurrentUserName();
	@Override  
    public void onTerminate(){  
        super.onTerminate();  
    }  
  
    public void onConfigurationChanged(){  
        Log.e("ConstantApplication","onConfigurationChanged");  
    }  
}
