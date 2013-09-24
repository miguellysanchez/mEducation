package com.voyager.meducation.activities;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;
import com.voyager.meducation.fragments.SingleStudentFragment;
import com.voyager.meducation.utils.DateUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

public class ViewLessonResourcesActivity extends Activity {

	public static final String TAG = ViewLessonResourcesActivity.class.getSimpleName();
	
	public static final String SUBJECT_NAME = "subject_name";
	public static final String LESSON_NAME = "lesson_name";

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.view_lesson_resources_activity);
		ListView lv = (ListView)findViewById(R.id.listAllLessonResources);
		String subjectName = getIntent().getStringExtra(SUBJECT_NAME);
		String lessonName = getIntent().getStringExtra(LESSON_NAME);

		File sourceDir = getSourceDir();
		
		//filter all files from selected directory
		File[] allSelectedFiles = sourceDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if(!f.isDirectory()&&f.getName().split("_").length>3&& f.getName().split("_")[3].equals("-") ){
//					Log.i(TAG,">>>New file"+ f.getPath() + " | Name: "+f.getName());
					return true;
				}
				return false;
			}
		});

		ArrayList<String> selectedFileNames = new ArrayList<String>();

		if(allSelectedFiles!=null&&allSelectedFiles.length>0){
			if(subjectName== null&&lessonName==null){
				for(File f : allSelectedFiles){
					if(f!=null){
						selectedFileNames.add(f.getName());
					}
				}
				getActionBar().setTitle("MEducation - View lesson resources");
			}
			else if(subjectName!=null){
				for(File f : allSelectedFiles){
					if(f!=null && f.getName().split("_").length>0 && f.getName().split("_")[0].equals(subjectName)){
						selectedFileNames.add(f.getName());
					}
				}
				getActionBar().setTitle("MEducation - View lesson resources | Subject: "+subjectName);
			}
			else if(lessonName!=null){
				for(File f : allSelectedFiles){
					if(f!=null && f.getName().split("_").length>1 && f.getName().split("_")[1].equals(lessonName)){
						selectedFileNames.add(f.getName());
					}
				}
				getActionBar().setTitle("MEducation - View lesson resources | Lesson: "+lessonName);
			}
		}
		
		final ArrayAdapter adapter = new ArrayAdapter(ViewLessonResourcesActivity.this, android.R.layout.simple_list_item_1, selectedFileNames);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
//				Toast.makeText(getActivity().getApplicationContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
				File targetFile = new File(getSourceDir().getPath()+File.separator+parent.getItemAtPosition(position).toString());
				Uri targetFileUri = Uri.fromFile(targetFile);
				String fileExtension = MimeTypeMap
						.getFileExtensionFromUrl(targetFileUri.toString());
				String mimeType = MimeTypeMap.getSingleton()
						.getMimeTypeFromExtension(fileExtension);
				Log.i(TAG, ">>>ooPath: "+targetFileUri.getPath()+" | " + mimeType);
				
				if(mimeType.split("/")[0].equals("image")){
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(targetFileUri, "image/*");
					startActivity(intent);
				}
				else{
					Intent intent = new Intent();
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setAction(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(targetFile), mimeType);
					startActivity(intent); 
				}
			}
		});
		String acctType = ((MEducationApplication)getApplication()).getAccountType();
		if(acctType.equals(MEducationApplication.TEACHER)){
			lv.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
					final File targetFile = new File(getSourceDir().getPath()+File.separator+parent.getItemAtPosition(position).toString());
					final Uri targetFileUri = Uri.fromFile(targetFile);
					String fileExtension = MimeTypeMap
							.getFileExtensionFromUrl(targetFileUri.toString());
					String mimeType = MimeTypeMap.getSingleton()
							.getMimeTypeFromExtension(fileExtension);
					Log.i(TAG, ">>>ooPath: "+targetFileUri.getPath()+" | " + mimeType);
					
					if(mimeType.split("/")[0].equals("image")){
						
						final CharSequence[] items = new CharSequence[]{"View","Grade this test"};
						AlertDialog.Builder builder= new AlertDialog.Builder(ViewLessonResourcesActivity.this);
						builder.create();
						builder.setItems(items, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int item) {
								if(item==0){
									Intent intent = new Intent();
									intent.setAction(Intent.ACTION_VIEW);
									intent.setDataAndType(targetFileUri, "image/*");
									startActivity(intent);
								}
								else if(item==1){
									Intent photoEditIntent = new Intent( ViewLessonResourcesActivity.this, FeatherActivity.class );
									photoEditIntent.setData( targetFileUri );
									photoEditIntent.putExtra(Constants.EXTRA_OUTPUT, targetFileUri);
									photoEditIntent.putExtra(Constants.EXTRA_TOOLS_LIST, new String[]{"TEXT","ADJUST", "DRAWING"});
									photoEditIntent.putExtra(Constants.EXTRA_OUTPUT_FORMAT, Bitmap.CompressFormat.JPEG);
									photoEditIntent.putExtra(Constants.EXTRA_MAX_IMAGE_SIZE, SingleStudentFragment.MAX_IMAGE_SIZE);
									startActivityForResult( photoEditIntent, 1 ); 
								}
								
							}
						});
						AlertDialog alert = builder.create();
						alert.show();
					}
					return false;
				}
		
			});
		}
	}
	private File getSourceDir() {
		File sdDir = Environment
				.getExternalStorageDirectory();
		return new File(sdDir, "MEducation");
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;
		}
		return true;
	}
	
}
