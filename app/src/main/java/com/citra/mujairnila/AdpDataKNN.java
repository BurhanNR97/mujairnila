package com.citra.mujairnila;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class AdpDataKNN extends RecyclerView.Adapter {

    private List<modelDataKNN> dataList;

    public AdpDataKNN(List<modelDataKNN> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tabel_knn, parent, false);

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
            rowViewHolder.nilai.setBackgroundResource(R.drawable.border_right);

            rowViewHolder.no.setTypeface(null, Typeface.BOLD);
            rowViewHolder.kode.setTypeface(null, Typeface.BOLD);
            rowViewHolder.jenis.setTypeface(null, Typeface.BOLD);
            rowViewHolder.nilai.setTypeface(null, Typeface.BOLD);

            rowViewHolder.no.setTextSize(18f);
            rowViewHolder.kode.setTextSize(18f);
            rowViewHolder.jenis.setTextSize(18f);
            rowViewHolder.nilai.setTextSize(18f);

            rowViewHolder.no.setText("No");
            rowViewHolder.kode.setText("Kode");
            rowViewHolder.jenis.setText("Jenis Ikan");
            rowViewHolder.nilai.setText("Nilai");
        } else {
            modelDataKNN data = dataList.get(rowPos - 1);

            rowViewHolder.no.setBackgroundResource(R.drawable.border_field);
            rowViewHolder.kode.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.jenis.setBackgroundResource(R.drawable.border_bottom);
            rowViewHolder.nilai.setBackgroundResource(R.drawable.border_bottom);

            rowViewHolder.no.setTextSize(18f);
            rowViewHolder.kode.setTextSize(18f);
            rowViewHolder.jenis.setTextSize(18f);
            rowViewHolder.nilai.setTextSize(18f);

            rowViewHolder.no.setText(data.getNo() + "");
            rowViewHolder.kode.setText(data.getKode() + "");
            rowViewHolder.jenis.setText(data.getJennis() + "");
            rowViewHolder.nilai.setText(String.format("%.7f", data.getNilai()));
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
        TextView nilai;

        RowViewHolder(View itemView) {
            super(itemView);
            no = itemView.findViewById(R.id.knn_rank);
            kode = itemView.findViewById(R.id.knn_kode);
            jenis = itemView.findViewById(R.id.knn_jenis);
            nilai = itemView.findViewById(R.id.knn_nilai);
        }
    }
}
