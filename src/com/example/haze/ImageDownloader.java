package com.example.haze;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class ImageDownloader extends AsyncTask {

	private static final String TAG = "Haze.java";
	private List<String> urls;
	private List<String> ids;

	public ImageDownloader(List<String> links, List<String> id) {
		this.urls = links;
		this.ids = id;
	}

	@Override
	protected Object doInBackground(Object... arg0) {
		Log.v(TAG, "External Storage State: " + Environment.getExternalStorageState());
		
		File directory = new File(Environment.getExternalStorageDirectory(), "/Haze");
		if (directory.exists() == false) {
			directory.mkdir();
		}
		
		for (int i =0; i< urls.size(); i++){
			try{
				File firstFile = new File(directory+"/"+ids.get(i)+".jpeg");
				if (firstFile.exists()==false)
				{
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(urls.get(i));
					HttpResponse resp = httpClient.execute(httpGet);
					
					HttpEntity entity = resp.getEntity();
					InputStream is = entity.getContent();
					Boolean status = firstFile.createNewFile();
					
					FileOutputStream foutS = new FileOutputStream(firstFile);
					byte[] buffer = new byte[1024];
					long total = 0;
					int count;
					while ((count = is.read(buffer))!=-1){
						total += count;	
						foutS.write(buffer, 0 , count);
					}
					foutS.close();
					is.close();
					publishProgress(i);
					
				}
			} catch (MalformedURLException e){
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return null;
	}
	
	@Override
	protected void onProgressUpdate(Object... values){
		super.onProgressUpdate(values);
	}
}
