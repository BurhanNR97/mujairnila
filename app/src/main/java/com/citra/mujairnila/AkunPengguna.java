package com.citra.mujairnila;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AkunPengguna extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private adpAkun adapter;
    private ArrayList<modelUser> list;
    private DatabaseReference db;
    ListView layout;
    TextView kosong;
    FloatingActionButton fab;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_akun_pengguna);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        ImageView kem = findViewById(R.id.back_akun);
        kem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        adapter = new adpAkun(AkunPengguna.this);
        list = new ArrayList<>();
        db = FirebaseDatabase.getInstance().getReference("users");
        layout = findViewById(R.id.rvakun);
        kosong = findViewById(R.id.kosong_akun);
        fab = findViewById(R.id.fabAkun);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AkunPengguna.this, R.style.AlertDialogTheme);
                builder.setCancelable(false);
                View v = LayoutInflater.from(AkunPengguna.this).inflate(R.layout.tambahdata,
                        (LinearLayout) findViewById(R.id.layoutTambahh));
                builder.setView(v);

                final AlertDialog alertDialog = builder.create();
                TextView info = v.findViewById(R.id.Tambah_info);
                info.setText("TAMBAH DATA");
                v.findViewById(R.id.tamba_hapus).setVisibility(View.GONE);

                EditText getkode = v.findViewById(R.id.Tambah_AkunID);
                EditText getnama = v.findViewById(R.id.Tambah_AkunNama);
                EditText getusername = v.findViewById(R.id.Tambah_AkunUser);
                EditText getpassword = v.findViewById(R.id.Tambah_AkunPassword);
                TextInputLayout hh = v.findViewById(R.id.idididid);

                String m = list.get(list.size() - 1).getId().substring(2);
                int id = Integer.parseInt(m) + 1;

                if (String.valueOf(id).length() == 1) {
                    getkode.setText("ID00" + id);
                } else
                if (String.valueOf(id).length() == 2) {
                    getkode.setText("ID0" + id);
                } else
                if (String.valueOf(id).length() == 3) {
                    getkode.setText("ID" + id);
                }

                errorr(getnama); errorr(getusername); errorr(getpassword);

                v.findViewById(R.id.tamba_simpan).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getnama.getText().toString().isEmpty()) {
                            getnama.requestFocus();
                            getnama.setError("Masukkan nama");
                        } else
                        if (getusername.getText().toString().isEmpty()) {
                            getusername.requestFocus();
                            getusername.setError("Masukkan username");
                        } else
                        if (getpassword.getText().toString().isEmpty()) {
                            getpassword.requestFocus();
                            getpassword.setError("Masukkan password");
                        } else
                        if (getpassword.getText().toString().length() < 4) {
                            getpassword.requestFocus();
                            getpassword.setError("Password minimal 4 karakter");
                        } else {
                            int n = cekUsername(getusername.getText().toString());
                            if (list.get(n).getUsername().equals(getusername.getText().toString())) {
                                getusername.requestFocus();
                                getusername.setError("Username sudah digunakan");
                            } else {
                                modelUser model = new modelUser();
                                model.setId(getkode.getText().toString());
                                model.setNama(getnama.getText().toString());
                                model.setUsername(getusername.getText().toString());
                                model.setPassword(getpassword.getText().toString());
                                db.child(getkode.getText().toString()).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(AkunPengguna.this, "Data berhasil ditambah", Toast.LENGTH_SHORT).show();
                                            alertDialog.cancel();
                                        }
                                    }
                                });
                            }
                        }
                    }
                });

                v.findViewById(R.id.tamba_batal).setOnClickListener(new View.OnClickListener() {
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
        });

        layout.setOnItemClickListener(AkunPengguna.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelUser model = ds.getValue(modelUser.class);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AkunPengguna.this, Admin.class);
        intent.putExtra("i", getIntent().getStringExtra("i"));
        intent.putExtra("n", getIntent().getStringExtra("n"));
        startActivity(intent);
        finish();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        TextView id = view.findViewById(R.id.itemAkun_kode);
        final String getId = id.getText().toString().trim();

        AlertDialog.Builder builder = new AlertDialog.Builder(AkunPengguna.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View v = LayoutInflater.from(AkunPengguna.this).inflate(R.layout.tambahdata,
                (LinearLayout) findViewById(R.id.layoutTambahh));
        builder.setView(v);

        final AlertDialog alertDialog = builder.create();
        TextView info = v.findViewById(R.id.Tambah_info);
        info.setText("UBAH DATA");

        EditText getkode = v.findViewById(R.id.Tambah_AkunID);
        EditText getnama = v.findViewById(R.id.Tambah_AkunNama);
        EditText getusername = v.findViewById(R.id.Tambah_AkunUser);
        EditText getpassword = v.findViewById(R.id.Tambah_AkunPassword);

        final String[] oldUser = {""};

        db.child(getId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                getkode.setText(snapshot.child("id").getValue().toString());
                getnama.setText(snapshot.child("nama").getValue().toString());
                oldUser[0] = snapshot.child("username").getValue().toString();
                getusername.setText(snapshot.child("username").getValue().toString());
                getpassword.setText(snapshot.child("password").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        errorr(getnama); errorr(getusername); errorr(getpassword);

        v.findViewById(R.id.tamba_simpan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getnama.getText().toString().isEmpty()) {
                    getnama.requestFocus();
                    getnama.setError("Masukkan nama");
                } else
                if (getusername.getText().toString().isEmpty()) {
                    getusername.requestFocus();
                    getusername.setError("Masukkan username");
                } else
                if (getpassword.getText().toString().isEmpty()) {
                    getpassword.requestFocus();
                    getpassword.setError("Masukkan password");
                } else
                if (getpassword.getText().toString().length() < 4) {
                    getpassword.requestFocus();
                    getpassword.setError("Password minimal 4 karakter");
                } else {
                    if (oldUser[0].equals(getusername.getText().toString())) {
                        Map<String, Object> map = new HashMap<>();
                        map.put("id", getkode.getText().toString());
                        map.put("nama", getnama.getText().toString());
                        map.put("username", getusername.getText().toString());
                        map.put("password", getpassword.getText().toString());
                        db.child(getkode.getText().toString()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AkunPengguna.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                                    alertDialog.cancel();
                                }
                            }
                        });
                    } else {
                        int n = cekUsername(getusername.getText().toString());
                        if (list.get(n).getUsername().equals(getusername.getText().toString())) {
                            getusername.requestFocus();
                            getusername.setError("Username sudah digunakan");
                        } else {
                            Map<String, Object> map = new HashMap<>();
                            map.put("id", getkode.getText().toString());
                            map.put("nama", getnama.getText().toString());
                            map.put("username", getusername.getText().toString());
                            map.put("password", getpassword.getText().toString());
                            db.child(getkode.getText().toString()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AkunPengguna.this, "Data berhasil diubah", Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });

        v.findViewById(R.id.tamba_hapus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(AkunPengguna.this, R.style.AlertDialogTheme);
                builder1.setCancelable(false);
                View v1 = LayoutInflater.from(AkunPengguna.this).inflate(R.layout.dialog_popup,
                        (ConstraintLayout) findViewById(R.id.layoutDialogContainer));
                builder1.setView(v1);

                final AlertDialog alertDialog1 = builder1.create();

                v1.findViewById(R.id.btnYa).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (getkode.getText().toString().equals("ID001")) {
                            Toast.makeText(AkunPengguna.this, "Tidak dapat menghapus data ini", Toast.LENGTH_SHORT).show();
                            alertDialog1.cancel();
                        } else {
                            db.child(getkode.getText().toString()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(AkunPengguna.this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
                                        alertDialog.cancel();
                                        alertDialog1.cancel();
                                    }
                                }
                            });
                        }
                    }
                });

                v1.findViewById(R.id.btnTidak).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog1.cancel();
                    }
                });

                if (alertDialog1.getWindow() != null) {
                    alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                }
                alertDialog1.show();
            }
        });

        v.findViewById(R.id.tamba_batal).setOnClickListener(new View.OnClickListener() {
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

    private void errorr(TextView teks) {
        teks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!teks.getText().toString().isEmpty()) {
                    teks.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
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