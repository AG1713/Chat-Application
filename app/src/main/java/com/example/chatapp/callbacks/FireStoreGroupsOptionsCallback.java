package com.example.chatapp.callbacks;

import com.example.chatapp.model.Group;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public interface FireStoreGroupsOptionsCallback {
    void onCallBack(FirestoreRecyclerOptions<Group> options);
}
