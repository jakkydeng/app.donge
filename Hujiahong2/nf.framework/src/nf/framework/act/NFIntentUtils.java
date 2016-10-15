package nf.framework.act;

import nf.framework.act.browser.InnerBrowserActivity;
import nf.framework.act.browser.InnerBrowserByTitleActivity;
import android.content.Context;
import android.content.Intent;

public class NFIntentUtils {

	/**
	 * 
	 * @param context
	 * @param source
	 * @param title
	 * @param url
	 */
	public static void intentToInnerBrowserAct(Context context,String source,String title,String url) {

		Intent intent=new Intent();
		intent.setClass(context, InnerBrowserActivity.class);
		intent.putExtra(InnerBrowserActivity.INTENT_SOURCE, source);
		intent.putExtra(InnerBrowserActivity.INTENT_TITLE,title);
		intent.putExtra(InnerBrowserActivity.INTENT_URL, url);
		context.startActivity(intent);
		
	}

	/**
	 * 
	 * @param context
	 * @param source
	 * @param title
	 * @param url
	 */
	public static void intentToInnerBrowserByTitleAct(Context context,String source,String title,String url) {
		Intent intent=new Intent();
		intent.setClass(context, InnerBrowserByTitleActivity.class);
		intent.putExtra(InnerBrowserByTitleActivity.INTENT_TITLE,title);
		intent.putExtra(InnerBrowserByTitleActivity.INTENT_URL, url);
		context.startActivity(intent);
	}
	
}
