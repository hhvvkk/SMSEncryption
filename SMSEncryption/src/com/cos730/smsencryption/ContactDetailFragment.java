package com.cos730.smsencryption;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cos730.database.Contact;
import com.cos730.database.ContactContent;
import com.cos730.encryption.Charset;


public class ContactDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    public static Context context;
    
    private TextView messageLengthDisplay = null;

    private ContactContent.ContactItem contactItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
        	ContactContent cc = new ContactContent(getActivity().getApplicationContext());
            contactItem = cc.getItemMap().get(getArguments().getString(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_contact_detail, container, false);

        if (contactItem != null) {
            ((TextView) rootView.findViewById(R.id.contact_detail)).setText(contactItem.name);
        }
        

        messageLengthDisplay = (TextView)rootView.findViewById(R.id.textViewMessageLength);
        EditText theMessageEditText = (EditText)rootView.findViewById(R.id.textMultiLineMessage);
        
        final TextWatcher txwatcher = new TextWatcher() {
        	   public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        	   }

        	   public void onTextChanged(CharSequence s, int start, int before, int count) {
        		   messageLengthDisplay.setText(getString(R.string.messageLengthTextView)+String.valueOf(s.length()));
        	   }
 
        	   public void afterTextChanged(Editable s) {
        	   }
        };

        theMessageEditText.addTextChangedListener(txwatcher);
        
        return rootView;
    }
   
}
