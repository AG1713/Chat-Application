package com.example.chatapp.Views;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.Views.adapter.SearchUserRecyclerAdapter;
import com.example.chatapp.databinding.ActivitySearchUserBinding;
import com.example.chatapp.viewModel.MyViewModel;

public class SearchUserActivity extends AppCompatActivity {
    ActivitySearchUserBinding binding;
    SearchUserRecyclerAdapter adapter;
    MyViewModel viewModel;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(this).get(MyViewModel.class);

        recyclerView = binding.usersRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.btnSearchUser.setOnClickListener(v -> {
            String username = binding.edtSearchUser.getText().toString().trim();

            if (username.isEmpty()){
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else {
                getUsersList(binding.edtSearchUser.getText().toString());
            }
        });




    }

    public void getUsersList(String username){

        adapter = new SearchUserRecyclerAdapter(viewModel.getSpecifiedUser(username), this);
        recyclerView.setAdapter(adapter);

        // Restart listening for changes after updating the query
        adapter.startListening();
        Log.d("SearchUserActivity", "Adapter listening started.");
    }

    // startListening
    // - begins listening for any changes in FireStore data that match the query
    // - without calling startListening(), the adapter will not observe or populate any data from
    //   FireStore
    // - Must be called in Lifecycle, ensuring that data is refreshed when UI is available


    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
            Log.d("SearchUserActivity", "onStart: Adapter started listening.");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null) {
            adapter.stopListening();
            Log.d("SearchUserActivity", "onStop: Adapter stopped listening.");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null) {
            adapter.startListening();
            Log.d("SearchUserActivity", "onResume: Adapter started listening.");
        }
    }
}