package org.dlug.disastercenter.activity;

import java.util.ArrayList;
import java.util.HashMap;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.fragment.BaseFragment;
import org.dlug.disastercenter.fragment.DisasterBoardFragment;
import org.dlug.disastercenter.fragment.DisasterInfoListFragment;
import org.dlug.disastercenter.fragment.DisasterNewsflashFragment;
import org.dlug.disastercenter.fragment.SettingFragment;

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
import android.widget.Button;

public class MainActivity extends BaseActivity {

	private MenuAdapter mMenuAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		ViewGroup menuLayout = (ViewGroup)findViewById(R.id.activity_main_LinearLayout_menu);
		SlidingPaneLayout layout = (SlidingPaneLayout)findViewById(R.id.activity_main_SlidingPaneLayout);
		layout.setSliderFadeColor(Color.TRANSPARENT);
		layout.setCoveredFadeColor(Color.BLACK);
		layout.setParallaxDistance(1);
		layout.closePane();
		
		
		mMenuAdapter = new MenuAdapter(this, menuLayout, layout, R.id.activity_main_FrameLayout_content);
	
		mMenuAdapter.addMenu("재난 정보", DisasterInfoListFragment.class, null);
		mMenuAdapter.addMenu("재난 속보", DisasterNewsflashFragment.class, null);
		mMenuAdapter.addMenu("게시판", DisasterBoardFragment.class, null);
		mMenuAdapter.addMenu("설정", SettingFragment.class, null);
		
		
		mMenuAdapter.openMenu("Folder");
	}


	@Override
	protected void onStart() {
		super.onStart();
		
		
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		
	}
	
	@Override
	public void onBackPressed() {
		if ( mMenuAdapter.onBackPressed() ) {
			moveTaskToBack(true);
		}
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
		
		public void addMenu(String _tag, Class<? extends BaseFragment> _class, Bundle _args) {
			MenuInfo menuInfo = new MenuInfo(_tag, _class, _args);
			
			menuInfo.fragment = (BaseFragment)mActivity.getSupportFragmentManager().findFragmentByTag(_tag);
			
			if (menuInfo.fragment != null && !menuInfo.fragment.isDetached()) {	
				FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
				ft.detach(menuInfo.fragment);
				ft.commit();
			}
			
			mMenuMap.put(_tag, menuInfo);
			
			
			// CustomView
			Button menu = new Button(mActivity);
			menu.setText(_tag);
			menu.setTag(_tag);
			menu.setOnClickListener(mMenuClickListener);
			
			mMenuList.add(menu);
			mMenuLayout.addView(menu);
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
				ft.commit();
				fm.executePendingTransactions();
				
				
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