package org.dlug.disastercenter.fragment;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.data.DisasterInfoData;
import org.dlug.disastercenter.data.DisasterReportListData;
import org.dlug.disastercenter.fragment.XenixMapFragment.OnGoogleMapCreateListener;
import org.dlug.disastercenter.utils.GoogleMapUtils;
import org.dlug.disastercenter.utils.LocationUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;

@SuppressLint("ValidFragment")
public class DisasterReportViewFragment extends BaseFragment {

	private DisasterReportListData mData;
	

	public DisasterReportViewFragment(DisasterReportListData data) {
		mData = data;	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Context context = getActivity();
		View view = inflater.inflate(R.layout.fragment_disaster_report_view, null);
		

		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
	
	}


	
}