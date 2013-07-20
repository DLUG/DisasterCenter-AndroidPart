package org.dlug.disastercenter.popup;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class OverflowPopupWindow extends PopupWindow {
	private ArrayList<OverflowItem> mActionItems;
	private ArrayList<ImageView> mDividerList;
	
	private Context mContext;
	
	private LinearLayout mOverflowLayout;
	private OnOverflowSelectedListener mOnOverflowSelectedListener;
	private int mDividerId;

	public OverflowPopupWindow(Context context) {
		super(context);
		mContext = context;
		mActionItems = new ArrayList<OverflowItem>();
		mDividerList = new ArrayList<ImageView>();
		
		mOverflowLayout = new LinearLayout(context);
		mOverflowLayout.setOrientation(LinearLayout.VERTICAL);
		
		setContentView(mOverflowLayout);
		setWidth(LayoutParams.WRAP_CONTENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		
		setFocusable(true);
		setTouchable(true);
		setOutsideTouchable(true);
	}
	
	public void addItem(int itemId, String name) {
		if ( mActionItems.size() > 0 ) {
			ImageView divider = new ImageView(mContext);
			divider.setImageResource(mDividerId);
			divider.setScaleType(ScaleType.FIT_XY);
			mDividerList.add(divider);
			mOverflowLayout.addView(divider);
		}
		
		Button overflowButton = new Button(mContext);
		overflowButton.setText(name);
		overflowButton.setTextSize(12);
		overflowButton.setTextColor(Color.WHITE);
		overflowButton.setId(itemId);
		overflowButton.setOnClickListener(mClickListener);
		overflowButton.setBackgroundDrawable(null);
		overflowButton.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		
		
		OverflowItem item = new OverflowItem(itemId, name);
		
		overflowButton.setTag(item);
		
		mOverflowLayout.addView(overflowButton);
		mActionItems.add(item);
		
	}
	
	
	
	public void setOnOverflowSelectedListener(OnOverflowSelectedListener listener) {
		mOnOverflowSelectedListener = listener;
	}
	
	
	public void setDividerResource(int resId) {
		mDividerId = resId;
		
		for ( ImageView divider : mDividerList ) {
			divider.setImageResource(resId);
		}
	}
	
	private OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dismiss();
			if ( mOnOverflowSelectedListener == null )
				return ;
			
			OverflowItem item = (OverflowItem)v.getTag();
			mOnOverflowSelectedListener.onOverflowSelected(item.itemId, item.name);
			
		}
	};
	
	
	private static class OverflowItem {
		public final int itemId;
		public final String name;
		
		public OverflowItem(int itemId, String name) {
			this.itemId = itemId;
			this.name = name;
		}
	}
	
	public static interface OnOverflowSelectedListener {
		public void onOverflowSelected(int itemId, String name);
	}
}
