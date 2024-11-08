package com.example.chatapp.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.Callbacks.FireStoreChatRoomIdCallback;
import com.example.chatapp.repository.Repository;

import java.util.ArrayList;

public class CreateGroupActivityViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<String> chatRoomId;

    public CreateGroupActivityViewModel(){
        repository = new Repository();
        chatRoomId = new MutableLiveData<>();
    }

    public void createChatRoomForGroups(String groupName, String description, ArrayList<String> members, FireStoreChatRoomIdCallback callback){
        repository.createChatRoomForGroups(groupName, description, members, callback);
    }


}
