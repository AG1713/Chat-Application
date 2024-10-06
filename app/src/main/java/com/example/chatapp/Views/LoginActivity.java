package com.example.chatapp.Views;

import android.content.Intent;
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
import com.example.chatapp.viewModel.MyViewModel;
import com.example.chatapp.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    MyViewModel viewModel;
    ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding.setViewmodel(viewModel);

//        viewModel.signOut();

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (binding.edtEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(LoginActivity.this,
                            "Email cannot be empty",
                            Toast.LENGTH_SHORT).show();
                } else if (binding.edtPassword.getText().toString().contains(" ")) {
                    Toast.makeText(LoginActivity.this,
                            "Password cannot contain space",
                            Toast.LENGTH_SHORT).show();
                } else if (!binding.edtPassword.getText().toString().matches(".*[a-zA-z].*") ||
                        !binding.edtPassword.toString().matches(".*\\d.*")) {
                    Toast.makeText(LoginActivity.this,
                            "Passwords must contain letters and numbers",
                            Toast.LENGTH_SHORT).show();
                } else if (binding.edtPassword.getText().toString().length() < 7) {
                    Toast.makeText(LoginActivity.this,
                            "Password must be of at least 8 characters in length",
                            Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.signInVerifiedUser(binding.edtEmail.getText().toString(),
                            binding.edtPassword.getText().toString());
                }

            }
        });
    }



}