package com.example.bakseynoamsa.Activities.Messaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.bakseynoamsa.R;
import com.example.bakseynoamsa.Items.UserItems;
import com.example.bakseynoamsa.Users.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.xwray.groupie.GroupAdapter;
import com.xwray.groupie.Item;

import java.util.Objects;

public class NewMessageActivity extends AppCompatActivity {

    private static final String TAG = "newmessag000" ;
    RecyclerView recyclerView_new_message;
    public static final String USER_KEY = "user_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);

        Objects.requireNonNull(getSupportActionBar()).setTitle("People");

        recyclerView_new_message = findViewById(R.id.recyclerview_new_message);

        Log.e("test999", "top of rend user");
        rendUsers();
        Log.e("test999", "under rend user");
    }

    private void rendUsers() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GroupAdapter adapter = new GroupAdapter();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                   Users user = snapshot.getValue(Users.class);

                    if (user != null) {
                        if(LogChatActivity.thisUser!=null){
                            if(!user.getUid().equals(LogChatActivity.thisUser.getUid())) {
                                adapter.add(new UserItems(user));
                            }
                        }
                    }
                    adapter.setOnItemClickListener((item, view) -> {

                        UserItems userItems = (UserItems) item;
                        Intent intent = new Intent(getApplicationContext(), ChattingActivity.class);
                        intent.putExtra(USER_KEY, userItems.getUser());
                        startActivity(intent);
                        finish();
                    });
                }
                recyclerView_new_message.setAdapter(adapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
