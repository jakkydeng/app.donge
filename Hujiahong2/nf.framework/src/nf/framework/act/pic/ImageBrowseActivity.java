package nf.framework.act.pic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import nf.framework.R;
import nf.framework.act.AbsBaseActivity;
import nf.framework.act.NFIntentUtils;
import nf.framework.core.exception.LogUtil;
import nf.framework.core.util.android.CloseActivityClass;
import nf.framework.expand.widgets.HackyViewPager;
import nf.framework.expand.widgets.zoomPhotoView.ImageTagVO;
import nf.framework.expand.widgets.zoomPhotoView.PhotoView;
import nf.framework.expand.widgets.zoomPhotoView.PhotoView.OnImageTagItemClickListener;
import nf.framework.expand.widgets.zoomPhotoView.PhotoViewAttacher.OnViewTapListener;


public class ImageBrowseActivity extends AbsBaseActivity {

	private ViewPager mViewPager;

	public static final String WALLPAPERBROWSE_INTENT_LIST = "wallPaperList";

	public static final String WALLPAPERBROWSE_INTENT_PAGEINDEX = "pageIndex";

	private List<ImageBrowserVO> list = new ArrayList<ImageBrowserVO>();
	private WallPaperBrowseAdapter wallPaperAdapter = null;
	private Context mcontext;
	private int currentPosition=0;
	private View bottomLayout;
	private TextView desView;
	private boolean isHidden;
	private DisplayImageOptions options;
	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mcontext = this;
		CloseActivityClass.activityList.add(this);
		
		
		initView();
//		initImageLoader(this);
		Intent intent = getIntent();
		list = (List<ImageBrowserVO>) intent
				.getSerializableExtra(WALLPAPERBROWSE_INTENT_LIST);
		currentPosition = intent.getIntExtra(WALLPAPERBROWSE_INTENT_PAGEINDEX, 0);

