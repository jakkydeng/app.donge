package nf.framework.fragment;

import nf.framework.R;
import nf.framework.expand.widgets.pulltorefresh.PullToRefreshBase;
import nf.framework.expand.widgets.pulltorefresh.PullToRefreshListView;
import nf.framework.statistic.MobStatisticUtils;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public abstract class AbsListBaseFragment<T> extends AbsBaseFragment implements
		OnItemClickListener, PullToRefreshBase.OnRefreshListener<ListView>, PullToRefreshBase.OnLastItemVisibleListener {
	protected PullToRefreshListView mlistview;
	private AbsListAdapter<?, ?> listItemAdapter;
	private View viewLayout = null;
	private MobStatisticUtils mobStatisticUtils;
	protected LinearLayout  emptyLayout;
	protected ImageView emptyImgView;
	protected TextView referView1;
	protected TextView referView2;
	protected Button emptyBtn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		mobStatisticUtils=new MobStatisticUtils(getActivity());
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		viewLayout = inflater.inflate(R.layout.listview_layout2, container,false);
		initView(viewLayout);
		return viewLayout;
	}

	@Override
	public void onResume() {
		super.onResume();
		mobStatisticUtils.onStaFragmentResume(
				getPageName()!=null?getPageName():getClass().getSimpleName());
	}

	@Override
	public void onPause() {
		super.onPause();
		mobStatisticUtils.onStaFragmentPause(
				getPageName()!=null?getPageName():getClass().getSimpleName());
	}
	protected void initView(View v) {

		mlistview = (PullToRefreshListView) v.findViewById(R.id.common_listview);
		mlistview.setShowIndicator(false);
		mlistview.addFooterView();
		View headerView=getListHeaderView();
		if(headerView!=null){
			mlistview.addHeaderView(headerView,null, false);
		}
		listItemAdapter=createAbsListAdapter();
		if(listItemAdapter!=null){
			mlistview.setAdapter(listItemAdapter);
			listItemAdapter.notifyDataSetChanged();
		}
		mlistview.setOnRefreshListener(this);
		mlistview.setOnLastItemVisibleListener(this);
		mlistview.setOnItemClickListener(this);
		emptyLayout =(LinearLayout)v.findViewById(R.id.common_listview_empty_layout);
		View emptyView=LayoutInflater.from(getActivity()).inflate(R.layout.list_empty_view,emptyLayout, false);
		emptyImgView=(ImageView)emptyView.findViewById(R.id.list_empty_view_iv);
		referView1=(TextView)emptyView.findViewById(R.id.list_empty_view_tv_refer);
		referView2 =(TextView)emptyView.findViewById(R.id.list_empty_view_tv_refer2);
		emptyBtn=(Button)emptyView.findViewById(R.id.list_empty_view_btn);
		emptyLayout.addView(emptyView);
		emptyLayout.setVisibility(View.GONE);
	}
	public void setEmptyViewShow(boolean isShow){
		
		if(emptyLayout!=null){
			emptyLayout.setVisibility(isShow?View.VISIBLE:View.GONE);
		}
	}
	
	public void setEmptyViewText(int resId,String refer1,String refer2){
		emptyImgView.setVisibility(resId==0?View.GONE:View.VISIBLE);
		emptyImgView.setImageResource(resId);
		referView1.setVisibility(TextUtils.isEmpty(refer1)?View.GONE:View.VISIBLE);
		referView1.setText(refer1);
		referView2.setVisibility(TextUtils.isEmpty(refer2)?View.GONE:View.VISIBLE);
		referView2.setText(refer2);
	}
	protected abstract AbsListAdapter<?, ?> createAbsListAdapter();
	
	
	protected View getListHeaderView(){
		return null;
		
	}
	
	@Override  
    public void setUserVisibleHint(boolean isVisibleToUser) {  
       
       if (isVisibleToUser &&this.isResumed()){
           //相当于Fragment的onResume  
    	   onlazyLoad();
       } else {  
           //相当于Fragment的onPause  
       } 
       super.setUserVisibleHint(isVisibleToUser);
    } 
	
	public void onlazyLoad() {
		
	}
	
	protected AbsListAdapter<?, ?> getCurrentListAdapter(){
		
		return listItemAdapter;
	}
	
	protected int getListItemCount(){
		
		return listItemAdapter!=null?listItemAdapter.getCount():0;
	}
	
	protected abstract String getPageName();
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	
		if(listItemAdapter!=null){
			listItemAdapter.clearDisplayedImages();
		}
		
	}
}
