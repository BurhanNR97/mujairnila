package com.citra.mujairnila;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AdapRekap extends ArrayAdapter<modelUji> {
    private Context mContext;

    public AdapRekap(@NonNull Context context, ArrayList<modelUji> modelList) {
        super(context, 0, modelList);
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        View listitemView = view;
        if (listitemView == null) {
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.item_rekap, parent, false);
        }

        modelUji model = getItem(position);
        ImageView foto = listitemView.findViewById(R.id.itemUji_img);
        TextView id = listitemView.findViewById(R.id.itemUji_kode);
        TextView tanggal = listitemView.findViewById(R.id.itemUji_tanggal);
        TextView hasil = listitemView.findViewById(R.id.itemUji_hasil);

        id.setText(model.getId());
        tanggal.setText(model.getTanggal());
        if (model.getHasil().equals("Tidak Terdeteksi")) {
            hasil.setText("Tidak Terdetekdi");
        } else {
            hasil.setText(String.format("%.2f", model.getPersen()) + "% " + model.getHasil());
        }
        Glide.with(mContext).load(model.getUrl()).into(foto);

        String path = id.getText().toString().trim()+".jpg";
        StorageReference db = FirebaseStorage.getInstance().getReference("rgb");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("uji");


        View finalListitemView = listitemView;
        listitemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vie) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                builder.setCancelable(false);
                View v = LayoutInflater.from(getContext()).inflate(R.layout.dialog_popup, (ConstraintLayout) finalListitemView.findViewById(R.id.layoutDialogContainer));
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();

                TextView txTanya = v.findViewById(R.id.txtTanyaDialog);
                txTanya.setText("Ingin menghapus data ini ?");

                v.findViewById(R.id.btnYa).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        db.child(path).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                ref.child(model.getId()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                    }
                                });
                            }
                        });
                    }
                });

                v.findViewById(R.id.btnTidak).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                    }
                });

                if (alertDialog.getWindow()!=null) {
                    alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog.show();
            }
        });

        return listitemView;
    }
}
