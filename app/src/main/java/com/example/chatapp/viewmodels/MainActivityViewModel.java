package com.example.chatapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.Callbacks.FireStoreDocumentReferenceCallback;
import com.example.chatapp.repository.models.User;
import com.example.chatapp.repository.models.UserChat;
import com.example.chatapp.repository.models.UserGroup;
import com.example.chatapp.repository.Repository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

public class MainActivityViewModel extends ViewModel {
    private final Repository repository;
    public MutableLiveData<User> currentUser;

    public MainActivityViewModel(){
        this.repository = new Repository();
        this.currentUser = new MutableLiveData<>();
    }

    public LiveData<User> getCurrentUser(){
        repository.getCurrentUser().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                currentUser.postValue(snapshot.toObject(User.class));
            }
        });
        return currentUser;
    }

    public void editCurrentUserProfile(String description){
        repository.editCurrentUserProfile(description, new FireStoreDocumentReferenceCallback() {
                    @Override
                    public void onCallback(DocumentReference user) {
                        user.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot snapshot) {
                                currentUser.postValue(snapshot.toObject(User.class));
                            }
                        });
                    }
                }
        );
    }

    public FirestoreRecyclerOptions<UserChat> getCurrentUsersChats(){
        return repository.getCurrentUsersChats();
    }

    public FirestoreRecyclerOptions<UserGroup> getCurrentUsersGroups(){
        return repository.getCurrentUsersGroups();
    }

    public void signOut(){
        repository.signOut();
    }

}
