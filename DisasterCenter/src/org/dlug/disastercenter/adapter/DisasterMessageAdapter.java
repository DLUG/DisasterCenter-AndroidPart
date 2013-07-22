package org.dlug.disastercenter.adapter;

import java.util.List;

import org.dlug.disastercenter.data.DisasterMessageData;
import org.dlug.disastercenter.row.DisasterMessageRow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DisasterMessageAdapter extends BaseAdapter {
	private Context mContext;
	private List<DisasterMessageData> mDataList;
	
	public DisasterMessageAdapter(Context context, List<DisasterMessageData> dataList) {	
		mContext = context;
		mDataList = dataList;
	}
	
	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public DisasterMessageData getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DisasterMessageRow row = (DisasterMessageRow)convertView;
		
		if ( row == null ) {
			row = new DisasterMessageRow(mContext);
		}
		
		DisasterMessageData data = getItem(position);
		row.setData(data);
		
		return row;
	}

}
