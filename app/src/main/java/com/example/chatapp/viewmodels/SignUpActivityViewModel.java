package com.example.chatapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.repository.models.User;
import com.example.chatapp.repository.Repository;

public class SignUpActivityViewModel extends ViewModel {
    private final Repository repository;
    public MutableLiveData<String> currentUserId;

    public SignUpActivityViewModel(){
        this.repository = new Repository();
        this.currentUserId = new MutableLiveData<>();
    }

    public LiveData<String> signUp(String email, String password, String username, String description){
        User user = new User(username, description, email);

        repository.createUser(email, password, user, Uid -> currentUserId.postValue(Uid));

        return currentUserId;
    }

}
