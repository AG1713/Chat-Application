package com.example.chatapp.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.RowChatBinding;
import com.example.chatapp.repository.models.Message;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.function.Consumer;

public class ChatsAdapter extends FirestoreRecyclerAdapter<Message, ChatsAdapter.MyViewHolder> {
    Consumer<Boolean> emptyOptionsCallback;

    public ChatsAdapter(@NonNull FirestoreRecyclerOptions<Message> options, Consumer<Boolean> emptyOptionsCallback) {
        super(options);
        this.emptyOptionsCallback = emptyOptionsCallback;
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

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        if (getItemCount() <= 0) emptyOptionsCallback.accept(true);
        else emptyOptionsCallback.accept(false);
    }
}
