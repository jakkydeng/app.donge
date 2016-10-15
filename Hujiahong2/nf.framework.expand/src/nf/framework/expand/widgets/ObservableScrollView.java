/**
 * d.java
 * com.Apricotforest.widgets
 * 工程：medicalJournals_for_android
 * 功能：scrollview 动态滚动 实现scollView 自动滚动时的事件监听 


 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-9-5       下午02:18:12
 * Copyright (c) 2012, TNT All Rights Reserved.
 */

package nf.framework.expand.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;
import android.widget.Scroller;

public class ObservableScrollView extends ScrollView {

	private ScrollViewListener scrollViewListener = null;
	private Scroller mScroller;

	public ObservableScrollView(Context context) {
		super(context);
	}

	public ObservableScrollView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		mScroller = new Scroller(context);
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mScroller = new Scroller(context);
	}

	public void setScrollViewListener(ScrollViewListener scrollViewListener) {
		this.scrollViewListener = scrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (scrollViewListener != null) {
			scrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

	/**
	 * 滑动事件 降低滚动速度
	 */
	// @Override
	// public void fling(int velocityY) {
	// super.fling(velocityY / 10);
	// }

	public interface ScrollViewListener {

		void onScrollChanged(ObservableScrollView scrollView, int x, int y,
				int oldx, int oldy);

	}



	//调用此方法滚动到目标位置  duration滚动时间
	public void smoothScrollToSlow(int fx, int fy, int duration) {
		int dx = fx - getScrollX();//mScroller.getFinalX();  普通view使用这种方法
		int dy = fy - getScrollY();  //mScroller.getFinalY();
		smoothScrollBySlow(dx, dy, duration);
	}

	//调用此方法设置滚动的相对偏移
	public void smoothScrollBySlow(int dx, int dy,int duration) {

		//设置mScroller的滚动偏移量
		mScroller.startScroll(getScrollX(), getScrollY(), dx, dy,duration);//scrollView使用的方法（因为可以触摸拖动）
//        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, duration);  //普通view使用的方法
		invalidate();//这里必须调用invalidate()才能保证computeScroll()会被调用，否则不一定会刷新界面，看不到滚动效果
	}

	@Override
	public void computeScroll() {

		//先判断mScroller滚动是否完成
		if (mScroller.computeScrollOffset()) {

			//这里调用View的scrollTo()完成实际的滚动
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());

			//必须调用该方法，否则不一定能看到滚动效果
			postInvalidate();
		}
		super.computeScroll();
	}

	/**
	 * 滑动事件，这是控制手指滑动的惯性速度
	 */
	@Override
	public void fling(int velocityY) {
		super.fling(velocityY / 4);
	}
}