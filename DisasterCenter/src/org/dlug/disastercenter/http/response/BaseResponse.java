package org.dlug.disastercenter.http.response;

import org.dlug.disastercenter.xml.XMLElement;
import org.json.JSONArray;
import org.json.JSONObject;

public abstract class BaseResponse {

	public BaseResponse(String text) {
		
	}
	
	public BaseResponse(JSONArray jsonArray) {
		
	}
	
	public BaseResponse(JSONObject jsonObject) {
		
	}
	
	public BaseResponse(XMLElement xmlElement) {
		
	}
}
