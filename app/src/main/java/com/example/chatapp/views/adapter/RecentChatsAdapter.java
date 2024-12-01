package com.example.chatapp.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatapp.Callbacks.StoragePhotoUrlCallback;
import com.example.chatapp.R;
import com.example.chatapp.viewmodels.MainActivityViewModel;
import com.example.chatapp.views.ChatActivity;
import com.example.chatapp.databinding.RecentChatRowBinding;
import com.example.chatapp.repository.models.UserChat;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecentChatsAdapter extends FirestoreRecyclerAdapter<UserChat, RecentChatsAdapter.MyViewHolder> {
    Context context;
    MainActivityViewModel viewModel;

    public RecentChatsAdapter(@NonNull FirestoreRecyclerOptions<UserChat> options, MainActivityViewModel viewModel, Context context) {
        super(options);
        this.context = context;
        this.viewModel = viewModel;

        Log.d("RecentChatsAdapter", "Number of chats: " + options.getSnapshots().size());
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull UserChat model) {
        holder.binding.setUserChat(model);

        viewModel.getUserProfilePhotoUrl(model.getOtherUserId(), new StoragePhotoUrlCallback() {
            @Override
            public void onCallback(String Url) {
                Glide.with(holder.binding.getRoot().getContext())
                        .load(Url)
                        .placeholder(R.drawable.baseline_person)
                        .error(R.drawable.baseline_person)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.binding.otherUserProfilePhoto.profilePicImageview);
            }
        });



        /*
        TODO: Chatroom creation asynchronous. Make it such that the passing of intent happens
            after creation of chatroom
        */

        holder.binding.getRoot().setOnClickListener(v -> {
            Intent i = new Intent(context, ChatActivity.class);
            i.putExtra("USER_ID", model.getOtherUserId());
            i.putExtra("USER_NAME", model.getOtherUserName());
            i.putExtra("CHATROOM_ID", model.getChatRoomId());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
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
