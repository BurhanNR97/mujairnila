package com.citra.mujairnila;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KNN {
    private String[] kode;
    private double[][] listMoments;
    private DatabaseReference db;
    private String[] mKode;
    private String[] mJenis;
    private double[] mMoments;
    private int[] mRank;

    public String[] getmKode() {
        return mKode;
    }

    public void setmKode(String[] mKode) {
        this.mKode = mKode;
    }

    public String[] getmJenis() {
        return mJenis;
    }

    public void setmJenis(String[] mJenis) {
        this.mJenis = mJenis;
    }

    public double[] getmMoments() {
        return mMoments;
    }

    public void setmMoments(double[] mMoments) {
        this.mMoments = mMoments;
    }

    public int[] getmRank() {
        return mRank;
    }

    public void setmRank(int[] mRank) {
        this.mRank = mRank;
    }

    private String[] jenis;

    public KNN() {
        db = FirebaseDatabase.getInstance().getReference("sampel");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int n = (int) snapshot.getChildrenCount();
                kode = new String[n];
                listMoments = new double[n][7];
                jenis = new String[n];

                ArrayList<String> k = new ArrayList<>();
                ArrayList<Double> m1 = new ArrayList<>();
                ArrayList<Double> m2 = new ArrayList<>();
                ArrayList<Double> m3 = new ArrayList<>();
                ArrayList<Double> m4 = new ArrayList<>();
                ArrayList<Double> m5 = new ArrayList<>();
                ArrayList<Double> m6 = new ArrayList<>();
                ArrayList<Double> m7 = new ArrayList<>();
                ArrayList<String> j = new ArrayList<>();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelSampel model = ds.getValue(modelSampel.class);
                    k.add(model.getId());   m1.add(model.getM1());
                    m2.add(model.getM2());  m3.add(model.getM3());
                    m4.add(model.getM4());  m5.add(model.getM5());
                    m6.add(model.getM6());  m7.add(model.getM7());
                    j.add(model.getJenis());
                }

                for (int i=0; i<n; i++) {
                    kode[i] = k.get(i);
                    jenis[i] = j.get(i);
                    listMoments[i][0] = m1.get(i);
                    listMoments[i][1] = m2.get(i);
                    listMoments[i][2] = m3.get(i);
                    listMoments[i][3] = m4.get(i);
                    listMoments[i][4] = m5.get(i);
                    listMoments[i][5] = m6.get(i);
                    listMoments[i][6] = m7.get(i);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void proses(double[] moments) {
        double[] hasil = new double[listMoments.length];
        for (int i=0; i<listMoments.length; i++) {
            double m1 = Math.pow(listMoments[i][0] - moments[0], 2);
            double m2 = Math.pow(listMoments[i][1] - moments[1], 2);
            double m3 = Math.pow(listMoments[i][2] - moments[2], 2);
            double m4 = Math.pow(listMoments[i][3] - moments[3], 2);
            double m5 = Math.pow(listMoments[i][4] - moments[4], 2);
            double m6 = Math.pow(listMoments[i][5] - moments[5], 2);
            double m7 = Math.pow(listMoments[i][6] - moments[6], 2);
            double total = m1 + m2 + m3 + m4 + m5 + m6 + m7;
            hasil[i] = Math.sqrt(total);
        }

        String[] hKode = new String[listMoments.length];
        double[] hNilai = new double[listMoments.length];
        String[] hJenis = new String[listMoments.length];

        for (int j=0; j<hasil.length; j++) {
            hKode[j] = kode[j];
            hNilai[j] = hasil[j];
            hJenis[j] = jenis[j];
        }
        for (int x=0; x<listMoments.length; x++) {
            for (int y=x; y<listMoments.length; y++) {
                if (hNilai[x] > hNilai[y]) {
                    String tm = hKode[x];
                    hKode[x] = hKode[y];
                    hKode[y] = tm;

                    double tmp = hNilai[x];
                    hNilai[x] = hNilai[y];
                    hNilai[y] = tmp;

                    String t = hJenis[x];
                    hJenis[x] = hJenis[y];
                    hJenis[y] = t;
                }
            }
        }

        mKode = new String[listMoments.length];
        mMoments = new double[listMoments.length];
        mRank = new int[2];

        ArrayList<Integer> mujair = new ArrayList<>();
        ArrayList<Integer> nila = new ArrayList<>();
        for (int z=0; z<15; z++) {
            mKode[z] = hKode[z];
            mJenis[z] = hJenis[z];
            mMoments[z] = hNilai[z];

            if (mJenis[z].equals("Ikan Mujair")) {
                mujair.add(1);
            } else
            if (mJenis[z].equals("Ikan Nila")) {
                nila.add(1);
            }
        }

        mRank[0] = mujair.size();
        mRank[1] = nila.size();
    }
}
