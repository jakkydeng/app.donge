package nf.framework.expand.widgets;

/**
 * BannerView.java
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-10-8       下午03:24:38
 * Copyright (c) 2012, TNT All Rights Reserved.
 */


import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;

public class BannerGalleryView extends RelativeLayout {

	private Context mcontext;
	private CancleInertiaGallery imggallery;
	private PagerControl pagercontrol;
	private OnBannerItemClickListener mItemClicklistener;
	private OnBannerItemSelectedListener mItemSelectedlistener;
	private static int TICK_WHAT = 5;
	private long millionseconds = 6000;// 自动滚屏间隔时间
	private int contentItemCount = 0;

	public BannerGalleryView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		mcontext = context;
		initView();
	}

	public BannerGalleryView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public BannerGalleryView(Context context) {
		this(context,null);
	}

	private void initView() {
		LayoutParams layoutParams=	new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		RelativeLayout layout=new RelativeLayout(mcontext);
		imggallery =new CancleInertiaGallery(mcontext);
		imggallery.setLayoutParams(layoutParams);
		imggallery.setFadingEdgeLength(0);
		pagercontrol =new  PagerControl(mcontext,null);
		DisplayMetrics metric = new DisplayMetrics();
		( (Activity)mcontext).getWindowManager().getDefaultDisplay().getMetrics(metric);
		float density=metric.density;
		LayoutParams pagercontrolParams=	new LayoutParams((int)(80*density),(int)(25*density));
		pagercontrolParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		pagercontrolParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		pagercontrol.setLayoutParams(pagercontrolParams);
		layout.addView(imggallery);
		layout.addView(pagercontrol);
			imggallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(mItemClicklistener!=null)
				mItemClicklistener.onBannerItemClick(arg0, arg1, arg2, arg3);
			}
		});
		imggallery.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				if (pagercontrol.getNumPages() != 0)
					pagercontrol.setCurrentPage(arg2);
				if(mItemSelectedlistener!=null){
					mItemSelectedlistener.onBannerItemSelected(arg0, arg1, arg2, arg3);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				if(mItemSelectedlistener!=null){
					mItemSelectedlistener.onBannerNothingSelected(arg0);
				}
			}
		});
		addView(layout);
	}
	
	@Override
	public void setVisibility(int visibility) {
		// TODO Auto-generated method stub
		super.setVisibility(visibility);
	
		int childCount =getChildCount();
	
		for(int i=0;i<childCount;i++){
			getChildAt(i).setVisibility(visibility);
		}
	}
	
	/**
	 * 
	 * @param adapter
	 */
	public void setAdapter(SpinnerAdapter adapter) {
		if (imggallery != null)
			imggallery.setAdapter(adapter);
	}

	public SpinnerAdapter getAdapter() {
		if (imggallery != null)
			return imggallery.getAdapter();
		return null;

	}

	public void notifyPagerControler() {
		contentItemCount = imggallery.getAdapter().getCount();
		if (pagercontrol.getNumPages() == 0 && contentItemCount > 1) {
			pagercontrol.setNumPages(contentItemCount);
			if (contentItemCount > 5)
				pagercontrol.setPageWidth(8);
			else
				pagercontrol.setPageWidth(12);
		}
	}

	/**
	 * 开始自动滚动方法
	 * 
	 * @param ms
	 */
	public void startAutoScroll(long ms) {
		contentItemCount = imggallery.getAdapter().getCount();// 获取显示内容的个数
		if (contentItemCount > 1) {
			mHandler.removeMessages(TICK_WHAT);
			mHandler.sendMessageDelayed(Message.obtain(mHandler, TICK_WHAT), ms);
			this.millionseconds = ms;
			notifyPagerControler();
		}
	}

	/**
	 * 开始自动滚动方法
	 * mod by gzf 左右 “摇摆”循环
	 * 
	 * @param ms
	 */
	public void startAutoScroll() {
		startAutoScroll(millionseconds);
	}
	boolean dir = true;
	/**
	 * 如果当前显示的图片并不是自动滚动后显示的图片id（被手动滚动过），那该信息发送失效
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(Message m) {

			if (!imggallery.isWaitHander()) {
				int nextItem = 0;
				nextItem = imggallery.getSelectedItemPosition();
				if ( dir && nextItem < contentItemCount - 1) {
					nextItem++;
				} else {
					dir = false;
				}
				
				if(!dir && nextItem >0){
					nextItem--;
				}else{
					dir = true;
				}
				imggallery.setSelection(nextItem, false);
				pagercontrol.setCurrentPage(nextItem);
			} else {
				imggallery.setWaitHander(false);
			}
			sendMessageDelayed(Message.obtain(this, TICK_WHAT), millionseconds);
		}
	};

	public CancleInertiaGallery getImggallery() {
		return imggallery;
	}

	public PagerControl getPagercontrol() {
		return pagercontrol;
	}
	/****
	 * default CENTER_HORIZONTAL
	 * @param relativeParam   
	 */
	
	public void setPagerControlRelativeParamRole(int relativeParam){
		
		RelativeLayout.LayoutParams param = (LayoutParams) pagercontrol.getLayoutParams();
		param.addRule(relativeParam);
		pagercontrol.setLayoutParams(param);
	}
	
	public interface OnBannerItemClickListener {

		public void onBannerItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3);
	}

	public void OnBannerItemClickListener(OnBannerItemClickListener onitemclick) {
		mItemClicklistener = onitemclick;
	}
	public interface OnBannerItemSelectedListener {

		public void onBannerItemSelected(AdapterView<?> arg0, View arg1,
				int arg2, long arg3);

		public void onBannerNothingSelected(AdapterView<?> arg0);
	}

	public void OnBannerItemSelectedListener(OnBannerItemSelectedListener onitemSelected) {
		mItemSelectedlistener = onitemSelected;
	}
}
