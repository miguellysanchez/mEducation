package com.voyager.meducation.activities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.DropboxAPI.DropboxFileInfo;
import com.dropbox.client2.DropboxAPI.Entry;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.exception.DropboxException;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.Session;
import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;
import com.voyager.meducation.fragments.ClassroomsFragment;
import com.voyager.meducation.fragments.DashboardFragment;
import com.voyager.meducation.fragments.LessonsFragment;
import com.voyager.meducation.fragments.SingleStudentFragment;
import com.voyager.meducation.fragments.StudentClassroomFragment;
import com.voyager.meducation.fragments.SubjectsFragment;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

public class MainPageActivity extends Activity implements TabListener {

	public static final String TAG = MainPageActivity.class.getSimpleName();
	public static final String WELCOME_MSG = "Welcome to M-Education, ";
	
	ActionBar actionBar;
	DashboardFragment mDashboardFragment;
	SubjectsFragment mSubjectsFragment;
	LessonsFragment mLessonsFragment;
	ClassroomsFragment mClassroomsFragment;
	StudentClassroomFragment mStudentsFragment;
	SingleStudentFragment mSingleStudentFragment;
	int currentTab = 0;
	DropboxAPI<AndroidAuthSession> mDBApi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);

		//Create directory
		(new File(Environment.getExternalStorageDirectory(), "MEducation")).mkdirs();
		//
		
		mDBApi = ((MEducationApplication) getApplication()).getDBApi();

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.main_page_activity);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		mDashboardFragment = null;
		mDashboardFragment = new DashboardFragment();

		fragmentTransaction.replace(R.id.fragment_container,
				mDashboardFragment, DashboardFragment.TAG).commit();

		actionBar.addTab(actionBar.newTab().setText("TASKS")
				.setTabListener(this));
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		invalidateOptionsMenu();
		
