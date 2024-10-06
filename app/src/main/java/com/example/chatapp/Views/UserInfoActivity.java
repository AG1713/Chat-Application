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
import com.example.chatapp.databinding.ActivityUserInfoBinding;
import com.example.chatapp.model.User;
import com.example.chatapp.viewModel.MyViewModel;

public class UserInfoActivity extends AppCompatActivity {
    ActivityUserInfoBinding binding;
    MyViewModel myViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_user_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);


        String userId = getIntent().getStringExtra("USER_ID");

        myViewModel.getUser(userId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    binding.setUser(user);
                }
            }
        });


    }
}