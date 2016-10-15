package nf.framework.act;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import nf.framework.expand.viewpagerindicator.IconPagerAdapter;
import nf.framework.expand.viewpagerindicator.TabPageIndicator;
import nf.framework.statistic.MobStatisticUtils;

/**
 * Tab页面手势滑动切换以及动画效果
 * 
 * @author D.Winter
 * 
 */
public abstract class AbsSlidingTabBarActivity extends AbsSlidingBaseActivity{

	private ViewPager mPager;// 页卡内容
	private List<TabBarVO> tabBarList = new ArrayList<TabBarVO>();// 页卡头标
	
	private List<Fragment> fragmentList = new ArrayList<Fragment>();
	protected TabPageIndicator indicator = null;
	private SectionsPagerAdapter mSectionsPagerAdapter;
	private ViewGroup mainLanderView;
	private MobStatisticUtils mobStatisticUtils;
	private boolean isAutoLoadTab =true;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mobStatisticUtils=new MobStatisticUtils(this);
		initView();
	}

	private void initView() {
		// 设置布局
		mainLanderView = (ViewGroup) LayoutInflater.from(this).inflate(getMainLayout(), null);
		super.mainlayout.addView(mainLanderView);
		indicator = (TabPageIndicator) findViewById(getTabBarLinearLayoutId());
		indicator.setVisibility(View.GONE);
		mPager = (ViewPager) findViewById(getViewPagerId());
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),fragmentList);
		mPager.setAdapter(mSectionsPagerAdapter);
		mSectionsPagerAdapter.notifyDataSetChanged();
		indicator.setViewPager(mPager);
		indicator.setOnPageChangeListener(new TabBarOnPageChangeListener());
		indicator.setTabViewPadding(30,10,30,10);
	}

	protected abstract int getMainLayout();

	protected abstract int getTabBarLinearLayoutId();

	protected abstract List<TabBarVO> makeTabBarList();

	protected abstract int getViewPagerId();

	protected abstract List<Fragment> getFragmentList(List<TabBarVO> tabBarList);
	
	public List<TabBarVO> getTabBarList() {
		return tabBarList;
	}

	public void afterFragmentFinished() {
		// TODO Auto-generated method stub
		
	}
	public void	onRebuildTabView(){
		
		new TabBarAsync().execute();
	}
	
	/***
	 * 
	 * @param tabBar
	 */
	public void setCurrentTabItem(TabBarVO tabBar) {
		if (tabBar == null || !tabBarList.contains(tabBar)) {
			return;
		}
		setCurrentTabItem(tabBarList.indexOf(tabBar));
	}

	/***
	 * 
	 * @param position
	 */
	public void setCurrentTabItem(int position) {
		mPager.setCurrentItem(position);
	}

	public Fragment getCurrentTabFragment() {
		if(mSectionsPagerAdapter.getCount()==0) return null;
		return mSectionsPagerAdapter.getItem(mPager.getCurrentItem());
	
	}
	
	/***
	 * 默认为true
	 * @return
	 */
	public boolean isAutoLoadTab() {
		return isAutoLoadTab;
	}

	public void setAutoLoadTab(boolean isAutoLoadTab) {
		this.isAutoLoadTab = isAutoLoadTab;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mobStatisticUtils.onStatisticResume();
		if(tabBarList.isEmpty()&&isAutoLoadTab()){
			new TabBarAsync().execute();
		}
		
	}
 
    @Override
    protected void onPause() {
    	// TODO Auto-generated method stub
    	super.onPause();
    	mobStatisticUtils.onStatisticPause();
    }
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
		List<Fragment> dataFragmentList = null;
		FragmentManager mFragmentManager;
		public SectionsPagerAdapter(FragmentManager fm, List<Fragment> dataFragmentList) {
			super(fm);
			this.mFragmentManager=fm;
			this.dataFragmentList = dataFragmentList;
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = dataFragmentList.get(position);
			return fragment;
		}
		@Override  
		public int getItemPosition(Object object) {  
		    return POSITION_NONE;  
		}  
		
		@Override
        public CharSequence getPageTitle(int position) {
            return tabBarList.get(position).getTabTitle();
        }
		@Override
		public int getCount() {

			return tabBarList.size();
		}
		public void setFragments( List<Fragment> fragmentList) {
			if(this.dataFragmentList != null){
			      FragmentTransaction ft = mFragmentManager.beginTransaction();
			      for(Fragment f:this.dataFragmentList){
			        ft.remove(f);
			      }
			      ft.commitAllowingStateLoss();
			      ft=null;
			      mFragmentManager.executePendingTransactions();
			   }
			  this.dataFragmentList = fragmentList;
			  notifyDataSetChanged();
		}

		@Override
		public int getIconResId(int index) {
			// TODO Auto-generated method stub
			return tabBarList.get(index).getImageResId();
		}
		@Override
		public boolean getReadPoint(int index) {
			// TODO Auto-generated method stub
			return tabBarList.get(index).isRead();
		}
	}

	public DataFragmentCallBack dataCallBack = new DataFragmentCallBack() {

		@Override
		public void fragmentCallBack(Fragment fragment, TabBarVO mTabBarVO) {
			// current fragment view init data
			if (mSectionsPagerAdapter.getItem(mPager.getCurrentItem()).equals(fragment)) {
				buildCurrentFragmentListData(fragment);
			}
		}

		@Override
		public void fragmentListOnItemClick(Fragment fragment, AdapterView<?> arg0, View arg01, int arg2, long arg3) {
			// TODO Auto-generated method stub
			currentFragmentListOnItemClick(fragment, arg0, arg01, arg2, arg3);
		}

		@Override
		public void fragmentListLoadMoreCallBack(Fragment fragment, TabBarVO mTabBarVO, int pageIndex) {
			// TODO Auto-generated method stub
			listLoadMoreListener(fragment, mTabBarVO, pageIndex);
		}

		@Override
		public void fragmentListReloadCallBack(Fragment fragment, TabBarVO mTabBarVO) {
			// TODO Auto-generated method stub
			fragmentListReloadCallBack(fragment, mTabBarVO);
		}
	};

	protected abstract void buildCurrentFragmentListData(Fragment currentFragment);

	protected void currentFragmentListOnItemClick(Fragment tabBarListFragment, AdapterView<?> arg0, View arg01, int arg2, long arg3){
		
	}

	protected void listLoadMoreListener(Fragment fragment, TabBarVO mTabBarVO, int pageIndex) {

	}

	protected void listReloadListener(Fragment fragment, TabBarVO mTabBarVO) {

	}

	/**
	 * 头标点击监听
	 */
	public class TabBarTitleViewOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			mPager.setCurrentItem((Integer) v.getTag());
		}
	};

	public ViewPager getmPager() {
		return mPager;
	}

	public void slideview(final View view, final float p1, final float p2) {
		TranslateAnimation animation = new TranslateAnimation(p1, p2, 0, 0);
		animation.setInterpolator(new OvershootInterpolator());
		animation.setDuration(300);
		// animation.setStartOffset(delayMillis);
		animation.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				int left = view.getLeft() + (int) (p2 - p1);
				int top = view.getTop();
				int width = view.getWidth();
				int height = view.getHeight();
				view.clearAnimation();
				view.layout(left, top, left + width, top + height);
			}
		});
		view.startAnimation(animation);
	}

	/**
	 * 页卡切换监听
	 */
	public class TabBarOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {

			Fragment fragment = (Fragment) (mSectionsPagerAdapter.getItem(position));
			buildCurrentFragmentListData(fragment);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
	private class TabBarAsync extends AsyncTask<String, Integer,List<TabBarVO>>{

		@Override
		protected List<TabBarVO> doInBackground(String... params) {
			// TODO Auto-generated method stub
			return makeTabBarList();
		}
		@Override
		protected void onPostExecute(List<TabBarVO> list) {
			// TODO Auto-generated method stub
			if(list!=null){
				tabBarList.clear();
				tabBarList.addAll(list);
				
				indicator.setVisibility(tabBarList.size()>1?View.VISIBLE:View.GONE);
				if(!tabBarList.isEmpty()){
					mSectionsPagerAdapter.setFragments(getFragmentList(tabBarList));
					mPager.setOffscreenPageLimit(tabBarList.size());// 设置缓存页面，当前页面的相邻N各页面都会被缓存
					setCurrentTabItem(0);
				}
				indicator.notifyDataSetChanged();
				afterFragmentFinished();
			}
		}
	}


}