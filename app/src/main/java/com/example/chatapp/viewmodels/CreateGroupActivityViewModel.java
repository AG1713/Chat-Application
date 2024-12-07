package com.example.chatapp.viewmodels;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.Callbacks.FireStoreChatRoomIdCallback;
import com.example.chatapp.Callbacks.FireStoreDocumentReferenceCallback;
import com.example.chatapp.repository.Repository;
import com.example.chatapp.repository.models.Member;
import com.example.chatapp.repository.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class CreateGroupActivityViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<String> chatRoomId;

    public CreateGroupActivityViewModel(){
        repository = new Repository();
        chatRoomId = new MutableLiveData<>();
    }

    public void createChatRoomForGroups(String groupName, String description, ArrayList<Member> members, FireStoreChatRoomIdCallback callback){
        repository.createChatRoomForGroups(groupName, description, members, callback);
    }

    public void updateGroupPhoto(Uri imageUri){
        repository.updateProfilePhoto(imageUri, new FireStoreDocumentReferenceCallback() {
            @Override
            public void onCallback(DocumentReference documentReference) {

            }
        });
    }


}
