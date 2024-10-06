package com.example.chatapp.Views;

import android.content.Intent;
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
import com.example.chatapp.Views.adapter.ChatsAdapter;
import com.example.chatapp.databinding.ActivityChatBinding;
import com.example.chatapp.model.ChatRoom;
import com.example.chatapp.viewModel.MyViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private MyViewModel myViewModel;
    RecyclerView recyclerView;
    ChatsAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


        // Getting group name from the clicked item in GroupsActivity
        String groupId = getIntent().getStringExtra("GROUP_ID");
        String userId = getIntent().getStringExtra("USER_ID");
        String chatRoomId = "";


        if (userId != null){

            String currentUserId = myViewModel.getCurrentUserId();
            chatRoomId = myViewModel.getChatRoomIdForUsers(currentUserId, userId);
            String user = getIntent().getStringExtra("USERNAME");

            String roomId = chatRoomId;
            binding.title.setText(user);
            Log.d("ChatActivity", "onCreate: Running");

            myViewModel.getCurrentChatRoom(currentUserId, userId, new ArrayList<>(
                            Arrays.asList(currentUserId, userId)
                    ))
                    .observe(this, chatRoom -> {
                        if (chatRoom != null){
                            Log.d("ChatActivity", "onCreate: Chatroom not null");
                            adapter = new ChatsAdapter(myViewModel.getAllChats(roomId));
                            adapter.startListening();
                            recyclerView.setAdapter(adapter);

                            binding.sendBtn.setOnClickListener(v -> {
                                String messageText = binding.edittextChatMessage.getText().toString().trim();
                                Log.d("ChatActivity", "Sent message : " + messageText);

                                if (messageText.isEmpty()) return;

                                myViewModel.sendMessageToUser(messageText);
                                binding.edittextChatMessage.getText().clear();
                            });
                        }
                        else {
                            Log.d("ChatActivity", "onCreate: Chatroom is null?!");
                        }
                    });

        }
        else {
            // It is a group chat
            String groupName = getIntent().getStringExtra("GROUP_NAME");
            chatRoomId = getIntent().getStringExtra("CHATROOM_ID");


            binding.title.setText(groupName);
            String roomId = chatRoomId;
            myViewModel.getChatRoomForGroups(chatRoomId).observe(this, new Observer<ChatRoom>() {
                @Override
                public void onChanged(ChatRoom chatRoom) {
                    if (chatRoom != null){
                        Log.d("ChatActivity", "onCreate: Chatroom not null");
                        adapter = new ChatsAdapter(myViewModel.getAllChats(roomId));
                        adapter.startListening();
                        recyclerView.setAdapter(adapter);

                        binding.sendBtn.setOnClickListener(v -> {
                            String messageText = binding.edittextChatMessage.getText().toString().trim();
                            Log.d("ChatActivity", "Sent message : " + messageText);

                            if (messageText.isEmpty()) return;

                            myViewModel.sendMessageToUser(messageText);
                            binding.edittextChatMessage.getText().clear();
                        });
                    }
                }
            });

        }

        String newChatRoomId = chatRoomId;
        binding.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId != null){
                    Intent i = new Intent(ChatActivity.this, UserInfoActivity.class);
                    i.putExtra("CHATROOM_ID", newChatRoomId);
                    i.putExtra("USER_ID", userId);
                    startActivity(i);
                }
                else {
                    Intent i = new Intent(ChatActivity.this, GroupInfoActivity.class);
                    i.putExtra("CHATROOM_ID", newChatRoomId);
                    i.putExtra("GROUP_ID", groupId);
                    startActivity(i);
                }
            }
        });

    }



}