package com.example.chatapp.Callbacks;

import com.example.chatapp.repository.models.UserChat;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public interface FireStoreUserChatOptionsCallback {
    void onCallback(FirestoreRecyclerOptions<UserChat> options);
}
