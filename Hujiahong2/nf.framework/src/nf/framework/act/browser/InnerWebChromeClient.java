package nf.framework.act.browser;

import nf.framework.expand.dialog.AbsBaseDialog;
import nf.framework.expand.dialog.AbsBaseDialog.DialogLeftBtnOnClickListener;
import nf.framework.expand.dialog.AbsBaseDialog.DialogRightBtnOnClickListener;
import nf.framework.expand.dialog.AbsBaseDialog.DialogUpBtnOnClickListener;
import nf.framework.expand.dialog.BaseDialog;
import nf.framework.expand.dialog.EditTextDialog;
import nf.framework.expand.dialog.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnKeyListener;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 * http://618119.com/archives/2010/12/20/199.html
 */

public class InnerWebChromeClient extends WebChromeClient {

	private ProgressDialog progressBarDialog = null;

	@Override
	public void onCloseWindow(WebView window) {
		super.onCloseWindow(window);

	}

	@Override
	public boolean onCreateWindow(WebView view, boolean dialog,
			boolean userGesture, Message resultMsg) {
		return super.onCreateWindow(view, dialog, userGesture, resultMsg);
	}

	/**
	 * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
	 */
	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			final JsResult result) {
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

	public boolean onJsBeforeUnload(WebView view, String url, String message,
			JsResult result) {
		return super.onJsBeforeUnload(view, url, message, result);
	}

	/**
	 * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
	 */
	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			final JsResult result) {
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

	/**
	 * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
	 * window.prompt('请输入您的域名地址', '618119.com');
	 */
	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, final JsPromptResult result) {
		final EditTextDialog baseDialog = new EditTextDialog(view.getContext(),
				AbsBaseDialog.DIALOG_BUTTON_STYLE_TWO);
		baseDialog.show();
		baseDialog.setTitleText("提示");
		baseDialog.setEditTextContent(defaultValue);
		// 禁止响应按back键的事件
		baseDialog.setCancelable(false);
		baseDialog.setOnKeyListener(new OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				Log.v("onJsPrompt", "keyCode==" + keyCode + "event=" + event);
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
		return true;
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		super.onProgressChanged(view, newProgress);
		try {
			if (newProgress == 100) {
				if (progressBarDialog != null && progressBarDialog.isShowing()) {
					progressBarDialog.dismiss();
					progressBarDialog = null;
					view.requestFocus();
				}
			} else {
				if (progressBarDialog == null) {
					progressBarDialog = new ProgressDialog(view.getContext());
				}
				if (progressBarDialog != null && !progressBarDialog.isShowing()) {
					progressBarDialog.show();
				}
			}
		} catch (Exception e) {

		}
	}
}
