package org.dlug.disastercenter.fragment;

import org.apache.commons.lang3.StringEscapeUtils;
import org.dlug.disastercenter.R;
import org.dlug.disastercenter.data.DisasterInfoData;
import org.dlug.disastercenter.utils.DisasterInfoDisplayUtils;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class DisasterInfoViewFragment extends BaseFragment {
	private DisasterInfoData mData;


	public DisasterInfoViewFragment(DisasterInfoData data) {
		mData = data;
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_disaster_info_view, null);
		
		ImageView iconImageView = (ImageView)view.findViewById(R.id.fragment_disaster_info_view_ImageView_icon);
		TextView disasterTextView = (TextView)view.findViewById(R.id.fragment_disaster_info_view_TextView_disasterType);
		TextView titleTextView = (TextView)view.findViewById(R.id.fragment_disaster_info_view_TextView_title);
		TextView dateTextView = (TextView)view.findViewById(R.id.fragment_disaster_info_view_TextView_date);
		WebView contentWebView = (WebView)view.findViewById(R.id.fragment_disaster_info_view_WebView_content);
		
		iconImageView.setImageResource(DisasterInfoDisplayUtils.getDisasterIconResource(mData));
		disasterTextView.setText(DisasterInfoDisplayUtils.getDisplayDisasterType(mData));
		dateTextView.setText(DisasterInfoDisplayUtils.getDisplayTimestamp(mData));
		titleTextView.setText(mData.getTitle());
		
		String content = StringEscapeUtils.unescapeHtml4(mData.getContent());
		
		String html = "<html>" + content + "</html>";
		contentWebView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
		
		return view;
	}
	
	
}
