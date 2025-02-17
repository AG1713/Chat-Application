package com.example.chatapp.views;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.R;
import com.example.chatapp.viewmodels.SearchUserActivityViewModel;
import com.example.chatapp.views.adapter.SearchUserRecyclerAdapter;
import com.example.chatapp.databinding.ActivitySearchUserBinding;

public class SearchUserActivity extends AppCompatActivity {
    ActivitySearchUserBinding binding;
    SearchUserRecyclerAdapter adapter;
    SearchUserActivityViewModel viewModel;
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

        viewModel = new ViewModelProvider(this).get(SearchUserActivityViewModel.class);

        recyclerView = binding.usersRecyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        binding.btnSearchUser.setOnClickListener(v -> {
            String hint = binding.edtSearchUser.getText().toString().trim();

            if (hint.isEmpty()){
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            }
            else {
                adapter = new SearchUserRecyclerAdapter(viewModel.getUserOptions(hint), SearchUserActivity.this, viewModel,
                        value -> {
                            if (value) binding.emptyTextView.setVisibility(View.VISIBLE);
                            else binding.emptyTextView.setVisibility(View.GONE);
                        });
                recyclerView.setAdapter(adapter);

                // Restart listening for changes after updating the query
                adapter.startListening();
                // startListening
                // - begins listening for any changes in FireStore data that match the query
                // - without calling startListening(), the adapter will not observe or populate any data from
                //   FireStore
                // - Must be called in Lifecycle, ensuring that data is refreshed when UI is available
                Log.d("SearchUserActivity", "Adapter listening started.");
            }
        });

    }

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