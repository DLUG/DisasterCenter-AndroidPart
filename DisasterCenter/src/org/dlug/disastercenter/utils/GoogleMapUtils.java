package org.dlug.disastercenter.utils;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapUtils {


	public static Marker setMarker(GoogleMap googleMap, double latitude, double longitude, int resId) {
		return setMarker(googleMap,  new LatLng(latitude, longitude), resId);
	}
	
	public static Marker setMarker(GoogleMap googleMap, LatLng latLng, int resId) {
		if ( googleMap == null || latLng == null )
			return null;
		
		return googleMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(resId)));
	}
	
	

	public static void moveMapViewCenter(GoogleMap googleMap, double latitude, double longitude, boolean withAnim) {
		moveMapViewCenter(googleMap, new LatLng(latitude, longitude), withAnim);
	}
	

	public static void moveMapViewCenter(GoogleMap googleMap, LatLng latLng, boolean withAnim) {
		if ( googleMap == null || latLng == null)
			return ;
		
		if ( withAnim ) {
			googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
		}
		else {
			googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		}
	}
	
}
