package org.dlug.disastercenter.fragment;

import org.dlug.disastercenter.constSet.ConstSet.InfoListType;

public class DisasterInfoListFragment extends DisasterBaseInfoListFragment {


	@Override
	protected int getInfoListType() {
		return InfoListType.INFO;
	}

	@Override
	protected String getTitle() {
		return "재난정보";
	}

}
