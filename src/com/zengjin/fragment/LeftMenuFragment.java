package com.zengjin.fragment;

import java.util.List;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zengjin.base.BaseFragment;
import com.zengjin.base.impl.NewsCenterPager;
import com.zengjin.domain.NewsCenterBean;
import com.zengjin.domain.NewsCenterBean.NewsCenterData;
import com.zengjin.hmygz.MainUI;
import com.zengjin.hmygz.R;

import android.R.integer;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author Administrator 左侧菜单fragment
 */
public class LeftMenuFragment extends BaseFragment implements
		OnItemClickListener {
	private List<NewsCenterData> mMenuListData;
	private ListView mListView;
	private int selectPosition;// 默认选中的菜单选项
	private MenuAdapter menuAdapter;

	@Override
	public View initView(LayoutInflater inflater) {
		// inflater可用可不用，只要返回一个view就ok
		// TextView tx=new TextView(mActivity);
		// tx.setText("左侧菜单");
		// tx.setTextSize(20);

		mListView = new ListView(mActivity);

		mListView.setCacheColorHint(Color.TRANSPARENT);
		mListView.setDividerHeight(0);
		mListView.setBackgroundColor(Color.BLACK);
		mListView.setPadding(0, 30, 0, 0);
		mListView.setOnItemClickListener(this);
		// 设置它的默认选择器为透明
		mListView.setSelector(android.R.color.transparent);
		return mListView;
	}

	public void setMenuListData(List<NewsCenterData> menuListData) {
		this.mMenuListData = menuListData;

		selectPosition = 0;
		menuAdapter = new MenuAdapter();
		mListView.setAdapter(menuAdapter);
		switchNewsCenterContentPager();
	}

	/**
	 * 根据selectposition来切换当前新闻中心的内容页面
	 */
	private void switchNewsCenterContentPager() {
		// 默认选中第0个菜单，菜单对应页面需要切换为第0个页面
		MainUI mainUI = (MainUI) mActivity;
		MainContentFragment fragment = mainUI.getMainContentFragment();
		NewsCenterPager newscCenterPager = fragment.getNewscCenterPager();
		newscCenterPager.switchCurrentPager(selectPosition);
	}

	class MenuAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mMenuListData.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = null;
			if (convertView == null) {
				view = (TextView) view.inflate(mActivity, R.layout.menu_item,null);
			} else {
				view = (TextView) convertView;
			}
			view.setText(mMenuListData.get(position).title);

			// 设置当前item是否选中
			view.setEnabled(selectPosition == position);
			return view;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		selectPosition = position;
		menuAdapter.notifyDataSetChanged();

		// 点完之后把菜单收回去
		SlidingMenu slidingMenu = ((MainUI) mActivity).getSlidingMenu();
		slidingMenu.toggle();
		// 主界面中间部分要显示对应position的页面
switchNewsCenterContentPager();
	}
}
