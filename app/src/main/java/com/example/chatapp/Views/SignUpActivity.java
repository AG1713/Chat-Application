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
import com.example.chatapp.databinding.ActivitySignUpBinding;
import com.example.chatapp.model.User;
import com.example.chatapp.viewModel.MyViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    MyViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding.setViewModel(viewModel);

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.edtEmail.toString().trim().isEmpty()) {
                    Toast.makeText(SignUpActivity.this,
                            "Email cannot be empty",
                            Toast.LENGTH_SHORT).show();
                } else if (binding.edtPassword.getText().toString().contains(" ")) {
                    Toast.makeText(SignUpActivity.this,
                            "Password cannot contain space",
                            Toast.LENGTH_SHORT).show();
                } else if (!binding.edtPassword.getText().toString().matches(".*[a-zA-z].*") ||
                        !binding.edtPassword.toString().matches(".*\\d.*")) {
                    Toast.makeText(SignUpActivity.this,
                            "Passwords must contain letters and numbers",
                            Toast.LENGTH_SHORT).show();
                } else if (binding.edtPassword.getText().toString().length() < 7) {
                    Toast.makeText(SignUpActivity.this,
                            "Password must be of at least 8 characters in length",
                            Toast.LENGTH_SHORT).show();
                } else if (binding.edtUsername.getText().toString().trim().length() < 3) {
                    Toast.makeText(SignUpActivity.this,
                            "Username must be of at least 4 characters in length",
                            Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.createVerifiedUser(binding.edtEmail.getText().toString(),
                            binding.edtPassword.getText().toString(),
                            new User(
                                    binding.edtUsername.getText().toString(),
                                    binding.edtDescription.getText().toString(),
                                    binding.edtEmail.getText().toString()
                            )
                            ,SignUpActivity.this);
                }
            }
        });

    }



}