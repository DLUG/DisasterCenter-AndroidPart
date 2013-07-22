package org.dlug.disastercenter.fragment;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dlug.disastercenter.R;
import org.dlug.disastercenter.data.DisasterInfoData;
import org.dlug.disastercenter.utils.TextViewUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DisasterInfoViewFragment extends BaseFragment {
	private DisasterInfoData mData;


	public DisasterInfoViewFragment(DisasterInfoData data) {
		mData = data;
		
	}
	
	@Override
	public View onCreateViewEx(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_disaster_info_view, null);
		

		WebView contentWebView = (WebView)view.findViewById(R.id.fragment_disaster_info_view_WebView_content);
		TextView titleTextView = (TextView)view.findViewById(R.id.fragment_disaster_info_view_TextView_title);
		
		titleTextView.setText(getTitle());
		TextViewUtils.setBold(titleTextView, true);
	
		String content = mData.getContent();
		int width = getActivity().getResources().getDisplayMetrics().widthPixels;
		
		String html = StringEscapeUtils.unescapeHtml4(content);
		html = html + String.format("<meta name=\"viewport\" content=\"width=%d, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\" />", width);
		
		contentWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
		
		return view;
	}
	
	@Override
	protected String getTitle() {
		return mData.getTitle();
	}

}
