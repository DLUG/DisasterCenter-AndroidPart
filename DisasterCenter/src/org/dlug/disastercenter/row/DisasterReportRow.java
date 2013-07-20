package org.dlug.disastercenter.row;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.data.DisasterReportListData;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DisasterReportRow extends FrameLayout {
	private ImageView mIconImageView, mReportIconImageView;
	private TextView mStateTextView, mContentTextView, mDateTextView;
	
	private DisasterReportListData mData;

	public DisasterReportRow(Context context) {
		super(context);
		
		View.inflate(context, R.layout.row_disaster_info, this);

		mIconImageView = (ImageView)findViewById(R.id.row_disaster_report_ImageView_icon);
		mReportIconImageView = (ImageView)findViewById(R.id.row_disaster_report_ImageView_reportType);
		mStateTextView = (TextView)findViewById(R.id.row_disaster_report_TextView_state);
		mContentTextView = (TextView)findViewById(R.id.row_disaster_report_TextView_content);
		mDateTextView = (TextView)findViewById(R.id.row_disaster_report_TextView_date);

		
	}
	
	public void setData(DisasterReportListData data) {
		mData = data;
		
		// TODO icon setting
		switch ( data.getDisasterType() ) {
			
		}
		mIconImageView.setImageResource(R.drawable.info_img_document);
		mContentTextView.setText(data.getContent());
	}
	
}
