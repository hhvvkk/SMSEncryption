package com.cos730.smsencryption;

import java.util.List;
import java.util.Map;
import java.util.logging.LoggingPermission;

import com.cos730.database.Contact;
import com.cos730.database.ContactContent;
import com.cos730.database.ContactContent.ContactItem;
import com.cos730.database.DatabaseHandler;
import com.cos730.encryption.Charset;
import com.cos730.encryption.OneTimePadHybridEncryption;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

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

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
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
    
    
    public void Encrypt(View view){
    	
    	EditText text = (EditText)findViewById(R.id.textMultiLineMessage);
    	
    	Charset cs = new Charset();   	    	
    	
    	DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
    	
    	Contact contact = dbHandler.getContact(ITEM_ID);   
    	
    	System.out.println("name : "+contact.getName());
    	System.out.println("his seed : "+contact.getHisSeed());
    	System.out.println("my seed : "+contact.getMySeed());
    	
        OneTimePadHybridEncryption hybrid = new OneTimePadHybridEncryption(cs);
        
        String encrypted=hybrid.Encrypt(text.getText().toString(),contact,getApplicationContext());        
        
        text.setText(encrypted);
    	
    }
    
    public void Decrypt(View view){
    	
    	EditText text = (EditText)findViewById(R.id.textMultiLineMessage);
    	
    	Charset cs = new Charset();
    	
    	DatabaseHandler dbHandler = new DatabaseHandler(getApplicationContext());
    	
    	Contact contact = dbHandler.getContact(ITEM_ID);    	
    	
    	System.out.println("name : "+contact.getName());
    	System.out.println("his seed : "+contact.getHisSeed());
    	System.out.println("my seed : "+contact.getMySeed());
	
        OneTimePadHybridEncryption hybrid = new OneTimePadHybridEncryption(cs);
        
        String Decrypted=hybrid.Decrypt(text.getText().toString(),contact,getApplicationContext(),this);        
        
        text.setText(Decrypted);
    }
}
