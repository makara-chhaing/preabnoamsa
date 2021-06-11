package com.example.bakseynoamsa.Items;

import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import com.example.bakseynoamsa.R;
import com.example.bakseynoamsa.Users.Users;
import com.squareup.picasso.Picasso;
import com.xwray.groupie.Item;
import com.xwray.groupie.GroupieViewHolder;


public class UserItems extends Item<GroupieViewHolder>   {
    private Users user;
    public UserItems(Users user) {
        this.user = user;
    }
    public UserItems() {
    }

    public Users getUser() {
        return user;
    }

    @Override
    public void bind(@NonNull GroupieViewHolder viewHolder, int position) {
        if (user != null) {
            TextView viewById = (viewHolder.itemView.findViewById(R.id.tv_user_row_username));
            viewById.setText(user.getUsername());

            Picasso .get().load(user.getProfileImageUrl()).into((ImageView) viewHolder.itemView.findViewById(R.id.user_row_imageView));
        }

    }

    @Override
    public int getLayout() {
        return R.layout.user_rows_newmessage;
    }
}
