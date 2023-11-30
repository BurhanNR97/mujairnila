package com.citra.mujairnila;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddSampel extends AppCompatActivity {
    final int kode_gallery = 1;
    public static final int MODE_CONTINUOUS = 1;
    public static final int MODE_ITERATIVE = 2;
    int k = 15;
    String m = "-1";
    int mode = 1;
    Bitmap bmp = null;
    Bitmap bp = null;
    ImageView gbr_input, kembali;
    AppCompatButton proses;
    Spinner spinJenis;
    List<modelDataSampel> listMoments;
    double[] listNilai = new double[7];

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sampel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        kembali = findViewById(R.id.back_addSampel);
        kembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        gbr_input = findViewById(R.id.imgSampel);
        gbr_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("outputX", 350);
                intent.putExtra("outputY", 200);
                intent.putExtra("aspectX", 16);
                intent.putExtra("aspectY", 9);
                intent.putExtra("return-data", true);
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
                startActivityForResult(intent, kode_gallery);
            }
        });

        spinJenis = findViewById(R.id.spinner_jenis);
        proses = findViewById(R.id.btnProses);
        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bmp == null) {
                    Toast.makeText(AddSampel.this, "Silahkan masukkan gambar", Toast.LENGTH_SHORT).show();
                } else
                if (spinJenis.getSelectedItemPosition() == 0) {
                    Toast.makeText(AddSampel.this, "Silahkan pilih jenis ikan", Toast.LENGTH_SHORT).show();
                } else {

                    Bitmap resize = scalling(AddSampel.this, bmp, bmp.getWidth(), bmp.getHeight());

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("sampel");
                    db.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getChildrenCount() > 0) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    int a = Integer.parseInt(ds.getKey());
                                    int b = a + 1;
                                    simpanData(db, String.valueOf(b));
                                }
                            } else {
                                simpanData(db, "10001");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });
    }

    private String desimal(double n) {
        double nilai = n * 1000f;
        return String.format("%.3f", nilai) + " x 10^-4";
    }

    private void simpanData(DatabaseReference db, String id) {
        Calendar calendar = Calendar.getInstance();
        //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddHHmmss");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String tgl = simpleDateFormat.format(calendar.getTime());

        modelSampel model = new modelSampel();
        model.setId(id);
        model.setJenis(spinJenis.getSelectedItem().toString());
        model.setTanggal(tgl);
        model.setM1(listNilai[0]);  model.setM2(listNilai[1]); model.setM3(listNilai[2]); model.setM4(listNilai[3]);
        model.setM5(listNilai[4]);  model.setM6(listNilai[5]); model.setM7(listNilai[6]);
        db.child(id).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    saveImage(bmp, bp, id);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(AddSampel.this, Sampel.class);
        intent.putExtra("i", getIntent().getStringExtra("i"));
        intent.putExtra("n", getIntent().getStringExtra("n"));
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == kode_gallery && resultCode == RESULT_OK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(AddSampel.this, R.style.AlertDialogTheme);
            builder.setCancelable(false);
            View v = LayoutInflater.from(AddSampel.this).inflate(R.layout.dialog_proses,
                    (LinearLayout) findViewById(R.id.dialogproses));
            builder.setView(v);
            final AlertDialog alertDialog = builder.create();
            if (alertDialog.getWindow() != null) {
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.show();

            Bundle extras = data.getExtras();
            bmp = (Bitmap) extras.getParcelable("data");

            /*Uri imgUri = data.getData();
            try {
                bmp = MediaStore.Images.Media.getBitmap(AddSampel.this.getContentResolver(), imgUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/

            gbr_input.setImageBitmap(bmp);

            //Canny canny = new Canny();
            CannyEdge canny = new CannyEdge();
            canny.process(bmp);
            bp = canny.getEdgesImage();

            //Bitmap bp = canny.process(resize, id);

            double[][] pikselGambar = getPikselMatrix(bp);
            HusMoment moment = new HusMoment();
            listMoments = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                int nmr = i + 1;
                listMoments.add(new modelDataSampel("M" + nmr, moment.getHuMoment(pikselGambar, nmr)));
                listNilai[i] = moment.getHuMoment(pikselGambar, nmr);
            }
            RecyclerView tampilTabel = findViewById(R.id.viewSampel);
            AdpDataSampel adapter = new AdpDataSampel(listMoments);
            LinearLayoutManager lm = new LinearLayoutManager(AddSampel.this);
            tampilTabel.setLayoutManager(lm);
            tampilTabel.setAdapter(adapter);
            alertDialog.cancel();
        }
    }

    private double[][] getPikselMatrix(Bitmap image) {
        int piksel;
        double [][] matriks = new double[image.getWidth()][image.getHeight()];
        int R, G, B;
        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                piksel = image.getPixel(i, j);
                R = Color.red(piksel);
                G = Color.green(piksel);
                B = Color.blue(piksel);

                double gray = (R + G + B) / 3;
                matriks[i][j] = gray;
            }
        }
        return matriks;
    }

    private Bitmap scalling (Context mContext, Bitmap bmp, int width, int height) {
        int w = bmp.getWidth();
        int h = bmp.getHeight();

        float densityFactor = mContext.getResources().getDisplayMetrics().density;
        float nW = width * densityFactor;
        float nH = height * densityFactor;

        float scalaW = nW / w;
        float scalaH = nH / h;

        Matrix matriks = new Matrix();
        matriks.postScale(scalaW, scalaH);
        return Bitmap.createBitmap(bmp, 0, 0, width, height, matriks, true);
    }

    private void saveImage(Bitmap rgb, Bitmap edges, String image_name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddSampel.this, R.style.AlertDialogTheme);
        builder.setCancelable(false);
        View v = LayoutInflater.from(AddSampel.this).inflate(R.layout.dialog_proses,
                (LinearLayout) findViewById(R.id.dialogproses));
        builder.setView(v);
        final AlertDialog alertDialog = builder.create();
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        rgb.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFoto = stream.toByteArray();
        String path = image_name + ".jpg";
        StorageReference ref = FirebaseStorage.getInstance().getReference("rgb");
        UploadTask task = ref.child(path).putBytes(byteFoto);
        task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    ByteArrayOutputStream streams = new ByteArrayOutputStream();
                    edges.compress(Bitmap.CompressFormat.JPEG, 100, streams);
                    byte[] byteImg = streams.toByteArray();
                    StorageReference ref = FirebaseStorage.getInstance().getReference("edge");
                    UploadTask uploadTask = ref.child(path).putBytes(byteImg);
                    uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(AddSampel.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                alertDialog.cancel();
                                onBackPressed();
                            }
                        }
                    });
                }
            }
        });
    }
}