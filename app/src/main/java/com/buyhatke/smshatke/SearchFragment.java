package com.buyhatke.smshatke;


import android.annotation.TargetApi;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.buyhatke.smshatke.adapters.SearchAdapter;
import com.buyhatke.smshatke.adapters.SearchViewHolder;
import com.buyhatke.smshatke.models.SearchData;
import com.buyhatke.smshatke.utils.KeyboardUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private EditText mQuery;
    private Context mContext;
    private RecyclerView mRecyclerView;
    private SearchAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private String mSearchString;
    ConversationsFragment.ChangeAcivitiy changeActivity;
    List<SearchData> searchDataList = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        mContext = getActivity();
        mQuery = (EditText) view.findViewById(R.id.search_query);
        changeActivity = (ConversationsFragment.ChangeAcivitiy) mContext;

        mQuery.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mSearchString = mQuery.getText().toString();
                    query();

                    // Hide the keyboard when the user makes a query
                    mQuery.clearFocus();
                    KeyboardUtils.hide(mContext, mQuery);
                    return true;
                }
                return false;
            }
        });

        mLayoutManager = new LinearLayoutManager(mContext);
        mAdapter = new SearchAdapter(mContext, searchDataList, new SearchViewHolder.ViewHolderClicks() {
            @Override
            public void onSearchClick(View v, int position) {
                changeActivity.changeAcitivity(searchDataList.get(position).getThreadId(), searchDataList.get(position).getAddress());
            }
        });

        mRecyclerView = (RecyclerView) view.findViewById(R.id.search_list);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void query() {
        mAdapter.setQuery(mSearchString);
        searchDataList.clear();
        Uri uri = Telephony.MmsSms.SEARCH_URI.buildUpon().appendQueryParameter("pattern", mSearchString).build();
        Cursor mCursor = mContext.getContentResolver().query(uri, null, null, null, null);
        try {
            mCursor.moveToPosition(-1);
            while (mCursor.moveToNext()) {
                SearchData searchData = new SearchData();
                searchData.setAddress(mCursor.getString(mCursor.getColumnIndex("address")));
                searchData.setBody(mCursor.getString(mCursor.getColumnIndex("body")));
                searchData.setThreadId(mCursor.getString(mCursor.getColumnIndex("thread_id")));
                Date date = new Date(Long.valueOf(mCursor.getString(mCursor.getColumnIndex("date"))));
                String formattedDate = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a").format(date);
                searchData.setDate(formattedDate);
                searchDataList.add(searchData);
            }

        } finally {
            mCursor.close();
        }
        mAdapter.notifyDataSetChanged();

    }

}
