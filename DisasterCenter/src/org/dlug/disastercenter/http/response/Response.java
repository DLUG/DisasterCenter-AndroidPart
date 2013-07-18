package org.dlug.disastercenter.http.response;

import org.json.JSONObject;

public class Response extends BaseResponse {
	private String mStatus;
	private String mMessage;
	

	public Response(JSONObject jsonObject) {
		super(jsonObject);
		mStatus = jsonObject.optString("status", "");
		mMessage = jsonObject.optString("msg", "");
	}
	
	public boolean isSuccess() {
		return true;
	}
	
	public String getStatus() {
		return mStatus;
	} 
	
	public String getMessage() {
		return mMessage;
	} 

}
