package com.citra.mujairnila;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Sampel extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private adpSampel adapter;
    private ArrayList<modelSampel> list;
    private DatabaseReference db;
    ListView layout;
    TextView kosong;
    FloatingActionButton fab;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sampel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        ImageView kembali = findViewById(R.id.back_Sampel);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        adapter = new adpSampel(Sampel.this);
        list = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("sampel");
        layout = findViewById(R.id.rvsampel);
        kosong = findViewById(R.id.kosong_sampel);
        fab = findViewById(R.id.fabSampel);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Sampel.this, AddSampel.class));
                finish();
            }
        });

        layout.setOnItemClickListener(Sampel.this);
    }

    public void setListView() {
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelSampel model = ds.getValue(modelSampel.class);
                    list.add(model);
                }

                adapter.setMyList(list);
                layout.setAdapter(adapter);

                if (list.size() == 0) {
                    layout.setVisibility(View.GONE);
                    kosong.setVisibility(View.VISIBLE);
                } else {
                    layout.setVisibility(View.VISIBLE);
                    kosong.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setListView();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Sampel.this, Admin.class);
        intent.putExtra("i", getIntent().getStringExtra("i"));
        intent.putExtra("n", getIntent().getStringExtra("n"));
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView id = view.findViewById(R.id.itemSampel_kode);
        final String getId = id.getText().toString().trim();

        AlertDialog.Builder builder = new AlertDialog.Builder(Sampel.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View v = LayoutInflater.from(Sampel.this).inflate(R.layout.dialog_popup,
                (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        TextView tanya = v.findViewById(R.id.txtTanyaDialog);
        tanya.setText("Anda ingin menghapus data ini ?");

        v.findViewById(R.id.btnYa).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.child(getId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Sampel.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                            alertDialog.cancel();
                        }
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

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}