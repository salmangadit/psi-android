package com.example.haze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Hazestagram extends Activity {
	private SessionStore mSessionStore;
	String urlString = null;
	URL url = null;
	String TAG = "Haze.java";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hazestagram_main);

		mSessionStore = new SessionStore(this.getApplicationContext());
		String access_token = mSessionStore.getInstaAccessToken();

		urlString = Instagram.APIURL + "tags/" + "hazesg"
				+ "/media/recent?access_token=" + access_token;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		InputStream inputStream;
		try {
			inputStream = url.openConnection().getInputStream();
			String response = streamToString(inputStream);
			
			JSONObject jsonObject;
			try {
				jsonObject = (JSONObject) new JSONTokener(response).nextValue();
				JSONArray jsonArray = jsonObject.getJSONArray("data");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String streamToString(InputStream is) throws IOException {
		String string = "";

		if (is != null) {
			StringBuilder stringBuilder = new StringBuilder();
			String line;
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));

				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line);
				}

				reader.close();
			} finally {
				is.close();
			}

			string = stringBuilder.toString();
		}

		return string;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hazestagram, menu);
		return true;
	}

}
