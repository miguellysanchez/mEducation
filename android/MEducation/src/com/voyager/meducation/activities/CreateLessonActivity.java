package com.voyager.meducation.activities;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import com.voyager.meducation.R;
import com.voyager.meducation.utils.DateUtils;
import com.voyager.meducation.utils.FileUtils;

import android.widget.RelativeLayout.LayoutParams;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Spinner;

public class CreateLessonActivity extends Activity {

	private static final int REQUEST_FILE_SELECT_CODE = 0;
	private static final int REQUEST_IMAGE_CAPTURE = 1888;
	private static final int REQUEST_VIDEO_CAPTURE = 1889;
	private static final String TEMP_VIDEO_NAME = "MEducation_temp_video_";
	private static final String TEMP_IMAGE_NAME = "MEducation_temp_image_";

	private static final String TAG = CreateLessonActivity.class
			.getSimpleName();
	private int lessonResourceIndex = -1;
	ArrayList<View> listLessonFields = new ArrayList<View>();
	Uri mImageUri;
	Uri mVideoUri;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_lesson_activity);

		getActionBar().setDisplayHomeAsUpEnabled(true);

		findViewById(R.id.btnAddCreateLessonField).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						createNewLessonField();
					}
				});
		
		findViewById(R.id.btnCreateNewLesson).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				for(View v : listLessonFields){
					LinearLayout ll =((LinearLayout)v);
					RelativeLayout rl = ((RelativeLayout)ll.getChildAt(0));
					String path = ((TextView)rl.findViewById(R.id.extraTextLessonResource)).getText().toString();	
					String resourcePartName = ((EditText)rl.findViewById(R.id.extraEditLessonResourceName)).getText().toString();
					String subjectPartName = ((Spinner)findViewById(R.id.dropdownCreateLessonSubjectsList)).getSelectedItem().toString();
					String lessonPartName = ((EditText)findViewById(R.id.editLessonTitle)).getText().toString();
					String fileName = subjectPartName+"_"+lessonPartName+"_-_-_"+resourcePartName+"_"+DateUtils.getDateString();
					copyFile(path, fileName);
				}
			
				finish();
			}
		});
	}

	private void createNewLessonField() {
		final LinearLayout createLessonFieldsInner = (LinearLayout) findViewById(R.id.createLessonFieldsInner);
		final LinearLayout addLessonItem = (LinearLayout) getLayoutInflater()
				.inflate(R.layout.add_lesson_item, null);
		createLessonFieldsInner.getChildCount();
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		listLessonFields.add(addLessonItem);

		addLessonItem.findViewById(R.id.extraBtnBrowseResource)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						showFileChooser(listLessonFields.indexOf(addLessonItem));
					}
				});
		addLessonItem.findViewById(R.id.extraBtnRemoveResource)
				.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						listLessonFields.remove(listLessonFields
								.indexOf(addLessonItem));
						createLessonFieldsInner.removeView(addLessonItem);
					}
				});
		createLessonFieldsInner.addView(addLessonItem, params);
	}

	private void showFileChooser(int index) {
		Log.d(TAG, ">>>FOOO:" + index);
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.setType("*/*");
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		lessonResourceIndex = index;
		try {
			startActivityForResult(
					Intent.createChooser(intent, "Select a File to add"),
					REQUEST_FILE_SELECT_CODE);
		} catch (android.content.ActivityNotFoundException ex) {
			// Potentially direct the user to the Market with a Dialog
			Toast.makeText(this, "Please install a File Manager.",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (resultCode == RESULT_OK) {
			
			if(data==null){
				Log.e(TAG, ">>>FUUUUU");
			}else{
				Log.e(TAG, ">>>MERON");
			}
			
			Uri dataUri=null;
			if(data!=null){
				 dataUri = data.getData();
			}
			switch (requestCode) {
			case REQUEST_FILE_SELECT_CODE:
				// Get the Uri of the selected file
				
				Log.d(TAG, "File Uri: " + dataUri.toString());
				// Get the path
				String path = null;
				try {
					path = FileUtils.getPath(this, dataUri);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				LinearLayout ll =((LinearLayout)listLessonFields.get(lessonResourceIndex));
				RelativeLayout rl = ((RelativeLayout)ll.getChildAt(0));
				((TextView)rl.findViewById(R.id.extraTextLessonResource)).setText(path);
				
				Log.d(TAG, ">>>File Path: " + path+" |  "+lessonResourceIndex);
				// Get the file instance
				// File file = new File(path);
				// Initiate the upload
				lessonResourceIndex = -1;
				break;
			case REQUEST_VIDEO_CAPTURE:
				String path2 = "";
				try {
					path2 = FileUtils.getPath(this, mVideoUri);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				Log.i(TAG, ">>>File Uri: " + path2);
				createNewLessonField();
				LinearLayout ll2 =((LinearLayout)listLessonFields.get(listLessonFields.size()-1));
				RelativeLayout rl2 = ((RelativeLayout)ll2.getChildAt(0));
				((TextView)rl2.findViewById(R.id.extraTextLessonResource)).setText(path2);
				
				break;
			case REQUEST_IMAGE_CAPTURE:
				Log.i(TAG, ">>>ACTIVITYFORRESULT- IMAGE");
				String path3 = "";
				try {
					path3 = FileUtils.getPath(this, mImageUri);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
				Log.i(TAG, ">>>File Uri: " + path3);
				createNewLessonField();
				LinearLayout ll3 =((LinearLayout)listLessonFields.get(listLessonFields.size()-1));
				RelativeLayout rl3 = ((RelativeLayout)ll3.getChildAt(0));
				((TextView)rl3.findViewById(R.id.extraTextLessonResource)).setText(path3);
				
				break;
			}
			mImageUri=null;
			mVideoUri=null;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	public boolean copyFile(String from, String fileName) {
		String extension = from.substring(from.lastIndexOf("."));
		Log.i(TAG,"Copy from: "+from+" | fileName: "+fileName+extension);
		try {
			File source = new File(from);
			
			File destination =new File(getParentDir(), fileName+extension);

			if (source.exists()) {
				FileInputStream fis = new FileInputStream(source);
				FileOutputStream fos = new FileOutputStream(destination);
				FileChannel src = fis.getChannel();
				FileChannel dst = fos.getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				fis.close();
				fos.close();
			}
			else{
				Log.i(TAG,">>>NO");
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private File getParentDir() {
		File sdDir = Environment
				.getExternalStorageDirectory();
		Log.i(TAG, ">>>DIRECTORY: "+sdDir.getPath());
		return new File(sdDir, "MEducation");
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_bar_menu_create_lesson, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		case R.id.action_add_video_resource:
			Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		    mVideoUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES),TEMP_VIDEO_NAME+DateUtils.getDateString()+".mp4"));
	 	    intent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoUri);;
		    startActivityForResult(intent, REQUEST_VIDEO_CAPTURE);
			break;
		case R.id.action_add_image_resource:
			Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			mImageUri = Uri.fromFile(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), TEMP_IMAGE_NAME+DateUtils.getDateString()+".jpg"));
			intent2.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
			startActivityForResult(intent2, REQUEST_IMAGE_CAPTURE);
			break;
		}
		return true;
	}
}
