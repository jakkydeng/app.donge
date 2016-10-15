/**
 * Summary: 应用中使用的WebChromeClient基类
 * Version 1.0
 * Date: 13-11-8
 * Time: 下午2:31
 * Copyright: Copyright (c) 2013
 */

package nf.framework.act.browser;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import nf.framework.expand.dialog.AbsBaseDialog;
import nf.framework.expand.dialog.AbsBaseDialog.DialogLeftBtnOnClickListener;
import nf.framework.expand.dialog.AbsBaseDialog.DialogRightBtnOnClickListener;
import nf.framework.expand.dialog.AbsBaseDialog.DialogUpBtnOnClickListener;
import nf.framework.expand.dialog.BaseDialog;
import nf.framework.expand.widgets.ProgressWebView;


public class InjectedChromeClient extends WebChromeClient {
    private final String TAG = "InjectedChromeClient";
    private JsCallJava mJsCallJava;
    private boolean mIsInjectedJS;
	protected PageProgressLisener pageProgressLisener;
    public InjectedChromeClient (String injectedName, Class injectedCls) {
        mJsCallJava = new JsCallJava(injectedName, injectedCls);
    }

    public InjectedChromeClient (JsCallJava jsCallJava) {
        mJsCallJava = jsCallJava;
    }
    
    /**
	 * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
	 */
	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			final JsResult result) {
		if(view.getContext() instanceof Activity){
			Activity activity =(Activity)view.getContext();
			if(activity.isFinishing())
			return true;
		}
		final BaseDialog baseDialog = new BaseDialog(view.getContext(),
				AbsBaseDialog.DIALOG_BUTTON_STYLE_ONE);
		baseDialog.show();
		baseDialog.setTitleText("提示");
		baseDialog.setContent(message);
		baseDialog.setBtnName("确定", null, null);
		baseDialog.setCancelable(false);
		// 不需要绑定按键事件 屏蔽keycode等于84之类的按键
		baseDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				Log.v("onJsAlert", "keyCode==" + keyCode + "event=" + event);
				return true;
			}
		});
		baseDialog
				.setDialogUpBtnOnClickListener(new DialogUpBtnOnClickListener() {
					@Override
					public void onButtonClick(View upBtn) {
						result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
					}
				});
		return true;
	}
	
	
    @Override
    public void onProgressChanged (WebView view, int newProgress) {
		Log.e("hjh =====>",newProgress+"=====>100");
        //为什么要在这里注入JS
        //1 OnPageStarted中注入有可能全局注入不成功，导致页面脚本上所有接口任何时候都不可用
        //2 OnPageFinished中注入，虽然最后都会全局注入成功，但是完成时间有可能太晚，当页面在初始化调用接口函数时会等待时间过长
        //3 在进度变化时注入，刚好可以在上面两个问题中得到一个折中处理
        //为什么是进度大于25%才进行注入，因为从测试看来只有进度大于这个数字页面才真正得到框架刷新加载，保证100%注入成功
        if (newProgress <= 25) {
            mIsInjectedJS = false;
        } else if (!mIsInjectedJS) {
            view.loadUrl(mJsCallJava.getPreloadInterfaceJS());
            mIsInjectedJS = true;
            Log.d(TAG, "hjh inject js interface completely on progress " + newProgress);
        }
        super.onProgressChanged(view, newProgress);
        
        onExpandProgressChanged(view, newProgress);
    }
    protected void onExpandProgressChanged(WebView view, int newProgress) {
		// TODO Auto-generated method stub
		if(view instanceof ProgressWebView){
			ProgressWebView webView =(ProgressWebView)view;
			ProgressBar progressbar =webView.getProgressbar();
			
		    if (newProgress == 100) {
                progressbar.setVisibility(View.GONE);
				if (pageProgressLisener!=null)
				pageProgressLisener.OnFinished(view);
            } else {
                if (progressbar.getVisibility() == View.GONE)
                    progressbar.setVisibility(View.VISIBLE);
                progressbar.setProgress(newProgress);
            }
		}
	}

	/**
	 * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
	 */
	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			final JsResult result) {
		if(view.getContext() instanceof Activity){
			Activity activity =(Activity)view.getContext();
			if(activity.isFinishing())
			return true;
		}
		final BaseDialog baseDialog = new BaseDialog(view.getContext(),
				AbsBaseDialog.DIALOG_BUTTON_STYLE_TWO);
		baseDialog.show();
		baseDialog.setTitleText("提示");
		baseDialog.setContent(message);
		// 禁止响应按back键的事件
		baseDialog.setCancelable(false);
		// 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
		baseDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				Log.v("onJsConfirm", "keyCode==" + keyCode + "event=" + event);
				return true;
			}
		});
		baseDialog.setBtnName(null, "确定", "取消");
		baseDialog
				.setDialogLeftBtnOnClickListener(new DialogLeftBtnOnClickListener() {

					@Override
					public void onButtonClick(View leftBtn) {
						// TODO Auto-generated method stub
						result.confirm();
					}
				});
		baseDialog
				.setDialogRightBtnOnClickListener(new DialogRightBtnOnClickListener() {

					@Override
					public void onButtonClick(View rightBtn) {
						// TODO Auto-generated method stub
						result.cancel();
					}
				});
		baseDialog.setOnCancelListener(new OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				result.cancel();
			}
		});
		return true;
	}
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
        result.confirm(mJsCallJava.call(view, message));
        return true;
    }
	public   interface PageProgressLisener{
		void OnFinished(WebView webView);
	}
	public void setPageProgressLinser(PageProgressLisener pageProgressLinser){
		this.pageProgressLisener = pageProgressLinser;
	}



}
