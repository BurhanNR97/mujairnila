package com.citra.mujairnila;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdpData15 extends RecyclerView.Adapter {

    private List<modelData15> dataList;

    public AdpData15(List<modelData15> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tabel_moments, parent, false);

        return new RowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {

            rowViewHolder.no.setBackgroundResource(R.drawable.border_header);
            rowViewHolder.kode.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.jenis.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.m1.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.m2.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.m3.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.m4.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.m5.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.m6.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.m7.setBackgroundResource(R.drawable.border_right);

            rowViewHolder.no.setTypeface(null, Typeface.BOLD);
            rowViewHolder.kode.setTypeface(null, Typeface.BOLD);
            rowViewHolder.jenis.setTypeface(null, Typeface.BOLD);
            rowViewHolder.m1.setTypeface(null, Typeface.BOLD);
            rowViewHolder.m2.setTypeface(null, Typeface.BOLD);
            rowViewHolder.m3.setTypeface(null, Typeface.BOLD);
            rowViewHolder.m4.setTypeface(null, Typeface.BOLD);
            rowViewHolder.m5.setTypeface(null, Typeface.BOLD);
            rowViewHolder.m6.setTypeface(null, Typeface.BOLD);
            rowViewHolder.m7.setTypeface(null, Typeface.BOLD);

            rowViewHolder.no.setTextSize(18f);
            rowViewHolder.kode.setTextSize(18f);
            rowViewHolder.jenis.setTextSize(18f);
            rowViewHolder.m1.setTextSize(18f);
            rowViewHolder.m2.setTextSize(18f);
            rowViewHolder.m3.setTextSize(18f);
            rowViewHolder.m4.setTextSize(18f);
            rowViewHolder.m5.setTextSize(18f);
            rowViewHolder.m6.setTextSize(18f);
            rowViewHolder.m7.setTextSize(18f);

            rowViewHolder.no.setText("No");
            rowViewHolder.kode.setText("Kode");
            rowViewHolder.jenis.setText("Jenis Ikan");
            rowViewHolder.m1.setText("M1");
            rowViewHolder.m2.setText("M2");
            rowViewHolder.m3.setText("M3");
            rowViewHolder.m4.setText("M4");
            rowViewHolder.m5.setText("M5");
            rowViewHolder.m6.setText("M6");
            rowViewHolder.m7.setText("M7");

            rowViewHolder.m6.setVisibility(View.GONE);
            rowViewHolder.m7.setVisibility(View.GONE);
        } else {
            modelData15 data = dataList.get(rowPos - 1);

            rowViewHolder.no.setBackgroundResource(R.drawable.border_field);
            rowViewHolder.kode.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.jenis.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.jenis.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.jenis.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.jenis.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.jenis.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.jenis.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.jenis.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.jenis.setBackgroundResource(R.drawable.border_bottom);

            rowViewHolder.no.setTextSize(18f);
            rowViewHolder.kode.setTextSize(18f);
            rowViewHolder.jenis.setTextSize(18f);
            rowViewHolder.m1.setTextSize(18f);
            rowViewHolder.m2.setTextSize(18f);
            rowViewHolder.m3.setTextSize(18f);
            rowViewHolder.m4.setTextSize(18f);
            rowViewHolder.m5.setTextSize(18f);
            rowViewHolder.m6.setTextSize(18f);
            rowViewHolder.m7.setTextSize(18f);

            rowViewHolder.no.setText(data.getNo()+ "");
            rowViewHolder.kode.setText(data.getKode());
            rowViewHolder.jenis.setText(data.getJenis());
            rowViewHolder.m1.setText(String.format("%.7f", data.getM1()) + "");
            rowViewHolder.m2.setText(String.format("%.7f", data.getM2()) + "");
            rowViewHolder.m3.setText(String.format("%.7f", data.getM3()) + "");
            rowViewHolder.m4.setText(String.format("%.7f", data.getM4()) + "");
            rowViewHolder.m5.setText(String.format("%.7f", data.getM5()) + "");
            rowViewHolder.m6.setText(String.format("%.7f", data.getM6()) + "");
            rowViewHolder.m7.setText(String.format("%.7f", data.getM7()) + "");

            rowViewHolder.m6.setVisibility(View.GONE);
            rowViewHolder.m7.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {
        TextView no;
        TextView kode;
        TextView jenis;
        TextView m1;
        TextView m2;
        TextView m3;
        TextView m4;
        TextView m5;
        TextView m6;
        TextView m7;

        RowViewHolder(View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.m_rank);
            kode = itemView.findViewById(R.id.m_kode);
            jenis = itemView.findViewById(R.id.m_jenis);
            m1 = itemView.findViewById(R.id.m_1);
            m2 = itemView.findViewById(R.id.m_2);
            m3 = itemView.findViewById(R.id.m_3);
            m4 = itemView.findViewById(R.id.m_4);
            m5 = itemView.findViewById(R.id.m_5);
            m6 = itemView.findViewById(R.id.m_6);
            m7 = itemView.findViewById(R.id.m_7);
        }
    }
}
