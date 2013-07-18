package org.dlug.disastercenter.http.api;

import java.util.HashMap;

import org.dlug.disastercenter.constSet.ConstSet.URLSet;
import org.dlug.disastercenter.http.response.Response;
import org.dlug.disastercenter.utils.Trace;
import org.json.JSONException;
import org.json.JSONObject;

public class DisasterApi extends BaseApi {


	@Override
	protected Object convertResponse(int requestTag, String responseText) {
		try {
			JSONObject response = new JSONObject(responseText);
			Trace.Error(response.toString());
			return response;
			
		} catch (JSONException e) {
			Trace.Error(e.toString());
		}
		
		return responseText; 
	}
	
	public void disasterNewsAsync(int requestTag, long page, ApiDelegate<Response> delegate) {
		HashMap<String, String> params = new HashMap<String, String>(1);
		params.put("page", String.valueOf(page));
		
		startRequestAsync(requestTag, URLSet.URL_DISASTER_NEWS, params, null, Response.class, delegate);
	}


}
