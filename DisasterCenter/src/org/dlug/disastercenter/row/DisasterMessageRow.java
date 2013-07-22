package org.dlug.disastercenter.row;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.data.DisasterMessageData;
import org.dlug.disastercenter.utils.DisasterDisplayUtils;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DisasterMessageRow extends FrameLayout {
	private ImageView mIconImageView;
	private TextView mContentTextView, mDateTextView;
	
	private DisasterMessageData mData;

	public DisasterMessageRow(Context context) {
		super(context);
		
		View.inflate(context, R.layout.row_disaster_message, this);
		
		mIconImageView = (ImageView)findViewById(R.id.row_disaster_message_ImageView_icon);
		mContentTextView = (TextView)findViewById(R.id.row_disaster_message_TextView_content);
		mDateTextView = (TextView)findViewById(R.id.row_disaster_message_TextView_date);
		
	}
	
	public void setData(DisasterMessageData data) {
		mData = data;
		
		mIconImageView.setImageResource(DisasterDisplayUtils.getDisasterIconResource(data.getDisasterType()));
		mContentTextView.setText(data.getContent());
		mDateTextView.setText(DisasterDisplayUtils.getDisplayTimestamp(data.getDate()));
	}
	
}
