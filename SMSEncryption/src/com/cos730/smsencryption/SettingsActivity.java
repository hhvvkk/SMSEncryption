package com.cos730.smsencryption;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cos730.database.Contact;
import com.cos730.database.DatabaseHandler;
import com.cos730.database.User;
import com.cos730.encryption.AppSecurity;

public class SettingsActivity extends Activity {

	private boolean DONE = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		DONE = false;

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.updateUser_action_back) {
			goBack();
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
			View rootView = inflater.inflate(R.layout.fragment_settings,
					container, false);
			return rootView;
		}
	}
	
	/**
	 * Update the settings of the application including login details
	 * @param view
	 */
	public void updateSettings(View view){
		/**#FRQ2 :: Realizes Edit User Account
		 * This is done by allowing user to edit the current password or current username.
		 * When user enters something in, it saves new user details, if left blank, the old details are used. 
		 * */
		
		
		EditText newUsername = (EditText)findViewById(R.id.editTextSettingsLoginUsername);
		
		String snewUsername=newUsername.getText().toString();
		
		EditText newPassword = (EditText)findViewById(R.id.editTextSettingsLoginOldPassword);
		
		
		String snewPassword=newPassword.getText().toString();
		
		DatabaseHandler db=new DatabaseHandler(this.getApplicationContext());
		String oldusername=db.getUsername("COS730");
		String oldpassword=db.getPassword("COS730");
		
		
		List<Contact> allcontacts=db.getAllContacts();
		
		if(snewUsername.equals(""))
		{
			snewUsername=oldusername;
		}
		
		if(snewPassword.equals(""))
		{
			snewPassword=oldpassword;
		}
		
		db.setEncryption(snewUsername, snewPassword);

		for(int a=0;a<allcontacts.size();a++)
		{
			Contact temp=allcontacts.get(a);
			db.updateContact(temp);
		}
		
		
		List<User> users=db.getAllUsers();
		AppSecurity sec=new AppSecurity();
		
		for(int a=0;a<users.size();a++)
		{
			User temp=users.get(a);
			System.out.println(temp.getName());
			if(temp.getName().equals(oldusername))
			{

				User temp2=new User(temp.getID(),snewUsername,sec.generateHash(snewPassword));
				db.UpdateSettings(temp2);
				
				showMessage("Successfully updated settings", "Success", true);
			}
				
		}
		
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
	        		goBack();
	        }
	     })
	     .show();
	}
	
	private void goBack(){
		Intent intent = new Intent(this, ContactListActivity.class);
		startActivity(intent);
		finish();
	}
}
