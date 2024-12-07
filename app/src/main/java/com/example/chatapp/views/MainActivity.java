package com.example.chatapp.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.chatapp.R;
import com.example.chatapp.viewmodels.MainActivityViewModel;
import com.example.chatapp.views.adapter.MyViewPagerAdapter;
import com.example.chatapp.views.fragments.ChatsFragment;
import com.example.chatapp.views.fragments.GroupsFragment;
import com.example.chatapp.views.fragments.ProfileFragment;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MainActivityViewModel viewModel;

    // ViewPager2 and TabLayout
    ViewPager2 viewPager;
    TabLayout tabLayout;
    ArrayList<Fragment> fragmentArrayList;
    SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        pref = getSharedPreferences("User", MODE_PRIVATE);
        Log.d("MainActivity", pref.getString("User_id", null) + " " + pref.getString("User_name", null));

        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Log.d("Token", "Token: " + s);
            }
        });

        // ViewPager2 and TabLayout
        viewPager = binding.viewPager;
        tabLayout = binding.tabLayout;
        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(new ChatsFragment());
        fragmentArrayList.add(new GroupsFragment());
        fragmentArrayList.add(new ProfileFragment());

        MyViewPagerAdapter adapter = new MyViewPagerAdapter(
                getSupportFragmentManager(),
                getLifecycle(),
                fragmentArrayList
        );

        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(
                tabLayout,
                viewPager,

                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        switch (position){
                            case 0:{
                                tab.setText("Chats");
                                break;
                            }
                            case 1:{
                                tab.setText("Groups");
                                break;
                            }
                            case 2:{
                                tab.setText("Profile");
                                break;
                            }
                            default:{
                                tab.setText("N/A");
                                break;
                            }
                        }
                    }
                }
        ).attach();

        viewModel.updateLastActiveTime();
    }

}