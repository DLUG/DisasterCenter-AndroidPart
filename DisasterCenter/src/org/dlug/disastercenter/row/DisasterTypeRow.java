package org.dlug.disastercenter.row;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.data.DisasterTypeData;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class DisasterTypeRow extends FrameLayout {
	private TextView mTextView;
	
	public DisasterTypeRow(Context context) {
		super(context);
		init(context);
	}
	
	
	public DisasterTypeRow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}


	public DisasterTypeRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}


	private void init(Context context) {
		View.inflate(context, R.layout.row_disaster_type, this);
		
		mTextView = (TextView)findViewById(R.id.row_disaster_type_TextView);
	}
	
	
	public void setData(DisasterTypeData data) {
		mTextView.setText(data.getDisplayName());
	}

}










