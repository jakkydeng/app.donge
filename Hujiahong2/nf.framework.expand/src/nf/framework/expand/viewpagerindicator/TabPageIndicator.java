/*
 * Copyright (C) 2011 The Android Open Source Project
 * Copyright (C) 2011 Jake Wharton
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nf.framework.expand.viewpagerindicator;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import nf.framework.core.util.android.DensityUtil;
import nf.framework.expand.R;
import android.R.bool;
import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class TabPageIndicator extends HorizontalScrollView implements PageIndicator {
    /** Title text used when no title is provided by the adapter. */
    private static final CharSequence EMPTY_TITLE = "";

    /**
     * Interface for a callback when the selected tab has been reselected.
     */
    public interface OnTabReselectedListener {
        /**
         * Callback when the selected tab has been reselected.
         *
         * @param position Position of the current center item.
         */
        void onTabReselected(int position);
    }

    private Runnable mTabSelector;

    private final OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
            TabView tabView = (TabView)view;
            final int oldSelected = mViewPager.getCurrentItem();
            final int newSelected = tabView.getIndex();
            mViewPager.setCurrentItem(newSelected);
            if (oldSelected == newSelected && mTabReselectedListener != null) {
                mTabReselectedListener.onTabReselected(newSelected);
            }
//            tabView.setPointVisiable(false);
        }
    };

    private final IcsLinearLayout mTabLayout;

    private ViewPager mViewPager;
    private ViewPager.OnPageChangeListener mListener;

    private int mMaxTabWidth;
    private int mSelectedTabIndex;
    
    private OnTabReselectedListener mTabReselectedListener;

    private int mTabViewGravity = Gravity.CENTER;
    
    private int mTabViewBackground = R.drawable.expand_tab_indicator_bg;
    
    private int mTabViewPaddingTop =10;
    private int mTabViewPaddingLeft =0;
    private int mTabViewPaddingRight =0;
    private int mTabViewPaddingBottom =10;
    
    private int mTabViewTextSize =14;
    private int mTabViewTextColor = android.R.color.black;
    
    private TabImgPosition mImgPosition= TabImgPosition.Left;
    public TabPageIndicator(Context context) {
        this(context, null);
    }

    public TabPageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        setHorizontalScrollBarEnabled(false);
        mTabLayout = new IcsLinearLayout(context,attrs);
        addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT, MATCH_PARENT));
    }

    public void setOnTabReselectedListener(OnTabReselectedListener listener) {
        mTabReselectedListener = listener;
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int)(MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }

    private void addTab(int index, CharSequence text, int iconResId,boolean readPoint) {
        final TabView tabView = new TabView(getContext());
        tabView.mIndex = index;
        tabView.setFocusable(true);
        tabView.setOnClickListener(mTabClickListener);
        tabView.setText(text);
        tabView.setSingleLine(true);
        tabView.setTextColor(getResources().getColorStateList(mTabViewTextColor));
        tabView.setTextSize(TypedValue.COMPLEX_UNIT_SP, mTabViewTextSize);
        tabView.setGravity(mTabViewGravity);
        tabView.setBackgroundResource(mTabViewBackground);
        tabView.setPadding(mTabViewPaddingLeft,mTabViewPaddingTop, mTabViewPaddingRight,mTabViewPaddingBottom);
        tabView.setImageResource(iconResId);
        tabView.setTag(index);
        tabView.setPointVisiable(false);
        mTabLayout.addView(tabView, new LinearLayout.LayoutParams(0, MATCH_PARENT, 1));
        
        
    }

    public void setPointVisiable(int index,boolean visiable){
    	TabView tabView  =(TabView) mTabLayout.getChildAt(index);
    	if(tabView !=null)
    	tabView.setPointVisiable(visiable);
    }
    
    public boolean isPointVisiable(int index){
    	TabView tabView  =(TabView) mTabLayout.getChildAt(index);
    	if(tabView !=null){
    		return tabView.isPointVisiable();
    	}
    	return false;
    }
    
    public void setTabViewGravity(int gravity){
    	
    	this.mTabViewGravity=gravity;
    	for(int i= 0;i<mTabLayout.getChildCount();i++){
    		View childView =mTabLayout.getChildAt(i);
    		if(childView instanceof TabView){
    			((TabView) childView).setGravity(mTabViewGravity);
    		}
    	}
    }
    public void setImgPosition(TabImgPosition position){
    	
    	this.mImgPosition =position;
    	notifyDataSetChanged();
    }
    
    public void setTabViewBackground(int backgroundDrawable){
    	
    	this.mTabViewBackground = backgroundDrawable;
    	for(int i= 0;i<mTabLayout.getChildCount();i++){
    		View childView =mTabLayout.getChildAt(i);
    		if(childView instanceof TabView){
    			childView.setBackgroundResource(backgroundDrawable);
    		}
    	}
    }
    public void setTabViewPadding(int left,int top,int right ,int bottom){
    	this.mTabViewPaddingLeft =left;
    	this.mTabViewPaddingTop =top;
    	this.mTabViewPaddingRight =right;
    	this.mTabViewPaddingBottom =bottom;
    	for(int i= 0;i<mTabLayout.getChildCount();i++){
    		View childView =mTabLayout.getChildAt(i);
    		if(childView instanceof TabView){
    			childView.setPadding(left, top, right, bottom);
    		}
    	}
    }
    public void setTabViewTextSize(int textSize){
    	
    	this.mTabViewTextSize =textSize;
    	for(int i= 0;i<mTabLayout.getChildCount();i++){
    		View childView =mTabLayout.getChildAt(i);
    		if(childView instanceof TabView){
    			((TabView) childView).setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    		}
    	}
    }
    
    public void setTabViewTextColor(int textColor){
    	
    	this.mTabViewTextColor =textColor;
    	for(int i= 0;i<mTabLayout.getChildCount();i++){
    		View childView =mTabLayout.getChildAt(i);
    		if(childView instanceof TabView){
    			((TabView) childView).setTextColor(getResources().getColorStateList(mTabViewTextColor));
    		}
    	}
    }
   
    @Override
    public void onPageScrollStateChanged(int arg0) {
        if (mListener != null) {
            mListener.onPageScrollStateChanged(arg0);
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        if (mListener != null) {
            mListener.onPageScrolled(arg0, arg1, arg2);
//            setPointVisiable(arg0,false);
        }
    }

    @Override
    public void onPageSelected(int arg0) {
        setCurrentItem(arg0);
        if (mListener != null) {
            mListener.onPageSelected(arg0);
        }
    }

    @Override
    public void setViewPager(ViewPager view) {
        if (mViewPager == view) {
            return;
        }
        if (mViewPager != null) {
            mViewPager.setOnPageChangeListener(null);
        }
        final PagerAdapter adapter = view.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        mViewPager = view;
        view.setOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        mTabLayout.removeAllViews();
        PagerAdapter adapter = mViewPager.getAdapter();
        IconPagerAdapter iconAdapter = null;
        if (adapter instanceof IconPagerAdapter) {
            iconAdapter = (IconPagerAdapter)adapter;
        }
        final int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            CharSequence title = adapter.getPageTitle(i);
            if (title == null) {
                title = EMPTY_TITLE;
            }
            int iconResId = 0;
            if (iconAdapter != null) {
                iconResId = iconAdapter.getIconResId(i);
            }
            addTab(i, title, iconResId,iconAdapter.getReadPoint(i));
            
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    @Override
    public void setViewPager(ViewPager view, int initialPosition) {
        setViewPager(view);
        setCurrentItem(initialPosition);
    }

    @Override
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
         mViewPager.setCurrentItem(item);

        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final View child = mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
                animateToTab(item);
            }
        }
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mListener = listener;
    }

    private class TabView extends RelativeLayout {
        private int mIndex;
        private TextView textView;
        private TextView redPoint;
        private ImageView imageView;
        public TabView(Context context) {
			this(context,null);
		}
	
		public TabView(Context context,AttributeSet attrs) {
            super(context,attrs);
            initView();
		}
		public void initView() {
			LinearLayout linearLayout =new LinearLayout(getContext());
			RelativeLayout.LayoutParams liParam =new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			liParam.addRule(RelativeLayout.CENTER_IN_PARENT);
			linearLayout.setGravity(Gravity.CENTER);
			
			imageView =new ImageView(getContext());
			imageView.setId(1);
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.CENTER_INSIDE);
			int size = DensityUtil.dip2px(getContext(),30);
			LinearLayout.LayoutParams imageParam =new LinearLayout.LayoutParams(size,size);
			imageView.setLayoutParams(imageParam);
			linearLayout.addView(imageView);
			imageView.setVisibility(View.GONE);
			
			textView =new TextView(getContext());
			LinearLayout.LayoutParams textParam =new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			textView.setLayoutParams(textParam);
			linearLayout.addView(textView);
			
			redPoint =new TextView(getContext());
			redPoint.setTextSize(9);
			redPoint.setGravity(Gravity.CENTER);
			redPoint.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_circle_1));
            int lib = DensityUtil.dip2px(getContext(),10);
            RelativeLayout.LayoutParams redPointParam =new RelativeLayout.LayoutParams(lib,lib);
            redPointParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            redPointParam.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            redPoint.setLayoutParams(redPointParam);
