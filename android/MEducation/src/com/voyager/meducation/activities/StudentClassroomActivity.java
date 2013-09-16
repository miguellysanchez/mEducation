package com.voyager.meducation.activities;

import com.voyager.meducation.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class StudentClassroomActivity extends Activity {

	ActionBar actionBar; 
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		setContentView(R.layout.student_classroom_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_menu_student_classroom, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			overridePendingTransition(R.anim.right_slide_out, R.anim.left_slide_in);
			break;
		case R.id.action_search:
			Toast.makeText(this, "Menu Item 1 selected", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.action_camera_test:
			Intent examPhotoIntent = new Intent(StudentClassroomActivity.this, TakeExamPhotoActivity.class);
			startActivity(examPhotoIntent);
			break;

		default:
			break;
		}

		return true;
	}

}
