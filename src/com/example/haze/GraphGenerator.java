package com.example.haze;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class GraphGenerator extends AsyncTask<Void, Void, String> {
	private Cache cache = null;
	private static String TAG = "Haze.java";
	static JSONArray jObj = null;
	ProgressDialog progressDialog;
	GraphActivity main = null;
	String endpoint;

	public GraphGenerator(Cache con, GraphActivity main) {
		cache = con;
		this.main = main;
		endpoint = "http://hidden-ocean-3278.herokuapp.com";
	}

	@Override
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(main,
				"Generating latest PSI graph...", "", true);
	};

	@Override
	protected String doInBackground(Void... arg0) {
		HttpClient httpClient = new DefaultHttpClient();
		// HttpContext localContext = new BasicHttpContext();
		HttpGet httpGet = new HttpGet(endpoint + "/list");
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
		Log.v(TAG, "Response string: " + results);
		if (results != null) {
			try {
				jObj = new JSONArray(results);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.e("JSON Parser", "Error parsing data " + e.toString());
				e.printStackTrace();

			}

			try {
				for (int i = 0; i < jObj.length(); i++) {
					PSIValue val = new PSIValue();
					val.time = jObj.getJSONObject(i).getString("time");
					if (jObj.getJSONObject(i).getString("reading") != "-") {
						val.value = Integer.parseInt(jObj.getJSONObject(i)
								.getString("reading"));
					} else {
						val.value = -1;
					}

					cache.last_ten_readings.add(val);
				}

				main.drawGraph(cache.last_ten_readings);

				Globals appState = ((Globals) main.getApplicationContext());
				appState.setCache(cache);

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		progressDialog.dismiss();

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
