package com.example.haze;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Application;

public class Globals extends Application {
	private Cache cache = new Cache();
	
	@Override
    public void onCreate() {
        super.onCreate();

        // Create global configuration and initialize ImageLoader with this configuration
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
            .build();
        ImageLoader.getInstance().init(config);
    }

	public Cache getCache() {
		return cache;
	}

	public void setCache(Cache c) {
		cache = c;
	}
}
