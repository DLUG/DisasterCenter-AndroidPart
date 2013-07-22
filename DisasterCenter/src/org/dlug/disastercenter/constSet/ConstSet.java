package org.dlug.disastercenter.constSet;

public class ConstSet {

	public static final class CommonSet {
		public static final String GCM_PROJECT_ID = "876702081363";
	}
	
	public static final class URLSet {
		public static final String HOST = "http://disastercenter.dlug.org/api/";
		public static final String URL_REG_APP = HOST + "reg_app";
		public static final String URL_PUT_LOCATION = HOST + "put_location";
		public static final String URL_REPORT_DISASTER = HOST + "report_disaster";
		public static final String URL_DISASTER_REPORT_LIST = HOST + "disaster_report";
		public static final String URL_DISASTER_NEWS = HOST + "disaster_news";
		public static final String URL_DISASTER_INFO = HOST + "disaster_info";
	}
	
	
	/** 응답 상태 코드 */
	public static final class StatusCode {
		/** 성공 */
		public static final int SUCCESS = 0;
		
		/** GCM값 불량 */
		public static final int INVALID_REG_ID = 100;

		/** Session 불량 */
		public static final int INVALID_SESSION = 101;
		
		/** 기타 인증 불가 */
		public static final int AUTH_FAIL = 199;
		
		/** 데이터 없음 */
		public static final int NO_DATA = 200;

		/** lat 불량 */
		public static final int INVALID_LAT = 300;

		/** lng 불량 */
		public static final int INVALID_LNG = 301;	

		/** 기타 입력 데이터 불량 */
		public static final int WRONG_PARAM = 399;

		/** 기타 동작 불량 */
		public static final int ERROR = 999;
	}
	

	/** 재난 유형 코드 */
	public static class DisasterCode {
		/** 폭우 */
		public static final int HEAVY_RAIN = 101;
		
		/** 홍수 */
		public static final int OVER_FLOOD = 102;
		
		/** 해일 */
		public static final int SURGE = 103;
		
		/** 산사태 */
		public static final int LANDSLIDE = 104;
		
		/** 폭설 */
		public static final int HEAVY_SNOW = 201;

		/** 눈사태 */
		public static final int AVALANCHE = 202;
		
		/** 혹한 */
		public static final int BITTER_COLD = 203;
		
		/** 폭염 */
		public static final int HEAT_WAVE = 301;
		
		/** 건물붕괴 */
		public static final int BUILD_DESTROY = 401;
		
		/** 교량붕괴 */
		public static final int BRIDGE_DESTROY = 402;
	}
	
	/** 재난 신고 유형 코드 */
	public static class DisasterReportType {
		/** 공공데이터 API */
		public static final int PUBLIC = 0;
		
		/** 사용자 신고 */
		public static final int USER = 1;		
	}
	
	public static class AlarmRange {
		public static final int RANGE_10 = 10;
		public static final int RANGE_100 = 100;
	}
	
	
	public static class AlarmCondition {
		public static final int A = 0x01;
		public static final int B = 0x01;
		public static final int C = 0x01;
	}
	


	public static class PreferenceKey {
		public static final String SECRET_CODE = "SECRET_CODE";
		public static final String MESSAGE_RECEIVE = "MESSAGE_RECEIVE";
		public static final String ALARM_SOUND = "ALARM_SOUND";
		public static final String ALRAM_VIBE = "ALRAM_VIBE";
		public static final String ALARM_CONDITION = "ALARM_CONDITION";
		public static final String ALARM_RANGE = "ALARM_RANGE";

	}
	
	public static class InfoListType {
		public static final int INFO = 0x01;
		public static final int NEWS = 0x02;
		
	}
}

