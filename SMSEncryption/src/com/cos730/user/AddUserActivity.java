package com.cos730.user;

import com.cos730.database.DatabaseHandler;
import com.cos730.database.User;
import com.cos730.smsencryption.R;
import com.cos730.smsencryption.R.id;
import com.cos730.smsencryption.R.layout;
import com.cos730.smsencryption.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.os.Build;

public class AddUserActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_user, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.addUser_action_back) {
			finish();//finish this activity to return
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
			View rootView = inflater.inflate(R.layout.fragment_add_user,
					container, false);
			return rootView;
		}
	}
	
	public void addUser(View view){
		//firstly validate whether details have been put in correctly
		
		EditText editTextName = (EditText)findViewById(R.id.editTextAddUserLoginUsername);
		EditText editTextPassword = (EditText)findViewById(R.id.editTextAddUserLoginPassword);
		EditText editTextPasswordConfirm = (EditText)findViewById(R.id.editTextAddUserLoginPasswordConfirm);
		
		String username = editTextName.getText().toString();
		char []password = editTextPassword.getText().toString().toCharArray();
		char []passwordConfirm = editTextPasswordConfirm.getText().toString().toCharArray();
		
		boolean success = validateInputs(username, password, passwordConfirm);
		
		//thereafter add the user
			//only if user does not already exist
		if(!success){
			return;
		}
		
		success = checkAvailabilityOfName(username);
		
		//if it is not available
		if(!success){
			return;
		}
		
		//find the hashed password
		String hashedPassword = LoginHandler.hashValue(password);
		
		//else all is well and can add to database
		DatabaseHandler dbHandler = new DatabaseHandler(this.getApplicationContext());
		
		int userCount = dbHandler.getUsersCount(false);
		
		if(userCount >= 1){
			showMessage("A user already exist, and you can only have one active user","Failed");
		}
		else{
			User newUser = new User(username, hashedPassword);
			dbHandler.addUser(newUser);

			showMessage("Successfully added the user","Success");
		}
		
		
	}

	private boolean validateInputs(String username, char []password, char []passwordConfirm){
		/**
		 * TODO: need to implement validation
		 */
		//ifusername size invalid
//		showMessage("Your username size is invalid. You must at least have 4 characters","Error");
//		return false
		
		//if password != passowrdConfirm
//		showMessage("Your password does not match the confirm password","Error");
//		return false;

		//if(password size invalid
//		showMessage("Your password size is invalid. You must at least have 4 characters","Error");
//		return false
		
		return true;
	}
	
	private boolean checkAvailabilityOfName(String username){
		return true;
	}
	
	

	private void showMessage(String message, String title){
		new AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            finish();
	        }
	     })
	     .show();
	}
	
}
