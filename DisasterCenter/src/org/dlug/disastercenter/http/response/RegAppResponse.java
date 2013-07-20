package org.dlug.disastercenter.http.response;

import org.json.JSONObject;

public class RegAppResponse extends BaseDisasterResponse {
	private String mSecretCode;
	
	public RegAppResponse(JSONObject jsonObject) {
		super(jsonObject);
		mSecretCode = jsonObject.optString("secret_code");
	}
	
	public String getSecretCode() {
		return mSecretCode;
	}

}
