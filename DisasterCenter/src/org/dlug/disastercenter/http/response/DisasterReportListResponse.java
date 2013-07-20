package org.dlug.disastercenter.http.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dlug.disastercenter.data.DisasterReportListData;
import org.json.JSONArray;
import org.json.JSONObject;

public class DisasterReportListResponse extends BaseDisasterResponse {
	private List<DisasterReportListData> mDataList;
	private int mTotalAmount;
	
	public DisasterReportListResponse(JSONObject jsonObject) {
		super(jsonObject);
		
		mTotalAmount = jsonObject.optInt("total_amount");
		JSONArray dataArray = jsonObject.optJSONArray("data");
		
		
		if ( dataArray != null ) {
			int dataLength = dataArray.length();
			
			mDataList = new ArrayList<DisasterReportListData>(dataLength);
			
			for ( int i = 0; i < dataLength; i++ ) {
				JSONObject dataObject = dataArray.optJSONObject(i);
				if ( dataObject != null ) {
					mDataList.add(new DisasterReportListData(dataObject));
				}
			}
		}
		else {
			mDataList = new ArrayList<DisasterReportListData>(0);
		}
	}
	
	public int getTotalAmount() {
		return mTotalAmount;
	}
	
	public Iterator<DisasterReportListData> iterator() {
		return mDataList.iterator();
	}
	
	public int size() {
		return mDataList.size();
	}
	
	public DisasterReportListData getData(int index) {
		return mDataList.get(index);
	}
}
