package org.dlug.disastercenter.adapter;

import java.util.List;

import org.dlug.disastercenter.data.DisasterReportListData;
import org.dlug.disastercenter.row.DisasterReportRow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DisasterReportAdapter extends BaseAdapter {
	private Context mContext;
	private List<DisasterReportListData> mDataList;
	
	public DisasterReportAdapter(Context context, List<DisasterReportListData> dataList) {	
		mContext = context;
		mDataList = dataList;
	}
	
	@Override
	public int getCount() {
		return mDataList.size();
	}

	@Override
	public DisasterReportListData getItem(int position) {
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DisasterReportRow row = (DisasterReportRow)convertView;
		
		if ( row == null ) {
			row = new DisasterReportRow(mContext);
		}
		
		DisasterReportListData data = getItem(position);
		row.setData(data);
		
		return row;
	}

}
