package com.buyhatke.smshatke;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.buyhatke.smshatke.adapters.AdapterConv;
import com.buyhatke.smshatke.adapters.ConversationHolder;
import com.buyhatke.smshatke.models.Conversation;
import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class ConversationsFragment extends Fragment {

    private RecyclerView rvConv;
    private RecyclerView.Adapter adapterConv;
    private RecyclerView.LayoutManager lmConv;
    List<Conversation> listConv;
    private ContentResolver cr;
    private Cursor mCursor;
    private Uri uriSms = Uri.parse("content://sms/");
    private ChangeAcivitiy changeAct;
    private ChangeFragment changeFrag;
    private Uri uriConv;
    private Context mContext;
    private FloatingActionButton fab;
    private LinearLayout noMessageLayout;

    public ConversationsFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listConv = new ArrayList<>();
        uriConv = Uri.parse("content://sms/conversations");
        cr = getActivity().getContentResolver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mContext = getActivity();
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        rvConv = (RecyclerView) view.findViewById(R.id.conversations_list);
        rvConv.setHasFixedSize(true);
        lmConv = new LinearLayoutManager(getActivity());
        noMessageLayout = (LinearLayout) view.findViewById(R.id.empty_state);
        updateConvList();
        adapterConv = new AdapterConv(getActivity(), listConv, new ConversationHolder.ViewHolderClicks() {
            @Override
            public void onLayout(View v, int position) {
                changeAct.changeAcitivity(listConv.get(position).getThread_id(), listConv.get(position).getAddress());
            }
        });
        rvConv.setLayoutManager(lmConv);
        rvConv.setAdapter(adapterConv);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.attachToRecyclerView(rvConv);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeFrag.changeFrag();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        changeAct = (ChangeAcivitiy) getActivity();
        changeFrag = (ChangeFragment) getActivity();
    }


    public interface ChangeAcivitiy {
        void changeAcitivity(String threadID, String address);
    }

    interface ChangeFragment {
        void changeFrag();
    }

    void updateConvList() {
        new SnippetLoader().execute(uriConv);
    }


    class SnippetLoader extends AsyncTask<Uri, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
            // showDialog();
        }


        @Override
        protected String doInBackground(Uri... params) {
            if (params[0].equals(uriConv)) {
                mCursor = cr.query(params[0], null, null, null, "Date DESC");

                try {
                    mCursor.moveToPosition(-1);
                    while (mCursor.moveToNext()) {
                        Conversation conv = new Conversation();
                        conv.setMessage_count(mCursor.getString(mCursor.getColumnIndex("msg_count")));
                        conv.setSnippet(mCursor.getString(mCursor.getColumnIndex("snippet")));
                        conv.setThread_id(mCursor.getString(mCursor.getColumnIndex("thread_id")));
                        listConv.add(conv);
                    }
                } finally {
                    mCursor.close();
                }
                return "uriConv";
            } else {
                mCursor = cr.query(params[0], null, null, null, "Date DESC");
                for (int i = 0; i < listConv.size(); i++) {
                    mCursor = cr.query(uriSms, new String[]{"address"}, "thread_id" + " = ?", new String[]{listConv.get(i).getThread_id()}, null);
                    try {
                        mCursor.moveToPosition(-1);
                        while (mCursor.moveToNext()) {
                            listConv.get(i).setAddress(mCursor.getString(mCursor.getColumnIndex("address")));
                        }
                    } finally {
                        mCursor.close();
                    }
                }
                return "uriSms";
            }


        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equalsIgnoreCase("uriConv"))
                new SnippetLoader().execute(uriSms);
            else {
                adapterConv.notifyDataSetChanged();
                if (listConv.size() == 0) {
                    noMessageLayout.setVisibility(View.VISIBLE);
                }
            }
        }


    }
}
