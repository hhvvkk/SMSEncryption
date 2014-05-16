package com.cos730.user;

import java.util.Calendar;
import java.util.Date;

import com.cos730.database.DatabaseHandler;
import com.cos730.database.User;
import com.cos730.smsencryption.ContactListActivity;
import com.cos730.smsencryption.R;
import com.cos730.smsencryption.R.id;
import com.cos730.smsencryption.R.layout;
import com.cos730.smsencryption.R.menu;

import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
	
	public static final String INVALID_LOGIN = "Username or password is incorrect";
	
	private static  Calendar cal = Calendar.getInstance();
	
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
	
	int LoginAttempts=0;
	Date attempt1;
	Date attempt2;
	Date attempt3;
	public void validateAndContinue(View view){
		Intent intent = new Intent(this, ContactListActivity.class);
		
		EditText userNameEditText = (EditText) findViewById(R.id.editTextLoginUsername);
		
		EditText passwordEditText = (EditText) findViewById(R.id.editTextLoginPassword);
		
		passwordEditText.clearComposingText();
		
		String username = userNameEditText.getText().toString();
		
		//Password stored as char array and not string to 
		//prevent it from appearing in the string pool used by  java
		char [] password = passwordEditText.getText().toString().toCharArray();
		
		User currentUser = LoginHandler.getUserByName(username, this.getApplicationContext());
		
		if(currentUser == null)
		{
			
				
			if(LoginAttempts==0)
			{
			cal = Calendar.getInstance();
			attempt1=cal.getTime();
			
			//first attempt
			showLoginError(INVALID_LOGIN+", 2 more attempt(s) remain.s");

			
			LoginAttempts++;
			}else if(LoginAttempts==1)
			{
				cal = Calendar.getInstance();
				attempt2=cal.getTime();
				
				//miliseconds where 180000 is 3 minutes
				if(attempt2.getTime() - attempt1.getTime() > 180000)
				{
					showLoginError(INVALID_LOGIN+", 2 more attempt(s) remains.");
					LoginAttempts=1;
				}
				else
				{
				
				//second attempt
				showLoginError(INVALID_LOGIN+", 1 more attempt(s) remains.");
				LoginAttempts++;
				}
				
			}else if(LoginAttempts==2)
			{
				cal = Calendar.getInstance();
				attempt3=cal.getTime();
				
				//miliseconds where 180000 is 3 minutes
				if(attempt3.getTime() - attempt2.getTime() > 180000)
				{
					showLoginError(INVALID_LOGIN+", 2 more attempt(s) remains.");
					LoginAttempts=1;
				}
				else
				{
				
					//third attempt
					showLoginError(INVALID_LOGIN+", please wait 3 minutes and try again.");					
					LoginAttempts++;
				}				

			}
			else
			{
				cal = Calendar.getInstance();
				long milisecondsPassed=(cal.getTime().getTime() - attempt3.getTime());
				System.out.println(cal.getTime().getTime());
				System.out.println(attempt3.getTime());
				//3 minutes passed
				if(milisecondsPassed > 180000)
				{
					LoginAttempts=0;
					validateAndContinue(view);
				}
				else
				{
					int minutes=(int) ((180000-milisecondsPassed)/60000.0);
					int seconds= (int)(((180000-milisecondsPassed) - minutes*60000.0) / 1000.0);
					
					showLoginError("Please wait "+minutes+" minutes and "+seconds+" seconds");
				}
			}
			
			return; //if the user does not exist
		}
		
		//get the hashed value of the current password typed in
		String hashedString = LoginHandler.hashValue(password);
		
		if(!currentUser.getPasswordHash().equals(hashedString)){
			showLoginError(INVALID_LOGIN);
			return; //if the user password does not match
		}
		
		//continue
		intent.putExtra(LOGIN_PASSWORD, password);
		
		//dbhandler to assign values for encryption
		DatabaseHandler dbHandler = new DatabaseHandler(this.getApplicationContext());
		dbHandler.setEncryption(username,passwordEditText.getText().toString());
		
		startActivity(intent);
	}
	
	
	/**
	 * Opens the activity whereby a new user can be added
	 */
	public void openAddUserActivity(){
		Intent intent = new Intent(this, AddUserActivity.class);
		startActivity(intent);
		
	}
	


	private void showLoginError(String errorMessage){
		new AlertDialog.Builder(this)
		.setTitle("Error")
		.setMessage(errorMessage)
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            
	        }
	     })
	     .show();
	}
	
}
