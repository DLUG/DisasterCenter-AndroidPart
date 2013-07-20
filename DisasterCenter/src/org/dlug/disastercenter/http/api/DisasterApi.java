package org.dlug.disastercenter.http.api;

import java.util.HashMap;

import org.dlug.disastercenter.constSet.ConstSet.URLSet;
import org.dlug.disastercenter.http.response.BaseDisasterResponse;
import org.dlug.disastercenter.http.response.DisasterInfoResponse;
import org.dlug.disastercenter.http.response.DisasterReportListResponse;
import org.dlug.disastercenter.http.response.RegAppResponse;
import org.dlug.disastercenter.preference.DisasterPreference;
import org.dlug.disastercenter.utils.UUIDGenerator;
import org.json.JSONObject;

import android.content.Context;

public class DisasterApi extends BaseApi {
	private Context mContext;
	
	public DisasterApi(Context context) {
		mContext = context;
	}
	
	@Override
	protected Object convertResponse(int requestTag, String responseText) throws Exception {
		return new JSONObject(responseText);
	}
	
	
	//
	public void regAppAsync(int requestTag, String registationId, ApiDelegate<RegAppResponse> delegate) {
		HashMap<String, String> params = new HashMap<String, String>(2);
		params.put("key", UUIDGenerator.getUUID(mContext));
		params.put("reg_id", registationId);
		
		startRequestAsync(requestTag, URLSet.URL_REG_APP, params, null, RegAppResponse.class, delegate);
	}
	
	
	//
	public void putLocationAsync(int requestTag, double latitude, double longitude, long range, ApiDelegate<BaseDisasterResponse> delegate) {
		HashMap<String, String> params = new HashMap<String, String>(5);
		params.put("key", UUIDGenerator.getUUID(mContext));
		params.put("secret_code", DisasterPreference.getSecretCode(mContext));
		params.put("lat", String.valueOf(latitude));
		params.put("lng", String.valueOf(longitude));
		params.put("range", String.valueOf(range));
		
		startRequestAsync(requestTag, URLSet.URL_PUT_LOCATION, params, null, BaseDisasterResponse.class, delegate);
	}
	
	
	//
	public void reportDisasterAsync(int requestTag, double latitude, double longitude, double accuracy, int disasterType, String content, ApiDelegate<BaseDisasterResponse> delegate) {
		HashMap<String, String> params = new HashMap<String, String>(7);
		params.put("key", UUIDGenerator.getUUID(mContext));
		params.put("secret_code", DisasterPreference.getSecretCode(mContext));
		params.put("lat", String.valueOf(latitude));
		params.put("lng", String.valueOf(longitude));
		params.put("accuracy", String.valueOf(accuracy));
		params.put("type_disaster", String.valueOf(disasterType));
		params.put("content", String.valueOf(content));
		
		startRequestAsync(requestTag, URLSet.URL_REPORT_DISASTER, params, null, BaseDisasterResponse.class, delegate);
	}

	
	//
	public void disasterReportListAsync(int requestTag, long offset, ApiDelegate<DisasterReportListResponse> delegate) {
		disasterReportListAsync(requestTag, null, null, null, offset, delegate);
	}
	
	public void disasterReportListAsync(int requestTag, Double latitude, Double longitude, Double range, long offset, ApiDelegate<DisasterReportListResponse> delegate) {
		HashMap<String, String> params = new HashMap<String, String>(4);
		
		if ( latitude != null ) 
			params.put("lat", String.valueOf(latitude));
		
		if ( longitude != null ) 
			params.put("lng", String.valueOf(longitude));
		
		if ( range != null ) 
			params.put("range", String.valueOf(range));
		
		params.put("offset", String.valueOf(offset));
		
		
		startRequestAsync(requestTag, URLSet.URL_DISASTER_REPORT_LIST, params, null, DisasterReportListResponse.class, delegate);
	}
	
	
	//
	public void disasterNewsAsync(int requestTag, long offset, ApiDelegate<DisasterInfoResponse> delegate) {
		HashMap<String, String> params = new HashMap<String, String>(1);
		params.put("offset", String.valueOf(offset));
		
		startRequestAsync(requestTag, URLSet.URL_DISASTER_NEWS, params, null, DisasterInfoResponse.class, delegate);
	}
	
	
	//
	public void disasterInfoAsync(int requestTag, long offset, ApiDelegate<DisasterInfoResponse> delegate) {
		HashMap<String, String> params = new HashMap<String, String>(1);
		params.put("offset", String.valueOf(offset));
		
		startRequestAsync(requestTag, URLSet.URL_DISASTER_INFO, params, null, DisasterInfoResponse.class, delegate);
	}
}
