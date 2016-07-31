package com.buyhatke.smshatke;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.melnykov.fab.FloatingActionButton;


public class ComposeFragment extends Fragment {
    private EditText etAddresses;
    private EditText message;
    private FloatingActionButton fab;
    private RelativeLayout mLayout;
    private String[] addresses;
    private Deliver deliver;

    public ComposeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_compose, container, false);
        etAddresses = (EditText) view.findViewById(R.id.compose_recipients);
        etAddresses.requestFocus();
        message = (EditText) view.findViewById(R.id.messageEdit);
        deliver = (Deliver) getActivity();
        fab = (FloatingActionButton) view.findViewById(R.id.msgSendButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
        mLayout = (RelativeLayout) view.findViewById(R.id.compose_fragment_root);
        return view;
    }

    private void sendMessage() {
        String msg = message.getText().toString();
        if (!msg.trim().equalsIgnoreCase("") && checkContact()) {
            deliver.deliverMsg(addresses, msg);
        } else {
            Snackbar.make(mLayout,
                    "Enter a number and a message",
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private boolean checkContact() {
        addresses = etAddresses.getText().toString().trim().split(",");
        if (addresses[0].equalsIgnoreCase(""))
            return false;
        return true;
    }

    interface Deliver {
        void deliverMsg(String[] addresses, String msg);
    }


}
