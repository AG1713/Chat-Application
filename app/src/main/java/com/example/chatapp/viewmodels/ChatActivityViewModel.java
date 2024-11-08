package com.example.chatapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatapp.repository.models.ChatRoom;
import com.example.chatapp.repository.models.Message;
import com.example.chatapp.repository.Repository;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class ChatActivityViewModel extends ViewModel {
    private final Repository repository;
    private MutableLiveData<ChatRoom> currentChatRoom;
    // Normally, this won't need to have a chatroom model, but who knows if later we needed to
    // display some attributes of the chatroom in future (currently, there is only members, but if that changes too)
    // Lets just work like that here then

    public ChatActivityViewModel(){
        repository = new Repository();
        currentChatRoom = new MutableLiveData<>();
    }

    public LiveData<ChatRoom> setCurrentChatRoom(String chatRoomId){
        repository.getChatRoom(chatRoomId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot snapshot) {
                currentChatRoom.postValue(snapshot.toObject(ChatRoom.class));
            }
        });

        return currentChatRoom;
    }

    public FirestoreRecyclerOptions<Message> getAllChats(){
        return repository.getAllMessages(Objects.requireNonNull(currentChatRoom.getValue()).getId());
    }

    public void sendMessageToUser(String username, String messageText){
        repository.sendMessageToUser(username, Objects.requireNonNull(currentChatRoom.getValue()).getId(), messageText);
    }

    public void sendMessageToGroup(String username, String messageText){
        repository.sendMessageToGroup(username, Objects.requireNonNull(currentChatRoom.getValue()).getId(), messageText);
    }

}
