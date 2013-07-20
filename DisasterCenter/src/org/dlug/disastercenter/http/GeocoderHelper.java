package org.dlug.disastercenter.http;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Looper;

public class GeocoderHelper {

	private HashMap<Integer, Thread> mGeocodingMap;
	private Context mContext;
	private Handler mHandler;
	private Geocoder mGeocoder;
	private GeocoderHelperDelegate mDelegate;
	
	
	public GeocoderHelper(Context context) {
		mContext = context;
		mHandler = new Handler(Looper.getMainLooper());
	
		mGeocoder = new Geocoder(mContext);
		mGeocodingMap = new HashMap<Integer, Thread>();
	}
	
	public void setDelegate(GeocoderHelperDelegate delegate) {
		mDelegate = delegate;
	}
	
	
	public void cancel(int tag) {
		Thread thread = mGeocodingMap.get(tag);
		if ( thread != null && thread.isAlive() ) {
			thread.interrupt();
		}
		mGeocodingMap.remove(tag);
	}
	
	public void release() {
		Set<Integer> tagSet = mGeocodingMap.keySet();
		for ( Integer tag : tagSet ) {
			Thread thread = mGeocodingMap.get(tag);
			if ( thread != null && thread.isAlive() ) {
				thread.interrupt();
			}
		}
		mGeocodingMap.clear();
	}
	
	public void requestAddressFromLocation(final int tag, final double latitude, final double longitude, final int maxResults) {
		Thread geocodingThread = new Thread(){
			@Override
			public void run() {
				try {
					final List<Address> addressList = mGeocoder.getFromLocation(latitude, longitude, maxResults);
					
					if ( Thread.currentThread().isInterrupted() )
						return ;
					
					mGeocodingMap.remove(tag);
					
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							if ( mDelegate != null ) {
								mDelegate.onReceiveAddressList(tag, addressList);
							}
						}
					});
					
				} catch (final Exception e) {
					
					if ( Thread.currentThread().isInterrupted() )
						return ;
					
					mGeocodingMap.remove(tag);
					
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							if ( mDelegate != null ) {
								mDelegate.onReceiveFail(tag, e);
							}
						}
					});
				}

			}
		};
		
		mGeocodingMap.put(tag, geocodingThread);
		geocodingThread.start();
		
	}
	
	public void requestAddressFromLocationName(final int tag, final String locationName, final int maxResults) {
		Thread geocodingThread = new Thread(){
			@Override
			public void run() {
				try {
					final List<Address> addressList = mGeocoder.getFromLocationName(locationName, maxResults);
					
					if ( Thread.currentThread().isInterrupted() )
						return ;
					
					mGeocodingMap.remove(tag);
					
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							if ( mDelegate != null ) {
								mDelegate.onReceiveAddressList(tag, addressList);
							}
						}
					});
					
				} catch (final Exception e) {
					if ( Thread.currentThread().isInterrupted() )
						return ;
					
					mGeocodingMap.remove(tag);
					
					mHandler.post(new Runnable() {
						
						@Override
						public void run() {
							if ( mDelegate != null ) {
								mDelegate.onReceiveFail(tag, e);
							}
						}
					});
				}

			}
		};
		
		mGeocodingMap.put(tag, geocodingThread);
		geocodingThread.start();
	}
	
	
	public static interface GeocoderHelperDelegate {
		public void onReceiveAddressList(int tag, List<Address> addressList);
		public void onReceiveFail(int tag, Exception e);
	}
}
