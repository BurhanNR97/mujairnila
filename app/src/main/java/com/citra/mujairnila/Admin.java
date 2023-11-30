package com.citra.mujairnila;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Admin extends AppCompatActivity {
    String id = "";
    String nama = "";
    TextView txNama, txId;
    DatabaseReference dbUser;
    DatabaseReference dbSampel;
    DatabaseReference dbRekap;
    TextView qty_akun, qty_sampel, qty_rekap;
    LinearLayout ly_akun, ly_sampel, ly_rekap;
    AppCompatButton logout;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        dbSampel = FirebaseDatabase.getInstance().getReference("sampel");
        dbUser = FirebaseDatabase.getInstance().getReference("users");
        dbRekap = FirebaseDatabase.getInstance().getReference("uji");
        txNama = findViewById(R.id.admin_namaa);
        txId = findViewById(R.id.admin_nik);
        qty_sampel = findViewById(R.id.qty_citra);
        qty_rekap = findViewById(R.id.qty_rekap);
        qty_akun = findViewById(R.id.qty_akun);
        ly_sampel = findViewById(R.id.ly_citra);
        ly_rekap = findViewById(R.id.ly_rekap);
        ly_akun = findViewById(R.id.ly_akun);

        logout = findViewById(R.id.admin_logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Admin.this, R.style.AlertDialogTheme);
                builder.setCancelable(false);
                View v = LayoutInflater.from(Admin.this).inflate(R.layout.dialog_popup,
                        (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
                builder.setView(v);
                final AlertDialog alertDialog = builder.create();

                TextView txTanya = v.findViewById(R.id.txtTanyaDialog);
                txTanya.setText("Anda ingin logout ?");

                v.findViewById(R.id.btnYa).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.cancel();
                        startActivity(new Intent(Admin.this, MainActivity.class));
                        finish();
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

        ly_sampel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this, Sampel.class);
                intent.putExtra("i", id);
                intent.putExtra("n", nama);
                startActivity(intent);
                finish();
            }
        });

        ly_rekap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this, Rekap.class);
                intent.putExtra("i", id);
                intent.putExtra("n", nama);
                startActivity(intent);
                finish();
            }
        });

        ly_akun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Admin.this, AkunPengguna.class);
                intent.putExtra("i", id);
                intent.putExtra("n", nama);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        id = getIntent().getStringExtra("i");
        nama = getIntent().getStringExtra("n");
        txId.setText(id);
        txNama.setText(nama);

        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qty_akun.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbSampel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qty_sampel.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dbRekap.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                qty_rekap.setText("" + snapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}