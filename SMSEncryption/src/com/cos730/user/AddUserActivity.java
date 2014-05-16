package com.cos730.user;

import android.app.Activity;
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

import com.cos730.database.DatabaseHandler;
import com.cos730.database.User;
import com.cos730.smsencryption.R;

public class AddUserActivity extends Activity {

	private static boolean DONE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);

		DONE = false;
		
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
	

	/**
	 * Adds a user to the database
	 * @param view
	 */
	public void addUser(View view){
		/**#FRQ1 :: Realizes Add User Account
		 * The user details entered will be captured and stored appropriately in the database.
		 * */
		
		EditText editTextName = (EditText)findViewById(R.id.editTextAddUserLoginUsername);
		EditText editTextPassword = (EditText)findViewById(R.id.editTextAddUserLoginPassword);
		EditText editTextPasswordConfirm = (EditText)findViewById(R.id.editTextAddUserLoginPasswordConfirm);
		
		String username = editTextName.getText().toString();
		char []password = editTextPassword.getText().toString().toCharArray();
		char []passwordConfirm = editTextPasswordConfirm.getText().toString().toCharArray();
		
		
		
		//find the hashed password
		String hashedPassword = LoginHandler.hashValue(password);
		
		DatabaseHandler dbHandler = new DatabaseHandler(this.getApplicationContext());
		
		//get the amount of users currently
		int userCount = dbHandler.getUsersCount(false);
		
		//find out if a user already exist, if so prevent the creation of a new user
		if(userCount >= 1){ 
			showMessage("A user already exist, and you can only have one active user","Failed", true);
		}
		else{
			//validate all inputs
			boolean success = validateInputs(username, password, passwordConfirm);
			
			
			if(!success){
				return;
			}
			
			success = checkAvailabilityOfName(username);
			
			//if it is not available
			if(!success){
				return;
			}
			
			//thereafter add the user if all is well
			User newUser = new User(username, hashedPassword);
			dbHandler.addUser(newUser);

			showMessage("Successfully added the user","Success", true);
		}
		
		
	}

	private boolean validateInputs(String username, char []password, char []passwordConfirm){
		
		if(username.length() < 2){ //check if user name is correct length
			showMessage("Your username size is invalid. You must at least have 2 characters.", "Failed", false);
			return false;
		}
		if(password.length < 2 || passwordConfirm.length < 2){ //check if password is correct length
			showMessage("Your password or password confirm size is invalid. You must at least have 2 characters.", "Failed", false);
			return false;
		}
		else {
			boolean same = true;

			if(password.length != passwordConfirm.length){
				same = false;
			}
			else{
				//find out whether there exist differences in the passwords
				for(int i = 0; (i < password.length) && (i < passwordConfirm.length); i++){
					if(password[i] != passwordConfirm[i]){
						same = false;
						break;
					}
				}
			}
			
			if(!same){//notify if user password does not match confirm password
				showMessage("Your password does not match the confirm password","Failed",false);
				return false;
			}
	
		
		}
		
		return true;
	}
	
	/**
	 * TODO: Future development which includes multiple users
	 * @param username The user name which the user wants to create now
	 * @return Return true if there exist another user with that user name already
	 */
	private boolean checkAvailabilityOfName(String username){
		return true;
	}
	
	

	/**
	 * Shows a message with a certain title and exit activity if needed
	 * @param message The message to be shown
	 * @param title The title of the message
	 * @param done A boolean indicating if the activity can exit by calling finish
	 */
	private void showMessage(String message, String title, boolean done){
		DONE = done;
		new AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	if(DONE)
	        		finish();
	        }
	     })
	     .show();
	}
	
}
