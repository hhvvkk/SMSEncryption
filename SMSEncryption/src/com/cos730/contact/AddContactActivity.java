package com.cos730.contact;

import android.app.Activity;
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

import com.cos730.database.Contact;
import com.cos730.database.DatabaseHandler;
import com.cos730.encryption.Charset;
import com.cos730.encryption.Keys.KeyGenerator;
import com.cos730.smsencryption.ContactListActivity;
import com.cos730.smsencryption.R;


public class AddContactActivity extends Activity {

	private static boolean DONE;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_contact);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		DONE = false;
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_contact, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.addContact_action_back) {
			goBack();
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
			View rootView = inflater.inflate(R.layout.fragment_add_contact,
					container, false);
			
			EditText etMySeed = (EditText)rootView.findViewById(R.id.editTextMyKey);
			
			Charset cs=new Charset();
			KeyGenerator gen=new KeyGenerator(cs);
			
			String mySeed = gen.genKey();
			
			etMySeed.setText(mySeed);
			
			return rootView;
		}
	}
	
	/**
	 * The add contact to add a contact to the current user
	 * @param view
	 */
	public void addContact(View view){
		/**#FRQ10 :: Realizes Add Contact
		 * Entered contact details will be validated.
		 * The contact will be added to the database.
		 * */
		
		EditText etName = (EditText)findViewById(R.id.editTextNewContactName);
		EditText etNumber = (EditText)findViewById(R.id.editTextNewContactNumber);
		EditText etHisSeed = (EditText)findViewById(R.id.editTextContactKey);
		EditText etMySeed = (EditText)findViewById(R.id.editTextMyKey);
		
		//validate first
		boolean success = validate(etName.getText().toString());
		
		if(success){

			DatabaseHandler dbHandler = new DatabaseHandler(this);
			
			Contact newContact = new Contact(etName.getText().toString(), etNumber.getText().toString(),etHisSeed.getText().toString(),etMySeed.getText().toString());
			
			dbHandler.addContact(newContact);
			
			showMessage("Successfully added a contact", "Success", true);
		}
	}
	

	private boolean validate(String name){
		if(name.length() < 2){ //check if username is correct length
			showMessage("Contact name size is invalid. You must at least have 2 characters.", "Failed", false);
			return false;
		}
		return true;
	}
	
	public void GenerateKey(View view)
	{
		EditText etMySeed = (EditText)findViewById(R.id.editTextMyKey);
		
		Charset cs=new Charset();
		KeyGenerator gen=new KeyGenerator(cs);
		
		String mySeed = gen.genKey();
		
		etMySeed.setText(mySeed);
		
	}


	/**
	 * Shows a message with a certain title and exit activity if needed
	 * @param message The message to be shown
	 * @param title The title of the message
	 * @param done A boolean indicating if the activity can exit by calling finish
	 */
	private void showMessage(String message, String title, boolean done ){
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
