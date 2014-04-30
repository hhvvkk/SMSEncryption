package com.cos730.contact;

import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.cos730.database.Contact;
import com.cos730.database.DatabaseHandler;
import com.cos730.encryption.Charset;
import com.cos730.encryption.Keys.KeyGenerator;
import com.cos730.smsencryption.R;

public class EditContactActivity extends Activity {
	
	protected static Contact currentContact = null;
	
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
			finish();
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
		
		/**
		 * Clear the fields as if nothing is selected
		 */
		private void clearFields(){
			
		}
		
	}
	
	public void saveChanges(View view){
		//take all the items from fields
		EditText editTextName = (EditText)findViewById(R.id.editTextEditContactContactName);
		EditText editTextNumber = (EditText)findViewById(R.id.editTextEditContactContactNumber);
		EditText editTextMyKey = (EditText)findViewById(R.id.editTextEditContactMyKey);
		EditText editTextContactKey = (EditText)findViewById(R.id.editTextEditContactContactKey);
		
		String name = editTextName.getText().toString();
		String number = editTextNumber.getText().toString();
		String myKey = editTextMyKey.getText().toString();
		String contactKey = editTextContactKey.getText().toString();
		
		if(currentContact != null){
			DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
			
			//change values
			currentContact.setName(name);
			currentContact.setPhoneNumber(number);
			currentContact.setMySeed(myKey);
			currentContact.setHisSeed(contactKey);
			
			dbHandler.updateContact(currentContact);
			showUpdatedDialogue();
		}
		
		
	}
	
	public void deleteContact(View view){
		showDeleteDialogue();
	}
	
	private void showUpdatedDialogue(){
		new AlertDialog.Builder(this)
			.setTitle("Update")
			.setMessage("Successfully updated contact")
		    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) { 
		            // continue with delete
		        }
		     })
		     .show();
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
	
	private void deleteContact(){
		
	}
	
	public void GenerateKey(View view)
	{
		EditText etMySeed = (EditText)findViewById(R.id.editTextEditContactMyKey);
		
		Charset cs=new Charset();
		KeyGenerator gen=new KeyGenerator(cs);
		
		String mySeed = gen.genKey();
		
		etMySeed.setText(mySeed);
		
	}

}
