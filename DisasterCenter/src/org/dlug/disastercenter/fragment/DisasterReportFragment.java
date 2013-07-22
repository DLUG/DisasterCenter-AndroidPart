package org.dlug.disastercenter.fragment;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.adapter.DisasterTypeAdapter;
import org.dlug.disastercenter.data.DisasterTypeData;
import org.dlug.disastercenter.fragment.XenixMapFragment.OnGoogleMapCreateListener;
import org.dlug.disastercenter.http.api.BaseApi.ApiDelegate;
import org.dlug.disastercenter.http.api.DisasterApi;
import org.dlug.disastercenter.http.response.BaseDisasterResponse;
import org.dlug.disastercenter.utils.GoogleMapUtils;
import org.dlug.disastercenter.utils.LocationUtils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class DisasterReportFragment extends BaseFragment {
	private GoogleMap mGoogleMap;
	private XenixMapFragment mSupportMapFragment;
	
	private Button mDisasterTypeButton;
	private Button mReportButton;
	
	private EditText mReportContentEditText;
	
	private DisasterApi mDisasterApi;
	
	private ProgressDialog mProgressDialog;
	private DisasterTypeAdapter mDisasterTypeAdapter;
	
	private DisasterTypeData mSelectedData;
	
	private Marker mMyLocationMarker;

	
	@Override
	public View onCreateViewEx(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.fragment_disaster_report, null);
	
		
		mDisasterTypeButton = (Button)view.findViewById(R.id.fragment_disaster_report_Button_disasterType);
		mDisasterTypeButton.setOnClickListener(mOnClickListener);

		mReportButton = (Button)view.findViewById(R.id.fragment_disaster_report_Button_report);
		mReportButton.setOnClickListener(mOnClickListener);
		mReportButton.setEnabled(false);
		
		mReportContentEditText = (EditText)view.findViewById(R.id.fragment_disaster_report_EditText_content);
		
		Context context = getActivity();
		mDisasterApi = new DisasterApi(context);
		mProgressDialog = new ProgressDialog(context);
		
		initView();
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
			.add(R.id.fragment_disaster_report_FrameLayout_map, mSupportMapFragment, "2")
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
		return "재난신고";
	}
	
	private void initView() {
		mReportContentEditText.setText("");
		mSelectedData = null;
		mReportButton.setEnabled(false);
		mDisasterTypeButton.setText("재난유형");
		mProgressDialog.dismiss();
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
	
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch ( v.getId() ) {
			case R.id.fragment_disaster_report_Button_disasterType:
				if ( mDisasterTypeAdapter == null ) {
					mDisasterTypeAdapter = new DisasterTypeAdapter(getActivity());
				}
				
				new AlertDialog.Builder(getActivity())
				.setSingleChoiceItems(mDisasterTypeAdapter, -1, mDisasterTypeClickListener)
				.create()
				.show();
				break;
				
			case R.id.fragment_disaster_report_Button_report:
				Location myLocation = mGoogleMap.getMyLocation();
				String content = mReportContentEditText.getText().toString();
				
				if ( mSelectedData != null ) {
					mProgressDialog.show();
					mDisasterApi.reportDisasterAsync(-1, myLocation.getLatitude(), myLocation.getLongitude(), myLocation.getAccuracy(), mSelectedData.getType(), content, mApiDelegate);	
				}
				
				break;
			}
		}
	};
	
	private DialogInterface.OnClickListener mDisasterTypeClickListener = new DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			dialog.dismiss();
			mSelectedData = mDisasterTypeAdapter.getItem(which);
			mDisasterTypeButton.setText(mSelectedData.getDisplayName());
			mReportButton.setEnabled(true);
		}
	};
	
	private ApiDelegate<BaseDisasterResponse> mApiDelegate = new ApiDelegate<BaseDisasterResponse>() {

		@Override
		public void onResponse(int requestTag, BaseDisasterResponse response) {
			if ( response.isSuccess() ) {
				getActivity().runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						Toast.makeText(getActivity(), "신고 완료", Toast.LENGTH_SHORT).show();
						initView();
					}
				});
			}
			else {
				onError(requestTag, new Exception(response.getMessage()));
			}
		}

		@Override
		public void onError(int requestTag, final Exception e) {
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();

					mProgressDialog.dismiss();
				}
			});			
		}

		@Override
		public void onCancel(int requestTag) {
			getActivity().runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mProgressDialog.dismiss();
				}
			});			
		}
	};

}