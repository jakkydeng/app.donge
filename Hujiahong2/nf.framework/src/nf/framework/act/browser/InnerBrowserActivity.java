package nf.framework.act.browser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import nf.framework.R;
import nf.framework.act.AbsBaseActivity;
import nf.framework.core.LoadSysSoft;
import nf.framework.core.util.android.AndroidVersionCheckUtils;
import nf.framework.core.util.android.CloseActivityClass;
import nf.framework.expand.widgets.ProgressWebView;

public class InnerBrowserActivity extends AbsBaseActivity {
	private Context mcontext;
	private ProgressWebView webview;
	public ProgressWebView getWebview() {
		return webview;
	}

	public void setWebview(ProgressWebView webview) {
		this.webview = webview;
	}

	private Intent homeIntent;
	private String urlAddress;
	private ImageView gobackBtn;
	private ImageView goforwardBtn;
	private ImageView refreshBtn;
	private ImageView browserBtn;
	private String intentSource;
	public RelativeLayout relativeLayout;
	public static final String INTENT_TITLE = "param_title";
	public static final String INTENT_URL = "url";
	public static final String INTENT_SOURCE = "intentSource";
    public static final String INTENT_SIGN = "intentSign";//显示报名按钮


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mcontext = this;
		CloseActivityClass.activityList.add(this);
		initView();
	}

	@SuppressLint("SetJavaScriptEnabled")
	private void initView() {
		View view = LayoutInflater.from(mcontext).inflate(R.layout.common_web_browser_main, super.mainlayout,false);
		super.mainlayout.addView(view);
		super.leftButton.setVisibility(View.VISIBLE);
		super.leftButton.setImageResource(R.drawable.common_navigate_back_btn);
		relativeLayout = (RelativeLayout) this.findViewById(R.id.common_web_main_top_bar_layout);
		gobackBtn = (ImageView) this
				.findViewById(R.id.common_web_toolbar_goback_btn);
		goforwardBtn = (ImageView) this
				.findViewById(R.id.common_web_toolbar_goforward_btn);
		refreshBtn = (ImageView) this
				.findViewById(R.id.common_web_toolbar_refresh_btn);
		browserBtn = (ImageView) this
				.findViewById(R.id.common_web_toolbar_browser_btn);
		webview = (ProgressWebView) this.findViewById(R.id.common_web_main_web_context);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setUseWideViewPort(true);
		webview.setVerticalScrollBarEnabled(true);
		webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);  //设置 缓存模式
		webview.getSettings().setBuiltInZoomControls(true); 
		if(AndroidVersionCheckUtils.hasHoneycomb())
		webview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
		webview.requestFocus();
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new InnerWebViewClient(this));
		webview.setWebChromeClient(new mWebChromeClient("JsCallBack",HostJsScope.class));

		gobackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (webview.canGoBack()) {
					webview.goBack();
				}
			}
		});
		goforwardBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (webview.canGoForward()) {
					webview.goForward();
				}
			}
		});
		super.leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				onBackPressed();
			}
		});
		refreshBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				webview.loadUrl(urlAddress);
			}
		});
		browserBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				String urlAddress = getIntent().getStringExtra(INTENT_URL);
//				if (!TextUtils.isEmpty(urlAddress)) {
//					new LoadSysSoft().OpenBrowser(mcontext, urlAddress);
//				}
			}
		});
	}

    protected void reloadWeb(){
        webview.loadUrl(urlAddress);
    }
	@Override
	protected void onResume() {
		super.onResume();
		webview.onResume();
		if(urlAddress==null){
			homeIntent = this.getIntent();
			String titleName = homeIntent.getStringExtra(INTENT_TITLE);
			if (!TextUtils.isEmpty(titleName)) {
				super.top_textview.setText(titleName);
			}
			urlAddress = homeIntent.getStringExtra(INTENT_URL);
			webview.loadUrl(urlAddress);
		}
	}

	public void setToolbarState(boolean clickable, ImageView imageView) {
		imageView.setClickable(clickable);
		if (imageView.equals(gobackBtn)) {
			imageView
					.setImageResource(clickable ? R.drawable.common_web_toolbar_goback_btn_clickable
							: R.drawable.common_web_toolbar_goback_btn_normal);
		} else if (imageView.equals(goforwardBtn)) {
			imageView
					.setImageResource(clickable ? R.drawable.common_web_toolbar_gofoward_btn_clickable
							: R.drawable.common_web_toolbar_goforward_btn_normal);
		}

	}

	protected void onPause() {
		super.onPause();
		webview.onPause();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		webview.clearCache(true);
		webview.clearHistory();
		finishActivity();
	}

	private void finishActivity() {

		if (intentSource != null) {
			new LoadSysSoft().openAPP(mcontext, mcontext.getPackageName());
		} else {
			setResult(RESULT_OK, homeIntent);
		}
		finish();
//		overridePendingTransition(R.anim.common_slide_up_in,
//				R.anim.common_slide_down_out);
		overridePendingTransition(R.anim.common_push_right_in,
				R.anim.common_push_right_out);
	}
	
	
	protected class mWebChromeClient extends InjectedChromeClient{
		
		public mWebChromeClient(String injectedName, Class injectedCls) {
			super(injectedName, injectedCls);
			// TODO Auto-generated constructor stub
		}
		@Override
		public void onExpandProgressChanged(WebView view, int newProgress) {
			super.onExpandProgressChanged(view, newProgress);
			if (newProgress == 100) {
				setToolbarState(true, gobackBtn);
				setToolbarState(view.canGoBack(), gobackBtn);
				setToolbarState(view.canGoForward(), goforwardBtn);
				webview.requestFocus();
			} else {
				setToolbarState(false, gobackBtn);
				setToolbarState(false, goforwardBtn);
			}
		}
	}
}
