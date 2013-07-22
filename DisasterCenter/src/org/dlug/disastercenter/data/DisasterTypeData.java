package org.dlug.disastercenter.data;

import java.io.Serializable;

import org.dlug.disastercenter.utils.DisasterDisplayUtils;

public class DisasterTypeData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3227711663789723725L;
	public static final String INTENT_FILTER = DisasterTypeData.class.getName();
	
	private final String mDisplayName;
	private final int mType;

	public DisasterTypeData(int type) {
		mType = type;
		mDisplayName = DisasterDisplayUtils.getDisplayDisasterType(type);
	}

	public String getDisplayName() {
		return mDisplayName;
	}

	public int getType() {
		return mType;
	}


	
}
