/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package nf.framework.expand.widgets.zoomPhotoView;

import java.util.List;

import nf.framework.expand.widgets.zoomPhotoView.PhotoViewAttacher.OnMatrixChangedListener;
import nf.framework.expand.widgets.zoomPhotoView.PhotoViewAttacher.OnPhotoTapListener;
import nf.framework.expand.widgets.zoomPhotoView.PhotoViewAttacher.OnViewTapListener;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;


public class PhotoView extends ImageView implements IPhotoView {

	private final PhotoViewAttacher mAttacher;

	private ScaleType mPendingScaleType;

	private boolean isShowTag=true;
	private int innerCircle=3;
	private int ringWidth=1;
	private int rectHeight=20;
	private int padding=5;
	private int rectWidth =30;
	private RectF currentDrawRectF;
	private List<ImageTagVO> imageTagList ;
	private OnViewTapListener onViewTapListener;
	private OnImageTagItemClickListener onImageTagItemClickListener;
	private OnMatrixChangedListener onMatrixChangedListener;
	public PhotoView(Context context) {
		this(context, null);
	}

	public PhotoView(Context context, AttributeSet attr) {
		this(context, attr, 0);
	}
	
	public PhotoView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		super.setScaleType(ScaleType.MATRIX);
		mAttacher = new PhotoViewAttacher(this);

		if (null != mPendingScaleType) {
			setScaleType(mPendingScaleType);
			mPendingScaleType = null;
		}
		innerCircle = dip2px(getContext(),3); //设置内圆半径  
        ringWidth = dip2px(getContext(),1); //设置圆环宽度  
        rectHeight= dip2px(getContext(),20);
        rectWidth =dip2px(getContext(), 40);
        padding=dip2px(getContext(), 5);
        
	    
	    mAttacher.setOnViewTapListener(new OnViewTapListener() {
			
			@Override
			public void onViewTap(View view, float x, float y) {
				// TODO Auto-generated method stub
				boolean flag =whichTxtView(x,y);
				if(!flag&&onViewTapListener!=null){
					onViewTapListener.onViewTap(view, x, y);
				}
			}
		});
	    mAttacher.setOnMatrixChangeListener(new OnMatrixChangedListener() {
			
			@Override
			public void onMatrixChanged(RectF rect) {
				// TODO Auto-generated method stub
				currentDrawRectF=rect;
				postInvalidate();
				if(onMatrixChangedListener!=null){
					onMatrixChangedListener.onMatrixChanged(rect);
				}
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Log.d("--------------------------", "-----------------------"+getInitScale());
		if(imageTagList!=null&&isShowTag()){
			for(ImageTagVO imageTag :imageTagList){
				imageTag.setScale(getInitScale());
				drawIamgeTagView(canvas,imageTag);
			}
		}
	}
	
	private void drawIamgeTagView(Canvas canvas,ImageTagVO imageTag){
		
        if(currentDrawRectF==null){
    	   return;
        }
        float circleCenterX =currentDrawRectF.left+imageTag.getX()*getScale();
        float circleCenterY =currentDrawRectF.top+imageTag.getY()*getScale();
        //圆环
        drawRing(canvas, circleCenterX, circleCenterY);
        //标签显示
        drawImageTag(canvas, imageTag, circleCenterX, circleCenterY);
	}
	
	private int[] getTextRectSize(String text,Paint paint){
		
		Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return new int[]{rect.width(),rect.height()};
	}
	/***
	 * 绘制圆环
	 * @param canvas
	 * @param circleCenterX
	 * @param circleCenterY
	 */
	private void drawRing(Canvas canvas,float circleCenterX,float circleCenterY){
		
        //绘制内圆  
    	Paint innerCirclePaint = new Paint();  
    	innerCirclePaint.setAntiAlias(true); //消除锯齿  
    	innerCirclePaint.setStyle(Paint.Style.FILL);
    	innerCirclePaint.setColor(Color.WHITE);  
    	innerCirclePaint.setStrokeWidth(2);  
        canvas.drawCircle(circleCenterX,circleCenterY, innerCircle, innerCirclePaint);  
          
    	Paint circlePaint = new Paint();  
        circlePaint.setAntiAlias(true); //消除锯齿  
        circlePaint.setStyle(Paint.Style.STROKE); //绘制空心圆   
        //绘制圆环  
        circlePaint.setColor(Color.BLACK);  
        circlePaint.setStrokeWidth(ringWidth);  
        canvas.drawCircle(circleCenterX,circleCenterY, innerCircle+1+ringWidth/2, circlePaint);  
        //绘制外圆  
        circlePaint.setStrokeWidth(2);  
        canvas.drawCircle(circleCenterX,circleCenterY, innerCircle+ringWidth,circlePaint); 
	}
	
	private void drawImageTag(Canvas canvas,ImageTagVO imageTag,float circleCenterX,float circleCenterY){
		
		 //文字
        Paint textPaint = new Paint(); 
        textPaint.setTextSize(sp2px(getContext(), 14));  
        textPaint.setStrokeWidth(2);
        textPaint.setColor(Color.WHITE);  
      //文字背景框
        int[] titleSize= getTextRectSize(imageTag.getName(), textPaint);
       
        Paint bgPaint = new Paint(); 
        bgPaint.setStrokeWidth(ringWidth);
        bgPaint.setAntiAlias(true); //消除锯齿  
        bgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        bgPaint.setColor(Color.BLACK);
        bgPaint.setAlpha(100); 
        RectF bgRect= null; 
        //如果圆点距离图片右边太近，则考虑将显示标签放到环左侧
        if(circleCenterX>currentDrawRectF.right-100){ 
        	
        	bgRect=new RectF( (int)circleCenterX-innerCircle-ringWidth-titleSize[0]-padding*3
            		,(int)circleCenterY-titleSize[1]/2-padding
            		,(int)circleCenterX-innerCircle-ringWidth-padding
            		, (int)circleCenterY+titleSize[1]/2+padding);
        	
        }else{
        	
        	bgRect=new RectF((int)circleCenterX+innerCircle+ringWidth+padding
        		,(int)circleCenterY-titleSize[1]/2-padding
        		, (int)circleCenterX+innerCircle+ringWidth+titleSize[0]+padding*3
        		, (int)circleCenterY+titleSize[1]/2+padding);
        	 
        }
        canvas.drawRoundRect(bgRect,5,5, bgPaint);
        
        imageTag.setRectF(bgRect);
        
      //如果圆点距离图片右边太近，则考虑将显示标签放到环左侧
        if(circleCenterX>currentDrawRectF.right-100){ 
	        //文字显示
	    	canvas.drawText(imageTag.getName(),circleCenterX-innerCircle-ringWidth-padding*2-titleSize[0]
	     		,circleCenterY+sp2px(getContext(), 5), textPaint); 
        }else{
        	//文字显示
        	canvas.drawText(imageTag.getName(),circleCenterX+innerCircle+ringWidth+padding*2
         		,circleCenterY+sp2px(getContext(), 5), textPaint); 
        }
	}
	
	@Override
	public boolean canZoom() {
		return mAttacher.canZoom();
	}

	@Override
	public RectF getDisplayRect() {
		return mAttacher.getDisplayRect();
	}

	@Override
	public float getMinScale() {
		return mAttacher.getMinScale();
	}

	@Override
	public float getMidScale() {
		return mAttacher.getMidScale();
	}

	@Override
	public float getMaxScale() {
		return mAttacher.getMaxScale();
	}

	@Override
	public float getScale() {
		return mAttacher.getScale();
	}

	@Override
	public ScaleType getScaleType() {
		return mAttacher.getScaleType();
	}
	@Override
	public float getInitScale() {
		// TODO Auto-generated method stub
		return mAttacher.getInitScale();
	}
    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttacher.setAllowParentInterceptOnEdge(allow);
    }

    @Override
	public void setMinScale(float minScale) {
		mAttacher.setMinScale(minScale);
	}

	@Override
	public void setMidScale(float midScale) {
		mAttacher.setMidScale(midScale);
	}

	@Override
	public void setMaxScale(float maxScale) {
		mAttacher.setMaxScale(maxScale);
	}

	@Override
	// setImageBitmap calls through to this method
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}

