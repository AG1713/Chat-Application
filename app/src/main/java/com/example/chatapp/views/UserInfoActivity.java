package com.example.chatapp.views;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatapp.R;
import com.example.chatapp.viewmodels.UserInfoActivityViewModel;
import com.example.chatapp.databinding.ActivityUserInfoBinding;
import com.example.chatapp.repository.models.User;

public class UserInfoActivity extends AppCompatActivity {
    ActivityUserInfoBinding binding;
    UserInfoActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        viewModel = new ViewModelProvider(this).get(UserInfoActivityViewModel.class);

        ViewGroup.LayoutParams params = binding.userProfilePicture.profilePicImageview.getLayoutParams();
        params.height = 300;
        params.width = 300;
        binding.userProfilePicture.profilePicImageview.setLayoutParams(params);

        String userId = getIntent().getStringExtra("USER_ID");
        Log.d("UserInfoActivity", "User : " + userId);

        viewModel.getUser(userId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    binding.setUser(user);
                    Glide.with(UserInfoActivity.this)
                            .load(user.getProfilePhotoUrl())
                            .placeholder(R.drawable.baseline_person)
                            .error(R.drawable.baseline_person)
                            .apply(RequestOptions.circleCropTransform())
                            .into(binding.userProfilePicture.profilePicImageview);
                }
            }
        });


    }
}