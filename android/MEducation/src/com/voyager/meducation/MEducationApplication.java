package com.voyager.meducation;


import android.app.Application;
import android.content.SharedPreferences;

public class MEducationApplication extends Application {
	
	private static final String TAG = MEducationApplication.class.getSimpleName();
	
	public static final String TEACHER = "teacher";
	public static final String PROCTOR = "proctor";
	public static final String ACCOUNT_TYPE = "account_type";

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
	
	public SharedPreferences getPrefs(){
		if (prefs == null)
			prefs = getSharedPreferences(
					MEducationApplication.class.getSimpleName(), MODE_PRIVATE);
		return prefs;
	}
	
}
