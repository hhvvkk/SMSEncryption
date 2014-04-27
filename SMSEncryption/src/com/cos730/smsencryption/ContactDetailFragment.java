package com.cos730.smsencryption;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cos730.database.ContactContent;


public class ContactDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";

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
        

        return rootView;
    }
}
