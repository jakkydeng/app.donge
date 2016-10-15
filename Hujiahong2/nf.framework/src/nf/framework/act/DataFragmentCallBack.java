package nf.framework.act;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;

public interface DataFragmentCallBack {

	public void fragmentCallBack(Fragment fragment, TabBarVO mTabBarVO);

	public void fragmentListOnItemClick(Fragment fragment, AdapterView<?> arg0, View arg01, int arg2, long arg3);

	public void fragmentListReloadCallBack(Fragment fragment, TabBarVO mTabBarVO);

	public void fragmentListLoadMoreCallBack(Fragment fragment, TabBarVO mTabBarVO, int pageIndex);
}
