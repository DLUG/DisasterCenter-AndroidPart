package org.dlug.disastercenter.http.api;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.dlug.disastercenter.http.HttpConnector;
import org.dlug.disastercenter.http.HttpConnector.Method;
import org.dlug.disastercenter.http.response.BaseResponse;
import org.dlug.disastercenter.utils.Trace;

import android.util.SparseArray;

// 여기서 세션 등을 체크한다.
public abstract class BaseApi {
	private SparseArray<Thread> mHttpThreadMap;
	
	protected BaseApi() {
		mHttpThreadMap = new SparseArray<Thread>();
	}

	protected BaseResponse startRequestSync(final int requestTag,
			String url,
			HashMap<String, String> params,
			HashMap<String, String> headerParams, 
			Class<? extends BaseResponse> responseClass)
			throws InterruptedException, Exception {

		BaseResponse response = null;
		try {
			HttpConnector connector = generateConnector(requestTag, url, params, headerParams);
			String responseText = connector.sendRequestSync();
			Object responseObject = convertResponse(requestTag, responseText); 
			
			response = generateResponse(responseClass, responseObject);
			onResponse(requestTag, url, response);
		} catch (InterruptedException e) {
			onCancel(requestTag, url);
			throw e;
		} catch (Exception e) {
			onError(requestTag, url);
			throw e;
		}
		
		return response;
	}

	protected void startRequestAsync(final int requestTag, 
			final String url,
			final HashMap<String, String> params,
			final HashMap<String, String> headerParams, 
			final Class<? extends BaseResponse> responseClass,
			final ApiDelegate delegate) {
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					HttpConnector connector = generateConnector(requestTag, url, params, headerParams);
					String responseText = connector.sendRequestSync();
					
					if ( delegate != null ) {
						Object responseObject = convertResponse(requestTag, responseText);
						BaseResponse response = generateResponse(responseClass, responseObject);
						
						onResponse(requestTag, url, response);
						delegate.onResponse(requestTag, response);
					}
				} catch (InterruptedException e) {
					Trace.Error("Cancel: " + params.toString());
					if ( delegate != null ) {
						delegate.onCancel(requestTag);
						onCancel(requestTag, url);
					}
				} catch (Exception e) {
					if ( delegate != null ) {
						delegate.onError(requestTag, e);
						onError(requestTag, url);
					}
				}
			}
		});
		cancelRequest(requestTag);
		mHttpThreadMap.put(requestTag, thread);
		thread.start();
	}
	
	

	public void cancelRequest(int requestTag) {
		Thread httpThread = mHttpThreadMap.get(requestTag);
		if ( httpThread != null ) {
			httpThread.interrupt();
			mHttpThreadMap.remove(requestTag);
		}
	}
	
	
	private BaseResponse generateResponse(Class responseClass, Object response) throws Exception {
		Constructor<BaseResponse> responseContructor = responseClass.getDeclaredConstructor(response.getClass());
		return responseContructor.newInstance(response);
	}
	
	private HttpConnector generateConnector(int requestTag, String url, HashMap<String, String> params, HashMap<String, String> headerParams) {
		HttpConnector connector = new HttpConnector(url, requestTag);
		connector.setMethod(Method.POST);
		
		if ( params != null ) {
			Set<Entry<String, String>> paramSet = params.entrySet();
			for ( Entry<String, String> paramEntry : paramSet ) {
				connector.addParam(paramEntry.getKey(), paramEntry.getValue());
			}
		}
		
		if ( headerParams != null ) {
			Set<Entry<String, String>> headerParamSet = headerParams.entrySet();
			for ( Entry<String, String> paramEntry : headerParamSet ) {
				connector.addHeaderParam(paramEntry.getKey(), paramEntry.getValue());
			}
		}
		
		onGenerateConnector(connector);
		
		return connector;
	}
	
	
	protected void onGenerateConnector(HttpConnector connector) {
		
	}
	
	protected void onResponse(int requestTag, String url, BaseResponse response) {
		
	}
	
	protected void onCancel(int requestTag, String url) {
		
	}
	
	protected void onError(int requestTag, String url) {
		
	}

	protected abstract Object convertResponse(int requestTag, String responseText);
	
	
	
	public static interface ApiDelegate<Response extends BaseResponse> {
		public void onResponse(int requestTag, Response response);
		public void onError(int requestTag, Exception e);
		public void onCancel(int requestTag);
		
	}
}
