package com.example.chatapp.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.example.chatapp.R;
import com.example.chatapp.databinding.ActivityCreateGroupBinding;
import com.example.chatapp.viewModel.MyViewModel;

import java.util.ArrayList;

public class CreateGroupActivity extends AppCompatActivity {
    ActivityCreateGroupBinding binding;
    MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_group);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        binding.btnCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupName = binding.edtGroupName.getText().toString().trim();
                String groupDescription = binding.edtGroupDescription.getText().toString().trim();

                if (groupName.isEmpty()){
                    Toast.makeText(CreateGroupActivity.this, "Group Name cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (groupDescription.isEmpty()) {
                    Toast.makeText(CreateGroupActivity.this, "Group Description cannot be empty", Toast.LENGTH_SHORT).show();
                } else if (groupName.length() < 3) {
                    Toast.makeText(CreateGroupActivity.this, "Group Name too short", Toast.LENGTH_SHORT).show();
                } else {
                    ArrayList<String> members = new ArrayList<>();
                    members.add(myViewModel.getCurrentUserId());
                    myViewModel.getOrCreateGroup(groupName, groupDescription, members);

                    // TODO: Add an intent here
                    finish();
                }


            }
        });



    }
}