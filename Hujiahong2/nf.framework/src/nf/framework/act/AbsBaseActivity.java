package nf.framework.act;

/**
 * 功能： TODO 
 * author      date          time      
 * ─────────────────────────────────────────────
 * niufei     2012-11-8       上午10:57:50
 * Copyright (c) 2012, TNT All Rights Reserved.
 */


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import nf.framework.R;
import nf.framework.act.MyLeftRightGestureListener.LeftRightGestureListenerCallback;
import nf.framework.act.lockscreen.LockScreen;
import nf.framework.expand.dialog.AbsBaseDialog;
import nf.framework.expand.dialog.BaseDialog;
import nf.framework.expand.dialog.ProgressDialog;
import nf.framework.statistic.MobStatisticUtils;

/***
 *
 *
 * @author niufei
 *
 */
public abstract class AbsBaseActivity extends FragmentActivity{

	protected TextView top_textview;
	protected TextView right_textview;
	protected ImageButton leftButton;
	protected ImageButton rightButton;
	protected TextView rightTxtBtn;
	protected LinearLayout mainlayout;
	protected ViewGroup framelayout;
	public ViewGroup navigationBarLayout;
	private boolean isLeftRightGesture = false;
	private GestureDetector leftRightDetector = null;
	private LockScreen mLockScreen = new LockScreen(this);
	private ProgressDialog progressDialog=null;
	private MobStatisticUtils mobStatisticUtils;
	public TextView rightTextBtn2;
	public ImageButton rightButton2;
	public View  common_base_top_line;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mobStatisticUtils=new MobStatisticUtils(this);

