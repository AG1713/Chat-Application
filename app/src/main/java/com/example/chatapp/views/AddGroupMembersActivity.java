package com.example.chatapp.views;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

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
import com.example.chatapp.repository.models.Group;
import com.example.chatapp.viewmodels.AddGroupMembersActivityViewModel;
import com.example.chatapp.views.adapter.AddUserAdapter;
import com.example.chatapp.databinding.ActivityAddGroupMembersBinding;

public class AddGroupMembersActivity extends AppCompatActivity {
    ActivityAddGroupMembersBinding binding;
    AddGroupMembersActivityViewModel viewModel;
    RecyclerView recyclerView;
    AddUserAdapter adapter;
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

        viewModel = new ViewModelProvider(this).get(AddGroupMembersActivityViewModel.class);

        recyclerView = binding.usersRecyclerView;
        chatRoomId = getIntent().getStringExtra("CHATROOM_ID");
        groupId = getIntent().getStringExtra("GROUP_ID");


        viewModel.getGroup(groupId).observe(this, group ->
                binding.btnSearchUser.setOnClickListener(v -> getUsers()));
        
    }

    public void getUsers(){
        adapter = new AddUserAdapter(viewModel.getUserOptions(binding.edtSearchUser.getText().toString()), this, viewModel,
                value -> {
                    if (value) binding.emptyTextView.setVisibility(View.VISIBLE);
                    else binding.emptyTextView.setVisibility(View.GONE);
                });
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.startListening();
    }


}