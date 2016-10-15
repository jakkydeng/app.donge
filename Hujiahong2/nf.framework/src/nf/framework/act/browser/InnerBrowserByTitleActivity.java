package nf.framework.act.browser;

import nf.framework.R;
import nf.framework.act.AbsBaseActivity;
import nf.framework.core.util.android.CloseActivityClass;
import nf.framework.expand.widgets.ProgressWebView;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class InnerBrowserByTitleActivity extends AbsBaseActivity {

	private Context mcontext;
	private ProgressWebView detailwebview;
	private Intent homeIntent;
	public static final String INTENT_TITLE = "param_item";
	public static final String INTENT_URL = "URL";
	public static final String INTENT_ReferInfo = "ReferInfo";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mcontext = this;
		CloseActivityClass.activityList.add(this);

		initView();
		homeIntent = this.getIntent();
		String titleName = homeIntent.getStringExtra(INTENT_TITLE);
		String urlAddress = homeIntent.getStringExtra(INTENT_URL);
		super.top_textview.setText(titleName);
		detailwebview.loadUrl(urlAddress);
	}
	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		super.leftButton.setVisibility(View.VISIBLE);
		super.leftButton.setImageResource(R.drawable.common_navigate_back_btn);
		View webviewLayout = LayoutInflater.from(this).inflate(R.layout.common_web_layout,super.mainlayout,false);
		super.mainlayout.addView(webviewLayout);
		detailwebview=(ProgressWebView) findViewById(R.id.common_web_main_web_view);
		detailwebview.getSettings().setJavaScriptEnabled(true);
		detailwebview.canGoBack();
		detailwebview.setVerticalScrollBarEnabled(true);
		detailwebview.requestFocus();
		detailwebview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		detailwebview.setWebViewClient(new InnerWebViewClient(this));
		detailwebview.setWebChromeClient(new InjectedChromeClient("JsCallBack",InnerHostJsScope.class)); 
		detailwebview.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				detailwebview.requestFocus();
				return false;
			}
		});
		super.leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!detailwebview.canGoBack()) {
					finishActivity();
				} else {
					detailwebview.goBack();
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		detailwebview.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
		detailwebview.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (!detailwebview.canGoBack()) {
				// detailwebview.clearHistory();
				// detailwebview.clearCache(true);
				finishActivity();
			} else {
				detailwebview.goBack();  
			}
		}
		return false;
	}
	private void finishActivity() {
		detailwebview.clearCache(true);
		detailwebview.clearHistory();
		setResult(RESULT_OK, homeIntent);
		finish();
		overridePendingTransition(R.anim.common_push_right_in,
				R.anim.common_push_right_out);
	}
	public static class InnerHostJsScope extends HostJsScope{
		
		public static void getSuccessFeedBack(final WebView webView, String message) {
			alert(webView, message);
		}

		public static void getFailureFeedBack(WebView webView,String message) {
			alert(webView, message);
		}
	} 
}
