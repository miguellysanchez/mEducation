package com.voyager.meducation.activities;

import java.util.ArrayList;

import com.voyager.meducation.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SubjectsActivity extends Activity {

	public static final String TAG = SubjectsActivity.class.getSimpleName();
	ActionBar actionBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		setContentView(R.layout.subjects_activity);

		// hardcoded subjects
		ListView subjectsListView = (ListView) findViewById(R.id.listSubjects);
		String[] values = new String[] { "Math", "Science", "English",
				"Computer" };
		ArrayList<String> list = new ArrayList<String>();
		for (int j = 0; j < values.length; j++) {
			list.add(values[j]);
		}
		final ArrayAdapter adapter = new ArrayAdapter(this,
				android.R.layout.simple_list_item_1, list);
		subjectsListView.setAdapter(adapter);
		subjectsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view,
					int position, long id) {
				switch (position) {
				default:
					Intent viewSubjectIntent = new Intent(
							SubjectsActivity.this, ClassroomsActivity.class);
					startActivity(viewSubjectIntent);
					overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
					break;
				}

			}

		});
		subjectsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							final View view, int position, long id) {
						switch (position) {
						default:
							CharSequence[] items = new CharSequence[] {
									"View/Edit Details",
									"Remove subjects from account" };
							AlertDialog.Builder builder = new AlertDialog.Builder(
									parent.getContext());
							builder.setItems(items, null);

							builder.show();
							break;
						}
						return true;
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