		setBaseContentView();
		this.leftRightDetector = new GestureDetector(getApplicationContext(),
				new MyLeftRightGestureListener(new MyCallBack()));
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mobStatisticUtils.onStatisticResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mobStatisticUtils.onStatisticPause();
	}
	/***
	 * 设置布局
	 * 如右侧文字
	 */
	private void setBaseContentView() {

		setContentView(R.layout.common_basemain);
		framelayout = (ViewGroup) this.findViewById(R.id.common_base_main_fragmentlayout);
		top_textview = (TextView) this.findViewById(R.id.common_base_top_title_textview);
		leftButton = (ImageButton) findViewById(R.id.common_base_toptitle_left_img);
		rightButton = (ImageButton) findViewById(R.id.common_base_toptitle_right_img);
		rightTxtBtn = (TextView) findViewById(R.id.common_base_toptitle_right_txt);
		rightTxtBtn.setTextSize(16);
		right_textview = (TextView) findViewById(R.id.common_base_toptitle_right_textview);
		navigationBarLayout = (ViewGroup) findViewById(R.id.common_basemain_navigationbar_layout);
		mainlayout = (LinearLayout) findViewById(R.id.common_basemain_main_layout);
		rightTextBtn2 = (TextView) findViewById(R.id.common_base_toptitle_right_txt2);
		rightTxtBtn.setTextSize(16);
		common_base_top_line = findViewById(R.id.common_base_top_line);
		rightButton2 = (ImageButton) findViewById(R.id.common_base_toptitle_right_img2);

	}

	/***
	 * 当右上角提示标示数字发生变化时可调用更新
	 */
	protected void setRightReferText(int referNum) {
		if (right_textview == null) {
			return;
		}
		if (referNum == 0) {
			right_textview.setVisibility(View.GONE);
		} else {
			right_textview.setVisibility(View.VISIBLE);
			right_textview.setText(referNum + "");
		}
	}

	/**
	 * 制定位置替换fragment replace()这个方法只是在上一个Fragment不再需要时采用的简便方法。
	 *
	 * @param layoutId
	 *            制定activity中的layout位置
	 * @param removeFragment
	 * @param addFragment
	 */
	public void replaceFragmentView(FragmentManager fragmentManager, int layoutId, Fragment removeFragment, Fragment addFragment) {
		if (!addFragment.isAdded()) {
			FragmentTransaction ft = fragmentManager.beginTransaction();
			ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
			if (removeFragment.isAdded()) {
				ft.remove(removeFragment);
			}
			ft.replace(layoutId, addFragment);
			// ft.addToBackStack(null);

			// /解决IllegalStateException:
			// /2014.03.17 niufei
			// /Can not perform this action after onSaveInstanceState
			// ft.commit();
			ft.commitAllowingStateLoss();
			ft.show(addFragment);
		}
	}

	/***
	 * 正确的切换方式是add()，切换时hide()，add()另一个Fragment；再次切换时，只需hide()当前，show()另一个。
	 *
	 * @param fragmentManager
	 * @param layoutId
	 * @param removeFragment
	 * @param addFragment
	 */
	public void setFragmentView(FragmentManager fragmentManager, int layoutId, Fragment removeFragment, Fragment addFragment) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		if (!addFragment.isAdded()) { // 先判断是否被add过
			transaction.hide(removeFragment).add(layoutId, addFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
			transaction.show(addFragment);
		} else {
			transaction.hide(removeFragment).show(addFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
		}
	}

	/***
	 * 正确的切换方式是add()，切换时hide()，add()另一个Fragment；再次切换时，只需hide()当前，show()另一个。
	 *
	 * 切换时间很短 100毫秒
	 *
	 * @param fragmentManager
	 * @param layoutId
	 * @param removeFragment
	 * @param addFragment
	 */
	public void setFragmentViewTimeShort(FragmentManager fragmentManager, int layoutId, Fragment removeFragment, Fragment addFragment) {
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.setCustomAnimations(R.anim.common_fade_in_short, R.anim.common_fade_out_short);
		if (!addFragment.isAdded()) { // 先判断是否被add过
			transaction.hide(removeFragment).add(layoutId, addFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
			transaction.show(addFragment);
		} else {
			transaction.hide(removeFragment).show(addFragment).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
		}
	}

	/**
	 * 制定位置替换fragment
	 *
	 * @param layoutId
	 *            制定activity中的layout位置
	 * @param removeFragment
	 * @param addFragment
	 */
	public void setFragmentView(int layoutId, Fragment addFragment) {
		if (!addFragment.isAdded()) {
			FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
			ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
			ft.add(layoutId, addFragment);
			// ft.addToBackStack(null);
			ft.show(addFragment);
			ft.commitAllowingStateLoss();
		}
	}

	public void showProgressBar(){
		if(progressDialog==null){
			progressDialog	=new ProgressDialog(this);
		}
		if(!progressDialog.isShowing()&&!isFinishing())
			progressDialog.show(false);
	}

	public void dismissProgressBar(){

		if(progressDialog!=null){
			progressDialog.dismiss();
		}
		progressDialog=null;
	}
	/**
	 * 当用户信息发生变化时执行操作
	 */
	protected boolean AfterUserInfoChanged(Object... params) {

		int userId = (Integer) params[0];
		String userName = (String) params[1];
		return true;
	}

	/**
	 * 修改按钮的样式和字体颜色
	 *
	 * @param selectBtn
	 * @param unSelectBtn
	 */
	protected void setTabBtnStyle(Button selectBtn, Button unSelectBtn) {
		if (unSelectBtn != null && selectBtn != null) {
			unSelectBtn.setSelected(false);
			selectBtn.setSelected(true);
		}
	}

	/**
	 * 修改按钮的样式和字体颜色
	 *
	 * @param selectBtn
	 * @param unSelectBtn
	 */
	protected void setTabBtnStyle(TextView selectBtn, TextView... unSelectBtns) {
		if (unSelectBtns != null && selectBtn != null) {
			for (TextView unSelectBtn : unSelectBtns) {
				unSelectBtn.setSelected(false);
			}
			selectBtn.setSelected(true);
		}
	}
	public boolean isAddLeftRightGesture() {
		return isLeftRightGesture;
	}

	/***
	 * 设置左右滑屏
	 * @param isLeftRightGesture
	 */
	public void setLeftRightGesture(boolean isLeftRightGesture) {
		this.isLeftRightGesture = isLeftRightGesture;
	}
	/**
	 * 设置锁屏
	 * @param isLockScreen
	 */
	public void setLockScreen(boolean isLockScreen){
		this.mLockScreen.setLockScreen(isLockScreen);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		try{
			this.mLockScreen.startLockRunnable();

			if(this.isLeftRightGesture && this.mainlayout != null){
				float ex = event.getX();
				float ey = event.getY();

				boolean flag = false;

				Rect listviewRec = new Rect();
				this.mainlayout.getGlobalVisibleRect(listviewRec);

				if ((listviewRec.left < ex && ex < listviewRec.right)
						&& (listviewRec.top < ey && ey < listviewRec.bottom)
						&& listviewRec.top != 0) {
					// NewsUtils.log(getClass(), "### Rect right:" + listviewRec.right +
					// " Rect bottom:" + listviewRec.bottom + " ex:" + ex + " ey:" +
					// ey);
					flag = leftRightDetector.onTouchEvent(event);
					// NewsUtils.log(getClass(), "### leftRightDetector flag: " + flag);
					if (flag) {
						return flag;
					}
				}

				flag = super.dispatchTouchEvent(event);
				return flag;
			}
			else{
				return super.dispatchTouchEvent(event);
			}
		}
		catch(OutOfMemoryError e){

		}

		return false;
	}

	private class MyCallBack implements LeftRightGestureListenerCallback {

		@Override
		public void onLeft() {
			AbsBaseActivity.this.onBackPressed();
		}

		@Override
		public void onRight() {
		}

	}


	public static void showInfoDialog(Context context,String message){

		BaseDialog dialog = new BaseDialog(context,AbsBaseDialog.DIALOG_BUTTON_STYLE_ONE);
		dialog.show();
		dialog.setTitle(R.drawable.expand_dialog_login_logo,"提示");
		dialog.setContent(message);
	}

	protected void showToast(String msg){
		if(TextUtils.isEmpty(msg)){
			return;
		}
		Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
	}
}
