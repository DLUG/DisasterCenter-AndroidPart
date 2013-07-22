package org.dlug.disastercenter.row;

import org.dlug.disastercenter.R;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

public class PreferenceCheckRow extends BasePreferenceRow {
	private CheckBox mCheckBox;

	public PreferenceCheckRow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public PreferenceCheckRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public PreferenceCheckRow(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		mCheckBox = new CheckBox(context);
		mCheckBox.setClickable(false);
		mCheckBox.setButtonDrawable(R.drawable.checkbox_setting_selector);
		addValueView(mCheckBox);
	}
	
	public void setChecked(boolean checked) {
		mCheckBox.setChecked(checked);
	}
	
	public boolean isChecked() {
		return mCheckBox.isChecked();
	}
}
