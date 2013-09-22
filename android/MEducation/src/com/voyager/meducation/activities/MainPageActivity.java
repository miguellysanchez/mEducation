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
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
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

		mDBApi = ((MEducationApplication) getApplication()).getDBApi();

		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.main_page_activity);
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		mDashboardFragment = new DashboardFragment();

		fragmentTransaction.replace(R.id.fragment_container,
				mDashboardFragment, DashboardFragment.TAG).commit();

		actionBar.addTab(actionBar.newTab().setText("TASKS")
				.setTabListener(this));
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	}

	// /ACTIONS INVOKED BY DASHBOARDFRAGMENT
	public void logout() {
		((MEducationApplication)getApplication()).setAccountType(null);
		((MEducationApplication)getApplication()).setUsername(null);
		((MEducationApplication)getApplication()).setIsLoggedIn(false);
		
		finish();
		overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
	}

	public void goToSubjects() {
		Log.d(TAG, ">>>TO SUBJ");
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

	}

	// INVOKED BY SUBJECTSFRAGMENT
	public void goToLessons(String subject) {
		Log.d(TAG, ">>>TO LESSONS");
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

	}

	// /ACTIONS INVOKED BY LESSONSFRAGMENT
	public void goToClassrooms() {
		Log.d(TAG, ">>>TO CLASSROOM");
		actionBar.addTab(actionBar.newTab().setText("CLASSROOMS")
				.setTabListener(this));
		mClassroomsFragment = new ClassroomsFragment();

		FragmentTransaction fTrans = getFragmentManager().beginTransaction();
		fTrans.setCustomAnimations(R.anim.frag_right_slide_in,
				R.anim.frag_left_slide_out);
		fTrans.replace(R.id.fragment_container, mClassroomsFragment,
				ClassroomsFragment.TAG).commit();
		currentTab = 3;
		getActionBar().setSelectedNavigationItem(currentTab);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

	}

	// /ACTIONS INVOKED BY CLASSROOMSFRAGMENT
	public void goToStudentClassroom() {
		Log.d(TAG, ">>>TO STUDENTS");
		actionBar.addTab(actionBar.newTab().setText("STUDENTS")
				.setTabListener(this));
		mStudentsFragment = new StudentClassroomFragment();

		FragmentTransaction fTrans = getFragmentManager().beginTransaction();
		fTrans.setCustomAnimations(R.anim.frag_right_slide_in,
				R.anim.frag_left_slide_out);
		fTrans.replace(R.id.fragment_container, mStudentsFragment,
				StudentClassroomFragment.TAG).commit();
		currentTab = 4;
		getActionBar().setSelectedNavigationItem(currentTab);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
		if(((MEducationApplication)getApplication()).getAccountType().equals(MEducationApplication.STUDENT)){
			currentTab = 3;
		}
		else{
			currentTab = 5;
		}
		getActionBar().setSelectedNavigationItem(currentTab);
		getActionBar().getSelectedTab().setText(name);
		getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
		switch (currentTab) {
		case 0:
			fTrans.add(R.id.fragment_container, mDashboardFragment).commit();
			mSubjectsFragment = null;
			mLessonsFragment = null;
			mClassroomsFragment = null;
			mStudentsFragment = null;
			mSingleStudentFragment = null;
			break;
		case 1:
			fTrans.add(R.id.fragment_container, mSubjectsFragment).commit();
			mLessonsFragment = null;
			mClassroomsFragment = null;
			mStudentsFragment = null;
			mSingleStudentFragment = null;
			break;
		case 2:
			fTrans.add(R.id.fragment_container, mLessonsFragment).commit();
			mClassroomsFragment = null;
			mStudentsFragment = null;
			mSingleStudentFragment = null;
			break;
		case 3:
			fTrans.add(R.id.fragment_container, mClassroomsFragment).commit();
			mStudentsFragment = null;
			mSingleStudentFragment = null;
			break;
		case 4:
			fTrans.add(R.id.fragment_container, mStudentsFragment).commit();
			mSingleStudentFragment = null;
			break;
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
			(new UploadFiles()).execute();
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
					Toast.LENGTH_LONG).show();
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
					Toast.LENGTH_LONG).show();
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
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(getApplicationContext(), "SYNC COMPLETE",
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
