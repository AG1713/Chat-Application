package com.example.chatapp.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.viewmodels.MainActivityViewModel;
import com.example.chatapp.views.SearchUserActivity;
import com.example.chatapp.views.adapter.RecentChatsAdapter;
import com.example.chatapp.databinding.FragmentChatsBinding;


public class ChatsFragment extends Fragment {
    FragmentChatsBinding binding;
    RecentChatsAdapter adapter;
    RecyclerView recyclerView;
    MainActivityViewModel viewModel;
    SharedPreferences pref;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false);

        pref = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        viewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        binding.fab.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), SearchUserActivity.class);
            startActivity(i);
        });

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        viewModel.getCurrentUser().observe(getActivity(), user -> {
            if (user != null){
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("User_name", user.getUsername());
                editor.commit();
                adapter = new RecentChatsAdapter(viewModel.getCurrentUsersChats(), getActivity());
                adapter.startListening();
                recyclerView.setAdapter(adapter);
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