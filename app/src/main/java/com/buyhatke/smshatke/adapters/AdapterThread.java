package com.buyhatke.smshatke.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buyhatke.smshatke.R;
import com.buyhatke.smshatke.models.Message;

import java.util.Collections;
import java.util.List;

/**
 * Created by ssaxena on 7/30/16.
 */
public class AdapterThread extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Message> data = Collections.emptyList();
    private Context context;
    private LayoutInflater inflater;
    private static final int TYPE_IN = 0;
    private static final int TYPE_OUT = 1;

    public AdapterThread(Context context, List<Message> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_IN) {
            View view = inflater.inflate(R.layout.list_item_thread_in, parent, false);
            ThreadHolderIn holder = new ThreadHolderIn(view);
            return holder;
        } else {
            View view = inflater.inflate(R.layout.list_item_thread_out, parent, false);
            ThreadHolderOut holder = new ThreadHolderOut(view);
            return holder;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ThreadHolderIn) {
            ThreadHolderIn threadHolderIn = (ThreadHolderIn) holder;
            Message current = data.get(position);
            threadHolderIn.body.setText(current.getBody());
            threadHolderIn.date.setText(current.getDate());
        } else {
            ThreadHolderOut threadHolderOut = (ThreadHolderOut) holder;
            Message current = data.get(position);
            threadHolderOut.body.setText(current.getBody());
            threadHolderOut.date.setText(current.getDate());
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        Message current = data.get(position);
        if (current.getType().equalsIgnoreCase("1"))
            return TYPE_IN;
        else {
            return TYPE_OUT;
        }
    }

    public class ThreadHolderIn extends RecyclerView.ViewHolder {
        TextView body;
        TextView date;

        public ThreadHolderIn(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body_in);
            date = (TextView) itemView.findViewById(R.id.date_in);
        }
    }

    public class ThreadHolderOut extends RecyclerView.ViewHolder {
        TextView body;
        TextView date;

        public ThreadHolderOut(View itemView) {
            super(itemView);
            body = (TextView) itemView.findViewById(R.id.body_out);
            date = (TextView) itemView.findViewById(R.id.date_out);
        }
    }


}
