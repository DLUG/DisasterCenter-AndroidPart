package org.dlug.disastercenter.receiver;

import org.dlug.disastercenter.service.DisasterService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		context.startService(new Intent(context, DisasterService.class));
	}

}
