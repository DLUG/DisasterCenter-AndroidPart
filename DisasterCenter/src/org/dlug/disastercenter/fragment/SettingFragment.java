package org.dlug.disastercenter.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingFragment extends BaseFragment {


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Context context = getActivity();
		
		TextView textView = new TextView(context);
		textView.setText(SettingFragment.class.getName());
		textView.setGravity(Gravity.CENTER);
		
		
		return textView;
	}
}