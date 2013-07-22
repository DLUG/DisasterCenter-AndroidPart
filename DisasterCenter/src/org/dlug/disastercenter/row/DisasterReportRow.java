package org.dlug.disastercenter.row;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.data.DisasterReportListData;
import org.dlug.disastercenter.utils.DisasterDisplayUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DisasterReportRow extends FrameLayout {
	private ImageView mIconImageView;
	private TextView mContentTextView, mDateTextView, mReportTypeTextView;
	
	private DisasterReportListData mData;

	public DisasterReportRow(Context context) {
		super(context);
		init(context);
	}
	
	
	
	public DisasterReportRow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}



	public DisasterReportRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		
		View.inflate(context, R.layout.row_disaster_report, this);

		mIconImageView = (ImageView)findViewById(R.id.row_disaster_report_ImageView_icon);
		mReportTypeTextView = (TextView)findViewById(R.id.row_disaster_report_TextView_reportType);
		mContentTextView = (TextView)findViewById(R.id.row_disaster_report_TextView_content);
		mDateTextView = (TextView)findViewById(R.id.row_disaster_report_TextView_date);	
	}

	public void setData(DisasterReportListData data) {
		mData = data;
		
		
		mIconImageView.setImageResource(DisasterDisplayUtils.getDisasterIconResource(data.getDisasterType()));
		mContentTextView.setText(data.getContent());
		mDateTextView.setText(DisasterDisplayUtils.getDisplayTimestamp(data.getTimestamp()));
		mReportTypeTextView.setText(DisasterDisplayUtils.getDisplayReportType(data.getReportType()));
	}
	
}
