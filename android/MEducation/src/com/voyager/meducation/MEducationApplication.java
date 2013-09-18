package com.voyager.meducation;


import android.app.Application;
import android.content.SharedPreferences;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session.AccessType;
import com.voyager.meducation.activities.LoginActivity;

public class MEducationApplication extends Application {
	
	private static final String TAG = MEducationApplication.class.getSimpleName();
	
	public static final String TEACHER = "teacher";
	public static final String PROCTOR = "proctor";
	public static final String ACCOUNT_TYPE = "account_type";
    public static final String ACCESS_KEY_NAME = "ACCESS_KEY";
    public static final String ACCESS_SECRET_NAME = "ACCESS_SECRET";
	
	//DROPBOX STUFF
	final static public String APP_KEY = "n706k8ax7vymwzp";
	final static public String APP_SECRET = "czjpvb5x6zclr9r";
	final static public AccessType ACCESS_TYPE = AccessType.DROPBOX;

	// In the class declaration section:


	private static final String IS_LOGGED_IN = "is_logged_in";
	SharedPreferences prefs;
	
	public void onCreate(){
		super.onCreate();
		prefs = getSharedPreferences(MEducationApplication.class.getSimpleName(),MODE_PRIVATE);
	}

	public boolean isLoggedIn(){
		return prefs.getBoolean(IS_LOGGED_IN,false);
	}
	
	public void setIsLoggedIn(boolean bool){
		prefs.edit().putBoolean(IS_LOGGED_IN, bool).commit();
		
	}
	
	public String getAccountType(){
		return prefs.getString(ACCOUNT_TYPE, null);
	}
	
	public void setAccountType(String acctType){
		prefs.edit().putString(ACCOUNT_TYPE, acctType).commit();
	}
	
	public String[] getKeys() {
        String key = prefs.getString(ACCESS_KEY_NAME, null);
        String secret = prefs.getString(ACCESS_SECRET_NAME, null);
        if (key != null && secret != null) {
        	String[] ret = new String[2];
        	ret[0] = key;
        	ret[1] = secret;
        	return ret;
        } else {
        	return null;
        }
	}

	public void setKeys(String key, String secret){
		prefs.edit().putString(ACCESS_KEY_NAME, key);
        prefs.edit().putString(ACCESS_SECRET_NAME, secret);
	}
	
	public SharedPreferences getPrefs(){
		if (prefs == null)
			prefs = getSharedPreferences(
					MEducationApplication.class.getSimpleName(), MODE_PRIVATE);
		return prefs;
	}
	
	
	
}