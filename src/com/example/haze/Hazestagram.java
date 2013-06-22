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
import android.util.Log;
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
		
		Globals appState = ((Globals) getApplicationContext());
		Cache cache = appState.getCache();
		
		urlString = Instagram.APIURL + "tags/" + "hazesg"
				+ "/media/recent?access_token=" + cache.access_token;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		new HazestagramGenerator(this, url, urlString).execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hazestagram, menu);
		return true;
	}

}
