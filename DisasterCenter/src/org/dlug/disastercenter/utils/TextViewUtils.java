package org.dlug.disastercenter.utils;

import java.util.ArrayList;

import android.graphics.Paint;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.widget.TextView;

public class TextViewUtils {
	public static void drawMidLine(TextView textView) {
		textView.setPaintFlags(textView.getPaintFlags()|Paint.STRIKE_THRU_TEXT_FLAG);
	}
	
	public static void undrawMidLine(TextView textView) {
		textView.setPaintFlags(textView.getPaintFlags()|~Paint.STRIKE_THRU_TEXT_FLAG);
	}
	
	public static void drawUnderLine(TextView textView) {
		textView.setPaintFlags(textView.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
	}
	
	public static void undrawUnderLine(TextView textView) {
		textView.setPaintFlags(textView.getPaintFlags()|~Paint.UNDERLINE_TEXT_FLAG);
	}
	
	public static void setBold(TextView textView, boolean work) {
		int flags = textView.getPaintFlags();
		if ( work ) {
			textView.setPaintFlags(flags | Paint.FAKE_BOLD_TEXT_FLAG);
		}
		else {
			textView.setPaintFlags(flags |~ Paint.FAKE_BOLD_TEXT_FLAG);
		}
	}
	
	public static void setMaxLength(TextView textView, int length) {
		InputFilter[] inputFilters = textView.getFilters();
		ArrayList<InputFilter> inputFilterArray = new ArrayList<InputFilter>();
		
		if ( inputFilters != null ) {
			for ( int i = 0; i < inputFilters.length; i++ ) {
				InputFilter inputFilter = inputFilters[i];
				
				if ( !(inputFilter instanceof LengthFilter) ) 
					inputFilterArray.add(inputFilter);
			}
			
		}
		inputFilterArray.add(new LengthFilter(length));
		textView.setFilters(inputFilterArray.toArray(new InputFilter[0]));		
	}
}
