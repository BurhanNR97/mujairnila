package com.citra.mujairnila;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity {
    ArrayList<modelUser> list;
    DatabaseReference db;
    AppCompatButton batal, login;
    TextView usr, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        batal = findViewById(R.id.btLoginBatal);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        db = FirebaseDatabase.getInstance().getReference("users");
        usr = findViewById(R.id.login_user);
        pass = findViewById(R.id.login_pass);
        login = findViewById(R.id.btLogin);

    }

    @Override
    protected void onResume() {
        super.onResume();
        list = new ArrayList<>();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelUser model = ds.getValue(modelUser.class);
                    list.add(model);
                }

                login.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String username = usr.getText().toString().trim();
                        String password = pass.getText().toString().trim();

                        if (username.isEmpty() || password.isEmpty()) {
                            Toast.makeText(Login.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                        } else {
                            int n = cekUsername(username);
                            String user = list.get(n).getUsername();
                            String pas = list.get(n).getPassword();
                            String nama = list.get(n).getNama();
                            String id = list.get(n).getId();
                            if (user.equals(username) && pas.equals(password)) {
                                Intent intent = new Intent(Login.this, Admin.class);
                                intent.putExtra("u", user);
                                intent.putExtra("i", id);
                                intent.putExtra("n", nama);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login.this, "username atau passowrd salah", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }

    private int cekUsername (String username) {
        int indeks = 0;
        for (int n = 0; n < list.size(); n++) {
            String ambil = list.get(n).getUsername();
            if (ambil.equals(username)) {
                indeks = n;
            }
        }
        return indeks;
    }
}