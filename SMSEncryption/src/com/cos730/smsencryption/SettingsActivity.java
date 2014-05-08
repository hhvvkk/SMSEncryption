package com.cos730.smsencryption;

import java.util.List;

import com.cos730.database.Contact;
import com.cos730.database.DatabaseHandler;
import com.cos730.database.User;
import com.cos730.encryption.AppSecurity;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.os.Build;

public class SettingsActivity extends Activity {

	private boolean endAfterDialog = false;
	
	private boolean editPassword = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

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
			finish();
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
	
	public void updateSettings(View view){

		
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
		
		
		System.out.println("snewUsernames "+snewUsername);
		System.out.println("snewPassword "+snewPassword);
		System.out.println("oldusername "+oldusername);
		System.out.println("oldpassword "+oldpassword);
		

		
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
				
				new AlertDialog.Builder(this)
				.setTitle("Settings")
				.setMessage("Successfully updated settings")
			    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            finish();
			        }
			     })
			     .show();
				return;
			}
				
		}
		
	}
	
	/**
	 * Shows the fields to be able to edit password to a new password
	 * @param view
	 */
	public void showNewPasswordFields(View view){
		editPassword = true;
		
		LinearLayout linearLayoutNewPassword = (LinearLayout) findViewById(R.id.linearLayoutNewPassword);
		LinearLayout linearLayoutNewPasswordConfirm = (LinearLayout) findViewById(R.id.linearLayoutNewPasswordConfirm);
		//Button changePasswordButton = (Button)findViewById(R.id.buttonShowNewPasswordFields);
		
		linearLayoutNewPassword.setVisibility(View.VISIBLE);
		linearLayoutNewPasswordConfirm.setVisibility(View.VISIBLE);
	//	changePasswordButton.setVisibility(View.INVISIBLE);//why wont you work!
	}

	/**
	 * Shows a dialog on the screen with a message and a title
	 * @param message The message to be shown
	 * @param title The title of the message which will be shown
	 * @param end A boolean indicating whether the activity will close after the dialog ok is clicked
	 */
	private void showMessage(String message, String title, boolean end){
		endAfterDialog = end;
		new AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	if(endAfterDialog == true)
	        		finish();
	        }
	     })
	     .show();
	}

}
