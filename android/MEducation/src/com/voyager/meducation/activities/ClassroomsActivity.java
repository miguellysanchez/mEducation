package com.voyager.meducation.activities;


import java.util.ArrayList;

import com.voyager.meducation.R;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ClassroomsActivity extends Activity {

	public static final String TAG = ClassroomsActivity.class.getSimpleName();
	
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.classrooms_activity);
		
		//hardcoded classrooms
		ListView classroomsListView = (ListView)findViewById(R.id.listClassrooms);
		String[] values = new String[]{
			"Class-A", "Class-B1","Class-B2", "Class-B3", "Class-C", "Class-D"
		};
		ArrayList<String> list = new ArrayList<String>();
		for(int j=0;j<values.length ;j++){
			list.add(values[j]);
		}
		final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		classroomsListView.setAdapter(adapter);
		classroomsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				switch (position) {
				default:
					Intent viewSubjectIntent = new Intent(ClassroomsActivity.this, StudentClassroomActivity.class);
					startActivity(viewSubjectIntent);
					overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
					break;
				}
				
			}
		
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			overridePendingTransition(R.anim.right_slide_out, R.anim.left_slide_in);
			break;
		case R.id.action_search:
			Toast.makeText(this, "Menu Item 1 selected", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.action_settings:
			Toast.makeText(this, "Menu item 2 selected", Toast.LENGTH_SHORT)
					.show();
			break;

		default:
			break;
		}

		return true;
	}
	
}
