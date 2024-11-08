package com.example.chatapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.repository.Repository;

public class SignInActivityViewModel extends ViewModel {
    private final Repository repository;
    public MutableLiveData<String> currentUserId;

    public SignInActivityViewModel(){
        this.repository = new Repository();
        this.currentUserId = new MutableLiveData<>();
    }

    public LiveData<String> signIn(String email, String password){
        repository.signInUser(email, password, Uid -> currentUserId.postValue(Uid));
        return currentUserId;
    }


}
