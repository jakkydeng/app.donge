/*
 * @(#)ViewUtil.java		       Project:com.sinaapp.msdxblog.androidkit
 * Date:2012-3-17
 *
 * Copyright (c) 2011 CFuture09, Institute of Software, 
 * Guangdong Ocean University, Zhanjiang, GuangDong, China.
 * All rights reserved.
 */
package nf.framework.core.util.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Rect;
import android.view.View;

public class ScreenCutUtil {

	 /**
     *  获取指定Activity的截屏，保存到png文件
     * @param activity
     * @return
     */
	public static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        // 获取屏幕长和高
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();
        return takeViewShot(activity,view,statusBarHeight,width,height);
    }
	/***
	 * 根据view截图
	 * View是你需要截图的View
	 * @param activity
	 * @param view
	 * @return
	 */
	public static Bitmap takeViewShot(Activity activity,View view){
		
		return takeViewShot(activity, view,0,view.getWidth(),view.getHeight());
		
	}


	public static Bitmap takeViewShot(Activity activity,View view,int tempHeight,int width,int height){
		
			Bitmap b1 =loadBitmapFromView(view);    
	        if(b1!=null&&tempHeight!=0){
	        	b1 = Bitmap.createBitmap(b1, 0, tempHeight, width, height - tempHeight);
	        }
	        return b1;
	}
	
	public static String takeViewShotPath(Activity activity,View view){
		Bitmap bitmap =takeViewShot(activity, view);
		return	takeViewShotPath(activity,bitmap);
	}
	public static String takeViewShotPath(Activity activity,Bitmap bitmap){
		
		String path =activity.getExternalCacheDir()+File.separator+"screenshot.png";
		try {
			File file =new File(path);
			if(!file.exists()){
				file.createNewFile();
			}
			if(bitmap!=null){
				savePic(bitmap, path);
				bitmap.recycle();
				bitmap=null;
			}
		} catch (IOException e) {
			return null;
		}
		return path;
	}
    /**
     *  保存到sdcard
     * @param b
     * @param strFileName
     */
    public static void savePic(Bitmap b, String strFileName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(strFileName);
            if (null != fos) {
                b.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                fos.flush();
                fos.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 把View绘制到Bitmap上
     * 
     * @param view 需要绘制的View
     * @param width 该View的宽度
     * @param height 该View的高度
     * @return 返回Bitmap对象
     */
    private static Bitmap getBitmapFromView(View view, int width, int height)
    {
                int widthSpec = View.MeasureSpec.makeMeasureSpec(width,
                    View.MeasureSpec.EXACTLY);
                int heightSpec = View.MeasureSpec.makeMeasureSpec(height,
                    View.MeasureSpec.EXACTLY);
                view.measure(widthSpec, heightSpec);
                view.layout(0, 0, width, height);
        Bitmap bitmap = Bitmap.createBitmap(width, height,
            Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
            | Paint.FILTER_BITMAP_FLAG));
        view.draw(canvas);

        return bitmap;
    }
    public static Bitmap loadBitmapFromView(View v) {
		if (v == null) {
			return null;
		}
		Bitmap screenshot = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Config.RGB_565);
		Canvas c = new Canvas(screenshot);
//		c.translate(-v.getScrollX(), -v.getScrollY());
		v.draw(c);
		return screenshot;
	}
}
