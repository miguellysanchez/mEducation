package com.voyager.meducation.fragments;

import java.util.ArrayList;

import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;
import com.voyager.meducation.activities.MainPageActivity;
import com.voyager.meducation.activities.TakeExamPhotoActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class StudentClassroomFragment extends Fragment {

	public static final String TAG = StudentClassroomFragment.class.getSimpleName();
	
	ActionBar actionBar;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View thisView = inflater.inflate(R.layout.student_classroom_fragment, container, false);
		String[] values={"Juan Luna", "Jose Rizal", "Yoshino Shinobu" , "Miguel Sanchez", "JV Vitug", "Edgar Parokya" , "Johnny Bravo"};
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<values.length;i++){
        	list.add(values[i]);
        }
		final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
		ListView listStudents =(ListView) thisView.findViewById(R.id.listStudents);
		listStudents.setAdapter(adapter);
		listStudents.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				switch (position) {
				default:
					String targetName = parent.getItemAtPosition(position).toString();
					Log.i(TAG, ">>>NAME: "+targetName);
					((MainPageActivity)getActivity()).goToSingleStudent(targetName);
				break;
				}

			}

		});
		// Inflate the layout for this fragment
        return thisView;
    }
}
