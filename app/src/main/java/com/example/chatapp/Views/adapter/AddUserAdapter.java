package com.example.chatapp.Views.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.Views.AddGroupMembersActivity;
import com.example.chatapp.Views.ChatActivity;
import com.example.chatapp.databinding.SearchUserRecyclerRowBinding;
import com.example.chatapp.model.User;
import com.example.chatapp.viewModel.MyViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class AddUserAdapter extends FirestoreRecyclerAdapter<User, AddUserAdapter.UserModelHolder> {
    Context context;
    MyViewModel myViewModel;
    String chatRoomId;
    String groupId;


    public AddUserAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context, MyViewModel myViewModel, String chatRoomId, String groupId) {
        super(options);
        this.context = context;
        this.myViewModel = myViewModel;
        this.chatRoomId = chatRoomId;
        this.groupId = groupId;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelHolder holder, int position, @NonNull User model) {
        holder.binding.setUser(model);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<String> members = new ArrayList<>();
                members.add(model.getId());

                myViewModel.addGroupMembers(chatRoomId, groupId, members);
                if (context instanceof Activity){
                    ((Activity) context).finish();
                }

            }
        });

    }

    @NonNull
    @Override
    public UserModelHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SearchUserRecyclerRowBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.search_user_recycler_row,
                parent,
                false
        );

        return new UserModelHolder(binding);
    }

    class UserModelHolder extends RecyclerView.ViewHolder{
        SearchUserRecyclerRowBinding binding;

        public UserModelHolder(SearchUserRecyclerRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }



}
