package org.dlug.disastercenter.row;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.data.DisasterInfoData;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class DisasterInfoRow extends FrameLayout {
	private ImageView mIconImageView;
	private TextView mTitleTextView;
	
	private DisasterInfoData mData;

	public DisasterInfoRow(Context context) {
		super(context);
		
		View.inflate(context, R.layout.row_disaster_info, this);
		
		mIconImageView = (ImageView)findViewById(R.id.row_disaster_info_ImageView_icon);
		mTitleTextView = (TextView)findViewById(R.id.row_disaster_info_TextView_text);
	}
	
	public void setData(DisasterInfoData data) {
		mData = data;
		
		// TODO icon setting
		switch ( data.getDisasterType() ) {
			
		}
		mIconImageView.setImageResource(R.drawable.info_img_document);
		mTitleTextView.setText(data.getTitle());
	}
	
}
