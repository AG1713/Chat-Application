package com.example.chatapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.repository.models.Group;
import com.example.chatapp.repository.models.User;
import com.example.chatapp.repository.Repository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class AddGroupMembersActivityViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<Group> groupMutableLiveData;

    public AddGroupMembersActivityViewModel(){
        this.repository = new Repository();
        this.groupMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<Group> getGroup(String groupId){
        repository.getGroup(groupId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                groupMutableLiveData.postValue(snapshot.toObject(Group.class));
            }
        });

        return groupMutableLiveData;
    }

    public FirestoreRecyclerOptions<User> getUserOptions(String hint){
        return repository.getUserOptions(hint);
    }

    public void addGroupMembers(ArrayList<String> newMembers){
        repository.addGroupMembers(groupMutableLiveData.getValue().getId(),
                groupMutableLiveData.getValue().getGroupName(),
                groupMutableLiveData.getValue().getChatRoomId(),
                newMembers
        );
    }


}
