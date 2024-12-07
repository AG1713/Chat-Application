package com.example.chatapp.views;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatapp.Callbacks.CompletionCallback;
import com.example.chatapp.R;
import com.example.chatapp.repository.models.ChatRoom;
import com.example.chatapp.viewmodels.GroupInfoActivityViewModel;
import com.example.chatapp.databinding.ActivityGroupInfoBinding;
import com.example.chatapp.views.adapter.GroupMembersAdapter;

import java.io.IOException;
import java.io.InputStream;

public class GroupInfoActivity extends AppCompatActivity {
    ActivityGroupInfoBinding binding;
    GroupInfoActivityViewModel viewModel;
    RecyclerView recyclerView;
    GroupMembersAdapter adapter;
    ActivityResultLauncher<String> takePhoto;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(GroupInfoActivityViewModel.class);
        recyclerView = binding.memberList;

        String groupId = getIntent().getStringExtra("GROUP_ID");
        String chatRoomId = getIntent().getStringExtra("CHATROOM_ID");
        Log.d("GroupInfoActivity", "Group id : " + groupId);
        viewModel.getGroup(groupId).observe(this, group -> {
            if (group != null){
                ViewGroup.LayoutParams params = binding.groupPhoto.profilePicImageview.getLayoutParams();
                params.height = 300;
                params.width = 300;
                binding.groupPhoto.profilePicImageview.setLayoutParams(params);

                binding.setGroup(group);
                Glide.with(this)
                        .load(group.getGroupPhotoUrl())
                        .placeholder(R.drawable.baseline_person)
                        .error(R.drawable.baseline_person)
                        .apply(RequestOptions.circleCropTransform())
                        .into(binding.groupPhoto.profilePicImageview);
            }
            else {
                Log.d("GroupInfoActivity", "onCreate: " + " Group is null");
            }
        });

        viewModel.getChatRoom(groupId).observe(this, new Observer<ChatRoom>() {
            @Override
            public void onChanged(ChatRoom chatRoom) {
                if (chatRoom != null){
                    adapter = new GroupMembersAdapter(chatRoom.getMembers());
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(GroupInfoActivity.this));
                }
                else {
                    Log.d("GroupInfoActivity", "ChatRoom is null");
                }
            }
        });

        takePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        if (o != null) {
                            Log.d("ProfileFragment", "URI: " + o +
                                    "\ntrue?:" + o.getScheme().equals("content"));
                            imageUri = o;

                            ContentResolver resolver = getContentResolver();
                            try (InputStream inputStream = resolver.openInputStream(o)) {
                                if (inputStream != null) {
                                    Log.d("File Check", "File exists at URI.");
                                }
                            } catch (IOException e) {
                                Log.e("File Check", "File not found at URI.", e);
                            }

                            Glide.with(GroupInfoActivity.this)
                                    .load(o)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(binding.groupPhoto.profilePicImageview);

                            viewModel.updateGroupPhoto(groupId, o);
                        }
                    }
                }
        );

        binding.groupPhoto.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto.launch("image/*");
            }
        });

        binding.btnAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupInfoActivity.this, AddGroupMembersActivity.class);
                intent.putExtra("CHATROOM_ID", chatRoomId);
                intent.putExtra("GROUP_ID", groupId);
                startActivity(intent);
                finish();
            }
        });

        binding.btnLeaveGroup.setOnClickListener(v -> viewModel.leaveGroup(groupId, new CompletionCallback() {
            @Override
            public void onCallback() {
                Intent intent = new Intent(GroupInfoActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }));


    }
}