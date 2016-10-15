package nf.framework.act.lockscreen;

import android.app.Activity;
import android.os.Handler;

/**
 * @Company SINA
 *
 * @Copyright 2014-2015
 *
 * @author LIU ZHONG LEI
 *
 * @date 2014年8月8日 下午12:47:43
 *
 * @Version 1.0
 */
public class LockScreen implements LockInterface {
	private Activity mContext = null;
	private boolean isLockScreen = true;	
	private long lockDuration = 10*60*1000;
	
	private final static Handler mHandler = new Handler();
	private final LockRunnable lockRunnable = new LockRunnable();
	
	public LockScreen(Activity context){
		this.mContext = context;
	}
	
	@Override
	public void setLockScreen(boolean isLockScreen) {
		this.isLockScreen = isLockScreen;
	}

	@Override
	public void setLockDuration(long lockDuration) {
		this.lockDuration = lockDuration;
	}

	@Override
	public void startLockRunnable() {
		this.stopLockRunnable();
		if(this.isLockScreen){
			mHandler.postDelayed(lockRunnable, lockDuration);
		}
	}

	@Override
	public void stopLockRunnable() {
		mHandler.removeCallbacks(lockRunnable);
	}

	@Override
	public void onLock() {
		if(this.isLockScreen){
			//TODO 执行锁屏操作
		}
	}

	@Override
	public void onResume() {
		this.startLockRunnable();
		this.onLock();
	}

	@Override
	public void onPause() {		
		this.stopLockRunnable();
	}
	
	private final class LockRunnable implements Runnable{
		@Override
		public void run() {	
			onLock();
		} 	
    }
}
