package org.dlug.disastercenter.utils;

import android.util.Log;


public class Trace {
	private static final String LOGTYPE = "::Disaster Center::";
	
	public static void Print(String msg) {
		Log.i(LOGTYPE, msg);
	}
	
	public static void Error(String msg) {
		Log.e(LOGTYPE, msg);
	}
}
