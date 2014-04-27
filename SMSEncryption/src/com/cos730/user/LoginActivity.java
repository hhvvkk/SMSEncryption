package com.cos730.user;

import com.cos730.smsencryption.ContactListActivity;
import com.cos730.smsencryption.R;
import com.cos730.smsencryption.R.id;
import com.cos730.smsencryption.R.layout;
import com.cos730.smsencryption.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class LoginActivity extends Activity {

	//EXTRA to pass the information along to the other activity
	public static final String LOGIN_PASSWORD = "com.cos730.smsencryption.PASSWORD";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_add_user) {
			openAddUserActivity();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_login,
					container, false);
			return rootView;
		}
	}
	
	/**
	 * The function called when the user enters the password and user name
	 * @param view
	 */
	public void validateAndContinue(View view){
		Intent intent = new Intent(this, ContactListActivity.class);
		
		EditText userNameEditText = (EditText) findViewById(R.id.editTextLoginUsername);
		
		EditText passwordEditText = (EditText) findViewById(R.id.editTextLoginPassword);
		
		passwordEditText.clearComposingText();
		
		String username = userNameEditText.getText().toString();
		
		//Password stored as char array and not string to 
		//prevent it from appearing in the pool
		char [] password = passwordEditText.getText().toString().toCharArray();
		
		intent.putExtra(LOGIN_PASSWORD, password);
		
		
		startActivity(intent);
	}
	
	
	/**
	 * Opens the activity whereby a new user can be added
	 */
	public void openAddUserActivity(){
		Intent intent = new Intent(this, AddUserActivity.class);
		startActivity(intent);
		
	}
	

}
