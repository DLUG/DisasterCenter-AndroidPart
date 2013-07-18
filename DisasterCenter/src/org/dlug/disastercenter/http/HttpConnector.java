package org.dlug.disastercenter.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.dlug.disastercenter.utils.Trace;

import android.util.Log;

public class HttpConnector {
	public static enum Method {
		POST,
		GET
	}
	
	private static final String CONTENT_TYPE = "application/x-www-form-urlencoded; charset=UTF-8";
	private static final int TIME_LIMIT = 15000;
	
	public static final int RESULT_COMPLETE = 0x01;
	public static final int RESULT_FAIL 	= 0x02;
	public static final int RESULT_CANCEL 	= 0x03;
	
	private int mRequestTag;								// Request Tag
	
	private ArrayList<NameValuePair> mParams;				// Request Params
	private HashMap<String, String>	 mFileParams;
	private HashMap<String, String>  mHeaderParams;
	
	private String 					 mUrl;					// Request URL
	private Object					 mObject;
	private HttpConnectorDelegate	 mDelegate;
	private Thread					 mCurrentThread;
	
	private boolean mRequestFlag;

	private Method mMethod;		// Default is Get
	
	// 결과에 대한 값 반환.
	private int mResult;
	private int mStatusCode;
	private Exception mException;
	
	public HttpConnector(HttpConnector connector) {
		mResult = -1;
		mStatusCode = -1;

		mParams = new ArrayList<NameValuePair>();	
		mFileParams = new HashMap<String, String>();
		mHeaderParams = new HashMap<String, String>();
		
		mParams.addAll(connector.mParams);
		mFileParams.putAll(connector.mFileParams);
		mHeaderParams.putAll(connector.mHeaderParams);
		
		mRequestTag = connector.mRequestTag;
		mUrl = connector.mUrl;
		mMethod = connector.mMethod;
		
		mDelegate = connector.mDelegate;
		mObject = connector.mObject;
	}
	
	public HttpConnector(String url, int requestTag) {
		mRequestTag = requestTag;
		mUrl = url;
		mMethod = Method.POST;
		mResult = -1;
		mStatusCode = -1;

		mParams = new ArrayList<NameValuePair>();
		mFileParams = new HashMap<String, String>();
		mHeaderParams = new HashMap<String, String>();
	}
	
	// TODO PUBLIC
	public void sendRequest() {
		synchronized ( this ) {
			if ( mRequestFlag ) {
				throw new IllegalStateException("already started request");
			} 
			
			mRequestFlag = true;
			
			mCurrentThread = new Thread(new HttpRunnable());
			mCurrentThread.start();
		}
	}
	
	public String sendRequestSync() throws InterruptedException, Exception {
		synchronized ( this ) {
			if ( mRequestFlag ) {
				throw new IllegalStateException("already started request");
			}

			mRequestFlag = true;
			mCurrentThread = Thread.currentThread();
			return httpRequest();
		}
	}
	
	public void cancel() {
		synchronized ( this ) {
			if ( mCurrentThread != null ) {
				mCurrentThread.interrupt();
				mCurrentThread = null;
			}
		}
	}
	
	public void release() {
		cancel();
		
		if ( mParams != null ) {
			mParams.clear();
			mParams = null;
		}
		
		if ( mHeaderParams != null ) {
			mHeaderParams.clear();
			mHeaderParams = null;
		}
		
		mUrl = null;
		
		mObject = null;
		
		mDelegate = null;
		
		mCurrentThread = null;

		mMethod = null;
	}
	
	public void addParam(String key, String value) {
		synchronized ( this ) {
			if ( mRequestFlag ) {
				throw new IllegalStateException("already started request");
			} 
			mParams.add(new BasicNameValuePair(key, value));
		}
	}
	
	public void addFileParam(String key, String filePath) {
		synchronized ( this ) {
			if ( mRequestFlag ) {
				throw new IllegalStateException("already started request");
			} 
			mFileParams.put(key, filePath);
		}
	}
	
	public void addHeaderParam(String key, String value) {
		synchronized ( this ) {
			if ( mRequestFlag ) {
				throw new IllegalStateException("already started request");
			}
			mHeaderParams.put(key, value);
		}
	}
	
	/* * * * * * * * * *
	 * Setter & Getter *
	 * * * * * * * * * */
	public void setDelegate(HttpConnectorDelegate delegate) {
		synchronized ( this ) {
			if ( mRequestFlag ) {
				throw new IllegalStateException("already started request");
			} 
			mDelegate = delegate;
		}
	}
	
	public HttpConnectorDelegate getDelegate() {
		return mDelegate;
	}
	
	public void setMethod(Method method) {
		synchronized ( this ) {
			if ( mRequestFlag ) {
				throw new IllegalStateException("already started request");
			} 
			mMethod = method;
		}
	}
	
	public void setObject(Object object) {
		synchronized ( this ) {
			if ( mRequestFlag ) {
				throw new IllegalStateException("already started request");
			}
			mObject = object;
		}
	}
	
	public Method getMethod() {
		return mMethod;
	}
	
	public Object getObject() {
		return mObject;
	}
	
	public String getRequestURL() {
		return mUrl;
	}
	
