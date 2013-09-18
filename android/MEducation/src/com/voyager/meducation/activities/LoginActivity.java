package com.voyager.meducation.activities;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;

import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;
import com.voyager.meducation.fragments.DashboardFragment;

import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private static final String TAG = LoginActivity.class.getSimpleName();
	DropboxAPI<AndroidAuthSession> mDropboxApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// We create a new AuthSession so that we can use the Dropbox API.
		AndroidAuthSession session = buildSession();
		mDropboxApi = new DropboxAPI<AndroidAuthSession>(session);
		checkAppKeySetup();
		
		
		// HIDE THE ACTION BAR
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		// /////////////////////

		if (((MEducationApplication) getApplication()).isLoggedIn()) {
			Intent dashboardIntent = new Intent(LoginActivity.this,
					MainPageActivity.class);
			LoginActivity.this.finish();

			startActivity(dashboardIntent);
		} else {
			mDropboxApi.getSession().startAuthentication(LoginActivity.this);

			setContentView(R.layout.login_activity);

			findViewById(R.id.btnLogin).setOnClickListener(
					new OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent dashboardIntent = new Intent(
									LoginActivity.this, MainPageActivity.class);
							LoginActivity.this.finish();
							((MEducationApplication) getApplication())
									.setIsLoggedIn(true);
							// if (System.currentTimeMillis() % 2 == 0) {
							((MEducationApplication) getApplication())
									.setAccountType(MEducationApplication.TEACHER);
							// } else {
							// ((MEducationApplication) getApplication())
							// .setAccountType(MEducationApplication.PROCTOR);
							// }

							startActivity(dashboardIntent);
							overridePendingTransition(R.anim.right_slide_in,
									R.anim.left_slide_out);
						}
					});

			findViewById(R.id.textTeacherRegister).setOnClickListener(
					new OnClickListener() {
						public void onClick(View v) {
							Log.d(TAG, ">>>Teacher Registration");
							Intent registrationIntent = new Intent(
									LoginActivity.this,
									RegistrationActivity.class);
							registrationIntent.putExtra(
									MEducationApplication.ACCOUNT_TYPE,
									MEducationApplication.TEACHER);
							startActivity(registrationIntent);
							overridePendingTransition(R.anim.right_slide_in,
									R.anim.left_slide_out);
						}
					});

			findViewById(R.id.textProctorRegister).setOnClickListener(
					new OnClickListener() {
						public void onClick(View v) {
							Log.d(TAG, ">>>Proctor Registration");
							Intent registrationIntent = new Intent(
									LoginActivity.this,
									RegistrationActivity.class);
							registrationIntent.putExtra(
									MEducationApplication.ACCOUNT_TYPE,
									MEducationApplication.PROCTOR);
							startActivity(registrationIntent);
							overridePendingTransition(R.anim.right_slide_in,
									R.anim.left_slide_out);
						}
					});
		}
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private AndroidAuthSession buildSession() {
		AppKeyPair appKeyPair = new AppKeyPair(MEducationApplication.APP_KEY,
				MEducationApplication.APP_SECRET);
		AndroidAuthSession session;

		String[] stored = ((MEducationApplication) getApplication()).getKeys();
		if (stored != null) {
			AccessTokenPair accessToken = new AccessTokenPair(stored[0],
					stored[1]);
			session = new AndroidAuthSession(appKeyPair,
					MEducationApplication.ACCESS_TYPE, accessToken);
		} else {
			session = new AndroidAuthSession(appKeyPair,
					MEducationApplication.ACCESS_TYPE);
		}

		return session;
	}
	
	

	private void checkAppKeySetup() {
		// Check to make sure that we have a valid app key
		if (MEducationApplication.APP_KEY.startsWith("CHANGE")
				|| MEducationApplication.APP_SECRET.startsWith("CHANGE")) {
			Toast.makeText(
					this,
					"You must apply for an app key and secret from developers.dropbox.com, and add them to the DBRoulette ap before trying it.",
					Toast.LENGTH_LONG).show();
			finish();
			return;
		}

		// Check if the app has set up its manifest properly.
		Intent testIntent = new Intent(Intent.ACTION_VIEW);
		String scheme = "db-" + MEducationApplication.APP_KEY;
		String uri = scheme + "://" + AuthActivity.AUTH_VERSION + "/test";
		testIntent.setData(Uri.parse(uri));
		PackageManager pm = getPackageManager();
		if (0 == pm.queryIntentActivities(testIntent, 0).size()) {
			Toast.makeText(
					this,
					"URL scheme in your app's "
							+ "manifest is not set up correctly. You should have a "
							+ "com.dropbox.client2.android.AuthActivity with the "
							+ "scheme: " + scheme, Toast.LENGTH_LONG).show();
			finish();
		}
	}
	
//	@Override
//	protected void onResume() {
//	    super.onResume();
//
//	    if (mDropboxApi.getSession().authenticationSuccessful()) {
//	        try {
//	            // Required to complete auth, sets the access token on the session
//	            mDropboxApi.getSession().finishAuthentication();
//	            AccessTokenPair tokens = mDropboxApi.getSession().getAccessTokenPair();
//	        } catch (IllegalStateException e) {
//	            Log.i("DbAuthLog", "Error authenticating", e);
//	        }
//	    }
//	}
}