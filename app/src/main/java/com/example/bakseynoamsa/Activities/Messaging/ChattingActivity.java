package com.example.bakseynoamsa.Activities.Messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.bakseynoamsa.Items.ChatReceiverItems;
import com.example.bakseynoamsa.Items.ChatSenderItems;
import com.example.bakseynoamsa.R;
import com.example.bakseynoamsa.Users.Users;
import com.example.bakseynoamsa.MessagesUtil.ChatMessages;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.xwray.groupie.GroupAdapter;

public class ChattingActivity extends AppCompatActivity {

    private String TAG = "sendingmms";
    RecyclerView recyclerView_chatting;
    Button btn_send_message;
    EditText et_send_message;
    GroupAdapter adapter = new GroupAdapter();
    Users toUser = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        btn_send_message = findViewById(R.id.btn_send_message);
        et_send_message = findViewById(R.id.et_send_message);
        recyclerView_chatting = findViewById(R.id.recyclerview_chatting);
        toUser = getIntent().getParcelableExtra(NewMessageActivity.USER_KEY);

        if(toUser != null)
        {
            setTitle(toUser.getUsername());
        }

        recyclerView_chatting.setAdapter(adapter);

        fromPreviousMessagages();

        btn_send_message.setOnClickListener(view -> {
            onSendingMessagesToFirebase();
        });

    }

    private void fromPreviousMessagages() {
        String fromId = FirebaseAuth.getInstance().getUid();

        assert toUser != null;
        String toId = toUser.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/user-messages/" + fromId + "/" + toId);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessages chatMessages = dataSnapshot.getValue(ChatMessages.class);
                if (chatMessages != null) {
                    Log.e(TAG, chatMessages.getText());
                    if(chatMessages.getFromId().equals(FirebaseAuth.getInstance().getUid())) {
                        Users thisUser = LogChatActivity.thisUser;
                        adapter.add(new ChatSenderItems(chatMessages.getText(), thisUser));
                    }else  {
                        adapter.add(new ChatReceiverItems(chatMessages.getText(), toUser));
                    }
                }
                recyclerView_chatting.scrollToPosition(adapter.getItemCount() - 1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onSendingMessagesToFirebase() {
        String text = et_send_message.getText().toString();
        if(text.equals("")){
            return;
        }
        toUser = getIntent().getParcelableExtra(NewMessageActivity.USER_KEY);
        String fromId = FirebaseAuth.getInstance().getUid();

        assert toUser != null;
        String toId = toUser.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/user-messages/" + fromId + "/" + toId).push();
        DatabaseReference reverseReference = FirebaseDatabase.getInstance().getReference("/user-messages/" + toId + "/" + fromId).push();
        DatabaseReference latestReverseReference = FirebaseDatabase.getInstance().getReference("/latest-messages/" + toId + "/" + fromId);
        DatabaseReference latestReference = FirebaseDatabase.getInstance().getReference("/latest-messages/" + fromId + "/" + toId);

        ChatMessages chatMessages = new ChatMessages(reference.getKey(), text, fromId, toId, System.currentTimeMillis()/1000);
        reference.setValue(chatMessages);
        reverseReference.setValue(chatMessages);
        latestReference.setValue(chatMessages);
        latestReverseReference.setValue(chatMessages);
        et_send_message.setText("");
        recyclerView_chatting.scrollToPosition(adapter.getItemCount() - 1);

    }
}
