package com.example.chatapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.repository.models.ChatRoom;
import com.example.chatapp.viewmodels.GroupInfoActivityViewModel;
import com.example.chatapp.databinding.ActivityGroupInfoBinding;
import com.example.chatapp.views.adapter.GroupMembersAdapter;

public class GroupInfoActivity extends AppCompatActivity {
    ActivityGroupInfoBinding binding;
    GroupInfoActivityViewModel viewModel;
    RecyclerView recyclerView;
    GroupMembersAdapter adapter;

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
                binding.setGroup(group);
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


    }
}