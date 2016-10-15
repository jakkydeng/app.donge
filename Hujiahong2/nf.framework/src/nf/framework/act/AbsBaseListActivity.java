package nf.framework.act;

import nf.framework.R;
import nf.framework.expand.widgets.OnHeaderRefreshListener;
import nf.framework.expand.widgets.OnScrollLoadMoreListener;
import nf.framework.expand.widgets.UpFreshListView;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class AbsBaseListActivity extends AbsBaseActivity implements OnItemClickListener,OnHeaderRefreshListener,OnScrollLoadMoreListener{

	protected UpFreshListView mListView;
	protected LinearLayout  emptyLayout;
	protected ImageView emptyImgView;
	protected TextView referView1;
	protected TextView referView2;
	protected Button emptyBtn;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		View mainView =LayoutInflater.from(this).inflate(R.layout.listview_layout,super.mainlayout,false);
		super.mainlayout.addView(mainView);
		
		mListView=(UpFreshListView)mainView.findViewById(R.id.common_listview);
	
		mListView.setOnItemClickListener(this);
		mListView.setOnScrollLoadMoreListener(this);
		mListView.setOnHeaderRefreshListener(this);
		
		emptyLayout =(LinearLayout)findViewById(R.id.common_listview_empty_layout);
		View emptyView=LayoutInflater.from(this).inflate(R.layout.list_empty_view,emptyLayout, false);
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
}
