package com.example.chatapp.Views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.RowChatBinding;
import com.example.chatapp.model.Message;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ChatsAdapter extends FirestoreRecyclerAdapter<Message, ChatsAdapter.MyViewHolder> {


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ChatsAdapter(@NonNull FirestoreRecyclerOptions<Message> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Message model) {
        holder.binding.setMessage(model);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowChatBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.row_chat,
                parent,
                false
        );

        return new MyViewHolder(binding.getRoot(), binding);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        RowChatBinding binding;

        public MyViewHolder(@NonNull View itemView, RowChatBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }


}