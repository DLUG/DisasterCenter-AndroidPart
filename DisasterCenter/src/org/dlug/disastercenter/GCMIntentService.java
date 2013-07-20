package org.dlug.disastercenter;

import org.dlug.disastercenter.utils.PushWakeLock;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;



//
public class GCMIntentService extends GCMBaseIntentService {
	public static final int ID_MATCH = 0;
	public static final int ID_MSG = 1;
	public static final int ID_CONDITION = 2;
	public static final int ID_STORY = 3;
	public static final int ID_SCHEDULE = 3;
	
	private static Toast mToast;
	private Handler mHandler = new Handler(Looper.getMainLooper());

	public GCMIntentService() {
		this("597949027343", null);
		
	}

	public GCMIntentService(String... senderIds) {
		super("597949027343", null);
	}

	public static void generateNotification(Context context, int notifyID, String message, Bundle extras, Class<? extends Object> component) {
		
		long when = System.currentTimeMillis();

		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pendingIntent = null;
		
		Intent notificationIntent = new Intent(context, component);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		if ( extras != null )
			notificationIntent.putExtras(extras);

		pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
	
		
		
		// deprecate: new Notification(icon, tickerText, when)
		
		// deprecate: setLatestEventInfo(context, title, message, pedingIntent)
		Notification notification = null;
		
		if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH ) 
			notification = getNotification(context, notifyID, message, extras, component);	
		 
		else 
			notification = getNotificationForNewAPI(context, notifyID, message, extras, component);
		
		notification.contentIntent = pendingIntent;
		notification.flags |= Notification.FLAG_AUTO_CANCEL;		
		notification.tickerText = message;
		notification.when = when;
		
		notification.defaults = Notification.DEFAULT_ALL;

		notificationManager.cancel(notifyID);
		notificationManager.notify(notifyID, notification);
		
	}
	
	@SuppressWarnings("deprecation")
	private static Notification getNotification(Context context, int notifyID, String message, Bundle extras, Class<? extends Object> component) {
//		int icon = R.drawable.app_icon01;
//		
//		Notification notification = new Notification(icon, null, -1);
//		notification.setLatestEventInfo(context, "더블하트", message, null);
		
		
		
 
//		return notification;
		return null;
	}
	
	
	@SuppressLint("NewApi")
	private static Notification getNotificationForNewAPI(Context context, int notifyID, String message, Bundle extras, Class<? extends Object> component) {
//		int largeIcon = R.drawable.app_icon01;
//		int smallIcon = R.drawable.app_icon02;
//		
//		Notification notification = new Notification.Builder(context)
//		.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon))
//		.setSmallIcon(smallIcon)
//		.setContentText(message)
//		.setContentTitle("더블하트")
//		.getNotification();
//		
//		return notification;
		return null;
	}

	

	@Override
	protected void onMessage(Context context, Intent intent) {
		PushWakeLock.acquireCpuWakeLock(context);
		mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
            	PushWakeLock.releaseCpuLock();
            }
        }, 3500);
		
		Log.e("LOG", "PUSH: \n" + intent.getExtras().toString());
		
	}


	

	@Override
	protected void onRegistered(Context context, String reg_id) {
	}

	@Override
	protected void onUnregistered(Context context, String reg_id) {
	}

	@Override
	protected void onError(Context context, String errorId) {
	}
	
	
	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		return super.onRecoverableError(context, errorId);
	}

	
	
	private void showToast(final Context context, final String sender, final String msg) {
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
//				View toastView = View.inflate(context, R.layout.toast_chatting, null);
//				
//				((TextView)toastView.findViewById(R.id.toast_push_TextView_sender)).setText(sender);
//				((TextView)toastView.findViewById(R.id.toast_push_TextView_msg)).setText(msg);
//				
//				if ( mToast != null )
//					mToast.cancel();
//				
//				
//				mToast = new Toast(context);
//				mToast.setView(toastView);
//				mToast.setGravity(Gravity.TOP, 0, DimensionUtils.convertToPix(context, 150));
//				mToast.setDuration(Toast.LENGTH_SHORT);
//				mToast.show();
//				
//				PushWakeLock.acquireCpuWakeLock(context);
//
//				
//				
//				mHandler.postDelayed(new Runnable() {
//		            @Override
//		            public void run() {
//		            	PushWakeLock.releaseCpuLock();
//		            }
//		        }, 3500);
			}
		});
		
	
	}
}
