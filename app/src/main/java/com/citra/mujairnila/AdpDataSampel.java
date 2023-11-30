package com.citra.mujairnila;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdpDataSampel extends RecyclerView.Adapter {

    private List<modelDataSampel> dataList;

    public AdpDataSampel(List<modelDataSampel> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tabel_sampel, parent, false);

        return new RowViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RowViewHolder rowViewHolder = (RowViewHolder) holder;

        int rowPos = rowViewHolder.getAdapterPosition();

        if (rowPos == 0) {

            rowViewHolder.invariant.setBackgroundResource(R.drawable.border_header);
            rowViewHolder.nilai.setBackgroundResource(R.drawable.border_right);
            rowViewHolder.invariant.setTypeface(null, Typeface.BOLD);
            rowViewHolder.nilai.setTypeface(null, Typeface.BOLD);
            rowViewHolder.invariant.setTextSize(18f);
            rowViewHolder.nilai.setTextSize(18f);

            rowViewHolder.invariant.setText("Invariants");
            rowViewHolder.nilai.setText("Nilai");
        } else {
            modelDataSampel data = dataList.get(rowPos - 1);

            rowViewHolder.invariant.setBackgroundResource(R.drawable.border_field);
            rowViewHolder.nilai.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.invariant.setTextSize(18f);
            rowViewHolder.nilai.setTextSize(18f);

            rowViewHolder.invariant.setText(data.getMoments() + "");
            rowViewHolder.nilai.setText(String.format("%.7f",data.getNilai()));
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size() + 1;
    }

    public class RowViewHolder extends RecyclerView.ViewHolder {
        TextView invariant;
        TextView nilai;

        RowViewHolder(View itemView) {
            super(itemView);
            invariant = itemView.findViewById(R.id.tx_invariant);
            nilai = itemView.findViewById(R.id.tx_nilai);
        }
    }
}
