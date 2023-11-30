package com.citra.mujairnila;

import android.os.Parcel;
import android.os.Parcelable;

public class modelSampel implements Parcelable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public double getM1() {
        return m1;
    }

    public void setM1(double m1) {
        this.m1 = m1;
    }

    public double getM2() {
        return m2;
    }

    public void setM2(double m2) {
        this.m2 = m2;
    }

    public double getM3() {
        return m3;
    }

    public void setM3(double m3) {
        this.m3 = m3;
    }

    public double getM4() {
        return m4;
    }

    public void setM4(double m4) {
        this.m4 = m4;
    }

    public double getM5() {
        return m5;
    }

    public void setM5(double m5) {
        this.m5 = m5;
    }

    public double getM6() {
        return m6;
    }

    public void setM6(double m6) {
        this.m6 = m6;
    }

    public double getM7() {
        return m7;
    }

    public void setM7(double m7) {
        this.m7 = m7;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String id;
    private String jenis;
    private String tanggal;
    private double m1;
    private double m2;
    private double m3;
    private double m4;
    private double m5;
    private double m6;
    private double m7;
    private String url;

    public modelSampel() {};

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.jenis);
        dest.writeString(this.tanggal);
        dest.writeDouble(this.m1);
        dest.writeDouble(this.m2);
        dest.writeDouble(this.m3);
        dest.writeDouble(this.m4);
        dest.writeDouble(this.m5);
        dest.writeDouble(this.m6);
        dest.writeDouble(this.m7);
        dest.writeString(this.url);
    }

    protected modelSampel(Parcel in) {
        this.id = in.readString();
        this.jenis = in.readString();
        this.tanggal = in.readString();
        this.m1 = in.readDouble();
        this.m2 = in.readDouble();
        this.m3 = in.readDouble();
        this.m4 = in.readDouble();
        this.m5 = in.readDouble();
        this.m6 = in.readDouble();
        this.m7 = in.readDouble();
        this.url = in.readString();
    }

    public static final Creator<modelSampel> CREATOR = new Creator<modelSampel>() {
        @Override
        public modelSampel createFromParcel(Parcel parcel) {
            return new modelSampel(parcel);
        }

        @Override
        public modelSampel[] newArray(int i) {
            return new modelSampel[i];
        }
    };
}
