
package nf.framework.core.http;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

import nf.framework.core.exception.LogUtil;


public class ServerEngine {

	private static Thread sLinker = null;
	private static Object sMutex = new Object();
	
	private static Queue<AbsBaseRequestData> mRequestQueue = new LinkedList<AbsBaseRequestData>();

	private ServerEngine() { }
	private static ServerEngine INSTANCE = new ServerEngine();
	public static ServerEngine getInstance() {
		return INSTANCE;
	}
	public void request(AbsBaseRequestData data) {
		synchronized (sMutex) {
			if (null == sLinker) {
				sLinker = new Thread() {
					public void run() {
						runRequest();
					}
				};
				sLinker.start();
			}
		}
		try {
			LogUtil.e("hjh ==mRequestQueue.offer(data)=",""+data);
			boolean offer = mRequestQueue.offer(data);
			LogUtil.e("hjh offer==>",offer+"");
			LogUtil.e("hjh mRequestQueue.size()==>",""+mRequestQueue.size());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void cancel(AbsBaseRequestData data) {
		try {
			mRequestQueue.remove(data);
		}
		catch (Exception e) {
			
		}
	}
	
	public void cancelAll() {
		try {
			mRequestQueue.clear();
		}
		catch (Exception e) {
		}
	}
	
	public void stop() {
		synchronized (sMutex) {
			try {
				sLinker.interrupt();
				sLinker = null;
			}
			catch (Exception e) {
				e.getStackTrace();
			}
		}
	}
	
	private void runRequest() {
		try {
			while (true) {
				if (mRequestQueue.size() > 0) {
					try {
						AbsBaseRequestData data = mRequestQueue.poll();
						/*final BaseRequestData data1 = mRequestQueue.poll();
						if (data != null && data1 != null) {
							new Thread() {
								public void run() {
									data1.run();
								}
							}.start();
							data.run();
						}
						else */if (data != null) {

							data.run();
						}
						/*else if (data1 != null) {
							data1.run();
						}*/
					} catch (NoSuchElementException e) {
						mRequestQueue.clear();
						stop();
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
						stop();
					}
				}
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			stop();
		}
	}
	
	//private static boolean mIsNetworkSetting = false;
	/*public static void showNetworkSetting(final Context context) {
		//if (!(mContext instanceof Activity)) {
		//	return;
		//}
		if (mIsNetworkSetting) {
			return;
		}
		try {
			new AlertDialog.Builder(context)
			//.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle(R.string.network_failure_title)
			.setMessage(R.string.network_failure_content)
			.setPositiveButton(R.string.network_setting,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						mIsNetworkSetting = false;
						dialog.dismiss();
						context.startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
					}
				})
			.setNegativeButton(R.string.common_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int which) {
						mIsNetworkSetting = false;
						dialog.dismiss();
					}
				})
			.setOnCancelListener(new OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					mIsNetworkSetting = false;
				}
			})
			.show();
			mIsNetworkSetting = true;
		}
		catch (Exception e) {
			//e.printStackTrace();
			mIsNetworkSetting = false;
			WLog.d("NetworkSetting", e.getMessage());
		}
	}*/
	
	/*static boolean mStart = true;
	public static void init() {
		Thread th = new Thread() {
			public void run() {
				while (mStart) {
					try {
						openConnection();
						Thread.sleep(10*60*1000);
					}
					catch (Exception e) {
						HttpRequest.close();
						e.printStackTrace();
					}
				}
			}
		};
		th.start();
	}
	
	public static void stop() {
		HttpRequest.close();
		mStart = false;
	}
	
	static void openConnection() {
		HttpRequest.open("http://127.0.0.1:8080/test/index.jsp");
	}*/
}