	public int getRequestTag() {
		return mRequestTag;
	}
	
	public int getResult() {
		return mResult;
	}
	
	public int getStatusCode() {
		return mStatusCode;
	}
	
	public Exception getException() {
		return mException;
	}
	
	// TODO PRIVATE
	public String getFullURL() {
		StringBuilder urlButilder = new StringBuilder(1024);
		urlButilder.append(mUrl);
		urlButilder.append('?');
		for ( NameValuePair paramPair : mParams ) {
			urlButilder.append(paramPair.getName());
			urlButilder.append('=');
			urlButilder.append(paramPair.getValue());
			urlButilder.append('&');
		}
		urlButilder.deleteCharAt(urlButilder.length() - 1);
		
		return urlButilder.toString();
	}
	
	private void checkInterrupt() throws InterruptedException {
		if ( Thread.currentThread().isInterrupted() ) {
			throw new InterruptedException("Request is Canceled");
		}
	}
	
	private void onCompleteRequest(HttpConnector connector, String response) {
		mResult = RESULT_COMPLETE;
		if ( mDelegate != null ) {
			mDelegate.onCompleteRequest(this, response);
		}
	}
	
	private void onFailedRequest(HttpConnector connector, Exception e) {
		mResult = RESULT_FAIL;
		if ( mDelegate != null ) {
			mDelegate.onFailedRequest(this, e);
		}
	}
	
	private void onCanceledRequest(HttpConnector connector) {
		mResult = RESULT_CANCEL;
		if ( mDelegate != null ) {
			mDelegate.onCanceledRequest(this);
		}
	}
	
	//
	private class HttpRunnable implements Runnable {
		public void run() {
			try {
				httpRequest();
			} catch (InterruptedException e) {
				onCanceledRequest(HttpConnector.this);
			} catch (Exception e) {
				onFailedRequest(HttpConnector.this, e);
			}
		}
	};
	
	private String httpRequest() throws InterruptedException, Exception {
		HttpParams httpPrams = new BasicHttpParams();		
		HttpConnectionParams.setConnectionTimeout(httpPrams, TIME_LIMIT); 
		HttpConnectionParams.setSoTimeout(httpPrams, TIME_LIMIT);

		DefaultHttpClient httpClient = new DefaultHttpClient(httpPrams);
		
		InputStream is = null;
		BufferedInputStream bis = null;
		try {
			HttpUriRequest httpUriRequest = null;
			Trace.Error("FullURL: " + getFullURL());
			switch ( mMethod ) {
			case GET:
				httpUriRequest = new HttpGet(getFullURL());
				break;
				
			case POST:
				HttpPost httpPost = new HttpPost(mUrl);
				setPostParams(httpPost);
				httpUriRequest = httpPost;    
				break;
			}
			
//			httpUriRequest.setHeader("Content-Type", CONTENT_TYPE);
			httpUriRequest.setHeader("User-Agent", "ANDROID");
			
			Set<String> keySet = mHeaderParams.keySet();
			for ( String key : keySet ) {
				httpUriRequest.setHeader(key, mHeaderParams.get(key));
			}
			
			checkInterrupt();
			
		    HttpResponse httpResponse = httpClient.execute(httpUriRequest);
		    mStatusCode = httpResponse.getStatusLine().getStatusCode();
		    
		    checkInterrupt();
		    
		    if ( mStatusCode == HttpStatus.SC_OK ) {
		    	HttpEntity httpEntity = httpResponse.getEntity();
				BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(httpEntity);
				String response = EntityUtils.toString(bufHttpEntity, HTTP.UTF_8);
				
				Log.d("LOG", response);
				
				onCompleteRequest(this, response);
				
				return response;
				
		    }
		    else {
		    	throw new Exception("Http Failed / Status: " + mStatusCode);
		    }
		} catch (InterruptedException e) {
			mException = e;
			Log.d("LOG", e.toString());
			throw e;
			
		} catch (Exception e) {
			mException = e;
			Log.d("LOG", e.toString());

			throw e;
		} finally {
			if ( bis != null ) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			
			if ( is != null ) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			
			if ( httpClient != null )
				httpClient.getConnectionManager().shutdown();
		}
	}
	
	
	private void setPostParams(HttpPost httpPost) {
		try {
			if ( mFileParams.size() > 0 ) {
				MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, null);
				for ( NameValuePair paramPair : mParams ) {
					entity.addPart(paramPair.getName(), new StringBody(URLEncoder.encode(paramPair.getValue(), HTTP.UTF_8)));
				}
				

				Set<String> fileKeySet = mFileParams.keySet();
				for ( String key : fileKeySet ) {
					entity.addPart(key, new FileBody(new File(mFileParams.get(key))));
				}
				
				httpPost.setEntity(entity);
			}
			else {
			    httpPost.setEntity(new UrlEncodedFormEntity(mParams, HTTP.UTF_8));
			}
		} catch (UnsupportedEncodingException e) {
			
		}
	}
	
	
	
	public static interface HttpConnectorDelegate {
		public void onCompleteRequest(HttpConnector connector, String response);
		public void onFailedRequest(HttpConnector connector, Exception e);
		public void onCanceledRequest(HttpConnector connector);
	}
}
