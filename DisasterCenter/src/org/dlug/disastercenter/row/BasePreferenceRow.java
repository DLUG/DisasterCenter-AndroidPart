package org.dlug.disastercenter.row;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.utils.TextViewUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public abstract class BasePreferenceRow extends FrameLayout {
	private ViewGroup mValueLayout;
	private TextView mNameTextView;
	
	public BasePreferenceRow(Context context) {
		super(context);
		init(context);
	}
	
	
	public BasePreferenceRow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}


	public BasePreferenceRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}


	private void init(Context context) {
		mValueLayout = (ViewGroup)View.inflate(context, R.layout.row_preference, null);
		addView(mValueLayout);
		
		mNameTextView = (TextView)findViewById(R.id.row_preference_TextView_name);
		TextViewUtils.setBold(mNameTextView, true);
		setClickable(true);
	}
	
	
	public void setName(CharSequence name) {
		mNameTextView.setText(name);
	}
	
	protected void addValueView(View view) {
		mValueLayout.addView(view);
	}
	
}










