package org.dlug.disastercenter.fragment;

import java.util.ArrayList;
import java.util.List;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.adapter.DisasterInfoAdapter;
import org.dlug.disastercenter.constSet.ConstSet.InfoListType;
import org.dlug.disastercenter.data.DisasterInfoData;
import org.dlug.disastercenter.http.api.BaseApi.ApiDelegate;
import org.dlug.disastercenter.http.api.DisasterApi;
import org.dlug.disastercenter.http.response.DisasterInfoResponse;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.SupportMapFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public abstract class DisasterBaseInfoListFragment extends BaseFragment {
	private static final int REQUEST_REFRESH = 0x01;
	private static final int REQUEST_MORE = 0x02;
	
	private Activity mActivity;
	private DisasterApi mDisasterApi;
	
	private PullToRefreshListView mPullToRefreshListView;
	private DisasterInfoAdapter mAdapter;
	private List<DisasterInfoData> mDataList;
	
	private View mMoreView, mProgressView;
	
	
	@Override
	public View onCreateViewEx(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mActivity = getActivity();
		mDisasterApi = new DisasterApi(mActivity);
		View view = inflater.inflate(R.layout.fragment_disaster_info_list, null);

		mDataList = new ArrayList<DisasterInfoData>();
		mAdapter = new DisasterInfoAdapter(mActivity, mDataList);
		
		mPullToRefreshListView = (PullToRefreshListView)view.findViewById(R.id.fragment_disaster_info_list_PullToRefreshListView);
		mPullToRefreshListView.setOnRefreshListener(mRefreshListener);
		mPullToRefreshListView.setOnItemClickListener(mOnItemClickListener);

		mPullToRefreshListView.setAdapter(mAdapter);

		
		Button moreButton = new Button(mActivity);
		moreButton.setText("More");
		mMoreView = moreButton;
		mPullToRefreshListView.addFooterView(mMoreView);
		mMoreView.setOnClickListener(mMoreListener);
		
		getDataAsync(REQUEST_REFRESH);
		
		return view;
	}
	
	private void getDataAsync(int requestTag) {
		long offset = 0;
		
		switch ( requestTag ) {
		case REQUEST_REFRESH:

			break;
			
		case REQUEST_MORE:
			int listSize = mDataList.size();
			if ( listSize != 0 ) {
				DisasterInfoData lastData = mDataList.get(listSize - 1);
				offset = lastData.getIndex() - 1;
			}
			
			break;
		}
		
		mMoreView.setVisibility(View.GONE);
		
		switch ( getInfoListType() ) {
		case InfoListType.INFO:
			mDisasterApi.disasterInfoAsync(requestTag, offset, mInfoResponseDelegate);
			break;
			
		case InfoListType.NEWS:
			mDisasterApi.disasterNewsAsync(requestTag, offset, mInfoResponseDelegate);
			break;
		}
	}
	
	
	
	// 
	protected abstract int getInfoListType();
	
	
	
	//
	private OnClickListener mMoreListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			getDataAsync(REQUEST_MORE);
		}
	};
	
	//
	private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> adapter, View row, int position, long id) {
			
			DisasterInfoData data = (DisasterInfoData)adapter.getItemAtPosition(position);
			View view = getView();
			ViewParent viewParent = view.getParent();
			
			if ( View.class.isInstance(viewParent)  ) {
				int parentId = ((View)viewParent).getId();
				
				Bundle extras = new Bundle(1);
				extras.putSerializable(DisasterInfoData.INTENT_FILTER, data);
				
				String fragmentName = DisasterInfoViewFragment.class.getName();
				Fragment fragment = new DisasterInfoViewFragment(data);
				
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
	
	private ApiDelegate<DisasterInfoResponse> mInfoResponseDelegate = new ApiDelegate<DisasterInfoResponse>() {
		
		@Override
		public void onResponse(final int requestTag, final DisasterInfoResponse response) {
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
							DisasterInfoData data = response.getData(i);
							mDataList.add(data);
						}
						
						int dataSize = mDataList.size();
						if ( dataSize != 0 ) {
							DisasterInfoData lastData = mDataList.get(dataSize - 1);
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