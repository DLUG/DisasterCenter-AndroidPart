package org.dlug.disastercenter.fragment;

import org.dlug.disastercenter.constSet.ConstSet.InfoListType;

public class DisasterNewsListFragment extends DisasterBaseInfoListFragment {

	@Override
	protected int getInfoListType() {
		return InfoListType.NEWS;
	}

	@Override
	protected String getTitle() {
		return "재난뉴스";
	}

}
