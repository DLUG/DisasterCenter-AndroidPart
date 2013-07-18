package org.dlug.disastercenter.utils;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class InputMethodUtils {

	public static void hiddenSoftKeyboard(final Context context, final EditText editText) {
		editText.post(new Runnable() {
			@Override
			public void run() {
				InputMethodManager im = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
				im.hideSoftInputFromWindow(editText.getWindowToken(), 0);		
			}
		});
		
	}
	
	public static void showSoftKeyboard(final Context context, final EditText editText) {
		editText.post(new Runnable() {
			@Override
			public void run() {
				editText.requestFocus();
				InputMethodManager im = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
				im.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);		
			}
		});
	}
}
