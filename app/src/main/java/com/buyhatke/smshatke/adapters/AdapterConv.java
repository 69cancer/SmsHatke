package com.buyhatke.smshatke.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.buyhatke.smshatke.R;
import com.buyhatke.smshatke.models.Conversation;

import java.util.Collections;
import java.util.List;

/**
 * Created by ssaxena on 7/29/16.
 */
public class AdapterConv extends RecyclerView.Adapter<ConversationHolder> {
    List<Conversation> data = Collections.emptyList();
    private Context context;
    private LayoutInflater inflater;
    ConversationHolder.ViewHolderClicks mListener;

    public AdapterConv(Context context, List<Conversation> data, ConversationHolder.ViewHolderClicks listener) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        mListener = listener;
    }

    @Override
    public ConversationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_item_conversation, parent, false);
        final ConversationHolder holder = new ConversationHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLayout(v, holder.getPosition());
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ConversationHolder holder, int position) {
        Conversation current = data.get(position);
        holder.snippet.setText(current.getSnippet());
        holder.name.setText(current.getAddress());
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color1 = generator.getRandomColor();
        TextDrawable.IBuilder builder = TextDrawable.builder()
                .beginConfig()
                .withBorder(4)
                .endConfig()
                .rect();
        try {
            TextDrawable ic1 = builder.build(current.getAddress().charAt(0) + "", color1);
            holder.avatar.setImageDrawable(ic1);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            TextDrawable ic1 = builder.build("X", color1);
            holder.avatar.setImageDrawable(ic1);
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

}
