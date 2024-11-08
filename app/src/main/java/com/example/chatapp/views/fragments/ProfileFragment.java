package com.example.chatapp.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.viewmodels.MainActivityViewModel;
import com.example.chatapp.views.SignInActivity;
import com.example.chatapp.databinding.FragmentProfileBinding;
import com.example.chatapp.repository.models.User;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    MainActivityViewModel viewModel;
    ActivityResultLauncher<String> getProfilePhoto;
    SharedPreferences pref;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);

        pref = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putString("User_name", user.getUsername());
//                // The above isn't really needed

                if (user != null) binding.setUser(user);
                else Log.d("ProfileFragment Observer", "onChanged: User is null");
            }
        });



        binding.btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("User_id", null);
                editor.putString("User_name", null);
                editor.commit();
                Intent i = new Intent(getActivity(), SignInActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                viewModel.signOut();
                startActivity(i);
            }
        });

        binding.btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.btnEditProfile.getText().toString().equals("Edit Profile")){
                    binding.username.setEnabled(true);
                    binding.description.setEnabled(true);
                    binding.btnEditProfile.setText("Save");
                    binding.btnSignout.setVisibility(View.INVISIBLE);
                }
                else {
                    binding.username.setEnabled(false);
                    binding.description.setEnabled(false);
                    binding.btnEditProfile.setText("Edit Profile");
                    binding.btnSignout.setVisibility(View.VISIBLE);
                }

            }
        });



        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}