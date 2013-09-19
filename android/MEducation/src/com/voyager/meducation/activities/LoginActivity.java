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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.Window;
import android.widget.Toast;
import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.android.AuthActivity;
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
		
		//Build the android auth session, then use that to get the dropboxApi
		mDBApi = ((MEducationApplication)getApplication()).getDBApi();
		
		//If application is not yet linked to Dropbox then try to login
		if(!mDBApi.getSession().isLinked()){
			mDBApi.getSession().startAuthentication(LoginActivity.this);
		}
		
		setContentView(R.layout.login_activity);
	    if (((MEducationApplication) getApplication()).isLoggedIn()) {
			Intent dashboardIntent = new Intent(LoginActivity.this,
					MainPageActivity.class);
			LoginActivity.this.finish();

			startActivity(dashboardIntent);
		} 
		else {

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
	protected void onResume() {
	    super.onResume();
	    if (mDBApi.getSession().authenticationSuccessful()) {
	        try {
	            // Required to complete auth, sets the access token on the session
	            mDBApi.getSession().finishAuthentication();
	            AccessTokenPair tokens = mDBApi.getSession().getAccessTokenPair();
	            ((MEducationApplication)getApplication()).setKeys(tokens.key, tokens.secret);
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