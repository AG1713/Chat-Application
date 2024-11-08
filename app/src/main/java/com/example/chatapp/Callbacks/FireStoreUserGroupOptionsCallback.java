package com.example.chatapp.Callbacks;

import com.example.chatapp.repository.models.UserGroup;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public interface FireStoreUserGroupOptionsCallback {
    void onCallback(FirestoreRecyclerOptions<UserGroup> options);
}
