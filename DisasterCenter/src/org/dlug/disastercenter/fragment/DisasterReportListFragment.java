package org.dlug.disastercenter.fragment;

import java.util.ArrayList;
import java.util.List;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.adapter.DisasterReportAdapter;
import org.dlug.disastercenter.data.DisasterReportListData;
import org.dlug.disastercenter.fragment.XenixMapFragment.OnGoogleMapCreateListener;
import org.dlug.disastercenter.http.api.BaseApi.ApiDelegate;
import org.dlug.disastercenter.http.api.DisasterApi;
import org.dlug.disastercenter.http.response.DisasterReportListResponse;
import org.dlug.disastercenter.utils.GoogleMapUtils;
import org.dlug.disastercenter.utils.LocationUtils;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class DisasterReportListFragment extends BaseFragment {

	
	private static final int REQUEST_REFRESH = 0x01;
	private static final int REQUEST_MORE = 0x02;
	
	private Activity mActivity;
	private DisasterApi mDisasterApi;
	
	private PullToRefreshListView mPullToRefreshListView;
	private DisasterReportAdapter mAdapter;
	private List<DisasterReportListData> mDataList;
	
	private View mMoreView;
	
	private XenixMapFragment mSupportMapFragment;
	private GoogleMap mGoogleMap;
	
	@Override
	public View onCreateViewEx(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mActivity = getActivity();
		mDisasterApi = new DisasterApi(mActivity);
		
		View view = inflater.inflate(R.layout.fragment_disaster_report_list, null);
		

		mDataList = new ArrayList<DisasterReportListData>();
		mAdapter = new DisasterReportAdapter(mActivity, mDataList);
		
		mPullToRefreshListView = (PullToRefreshListView)view.findViewById(R.id.fragment_disaster_report_list_PullToRefreshListView);
		mPullToRefreshListView.setOnRefreshListener(mRefreshListener);
		mPullToRefreshListView.setOnItemClickListener(mOnItemClickListener);
		

		mPullToRefreshListView.setAdapter(mAdapter);

		
		Button moreButton = new Button(mActivity);
		moreButton.setText("More");
		mMoreView = moreButton;
		mPullToRefreshListView.addFooterView(mMoreView);
		
		getDataAsync(REQUEST_REFRESH);
		
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
			.add(R.id.fragment_disaster_report_list_map, mSupportMapFragment, "1")
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
		return "재난신고현황";
	}

	
	
	private void getDataAsync(int requestTag) {
		long offset = 0;
		
		switch ( requestTag ) {
		case REQUEST_REFRESH:

			break;
			
		case REQUEST_MORE:
			int listSize = mDataList.size();
			if ( listSize != 0 ) {
				DisasterReportListData lastData = mDataList.get(listSize - 1);
				offset = lastData.getIndex() - 1;
			}
			
			break;
		}
		mMoreView.setVisibility(View.GONE);
		mDisasterApi.disasterReportListAsync(requestTag, offset, mReportListResponseDelegate);
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
	
	//
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View row, int position, long id) {
			
			DisasterReportListData data = (DisasterReportListData)adapter.getItemAtPosition(position);
			View view = getView();
			ViewParent viewParent = view.getParent();
			
			if ( View.class.isInstance(viewParent)  ) {
				int parentId = ((View)viewParent).getId();
				
				Bundle extras = new Bundle(1);
				extras.putSerializable(DisasterReportListData.INTENT_FILTER, data);
				
				String fragmentName = DisasterInfoViewFragment.class.getName();
				Fragment fragment = new DisasterReportViewFragment(data);
				
				getFragmentManager().beginTransaction()
				.add(parentId, fragment, fragmentName)
				.addToBackStack(fragmentName)
				.commitAllowingStateLoss();
			}
			
		}
	};
	
	private OnRefreshListener<ListView> mRefreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			getDataAsync(REQUEST_REFRESH);
		}
	};
	
	private ApiDelegate<DisasterReportListResponse> mReportListResponseDelegate = new ApiDelegate<DisasterReportListResponse>() {
		
		@Override
		public void onResponse(final int requestTag, final DisasterReportListResponse response) {
			if ( response.isSuccess() ) {
				mActivity.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						mPullToRefreshListView.onRefreshComplete();
						
						switch ( requestTag ) {
						case REQUEST_REFRESH:
							mDataList.clear();
							break;
						}
						
						int size = response.size();
						for ( int i = 0; i < size; i++ ) {
							DisasterReportListData data = response.getData(i);
							mDataList.add(data);
						}
						
						int dataSize = mDataList.size();
						if ( dataSize != 0 ) {
							DisasterReportListData lastData = mDataList.get(dataSize - 1);
							long lastDataIndex = lastData.getIndex();
							if ( lastDataIndex > 1 ) {
								// footer 추가
								mMoreView.setVisibility(View.VISIBLE);
								
							}
						}
						
						mAdapter.notifyDataSetChanged();
					}
				});
			}
			else {
				onError(requestTag, new Exception(response.getMessage()));
			}
		}
		
		@Override
		public void onError(int requestTag, final Exception e) {
			mActivity.runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					mPullToRefreshListView.onRefreshComplete();
					
					Toast.makeText(mActivity, e.toString(), Toast.LENGTH_SHORT).show();
				}
			});
		}
		
		@Override
		public void onCancel(int requestTag) {
			
		}
	};


}