package com.cos730.smsencryption;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.text.Editable;
import android.text.TextWatcher;

import com.cos730.database.Contact;
import com.cos730.database.DatabaseHandler;
import com.cos730.encryption.Charset;
import com.cos730.encryption.OneTimePadHybridEncryption;

/**
 * An activity representing a single Contact detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ContactListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ContactDetailFragment}.
 */
public class ContactDetailActivity extends FragmentActivity {

	
	public static  int ITEM_ID=0;
	public static Context context;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);

        // Show the Up button in the action bar.
        getActionBar().setDisplayHomeAsUpEnabled(true);

        
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ContactDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ContactDetailFragment.ARG_ITEM_ID));
            
            ITEM_ID=Integer.parseInt(getIntent().getStringExtra(ContactDetailFragment.ARG_ITEM_ID));
            
            ContactDetailFragment fragment = new ContactDetailFragment();
            
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contact_detail_container, fragment)
                    .commit();
        }
        
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpTo(this, new Intent(this, ContactListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    

    /**
     * Encrypt the message currently entered in EditText message
     * @param view
     */
    public void Encrypt(View view){
    	/**#FRQ6 :: Realizes Encrypt Message
		 * The message entered will be encrypted using the keys of the contact 
		 * <<includes>>
		 * #FRQ7 :: Realizes Copy Encrypted Message
		 * The cipher which has been created after encryption will be copied to clipboard
		 * */
		
    	EditText text = (EditText)findViewById(R.id.textMultiLineMessage);
    	
    	String encryptMessage = text.getText().toString();
    	
    	if(encryptMessage.length() > 144){
    		showMessage("The message you are encrypting must be less than 144", "Error");
    		return;
    	}
    	if(encryptMessage.length() == 0){
    		showMessage("The message you are encrypting must be at least one character", "Error");
    		return;
    	}
    	
    	Charset cs = new Charset();   	    	
    	
    	DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
    	
    	Contact contact = dbHandler.getContact(ITEM_ID);   
    	
//    	System.out.println("name : "+contact.getName());
//    	System.out.println("his seed : "+contact.getHisSeed());
//    	System.out.println("my seed : "+contact.getMySeed());
    	
        OneTimePadHybridEncryption hybrid = new OneTimePadHybridEncryption(cs);
        
        String encrypted=hybrid.Encrypt(text.getText().toString(),contact,getApplicationContext());        
        
        text.setText(encrypted);
    	
        /*Copy the encrypted message to the clipboard*/    	
    	ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    	
    	ClipData clip = ClipData.newPlainText("smsEncryption", text.getText().toString());
    	clipboard.setPrimaryClip(clip);
    }
    
    /**
     * Decrypt the cipher currently entered in EditText message
     * @param view
     */
    public void Decrypt(View view){
    	/**#FRQ8 :: Realizes Decrypt Message
		 * The message entered will be decrypted using the keys of the contact 
		 * */
		
    	
    	EditText text = (EditText)findViewById(R.id.textMultiLineMessage);
    	
    	String encryptMessage = text.getText().toString();
    	
    	if(encryptMessage.length() != 160){
    		showMessage("You are trying to decrypt an incorrect message length(should be 160)", "Error");
    		return;
    	}
    	
    	Charset cs = new Charset();
    	
    	DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
    	
    	Contact contact = dbHandler.getContact(ITEM_ID);    	
    	
//    	System.out.println("name : "+contact.getName());
//    	System.out.println("his seed : "+contact.getHisSeed());
//    	System.out.println("my seed : "+contact.getMySeed());
	
        OneTimePadHybridEncryption hybrid = new OneTimePadHybridEncryption(cs);
        
        String Decrypted=hybrid.Decrypt(text.getText().toString(),contact,getApplicationContext(),this);        
        
        text.setText(Decrypted);
    }
    

    /**
     * Copy a message from the message EditText into the clipboard
     * @param view
     */
    public void Copy(View view){
    	/**
    	 * #FRQ7 :: Realizes Copy Encrypted Message
		 * The text contained in the message EditText can be copied to clipboard 
    	 */
    	EditText text = (EditText)findViewById(R.id.textMultiLineMessage);
    	
    	ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    	
    	ClipData clip = ClipData.newPlainText("smsEncryption", text.getText().toString());
    	clipboard.setPrimaryClip(clip);
    	
    	showMessage("Copied the message to clipboard", "Success");
    }
    

    /**
     * Paste the message from the clipboard into the message EditText
     * @param view
     */
    public void Paste(View view){
    	/**
    	 * #FRQ4.2 :: Realizes Paste Message
    	 * The clipboard is used to paste the message into the message EditText
    	 */
    	ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    	ClipData clipData = clipboard.getPrimaryClip();
    	
    	try{
    		String messageText = clipData.getItemAt(0).getText().toString();
    		EditText text = (EditText)findViewById(R.id.textMultiLineMessage);
        	text.setText(messageText);
    	}catch(Exception e){
    		
    	}
    		
    }
    

	/**
	 * Shows a message with a certain title and message
	 * @param message The message to be shown
	 * @param title The title of the message
	 */
	private void showMessage(String message, String title){
		new AlertDialog.Builder(this)
		.setTitle(title)
		.setMessage(message)
	    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	        	//do nothing
	        }
	     })
	     .show();
	}
}
