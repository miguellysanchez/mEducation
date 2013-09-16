package com.voyager.meducation.activities;

import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioGroup;

public class RegistrationActivity extends Activity{
	
	public static final String TAG = RegistrationActivity.class.getSimpleName();
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//HIDE THE ACTION BAR
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		///////////////////////
		setContentView(R.layout.registration_activity);
        ((TextView)findViewById(R.id.textTitle)).setText("Registration as "+getIntent().getStringExtra(MEducationApplication.ACCOUNT_TYPE));
        ((DatePicker)findViewById(R.id.datePickerBirthdate)).updateDate(1990, 1, 1);
        ((ImageView)findViewById(R.id.back)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
				finish();
				overridePendingTransition  (R.anim.left_slide_in, R.anim.right_slide_out);
			}
		});
        ((RadioGroup)findViewById(R.id.radioGender)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId==0){
					group.clearCheck();
					group.check(checkedId);
				}
				
				
			}
		});
        if(getIntent().getStringExtra(MEducationApplication.ACCOUNT_TYPE).equals(MEducationApplication.TEACHER)){
        	//Teacher stuff
        }
        else if(getIntent().getStringExtra(MEducationApplication.ACCOUNT_TYPE).equals(MEducationApplication.PROCTOR)){
        	//Proctor stuff	
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onBackPressed(){
    	InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
		finish();
		overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
    }
}
