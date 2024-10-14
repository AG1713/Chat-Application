package com.example.chatapp.Views;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
import com.example.chatapp.Views.adapter.MyViewPagerAdapter;
import com.example.chatapp.Views.fragments.ChatsFragment;
import com.example.chatapp.Views.fragments.GroupsFragment;
import com.example.chatapp.Views.fragments.ProfileFragment;
import com.example.chatapp.databinding.ActivityMainBinding;
import com.example.chatapp.viewModel.MyViewModel;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    MyViewModel myViewModel;

    // Dialog
//    private Dialog chatGroupDialog;

    // ViewPager2 and TabLayout
    ViewPager2 viewPager;
    TabLayout tabLayout;
    ArrayList<Fragment> fragmentArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        myViewModel = new ViewModelProvider(this).get(MyViewModel.class);
        myViewModel.getCurrentUser();


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

    }


//    private void showDialog(){
//        chatGroupDialog = new Dialog(this);
//        chatGroupDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        View view = LayoutInflater.from(this)
//                .inflate(R.layout.dialog_layout, null);
//
//        chatGroupDialog.setContentView(view);
//        chatGroupDialog.show();
//
//        Button submit = view.findViewById(R.id.submit);
//        EditText edt = view.findViewById(R.id.group_name_input);
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String groupName = edt.getText().toString();
//
//                Toast.makeText(MainActivity.this, "Test", Toast.LENGTH_SHORT).show();
//
//                chatGroupDialog.dismiss();
//            }
//        });
//
//    }

}