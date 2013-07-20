package org.dlug.disastercenter.fragment;

import org.dlug.disastercenter.R;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;

public class DisasterReportFragment extends BaseFragment {
	private GoogleMap mGoogleMap;
	private SupportMapFragment mSupportMapFragment;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		
		View view = inflater.inflate(R.layout.fragment_disaster_report, null);
		mSupportMapFragment = (SupportMapFragment)getFragmentManager().findFragmentById(R.id.fragment_disaster_report_FrameLayout_map);
		mGoogleMap = mSupportMapFragment.getMap();
		
		UiSettings uiSettings = mGoogleMap.getUiSettings();
		uiSettings.setAllGesturesEnabled(false);
		
		Spinner spinner = (Spinner)view.findViewById(R.id.fragment_disaster_report_Spiner_disasterType);
		
//		fragment_disaster_report_Spiner_disasterType

		return view;
	}
	
	@Override
	public void onDestroyView() {
        getFragmentManager().beginTransaction().remove(mSupportMapFragment).commitAllowingStateLoss();
	    
		super.onDestroyView();
	}


}