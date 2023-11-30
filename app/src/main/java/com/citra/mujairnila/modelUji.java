package com.citra.mujairnila;

import android.os.Parcel;
import android.os.Parcelable;

public class modelUji implements Parcelable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHasil() {
        return hasil;
    }

    public void setHasil(String hasil) {
        this.hasil = hasil;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public double getPersen() {
        return persen;
    }

    public void setPersen(double persen) {
        this.persen = persen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String id;
    private String hasil;
    private String tanggal;
    private double persen;
    private String url;

    public modelUji() {};

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.hasil);
        dest.writeString(this.tanggal);
        dest.writeDouble(this.persen);
        dest.writeString(this.url);
    }

    protected modelUji(Parcel in) {
        this.id = in.readString();
        this.hasil = in.readString();
        this.tanggal = in.readString();
        this.persen = in.readDouble();
        this.url = in.readString();
    }

    public static final Creator<modelUji> CREATOR = new Creator<modelUji>() {
        @Override
        public modelUji createFromParcel(Parcel parcel) {
            return new modelUji(parcel);
        }

        @Override
        public modelUji[] newArray(int i) {
            return new modelUji[i];
        }
    };
}
