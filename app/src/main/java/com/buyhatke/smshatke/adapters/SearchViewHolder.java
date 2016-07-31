package com.buyhatke.smshatke.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.buyhatke.smshatke.R;


public class SearchViewHolder extends RecyclerView.ViewHolder {

    protected View root;
    protected TextView name;
    protected TextView date;
    protected TextView snippet;

    public SearchViewHolder(View view) {
        super(view);
        root = view;
        name = (TextView) view.findViewById(R.id.search_name);
        date = (TextView) view.findViewById(R.id.search_date);
        snippet = (TextView) view.findViewById(R.id.search_snippet);
    }

    public interface ViewHolderClicks {
        void onSearchClick(View v, int position);
    }
}
