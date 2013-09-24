package com.voyager.meducation.fragments;

import java.util.ArrayList;

import com.aviary.android.feather.FeatherActivity;
import com.aviary.android.feather.library.Constants;
import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;
import com.voyager.meducation.activities.MainPageActivity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class LessonsFragment extends Fragment {

	String[] values;
	String subject;
	public LessonsFragment(String subj) {
		subject = subj;
	}
	
	
	public static final String TAG = LessonsFragment.class.getSimpleName();
	
	ActionBar actionBar;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View thisView = inflater.inflate(R.layout.lessons_fragment, container, false);

		final String acctType = ((MEducationApplication)getActivity().getApplication()).getAccountType();
		final String username = ((MEducationApplication)getActivity().getApplication()).getUsername();
		if (subject.equals("Science")) {
			values = new String[] {
					"Human Body", "Astronomy", "Measurement", "Scientific Method"
			};
		} else if (subject.equals("Mathematics")) {
			values = new String[] {
					"Addition", "Division", "Estimation", "Multiplication", "Rounding off numbers"
			};
		} else if (subject.equals("English")) {
			values = new String[] {
					"Subject-Verb Agreement", "Verb Tenses", "Spelling"
			};
		} else if (subject.equals("Filipino")) {
			values = new String[] {
					"Tanaga", "Pandiwa", "Pangungusap"
			};
		} else if (subject.equals("Art")) {
			values = new String[] {
					"Complementary Colors", "Perspective", "Painting"
			};
		} else if (subject.equals("Music")) {
			values = new String[] {
					"Tone", "Pitch", "Chords"
			};
		} else if (subject.equals("Computer")) {
			values = new String[] {
					"Visual Basic", "Excel", "Word", "Using internet browsers", "Parts of a computer"
			};
		}
		
        ArrayList<String> list = new ArrayList<String>();
        for(int i=0;i<values.length;i++){
        	list.add(values[i]);
        }
		final ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, list);
		ListView listLessons =(ListView) thisView.findViewById(R.id.listLessons);
		listLessons.setAdapter(adapter);
		listLessons.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				switch (position) {
				default:
					if(acctType.equals(MEducationApplication.STUDENT)){
						((MainPageActivity)getActivity()).goToSingleStudent(username);
					}
					else{
						((MainPageActivity)getActivity()).goToClassrooms();
					}
				break;
				}

			}

		});
		
		listLessons.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View v, int position,
					long id) {
				final String targetName = parent.getItemAtPosition(position).toString();
				CharSequence[] items = {"View lesson resources in lesson "+targetName};
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.create();
				builder.setItems(items, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int item) {
						if(item==0){
							((MainPageActivity)getActivity()).goToLessonResources(null, targetName);
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
