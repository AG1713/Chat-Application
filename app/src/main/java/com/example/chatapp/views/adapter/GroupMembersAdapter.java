package com.example.chatapp.views.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.databinding.GroupMemberBinding;
import com.example.chatapp.repository.models.Member;

import java.util.ArrayList;

public class GroupMembersAdapter extends RecyclerView.Adapter<GroupMembersAdapter.MyViewHolder> {
    ArrayList<Member> members;

    public GroupMembersAdapter(ArrayList<Member> members) {
        this.members = members;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupMemberBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.group_member,
                parent,
                false
        );

        return new MyViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.setMember(members.get(position));
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        GroupMemberBinding binding;


        public MyViewHolder(@NonNull View itemView, GroupMemberBinding binding) {
            super(itemView);
            this.binding = binding;
        }
    }


}
