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
import com.example.chatapp.Views.CreateGroupActivity;
import com.example.chatapp.Views.adapter.RecentGroupsAdapter;
import com.example.chatapp.databinding.FragmentGroupsBinding;
import com.example.chatapp.model.Group;
import com.example.chatapp.viewModel.MyViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;

public class GroupsFragment extends Fragment {
    private FragmentGroupsBinding binding;
    private RecyclerView recyclerView;
    private MyViewModel myViewModel;
    private RecentGroupsAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_groups, container, false);

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);

        // Set up an observer to listen changes in the "Live Data" object


        myViewModel.getAllRecentGroupsOptions().observe(getViewLifecycleOwner(), new Observer<FirestoreRecyclerOptions<Group>>() {
            @Override
            public void onChanged(FirestoreRecyclerOptions<Group> options) {
                if (options != null){
                    adapter = new RecentGroupsAdapter(options, getActivity(), myViewModel);
                    adapter.startListening();
                    recyclerView.setAdapter(adapter);

                }
            }
        });


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), CreateGroupActivity.class);
                startActivity(i);

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