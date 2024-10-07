package com.example.chatapp.Views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.Views.adapter.AddUserAdapter;
import com.example.chatapp.databinding.ActivityAddGroupMembersBinding;
import com.example.chatapp.viewModel.MyViewModel;

public class AddGroupMembersActivity extends AppCompatActivity {
    ActivityAddGroupMembersBinding binding;
    MyViewModel myViewModel;
    RecyclerView recyclerView;
    AddUserAdapter adapter;
    ArrayAdapter<String> listAdapter;
    String chatRoomId;
    String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_group_members);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        recyclerView = binding.usersRecyclerView;
        chatRoomId = getIntent().getStringExtra("CHATROOM_ID");
        groupId = getIntent().getStringExtra("GROUP_ID");

        binding.btnSearchUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUsers();
            }
        });




    }


    public void getUsers(){
        adapter = new AddUserAdapter(myViewModel.getSpecifiedUser(binding.edtSearchUser.getText().toString()), this, myViewModel, chatRoomId, groupId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }


}