//			linearLayout.addView(redPoint);


			addView(linearLayout,liParam);
            addView(redPoint,redPointParam);
            redPoint.setVisibility(INVISIBLE);

		}
		public void setImageResource(int iconResId){
			if(iconResId==0){
				imageView.setVisibility(View.GONE);
				return;
			}
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageResource(iconResId);
		}
		public void setTextSize(int complexUnitSp, int mTabViewTextSize) {
			textView.setTextSize(complexUnitSp, mTabViewTextSize);
		}
		public void setTextColor(ColorStateList colorStateList) {
			textView.setTextColor(colorStateList);
		}
		public void setSingleLine(boolean singleLine) {
			textView.setSingleLine(singleLine);
		}
		public void setText(CharSequence text) {
			textView.setText(text);
		}
		
		public void setPointVisiable(boolean visiable){
			if(visiable){
				redPoint.setVisibility(View.VISIBLE);
			}else{
				redPoint.setVisibility(View.GONE);
			}
		}
		
		public boolean isPointVisiable(){
			return redPoint.isShown();
		}
        @Override
        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            // Re-measure if we went beyond our maximum size.
            if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
                super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth, MeasureSpec.EXACTLY),
                        heightMeasureSpec);
            }
        }
        public int getIndex() {
            return mIndex;
        }
    }
    
    public enum TabImgPosition{
    	
    	Left,Top,Right,Bottom
    }

	
}
