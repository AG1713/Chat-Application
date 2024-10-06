package com.example.chatapp.Views.adapter;

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
import com.example.chatapp.callbacks.FireStoreCallBack;
import com.example.chatapp.databinding.RecentGroupsRowBinding;
import com.example.chatapp.model.ChatRoom;
import com.example.chatapp.model.Group;
import com.example.chatapp.viewModel.MyViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class RecentGroupsAdapter extends FirestoreRecyclerAdapter<Group, RecentGroupsAdapter.MyViewHolder> {

    Context context;
    MyViewModel myViewModel;

    public RecentGroupsAdapter(@NonNull FirestoreRecyclerOptions<Group> options, Context context, MyViewModel myViewModel) {
        super(options);
        this.context = context;
        this.myViewModel = myViewModel;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Group model) {
        holder.binding.setGroup(model);

        Log.d("Adapter", "Group Model: " + model.getGroupName());

        myViewModel.getChatRoomForGroups(model.getChatRoomId(), documentReference ->
                documentReference.get().addOnSuccessListener(documentSnapshot ->
                        holder.binding.setChatRoom(documentSnapshot.toObject(ChatRoom.class))));

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("GROUP_ID", model.getId());
                i.putExtra("GROUP_NAME", model.getGroupName());
                i.putExtra("CHATROOM_ID", model.getChatRoomId());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecentGroupsRowBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.recent_groups_row,
                parent,
                false
        );

        return new MyViewHolder(binding);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RecentGroupsRowBinding binding;

        public MyViewHolder(RecentGroupsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }


}
