package com.example.chatapp.Views.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.Views.ChatActivity;
import com.example.chatapp.databinding.RecentChatRowBinding;
import com.example.chatapp.model.ChatRoom;
import com.example.chatapp.model.User;
import com.example.chatapp.viewModel.MyViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecentChatsAdapter extends FirestoreRecyclerAdapter<ChatRoom, RecentChatsAdapter.MyViewHolder> {
    Context context;
    MyViewModel myViewModel;

    public RecentChatsAdapter(@NonNull FirestoreRecyclerOptions<ChatRoom> options, Context context, MyViewModel myViewModel) {
        super(options);
        this.context = context;
        this.myViewModel = myViewModel;

        Log.d("ChatsFragment", "Number of chats: " + options.getSnapshots().size());
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull ChatRoom model) {
        holder.binding.setChatRoom(model);

        myViewModel.getOtherUserFromChatroom(model.getMembers(), task -> {
            if (task.isSuccessful() && task.getResult() != null){
                holder.binding.txtUsername.setText(task.getResult().toObject(User.class).getUsername());

                holder.binding.getRoot().setOnClickListener(v -> {
                    User user = task.getResult().toObject(User.class);
                    Intent i = new Intent(context, ChatActivity.class);
                    i.putExtra("USER_ID", user.getId());
                    i.putExtra("USERNAME", user.getUsername());
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);

                });

            } else {
                Log.d("ViewModel", "onBindViewHolder: Cannot get username I guess");
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecentChatRowBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.recent_chat_row,
                parent,
                false
        );

        return new MyViewHolder(binding);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RecentChatRowBinding binding;

        public MyViewHolder(RecentChatRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
