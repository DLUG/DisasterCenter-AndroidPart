package org.dlug.disastercenter.notification;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationCenter {
	private static NotificationCenter DefaultCenter;
	private static final HashMap<String, NotificationCenter> NotificationCenterHash = new HashMap<String, NotificationCenter>(10);
	
	private ArrayList<NotificationHandlerInfo> mNotificationHandlerInfos;
	
	private NotificationCenter() {	}
	
	private void init() {
		mNotificationHandlerInfos = new ArrayList<NotificationCenter.NotificationHandlerInfo>();
	}
	
	public static synchronized NotificationCenter getDefaultCenter() {
		if ( DefaultCenter == null ) {
			DefaultCenter = new NotificationCenter();
			DefaultCenter.init();
		}
		return DefaultCenter;
	}
	
	public static synchronized NotificationCenter getCenter(String name) {
		NotificationCenter center = NotificationCenterHash.get(name);
		if ( center == null ) {
			center = new NotificationCenter();
			center.init();
			NotificationCenterHash.put(name, center);
		}
		
		return center;
	}
	
	public void postNotification(String name, Object data) {
		
		for ( NotificationHandlerInfo info : mNotificationHandlerInfos ) {
			if ( name != null && name.equals(info.mName) && info.mNotificationHandler != null )
				info.mNotificationHandler.notification(new NotificationData(name, data));
		}
	}
	
	public void addObserver(String name, NotificationHandler handler) {
		NotificationHandlerInfo info = new NotificationHandlerInfo(name, handler);
		if ( !mNotificationHandlerInfos.contains(info))
			mNotificationHandlerInfos.add(info);
	}
	
	public void removeObserver(String name, NotificationHandler handler) {
		NotificationHandlerInfo info = new NotificationHandlerInfo(name, handler);
		mNotificationHandlerInfos.remove(info);
	}
	

	
	public void removeObserver(String name) {
		NotificationHandlerInfo[] infos = mNotificationHandlerInfos.toArray(new NotificationHandlerInfo[0]);
		for ( NotificationHandlerInfo info : infos ) {
			if ( name != null && name.equals(info.mName) ) {
				mNotificationHandlerInfos.remove(info);
			}			
		}		
	}
	
	
	public void removeObserver(NotificationHandler handler) {
		NotificationHandlerInfo[] infos = mNotificationHandlerInfos.toArray(new NotificationHandlerInfo[0]);
		
		for ( NotificationHandlerInfo info : infos ) {
			
			if ( handler != null && handler.equals(info.mNotificationHandler) ) {
				mNotificationHandlerInfos.remove(info);
			}			
		}		
	}
	
	public interface NotificationHandler {
		public void notification(NotificationData notificationData);
	}
	
	
	
	private class NotificationHandlerInfo {
		private String mName;
		private NotificationHandler mNotificationHandler;
		
		
		public NotificationHandlerInfo(String name, NotificationHandler handler) {
			mName = name;
			mNotificationHandler = handler;
		}
		
		@Override
		public boolean equals(Object o) {
			// TODO Auto-generated method stub
			if ( o instanceof NotificationHandlerInfo ) {
				NotificationHandlerInfo info = (NotificationHandlerInfo)o;
				return (this.mName == info.mName) && (this.mNotificationHandler == info.mNotificationHandler);
			}
			return false;
		}

		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			String hashCodeStr = "";
			
			if ( this.mName != null )
				hashCodeStr = hashCodeStr + this.mName;
			
			if ( this.mNotificationHandler != null )
				hashCodeStr = hashCodeStr + this.mNotificationHandler.toString();
			
			return hashCodeStr.hashCode();
		}
	}
}
