package com.example.bakseynoamsa.Items;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.bakseynoamsa.R;
import com.example.bakseynoamsa.Users.Users;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.GroupieViewHolder;
import com.xwray.groupie.Item;

import static com.example.bakseynoamsa.R.id.chatting_sending_image;

public class ChatSenderItems extends Item<GroupieViewHolder> {
    public ChatSenderItems(String text) {
        this.text = text;
    }

    private Users user = new Users();
    private String text;
    public ChatSenderItems() {
    }
    public ChatSenderItems(String text, Users user) {
        this.text = text;
        this.user = user;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        TextView viewById = (viewHolder.itemView.findViewById(R.id.tv_chatting_sender));
        viewById.setText(text);
        if(user != null && user.getProfileImageUrl() != null) {
            Picasso.get().load(user.getProfileImageUrl()).into((ImageView) viewHolder.itemView.findViewById(chatting_sending_image));
        }
    }

    @Override
    public int getLayout() {
        return R.layout.chat_sender_row;
    }
}