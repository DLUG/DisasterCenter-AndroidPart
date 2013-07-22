package org.dlug.disastercenter.fragment;

import java.util.List;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.data.DisasterReportListData;
import org.dlug.disastercenter.fragment.XenixMapFragment.OnGoogleMapCreateListener;
import org.dlug.disastercenter.http.GeocoderHelper;
import org.dlug.disastercenter.http.GeocoderHelper.GeocoderHelperDelegate;
import org.dlug.disastercenter.row.DisasterReportRow;
import org.dlug.disastercenter.utils.GoogleMapUtils;
import org.dlug.disastercenter.utils.LocationUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

@SuppressLint("ValidFragment")
public class DisasterReportViewFragment extends BaseFragment {

	private DisasterReportListData mData;
	private XenixMapFragment mSupportMapFragment;
	private GoogleMap mGoogleMap;
	
	private GeocoderHelper mGeocoderHelper;
	
	private TextView mAddressTextView;

	public DisasterReportViewFragment(DisasterReportListData data) {
		mData = data;	
	}
	
	@Override
	public View onCreateViewEx(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Context context = getActivity();
		View view = inflater.inflate(R.layout.fragment_disaster_report_view, null);
		
		DisasterReportRow row = (DisasterReportRow)view.findViewById(R.id.fragment_disaster_report_view_DisasterReportRow);
		row.setData(mData);
		
		mAddressTextView = (TextView)view.findViewById(R.id.fragment_disaster_report_view_TextView_address);
		TextView contentTextView = (TextView)view.findViewById(R.id.fragment_disaster_report_view_TextView_content);
		
		contentTextView.setText(mData.getContent());
		
		mGeocoderHelper = new GeocoderHelper(context);

		mGeocoderHelper.setDelegate(mGeocoderHelperDelegate);
		mGeocoderHelper.requestAddressFromLocation(-1, mData.getLatitude(), mData.getLongitude(), 1);
		
		return view;
	}
	

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		if ( mSupportMapFragment == null ) {
			mSupportMapFragment = new XenixMapFragment();
			mSupportMapFragment.setOnGoogleMapCreateListener(mGoogleMapCreateListener);
		}

		if ( !mSupportMapFragment.isAdded() ) {
			getFragmentManager()
			.beginTransaction()
			.add(R.id.fragment_disaster_report_view_Map, mSupportMapFragment, "3")
			.commitAllowingStateLoss();
		}

	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if ( mSupportMapFragment.isAdded() ) {
			getFragmentManager().beginTransaction().remove(mSupportMapFragment).commitAllowingStateLoss();
		}
	}
	


	@Override
	protected String getTitle() {
		return mData.getContent();
	}

	private OnGoogleMapCreateListener mGoogleMapCreateListener = new OnGoogleMapCreateListener() {
		
		@Override
		public void onCreateGoogleMap(GoogleMap googleMap) {
			mGoogleMap = googleMap;
			googleMap.setMyLocationEnabled(false);
			
			UiSettings uiSettings = googleMap.getUiSettings();
			uiSettings.setZoomControlsEnabled(false);
			uiSettings.setMyLocationButtonEnabled(false);
			uiSettings.setCompassEnabled(false);
			uiSettings.setAllGesturesEnabled(false);
			
			googleMap.moveCamera(CameraUpdateFactory.zoomTo(15));
			
			Location myLocation = LocationUtils.getLastKnownLocation(getActivity());
			GoogleMapUtils.setMarker(googleMap, myLocation.getLatitude(), myLocation.getLongitude(), R.drawable.pin_map);
			GoogleMapUtils.moveMapViewCenter(googleMap, new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), false);
		}
	};
	
	
	private GeocoderHelperDelegate mGeocoderHelperDelegate = new GeocoderHelperDelegate() {
		
		@Override
		public void onReceiveFail(int tag, Exception e) {
			
		}
		
		@Override
		public void onReceiveAddressList(int tag, List<Address> addressList) {
			if ( addressList != null && addressList.size() > 0 ) {
				Address address = addressList.get(0);

				int addressLineCount = address.getMaxAddressLineIndex() + 1;

				StringBuilder addressBuilder = new StringBuilder();


				for ( int i = 0; i < addressLineCount; i++ ) {
					addressBuilder.append(address.getAddressLine(i)).append(' ');
				}

				mAddressTextView.setText(addressBuilder.toString());
			}
		}
	};
	
}