		wallPaperAdapter = new WallPaperBrowseAdapter(this, list);
		mViewPager.setAdapter(wallPaperAdapter);
		mViewPager.setCurrentItem(currentPosition, true);
		top_textview.setText((currentPosition+1) + "/" + list.size());
		top_textview.setTextColor(getResources().getColor(R.color.cgreen));
		setBottomViewShow(list.get(currentPosition).getDescription());
		options = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(R.drawable.empty_photo)
		.showImageOnFail(R.drawable.empty_photo)
		.resetViewBeforeLoading(true)
		.cacheOnDisk(true)
		.cacheInMemory(true)
		.imageScaleType(ImageScaleType.EXACTLY)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(0))
		.build();
	}

	/***
	 * 初始化视图控件
	 */
	private void initView() {
		 // 设置布局
		setContentView(R.layout.imagebrowser_main);
		mViewPager = (HackyViewPager)this.findViewById(R.id.imagebrowser_main_viewpager);
 		bottomLayout=	this.findViewById(R.id.imagebrowser_main_des_layout);
		desView=(TextView)this.findViewById(R.id.imagebrowser_main_des_txt);
		top_textview = (TextView) this.findViewById(R.id.common_base_top_title_textview);
		leftButton = (ImageButton) findViewById(R.id.common_base_toptitle_left_img);
		rightButton = (ImageButton) findViewById(R.id.common_base_toptitle_right_img);
		navigationBarLayout = (ViewGroup) findViewById(R.id.common_basemain_navigationbar_layout);
		leftButton.setVisibility(View.VISIBLE);
		leftButton.setImageResource(R.drawable.common_navigate_back_btn);
		leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			}
		});
		super.rightButton.setVisibility(View.VISIBLE);
		super.rightButton.setImageResource(R.drawable.common_navigate_share_btn);
		super.rightButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				openOptionsMenu();
			}
		});
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				currentPosition=arg0;
				if(list!=null&&desView!=null){
					String description=list.get(arg0).getDescription();
					setBottomViewShow(description);
				}
					
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				String title=(arg0 + 1) + File.separator+ mViewPager.getAdapter().getCount();
				top_textview.setText(title);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
	
	private void setBottomViewShow(String description){
		
		if(!TextUtils.isEmpty(description)){
			desView.setText(description);
		}else{
			bottomLayout.setVisibility(View.GONE);
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	/**
	 * 按下返回按钮
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	/* (non-Javadoc)
	 * @see com.Apricotforest.MJAbsBaseActivity#onFinishResult()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		finish();
	}
	
	private OnViewTapListener onImageViewTapListener(){
		return new OnViewTapListener() {
			
			@Override
			public void onViewTap(View view, float x, float y) {
				// TODO Auto-generated method stub
				isHidden =!isHidden;
				
				setTopViewAnimation(navigationBarLayout,isHidden);
				navigationBarLayout.setVisibility(isHidden?View.INVISIBLE:View.VISIBLE);
				if(!TextUtils.isEmpty(desView.getText())){
					setBottomViewAnimation(bottomLayout,isHidden);
					bottomLayout.setVisibility(isHidden?View.INVISIBLE:View.VISIBLE);
				}
			}
		};
		
	}
	private OnImageTagItemClickListener onImageTagItemClickListener(){
		
		return new OnImageTagItemClickListener() {
			
			@Override
			public void onImageTagItemClicked(ImageTagVO imageTag) {
				// TODO Auto-generated method stub
				if(imageTag!=null){
					NFIntentUtils.intentToInnerBrowserAct(
						mcontext, "image",imageTag.getName(), imageTag.getLink());
				}
			}
		};
	}
	
	private void setBottomViewAnimation(View view ,boolean isHidden){
		
		Animation animation = AnimationUtils.loadAnimation(this,isHidden? R.anim.imagebrowser_bottomview_exit:R.anim.imagebrowser_bottomview_enter);   
		view.startAnimation(animation); 
	}
	private void setTopViewAnimation(View view,boolean isHidden){
		
		Animation animation = AnimationUtils.loadAnimation(this,isHidden? R.anim.imagebrowser_topview_exit:R.anim.imagebrowser_topview_enter);   
		view.startAnimation(animation); 
	}
	 class WallPaperBrowseAdapter extends PagerAdapter {

		List<ImageBrowserVO> mlist = new ArrayList<ImageBrowserVO>();
		private ViewGroup mcontainer;
		private LayoutInflater inflater;
		public WallPaperBrowseAdapter(Context context, List<ImageBrowserVO> list) {
			this.mlist = list;
			inflater =((Activity) context).getLayoutInflater();
		}

		@Override
		public int getCount() {
			return mlist.size();
		}
		@Override
		public View instantiateItem(ViewGroup container, int position) {
			// Now just add PhotoView to ViewPager and return it
			View imageLayout = inflater.inflate(R.layout.wallpaper_browse_layout, container, false);
			PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.wallpaper_image_layout_image);
			final ProgressBar progressBar = (ProgressBar) imageLayout.findViewById(R.id.wallpaper_image_layout_loading);
			ImageBrowserVO wallPaper = mlist.get(position);
			String picUrl=null;
			if(!TextUtils.isEmpty(wallPaper.getPicUrl())){
				if(URLUtil.isFileUrl(wallPaper.getPicUrl())||wallPaper.getPicUrl().startsWith(File.separator)){
					picUrl=Scheme.FILE.wrap(wallPaper.getPicUrl());
				}else{
					picUrl =wallPaper.getPicUrl();
				}
			}
			ImageLoader.getInstance().displayImage(picUrl, imageView, options,new SimpleImageLoadingListener(){
				@Override
				public void onLoadingStarted(String imageUri, View view) {
					progressBar.setVisibility(View.VISIBLE);
				}
				@Override
				public void onLoadingComplete(String imageUri, View view,
						Bitmap loadedImage) {
					progressBar.setVisibility(View.GONE);
				}
				@Override
				public void onLoadingCancelled(String imageUri, View view) {
					progressBar.setVisibility(View.GONE);
				}
				@Override
				public void onLoadingFailed(String imageUri, View view,
						FailReason failReason) {
					progressBar.setVisibility(View.GONE);
				}
			});
//			new DownloadImgTask(mcontext,loadImage,imageView, progressBar,R.drawable.package_loading,wallPaper).execute();
			container.addView(imageLayout, LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT);
			mcontainer = container;
			imageView.setOnViewTapListener(onImageViewTapListener());
			imageView.setImageTagList(wallPaper.getTagList());
			imageView.setOnImageTagItemClickListener(onImageTagItemClickListener());
			return imageLayout;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
			
			mcontainer = container;
		}

		@Override
		public void startUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			super.startUpdate(container);
		}

		@Override
		public void finishUpdate(ViewGroup container) {
			// TODO Auto-generated method stub
			super.finishUpdate(container);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		public ViewGroup getMcontainer() {
			return mcontainer;
		}

		public void setMcontainer(ViewGroup mcontainer) {
			this.mcontainer = mcontainer;
		}

	}
	 
	 public ImageBrowserVO getCurrentShowImageVO(){
		 
		 ImageBrowserVO imageBrowser= list.get(currentPosition);
		 imageBrowser.setPicPath(getCurrentPosPicUrl());
		 return imageBrowser;
	 }
	/***
	 * return current pic name
	 * @return
	 */
	public String getCurrentPosPicName(){
		ImageBrowserVO imageBrowser=list.get(currentPosition);
		String bitmapFileName=ImageBrowseUtil.getTempBitmapFileName(mcontext,imageBrowser.getPicUrl());
		LogUtil.e(bitmapFileName,bitmapFileName);
		return bitmapFileName;
	}
	/**
	 * return current pic url
	 * @return
	 */
	public String getCurrentPosPicUrl(){
		
		String bitmapName=getCurrentPosPicName();
		String localImageUrl=ImageBrowseUtil.getTempBitmapFilePath(mcontext, bitmapName);
		LogUtil.e(localImageUrl,localImageUrl);
		return localImageUrl;
	}
}