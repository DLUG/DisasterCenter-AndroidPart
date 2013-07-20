package org.dlug.disastercenter.http.response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dlug.disastercenter.data.DisasterInfoData;
import org.json.JSONArray;
import org.json.JSONObject;

public class DisasterInfoResponse extends BaseDisasterResponse {
	private List<DisasterInfoData> mDataList;
	private int mTotalAmount;
	
	public DisasterInfoResponse(JSONObject jsonObject) {
		super(jsonObject);
		
		mTotalAmount = jsonObject.optInt("total_amount");
		JSONArray dataArray = jsonObject.optJSONArray("data");
		
		
		if ( dataArray != null ) {
			int dataLength = dataArray.length();
			
			mDataList = new ArrayList<DisasterInfoData>(dataLength);
			
			for ( int i = 0; i < dataLength; i++ ) {
				JSONObject dataObject = dataArray.optJSONObject(i);
				if ( dataObject != null ) {
					mDataList.add(new DisasterInfoData(dataObject));
				}
			}
		}
		else {
			mDataList = new ArrayList<DisasterInfoData>(0);
		}
	}
	
	public int getTotalAmount() {
		return mTotalAmount;
	}
	
	public Iterator<DisasterInfoData> iterator() {
		return mDataList.iterator();
	}
	
	public int size() {
		return mDataList.size();
	}
	
	public DisasterInfoData getData(int index) {
		return mDataList.get(index);
	}
}
