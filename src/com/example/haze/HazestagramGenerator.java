package com.example.haze;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
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
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class HazestagramGenerator extends AsyncTask<Void, Void, String> {
	private static String TAG = "Haze.java";
	static JSONObject jObj = null;
	ProgressDialog progressDialog;
	Hazestagram main = null;
	URL endpoint;
	String end;

	public HazestagramGenerator(Hazestagram main, URL endpoint, String url) {
		this.main = main;
		this.endpoint = endpoint;
		this.end = url;
	}

	@Override
	protected void onPreExecute() {
		progressDialog = ProgressDialog.show(main, "Loading Hazestagram...",
				"", true);
	};

	@Override
	protected String doInBackground(Void... arg0) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(end);
		Log.v(TAG, "Request string: " + end);
		String text = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			text = getASCIIContentFromEntity(entity);
		} catch (Exception e) {
			return e.getLocalizedMessage();
		}

		// InputStream inputStream;
		// try {
		// inputStream = endpoint.openConnection().getInputStream();
		// String response = streamToString(inputStream);
		// Log.v(TAG, response);
		// text = response;
		//
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return text;
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
	protected void onPostExecute(String results) {
		Log.v(TAG, "Response string: " + results);
		if (results != null) {
			JSONArray data = null;
			try {
				jObj = new JSONObject(results);
				data = jObj.getJSONArray("data");
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			List<String> urls = new ArrayList<String>();
			List<String> names = new ArrayList<String>();
			List<String> ids = new ArrayList<String>();
			
			for (int i=0; i<data.length(); i++){
				try {
					String image_url = data.getJSONObject(i).getJSONObject("images").getJSONObject("low_resolution").getString("url");
					String username = data.getJSONObject(i).getJSONObject("user").getString("username");
					String id = data.getJSONObject(i).getString("id");
					image_url=image_url.replace("\\", "");
					Log.v(TAG, image_url+","+username);
					urls.add(image_url);
					names.add(username);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			main.DownloadImages(urls, names, ids);
			
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
