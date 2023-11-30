package com.citra.mujairnila;

public class modelDataSampel {
    private String moments;
    private double nilai;

    public String getMoments() {
        return moments;
    }

    public void setMoments(String moments) {
        this.moments = moments;
    }

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }

    public modelDataSampel(String moments, double nilai) {
        this.moments = moments;
        this.nilai = nilai;
    }
}
