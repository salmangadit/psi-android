package com.example.haze;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationsServiceReceiver extends BroadcastReceiver {

	private static final String TAG = "Haze.java";

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent dailyUpdater = new Intent(context, PSIService.class);
		context.startService(dailyUpdater);
		Log.d(TAG,
				"Called context.startService from AlarmReceiver.onReceive");
	}

}
