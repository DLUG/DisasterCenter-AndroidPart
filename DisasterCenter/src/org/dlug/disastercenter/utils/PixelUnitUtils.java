package org.dlug.disastercenter.utils;


import android.content.Context;

public class PixelUnitUtils {	
    public static int convertToPix(Context context, float sizeInDips){
    	float scale = context.getResources().getDisplayMetrics().density; 
    	float size = sizeInDips * scale; 
    	return (int) size;
    }

}