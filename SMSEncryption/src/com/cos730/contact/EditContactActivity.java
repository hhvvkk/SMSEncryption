package com.cos730.contact;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.cos730.database.Contact;
import com.cos730.database.DatabaseHandler;
import com.cos730.encryption.Charset;
import com.cos730.encryption.Keys.KeyGenerator;
import com.cos730.smsencryption.ContactListActivity;
import com.cos730.smsencryption.R;

public class EditContactActivity extends Activity {
	
	protected static Contact currentContact = null;
	
	protected static boolean DONE = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_contact);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_contact, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.editContact_action_back) {
			goBack();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private List<Contact> contactList = null;
		
		private Spinner spinner; 
		
		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_edit_contact,
					container, false);

			loadContacts();
			loadSpinner(rootView);
			
			return rootView;
		}
		
		private void loadContacts(){
			DatabaseHandler db = new DatabaseHandler(this.getActivity().getApplicationContext());
			
			contactList = db.getAllContacts();
		}
		
		
		/**
		 * Load the values from the database into spinner
		 * @param rootView
		 */
		private void loadSpinner(View rootView){
			DatabaseHandler db = new DatabaseHandler(this.getActivity().getApplicationContext());
			
			List<String> lables = db.getAllLabels();
			 
			if(lables == null | lables.size() == 0){
				/*There is no users to edit, thus prevent send user back*/
				EditContactActivity myActivity = (EditContactActivity)this.getActivity();
				
				if(myActivity != null)
					myActivity.showMessage("You can't edit contact without a contact existing", "Notice", true);
			}
			
			ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
					R.layout.application_spinner, lables);
			
			dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			spinner = (Spinner) rootView.findViewById(R.id.spinnerContactList);
			
			spinner.setAdapter(dataAdapter);
			
			
			
			if(dataAdapter.getCount() != 0){
				spinner.setSelection(0);				
			}
			
			spinner.setOnItemSelectedListener(
	                new OnItemSelectedListener() {
	                    public void onItemSelected(
	                            AdapterView<?> parent, View view, int position, long id) {
	                    	loadContactIntoFields(position, id);
	                    }

	                    public void onNothingSelected(AdapterView<?> parent) {
	                        //clearFields();
	                    }
	                });
			
			//give the adapter the color in the resource
			dataAdapter.setDropDownViewResource(R.layout.application_spinner);
			
		}
		
		/**
		 * Loads the contact values into the fields
		 * @param position Position which was clicked
		 * @param id The id of the item which was clicked
		 */
		private void loadContactIntoFields(int position, long id){
			
			if(contactList == null)
				return;
			
			currentContact = contactList.get(position);
			
			EditText editTextName = (EditText)this.getActivity().findViewById(R.id.editTextEditContactContactName);
			EditText editTextNumber = (EditText)this.getActivity().findViewById(R.id.editTextEditContactContactNumber);
			EditText editTextMyKey = (EditText)this.getActivity().findViewById(R.id.editTextEditContactMyKey);
			EditText editTextContactKey = (EditText)this.getActivity().findViewById(R.id.editTextEditContactContactKey);
			
//			//set fields
			if(currentContact != null){
				editTextName.setText(currentContact.getName());
				editTextNumber.setText(currentContact.getPhoneNumber());
				editTextMyKey.setText(currentContact.getMySeed());
				editTextContactKey.setText(currentContact.getHisSeed());
			}
			else{
				editTextContactKey.setText("YES");
			}
		}
		
	}
	
	/**
	 * Saves changes to the current contact details entered
	 * @param view
	 */
	public void saveChanges(View view){
		/**#FRQ11 :: Realizes Edit Contact
		 * Entered contact details will be validated.
		 * The contact details will be updated in the database
		 * */
		/**#FRQ12 :: Realize Synchronise Contact
		 * The contact key of current device key and contact device key entered will be used.
		 * The contact keys will be saved in the database.
		 * */
		
		
		//take all the items from fields
		EditText editTextName = (EditText)findViewById(R.id.editTextEditContactContactName);
		EditText editTextNumber = (EditText)findViewById(R.id.editTextEditContactContactNumber);
		EditText editTextMyKey = (EditText)findViewById(R.id.editTextEditContactMyKey);
		EditText editTextContactKey = (EditText)findViewById(R.id.editTextEditContactContactKey);
		
		String name = editTextName.getText().toString();
		String number = editTextNumber.getText().toString();
		String myKey = editTextMyKey.getText().toString();
		String contactKey = editTextContactKey.getText().toString();
		
		boolean success = validate(name);
		
		if(!success)
			return;
		
		if(currentContact != null){
			DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
			
			//change values
			currentContact.setName(name);
			currentContact.setPhoneNumber(number);
			currentContact.setMySeed(myKey);
			currentContact.setHisSeed(contactKey);
			
			dbHandler.updateContact(currentContact);
			showMessage("Successfully updated contact", "Update", false);
		}
		
		
	}
	
	private boolean validate(String name){
		if(name.length() < 2){ //check if username is correct length
			showMessage("Contact name size is invalid. You must at least have 2 characters.", "Failed", false);
			return false;
		}
		return true;
	}
	
	public void deleteContact(View view){
		showDeleteDialogue();
	}
	
	private void showDeleteDialogue(){
		new AlertDialog.Builder(this)
	    .setTitle("Delete entry")
	    .setMessage("Are you sure you want to delete this entry?")
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        	deleteContact();
	        	
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // do nothing
	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert)
	     .show();
		

	}
	
	/**
	 * Deletes the current selected contact
	 */
	private void deleteContact(){
		/**#FRQ11 :: Realizes Remove Contact
		 * The currently selected contact will be deleted from the application and database
		 * */
		
		if(currentContact != null){
			DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());			
			dbHandler.deleteContact(currentContact);
			
			Intent intent = new Intent(this, ContactListActivity.class);
			finish();
			startActivity(intent);
		}
	}
	
	/**
	 * Generates a key for the contact
	 * @param view
	 */
	public void GenerateKey(View view)
	{
		EditText etMySeed = (EditText)findViewById(R.id.editTextEditContactMyKey);
		
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
	protected void showMessage(String message, String title, boolean done ){
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
	

	/**
	 * Goes back to the main activity(ContactListActivity)
	 */
	private void goBack(){
		Intent intent = new Intent(this, ContactListActivity.class);
		startActivity(intent);
		finish();
	}
}
