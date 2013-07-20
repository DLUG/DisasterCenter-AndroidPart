package org.dlug.disastercenter.data;

import java.io.Serializable;

import org.json.JSONObject;

public class DisasterReportListData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3908116303645903529L;
	public static final String INTENT_FILTER = DisasterReportListData.class.getName();
	
	private final long mIndex;
	private final double mLatitude, mLongitude;
	private final long mTimestamp;
	private final int mReportType, mDisasterType;
	private final String mContent;
	
	
	public DisasterReportListData(JSONObject jsonObject) {
		mIndex = jsonObject.optLong("idx", -1);
		
		mLatitude = jsonObject.optDouble("lat");
		mLongitude = jsonObject.optDouble("lng");
		
		mTimestamp = jsonObject.optLong("timestamp", -1);
		mReportType = jsonObject.optInt("type_report");
		mDisasterType = jsonObject.optInt("type_disaster");
		
		mContent = jsonObject.optString("content"); 
	}


	public long getIndex() {
		return mIndex;
	}


	public double getLatitude() {
		return mLatitude;
	}


	public double getLongitude() {
		return mLongitude;
	}


	public long getTimestamp() {
		return mTimestamp;
	}


	public int getReportType() {
		return mReportType;
	}


	public int getDisasterType() {
		return mDisasterType;
	}


	public String getContent() {
		return mContent;
	}
	
}
