package org.dlug.disastercenter.data;

import java.io.Serializable;

import org.json.JSONObject;

public class DisasterInfoData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3227711663789723725L;
	public static final String INTENT_FILTER = DisasterInfoData.class.getName();
	
	private final long mIndex;
	private final double mLatitude, mLongitude;
	private final long mTimestamp;
	private final int mDisasterType;
	private final String mTitle;
	private final String mContent;

	public DisasterInfoData(JSONObject jsonObject) {
		mIndex = jsonObject.optLong("idx", -1);
		
		mLatitude = jsonObject.optDouble("lat");
		mLongitude = jsonObject.optDouble("lng");
		
		mTimestamp = jsonObject.optLong("timestamp", -1);
		mDisasterType = jsonObject.optInt("type_disaster");
		
		mTitle = jsonObject.optString("title");
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


	public int getDisasterType() {
		return mDisasterType;
	}
	
	
	public String getTitle() {
		return mTitle;
	}


	public String getContent() {
		return mContent;
	}
	
}
