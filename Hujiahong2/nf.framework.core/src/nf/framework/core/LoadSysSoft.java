/**
 * LoadSysSoft.java
 * 功能：调用系统软件方法 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-5-11       上午11:26:58
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

package nf.framework.core;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nf.framework.core.exception.LogUtil;
import nf.framework.core.exception.NFRuntimeException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.provider.Settings;
import android.widget.Toast;

public class LoadSysSoft {

	/*** 相册中获取照片 ***/
	public static final int REQUEST_CODE_PICK_IMAGE = 1;
	/**** 拍照获取照片 ****/
	public static final int REQUEST_CODE_CAPTURE_CAMEIA = 2;
	/******* 拍照照片存放路径 ***********/
	public static final String CAPTURE_CAMERA_PIC_PATH = "capture_camera_pic_path";

	/**
	 * 使用系统邮件发送邮件
	 * 
	 * @param context
	 * @param sendContent
	 *            发送内容
	 * @param subject
	 *            邮件主题
	 * @param emailReciver
	 *            邮件接收者
	 * @param type
	 *            邮件类型
	 */
	public void sendInfoByEmail(Context context, String sendContent,
			String subject, String[] emailReciver, String type) {
		// 系统邮件系统的动作为android.content.Intent.ACTION_SEND
		Intent email = new Intent(android.content.Intent.ACTION_SEND);
		if (type == null) {
			type = "plain/text";
		}
		email.setType(type);
		// 设置邮件默认地址
		if (emailReciver != null && emailReciver.length != 0) {
			email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
		}
		// 设置邮件默认标题
		email.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		// 设置要默认发送的内容
		email.putExtra(android.content.Intent.EXTRA_TEXT, sendContent);
		// 调用系统的邮件系统
		context.startActivity(Intent.createChooser(email, "请选择邮件发送软件"));
	}

	/**
	 * 使用系统邮件发送邮件
	 * 
	 * @param context
	 * @param sendContent
	 *            发送内容
	 * @param subject
	 *            邮件主题
	 * @param emailReciver
	 *            邮件接收者
	 * @param type
	 *            邮件类型
	 * 
	 * @param attachFilePath
	 *            "file:///sdcard/mysong.mp3"
	 */
	public void sendInfoByEmail(Context context, String sendContent,
			String subject, String[] emailReciver, String type, Bitmap bitmap) {
		// 系统邮件系统的动作为android.content.Intent.ACTION_SEND
		Intent email = new Intent(android.content.Intent.ACTION_SEND);

		// 设置邮件默认地址
		if (emailReciver != null && emailReciver.length != 0) {
			email.putExtra(android.content.Intent.EXTRA_EMAIL, emailReciver);
		}
		if (bitmap != null) {
			type = "image/*";
			email.setType("message/rfc882");
			// Uri uri = Uri.parse("file://"+attachFilePath);
			Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(
					context.getContentResolver(), bitmap, null, null));
			email.putExtra(Intent.EXTRA_STREAM, uri);
		}
		email.setType(type == null ? "plain/text" : type);// 默认为文本
		// 设置邮件默认标题
		email.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
		// 设置要默认发送的内容
		email.putExtra(android.content.Intent.EXTRA_TEXT, sendContent);
		// 调用系统的邮件系统
		context.startActivity(Intent.createChooser(email, "请选择邮件发送软件"));

		bitmap = null;
	}

	/**
	 * 调用多媒体播放器
	 * 
	 * @param context
	 * @param videoUrl
	 */
	public void OpenVideo(Context context, String videoUrl) {
		Intent it = new Intent(Intent.ACTION_VIEW);
		Uri uri = Uri.parse(videoUrl);
		String temp = getVideoFomat(videoUrl);
		if (temp.equalsIgnoreCase("MP3")) {
			it.setDataAndType(uri, "audio/mp3");
		} else {
			it.setDataAndType(uri, "video/mp4");
		}
		context.startActivity(it);
	}

	/**
	 * 判断文件类型
	 * 
	 * @param videoUrl
	 * @return
	 */
	private String getVideoFomat(String videoUrl) {
		String tmpName = null;
		if (videoUrl != null) {
			tmpName = videoUrl.substring(videoUrl.lastIndexOf(".") + 1,
					videoUrl.length());
		}
		return tmpName;
	}

	/**
	 * 打开浏览器
	 * 
	 * @param context
	 * @param videoUrl
	 */
	public void OpenBrowser(Context context, String url) {
		if (url != null) {
			try {
				if (!url.startsWith("http://")) {
					url = "http://" + url;
				}
				Uri uri = Uri.parse(url);
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(it);
			} catch (Exception e) {
				Toast.makeText(context, "您未安装任何浏览器", Toast.LENGTH_SHORT).show();
			}
		}
	}

	/**
	 * 使用短信发送信息
	 * 
	 * @param context
	 * @param teleNum
	 * @param sendContent
	 * @param requestCode
	 */
	public void SendInfoByMessage(Context context, String teleNum,
			String sendContent, int requestCode) {
		try {
			Uri smsToUri = null;
			if (teleNum == null) {
				smsToUri = Uri.parse("smsto:");
			} else {
				smsToUri = Uri.parse("smsto:" + teleNum);
			}
			Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO,
					smsToUri);
			mIntent.putExtra("sms_body", sendContent);
			((Activity) context).startActivityForResult(mIntent, requestCode);
		} catch (Exception e) {
			Toast.makeText(context, "短信功能调用失败", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 启用拨打电话软件
	 * 
	 * @param context
	 * @param telphone
	 */
	public void OpenCallPhone(Context context, String telphone) {
		Intent intent = new Intent();
		// 系统默认的action，用来打开默认的电话界面
		intent.setAction(Intent.ACTION_CALL);
		// 需要拨打的号码
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
		intent.setData(Uri.parse("tel:" + telphone));
		context.startActivity(intent);
	}

	/**
	 * 调用手机中存在的市场
	 */
	public void OpenMarketApp(Context context, String appPackage) {
		try {
			Intent in = new Intent(Intent.ACTION_VIEW);
			in.setData(Uri.parse("market://details?id=" + appPackage));
			context.startActivity(in);
		} catch (Exception e) {
			Toast.makeText(context, "您未安装任何应用市场", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 安裝apk
	 * 
	 * @param file
	 */
	public void defalutInstall(Activity activity, String filepath) {
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.fromFile(new File(filepath)),
				"application/vnd.android.package-archive");
		activity.startActivity(i);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * 设置飞行模式状态
	 * 
	 * @param setAirPlane
	 */
	protected void setAirPlane(Context context, boolean setAirPlane) {
		Settings.System.putInt(context.getContentResolver(),
				Settings.System.AIRPLANE_MODE_ON, setAirPlane ? 1 : 0);
		// 等同于
		// Settings.System.AIRPLANE_MODE_ON, setAirPlane);
		Intent intent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
		intent.putExtra("state", setAirPlane ? 1 : 0);
		// 等同于
		// intent.putExtra("state", setAirPlane);
		context.sendBroadcast(intent);
	}

	/**
	 * 实现打开另一个应用程序的功能
	 * 
	 * @param mcontext
	 * @param packageName
	 */
	public void openAPP(Context mcontext, String packageName) {

		try {
			PackageManager packageManager = mcontext.getPackageManager();
			Intent intent = new Intent();
			intent = packageManager.getLaunchIntentForPackage(packageName);
			mcontext.startActivity(intent);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(mcontext, "请确定安装了此应用", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 是否应用在前台运行
	 */
	public static boolean isRunningApp(Context context, String packageName) {
		boolean isAppRunning = false;
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		for (RunningTaskInfo info : list) {
			if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
				isAppRunning = true;
				// find it, break
				break;
			}
		}
		return isAppRunning;
	}
	/**
	 * 调用相册获取照片
	 * 
	 * @param context
	 */
	public void getImageFromAlbum(Context context) {
		Intent intent = new Intent(Intent.ACTION_PICK);
		intent.setType("image/*");// 相片类型
		((Activity) context).startActivityForResult(intent,
				REQUEST_CODE_PICK_IMAGE);
	}

	/**
	 * 图片保存到相册
	 * 
	 * @param context
	 * @param fileSaveName
	 * @param filePath
	 * @return
	 */
	public boolean saveToCamenaAlbum(Context context, String fileSaveName,
			String filePath) throws Exception {
		if (filePath == null) {

			throw new NFRuntimeException("filePath为空");
		}
		LogUtil.e("LoadSysSoft",filePath	);
		final Uri STORAGE_URI = Images.Media.EXTERNAL_CONTENT_URI;
		final String IMAGE_MIME_TYPE = "image/jpeg";
		ContentValues values = new ContentValues(6);
		ContentResolver contentResolver = context.getContentResolver();
		values.put(Images.Media.TITLE, fileSaveName);
		values.put(Images.Media.DISPLAY_NAME, fileSaveName);
		values.put(Images.Media.DATE_TAKEN, new Date().getTime());
		values.put(Images.Media.MIME_TYPE, IMAGE_MIME_TYPE);
		values.put(Images.Media.ORIENTATION, 0);
		values.put(Images.Media.DATA, filePath);
		//一开始这儿刷新一个资源文件但是会出现两个一样的照片，所有在前边刷新
		//Uri dataUri = contentResolver.insert(STORAGE_URI, values);
		// 插入数据后刷新一下：
		//contentResolver.notifyChange(STORAGE_URI, null);

		return true;
	}

	/****
	 * 调用系统照相机获取照片
	 * 
	 * @param context
	 */
	public void getImageFromCamera(Context context, String picFilePath) {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {// android.media.action.IMAGE_CAPTURE
			Intent getImageByCamera = new Intent(
					android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
			getImageByCamera.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
					Uri.fromFile(new File(picFilePath)));
			((Activity) context).startActivityForResult(getImageByCamera,
					REQUEST_CODE_CAPTURE_CAMEIA);
		} else {
			Toast.makeText(context, "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 获取图片保存地址
	 * 
	 * @return
	 */
	public String getPicFilePath() {

		File PHOTO_DIR = new File(Environment.getExternalStorageDirectory()
				+ "/DCIM/Camera");
		if (!PHOTO_DIR.exists()) {
			PHOTO_DIR.mkdirs();// 创建照片的存储目录
		}
		return PHOTO_DIR + File.separator + getPhotoFileName();
	}

	/**
	 * 用当前时间给取得的图片命名
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	/**
	 * android获取一个用于打开Word文件的intent
	 * 
	 * @param param
	 * @return
	 */
	public static Intent getWordFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");

		intent.addCategory("android.intent.category.DEFAULT");

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Uri uri = Uri.fromFile(new File(param));

		intent.setDataAndType(uri, "application/msword");

		return intent;

	}

	/***
	 * android获取一个用于打开Excel文件的intent
	 * 
	 * @param param
	 * @return
	 */

	public static Intent getExcelFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");

		intent.addCategory("android.intent.category.DEFAULT");

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Uri uri = Uri.fromFile(new File(param));

		intent.setDataAndType(uri, "application/vnd.ms-excel");

		return intent;
	}

	/***
	 * android获取一个用于打开PPT文件的intent
	 * 
	 * @param param
	 * @return
	 */

	public static Intent getPptFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");

		intent.addCategory("android.intent.category.DEFAULT");

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Uri uri = Uri.fromFile(new File(param));

		intent.setDataAndType(uri, "application/vnd.ms-powerpoint");

		return intent;

	}

	/***
	 * android获取一个用于打开CHM文件的intent
	 * 
	 * @param param
	 * @return
	 */

	public static Intent getChmFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");

		intent.addCategory("android.intent.category.DEFAULT");

		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Uri uri = Uri.fromFile(new File(param));

		intent.setDataAndType(uri, "application/x-chm");

		return intent;

	}

	/***
	 * android获取一个用于打开PDF文件的intent
	 * 
	 * @param param
	 * @return
	 */

	public static Intent getPdfFileIntent(String param) {
		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		Uri uri = Uri.fromFile(new File(param));

		intent.setDataAndType(uri, "application/pdf");

		return intent;

	}

	/***
	 * android获取一个用于打开文本文件的intent
	 * 
	 * @param paramString
	 * @param paramBoolean
	 * @return
	 */

	public static Intent getTextFileIntent(String param) {

		Intent intent = new Intent("android.intent.action.VIEW");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		Uri uri1 = Uri.parse(param);
		intent.setDataAndType(uri1, "text/plain");
		return intent;
	}

	/**
	 * 开启Gps设置页面
	 * 
	 * @param activity
	 */
	public static void openGPSSettings(Activity activity, int requestCode) {

		Intent intent = new Intent();
		intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try {
			activity.startActivityForResult(intent, requestCode);
		} catch (ActivityNotFoundException ex) {
			// The Android SDK doc says that the location settings activity
			// may not be found. In that case show the general settings.
			// General settings activity
			intent.setAction(Settings.ACTION_SETTINGS);
			try {
				activity.startActivityForResult(intent, requestCode);
			} catch (Exception e) {
			}
		}
	}
}
