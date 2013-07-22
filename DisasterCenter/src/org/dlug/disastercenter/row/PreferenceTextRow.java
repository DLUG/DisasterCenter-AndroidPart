package org.dlug.disastercenter.row;

import org.dlug.disastercenter.utils.TextViewUtils;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.TextView;

public class PreferenceTextRow extends BasePreferenceRow {
	private TextView mTextView;

	public PreferenceTextRow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public PreferenceTextRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PreferenceTextRow(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		mTextView = new TextView(context);
		mTextView.setTextColor(Color.rgb(0x9A, 0x9A, 0x9A));
		mTextView.setPadding(0, 0, 10, 0);
		TextViewUtils.setBold(mTextView, true);
		
		addValueView(mTextView);
	}
	
	public void setText(CharSequence text) {
		mTextView.setText(text);
	}

}
