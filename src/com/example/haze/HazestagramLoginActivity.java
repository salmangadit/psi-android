package com.example.haze;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class HazestagramLoginActivity extends Activity implements OnClickListener {
	private Button mButton;
	private InstaAuth mInstaImpl;
	private Context mContext;
	private ResponseListener mResponseListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_hazestagram);
		// Show the Up button in the action bar.
		setupActionBar();

		mButton = (Button) findViewById(R.id.btnLogin);
		mButton.setOnClickListener(this);
		mResponseListener = new ResponseListener();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.hazestagram, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View arg0) {
		mInstaImpl = new InstaAuth(this);
		mInstaImpl.setAuthAuthenticationListener(new AuthListener());
	}

	public class AuthListener implements
			InstaAuth.AuthAuthenticationListener {
		@Override
		public void onSuccess() {
			Toast.makeText(HazestagramLoginActivity.this,
					"Instagram Authorization Successful", Toast.LENGTH_SHORT)
					.show();
			Intent intent = new Intent(getApplicationContext(), Hazestagram.class);
			startActivity(intent);
		}

		@Override
		public void onFail(String error) {
			Toast.makeText(HazestagramLoginActivity.this, "Authorization Failed",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.example.haze.responselistener");
		filter.addCategory("com.example.haze");
		registerReceiver(mResponseListener, filter);
	}

	@Override
	protected void onPause() {
		unregisterReceiver(mResponseListener);
		super.onPause();
	}

	public class ResponseListener extends BroadcastReceiver {

		public static final String ACTION_RESPONSE = "com.example.haze.responselistener";
		public static final String EXTRA_NAME = "90293d69-2eae-4ccd-b36c-a8d0c4c1bec6";
		public static final String EXTRA_ACCESS_TOKEN = "bed6838a-65b0-44c9-ab91-ea404aa9eefc";

		@Override
		public void onReceive(Context context, Intent intent) {
			mInstaImpl.dismissDialog();
			Bundle extras = intent.getExtras();
			String name = extras.getString(EXTRA_NAME);
			String accessToken = extras.getString(EXTRA_ACCESS_TOKEN);
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					mContext);
			alertDialog.setTitle("Your Details");
			alertDialog.setMessage("Name - " + name + ", Access Token - "
					+ accessToken);
			alertDialog.setPositiveButton("Ok",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			alertDialog.show();
		}
	}

}
