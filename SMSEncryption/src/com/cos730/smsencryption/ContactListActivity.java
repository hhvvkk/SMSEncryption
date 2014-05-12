package com.cos730.smsencryption;

import com.cos730.contact.AddContactActivity;
import com.cos730.contact.EditContactActivity;
import com.cos730.user.LoginActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


/**
 * An activity representing a list of Contacts. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ContactDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 * <p>
 * The activity makes heavy use of fragments. The list of items is a
 * {@link ContactListFragment} and the item details
 * (if present) is a {@link ContactDetailFragment}.
 * <p>
 * This activity also implements the required
 * {@link ContactListFragment.Callbacks} interface
 * to listen for item selections.
 */
public class ContactListActivity extends FragmentActivity
        implements ContactListFragment.Callbacks {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    
    private char [] password = null;
    
    /**
     * Add main menu to the activity
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    /**
     * When the menu is clicked
     */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_add_contact) {
			openAddContactActivity();
			return true;
		}
		else if(id == R.id.action_edit_contact){
        	openEditContactActivity();
			return true;
        }
        else if(id == R.id.action_settings){
        	openSettingsActivity();
			return true;
        }
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Opens activity add contact
	 */
	private void openAddContactActivity(){
		Intent intent = new Intent(this, AddContactActivity.class);
		startActivity(intent);
	}
	
	/**
	 * Opens activity edit contact
	 */
	private void openEditContactActivity(){
		Intent intent = new Intent(this, EditContactActivity.class);
		startActivity(intent);
	}


	/**
	 * Opens settings
	 */
	private void openSettingsActivity(){
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list);

        
        //get the password from the previous activity
        Intent starterIntent = getIntent();
		
		
		password = starterIntent.getCharArrayExtra(LoginActivity.LOGIN_PASSWORD);
		
		
        if (findViewById(R.id.contact_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ContactListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.contact_list))
                    .setActivateOnItemClick(true);
        }

        // TODO: If exposing deep links into your app, handle intents here.
    }

    /**
     * Callback method from {@link ContactListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(String id) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ContactDetailFragment.ARG_ITEM_ID, id);
            ContactDetailFragment fragment = new ContactDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.contact_detail_container, fragment)
                    .commit();
            
            Intent detailIntent = new Intent(this, ContactDetailActivity.class);
            detailIntent.putExtra(ContactDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
            

        } else {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ContactDetailActivity.class);
            detailIntent.putExtra(ContactDetailFragment.ARG_ITEM_ID, id);
            startActivity(detailIntent);
        }
    }
}
