package org.dlug.disastercenter.fragment;

import java.util.ArrayList;
import java.util.List;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.adapter.DisasterMessageAdapter;
import org.dlug.disastercenter.data.DisasterMessageData;
import org.dlug.disastercenter.db.DBAdapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class MessageBoxFragment extends BaseFragment {
	private ListView mListView;
	private DisasterMessageAdapter mAdapter;
	private List<DisasterMessageData> mDataList;
	private DBAdapter mDbAdapter;
	
	@Override
	public View onCreateViewEx(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Context context = getActivity();
		
		mDbAdapter = new DBAdapter(context);
		
		View view = inflater.inflate(R.layout.fragment_messagebox, null);
		mListView = (ListView)view.findViewById(R.id.fragment_messagebox_ListView);
		
		mDataList = new ArrayList<DisasterMessageData>();
		mAdapter = new DisasterMessageAdapter(context, mDataList);
		
		mListView.setAdapter(mAdapter);
		
		

		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		refreshMessage();
	}
	
	@Override
	protected String getTitle() {
		return "수신메세지";
	}

	
	private void refreshMessage() {
		if ( mDataList == null ) {
			return ;
		}
		
		mDataList.clear();
		
		mDbAdapter.open();
		mDataList.addAll(mDbAdapter.getDisasterMessageDataListAll());
		mDbAdapter.close();

		mAdapter.notifyDataSetChanged();
	}
}