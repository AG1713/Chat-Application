package com.example.chatapp.views.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.chatapp.repository.models.User;
import com.example.chatapp.viewmodels.MainActivityViewModel;
import com.example.chatapp.views.CreateGroupActivity;
import com.example.chatapp.views.adapter.RecentGroupsAdapter;
import com.example.chatapp.databinding.FragmentGroupsBinding;

public class GroupsFragment extends Fragment {
    private FragmentGroupsBinding binding;
    private RecyclerView recyclerView;
    private MainActivityViewModel viewModel;
    private RecentGroupsAdapter adapter;
    SharedPreferences pref;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_groups, container, false);

        recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        pref = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        viewModel.getCurrentUser().observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user != null){
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("User_name", user.getUsername());
                    editor.commit();
                    adapter = new RecentGroupsAdapter(viewModel.getCurrentUsersGroups(), getActivity());
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