package nf.framework.core.util.android;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import nf.framework.core.exception.LogUtil;
import nf.framework.core.exception.NFRuntimeException;
import nf.framework.core.util.io.ImageUtil;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;


/**
 * 照相机功能类，实现调用照相机拍照、从图库选择图片、对图片进行裁剪等功能
 * 
 * @author Jcking
 */
public class CameraUtil {
	private static final String TAG = "Camera";
	
	private static final int SCAN_MEDIA_START = 1;
	private static final int SCAN_MEDIA_FINISH = 2;
	private static final int SCAN_MEDIA_FILE = 3;
	private static final int SCAN_MEDIA_FILE_FINISH_INT = 4;
	private static final int SCAN_MEDIA_FILE_FAIL_INT = 5;
	
	private static final String SCAN_MEDIA_FILE_FINISH = "ACTION_MEDIA_SCANNER_SCAN_FILE_FINISH";

	public static final int PIC_REQUEST_CODE_WITH_DATA = 10000; // 标识获取图片数据
	public static final int PIC_REQUEST_CODE_SELECT_CAMERA = 10001; // 标识请求照相功能的activity 再次调用图片剪辑程序去修剪图片
	public static final int PIC_Select_CODE_ImageFromLoacal = 10002;// 标识请求相册取图功能的activity
	public static final int PIC_REQUEST_CODE_SELECT_CAMERA2 = 10003; // 标识请求照相功能的activity 直接返回图片bitmap
	private Activity mActivity;
	private int maxPicWidth=300;
	private int maxPicHeight=300;
	private static final File PHOTO_DIR = new File(
			Environment.getExternalStorageDirectory() + "/DCIM/Camera");
	
	private File mCurrentPhotoFile;//
	private String fileName;// 图片名称
	private static MediaActionReceiver actionReceiver;
	
	private float pic_size = 240;
	
	public CameraUtil(Context context){
		this.mActivity = (Activity)context;
		actionReceiver = new MediaActionReceiver();
	}
	
