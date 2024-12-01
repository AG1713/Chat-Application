package com.example.chatapp.views;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatapp.Callbacks.FireStoreChatRoomIdCallback;
import com.example.chatapp.R;
import com.example.chatapp.viewmodels.CreateGroupActivityViewModel;
import com.example.chatapp.databinding.ActivityCreateGroupBinding;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CreateGroupActivity extends AppCompatActivity {
    ActivityCreateGroupBinding binding;
    CreateGroupActivityViewModel viewModel;
    SharedPreferences pref;
    ActivityResultLauncher<String> takePhoto;
    Uri imageUri;
    boolean photoEdited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_group);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pref = getSharedPreferences("User", MODE_PRIVATE);
        String userId = pref.getString("User_id", null);
        viewModel = new ViewModelProvider(this).get(CreateGroupActivityViewModel.class);

        takePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
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

                        Glide.with(CreateGroupActivity.this)
                                .load(o)
                                .apply(RequestOptions.circleCropTransform())
                                .into(binding.groupPhoto.profilePicImageview);

                    }
                }
        );
        photoEdited = false;

        binding.groupPhoto.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto.launch("image/*");
                photoEdited = true;
            }
        });

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
                    members.add(userId);

                    Intent i = new Intent(CreateGroupActivity.this, ChatActivity.class);
                    i.putExtra("GROUP_NAME", groupName);

                    viewModel.createChatRoomForGroups(groupName, groupDescription, members, new FireStoreChatRoomIdCallback() {
                        @Override
                        public void onCallback(String chatRoomId) {
                            i.putExtra("GROUP_ID", chatRoomId);
                            // NOTE: The above is because currently groupId and chatRoomId are same
                            //       Might change later.

                            i.putExtra("CHATROOM_ID", chatRoomId);
                            Log.d("CreateGroupActivity", "ChatRoomId : " + chatRoomId);
                            startActivity(i);
                            finish();
                        }
                    });


                    // TODO: Add an intent here

                }


            }
        });



    }
}