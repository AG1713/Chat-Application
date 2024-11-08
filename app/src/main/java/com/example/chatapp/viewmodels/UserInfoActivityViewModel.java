package com.example.chatapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.repository.models.User;
import com.example.chatapp.repository.Repository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class UserInfoActivityViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<User> userMutableLiveData;

    public UserInfoActivityViewModel(){
        repository = new Repository();
        userMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<User> getUser(String userId){
        repository.getSpecifiedUser(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                userMutableLiveData.postValue(snapshot.toObject(User.class));
            }
        });

        return userMutableLiveData;
    }

}
