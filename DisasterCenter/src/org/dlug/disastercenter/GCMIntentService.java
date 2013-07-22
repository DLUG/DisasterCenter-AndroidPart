package org.dlug.disastercenter;

import org.dlug.disastercenter.constSet.ConstSet.CommonSet;
import org.dlug.disastercenter.data.DisasterMessageData;
import org.dlug.disastercenter.db.DBAdapter;
import org.dlug.disastercenter.preference.DisasterPreference;
import org.dlug.disastercenter.utils.PushWakeLock;
import org.dlug.disastercenter.utils.Trace;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;



//
public class GCMIntentService extends GCMBaseIntentService {
	public static final int ID_MSG = 1;
	
	private static Toast mToast;
	private Handler mHandler = new Handler(Looper.getMainLooper());

	public GCMIntentService() {
		this(CommonSet.GCM_PROJECT_ID, null);
	}

	public GCMIntentService(String... senderIds) {
		super(CommonSet.GCM_PROJECT_ID, null);
	}

	public static void generateNotification(Context context, int notifyID, String message, Bundle extras, Class<? extends Object> component) {
		
		long when = System.currentTimeMillis();

		
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent pendingIntent = null;
		
		if ( component != null ) {
			Intent notificationIntent = new Intent(context, component);
			notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			if ( extras != null )
				notificationIntent.putExtras(extras);
	
			pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		}
		
		
		// OS 버전에  따른 notification 생성.
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
		
		notification.defaults = Notification.DEFAULT_LIGHTS;
		
		// Setting에 의한 알림 형태 설정.
		if ( DisasterPreference.isAlarmSoundEnabled(context) ) {
			notification.defaults |= Notification.DEFAULT_SOUND;
		}		
		
		if ( DisasterPreference.isAlarmVibeEnabled(context) ) {
			notification.defaults |= Notification.DEFAULT_VIBRATE;
		}		

		notificationManager.cancel(notifyID);
		notificationManager.notify(notifyID, notification);
		
	}
	
	@SuppressWarnings("deprecation")
	private static Notification getNotification(Context context, int notifyID, String message, Bundle extras, Class<? extends Object> component) {
		int icon = R.drawable.ic_launcher;

		Notification notification = new Notification(icon, null, -1);
		notification.setLatestEventInfo(context, context.getString(R.string.app_name), message, null);
 
		return notification;
	}
	
	
	@SuppressLint("NewApi")
	private static Notification getNotificationForNewAPI(Context context, int notifyID, String message, Bundle extras, Class<? extends Object> component) {
		int largeIcon = R.drawable.ic_launcher;
		int smallIcon = R.drawable.ic_launcher;
		
		Notification notification = new Notification.Builder(context)
		.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), largeIcon))
		.setSmallIcon(smallIcon)
		.setContentText(message)
		.setContentTitle(context.getString(R.string.app_name))
		.getNotification();

		return notification;
	}

	

	@Override
	protected void onMessage(Context context, Intent intent) {
		context = context.getApplicationContext();
		
		PushWakeLock.acquireCpuWakeLock(context);
		mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
            	PushWakeLock.releaseCpuLock();
            }
        }, 3500);
		
		Trace.Error("onReceivedPush: \n" + intent.getExtras().toString());

		Bundle extras = intent.getExtras();
		if ( extras != null ) {
			long serverId = Long.valueOf(extras.getString("report_idx"));
			int disasterType = Integer.valueOf(extras.getString("type_disaster"));
			String content = extras.getString("type_disaster_string");
			long date = Long.valueOf(extras.getString("timestamp"));
			

			// TODO 받은 메시지를 DB에 저장해야함.
			DBAdapter adapter = new DBAdapter(context);
			adapter.open();
			
			// TODO serverId 타입 변경?
			adapter.insertDisasterMessageData(new DisasterMessageData(-1, (int)serverId, disasterType, content, date));
			adapter.close();
//			
			// TODO 이 부분에서 토스트를 띄워야함.
			showToast(context, content);
			generateNotification(context, ID_MSG, content, null, null);
		}
		
		
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

	
	private void showToast(final Context context, final String content) {
		
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				// TODO 토스트를 이 부분에서 구현해야만 띄울 수 있다. (Context에 대한 문제일 확률이 있으나 테스트 할 시간 없음)
				

				if ( mToast != null )
					mToast.cancel();

				mToast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
				mToast.setGravity(Gravity.CENTER, 0, 0);
				mToast.show();
			}
		});
		
	
	}
}
