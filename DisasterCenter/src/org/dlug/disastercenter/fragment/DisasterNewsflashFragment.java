package org.dlug.disastercenter.fragment;

import org.dlug.disastercenter.http.api.BaseApi.ApiDelegate;
import org.dlug.disastercenter.http.api.DisasterApi;
import org.dlug.disastercenter.http.response.Response;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class DisasterNewsflashFragment extends BaseFragment {
	private DisasterApi mDisasterApi;
	private TextView mTextView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Context context = getActivity();
		
		TextView textView = new TextView(context);
		textView.setText(DisasterNewsflashFragment.class.getName());
		textView.setGravity(Gravity.CENTER);
		mTextView = textView;
		
		// 
		mDisasterApi = new DisasterApi();
		mDisasterApi.disasterNewsAsync(-1, 0, mDisasterNewsDelegate);
		
		return textView;
	}
	
	private ApiDelegate<Response> mDisasterNewsDelegate = new ApiDelegate<Response>() {
		
		@Override
		public void onResponse(int requestTag, final Response response) {
			final Activity activity = getActivity();
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mTextView.setText(response.getMessage());
					Toast.makeText(activity , response.getMessage(), Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		@Override
		public void onError(int requestTag, final Exception e) {
			final Activity activity = getActivity();
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mTextView.setText(e.toString());
					Toast.makeText(activity , e.toString(), Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		@Override
		public void onCancel(int requestTag) {
			final Activity activity = getActivity();
			activity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(activity , "Cancel", Toast.LENGTH_SHORT).show();
				}
			});
		}
	};
}