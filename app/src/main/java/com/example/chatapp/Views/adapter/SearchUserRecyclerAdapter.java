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
import com.example.chatapp.Views.ChatActivity;
import com.example.chatapp.databinding.SearchUserRecyclerRowBinding;
import com.example.chatapp.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<User, SearchUserRecyclerAdapter.UserModelHolder> {
    Context context;



    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelHolder holder, int position, @NonNull User model) {
        holder.binding.setUser(model);

        holder.binding.getRoot().setOnClickListener(v -> {
            Intent i = new Intent(context, ChatActivity.class);
            i.putExtra("USER_ID", holder.binding.getUser().getId());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);

            if (context instanceof Activity){
                ((Activity) context).finish();
                // The context passed to the adapter is of type Context, which is a broader class
                // than Activity. To ensure that you're using the correct Activity context
                // when calling finish(), you should cast the context to Activity before calling finish()
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
