/**   
 * @Title: ListAdapter.java 
 * @Package com.example.apricotforest_commontest 
 * @Description: TODO(��һ�仰�������ļ���ʲô) 
 * @author niufei
 * @date 2014-5-8 ����12:08:56 
 * @version V1.0   
*/
package nf.framework.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import nf.framework.expand.widgets.UpFreshListView;
import nf.framework.util.CircleBitmapDisplayer;


public abstract class AbsListAdapter<T,ViewHolder> extends BaseAdapter {
	
	protected List<T> mList;
	protected LayoutInflater mLayoutInflater;
	protected static DisplayImageOptions options= new DisplayImageOptions.Builder()
	.cacheInMemory(true)
	.cacheOnDisk(true)
	.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(0))
	.build();
	/***
	 * circle DisplayImageOptions
	 */
	public static DisplayImageOptions circleOptions = new DisplayImageOptions.Builder()

	.cacheInMemory(true)
	.cacheOnDisk(true)
	.considerExifParams(true)
	.bitmapConfig(Bitmap.Config.RGB_565)
	.displayer(new CircleBitmapDisplayer())
			.displayer(new FadeInBitmapDisplayer(0))
	.build();

	protected ImageLoader imageLoader =ImageLoader.getInstance();
	protected int currentPos=0;
	protected AnimateFirstDisplayListener animateFirstListener = new AnimateFirstDisplayListener();
	/**
	 * @param mcontext
	 * @param list
	 */
	public AbsListAdapter(Context mcontext, List<T> list) {
		// TODO Auto-generated constructor stub
		this(mcontext,null,list);
		
	}
	public AbsListAdapter(Context mcontext, ListView listView,List<T> list) {
		// TODO Auto-generated constructor stub
		this(mcontext,listView,list,options);
	}
	
	public AbsListAdapter(Context mcontext,ListView listView,List<T> list,DisplayImageOptions options) {
		
		mList=list;
		mLayoutInflater=LayoutInflater.from(mcontext);
		this.options=options;
		if(listView!=null){
			OnScrollListener onScrollListener=null;
			if(listView instanceof UpFreshListView ){
				onScrollListener=	(OnScrollListener)listView;
			}
			listView.setOnScrollListener(new PauseOnScrollListener(imageLoader, true, true,onScrollListener));
		}
		
	}
	
	protected  void setImageLoader(final ImageView imageView,final String url){
		
		setImageLoader(imageView, url,options,new SimpleImageLoadingListener(){
			@Override
			public void onLoadingComplete(String imageUri, View view,
					Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);
				if(imageUri.equals(url)){
					imageView.setImageBitmap(loadedImage);
				} 
			}
		});
	}
	/**
	 * 圆形图片
	 * @param imageView
	 * @param url
	 */
	protected  void setCircleImageLoader(ImageView imageView,String url){
		
		setImageLoader(imageView, url,circleOptions);
	}
	
	protected  void setImageLoader(ImageView imageView,String url,DisplayImageOptions options){
		
		setImageLoader(imageView, url,options, animateFirstListener);
	}
	
	protected  void setImageLoader(ImageView imageView,String url,DisplayImageOptions options,SimpleImageLoadingListener imageLoadingListener){
		
		setImageLoader(imageView, url,options,imageLoadingListener,null);
	}
	
	protected  void setImageLoader(ImageView imageView,String url,DisplayImageOptions options,SimpleImageLoadingListener simpleImageLoadingListener,ImageLoadingProgressListener progressListener){
		if(imageView==null||TextUtils.isEmpty(url)){
			return;
		}
		ImageLoader.getInstance().displayImage(url,imageView, options, simpleImageLoadingListener,progressListener);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList!=null?mList.size():0;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public T getItem(int position) {
		// TODO Auto-generated method stub
		return mList!=null?mList.get(position):null;
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public int getCurrentPosition(){
		return currentPos;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if (convertView != null) {
			holder = (ViewHolder) convertView.getTag();

		} else{ 
			int layoutId=getItemViewLayout();
			if(layoutId==0){
				throw new RuntimeException("itemLayoutid == 0");
			}
			convertView =mLayoutInflater.inflate(layoutId,null);
			if(convertView==null){
				throw new RuntimeException("convertView is empty");
			}
			holder=	buildItemViewHolder(convertView);
			if(holder==null){
				throw new RuntimeException("holder is empty");
			}
			convertView.setTag(holder);
		}	
		
		T object=getItem(position);
		currentPos=position;
		if(object!=null){	
			bindDataToView(object,holder);
		}
		return convertView;
	}
	
	protected  void setImageLoader(ImageView imageView,String url,SimpleImageLoadingListener simpleImageLoadingListener){
		
		setImageLoader(imageView, url, simpleImageLoadingListener==null?animateFirstListener:simpleImageLoadingListener,null);
	}
	
	protected  void setImageLoader(ImageView imageView,String url,SimpleImageLoadingListener simpleImageLoadingListener,ImageLoadingProgressListener progressListener){
		if(imageView==null||TextUtils.isEmpty(url)){
			return;
		}
		ImageLoader.getInstance().displayImage(url,imageView, options, simpleImageLoadingListener,progressListener);
	}
	
	protected abstract int getItemViewLayout();
	
	
	protected abstract ViewHolder buildItemViewHolder(View convertView);
	
	
	protected abstract void bindDataToView(T object, ViewHolder holder);
	
	/**
	 * 清空显示图片
	 */
	public void clearDisplayedImages(){
		
		if(animateFirstListener!=null){
			((AnimateFirstDisplayListener) animateFirstListener).getDisplayedImages().clear();
		}
	}
	
	private class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());

		public List<String> getDisplayedImages(){
			return displayedImages;
		}
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 0);
					displayedImages.add(imageUri);
				}
			}
		}
		
	}
}
