package nf.framework.act.lockscreen;

/**
 * @Company SINA
 *
 * @Copyright 2014-2015
 *
 * @author LIU ZHONG LEI
 *
 * @date 2014年8月8日 下午12:39:09
 *
 * @Version 1.0
 */
public interface LockInterface {
	
	/**
	 * 是否执行锁屏
	 * @param isLockScreen
	 */
	public void setLockScreen(boolean isLockScreen);
	
	/**
	 * 设置锁屏间隔
	 * @param lockDuration
	 */
	public void setLockDuration(long lockDuration);
	
	/**
	 * 执行锁屏事件
	 */
	public void startLockRunnable();
	
	/**
	 * 停止锁屏事件
	 */
	public void stopLockRunnable();
	
	/**
	 * 锁屏时的操作
	 */
	public void onLock();
	
	/**
	 * 活动进Resume状态时，调用
	 */
	public void onResume();
	
	/**
	 * 活动进Pause状态时，调用
	 */
	public void onPause();
}
