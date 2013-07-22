package org.dlug.disastercenter.data;

public class DisasterMessageData {
	private int mId;
	private final long mServerId;
	private final int mDisasterType;
	private final String mContent;
	private final long mDate; 
	
	public DisasterMessageData(int id, int serverId, int disasterType, String content, long date) {
		mId = id;
		mServerId = serverId;
		mDisasterType = disasterType;
		mContent = content;
		mDate = date;
	}
	
	public void setId(int id) {
		mId = id;
	}
	
	public int getId() {
		return mId;
	}
	
	public long getServerId() {
		return mServerId;
	}
	
	public String getContent() {
		return mContent;
	}
	
	public int getDisasterType() {
		return mDisasterType;
	}
	
	public long getDate() {
		return mDate;
	}
}
