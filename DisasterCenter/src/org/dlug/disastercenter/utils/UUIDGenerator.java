package org.dlug.disastercenter.utils;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class UUIDGenerator {
	public static String getUUID(Context context) {
		SharedPreferences sp = context.getSharedPreferences("UUID", Context.MODE_PRIVATE);
		String uuid = sp.getString("UUID", null);
		
		if ( uuid == null || uuid.trim().length() == 0 ) {
			Editor editor = sp.edit();
			uuid = UUID.randomUUID().toString();
			editor.putString("UUID", uuid);
			editor.commit();
		}
		
		return uuid;
	}
	
}
