package org.dlug.disastercenter.fragment;

import org.dlug.disastercenter.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public abstract class BaseFragment extends Fragment {
	public BaseFragment() {
		super();
		setRetainInstance(false);
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup baseView = (ViewGroup)inflater.inflate(R.layout.fragment_base, null);
		
		View childView = onCreateViewEx(inflater, container, savedInstanceState);
		baseView.addView(childView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		
		TextView titleView = (TextView)baseView.findViewById(R.id.fragment_base_TextView_title);
		titleView.setText(getTitle());
		
		return baseView;
	}

	
	public boolean onBackPressed() {
		return true;
	}
	

	protected abstract String getTitle();
	protected abstract View onCreateViewEx(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}