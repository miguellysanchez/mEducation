package com.voyager.meducation.fragments;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import com.voyager.meducation.R;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class SingleStudentFragment extends Fragment {
	
	public static final String TAG = SingleStudentFragment.class.getSimpleName();
	private View thisView;
	
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
				if(!f.isDirectory()&&(f.getName().split("_")[0]).equals(studentName)){
					Log.i(TAG,">>>New file"+ f.getPath() + " | Name: "+f.getName());
					return true;
				}
				return false;
			}
		});
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		thisView = inflater.inflate(R.layout.single_student_fragment, container);
		
			

		ListView listStudentFiles = (ListView)thisView.findViewById(R.id.listFiles);
		ArrayList<String> selectedFileNames = new ArrayList<String>();
		
		for(File f: selectedFiles){
			selectedFileNames.add(f.getName());
		}
		final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, selectedFileNames);
		listStudentFiles.setAdapter(adapter);
	
		return thisView;
	}
	
	private File getSourceDir() {
		File sdDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		return new File(sdDir, "MEducation");
	}
	
}
