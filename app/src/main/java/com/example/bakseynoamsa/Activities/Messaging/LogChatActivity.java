package com.example.bakseynoamsa.Activities.Messaging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.bakseynoamsa.Activities.LoginAndRegistry.LoginActivity;
import com.example.bakseynoamsa.Items.UserItems;
import com.example.bakseynoamsa.R;
import com.example.bakseynoamsa.Users.Users;
import com.example.bakseynoamsa.MessagesUtil.ChatMessages;
import com.example.bakseynoamsa.MessagesUtil.LatestLogChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;
import com.xwray.groupie.OnItemClickListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.bakseynoamsa.Activities.Messaging.NewMessageActivity.USER_KEY;

public class LogChatActivity extends AppCompatActivity {

    public static Users thisUser;
    RecyclerView recyclerview_log_chat;
    GroupAdapter adapter = new GroupAdapter();
    HashMap<String, ChatMessages> logMessagesMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Chat Logs");
        recyclerview_log_chat = findViewById(R.id.recyclerview_log_chat);
        recyclerview_log_chat.setAdapter(adapter);
        recyclerview_log_chat.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        String uid = FirebaseAuth.getInstance().getUid();
        if (uid == null) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

        getThisUser();
        //
//        renderLogChat();
        lastesPreviousMessages();
        adapter.setOnItemClickListener((item, view) -> {
            LatestLogChat userItems = (LatestLogChat) item;
            Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
            intent.putExtra(USER_KEY, userItems.partnerUser);
            startActivity(intent);
        });
    }

    private void refreshRecyclerview() {
        adapter.clear();
        for(ChatMessages chatMessages : logMessagesMap.values()){
            adapter.add(new LatestLogChat(chatMessages));
        }
    }

    private void lastesPreviousMessages() {
        String fromUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(
                "/latest-messages/" + fromUid);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessages chatMessages = dataSnapshot.getValue(ChatMessages.class);
                logMessagesMap.put(dataSnapshot.getKey(), chatMessages);
                refreshRecyclerview();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                ChatMessages chatMessages = dataSnapshot.getValue(ChatMessages.class);
                logMessagesMap.put(dataSnapshot.getKey(), chatMessages);
                refreshRecyclerview();
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

    private void renderLogChat() {
        recyclerview_log_chat.setAdapter(adapter);

        adapter.add(new LatestLogChat());
        adapter.add(new LatestLogChat());
        adapter.add(new LatestLogChat());
    }

    private void getThisUser() {
        String thisUserUid = FirebaseAuth.getInstance().getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/users/" + thisUserUid);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                thisUser = dataSnapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigate_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId){
            case R.id.new_message_menu:
                Intent intent = new Intent(getApplicationContext(), NewMessageActivity.class);
                startActivity(intent);

                break;

            case R.id.sign_out:
                FirebaseAuth.getInstance().signOut();
                Intent intentOut = new Intent(getApplicationContext(), LoginActivity.class);
                intentOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intentOut);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}




