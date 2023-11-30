package com.citra.mujairnila;

public class modelDataKNN {
    private int no;
    private String kode;
    private String jennis;
    private double nilai;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getKode() {
        return kode;
    }

    public void setKode(String kode) {
        this.kode = kode;
    }

    public String getJennis() {
        return jennis;
    }

    public void setJennis(String jennis) {
        this.jennis = jennis;
    }

    public double getNilai() {
        return nilai;
    }

    public void setNilai(double nilai) {
        this.nilai = nilai;
    }

    public modelDataKNN(int no, String kode, String jenis, double nilai) {
        this.no = no;
        this.kode = kode;
        this.jennis = jenis;
        this.nilai = nilai;
    }
}