//		AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//		builder.setTitle("MEducation");
//		builder.setMessage(WELCOME_MSG+((MEducationApplication)getApplication()).getUsername());
//		AlertDialog dialog = builder.create();
//				dialog.show();
	}

	// /ACTIONS INVOKED BY DASHBOARDFRAGMENT
	public void logout() {
		((MEducationApplication) getApplication()).setAccountType(null);
		((MEducationApplication) getApplication()).setUsername(null);
		((MEducationApplication) getApplication()).setIsLoggedIn(false);

		finish();
		overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
	}

	public void goToSubjects() {
		Log.d(TAG, ">>>TO SUBJ");
		mSubjectsFragment = null;
		mSubjectsFragment = new SubjectsFragment();
		actionBar.addTab(actionBar.newTab().setText("SUBJECTS")
				.setTabListener(this));
		FragmentTransaction fTrans = getFragmentManager().beginTransaction();
		fTrans.setCustomAnimations(R.anim.frag_right_slide_in,
				R.anim.frag_left_slide_out);
		fTrans.replace(R.id.fragment_container, mSubjectsFragment,
				SubjectsFragment.TAG).commit();
		currentTab = 1;
		getActionBar().setSelectedNavigationItem(currentTab);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		invalidateOptionsMenu();

	}

	// INVOKED BY SUBJECTSFRAGMENT
	public void goToLessons(String subject) {
		Log.d(TAG, ">>>TO LESSONS");
		mLessonsFragment = null;
		mLessonsFragment = new LessonsFragment(subject);
		actionBar.addTab(actionBar.newTab().setText("LESSONS")
				.setTabListener(this));
		FragmentTransaction fTrans = getFragmentManager().beginTransaction();
		fTrans.setCustomAnimations(R.anim.frag_right_slide_in,
				R.anim.frag_left_slide_out);
		fTrans.replace(R.id.fragment_container, mLessonsFragment,
				LessonsFragment.TAG).commit();
		currentTab = 2;
		getActionBar().setSelectedNavigationItem(currentTab);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		invalidateOptionsMenu();

	}

	// /ACTIONS INVOKED BY LESSONSFRAGMENT
	public void goToClassrooms() {
		Log.d(TAG, ">>>TO CLASSROOM");
		actionBar.addTab(actionBar.newTab().setText("CLASSROOMS")
				.setTabListener(this));
		mClassroomsFragment = null;
		mClassroomsFragment = new ClassroomsFragment();

		FragmentTransaction fTrans = getFragmentManager().beginTransaction();
		fTrans.setCustomAnimations(R.anim.frag_right_slide_in,
				R.anim.frag_left_slide_out);
		fTrans.replace(R.id.fragment_container, mClassroomsFragment,
				ClassroomsFragment.TAG).commit();
		currentTab = 3;
		getActionBar().setSelectedNavigationItem(currentTab);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		invalidateOptionsMenu();

	}

	// /ACTIONS INVOKED BY CLASSROOMSFRAGMENT
	public void goToStudentClassroom() {
		Log.d(TAG, ">>>TO STUDENTS");
		actionBar.addTab(actionBar.newTab().setText("STUDENTS")
				.setTabListener(this));
		mStudentsFragment = null;
		mStudentsFragment = new StudentClassroomFragment();

		FragmentTransaction fTrans = getFragmentManager().beginTransaction();
		fTrans.setCustomAnimations(R.anim.frag_right_slide_in,
				R.anim.frag_left_slide_out);
		fTrans.replace(R.id.fragment_container, mStudentsFragment,
				StudentClassroomFragment.TAG).commit();
		currentTab = 4;
		getActionBar().setSelectedNavigationItem(currentTab);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		invalidateOptionsMenu();

	}

	// /ACTIONS INVOKED BY STUDENTSFRAGMENT
	public void goToSingleStudent(String name) {
		actionBar.addTab(actionBar.newTab().setText(name).setTabListener(this));
		mSingleStudentFragment = null;
		mSingleStudentFragment = new SingleStudentFragment(name);
		FragmentTransaction fTrans = getFragmentManager().beginTransaction();
		fTrans.setCustomAnimations(R.anim.frag_right_slide_in,
				R.anim.frag_left_slide_out);
		fTrans.replace(R.id.fragment_container, mSingleStudentFragment,
				SingleStudentFragment.TAG).commit();
		if (((MEducationApplication) getApplication()).getAccountType().equals(
				MEducationApplication.STUDENT)) {
			currentTab = 3;
		} else {
			currentTab = 5;
		}
		getActionBar().setSelectedNavigationItem(currentTab);
		getActionBar().getSelectedTab().setText(name);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		invalidateOptionsMenu();
	}

	public void goToLessonResources(String subjectName, String lessonName ){
		Intent lessonResourcesIntent = new Intent(MainPageActivity.this, ViewLessonResourcesActivity.class);
		lessonResourcesIntent.putExtra(ViewLessonResourcesActivity.SUBJECT_NAME, subjectName);
		lessonResourcesIntent.putExtra(ViewLessonResourcesActivity.LESSON_NAME, lessonName);
		startActivity(lessonResourcesIntent);
	}
	
	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {

		currentTab = tab.getPosition();
		Log.d(TAG, "Current Tab: " + currentTab + " | count:"
				+ getActionBar().getNavigationItemCount());

		// remove tabs in next categories
		while (getActionBar().getNavigationItemCount() > currentTab + 1) {
			getActionBar().removeTabAt(currentTab + 1);
		}
		FragmentManager fManager = getFragmentManager();
		FragmentTransaction fTrans = fManager.beginTransaction();
		fTrans.setCustomAnimations(R.anim.frag_left_slide_in,
				R.anim.frag_right_slide_out);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			switch (currentTab) {
			case 0:
				fTrans.replace(R.id.fragment_container, mDashboardFragment)
						.commit();

				break;
			case 1:
				fTrans.replace(R.id.fragment_container, mSubjectsFragment)
						.commit();

				break;
			case 2:
				fTrans.replace(R.id.fragment_container, mLessonsFragment)
						.commit();

				break;
			case 3:
				if (((MEducationApplication) getApplication()).getAccountType()
						.equals(MEducationApplication.STUDENT)) {
					fTrans.replace(R.id.fragment_container,
							mSingleStudentFragment).commit();
				} else {
					fTrans.replace(R.id.fragment_container, mClassroomsFragment)
							.commit();

				}
				break;
			case 4:
				fTrans.replace(R.id.fragment_container, mStudentsFragment)
						.commit();
				break;
			}
		} else {
			switch (currentTab) {
			case 0:
				fTrans.add(R.id.fragment_container, mDashboardFragment)
						.commit();

				break;
			case 1:
				fTrans.add(R.id.fragment_container, mSubjectsFragment).commit();

				break;
			case 2:
				fTrans.add(R.id.fragment_container, mLessonsFragment).commit();

				break;
			case 3:
				if (((MEducationApplication) getApplication()).getAccountType()
						.equals(MEducationApplication.STUDENT)) {
					fTrans.add(R.id.fragment_container, mSingleStudentFragment)
							.commit();
				} else {
					fTrans.add(R.id.fragment_container, mClassroomsFragment)
							.commit();

				}
				break;
			case 4:
				fTrans.add(R.id.fragment_container, mStudentsFragment).commit();
				break;
			}
		}
		invalidateOptionsMenu();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		if(((MEducationApplication) getApplication()).getAccountType().equals(MEducationApplication.TEACHER)){
			inflater.inflate(R.menu.action_bar_menu_teacher, menu);
			MenuItem miAddSubject = menu.findItem(R.id.action_add_subject); miAddSubject.setVisible(false);
			MenuItem miAddLesson = menu.findItem(R.id.action_add_lesson); miAddLesson.setVisible(false);
			MenuItem miAddClassroom = menu.findItem(R.id.action_add_classroom); miAddClassroom.setVisible(false);
			MenuItem miAddStudent= menu.findItem(R.id.action_add_student); miAddStudent.setVisible(false);
			MenuItem miViewGrades = menu.findItem(R.id.action_view_grade); miViewGrades.setVisible(false);
			MenuItem miPhotoTest = menu.findItem(R.id.action_camera_test); miPhotoTest.setVisible(false);
			if(currentTab==1){
				miAddSubject.setVisible(true);
			} else if (currentTab == 2) {
				miAddLesson.setVisible(true);
			} else if (currentTab == 3) {
				miAddClassroom.setVisible(true);
			} else if (currentTab == 4) {
				miAddStudent.setVisible(true);
				miPhotoTest.setVisible(true);
			} else if (currentTab == 5) {
				miViewGrades.setVisible(true);
				miPhotoTest.setVisible(true);

			}
		}
		else if(((MEducationApplication) getApplication()).getAccountType().equals(MEducationApplication.PROCTOR)){
			inflater.inflate(R.menu.action_bar_menu_proctor, menu);
			MenuItem miViewGrades = menu.findItem(R.id.action_view_grade); miViewGrades.setVisible(false);
			MenuItem miPhotoTest = menu.findItem(R.id.action_camera_test); miPhotoTest.setVisible(false);
			if (currentTab == 4) {
				miPhotoTest.setVisible(true);
			} else if (currentTab == 5) {
				miViewGrades.setVisible(true);
				miPhotoTest.setVisible(true);

			}
			
		}
		else if(((MEducationApplication) getApplication()).getAccountType().equals(MEducationApplication.STUDENT)){
			inflater.inflate(R.menu.action_bar_menu_student, menu);

		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.action_sync:
			if (mDBApi.getSession().authenticationSuccessful()){
				if(((MEducationApplication)getApplication()).getAccountType().equals(MEducationApplication.STUDENT)){
					(new DownloadFiles()).execute();
				}
				else{
					(new UploadFiles()).execute();
				}
			}
			else{
				AlertDialog.Builder builder = new AlertDialog.Builder(MainPageActivity.this);
				builder.setTitle("Unable to sync");
				builder.setMessage("Dropbox connection is required to sync. Please connect and link this account to the dropbox account, and try again.");
				builder.setPositiveButton("Link", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						mDBApi.getSession().startAuthentication(MainPageActivity.this);
					}
				});
				builder.setNegativeButton("Cancel", new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
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

	@Override
	public void onBackPressed() {
		if (currentTab > 0) {
			onTabSelected(getActionBar().getTabAt(currentTab - 1), null);
		}
		else if(currentTab==0){
			Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("MEducation");
			builder.setMessage("Are you sure you want to logout?");
			builder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					logout();
					arg0.dismiss();
				}
			});
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					arg0.dismiss();
				}
			});
			builder.show();
		}
	}

	@Override
	protected void onResume() {
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

	class UploadFiles extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			Toast.makeText(getApplicationContext(), "Starting Upload",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected String doInBackground(String... params) {
			int count = 0;
			Log.d(TAG, ">>>Cu" + count);
			count++;

			for (File file : getListFiles(getSourceDir())) {
				Log.d(TAG, ">>>Cu" + count);
				count++;
				try {
					FileInputStream fis = new FileInputStream(file);
					Entry response = mDBApi.putFileOverwrite(File.separator
							+ file.getName(), fis, file.length(), null);
				} catch (DropboxException e) {
					Log.e(TAG, ">>>DropboxError");

					e.printStackTrace();
					cancel(true);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(), "UPLOAD COMPLETE",
					Toast.LENGTH_LONG).show();

			(new DownloadFiles()).execute();
		}

		@Override
		protected void onCancelled() {
			Toast.makeText(
					getApplicationContext(),
					"Upload Unsuccessful. This may have been cause by an unavailable network, or Dropbox unlinking",
					Toast.LENGTH_LONG).show();
		}

		private List<File> getListFiles(File parentDir) {
			ArrayList<File> inFiles = new ArrayList<File>();
			File[] files = parentDir.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					if (file != null && file.isDirectory()) {
						inFiles.addAll(getListFiles(file));
					} else {
						inFiles.add(file);
					}
				}
			}
			return inFiles;
		}

	}

	class DownloadFiles extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			Toast.makeText(getApplicationContext(), "Starting Download",
					Toast.LENGTH_SHORT).show();
		}

		@Override
		protected String doInBackground(String... params) {
			try {
				ArrayList<String> allFileNames = getAllDropboxFileNames();

				for (String singleFileName : allFileNames) {
					File file = new File(getSourceDir() + singleFileName);
					FileOutputStream fos = new FileOutputStream(file);
					Log.i(TAG, ">>>>" + getSourceDir() + " | " + singleFileName);
					DropboxFileInfo info = mDBApi.getFile(singleFileName, null,
							fos, null);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (DropboxException e) {
				e.printStackTrace();
				cancel(true);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(), "SYNC COMPLETE",
					Toast.LENGTH_LONG).show();
		}

		@Override
		protected void onCancelled() {
			Toast.makeText(
					getApplicationContext(),
					"Download Unsuccessful. This may have been cause by an unavailable network, or Dropbox unlinking. Not all files may have been downloaded.",
					Toast.LENGTH_LONG).show();
		}

	}

	public ArrayList<String> getAllDropboxFileNames() {
		ArrayList<String> fileNames = new ArrayList<String>();
		Entry entries;
		try {
			entries = mDBApi.metadata("/", 100, null, true, null);

			for (Entry e : entries.contents) {
				if (!e.isDeleted && !e.isDir) {
					Log.i(TAG, ">>>" + e.path);
					fileNames.add(e.path);
				}
			}
		} catch (DropboxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return fileNames;
	}

	public File getSourceDir() {
		File sdDir = Environment.getExternalStorageDirectory();
		return new File(sdDir, "MEducation");
	}
}
