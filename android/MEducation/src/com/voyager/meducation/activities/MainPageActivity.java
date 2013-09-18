package com.voyager.meducation.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.Session;
import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;
import com.voyager.meducation.fragments.ClassroomsFragment;
import com.voyager.meducation.fragments.DashboardFragment;
import com.voyager.meducation.fragments.StudentsFragment;
import com.voyager.meducation.fragments.SubjectsFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class MainPageActivity extends Activity implements TabListener{

	public static final String TAG = MainPageActivity.class.getSimpleName();
	
	ActionBar actionBar;
	DashboardFragment mDashboardFragment;
	SubjectsFragment mSubjectsFragment;
	ClassroomsFragment mClassroomsFragment;
	StudentsFragment mStudentsFragment;
	DropboxAPI<AndroidAuthSession> dbApi;
	int currentTab = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		dbApi = ((MEducationApplication)getApplication()).getDropboxApi();
		
		if(dbApi==null){
			Log.d(TAG, ">>>FUUUU");
		}
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		setContentView(R.layout.main_page_activity);

		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		mDashboardFragment = new DashboardFragment();
		mSubjectsFragment = new SubjectsFragment();
		mClassroomsFragment = new ClassroomsFragment();
		mStudentsFragment = new StudentsFragment();
		
		fragmentTransaction.add(R.id.fragment_container, mDashboardFragment, DashboardFragment.TAG);
		
		actionBar.addTab(actionBar.newTab().setText("TASKS")
				.setTabListener(this));

		actionBar.addTab(actionBar.newTab().setText("SUBJECTS")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("CLASSROOMS")
				.setTabListener(this));
		actionBar.addTab(actionBar.newTab().setText("STUDENTS")
				.setTabListener(this));
	}
	
	///ACTIONS INVOKED BY DASHBOARDFRAGMENT
	public void logout(){
		startActivity(new Intent(MainPageActivity.this, LoginActivity.class));
		overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
		finish();
	}

	public void goToSubjects(){
		Log.d(TAG, ">>>TO SUBJ");
		
		FragmentTransaction fTrans = getFragmentManager().beginTransaction();
		fTrans.setCustomAnimations(R.anim.frag_left_slide_in, R.anim.frag_right_slide_out);
		fTrans.replace(R.id.fragment_container, mSubjectsFragment)
				.commit();
		currentTab = 1;
		getActionBar().setSelectedNavigationItem(currentTab);
	}

	// /ACTIONS INVOKED BY SUBJECTSFRAGMENT
	public void goToClassrooms() {
		Log.d(TAG, ">>>TO CLASSROOM");
		
		FragmentTransaction fTrans = getFragmentManager().beginTransaction();
		fTrans.setCustomAnimations(R.anim.frag_left_slide_in, R.anim.frag_right_slide_out);
		fTrans.replace(R.id.fragment_container, mClassroomsFragment)
				.commit();
		currentTab = 2;
		getActionBar().setSelectedNavigationItem(currentTab);

	}
	
	///ACTIONS INVOKED BY CLASSROOMSFRAGMENT
	public void goToStudents(){
		Log.d(TAG, ">>>TO STUD");
		FragmentTransaction fTrans = getFragmentManager().beginTransaction();
		fTrans.setCustomAnimations(R.anim.frag_left_slide_in, R.anim.frag_right_slide_out);
		fTrans.replace(R.id.fragment_container, mStudentsFragment)
				.commit();
		currentTab = 3;
		getActionBar().setSelectedNavigationItem(currentTab);

	}
	///ACTIONS INVOKED BY STUDENTSFRAGMENT
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		Log.d(TAG, ">>>TAB: "+tab.getPosition()+"Current: "+currentTab);
		//if selected tab index is greater than the current tab index, then retain current tab position
		if(tab.getPosition()>currentTab){
			Log.d(TAG, ">>RETAIN");
			getActionBar().selectTab(getActionBar().getTabAt(currentTab));
		}
		else if (tab.getPosition() == 0) {
			FragmentTransaction fTrans = getFragmentManager().beginTransaction();
			fTrans.setCustomAnimations(R.anim.frag_left_slide_in, R.anim.frag_right_slide_out);
			fTrans.replace(R.id.fragment_container, mDashboardFragment)
					.commit();
			currentTab = 0;
			getActionBar().setSelectedNavigationItem(currentTab);

		} 
		else if (tab.getPosition() == 1) {
			FragmentTransaction fTrans = getFragmentManager().beginTransaction();
			fTrans.setCustomAnimations(R.anim.frag_left_slide_in, R.anim.frag_right_slide_out);
			fTrans.replace(R.id.fragment_container, mSubjectsFragment)
					.commit();
			currentTab = 1;
		} 
		else if (tab.getPosition() == 2 ) {
			FragmentTransaction fTrans = getFragmentManager().beginTransaction();
			fTrans.setCustomAnimations(R.anim.frag_left_slide_in, R.anim.frag_right_slide_out);
			fTrans.replace(R.id.fragment_container, mClassroomsFragment)
					.commit();
			currentTab = 2;
		} 
		else if (tab.getPosition() == 3) {
			FragmentTransaction fTrans = getFragmentManager().beginTransaction();
			fTrans.setCustomAnimations(R.anim.frag_left_slide_in, R.anim.frag_right_slide_out);
			fTrans.replace(R.id.fragment_container, mStudentsFragment)
					.commit();
			currentTab = 3;
		} 
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
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
			break;
		case R.id.action_search:
			Toast.makeText(getApplicationContext(), "TRYING TO UPLOAD", Toast.LENGTH_LONG).show();
			File file = new File("working-draft.txt");
			FileInputStream inputStream;
			try {
				inputStream = new FileInputStream(file);
				try {
					Entry response = dbApi.putFile("/magnum-opus.txt", inputStream,file.length(), null, null);
				} catch (DropboxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			 
			break;
		case R.id.action_camera_test:
			Intent examPhotoIntent = new Intent(MainPageActivity.this,
					TakeExamPhotoActivity.class);
			startActivity(examPhotoIntent);
			break;

		default:
			break;
		}

		return true;
	}

}
