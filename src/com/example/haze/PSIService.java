package com.example.haze;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

public class PSIService extends IntentService {
	private static final String TAG = "Haze.java";

	public PSIService() {
		super("HazeService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//Log.d(TAG, "About to execute PSI value poll");
		new PSIPoller(this).execute();
	}

	private class PSIPoller extends AsyncTask<Void, Void, String> {
		String endpoint = "http://hidden-ocean-3278.herokuapp.com";
		JSONObject jObj = null;
		PSIService service = null;

		public PSIPoller(PSIService serv) {
			service = serv;
		}

		@Override
		protected String doInBackground(Void... arg0) {
			HttpClient httpClient = new DefaultHttpClient();
			// HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(endpoint + "/latest");
			String text = null;
			try {
				HttpResponse response = httpClient.execute(httpGet);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
			} catch (Exception e) {
				return e.getLocalizedMessage();
			}
			return text;
		}

		@Override
		protected void onPostExecute(String results) {
			//Log.v(TAG, "Response string: " + results);
			if (results != null) {
				try {
					jObj = new JSONObject(results);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					//Log.e("JSON Parser", "Error parsing data " + e.toString());
					e.printStackTrace();

				}

				try {
					int value = 0;
					if (jObj.getString("reading") != "-") {
						 value = Integer.parseInt(jObj.getString("reading"));
						 } else if (value >= 101 && value <= 200) {
						 service.sendNotification(
						 service.getApplicationContext(),
						 "Haze is unhealthy. PSI value = " + value);
						 } else if (value >= 201 && value <= 300) {
						 service.sendNotification(
						 service.getApplicationContext(),
						 "Haze is very unhealthy. PSI value = " + value);
						 } else {
						 service.sendNotification(
						 service.getApplicationContext(),
						 "Haze is hazardous. PSI value = " + value);
					}

//					service.sendNotification(service.getApplicationContext(),
//							"Haze is hazardous. PSI value = " + value);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		protected String getASCIIContentFromEntity(HttpEntity entity)
				throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n > 0) {
				byte[] b = new byte[4096];
				n = in.read(b);
				if (n > 0)
					out.append(new String(b, 0, n));
			}
			return out.toString();
		}
	}

	private void sendNotification(Context context, String warning) {
		Intent notificationIntent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		NotificationManager notificationMgr = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(
				R.drawable.ic_launcher, "Haze",
				System.currentTimeMillis());
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, "Haze", warning,
				contentIntent);
		notificationMgr.notify(0, notification);

	}
}
