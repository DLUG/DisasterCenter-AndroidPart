package org.dlug.disastercenter.fragment;

import com.actionbarsherlock.app.SherlockFragment;

public abstract class BaseFragment extends SherlockFragment {
	public BaseFragment() {
		super();
		setRetainInstance(true);
	}
	
	public boolean onBackPressed() {
		return true;
	}
}