	/**
	 * 调用照相机进行拍照  默认保存到sd卡中
	 * @param isScan 是否调用剪切程序
	 */
	public void takePhoto(boolean isScan){
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
			PHOTO_DIR.mkdirs();// 创建照片的存储目录
			fileName = getPhotoFileName();
			mCurrentPhotoFile = new File(PHOTO_DIR, fileName);// 给新照的照片文件命名
			takePhoto(isScan, mCurrentPhotoFile.getPath());
		} else {
			Toast.makeText(mActivity, "抱歉，没有检测到SD卡",	Toast.LENGTH_LONG).show();
		}
		
	}
	/**
	 * 调用照相机进行拍照 保存到指定路径
	 */
	public void takePhoto(boolean isScan,String fileSavePath){
		if(fileSavePath==null){
			return;
		}
		mCurrentPhotoFile = new File(fileSavePath);// 给新照的照片文件命名
		Intent cIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
		cIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
		mActivity.startActivityForResult(cIntent
				,isScan?PIC_REQUEST_CODE_SELECT_CAMERA:PIC_REQUEST_CODE_SELECT_CAMERA2);
	}
	/**
	 * 从图库选取图片
	 */
	public void selectPic(){
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			mActivity.startActivityForResult(intent,
					PIC_Select_CODE_ImageFromLoacal);
		} else {
			Toast.makeText(mActivity, "没有SD卡",
					Toast.LENGTH_LONG).show();
		}
	}

	public Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case SCAN_MEDIA_FINISH:
				Log.i(TAG, "sccan media finish");
				galleryPhoto();
				break;
			case SCAN_MEDIA_FILE:
				Log.i(TAG, "sccan file");
				ScanMediaThread sthread = new ScanMediaThread(
						mActivity, 40, 300);
				sthread.run();
				break;

			case SCAN_MEDIA_FILE_FINISH_INT:
				Log.i(TAG, "sccan file finish");
				galleryPhoto();
				break;

			case SCAN_MEDIA_FILE_FAIL_INT:
				Log.i(TAG, "sccan file fail");
				try {
					mActivity.unregisterReceiver(actionReceiver);
				} catch (Exception e) {
					Log.e(TAG, "actionReceiver not registed");
				}

				Toast.makeText(mActivity, "no take photo",
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	};
	/**
	 * 定义receiver接收其他线程的广播
	 * 
	 */
	public class MediaActionReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Intent.ACTION_MEDIA_SCANNER_STARTED.equals(action)) {
				mHandler.sendEmptyMessage(SCAN_MEDIA_START);
			}
			if (Intent.ACTION_MEDIA_SCANNER_FINISHED.equals(action)) {
				mHandler.sendEmptyMessage(SCAN_MEDIA_FINISH);
			}
			if (Intent.ACTION_MEDIA_SCANNER_SCAN_FILE.equals(action)) {
				mHandler.sendEmptyMessage(SCAN_MEDIA_FILE);
			}
		}
	}
	
	/**
	 * 调用系统默认图库进行图片裁剪
	 */
	private void galleryPhoto() {
		try {
			long id = 0;
			Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
			ContentResolver cr = mActivity.getContentResolver();

			Cursor cursor = cr.query(imgUri, null,
					MediaStore.Images.Media.DISPLAY_NAME + "='"
							+ mCurrentPhotoFile.getName() + "'", null, null);

			if (cursor != null && cursor.getCount() > 0) {
				cursor.moveToLast();
				id = cursor.getLong(0);

				/*
				 * update by lvxuejun on 20110322 2.1系统对 gallery
				 * 这个调用进行了修改，uri不让传file:///了，只能传图库中的图片，
				 * 比如此类uri：content://media/external/images/media/3 所以需要把FIle的Uri
				 * 转化成图库的Uri
				 */
				Uri uri = ContentUris.withAppendedId(imgUri, id);// Uri.fromFile(mCurrentPhotoFile);

				// 启动gallery去剪辑这个照片
				final Intent intent = getCropImageIntent(uri);
				if (intent != null) {
					mActivity.startActivityForResult(intent, PIC_REQUEST_CODE_WITH_DATA);
				}

			}
			try {
				mActivity.unregisterReceiver(actionReceiver);
			} catch (Exception e) {
				Log.e(TAG, "actionReceiver not registed");
			}
		} catch (Exception ee) {
			// TODO Auto-generated catch block
			Log.e(TAG, "", ee);

		}
	}
	/**
	 * 经过阅读android源代码发现，此方法返回的data 必须小于100k
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @return
	 */
	public CameraResponse onActivityResult(int requestCode, int resultCode, Intent data) {

		CameraResponse cameraResponse=new CameraResponse();
//		if (resultCode == Activity.RESULT_OK) {
			cameraResponse.setPicPath(mCurrentPhotoFile.getPath());
			switch (requestCode) {
				case PIC_REQUEST_CODE_WITH_DATA:// 调用Gallery返回的
					System.out.println("CameraUtil.onActivityResult()-----------PIC_REQUEST_CODE_WITH_DATA");
					if(data!=null){
						cameraResponse.setBitmap((Bitmap)data.getParcelableExtra("data"));
						cameraResponse.setThumpBit(getThumpPic(mActivity, data));
					}else{
						Bitmap	photo = ImageUtil.decodeBitmapFromFile(mCurrentPhotoFile.getPath(),maxPicWidth,maxPicHeight);
						cameraResponse.setBitmap(photo);
					}
					break;
			case PIC_REQUEST_CODE_SELECT_CAMERA: {// 照相机程序返回的,再次调用图片剪辑程序去修剪图片
				try {
					System.out.println("CameraUtil.onActivityResult()+222222");
					Uri fileUri = Uri.fromFile(mCurrentPhotoFile);
					IntentFilter intentFilter = new IntentFilter(
							Intent.ACTION_MEDIA_SCANNER_STARTED);
					intentFilter
							.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
					intentFilter.addDataScheme("file");
					intentFilter
							.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
					mActivity.registerReceiver(actionReceiver, intentFilter);
					mActivity.sendBroadcast(new Intent(
							Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
				} catch (Exception e) {
					Toast.makeText(mActivity, "获取图片异常，请重新尝试。", Toast.LENGTH_LONG).show();
					Log.e(TAG, "Cannot crop image:", e);
				}
				break;
			}
			case PIC_Select_CODE_ImageFromLoacal:
				Uri uri = data.getData();
				boolean isSDCard = true;
				Uri fileUri = null;
				ContentResolver cr = mActivity.getContentResolver();
				Cursor cursor = cr.query(uri, null, null, null, null);
				if (cursor != null) {
					cursor.moveToFirst();
					isSDCard = false;
					fileName = cursor.getString(3);
					mCurrentPhotoFile = new File(cursor.getString(1)); // 图片文件路径
				}
				if (isSDCard) {
					mCurrentPhotoFile = new File(uri.getEncodedPath());
					fileName = uri.getEncodedPath().substring(
							uri.getEncodedPath().lastIndexOf("/"));
				} 
				if (mCurrentPhotoFile.exists()) {
					fileUri = Uri.fromFile(mCurrentPhotoFile);
					try {
						IntentFilter intentFilter = new IntentFilter(
								Intent.ACTION_MEDIA_SCANNER_STARTED);
						intentFilter
								.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
						intentFilter.addDataScheme("file");
						intentFilter
								.addAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
						mActivity.registerReceiver(actionReceiver, intentFilter);
					} catch (NFRuntimeException e) {
						LogUtil.writeExceptionLog(e);
					}
					mActivity.sendBroadcast(new Intent(
							Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, fileUri));
				} else {
					Toast.makeText(mActivity, "该文件不存在!",
							Toast.LENGTH_LONG).show();
				}
				break;
				case	PIC_REQUEST_CODE_SELECT_CAMERA2:// 照相机程序返回的 直接返回bitmap
					if(data!=null){
						cameraResponse.setBitmap((Bitmap)data.getParcelableExtra("data"));
						cameraResponse.setThumpBit(getThumpPic(mActivity, data));
					}else{
						Bitmap	photo = ImageUtil.decodeBitmapFromFile(mCurrentPhotoFile.getPath(),maxPicWidth,maxPicHeight);
						cameraResponse.setBitmap(photo);
					}
				break;
			}
//		}
		return cameraResponse;
	}
	/***
	 * 获取缩略图
	 * @param context
	 * @param data
	 * @return
	 */
	public Bitmap   getThumpPic(Context context,Intent data){
		
		 ContentResolver resolver =context.getContentResolver();
         try {  
	         Uri originalUri = data.getData();
	         if(originalUri!=null){
		         Uri thumb = Uri.withAppendedPath(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,originalUri.getLastPathSegment());
		         return MediaStore.Images.Media.getBitmap(resolver, thumb);
	         }
	     } catch (IOException e) {
	    	 Log.v(TAG, "getThumpPic error");
	     }
         return null;
	}
	
	/**
	 * 调用图片剪辑程序
	 */
	private Intent getCropImageIntent(Uri photoUri) {
		
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(photoUri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 2);
		intent.putExtra("aspectY", 3);
		intent.putExtra("outputX", pic_size);//裁剪区的宽
		intent.putExtra("outputY", pic_size);//裁剪区的高
		intent.putExtra("noFaceDetection", true);  //关闭人脸识别
		intent.putExtra("return-data", true);// //需要返回数据</p>
		intent.putExtra("outputFormat", "JPEG");
		return intent;
	}
	/**
	 *  用当前时间给取得的图片命名
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	private String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}

	public class ScanMediaThread extends Thread {
		private int scanCount = 5;
		private int interval = 50;
		private Context cx = null;

		public ScanMediaThread(Context context, int count, int i) {
			scanCount = count;
			interval = i;
			cx = context;
			this.setName(System.currentTimeMillis() + "_ScanMediaThread");
		}

		@Override
		public void run() {
			Log.i(TAG, "scan thread start");
			if (this.cx == null)
				return;
			try {
				int j = 0;
				Uri imgUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver cr = mActivity.getContentResolver();

				Cursor cursor;
				for (j = 0; j < this.scanCount; j++) {
					Thread.sleep(this.interval);
					cursor = cr.query(imgUri, null,
							MediaStore.Images.Media.DISPLAY_NAME + "='"
									+ mCurrentPhotoFile.getName() + "'", null,
							null);
					Log.i(TAG, "scan thread " + j);
					if (cursor != null && cursor.getCount() > 0) {
						Log.i(TAG, "send finish " + SCAN_MEDIA_FILE_FINISH);
						mHandler.sendEmptyMessage(SCAN_MEDIA_FILE_FINISH_INT);
						break;
					}
				}
				if (j == this.scanCount) {
					Log.i(TAG, "send fail ");
					mHandler.sendEmptyMessage(SCAN_MEDIA_FILE_FAIL_INT);
				}
			} catch (Exception e) {
				//TODO
				e.printStackTrace();
			}
		}

	}
	public void onDestroy() {
		try {
			mActivity.unregisterReceiver(actionReceiver);
		} catch (Exception e) {
			Log.e(TAG, "actionReceiver not registed");
		}
	}

	public void setMaxPicWidth(int maxPicWidth) {
		this.maxPicWidth = maxPicWidth;
	}

	public void setMaxPicHeight(int maxPicHeight) {
		this.maxPicHeight = maxPicHeight;
	}
	
	public static class CameraResponse{
		
		String picPath;
		Bitmap bitmap;
		Bitmap thumpBit;
		public String getPicPath() {
			return picPath;
		}
		public void setPicPath(String picPath) {
			this.picPath = picPath;
		}
		public Bitmap getBitmap() {
			return bitmap;
		}
		public void setBitmap(Bitmap bitmap) {
			this.bitmap = bitmap;
		}
		public Bitmap getThumpBit() {
			return thumpBit;
		}
		public void setThumpBit(Bitmap thumpBit) {
			this.thumpBit = thumpBit;
		}
	
	
	}
	
}
