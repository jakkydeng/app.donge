package nf.framework.expand.widgets;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NFViewPager extends ViewPager {


	private UpFreshListView mListView;
	
	public NFViewPager(Context context) {
		this(context,null);
	}

	public NFViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	
	public void setmListView(UpFreshListView mListView) {
		this.mListView = mListView;
	}


	private boolean isIntercept;
	float x =0,y=0;
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// 当拦截触摸事件到达此位置的时候，返回true，
		// 说明将onTouch拦截在此控件，进而执行此控件的onTouchEvent

		int action = ev.getAction();  
        if (action==MotionEvent.ACTION_DOWN)  {
        	 x =ev.getX(); y=ev.getY();
        	 isIntercept=false;
        }else if(action== MotionEvent.ACTION_MOVE){
        	float tempX =Math.abs(ev.getX()-x);
        	float tempY =Math.abs(ev.getY()-y);
        	isIntercept=tempX>tempY;
        	
        }else if(action == MotionEvent.ACTION_UP){
        }
        if(isIntercept){
    		return true;
    	}
		return super.onInterceptTouchEvent(ev);
	}
}
