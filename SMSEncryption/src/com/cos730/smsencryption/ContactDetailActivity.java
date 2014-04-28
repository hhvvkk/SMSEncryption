package com.cos730.smsencryption;

import com.cos730.database.Contact;
import com.cos730.database.ContactContent;
import com.cos730.database.ContactContent.ContactItem;
import com.cos730.encryption.Charset;
import com.cos730.encryption.OneTimePadHybridEncryption;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
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
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ContactListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    
    public void Encrypt(View view){
    	
    	EditText text = (EditText)findViewById(R.id.textMultiLineMessage);
    	
    	Charset cs = new Charset();   	    	
    	
    	ContactDetailFragment frag=new ContactDetailFragment();
    	 ContactContent cc = new ContactContent(frag.getActivity().getBaseContext());
    	
    	ContactItem contactItem = cc.getItemMap().get(ITEM_ID);
        
        Contact temp=new Contact(contactItem.name,"",contactItem.hisSeed,contactItem.mySeed);
        
        OneTimePadHybridEncryption hybrid = new OneTimePadHybridEncryption(cs);
        
        String encrypted=hybrid.Encrypt(text.getText().toString(),temp);        
        
        text.setText(encrypted);
    	
    }
    
    public void Decrypt(View view){
    	
    	EditText text = (EditText)findViewById(R.id.textMultiLineMessage);
    	
    	Charset cs = new Charset();
    	
    	ContactContent cc = new ContactContent(getApplicationContext());
    	ContactItem contactItem = cc.getItemMap().get(ITEM_ID);
        
        Contact temp=new Contact(contactItem.name,"",contactItem.hisSeed,contactItem.mySeed);
        
        OneTimePadHybridEncryption hybrid = new OneTimePadHybridEncryption(cs);
        
        String Decrypted=hybrid.Decrypt(text.getText().toString(),temp);        
        
        text.setText(Decrypted);
    }
}
