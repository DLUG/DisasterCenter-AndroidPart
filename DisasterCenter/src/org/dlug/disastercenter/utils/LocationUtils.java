package org.dlug.disastercenter.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

public class LocationUtils {

	public static Location getLastKnownLocation(Context context) {
		LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if ( location == null )
			location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		
		if ( location == null )
			location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
		
		return location;
	}
	
}

