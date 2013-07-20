package org.dlug.disastercenter.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.constSet.ConstSet.DisasterCode;
import org.dlug.disastercenter.data.DisasterInfoData;

public class DisasterInfoDisplayUtils {
	public static int getDisasterIconResource(DisasterInfoData data) {
		return R.drawable.info_img_document;
	}
	
	public static String getDisplayTimestamp(DisasterInfoData data) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy MM dd HH:mm");
		return dateFormat.format(new Date(data.getTimestamp()));
	}
	
	
	public static String getDisplayDisasterType(DisasterInfoData data) {
		String displayText = "";
		switch ( data.getDisasterType() ) {
		case DisasterCode.HEAVY_RAIN:
			displayText = "폭우";
			break;
			
		case DisasterCode.OVER_FLOOD:
			displayText = "홍수";
			break;
			
		case DisasterCode.SURGE:
			displayText = "해일";
			break;
			
		case DisasterCode.LANDSLIDE:
			displayText = "산사태";
			break;
			
		case DisasterCode.HEAVY_SNOW:
			displayText = "폭우";
			break;
			
		case DisasterCode.AVALANCHE:
			displayText = "눈사태";
			break;
			
		case DisasterCode.BITTER_COLD:
			displayText = "혹한";
			break;
			
		case DisasterCode.HEAT_WAVE:
			displayText = "폭염";
			break;
			
		case DisasterCode.BUILD_DESTROY:
			displayText = "건물붕괴";
			break;
			
		case DisasterCode.BRIDGE_DESTROY:
			displayText = "교량붕괴";
			break;
		}
		return displayText;
	}
}
