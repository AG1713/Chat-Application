package com.example.chatapp.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.splashscreen.SplashScreen;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.chatapp.R;
import com.example.chatapp.viewmodels.SignInActivityViewModel;
import com.example.chatapp.databinding.ActivityLoginBinding;

public class SignInActivity extends AppCompatActivity {
    SignInActivityViewModel viewModel;
    ActivityLoginBinding binding;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen.installSplashScreen(this); // Enabling Splash Screen
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(SignInActivityViewModel.class);
        pref = getSharedPreferences("User", MODE_PRIVATE);
        String userId = pref.getString("User_id", null);
        Log.d("SignInActivity", pref.getString("User_id", null) + " " + pref.getString("User_name", null));

        if (userId != null){
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });

        binding.btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (binding.edtEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(SignInActivity.this,
                            "Email cannot be empty",
                            Toast.LENGTH_SHORT).show();
                } else if (binding.edtPassword.getText().toString().contains(" ")) {
                    Toast.makeText(SignInActivity.this,
                            "Password cannot contain space",
                            Toast.LENGTH_SHORT).show();
                } else if (!binding.edtPassword.getText().toString().matches(".*[a-zA-z].*") ||
                        !binding.edtPassword.toString().matches(".*\\d.*")) {
                    Toast.makeText(SignInActivity.this,
                            "Passwords must contain letters and numbers",
                            Toast.LENGTH_SHORT).show();
                } else if (binding.edtPassword.getText().toString().length() < 7) {
                    Toast.makeText(SignInActivity.this,
                            "Password must be of at least 8 characters in length",
                            Toast.LENGTH_SHORT).show();
                } else {
                    viewModel.signIn(binding.edtEmail.getText().toString(),
                            binding.edtPassword.getText().toString())
                            .observe(SignInActivity.this, new Observer<String>() {
                                @Override
                                public void onChanged(String Uid) {
                                    if (Uid == null){
                                        Toast.makeText(
                                                SignInActivity.this,
                                                "Invalid Username or password",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        SharedPreferences.Editor editor = pref.edit();
                                        editor.putString("User_id", Uid);
                                        editor.commit();
                                        Log.d("SignInActivity", Uid);
                                        Intent i = new Intent(SignInActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(i);
                                    }
                                }
                            });
                }

            }
        });


    }



}