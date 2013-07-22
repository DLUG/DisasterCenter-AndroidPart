package org.dlug.disastercenter.fragment;

import java.util.Iterator;
import java.util.List;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.preference.DisasterPreference;
import org.dlug.disastercenter.row.PreferenceCheckRow;
import org.dlug.disastercenter.row.PreferenceTextRow;
import org.dlug.disastercenter.service.DisasterService;
import org.dlug.disastercenter.utils.TextViewUtils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class SettingFragment extends BaseFragment {
	private PreferenceCheckRow mMessageReceiveRow;
	private PreferenceCheckRow mSoundRow;
	private PreferenceCheckRow mVibeRow;
	private PreferenceTextRow mRangeRow;
//	private PreferenceTextRow mConditionRow;
	

	@Override
	public View onCreateViewEx(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_disaster_setting, null);
		
		TextView titleTextView = (TextView)view.findViewById(R.id.fragment_disaster_setting_TextView_title);
		TextViewUtils.setBold(titleTextView, true);
		
		mMessageReceiveRow = (PreferenceCheckRow)view.findViewById(R.id.fragment_disaster_setting_PreferenceCheckRow_messageReceive);
		mSoundRow = (PreferenceCheckRow)view.findViewById(R.id.fragment_disaster_setting_PreferenceCheckRow_sound);
		mVibeRow = (PreferenceCheckRow)view.findViewById(R.id.fragment_disaster_setting_PreferenceCheckRow_vibe);
		mRangeRow = (PreferenceTextRow)view.findViewById(R.id.fragment_disaster_setting_PreferenceTextRow_range);
//		mConditionRow = (PreferenceTextRow)view.findViewById(R.id.fragment_disaster_setting_PreferenceTextRow_condition);

		mMessageReceiveRow.setName("메세지 수신");
		mSoundRow.setName("소리알림");
		mVibeRow.setName("진동알림");
		mRangeRow.setName("알림지역범위");
//		mConditionRow.setName("수신알림조건");
		
		
		mMessageReceiveRow.setOnClickListener(mClickListener);
		mSoundRow.setOnClickListener(mClickListener);
		mVibeRow.setOnClickListener(mClickListener);
		mRangeRow.setOnClickListener(mClickListener);
//		mConditionRow.setOnClickListener(mClickListener);
		
		Context context = getActivity();
		
		boolean isRunningService = isRunningService(context, DisasterService.class);
		DisasterPreference.setMessageReceiveEnabled(context, isRunningService);
		
		mMessageReceiveRow.setChecked(isRunningService);
		mSoundRow.setChecked(DisasterPreference.isAlarmSoundEnabled(context));
		mVibeRow.setChecked(DisasterPreference.isAlarmVibeEnabled(context));
	
		mRangeRow.setText(DisasterPreference.getAlarmRange(context) + "");
//		mConditionRow.setText(DisasterPreference.getAlarmCondition(context) + "");
		

		return view;
	}
	
	@Override
	protected String getTitle() {
		return "설정";
	}
	
	private boolean isRunningService(Context context, Class<? extends Service> service) {
		boolean checked = false;

		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> info = am.getRunningServices(Integer.MAX_VALUE);
		
		Iterator<RunningServiceInfo> iterator = info.iterator();
		while ( iterator.hasNext() ) {
			RunningServiceInfo runningTaskInfo = iterator.next();			
			if ( runningTaskInfo.service.getClass().equals(service) ) {
				checked = true;
				break;
			}
		}		
		
		return checked;

	}

	
	private OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Activity activity = getActivity();
			switch ( v.getId() ) {
			case R.id.fragment_disaster_setting_PreferenceCheckRow_messageReceive:
				
				// Toggle
				mMessageReceiveRow.setChecked(!mMessageReceiveRow.isChecked());
				boolean isMessaeEnabled = mMessageReceiveRow.isChecked();
				DisasterPreference.setMessageReceiveEnabled(activity, isMessaeEnabled);
				if ( isMessaeEnabled) {
					activity.startService(new Intent(activity, DisasterService.class));					
				}
				else {
					activity.stopService(new Intent(activity, DisasterService.class));
				}
				
				break;
				
				
			case R.id.fragment_disaster_setting_PreferenceCheckRow_sound:
				mSoundRow.setChecked(!mSoundRow.isChecked());
				DisasterPreference.setAlarmSoundEnabled(activity, mSoundRow.isChecked());

				break;
				

			case R.id.fragment_disaster_setting_PreferenceCheckRow_vibe:
				mVibeRow.setChecked(!mVibeRow.isChecked());
				DisasterPreference.setAlarmVibeEnabled(activity, mVibeRow.isChecked());
				
				break;
				
			case R.id.fragment_disaster_setting_PreferenceTextRow_range:
				break;
				
//			case R.id.fragment_disaster_setting_PreferenceTextRow_condition:
//				break;
			}
		}
	};
}