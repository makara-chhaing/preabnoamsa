package com.example.bakseynoamsa.Users;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    protected Users(Parcel in) {
        username = in.readString();
        profileImageUrl = in.readString();
        uid = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public String getUsername() {
        return username;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getUid() {
        return uid;
    }

    private String username;
    private String profileImageUrl;
    private String uid;

    private void setUsername(String username) {
        this.username = username;
    }

    private void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    private void setUid(String uid) {
        this.uid = uid;
    }

    public Users(String uid, String username, String profileImageUrl) {
        setUsername(username);
        setProfileImageUrl(profileImageUrl);
        setUid(uid);
    }
    public Users() {
        this.uid = "";
        this.profileImageUrl = "";
        this.username = "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(username);
        parcel.writeString(profileImageUrl);
        parcel.writeString(uid);
    }
}

