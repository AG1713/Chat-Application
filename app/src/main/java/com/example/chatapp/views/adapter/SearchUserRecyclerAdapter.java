package com.example.chatapp.views.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatapp.Callbacks.FireStoreChatRoomIdCallback;
import com.example.chatapp.R;
import com.example.chatapp.viewmodels.SearchUserActivityViewModel;
import com.example.chatapp.views.ChatActivity;
import com.example.chatapp.databinding.SearchUserRecyclerRowBinding;
import com.example.chatapp.repository.models.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<User, SearchUserRecyclerAdapter.UserModelHolder> {
    Context context;
    SearchUserActivityViewModel viewModel;
    boolean clickable; // Do pause the click listener when one of the item is clicked

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context, SearchUserActivityViewModel viewModel) {
        super(options);
        this.context = context;
        this.viewModel = viewModel;
        this.clickable = true;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelHolder holder, int position, @NonNull User model) {
        holder.binding.setUser(model);
        Glide.with(holder.binding.getRoot().getContext())
                .load(model.getProfilePhotoUrl())
                .placeholder(R.drawable.baseline_person)
                .error(R.drawable.baseline_person)
                .apply(RequestOptions.circleCropTransform())
                .into(holder.binding.otherUserProfilePhoto.profilePicImageview);

        holder.binding.getRoot().setOnClickListener(v -> {
            if (clickable){
                this.clickable = false;
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("USER_ID", model.getId());
                i.putExtra("USER_NAME", model.getUsername());

                viewModel.createChatRoomForUsers(model.getId(), model.getUsername(), new FireStoreChatRoomIdCallback() {
                    @Override
                    public void onCallback(String chatRoomId) {
                        i.putExtra("CHATROOM_ID", chatRoomId);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);

                        if (context instanceof Activity) {
                            ((Activity) context).finish();
                            // The context passed to the adapter is of type Context, which is a broader class
                            // than Activity. To ensure that you're using the correct Activity context
                            // when calling finish(), you should cast the context to Activity before calling finish()
                        }
                    }
                });


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
