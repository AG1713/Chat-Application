package com.example.chatapp.views.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCaller;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatapp.GlideApp;
import com.example.chatapp.R;
import com.example.chatapp.viewmodels.MainActivityViewModel;
import com.example.chatapp.views.SignInActivity;
import com.example.chatapp.databinding.FragmentProfileBinding;
import com.example.chatapp.repository.models.User;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ProfileFragment extends Fragment {
    FragmentProfileBinding binding;
    MainActivityViewModel viewModel;
    ActivityResultLauncher<String> getProfilePhoto;
    SharedPreferences pref;
    ActivityResultLauncher<String> takePhoto;
    Uri imageUri;
    boolean photoEdited = false;

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

                if (user != null) {
                    binding.setUser(user);
                    ViewGroup.LayoutParams params = binding.userProfilePicture.profilePicImageview.getLayoutParams();
                    params.height = 300;
                    params.width = 300;
                    binding.userProfilePicture.profilePicImageview.setLayoutParams(params);

                    Glide.with(getActivity())
                            .load(user.getProfilePhotoUrl())
                            .placeholder(R.drawable.baseline_person)
                            .error(R.drawable.baseline_person)
                            .override(500, 500) // Resize to 500x500 pixels
                            .apply(RequestOptions.circleCropTransform())
                            .into(binding.userProfilePicture.profilePicImageview);
                }
                else Log.d("ProfileFragment Observer", "onChanged: User is null");
            }
        });

        takePhoto = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri o) {
                        if (o != null) {
                            Log.d("ProfileFragment", "URI: " + o +
                                    "\ntrue?:" + o.getScheme().equals("content"));
                            imageUri = o;
                            ContentResolver resolver = getContext().getContentResolver();
                            try (InputStream inputStream = resolver.openInputStream(o)) {
                                if (inputStream != null) {
                                    Log.d("File Check", "File exists at URI.");
                                }
                            } catch (IOException e) {
                                Log.e("File Check", "File not found at URI.", e);
                            }
                            Glide.with(getActivity())
                                    .load(o)
                                    .placeholder(R.drawable.baseline_person)
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(binding.userProfilePicture.profilePicImageview);
                        }

                    }
                }
        );

        View.OnClickListener imageEditListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto.launch("image/*");
                photoEdited = true;
            }
        };

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
                    binding.description.setEnabled(true);
                    binding.btnEditProfile.setText("Save");
                    binding.btnSignout.setVisibility(View.INVISIBLE);
                    binding.description.requestFocus();
                    binding.userProfilePicture.getRoot().setOnClickListener(imageEditListener);
                }
                else {
                    binding.description.setEnabled(false);
                    if (photoEdited){
                        photoEdited = false;
                        viewModel.updateProfilePhoto(imageUri);
                    }
                    viewModel.editCurrentUserProfile(binding.description.getText().toString());
                    binding.btnEditProfile.setText("Edit Profile");
                    binding.btnSignout.setVisibility(View.VISIBLE);
                    binding.userProfilePicture.getRoot().setOnClickListener(null);

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