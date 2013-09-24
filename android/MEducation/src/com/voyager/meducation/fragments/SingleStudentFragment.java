package com.voyager.meducation.fragments;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.webkit.MimeTypeMap;

public class SingleStudentFragment extends Fragment {
	
	public static final String TAG = SingleStudentFragment.class.getSimpleName();
	private View thisView;
	public static final int MAX_IMAGE_SIZE = 1024*1024*200; //200 KB
	
	String studentName=null;
	ArrayList<File> selectedFiles;
	File[] allSelectedFiles;

	public SingleStudentFragment(String name){
		studentName = name;
		selectedFiles = new ArrayList<File>();
		File sourceDir = getSourceDir();
		
		//filter all files from selected directory
		allSelectedFiles = sourceDir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File f) {
				if(!f.isDirectory()&&f.getName().split("_").length>3&& f.getName().split("_")[3].equals(studentName)){
					Log.i(TAG,">>>New file"+ f.getPath() + " | Name: "+f.getName());
					return true;
				}
				return false;
			}
		});
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		String acctType = ((MEducationApplication)getActivity().getApplication()).getAccountType();
		
		thisView = inflater.inflate(R.layout.single_student_fragment, null);

		ListView listStudentFiles = (ListView)thisView.findViewById(R.id.listFiles);
		ArrayList<String> selectedFileNames = new ArrayList<String>();
		
		if(allSelectedFiles!=null&&allSelectedFiles.length>0){
			for(File file: allSelectedFiles){ 
				if(file!=null){
					selectedFileNames.add(file.getName());
				}
			}
		}
		final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, selectedFileNames);
		listStudentFiles.setAdapter(adapter);
		listStudentFiles.setOnItemClickListener(new OnItemClickListener() {
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
		if(acctType.equals(MEducationApplication.TEACHER)){
			listStudentFiles.setOnItemLongClickListener(new OnItemLongClickListener() {

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
						AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
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
									Intent photoEditIntent = new Intent( getActivity(), FeatherActivity.class );
									photoEditIntent.setData( targetFileUri );
									photoEditIntent.putExtra(Constants.EXTRA_OUTPUT, targetFileUri);
									photoEditIntent.putExtra(Constants.EXTRA_TOOLS_LIST, new String[]{"TEXT","ADJUST", "DRAWING"});
									photoEditIntent.putExtra(Constants.EXTRA_OUTPUT_FORMAT, Bitmap.CompressFormat.JPEG);
									photoEditIntent.putExtra(Constants.EXTRA_MAX_IMAGE_SIZE, MAX_IMAGE_SIZE);
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
		
		return thisView;
	}
	
	private File getSourceDir() {
		File sdDir = Environment
				.getExternalStorageDirectory();
		return new File(sdDir, "MEducation");
	}
	
}
