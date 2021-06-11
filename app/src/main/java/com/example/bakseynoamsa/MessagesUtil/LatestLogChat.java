package com.example.bakseynoamsa.MessagesUtil;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bakseynoamsa.R;
import com.example.bakseynoamsa.Users.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

public class LatestLogChat extends Item<GroupieViewHolder> {
    private ChatMessages chatMessages;
    public Users partnerUser = null;
    public LatestLogChat(ChatMessages chatMessages) {
        this.chatMessages = chatMessages;
    }
    public LatestLogChat(){

    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView viewById = (viewHolder.itemView.findViewById(R.id.tv_latest_messages));
        TextView viewByIdUser = (viewHolder.itemView.findViewById(R.id.tv_user_log));
        viewById.setText(chatMessages.getText());

        String chatPartner;
        if(chatMessages.getFromId().equals(FirebaseAuth.getInstance().getUid())){
            chatPartner = chatMessages.getToId();
        }else {
            chatPartner = chatMessages.getFromId();
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/users/" + chatPartner);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 partnerUser = dataSnapshot.getValue(Users.class);
                if( partnerUser != null) {
                    viewByIdUser.setText(partnerUser.getUsername());
                    if(partnerUser.getProfileImageUrl() != null){
                        Picasso.get().load(partnerUser.getProfileImageUrl()).into((ImageView) viewHolder.itemView.findViewById(R.id.iv_log_circleImageView));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getLayout() {
        return R.layout.chat_row_log_chat;
    }
}
