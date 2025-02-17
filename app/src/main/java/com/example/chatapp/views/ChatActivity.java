package com.example.chatapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.viewmodels.ChatActivityViewModel;
import com.example.chatapp.views.adapter.ChatsAdapter;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.example.chatapp.repository.models.ChatRoom;

import java.util.function.Consumer;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private ChatActivityViewModel viewModel;
    RecyclerView recyclerView;
    ChatsAdapter adapter;
    SharedPreferences pref;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        pref = getSharedPreferences("User", MODE_PRIVATE);
        Log.d("ChatActivity", pref.getString("User_id", null) + " " + pref.getString("User_name", null));
        viewModel = new ViewModelProvider(this).get(ChatActivityViewModel.class);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        // Getting group name from the clicked item in GroupsActivity
        String userId = getIntent().getStringExtra("USER_ID");
        String userName = getIntent().getStringExtra("USER_NAME");
        String groupId = getIntent().getStringExtra("GROUP_ID");
        String groupName = getIntent().getStringExtra("GROUP_NAME");
        String chatRoomId = getIntent().getStringExtra("CHATROOM_ID");
        Log.d("ChatActivity", "OtherUser : " + userId);

        if (userId != null){
            binding.title.setText(userName);

            viewModel.setCurrentChatRoom(chatRoomId)
                    .observe(this, chatRoom -> {
                        if (chatRoom != null){
                            Log.d("ChatActivity", "onCreate: Chatroom not null");
                            adapter = new ChatsAdapter(viewModel.getAllChats(),
                                    value -> {
                                        if (value) binding.emptyTextView.setVisibility(View.VISIBLE);
                                        else binding.emptyTextView.setVisibility(View.GONE);
                                    });
                            adapter.startListening();
                            recyclerView.setAdapter(adapter);

                            binding.sendBtn.setOnClickListener(v -> {
                                String messageText = binding.edittextChatMessage.getText().toString().trim();
                                Log.d("ChatActivity", "Sent message : " + messageText);

                                if (messageText.isEmpty()) return;

                                viewModel.sendMessageToUser(pref.getString("User_name", null), messageText);
                                binding.edittextChatMessage.getText().clear();
                            });
                        }
                        else {
                            Log.d("ChatActivity", "onCreate: Chatroom is null?!");
                        }
                    });

        }
        else {
            binding.title.setText(groupName);

            Log.d("ChatActivity", "Group id : " + groupId);

            viewModel.setCurrentChatRoom(chatRoomId).observe(this, new Observer<ChatRoom>() {
                @Override
                public void onChanged(ChatRoom chatRoom) {
                    if (chatRoom != null){
                        Log.d("ChatActivity", "onCreate: Chatroom not null");
                        adapter = new ChatsAdapter(viewModel.getAllChats(),
                                value -> {
                                    if (value) binding.emptyTextView.setVisibility(View.VISIBLE);
                                    else binding.emptyTextView.setVisibility(View.GONE);
                                });
                        adapter.startListening();
                        recyclerView.setAdapter(adapter);

                        binding.sendBtn.setOnClickListener(v -> {
                            String messageText = binding.edittextChatMessage.getText().toString().trim();
                            Log.d("ChatActivity", "Sent message : " + messageText);

                            if (messageText.isEmpty()) return;

                            viewModel.sendMessageToGroup(pref.getString("User_name", null), messageText);
                            binding.edittextChatMessage.getText().clear();
                        });
                    }
                    else {
                        Log.d("ChatActivity", "onCreate: Chatroom is null?!");
                    }
                }
            });

        }

        binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != null){
                    Intent i = new Intent(ChatActivity.this, UserInfoActivity.class);
                    i.putExtra("CHATROOM_ID", chatRoomId);
                    i.putExtra("USER_ID", userId);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(ChatActivity.this, GroupInfoActivity.class);
                    i.putExtra("CHATROOM_ID", chatRoomId);
                    i.putExtra("GROUP_ID", groupId);
                    startActivity(i);
                }
            }
        });

    }



}