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

public class StudentsFragment extends Fragment {

	public static final String TAG = StudentsFragment.class.getSimpleName();
	
	ActionBar actionBar;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View thisView = inflater.inflate(R.layout.dashboard_fragment, container, false);
		String[] values=null;
		if(((MEducationApplication) getActivity().getApplication()).getAccountType().equals(MEducationApplication.TEACHER)){
			values = new String[]{
		        	"Juan dela Cruz", "Michael Bay", "Pedro Santos" , "Kim Atienza", "Miguel Sanchez", "Edgar Parokya" , "Jose Rizal"
	        };
		}
		else if(((MEducationApplication) getActivity().getApplication()).getAccountType().equals(MEducationApplication.TEACHER)){
			values = new String[]{
		        	"Juan dela Cruz", "Michael Bay", "Pedro Santos" , "Kim Atienza", "Miguel Sanchez", "Edgar Parokya" , "Jose Rizal"
		    };
		}
        
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<values.length;i++){
        	list.add(values[i]);
        }
		final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
		ListView listDashboardTasks =(ListView) thisView.findViewById(R.id.listDashboardTasks);
		listDashboardTasks.setAdapter(adapter);
		listDashboardTasks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				switch (position) {
				default:
					;
				break;
				}

			}

		});
		// Inflate the layout for this fragment
        return thisView;
    }
}
