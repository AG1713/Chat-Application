package com.example.chatapp.Repository;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chatapp.Views.MainActivity;
import com.example.chatapp.callbacks.FireStoreCallBack;
import com.example.chatapp.callbacks.FireStoreGroupsOptionsCallback;
import com.example.chatapp.model.ChatRoom;
import com.example.chatapp.model.ChatRoomReference;
import com.example.chatapp.model.Group;
import com.example.chatapp.model.GroupReference;
import com.example.chatapp.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class Repository {

//    MutableLiveData<List<ChatGroup>> chatGroups;
//    MutableLiveData<List<ChatMessage>> messagesLiveData;
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore database;
    private final CollectionReference usersRef;
    private final CollectionReference groupsRef;
    private final CollectionReference chatRoomsRef;

//    FirebaseDatabase database;
//    DatabaseReference reference;
//    DatabaseReference groupReference;


    public Repository() {
//        this.chatGroups = new MutableLiveData<>();
//        database = FirebaseDatabase.getInstance();
//        reference = database.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        usersRef = database.collection("Users");
        groupsRef = database.collection("Groups");
        chatRoomsRef = database.collection("ChatRooms");

//        messagesLiveData = new MutableLiveData<>();
    }

    public void firebaseAnonymousAuth(Context context){
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    // Authentication is successful
                    Intent i = new Intent(context, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    // Indicates that a new task should be created for the activity that is being
                    // started. This means that the new activity will be started in the new task
                    // instead of same task
                    // IMPORTANT
                    // It is a good practice to use the above flag, since you are not moving from
                    // the current activity to other, you are doing it through another class,
                    // here, the repository

                    context.startActivity(i);

                }

            }
        });
    }

    public void createVerifiedUser(String email, String password, User userModel, Context context){
        if (!email.isEmpty() && !password.isEmpty()){
            FirebaseAuth.getInstance().signOut();

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show();

                            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                        @Override
                                        public void onSuccess(AuthResult authResult) {
                                            Toast.makeText(context, "Signed In Successfully", Toast.LENGTH_SHORT).show();

                                            userModel.setId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                            usersRef.document(userModel.getId()).set(userModel);

                                            Intent i = new Intent(context, MainActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            // The above starts a new activity, with no activity in the stack
                                            context.startActivity(i);
                                        }
                                    });
                        }
                    });


        }

    }

    public void signInVerifiedUser(String email, String password, Context context){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Signed In Successfully", Toast.LENGTH_SHORT).show();

                            Intent i = new Intent(context, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            // The above starts a new activity, with no activity in the stack
                            context.startActivity(i);
                        }
                        else {
                            Toast.makeText(context, "Invalid email id or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Sign out functionality
    public void signOut(){
        FirebaseAuth.getInstance().signOut();
    }

    public void getCurrentUser(FireStoreCallBack callBack){
        String uid = firebaseAuth.getUid();
        assert uid != null;
        callBack.onCallBack(usersRef.document(uid));
    }

    public String getCurrentUserId(){
        return firebaseAuth.getUid();
    }

    public CollectionReference getAllUsersCollectionReference(){
        return database.collection("Users");
    }

    public DocumentReference getUser(String userId){
        return usersRef.document(userId);
    }


    // ***************************** Chat rooms *****************************
    public String getChatRoomIdForUsers(String userId1, String userId2){
        if (userId1.compareTo(userId2) > 0){
            return userId1 + "_" + userId2;
        }
        return userId2 + "_" + userId1;
    }

    public void getOrCreateChatRoomForUsers(String chatRoomId, ArrayList<String> members, FireStoreCallBack callBack){

        chatRoomsRef.document(chatRoomId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()){

                        if (task.getResult().toObject(ChatRoom.class) == null){
                            // Creating a new chatroom
                            ChatRoom newChatRoom = new ChatRoom(
                                    chatRoomId,
                                    members,
                                    "",
                                    "",
                                    "",
                                    Timestamp.now()
                            );
                            chatRoomsRef.document(chatRoomId).set(newChatRoom)
                                    .addOnSuccessListener(unused -> {

                                        callBack.onCallBack(chatRoomsRef.document(chatRoomId));
                                        Log.d("Repository", "getOrCreateChatRoomReferenceForUsers: Ran");
                                    });
                        }
                        else {
                            callBack.onCallBack(chatRoomsRef.document(chatRoomId));
                        }

                    } else {
                        Log.d("Repository", "getOrCreateChatRoomReferenceForUsers: Unsuccessful");
                    }
                });
    }

    public void getChatRoomForGroups(String chatRoomId, FireStoreCallBack callBack){

        chatRoomsRef.document(chatRoomId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                callBack.onCallBack(chatRoomsRef.document(chatRoomId));
            }
        });

    }

    public DocumentReference getChatRoomForUsers(String chatRoomId){
        return chatRoomsRef.document(chatRoomId);
    }

    public CollectionReference getChatRoomChatMessagesReference(String chatRoomId){
        return chatRoomsRef.document(chatRoomId).collection("Messages");
    }

    public CollectionReference getAllChatRoomReference(){
        return chatRoomsRef;
    }

    // ***************************** User chats *****************************

    public void getOrCreateChat(String userId1, String userId2, ArrayList<String> members, FireStoreCallBack callBack){
        Log.d("Repository", "getOrCreateChat: Running");
        String chatRoomId = getChatRoomIdForUsers(userId1, userId2);

        getOrCreateChatRoomForUsers(chatRoomId, members,
                new FireStoreCallBack() {
                    @Override
                    public void onCallBack(DocumentReference documentReference) {
                        for (String userId : members){
                            usersRef.document(userId).collection("Chats").document(chatRoomId).set(
                                    new ChatRoomReference(chatRoomId)
                            );
                        }

                        callBack.onCallBack(documentReference);
                        Log.d("Repository", "getOrCreateChat: Ran");
                    }
                });


    }

    public void getOtherUserFromChatroom(ArrayList<String> members, OnCompleteListener<DocumentSnapshot> listener){
        DocumentReference documentReference;

        if (members.get(0).equals(getCurrentUserId())) {
            documentReference = usersRef.document(members.get(1));
        }
        else {
            documentReference = usersRef.document(members.get(0));
        }

        documentReference.get().addOnCompleteListener(listener);
    }

    public CollectionReference getCurrentUsersChatsCollection(){
        return usersRef.document(getCurrentUserId()).collection("Chats");
    }


    // ***************************** Groups *****************************
    public void getOrCreateGroup(String groupName, String groupDescription, ArrayList<String> members, FireStoreCallBack callBack){
        String id = groupName.replace(' ','_') + Timestamp.now();

        getOrCreateChatRoomForUsers(id, members, new FireStoreCallBack() {
            @Override
            public void onCallBack(DocumentReference documentReference) {
                Group group = new Group(id, groupName, groupDescription, Timestamp.now());

                groupsRef.document(id).set(group)
                        .addOnSuccessListener(unused -> {
                            for (String member : members){
                                usersRef.document(member).collection("Groups").document(id).set(new GroupReference(id));
                            }
                            callBack.onCallBack(groupsRef.document(id));

                        });
            }
        });
    }

    public DocumentReference getGroup(String groupId){
        return groupsRef.document(groupId);
    }

    public void addGroupMembers(String chatRoomId, String groupId, ArrayList<String> newMembers){

        chatRoomsRef.document(chatRoomId).update("members", FieldValue.arrayUnion(newMembers))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        for (String membersId : newMembers){
                            getUser(membersId).collection("Groups").document(groupId)
                                    .set(new GroupReference(groupId));
                        }
                    }
                });


    }

    public void getAllRecentGroupsOptions(FireStoreGroupsOptionsCallback callback){
        usersRef.document(getCurrentUserId()).collection("Groups").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                ArrayList<String> groupIds = new ArrayList<>();

                for (DocumentSnapshot doc : queryDocumentSnapshots){
                    groupIds.add(doc.getId());
                }

                Log.d("Repository", "groupIds size: " + groupIds.size());

                if (!groupIds.isEmpty()){
                    Query query = groupsRef.whereIn(FieldPath.documentId(), groupIds);

                    callback.onCallBack(new FirestoreRecyclerOptions.Builder<Group>()
                            .setQuery(query, Group.class).build());
                } else {
                    Log.d("Repository", "onSuccess: groupIds are empty");
                }

            }
        });



    }


}
