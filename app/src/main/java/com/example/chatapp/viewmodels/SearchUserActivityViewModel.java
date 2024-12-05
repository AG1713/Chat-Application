package com.example.chatapp.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.chatapp.Callbacks.FireStoreChatRoomIdCallback;
import com.example.chatapp.repository.models.User;
import com.example.chatapp.repository.Repository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SearchUserActivityViewModel extends ViewModel {
    private final Repository repository;

    public SearchUserActivityViewModel() {
        this.repository = new Repository();
    }

    public FirestoreRecyclerOptions<User> getUserOptions(String hint){
        return repository.getUserOptions(hint);
    }

    public void createChatRoomForUsers(String userId, String username, FireStoreChatRoomIdCallback callback){
        repository.createChatRoomForUsers(userId, username, callback);
    }

}
