package com.citra.mujairnila;

import android.os.Parcel;
import android.os.Parcelable;

public class modelUser implements Parcelable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String id;
    private String nama;
    private String username;
    private String password;

    public modelUser() {};

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.nama);
        dest.writeString(this.username);
        dest.writeString(this.password);
    }

    protected modelUser(Parcel in) {
        this.id = in.readString();
        this.nama = in.readString();
        this.username = in.readString();
        this.password = in.readString();
    }

    public static final Creator<modelUser> CREATOR = new Creator<modelUser>() {
        @Override
        public modelUser createFromParcel(Parcel parcel) {
            return new modelUser(parcel);
        }

        @Override
        public modelUser[] newArray(int i) {
            return new modelUser[i];
        }
    };
}
