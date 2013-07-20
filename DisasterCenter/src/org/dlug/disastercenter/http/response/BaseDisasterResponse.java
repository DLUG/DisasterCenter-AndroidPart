package org.dlug.disastercenter.http.response;

import org.dlug.disastercenter.constSet.ConstSet.StatusCode;
import org.json.JSONObject;

public class BaseDisasterResponse extends BaseResponse {
	private int mStatus;
	private String mMessage;
	

	public BaseDisasterResponse(JSONObject jsonObject) {
		super(jsonObject);
		mStatus = jsonObject.optInt("status", StatusCode.ERROR);
		mMessage = jsonObject.optString("msg", "");
	}
	
	public boolean isSuccess() {
		return mStatus == StatusCode.SUCCESS;
	}
	
	public int getStatus() {
		return mStatus;
	} 
	
	public String getMessage() {
		return mMessage;
	} 

}
