package com.example.haze;

import java.util.ArrayList;
import java.util.List;

import com.loopj.android.image.SmartImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultsAdapter extends BaseAdapter {

	private static List<String> images;
	private static List<String> usernames;
	private LayoutInflater mInflater;

	public ResultsAdapter(Context context, List<String> img, List<String> user) {
		images = img;
		usernames = user;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return images.size();
	}

	@Override
	public Object getItem(int position) {
		return images.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.custom_row_view, null);
			holder = new ViewHolder();
			holder.username = (TextView) convertView.findViewById(R.id.username);
			holder.image = (ImageView) convertView
					.findViewById(R.id.image);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.username.setText(usernames.get(position));
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(images.get(position), holder.image);
		
		return convertView;
	}

	static class ViewHolder {
		TextView username;
		ImageView image;
	}
}
