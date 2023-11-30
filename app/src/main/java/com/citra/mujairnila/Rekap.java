package com.citra.mujairnila;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Rekap extends AppCompatActivity {
    private AdapRekap adapter;
    private ArrayList<modelUji> list;
    private DatabaseReference db;
    ListView layout;
    TextView kosong;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rekap);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        list = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("uji");
        layout = findViewById(R.id.rvujii);
        kosong = findViewById(R.id.kosong_uji);

        ImageView kembali = findViewById(R.id.back_rekap);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    modelUji model = dataSnapshot.getValue(modelUji.class);
                    list.add(model);
                }

                adapter = new AdapRekap(Rekap.this, list);
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
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Rekap.this, Admin.class);
        intent.putExtra("i", getIntent().getStringExtra("i"));
        intent.putExtra("n", getIntent().getStringExtra("n"));
        startActivity(intent);
        finish();
    }
}