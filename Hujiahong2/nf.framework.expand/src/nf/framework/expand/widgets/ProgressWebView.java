package nf.framework.expand.widgets;

import nf.framework.core.util.android.DensityUtil;
import nf.framework.expand.R;
import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 带进度条的WebView
 * @author 农民伯伯
 * @see http://www.cnblogs.com/over140/archive/2013/03/07/2947721.html
 * 
 */
public class ProgressWebView extends WebView {

    protected ProgressBar progressbar;
    public ProgressWebView(Context context) {
        super(context);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,DensityUtil.dip2px(getContext(), 3), 0, 0));
        progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.expand_progress_web_horizontal));
        addView(progressbar);
        setWebChromeClient(new ProgressWebChromeClient());
    }
    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,DensityUtil.dip2px(getContext(), 3), 0, 0));
        progressbar.setProgressDrawable(getResources().getDrawable(R.drawable.expand_progress_web_horizontal));
        addView(progressbar);
        setWebChromeClient(new ProgressWebChromeClient());
    }

    
    
    public ProgressBar getProgressbar() {
		return progressbar;
	}

	public class ProgressWebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}