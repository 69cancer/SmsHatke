package com.buyhatke.smshatke.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.buyhatke.smshatke.R;

/**
 * Created by ssaxena on 7/30/16.
 */
public class ConversationHolder extends RecyclerView.ViewHolder {
    ImageView avatar;
    TextView name;
    TextView date;
    TextView snippet;
    ImageView badge;

    public ConversationHolder(View itemView) {
        super(itemView);
        avatar = (ImageView) itemView.findViewById(R.id.avatar);
        name = (TextView) itemView.findViewById(R.id.name);
        date = (TextView) itemView.findViewById(R.id.date);
        snippet = (TextView) itemView.findViewById(R.id.snippet);
        badge = (ImageView) itemView.findViewById(R.id.unread);
    }

    public interface ViewHolderClicks {
        void onLayout(View v, int position);
    }
}

