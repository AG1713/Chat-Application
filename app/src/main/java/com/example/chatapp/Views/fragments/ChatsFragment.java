package com.example.chatapp.Views.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chatapp.R;
import com.example.chatapp.Views.SearchUserActivity;
import com.example.chatapp.Views.adapter.RecentChatsAdapter;
import com.example.chatapp.databinding.FragmentChatsBinding;
import com.example.chatapp.model.ChatRoom;
import com.example.chatapp.viewModel.MyViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;


public class ChatsFragment extends Fragment {
    FragmentChatsBinding binding;
    RecentChatsAdapter adapter;
    RecyclerView recyclerView;
    MyViewModel myViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_chats, container, false);

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);


        binding.fab.setOnClickListener(v -> {
            Intent i = new Intent(getActivity(), SearchUserActivity.class);
            startActivity(i);
        });

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myViewModel.getCurrentUsersChats().observe(getViewLifecycleOwner(), new Observer<FirestoreRecyclerOptions<ChatRoom>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<ChatRoom> chatRoomFirestoreRecyclerOptions) {
                if (chatRoomFirestoreRecyclerOptions != null){
                    adapter = new RecentChatsAdapter(chatRoomFirestoreRecyclerOptions, getActivity(), myViewModel);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);
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