	public void setImageTagList(List<ImageTagVO> imageTagList){
		
		this.imageTagList =imageTagList;
	}
	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		if (null != mAttacher) {
			mAttacher.update();
		}
	}

	@Override
	public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
		this.onMatrixChangedListener=listener;
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		mAttacher.setOnLongClickListener(l);
	}

	@Override
	public void setOnPhotoTapListener(OnPhotoTapListener listener) {
		mAttacher.setOnPhotoTapListener(listener);
	}

	@Override
	public void setOnViewTapListener(OnViewTapListener listener) {
		this.onViewTapListener =listener;
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (null != mAttacher) {
			mAttacher.setScaleType(scaleType);
		} else {
			mPendingScaleType = scaleType;
		}
	}

	@Override
	public void setZoomable(boolean zoomable) {
		mAttacher.setZoomable(zoomable);
	}

	@Override
	public void zoomTo(float scale, float focalX, float focalY) {
		mAttacher.zoomTo(scale, focalX, focalY);
	}

	public boolean isShowTag() {
		return isShowTag;
	}

	public void setShowTag(boolean isShowTag) {
		this.isShowTag = isShowTag;
		postInvalidate();
	}

	@Override
	protected void onDetachedFromWindow() {
		mAttacher.cleanup();
		super.onDetachedFromWindow();
	}
	 @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
		 // 获取点击屏幕时的点的坐标   
         float x = event.getX();   
         float y = event.getY();   
		 
         whichTxtView(x,y);
		 
		 try {
            return super.onTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
	 
	 private boolean whichTxtView(float x, float y) {
		// TODO Auto-generated method stub

		if(imageTagList!=null&&onImageTagItemClickListener!=null){
			for (ImageTagVO imageTag:imageTagList) {
				RectF rectF =imageTag.getRectF();
				if(rectF!=null){
					if(x>rectF.left&&x<rectF.right&&y>rectF.top&&y<rectF.bottom){
						onImageTagItemClickListener.onImageTagItemClicked(imageTag);
						return true;
					}
				}
			}
		}
		return false;
	}

	/** 
	     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
	     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
    
    /** 
     * 将sp值转换为px值，保证文字大小不变 
     *  
     * @param spValue 
     * @param fontScale 
     *            （DisplayMetrics类中属性scaledDensity） 
     * @return 
     */  
    public static int sp2px(Context context, float spValue) {  
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;  
        return (int) (spValue * fontScale + 0.5f);  
    }  
    
    public void setOnImageTagItemClickListener(
			OnImageTagItemClickListener onImageTagItemClickListener) {
		this.onImageTagItemClickListener = onImageTagItemClickListener;
	}

	public interface OnImageTagItemClickListener{
    	
    	public void onImageTagItemClicked(ImageTagVO imageTag);
    }


}