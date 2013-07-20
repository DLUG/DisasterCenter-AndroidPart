package org.dlug.disastercenter.adapter;

import java.util.List;

import org.dlug.disastercenter.data.DisasterInfoData;
import org.dlug.disastercenter.row.DisasterInfoRow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DisasterInfoAdapter extends BaseAdapter {
	private Context mContext;
	private List<DisasterInfoData> mDataList;
	
	public DisasterInfoAdapter(Context context, List<DisasterInfoData> dataList) {	
		mContext = context;
		mDataList = dataList;
	}
	
	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public DisasterInfoData getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DisasterInfoRow row = (DisasterInfoRow)convertView;
		
		if ( row == null ) {
			row = new DisasterInfoRow(mContext);
		}
		
		DisasterInfoData data = getItem(position);
		row.setData(data);
		
		return row;
	}

}
