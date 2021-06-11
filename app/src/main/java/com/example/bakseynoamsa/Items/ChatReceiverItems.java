package com.example.bakseynoamsa.Items;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bakseynoamsa.R;
import com.example.bakseynoamsa.Users.Users;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

public class ChatReceiverItems extends Item<GroupieViewHolder> {
    private String text;
    private Users user;
    public ChatReceiverItems(String text) {
    }

    public ChatReceiverItems(String text, Users user) {
        this.text = text;
        this.user = user;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView viewById = (viewHolder.itemView.findViewById(R.id.tv_chatting_receiver));
        viewById.setText(text);
        if(user != null && user.getProfileImageUrl() != null) {
            Picasso.get().load(user.getProfileImageUrl()).into((ImageView) viewHolder.itemView.findViewById(R.id.chatting_receive_image));
        }
    }

    @Override
    public int getLayout() {
        return R.layout.chat_receive_row;
    }
}
