package com.buyhatke.smshatke.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.buyhatke.smshatke.R;
import com.buyhatke.smshatke.models.SearchData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
    List<SearchData> data = Collections.emptyList();
    private Context context;
    private LayoutInflater inflater;
    private SearchViewHolder.ViewHolderClicks mListener;
    String mQuery = "";

    public SearchAdapter(Context context, List<SearchData> data, SearchViewHolder.ViewHolderClicks listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        mListener = listener;
    }

    public void setQuery(String query) {
        mQuery = query;
    }

    @Override
    public SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.list_item_search, parent, false);
        final SearchViewHolder holder = new SearchViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSearchClick(view, holder.getPosition());
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(SearchViewHolder holder, int position) {
        SearchData current = data.get(position);
        holder.name.setText(current.getAddress());

        holder.date.setText(current.getDate());

        if (mQuery != null) {

            // We need to make the search string bold within the full message
            // Get all of the start positions of the query within the messages
            ArrayList<Integer> indices = new ArrayList<>();
            int index = current.getBody().toLowerCase().indexOf(mQuery.toLowerCase());
            while (index >= 0) {
                indices.add(index);
                index = current.getBody().toLowerCase().indexOf(mQuery.toLowerCase(), index + 1);
            }

            // Make all instances of the search query bold
            SpannableStringBuilder sb = new SpannableStringBuilder(current.getBody());
            for (int i : indices) {
                ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent));
                sb.setSpan(span, i, i + mQuery.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }

            holder.snippet.setText(sb);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
