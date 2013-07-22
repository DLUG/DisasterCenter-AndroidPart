package org.dlug.disastercenter.preference;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.dlug.disastercenter.constSet.ConstSet.AlarmCondition;
import org.dlug.disastercenter.constSet.ConstSet.AlarmRange;
import org.dlug.disastercenter.constSet.ConstSet.PreferenceKey;
import org.dlug.disastercenter.utils.Trace;

import android.content.Context;
import android.content.SharedPreferences;

public class DisasterPreference {
	private static final String PREFERENCE_NAME = " DisasterPreference";

	//
	public static void setSecretCode(Context context, String value) {
		setValue(context, PreferenceKey.SECRET_CODE, value);
	}
	
	public static String getSecretCode(Context context) {
		return getPreference(context).getString(PreferenceKey.SECRET_CODE, "");
	}
	
	
	//
	public static void setMessageReceiveEnabled(Context context, boolean value) {
		setValue(context, PreferenceKey.MESSAGE_RECEIVE, value);
	}
	
	public static boolean isMessageReceiveEnabled(Context context) {
		return getPreference(context).getBoolean(PreferenceKey.MESSAGE_RECEIVE, false);
	}
	
	
	// 
	public static void setAlarmSoundEnabled(Context context, boolean value) {
		setValue(context, PreferenceKey.ALARM_SOUND, value);
	}
	
	public static boolean isAlarmSoundEnabled(Context context) {
		return getPreference(context).getBoolean(PreferenceKey.ALARM_SOUND, true);
	}
	
	
	// 
	public static void setAlarmVibeEnabled(Context context, boolean value) {
		setValue(context, PreferenceKey.ALRAM_VIBE, value);
	}
	
	public static boolean isAlarmVibeEnabled(Context context) {
		return getPreference(context).getBoolean(PreferenceKey.ALRAM_VIBE, true);
	}
	
	
	// 
	public static void setAlarmRange(Context context, int value) {
		setValue(context, PreferenceKey.ALARM_RANGE, value);
	}
	
	public static int getAlarmRange(Context context) {
		return getPreference(context).getInt(PreferenceKey.ALARM_RANGE, AlarmRange.RANGE_10);
	}
	
	
	// 
	public static void setAlarmCondition(Context context, int value) {
		setValue(context, PreferenceKey.ALARM_CONDITION, value);
	}
	
	public static int getAlarmCondition(Context context) {
		return getPreference(context).getInt(PreferenceKey.ALARM_CONDITION, AlarmCondition.A);
	}
	

	
	
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////						값 저장을 위한 메소드								////////////////////	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////
	private static synchronized SharedPreferences getPreference(Context context) {
		return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE );
	}
	

	private static synchronized void setValue(Context context, String key, String value) {
		SharedPreferences pref = getPreference(context);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(key, value);
		editor.commit();
	}
	
	private static synchronized void setValue(Context context, String key, boolean value) {
		SharedPreferences pref = getPreference(context);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	private static synchronized void setValue(Context context, String key, int value) {
		SharedPreferences pref = getPreference(context);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putInt(key, value);
		editor.commit();
	}
	
	private static synchronized void setValue(Context context, String key, long value) {
		SharedPreferences pref = getPreference(context);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putLong(key, value);
		editor.commit();		
	}
	
	private static synchronized void setValue(Context context, String key, float value) {
		SharedPreferences pref = getPreference(context);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putFloat(key, value);
		editor.commit();		
	}
	
	private static synchronized void removeValue(Context context, String key) {
		SharedPreferences pref = getPreference(context);
		SharedPreferences.Editor editor = pref.edit();
		
		editor.remove(key);
		editor.commit();
	}
	

	private static synchronized void setObject(Context context, String name, Object object) {
		FileOutputStream fos   = null;
		ObjectOutputStream oos = null;
		clearObject(context, name);
		
		try {
			fos = context.openFileOutput(name, Context.MODE_PRIVATE);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.flush();
		} catch (Exception e) {
			
		} finally {
			if ( fos != null ) {
				try {
					fos.close();
				} catch (IOException e) { }
			}
			
			if ( oos != null ){
				try {
					oos.close();
				} catch (IOException e) { }
			}
		}	
	}
	
	private static synchronized Object getObject(Context context, String name) {
		Object object = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			fis = context.openFileInput(name);
			ois = new ObjectInputStream(fis);
			object = ois.readObject();
			
		} catch (Exception e) {
			Trace.Error(e.toString());
		} finally {
			if ( fis != null ) {
				try {
					fis.close();
				} catch (IOException e) { }
			}
			
			if ( ois != null ){
				try {
					ois.close();
				} catch (IOException e) { }
			}
		}
		return object;
	}
	 
	private static synchronized void clearObject(Context context, String name) {
		context.deleteFile(name);
	}
}
