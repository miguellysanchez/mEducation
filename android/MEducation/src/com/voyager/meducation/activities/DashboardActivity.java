package com.voyager.meducation.activities;

import java.util.ArrayList;

import com.voyager.meducation.MEducationApplication;
import com.voyager.meducation.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class DashboardActivity extends Activity {

	public static final String TAG = DashboardActivity.class.getSimpleName();
	
	public static final String WELCOME_MSG = "Welcome to M-Education";
	private String firstName = "David";
	private String acctType;
	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		
		setContentView(R.layout.dashboard_activity);
		acctType = ((MEducationApplication)getApplication()).getAccountType();

		
		
		String[] values = new String[]{};
		if(acctType.equals(MEducationApplication.TEACHER)){
				values = new String[]{"View subjects", "View and Approve proctors under you","View and edit profile", "Logout"	
			};
		}
		else if(acctType.equals(MEducationApplication.PROCTOR)){
				values = new String[]{"View managing teachers", "Request approval from Teacher", "View and edit profile","Logout"
			};
		}
		
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < values.length; ++i) {
		      list.add(values[i]);
		}
		
		final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, list);
		ListView listDashboardTasks = (ListView)findViewById(R.id.listDashboardTasks);
		listDashboardTasks.setAdapter(adapter);
		listDashboardTasks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
				if(acctType!=null&&acctType.equals(MEducationApplication.TEACHER)){
					switch (position) {
						case 0:
							Intent subjectsIntent = new Intent(DashboardActivity.this, SubjectsActivity.class);
							startActivity(subjectsIntent);
							overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
						break;
						case 1:
							
						break;
						case 2:
							
						
						break;
						case 3:
							Intent logoutIntent = new Intent(DashboardActivity.this, LoginActivity.class);
							((MEducationApplication)getApplication()).setIsLoggedIn(false);
							((MEducationApplication)getApplication()).setAccountType("");
							startActivity(logoutIntent);
							overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
							DashboardActivity.this.finish();
						break;
					}
				}
				else if(acctType!=null&&acctType.equals(MEducationApplication.PROCTOR)){
					switch (position) {
					case 0:
						
					break;
					case 1:
						
					break;
					case 2:
						
					
					break;
					case 3:
						Intent logoutIntent = new Intent(DashboardActivity.this, LoginActivity.class);
						((MEducationApplication)getApplication()).setIsLoggedIn(false);
						((MEducationApplication)getApplication()).setAccountType("");
						startActivity(logoutIntent);
						overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
						DashboardActivity.this.finish();
					break;
				}
				}

			}

		});
		
	}
	
	
	
}
