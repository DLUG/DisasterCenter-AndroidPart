package org.dlug.disastercenter.service;

import org.dlug.disastercenter.http.api.DisasterApi;
import org.dlug.disastercenter.preference.DisasterPreference;
import org.dlug.disastercenter.utils.LocationUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.IBinder;
import android.os.Message;

public class DisasterService extends Service {
	private final static long SENSING_INTERVAL = 1000 * 60 * 30;
	private final static long UPDATE_INTERVAL = 1000 * 60 * 1;
	
	private LocationManager mLocationManager;
	private Handler mHandler;
	private DisasterApi mDisasterApi;
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		mDisasterApi = new DisasterApi(this);
		mHandler = new Handler(mLocationUpdateCallback);

		mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, SENSING_INTERVAL, 0, mLocationListener);
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, SENSING_INTERVAL, 0, mLocationListener);
		mHandler.sendEmptyMessage(-1);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
		mLocationManager.removeUpdates(mLocationListener);
	}
	
	private Callback mLocationUpdateCallback = new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			Context context = getApplicationContext();
			Location location = LocationUtils.getLastKnownLocation(context);
			
			if ( location != null ) {
				int alertRange = DisasterPreference.getAlarmRange(context);
				mDisasterApi.putLocationAsync(-1, location.getLatitude(), location.getLongitude(), alertRange, null);
			}
			mHandler.sendEmptyMessageDelayed(-1, UPDATE_INTERVAL);
			return false;
		}
	};
	
	private LocationListener mLocationListener = new LocationListener() {
		
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
		
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		
		@Override
		public void onLocationChanged(Location location) {
			
		}
	};
}
