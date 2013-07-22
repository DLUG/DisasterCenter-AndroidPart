package org.dlug.disastercenter.adapter;

import org.dlug.disastercenter.constSet.ConstSet.DisasterCode;
import org.dlug.disastercenter.data.DisasterTypeData;
import org.dlug.disastercenter.row.DisasterTypeRow;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class DisasterTypeAdapter extends BaseAdapter {
	private Context mContext;
	private DisasterTypeData[] mDatas;
	
	public DisasterTypeAdapter(Context context) {	
		mContext = context;
		
		mDatas = new DisasterTypeData[]{
				new DisasterTypeData(DisasterCode.HEAVY_RAIN),
				new DisasterTypeData(DisasterCode.OVER_FLOOD),
				new DisasterTypeData(DisasterCode.SURGE),
				new DisasterTypeData(DisasterCode.LANDSLIDE),
				new DisasterTypeData(DisasterCode.HEAVY_SNOW),
				new DisasterTypeData(DisasterCode.AVALANCHE),
				new DisasterTypeData(DisasterCode.BITTER_COLD),
				new DisasterTypeData(DisasterCode.HEAT_WAVE),
				new DisasterTypeData(DisasterCode.BUILD_DESTROY),
				new DisasterTypeData(DisasterCode.BRIDGE_DESTROY)
		};
	}
	
	@Override
	public int getCount() {
		return mDatas.length;
	}

	@Override
	public DisasterTypeData getItem(int position) {
		return mDatas[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		DisasterTypeRow row = (DisasterTypeRow)convertView;
		if ( row == null ) {
			row = new DisasterTypeRow(mContext);
		}
		
		DisasterTypeData data = getItem(position);
		row.setData(data);
		
		return row;
	}

}
