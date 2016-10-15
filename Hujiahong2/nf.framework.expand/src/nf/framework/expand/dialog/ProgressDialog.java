package nf.framework.expand.dialog;

/**
 * ProgressDialog.java
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-7-25       下午02:06:47
 * Copyright (c) 2012, TNT All Rights Reserved.
*/


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;

import nf.framework.expand.R;

public class ProgressDialog extends Dialog{
	
	private  Context mcontext=null;
	private final int CENTERVIEWID=1;
	private final int PROGRESSBAR=2;
	//private CircleProgressBar circleProgressBar;
	CircleProgressBar progressWithArrow;
	CircleProgressBar progressWithoutBg;
	private Handler handler;
	public ProgressDialog(Context context) {
		super(context, R.style.expand_progress_dialog_style);
		mcontext=	context;
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		View view = LayoutInflater.from(mcontext).inflate(
				R.layout.circle_progresslayout, null);
		CircleProgressBar circleProgressBar = (CircleProgressBar) view.findViewById(R.id.progress2);
//		progressWithArrow = (CircleProgressBar) view.findViewById(R.id.progressWithArrow);
//		progressWithoutBg = (CircleProgressBar) view.findViewById(R.id.progressWithoutBg);
		circleProgressBar.setColorSchemeResources(R.color.main_green);
	//	progressWithArrow.setColorSchemeResources(android.R.color.holo_orange_light);
//		progressWithoutBg.setColorSchemeResources(android.R.color.holo_red_light);


//		handler = new Handler();
//		for (int i = 0; i < 10; i++) {
//			final int finalI = i;
//			handler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					if(finalI *10>=90){
//						//circleProgressBar.setVisibility(View.INVISIBLE);
//						circleProgressBar.setProgress(99);
//					}else {
//						circleProgressBar.setProgress(finalI * 10);
//					}
//				}
//			},200*(i+1));
//		}

//		RelativeLayout llpd=new RelativeLayout(mcontext);
//		llpd.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.FILL_PARENT));
//		RelativeLayout.LayoutParams centerpblp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,1);
//		centerpblp.addRule(RelativeLayout.CENTER_IN_PARENT);
//		TextView centerview=new TextView(mcontext);
//		centerview.setId(CENTERVIEWID);
//		llpd.addView(centerview,centerpblp);
//
//		ProgressBar pb=new ProgressBar(mcontext);
//		pb.setIndeterminateDrawable(mcontext.getResources().getDrawable(R.drawable.expand_progressbar_bg));
//		pb.setIndeterminate(false);
//		pb.setId(PROGRESSBAR);
//		RelativeLayout.LayoutParams pblp=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//		pblp.addRule(RelativeLayout.BELOW,CENTERVIEWID);
//		pblp.addRule(RelativeLayout.CENTER_HORIZONTAL);
////		Resources r = getContext().getResources();
////		float pxValue = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
////		int pxval=Float.floatToIntBits(pxValue);
//		pblp.setMargins(0,100, 0, 0);
//		llpd.addView(pb,pblp);
		this.setContentView(view);
	}
	/**
	 * 设定正在加载的状态
	 */
	public  void show(){
		// 点击对话框外部取消对话框显示
		setCanceledOnTouchOutside(true);
		super.show();  
	}
	/**
	 * 设定正在加载的状态
	 */
	public void show(boolean cancelTouchOutside) {
		// 点击对话框外部取消对话框显示
		setCanceledOnTouchOutside(cancelTouchOutside);
		super.show();
	}
	/**
	 * 设定未加载状态
	 */
	public void dismiss(){
			super.dismiss();
	}
	
}
