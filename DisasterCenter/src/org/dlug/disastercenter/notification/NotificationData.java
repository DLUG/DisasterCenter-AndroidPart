package org.dlug.disastercenter.notification;


public class NotificationData {
	private String mNotifyName;
	private Object mData;
	
	
	NotificationData(String name, Object data) {
		mNotifyName = name;
		mData  = data;
	}
	
	public String getNotifyName() {
		
		return mNotifyName;
	}
	
	public Object getData() {
		return mData;
	}
}
