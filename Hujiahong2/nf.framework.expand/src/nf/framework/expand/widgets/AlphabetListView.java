package nf.framework.expand.widgets;


import nf.framework.core.util.android.DensityUtil;
import nf.framework.expand.R;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * 右边带有字母查询的ListView
 * @author Davee
 */
public class AlphabetListView extends FrameLayout {
    private Context mContext;

    private UpFreshListView mListView;
    private LinearLayout alphabetLayout;
    private TextView mTextView;
    private AlphabetPositionListener positionListener;
    private AlphabetOnItemClickListener onItemClickListener;
    private float screenDensity;
    
    private Handler mHandler;
    
    private HideIndicator mHideIndicator = new HideIndicator();
    
    private int indicatorDuration = 1000;
    
    private int alphabetBg;
    
    public void setIndicatorDuration(int duration) {
        this.indicatorDuration = duration;
    }
    
    private final class HideIndicator implements Runnable {
        @Override
        public void run() {
            mTextView.setVisibility(View.INVISIBLE);
        }
    }

    public AlphabetListView(Context context) {
        super(context);
        init(context);
    }
    
    public AlphabetListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    
    /**
     * @param context
     */
    private void init(Context context) {
        mContext = context;

        screenDensity = context.getResources().getDisplayMetrics().density;
        
        mHandler = new Handler();

        mListView = new UpFreshListView(mContext);
        listViewEventListener();
        initAlphabetLayout(mContext);

        mTextView = new TextView(mContext);
        mTextView.setTextSize(convertDIP2PX(25));
        mTextView.setTextColor(Color.WHITE);
        mTextView.setBackgroundResource(R.drawable.alphabet_pop_bg);
        int pixels = convertDIP2PX(5);
        mTextView.setPadding(pixels, pixels, pixels, pixels);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setVisibility(View.INVISIBLE);
        FrameLayout.LayoutParams textLayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        textLayoutParams.gravity = Gravity.CENTER;
        mTextView.setLayoutParams(textLayoutParams);
    }
/**
 *列表事件监听
 * @param onItemClickListener
 */
    public void setOnItemClickListener(AlphabetOnItemClickListener onItemClickListener){
    	this.onItemClickListener=onItemClickListener;
    }
    
	public void listViewEventListener(){
		if(mListView!=null){
			mListView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					if(onItemClickListener!=null)
					onItemClickListener.OnItemClickListener(arg0, arg1, arg2, arg3);
				}
			});
		}
	}
	
    public UpFreshListView getmListView() {
    	return mListView;
	}

	/**
     * 添加Adapter
     * @param adapter
     * @param positionListener
     */
    public void setAdapter(ListAdapter adapter, AlphabetPositionListener positionListener) {
        if (positionListener == null) 
            throw new IllegalArgumentException("AlphabetPositionListener is required");

        mListView.setAdapter(adapter);
        this.positionListener = positionListener;

        this.addView(mListView);
        this.addView(alphabetLayout);
        this.addView(mTextView);
    }
    private void initAlphabetLayout(Context context) {
        alphabetLayout = new LinearLayout(context);
        alphabetLayout.setOrientation(LinearLayout.VERTICAL);
        FrameLayout.LayoutParams alphabetLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.FILL_PARENT);
        alphabetLayoutParams.gravity = Gravity.RIGHT;
        alphabetLayoutParams.setMargins(0,DensityUtil.dip2px(getContext(), 20), 0, DensityUtil.dip2px(getContext(), 20));
        alphabetLayout.setPadding(DensityUtil.dip2px(getContext(),5), DensityUtil.dip2px(getContext(),10), DensityUtil.dip2px(getContext(),5), DensityUtil.dip2px(getContext(),10));
        alphabetLayout.setLayoutParams(alphabetLayoutParams);
        alphabetLayout.setBackgroundResource(alphabetBg);
        final String[] alphabet = new String[]{"#","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.weight = 1;
        params.gravity = Gravity.CENTER_HORIZONTAL;
        for (int i=0;i<alphabet.length;i++) {
            TextView textView = new TextView(context);
            textView.setTextColor(Color.argb(255, 150, 150, 150));
            textView.setBackgroundColor(Color.argb(0, 255, 255, 0));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,11);
            textView.setText(alphabet[i]);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(params);
            textView.setTag(i);
            alphabetLayout.addView(textView);
        }
        alphabetLayout.setOnTouchListener(new OnTouchListener() {
        	int unit=0;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    	TextView tv=(TextView)alphabetLayout.findViewWithTag(0);
                    	unit=tv.getHeight();
                    	if(alphabetBg==0){
                    		alphabetLayout.setBackgroundColor(Color.argb(32, 0, 153, 204));
                    	}
                        int l = (int)(event.getY()/unit);
                        if(l>=alphabet.length){
                        		l=alphabet.length-1;
                        }
                        int pos = positionListener.getPosition(alphabet[l]);
                        if (pos != -1) {
                            mTextView.setText(alphabet[l]);
                            mTextView.setVisibility(View.VISIBLE);
                            mHandler.removeCallbacks(mHideIndicator);
                            mHandler.postDelayed(mHideIndicator, indicatorDuration);
                            mListView.setSelection(pos);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                    	l=(int)(event.getY()/unit);
                    	if(l>=alphabet.length){
                    		l=alphabet.length-1;
                    	}
                        pos = positionListener.getPosition(alphabet[l]);
                        if (pos != -1) {
                            mTextView.setText(alphabet[l]);
                            mTextView.setVisibility(View.VISIBLE);
                            mHandler.removeCallbacks(mHideIndicator);
                            mHandler.postDelayed(mHideIndicator, indicatorDuration);
                            mListView.setSelection(pos);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        alphabetLayout.setBackgroundResource(alphabetBg);
                        break;
                }
                return true;
            }
        });
    }

    public LinearLayout getAlphabetLayout() {
		return alphabetLayout;
	}

    public void setAlphabetLayoutBackground(int resId){
    	this.alphabetBg=resId;
    	 alphabetLayout.setBackgroundResource(alphabetBg);
    }
    
    public void setAlphabetTextView(int resColor, int textSize){
    	int count =alphabetLayout.getChildCount();
    	for(int i=0;i<count;i++){
    		View view =alphabetLayout.getChildAt(i);
    		if(view instanceof TextView){
    			((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    			((TextView) view).setTextColor(resColor);
    		}
    	}
    	
    }
    
	public int convertDIP2PX(float dip) {
        return (int)(dip*screenDensity + 0.5f*(dip>=0?1:-1));
    }
}
