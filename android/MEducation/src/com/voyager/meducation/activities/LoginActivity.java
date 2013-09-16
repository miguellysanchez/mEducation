package com.voyager.meducation.activities;

import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;

import android.os.Bundle;
import android.os.RemoteException;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.Window;

public class LoginActivity extends Activity{

    private static final String TAG = LoginActivity.class.getSimpleName();


	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//HIDE THE ACTION BAR
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    getActionBar().hide();
	    ///////////////////////
	    
		if(((MEducationApplication)getApplication()).isLoggedIn()){
			Intent dashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
			LoginActivity.this.finish();

			startActivity(dashboardIntent);
		}
		else{
	        setContentView(R.layout.login_activity);
	        
	        
	        findViewById(R.id.btnLogin).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent dashboardIntent = new Intent(LoginActivity.this, DashboardActivity.class);
					LoginActivity.this.finish();
					((MEducationApplication)getApplication()).setIsLoggedIn(true);
					if(System.currentTimeMillis()%2==0){
						((MEducationApplication)getApplication()).setAccountType(MEducationApplication.TEACHER);
					}
					else{
						((MEducationApplication)getApplication()).setAccountType(MEducationApplication.PROCTOR);
					}
					startActivity(dashboardIntent);
					overridePendingTransition(R.anim.right_slide_in,R.anim.left_slide_out);
				}
			});
	        
	        findViewById(R.id.textTeacherRegister).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
				Log.d(TAG, ">>>Teacher Registration");
				Intent registrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
				registrationIntent.putExtra(MEducationApplication.ACCOUNT_TYPE, MEducationApplication.TEACHER);
				startActivity(registrationIntent);
				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
				}
	        });
	        
	        findViewById(R.id.textProctorRegister).setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
				Log.d(TAG, ">>>Proctor Registration");
				Intent registrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
				registrationIntent.putExtra(MEducationApplication.ACCOUNT_TYPE, MEducationApplication.PROCTOR);
				startActivity(registrationIntent);
				overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
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
}