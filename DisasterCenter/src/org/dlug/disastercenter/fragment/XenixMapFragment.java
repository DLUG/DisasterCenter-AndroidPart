package org.dlug.disastercenter.fragment;

import android.app.Activity;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class XenixMapFragment extends SupportMapFragment {
	private OnGoogleMapCreateListener mGoogleMapCreateListener;
	private boolean mIsMapLoading;
	private Handler mHandler = new Handler(Looper.getMainLooper(), new Callback() {
		
		
		@Override
		public boolean handleMessage(Message msg) {
			GoogleMap googleMap = getMap();
			
			if ( googleMap != null ) { 
				if (mGoogleMapCreateListener != null )
					mGoogleMapCreateListener.onCreateGoogleMap(googleMap);
			}
			else {
				if ( mIsMapLoading )
					mHandler.sendMessageDelayed(msg, 10);
			}
			return false;
		}
	});

	public XenixMapFragment() {
	
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mIsMapLoading = false;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mIsMapLoading = true;
		mHandler.sendEmptyMessageDelayed(0, 10);
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mIsMapLoading = false;	
	}
		
	public void setOnGoogleMapCreateListener(OnGoogleMapCreateListener listener) {
		mGoogleMapCreateListener = listener;
	}

	
	public static interface OnGoogleMapCreateListener {
		public void onCreateGoogleMap(GoogleMap googleMap);
	}

}
