package com.example.haze;

import java.util.Calendar;
import java.util.TimeZone;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends Activity {
	private static final String TAG = "Haze.java";
	private Cache cache = null;
	public TextView hazard, value, time;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Globals appState = ((Globals) getApplicationContext());
		cache = appState.getCache();

		hazard = (TextView) this.findViewById(R.id.txthazard);
		value = (TextView) this.findViewById(R.id.txtPSI);
		time = (TextView) this.findViewById(R.id.txtTime);

		new ConstantsGenerator(cache, this).execute();
		
		setRecurringAlarm(this);

		value.setText(String.valueOf(cache.latest_value));
		time.setText(cache.latest_time);

		if (cache.latest_value >= 0 && cache.latest_value <= 50) {
			hazard.setText("Good");
		} else if (cache.latest_value >= 51 && cache.latest_value <= 100) {
			hazard.setText("Moderate");
		} else if (cache.latest_value >= 101 && cache.latest_value <= 200) {
			hazard.setText("Unhealthy");
		} else if (cache.latest_value >= 201 && cache.latest_value <= 300) {
			hazard.setText("Very unhealthy");
		} else {
			hazard.setText("Hazardous");
		}

	}

	private void setRecurringAlarm(Context context) {
		Calendar updateTime = Calendar.getInstance();
		updateTime.setTimeZone(TimeZone.getDefault());
		updateTime.set(Calendar.HOUR_OF_DAY, 12);
		updateTime.set(Calendar.MINUTE, 30);
		Intent downloader = new Intent(context,
				NotificationsServiceReceiver.class);
		downloader.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,
				downloader, PendingIntent.FLAG_CANCEL_CURRENT);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
				updateTime.getTimeInMillis(),
				AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);
		Log.d(TAG, "Set alarmManager.setRepeating to: "
				+ updateTime.getTime().toLocaleString());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_settings:
			openSettings();
			return true;
		case R.id.action_hazestagram:
			openHazestagram();
			return true;
		case R.id.action_graph:
			openGraph();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void openHazestagram() {
		Intent intent = new Intent(getApplicationContext(),
				HazestagramLoginActivity.class);
		startActivity(intent);
	}

	void openSettings() {
		// Intent intent = new Intent(getApplicationContext(), Settings.class);
		// startActivity(intent);
	}

	void openGraph() {
		Intent intent = new Intent(getApplicationContext(), GraphActivity.class);
		startActivity(intent);
	}

}
