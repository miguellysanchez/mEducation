package com.voyager.meducation.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;
import com.voyager.meducation.fragments.DashboardFragment;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.dropbox.client2.session.TokenPair;

public class LoginActivity extends Activity {

	private static final String TAG = LoginActivity.class.getSimpleName();
	final static private String APP_KEY = MEducationApplication.APP_KEY;
	final static private String APP_SECRET = MEducationApplication.APP_SECRET;
	final static private AccessType ACCESS_TYPE = MEducationApplication.ACCESS_TYPE;
	private DropboxAPI<AndroidAuthSession> mDBApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, ">>>ONCREATE");

		// Build the android auth session, then use that to get the dropboxApi
		mDBApi = ((MEducationApplication) getApplication()).getDBApi();

		// If application is not yet linked to Dropbox then try to login
		if (!mDBApi.getSession().authenticationSuccessful()) {
			Log.d(TAG, ">>>STARTING AUTHENTICATION");

			mDBApi.getSession().startAuthentication(LoginActivity.this);
		}

		setContentView(R.layout.login_activity);
		if (((MEducationApplication) getApplication()).isLoggedIn()) {
			Intent dashboardIntent = new Intent(LoginActivity.this,
					MainPageActivity.class);
			startActivity(dashboardIntent);
		}
		findViewById(R.id.btnDemoHelp).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(
								LoginActivity.this);
						builder.setTitle("Demo Login Instructions");
						builder.setMessage("To login as a Teacher:\n   Input \"Teacher\" in USERNAME field\n\nTo login as a Proctor:\n   Input \"Proctor\" in USERNAME field\n\nOtherwise, USERNAME is registered as student account\n\n\nLastly, PASSWORD field should match USERNAME field\nto be able to successfully login");
						builder.show();
					}
				});

		findViewById(R.id.btnLogin).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// if (mDBApi.getSession().authenticationSuccessful()) {
				EditText editLoginUsername = (EditText) findViewById(R.id.editLoginUsername);
				EditText editLoginPassword = (EditText) findViewById(R.id.editLoginPassword);
				boolean isMatch = editLoginUsername.getText().toString()
						.equals(editLoginPassword.getText().toString())
						&& !editLoginUsername.getText().toString().isEmpty();
				if (isMatch) {
					if (editLoginUsername.getText().toString()
							.contains("Teacher")) {
						Toast.makeText(getApplicationContext(),
								"LOGGED IN AS A TEACHER", Toast.LENGTH_LONG)
								.show();
						((MEducationApplication) getApplication())
						.setAccountType(MEducationApplication.TEACHER);
					} else if (editLoginUsername.getText().toString()
							.contains("Proctor")
							&& isMatch) {
						Toast.makeText(getApplicationContext(),
								"LOGGED IN AS A PROCTOR", Toast.LENGTH_LONG)
								.show();
						((MEducationApplication) getApplication())
						.setAccountType(MEducationApplication.PROCTOR);
					} else if (isMatch) {
						Toast.makeText(getApplicationContext(),
								"LOGGED IN AS A STUDENT", Toast.LENGTH_LONG)
								.show();
						((MEducationApplication) getApplication())
						.setAccountType(MEducationApplication.STUDENT);
					}
					((MEducationApplication)getApplication()).setUsername(editLoginUsername.getText().toString());
					Log.d(TAG, ">>>LOGGING");
					//START MAINPAGEACTIVITY
					Intent dashboardIntent = new Intent(LoginActivity.this,
							MainPageActivity.class);
					((MEducationApplication) getApplication()).setIsLoggedIn(true); // if
					startActivity(dashboardIntent);
					overridePendingTransition(R.anim.right_slide_in,
							R.anim.left_slide_out);
				} else {
					Toast.makeText(getApplicationContext(), "INVALID LOGIN",
							Toast.LENGTH_LONG).show();
				}
				
				// }
				// else{
				// Toast.makeText(LoginActivity.this,
				// "Cannot login without link to Dropbox",
				// Toast.LENGTH_SHORT).show();
				// mDBApi.getSession().startAuthentication(LoginActivity.this);
				// }
			}
		});

		findViewById(R.id.textTeacherRegister).setOnClickListener(
				new OnClickListener() {
					public void onClick(View v) {
						Log.d(TAG, ">>>Teacher Registration");
						Intent registrationIntent = new Intent(
								LoginActivity.this, RegistrationActivity.class);
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
								LoginActivity.this, RegistrationActivity.class);
						registrationIntent.putExtra(
								MEducationApplication.ACCOUNT_TYPE,
								MEducationApplication.PROCTOR);
						startActivity(registrationIntent);
						overridePendingTransition(R.anim.right_slide_in,
								R.anim.left_slide_out);
					}
				});
	}

	@Override
	protected void onResume() {
		Log.d(TAG, ">>>ONRESUME");
		super.onResume();
		if (mDBApi.getSession().authenticationSuccessful()) {

			try {
				// Required to complete auth, sets the access token on the
				// session
				mDBApi.getSession().finishAuthentication();
				AccessTokenPair tokens = mDBApi.getSession()
						.getAccessTokenPair();
				Log.d(TAG, ">>>TOKEN: key: " + tokens.key + " | secret: "
						+ tokens.secret);
				((MEducationApplication) getApplication()).setKeys(tokens.key,
						tokens.secret);
			} catch (IllegalStateException e) {
				Log.i("DbAuthLog", "Error authenticating", e);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}