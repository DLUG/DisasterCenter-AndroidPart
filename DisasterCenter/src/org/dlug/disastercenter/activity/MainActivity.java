package org.dlug.disastercenter.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.fragment.BaseFragment;
import org.dlug.disastercenter.fragment.DisasterInfoListFragment;
import org.dlug.disastercenter.fragment.DisasterNewsListFragment;
import org.dlug.disastercenter.fragment.DisasterReportFragment;
import org.dlug.disastercenter.fragment.DisasterReportListFragment;
import org.dlug.disastercenter.fragment.MessageBoxFragment;
import org.dlug.disastercenter.fragment.SettingFragment;
import org.dlug.disastercenter.view.MenuView;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SlidingPaneLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class MainActivity extends BaseActivity {

	private MenuAdapter mMenuAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		ViewGroup menuLayout = (ViewGroup)findViewById(R.id.activity_main_LinearLayout_menu);
		SlidingPaneLayout layout = (SlidingPaneLayout)findViewById(R.id.activity_main_SlidingPaneLayout);
		layout.setParallaxDistance(1);
		layout.setCoveredFadeColor(Color.BLACK);
		
		mMenuAdapter = new MenuAdapter(this, menuLayout, layout, R.id.activity_main_FrameLayout_content);
	
		mMenuAdapter.addMenu("재난신고 하기", DisasterReportFragment.class, null, R.drawable.ic_report);
		mMenuAdapter.addMenu("재난신고 현황", DisasterReportListFragment.class, null, R.drawable.ic_report_state);
		mMenuAdapter.addMenu("재난뉴스", DisasterNewsListFragment.class, null, R.drawable.ic_news);
		mMenuAdapter.addMenu("재난정보", DisasterInfoListFragment.class, null, R.drawable.ic_info);
		mMenuAdapter.addMenu("수신메세지", MessageBoxFragment.class, null, R.drawable.ic_message);
		mMenuAdapter.addMenu("설정", SettingFragment.class, null, R.drawable.ic_setting);
		
		mMenuAdapter.openMenu("재난뉴스");
		

	

	}


	@Override
	protected void onStart() {
		super.onStart();
		
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
	}
	
	
	private static class MenuAdapter {
		private final HashMap<String, MenuInfo> mMenuMap;
		
		private final FragmentActivity mActivity;
		private final int mContentLayoutId;

		private MenuInfo mLastMenu;
		
		private ArrayList<View> mMenuList;
		private ViewGroup mMenuLayout;
		private SlidingPaneLayout mLayout;
		
		private static final class MenuInfo {
            private final String tag;
            private final Class<? extends BaseFragment> clss;
            private final Bundle args;
            private BaseFragment fragment;
 
            MenuInfo(String _tag, Class<? extends BaseFragment> _class, Bundle _args) {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }
		
		public MenuAdapter(FragmentActivity activity, ViewGroup menuLayout, SlidingPaneLayout layout, int contentLayoutId) {
			mMenuMap = new HashMap<String, MenuInfo>();
			mMenuList = new ArrayList<View>();
			
			mActivity = activity;
			mMenuLayout = menuLayout;
			mLayout = layout;
			mContentLayoutId = contentLayoutId;
		}
		
		public boolean onBackPressed() {
			if ( mLastMenu != null && mLastMenu.fragment != null ) {
				return mLastMenu.fragment.onBackPressed();
			}
			
			return false;
		}
		
		public void addMenu(String _tag, Class<? extends BaseFragment> _class, Bundle _args, int iconResid) {
			MenuInfo menuInfo = new MenuInfo(_tag, _class, _args);
			
			menuInfo.fragment = (BaseFragment)mActivity.getSupportFragmentManager().findFragmentByTag(_tag);
			
			if (menuInfo.fragment != null && !menuInfo.fragment.isDetached()) {	
				FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
				ft.detach(menuInfo.fragment);
				ft.commit();
			}
			
			mMenuMap.put(_tag, menuInfo);
		
			if ( mMenuList.size() != 0 ) {
				ImageView lineView = new ImageView(mActivity);
				lineView.setScaleType(ScaleType.FIT_XY);
				lineView.setImageResource(R.drawable.line_main_menu);
				
				mMenuLayout.addView(lineView);
			}
			
			// CustomView
			MenuView menuView = new MenuView(mActivity);

			menuView.setMenuData(iconResid, _tag);
			menuView.setTag(_tag);
			menuView.setOnClickListener(mMenuClickListener);
			
			mMenuList.add(menuView);
			mMenuLayout.addView(menuView);
		}
		
		public void openMenu(String tag) {
			FragmentManager fm = mActivity.getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			MenuInfo newMenu = mMenuMap.get(tag);
			MenuInfo lastMenu = mLastMenu;
			
			
			if ( newMenu != lastMenu ) {
				if ( lastMenu != null && lastMenu.fragment != null ) {
					ft.detach(lastMenu.fragment);
				}
				
				if (newMenu != null) {
					if ( newMenu.fragment == null ) {
						newMenu.fragment = (BaseFragment)Fragment.instantiate(mActivity, newMenu.clss.getName(), newMenu.args);
						ft.add(mContentLayoutId, newMenu.fragment, newMenu.tag);
					} 
					else {
						ft.attach(newMenu.fragment);
					}
				}
				
				mLastMenu = newMenu;
				ft.commitAllowingStateLoss();
				
				
				for ( View view : mMenuList ) {
					view.setSelected(tag.equalsIgnoreCase(view.getTag().toString()));
				}
			}
			mLayout.closePane();
		}
		
		private OnClickListener mMenuClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {
				String tag = v.getTag().toString();
				openMenu(tag);
			}
		};	
		
		
	}
}