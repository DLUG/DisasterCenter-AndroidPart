package org.dlug.disastercenter.view;

import org.dlug.disastercenter.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuView extends FrameLayout {
	
	private ImageView mIconImageView;
	private TextView mNameTextView;
	
	
	
	public MenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public MenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public MenuView(Context context) {
		super(context);
		init(context);
	}
	
	private void init(Context context) {
		View view = View.inflate(context, R.layout.view_menu, null);
		addView(view);

		mIconImageView = (ImageView)view.findViewById(R.id.view_menu_ImageView_icon);
		mNameTextView = (TextView)view.findViewById(R.id.view_menu_TextView_name);
	}
	
	public void setMenuData(int iconResid, String name) {
		
		mIconImageView.setImageResource(iconResid);
		mNameTextView.setText(name);
//		mCountTextView.setText(String.valueOf(menuData.count));
	}
	
}
