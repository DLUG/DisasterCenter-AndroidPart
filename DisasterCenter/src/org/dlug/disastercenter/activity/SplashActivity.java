package org.dlug.disastercenter.activity;

import org.dlug.disastercenter.R;
import org.dlug.disastercenter.constSet.ConstSet.CommonSet;
import org.dlug.disastercenter.http.api.BaseApi.ApiDelegate;
import org.dlug.disastercenter.http.api.DisasterApi;
import org.dlug.disastercenter.http.response.RegAppResponse;
import org.dlug.disastercenter.preference.DisasterPreference;
import org.dlug.disastercenter.utils.StringUtils;
import org.dlug.disastercenter.utils.Trace;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class SplashActivity extends Activity {
	private final static int REQUEST_REG_APP = 0x01;
	
	private final static int REQUEST_CODE_GOOGLE_SERVICE = 0x01;
	
	private final static long SPLASH_DURATION = 2000L;
	private DisasterApi mDisasterApi;
	private long mStartTime;

	private View mProgressBar;
	private Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		mProgressBar = findViewById(R.id.activity_splash_ProgressBar);
		mStartTime = System.currentTimeMillis();

		mDisasterApi = new DisasterApi(this);

		registerReceiver(mRegistionReceiver, new IntentFilter("com.google.android.c2dm.intent.REGISTRATION"));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initApplication();
	}
	
	@Override
	protected void onDestroy() {
		mDisasterApi.cancelRequest(REQUEST_REG_APP);
		unregisterReceiver(mRegistionReceiver);
		super.onDestroy();
	}
    
	private void initApplication() {

		// Google Service 유효성 검사를 한다.
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
		if (resultCode == ConnectionResult.SUCCESS) {
			
			// 클라이언트에 저장된 SecretCode없을 경우에만 디바이스를 등록한다.
			// 디바이스 등록시 마다 발급받는 SecretCode달라지니 주의해야한다.
			
			mProgressBar.setVisibility(View.VISIBLE);
			
			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			final String regId = GCMRegistrar.getRegistrationId(this);
			
			if (!StringUtils.isEmpty(regId)) {
				String secretCode = DisasterPreference.getSecretCode(this);
				mDisasterApi.regAppAsync(REQUEST_REG_APP, regId, secretCode, mRegAppDelegate);
			}
			else {
				GCMRegistrar.register(this, CommonSet.GCM_PROJECT_ID);
			}
		}
		else {
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this, REQUEST_CODE_GOOGLE_SERVICE);
			dialog.setOnCancelListener(new OnCancelListener() {	
				@Override
				public void onCancel(DialogInterface dialog) {
					finish();
				}
			});
			dialog.show();
		}
		
	}
 
	private void startNextActivity() {
		long diff = System.currentTimeMillis() - mStartTime;
		long duration = Math.max(0, SPLASH_DURATION - diff);
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				startActivity(new Intent(SplashActivity.this, MainActivity.class));
				finish();
			}
		}, duration);
	}
	
	private ApiDelegate<RegAppResponse> mRegAppDelegate = new ApiDelegate<RegAppResponse>() {
		
		@Override
		public void onResponse(int requestTag, final RegAppResponse response) {
			if ( response.isSuccess() ) {
				runOnUiThread(new Runnable() {
					public void run() {
						DisasterPreference.setSecretCode(SplashActivity.this, response.getSecretCode());
						mProgressBar.setVisibility(View.GONE);	
					}
				});
				startNextActivity();
			}
			else {
				onError(requestTag, new Exception(response.getMessage()));
			}
		}
		
		@Override
		public void onError(int requestTag, final Exception e) {
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
					finish();
				}
			});
		}
		
		@Override
		public void onCancel(int requestTag) {
			
		}
	};
	
	private BroadcastReceiver mRegistionReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle extras = intent.getExtras();
			String registationId = extras.getString("registration_id");
			if ( StringUtils.isEmpty(registationId) ) {
				Trace.Print("GDM Regist fail");
				finish();
			}
			else {
				String secretCode = DisasterPreference.getSecretCode(getApplicationContext());

				Trace.Error("registationId: " + registationId);
				mDisasterApi.regAppAsync(REQUEST_REG_APP, registationId, secretCode, mRegAppDelegate);
			}
		}
	};
}
