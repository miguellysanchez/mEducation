package com.voyager.meducation.fragments;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;
import com.voyager.meducation.activities.LoginActivity;
import com.voyager.meducation.activities.MainPageActivity;

public class DashboardFragment extends Fragment {

	public static final String TAG = DashboardFragment.class.getSimpleName();
	
	public static final String WELCOME_MSG = "Welcome to M-Education";
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View thisView = inflater.inflate(R.layout.dashboard_fragment, container, false);
		String[] values=null;
		if(((MEducationApplication) getActivity().getApplication()).getAccountType().equals(MEducationApplication.TEACHER)){
			values = new String[]{
		        	"View subjects", "Create new lesson", "Edit profile" ,"Logout"
	        };
		}
		else if(((MEducationApplication) getActivity().getApplication()).getAccountType().equals(MEducationApplication.TEACHER)){
			values = new String[]{
		        	"View teachers", "A", "Start dsd test"
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
				case 0:
					((MainPageActivity)getActivity()).goToSubjects();
					break;
				case 1:
					break;
				case 2:
					break;
				default:
					((MEducationApplication)getActivity().getApplication()).setIsLoggedIn(false);
					((MainPageActivity)getActivity()).logout();
				break;
				}

			}

		});
		// Inflate the layout for this fragment
        return thisView;
    }
}
