package com.voyager.meducation.fragments;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;

import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;
import com.voyager.meducation.activities.MainPageActivity;

public class SubjectsFragment extends Fragment {

	public static final String TAG = SubjectsFragment.class.getSimpleName();
	ActionBar actionBar;

	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View thisView = inflater.inflate(R.layout.subjects_fragment, container, false);
		
		String[] values = new String[]{
	        	"Science","Mathematics", "English", "Filipino", "Art", "Music", "Computer"
	        };
		
        
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<values.length;i++){
        	list.add(values[i]);
        }
		final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
		ListView listSubjects =(ListView) thisView.findViewById(R.id.listSubjects);
		listSubjects.setAdapter(adapter);
		listSubjects.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				switch (position) {
				default:
					((MainPageActivity)getActivity()).goToLessons(parent.getItemAtPosition(position).toString());
					break;
				}

			}

		});
		
		listSubjects.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position,
					long id) {
				final String targetName = parent.getItemAtPosition(position).toString();
				CharSequence[] items = {"View lesson resources in subject: "+targetName};
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.create();
				builder.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int item) {
						if(item==0){
							((MainPageActivity)getActivity()).goToLessonResources(targetName, null);
						}
					}
				});
				AlertDialog alert = builder.create();
				alert.show();
				return false;
			}
		});
		// Inflate the layout for this fragment
        return thisView;
 
	}
}
