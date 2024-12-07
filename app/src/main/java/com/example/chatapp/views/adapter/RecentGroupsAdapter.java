package com.example.chatapp.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatapp.Callbacks.StoragePhotoUrlCallback;
import com.example.chatapp.R;
import com.example.chatapp.repository.models.UserGroup;
import com.example.chatapp.viewmodels.MainActivityViewModel;
import com.example.chatapp.views.ChatActivity;
import com.example.chatapp.databinding.RecentGroupsRowBinding;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RecentGroupsAdapter extends FirestoreRecyclerAdapter<UserGroup, RecentGroupsAdapter.MyViewHolder> {
    Context context;
    MainActivityViewModel viewModel;

    public RecentGroupsAdapter(@NonNull FirestoreRecyclerOptions<UserGroup> options, Context context, MainActivityViewModel viewModel) {
        super(options);
        this.context = context;
        this.viewModel = viewModel;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull UserGroup model) {
        holder.binding.setUserGroup(model);

        viewModel.getGroupPhotoUrl(model.getGroupId(), new StoragePhotoUrlCallback() {
            @Override
            public void onCallback(String Url) {
                Glide.with(holder.binding.getRoot().getContext())
                        .load(Url)
                        .placeholder(R.drawable.baseline_person)
                        .error(R.drawable.baseline_person)
                        .apply(RequestOptions.circleCropTransform())
                        .into(holder.binding.groupPhoto.profilePicImageview);
            }
        });

        Log.d("Adapter", "Group Model: " + model.getGroupName());

        // NOTE: Since in this app for now the groupId and the chatRoomId is intentionally kept same
        //       for simplicity, we are using groupId as chatRoomId.
        //       It might later be changed, when it does, it needs to reflect here

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ChatActivity.class);
                i.putExtra("GROUP_ID", model.getGroupId());
                i.putExtra("GROUP_NAME", model.getGroupName());
                i.putExtra("CHATROOM_ID", model.getGroupId());
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
