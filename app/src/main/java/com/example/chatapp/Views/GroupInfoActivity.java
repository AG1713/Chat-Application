package com.example.chatapp.Views;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityGroupInfoBinding;
import com.example.chatapp.model.Group;
import com.example.chatapp.viewModel.MyViewModel;

public class GroupInfoActivity extends AppCompatActivity {
    ActivityGroupInfoBinding binding;
    MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_group_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        String groupId = getIntent().getStringExtra("GROUP_ID");
        myViewModel.getGroup(groupId).observe(this, group -> {
            if (group != null){
                binding.setGroup(group);
            }
        });


    }
}