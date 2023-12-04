package com.citra.mujairnila;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.drjacky.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Pengujian extends AppCompatActivity {
    final int kode_gallery = 1;
    final int CAMERA_CAPTURE = 3;
    String inHasil = "";
    float inPersen = 0.0f;
    final int CROP_IMG = 2;
    private Uri uricrop;
    int[][] xTarget;
    List<Integer> bpKode;
    ArrayList<String> k = new ArrayList<>();
    ArrayList<Double> m1 = new ArrayList<>();
    ArrayList<Double> m2 = new ArrayList<>();
    ArrayList<Double> m3 = new ArrayList<>();
    ArrayList<Double> m4 = new ArrayList<>();
    ArrayList<Double> m5 = new ArrayList<>();
    ArrayList<Double> m6 = new ArrayList<>();
    ArrayList<Double> m7 = new ArrayList<>();
    ArrayList<String> j = new ArrayList<>();
    ArrayList<modelSampel> listSampel = new ArrayList<>();
    Bitmap bmp = null;
    String hhs = "";
    ImageView gbr_input, gbrGS, imgGray;
    TextView hasilUji;
    double[][] dbMoments= null;
    AlertDialog alertDialog = null;
    String[] dbKode = null;
    String[] dbJenis = null;
    AppCompatButton proses;
    NestedScrollView lyHitung;

    TextView gray_xn, gray_ny, gray_11, gray_12, gray_13, gray_14, gray_1n, gray_21, gray_22, gray_23, gray_24, gray_2n;
    TextView gray_31, gray_32, gray_33, gray_34, gray_3n, gray_41, gray_42, gray_43, gray_44, gray_4n;
    TextView gray_n1, gray_n2, gray_n3, gray_n4, gray_nn, ikan1, ikan2, persen1, persen2;
    EditText inputK;

    TextView komputasi;
    long mulai;
    long berenti = -1;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengujian);

        ImageView kemba = findViewById(R.id.back_pengujian);
        kemba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imgGray = findViewById(R.id.ujiGray);   gray_xn = findViewById(R.id.gray_xn);   gray_ny = findViewById(R.id.gray_ny);
        gray_11 = findViewById(R.id.gray_11);   gray_12 = findViewById(R.id.gray_12);   gray_13 = findViewById(R.id.gray_13);
        gray_14 = findViewById(R.id.gray_14);   gray_1n = findViewById(R.id.gray_1n);
        gray_21 = findViewById(R.id.gray_21);   gray_22 = findViewById(R.id.gray_22);   gray_23 = findViewById(R.id.gray_23);
        gray_24 = findViewById(R.id.gray_24);   gray_2n = findViewById(R.id.gray_2n);
        gray_31 = findViewById(R.id.gray_31);   gray_32 = findViewById(R.id.gray_32);   gray_33 = findViewById(R.id.gray_33);
        gray_34 = findViewById(R.id.gray_34);   gray_3n = findViewById(R.id.gray_3n);
        gray_41 = findViewById(R.id.gray_41);   gray_42 = findViewById(R.id.gray_42);   gray_43 = findViewById(R.id.gray_43);
        gray_44 = findViewById(R.id.gray_44);   gray_4n = findViewById(R.id.gray_4n);
        gray_n1 = findViewById(R.id.gray_n1);   gray_n2 = findViewById(R.id.gray_n2);   gray_n3 = findViewById(R.id.gray_n3);
        gray_n4 = findViewById(R.id.gray_n4);   gray_nn = findViewById(R.id.gray_nn);
        ikan1 = findViewById(R.id.rank_ikan1);  persen1 = findViewById(R.id.rank_persen1);
        ikan2 = findViewById(R.id.rank_ikan2);  persen2 = findViewById(R.id.rank_persen2);

        gbrGS = findViewById(R.id.ujiGS);
        komputasi = findViewById(R.id.waktuKomputasi);
        gbr_input = findViewById(R.id.imgUji);
        lyHitung = findViewById(R.id.lyHitung);
        inputK = findViewById(R.id.nilaiK);
        gbr_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.github.dhaval2404.imagepicker.ImagePicker.with(Pengujian.this)
                        .crop(350, 200)
                        .maxResultSize(350, 200)
                        .start();
            }
        });

        hasilUji = findViewById(R.id.hasilUji);
        proses = findViewById(R.id.btn_proses);
        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bmp == null) {
                    Toast.makeText(Pengujian.this, "Silahkan masukkan gambar", Toast.LENGTH_SHORT).show();
                } else
                if (inputK.getText().toString().isEmpty()) {
                    inputK.requestFocus();
                    inputK.setError("Input K");
                } else {
                    mulai = System.currentTimeMillis();

                    int iniK = Integer.parseInt(inputK.getText().toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(Pengujian.this, R.style.AlertDialogTheme);
                    builder.setCancelable(false);
                    View v = LayoutInflater.from(Pengujian.this).inflate(R.layout.dialog_proses,
                            (LinearLayout) findViewById(R.id.dialogproses));
                    builder.setView(v);
                    alertDialog = builder.create();
                    if (alertDialog.getWindow() != null) {
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    alertDialog.show();
                    lyHitung.setVisibility(View.VISIBLE);

                    Bitmap bitmapp = setGrayscale(bmp);

                    CannyEdge canny = new CannyEdge();
                    canny.process(bitmapp);
                    Bitmap bp = canny.getEdgesImage();

                    double[][] pikselGambar = getPikselMatrix(bp);
                    HusMoment moment = new HusMoment();

                    double[] listMoments = new double[7];
                    List<modelDataSampel> listt = new ArrayList<>();
                    for (int i = 0; i < 5; i++) {
                        int nmr = i + 1;
                        listMoments[i] = moment.getHuMoment(pikselGambar, i + 1);
                        listt.add(new modelDataSampel("M" + nmr, moment.getHuMoment(pikselGambar, nmr)));
                    }

                    RecyclerView tampilTabel = findViewById(R.id.viewUjiiii);
                    AdpDataSampel adapter = new AdpDataSampel(listt);
                    LinearLayoutManager lm = new LinearLayoutManager(Pengujian.this);
                    tampilTabel.setLayoutManager(lm);
                    tampilTabel.setAdapter(adapter);

                    float nm = 0f;

                    double[][] bpUji = new double[listMoments.length][4];
                    for (int i=0; i<4; i++) {
                        bpUji[0][i] = listMoments[i];
                    }

                    if (dbMoments != null) {

                        double[] hasil = new double[dbMoments.length];
                        for (int i=0; i<dbMoments.length; i++) {

                            //Manhattan Distance
                            double m1 = Math.abs(dbMoments[i][0] - listMoments[0]);
                            double m2 = Math.abs(dbMoments[i][1] - listMoments[1]);
                            double m3 = Math.abs(dbMoments[i][2] - listMoments[2]);
                            double m4 = Math.abs(dbMoments[i][3] - listMoments[3]);
                            double m5 = Math.abs(dbMoments[i][4] - listMoments[4]);
                            hasil[i] = m1 + m2 + m3 + m4 + m5;
                        }

                        String[] hKode = new String[dbMoments.length];
                        double[] hNilai = new double[dbMoments.length];
                        String[] hJenis = new String[dbMoments.length];

                        for (int r=0; r<hasil.length; r++) {
                            hKode[r] = dbKode[r];
                            hNilai[r] = hasil[r];
                            hJenis[r] = dbJenis[r];
                        }
                        for (int x=0; x<dbMoments.length; x++) {
                            for (int y=x; y<dbMoments.length; y++) {
                                if (hNilai[x] > hNilai[y]) {
                                    String tm = hKode[x];
                                    hKode[x] = hKode[y];
                                    hKode[y] = tm;

                                    double tmp = hNilai[x];
                                    hNilai[x] = hNilai[y];
                                    hNilai[y] = tmp;

                                    String t = hJenis[x];
                                    hJenis[x] = hJenis[y];
                                    hJenis[y] = t;
                                }
                            }
                        }

                        List<modelData15> model15 = new ArrayList<>();
                        for (int i = 0; i < iniK; i++) {
                            int nmr = i + 1;
                            int pos = k.indexOf(hKode[i]);
                            model15.add(new modelData15(nmr, listSampel.get(pos).getId(), listSampel.get(pos).getJenis(),
                                                        listSampel.get(pos).getM1(), listSampel.get(pos).getM2(),
                                                        listSampel.get(pos).getM3(), listSampel.get(pos).getM4(),
                                                        listSampel.get(pos).getM5(), listSampel.get(pos).getM6(),
                                                        listSampel.get(pos).getM7()));
                        }
                        RecyclerView tampilDB = findViewById(R.id.viewdb);
                        AdpData15 adapterDB = new AdpData15(model15);
                        LinearLayoutManager lDB = new LinearLayoutManager(Pengujian.this);
                        tampilDB.setLayoutManager(lDB);
                        tampilDB.setAdapter(adapterDB);

                        List<modelDataKNN> modelKNN = new ArrayList<>();
                        for (int i = 0; i<iniK; i++) {
                            int nmr = i + 1;
                            modelKNN.add(new modelDataKNN(nmr, hKode[i], hJenis[i], hNilai[i]));
                        }
                        RecyclerView tampilKNN = findViewById(R.id.viewknn);
                        AdpDataKNN adapterKNN = new AdpDataKNN(modelKNN);
                        LinearLayoutManager lKNN = new LinearLayoutManager(Pengujian.this);
                        tampilKNN.setLayoutManager(lKNN);
                        tampilKNN.setAdapter(adapterKNN);

                        ArrayList<Integer> mujair = new ArrayList<>();
                        ArrayList<Integer> nila = new ArrayList<>();

                        for (int z=0; z<iniK; z++) {
                            if (hJenis[z].equals("Ikan Mujair")) {
                                mujair.add(1);
                            } else
                            if (hJenis[z].equals("Ikan Nila")) {
                                nila.add(1);
                            }
                        }

                        String[][] hsl = new String[2][3];
                        float[] hitung = new float[2];
                        float iK = iniK * 1f;
                        hitung[0] = (mujair.size() / iK) * 100f;
                        hitung[1] = (nila.size() / iK) * 100f;

                        if (hitung[0] > hitung[1]) {
                            hhs = "Ikan Mujair";
                            nm = hitung[0];
                            hsl[0][0] = "1";    hsl[0][1] = hhs;    hsl[0][2] = String.format("%.2f", nm) + " %";
                            hsl[1][0] = "2";    hsl[1][1] = "Ikan Nila";    hsl[1][2] = String.format("%.2f", hitung[1]) + " %";
                        } else {
                            hhs = "Ikan Nila";
                            nm = hitung[1];
                            hsl[0][0] = "1";    hsl[0][1] = hhs;    hsl[0][2] = String.format("%.2f", nm) + " %";
                            hsl[1][0] = "2";    hsl[1][1] = "Ikan Mujair";    hsl[1][2] = String.format("%.2f", hitung[0]) + " %";
                        }
                        inHasil = hhs;
                        inPersen = Float.parseFloat(String.format("%.2f", nm).replace(",","."));
                        hasilUji.setText(hhs + " " + String.format("%.2f", nm) + "%");

                        ikan1.setText(hsl[0][1]);   persen1.setText(hsl[0][2]);
                        ikan2.setText(hsl[1][1]);   persen2.setText(hsl[1][2]);
                    }

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("d MMM yyyy");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
                    String tgl = simpleDateFormat.format(calendar.getTime());
                    String id = sdf.format(calendar.getTime());

                    modelUji model = new modelUji();
                    model.setId(id);
                    model.setTanggal(tgl);
                    model.setHasil(inHasil);
                    model.setPersen(inPersen);
                    saveImage(model, bmp, bp, id);

                    berenti = System.currentTimeMillis();
                    long mili = elapsed();
                    int dtk = (int) (mili / 1000) % 60;
                    int mnt = (int) ((mili / (1000 * 60)) % 60);
                    int jam = (int) ((mili / (1000 * 60 * 60)) % 24);
                    int mdt = (int) (mili - (dtk * 1000) - (mnt * 1000 * 60) - (jam * 1000 * 60 * 60));
                    if (mnt == 0) {
                        komputasi.setText("Waktu Komputasi: " + dtk + " detik (" + mdt + " milidetik)");
                    } else {
                        komputasi.setText("Waktu Komputasi: " + mnt + " menit " + dtk + " detik (" + mdt + " milidetik)");
                    }
                }
            }
        });
    }

    private long elapsed() {
        if (berenti >= 0) {
            return berenti - mulai;
        }
        return System.currentTimeMillis() - mulai;
    }

    @Override
    protected void onResume() {
        super.onResume();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference("sampel");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int n = (int) snapshot.getChildrenCount();
                dbKode = new String[n];
                bpKode = new ArrayList<>();
                dbJenis = new String[n];
                dbMoments = new double[n][7];
                xTarget = new int[n][1];

                for (DataSnapshot ds : snapshot.getChildren()) {
                    modelSampel model = ds.getValue(modelSampel.class);
                    k.add(model.getId());   m1.add(model.getM1());
                    m2.add(model.getM2());  m3.add(model.getM3());
                    m4.add(model.getM4());  m5.add(model.getM5());
                    m6.add(model.getM6());  m7.add(model.getM7());
                    j.add(model.getJenis());

                    if (model.getJenis().equals("Ikan Mujair")) {
                        bpKode.add(0);
                    } else {
                        bpKode.add(1);
                    }

                    listSampel.add(model);
                }

                for (int i=0; i<n; i++) {
                    dbKode[i] = k.get(i);
                    dbJenis[i] = j.get(i);
                    dbMoments[i][0] = m1.get(i);
                    dbMoments[i][1] = m2.get(i);
                    dbMoments[i][2] = m3.get(i);
                    dbMoments[i][3] = m4.get(i);
                    dbMoments[i][4] = m5.get(i);
                    dbMoments[i][5] = m6.get(i);
                    dbMoments[i][6] = m7.get(i);

                    if (j.get(i).equals("Ikan Mujair")) {
                        xTarget[i][0] = 0;
                    } else {
                        xTarget[i][0] = 1;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private double convertD(float a) {
        double i = 0;
        double n = Math.getExponent(a);
        i = jumlahnol(n);
        return i;
    }

    private int jumlahnol(double i) {
        int a = 0;
        String b = "1";
        for (int x=0; x<i; x++) {
            b += "0";
        }
        a = Integer.parseInt(b);
        return a;
    }

   @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Pengujian.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            Uri imgUri = data.getData();
            try {
                bmp = MediaStore.Images.Media.getBitmap(Pengujian.this.getContentResolver(), imgUri);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            gbr_input.setImageBitmap(bmp);
        }
    }

    private double[][] getPikselMatrix(Bitmap image) {
        int piksel;
        Bitmap bp = Bitmap.createBitmap(image.getWidth(), image.getHeight(), image.getConfig());
        double [][] matriks = new double[image.getWidth()][image.getHeight()];
        int R, G, B;

        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                piksel = image.getPixel(i, j);
                R = Color.red(piksel);
                G = Color.green(piksel);
                B = Color.blue(piksel);

                float gray = (R + G + B) / 3f;
                matriks[i][j] = (double) gray;
                bp.setPixel(i, j, Color.rgb((int)gray, (int)gray, (int)gray));
            }
        }
        imgGray.setImageBitmap(bp);

        return matriks;
    }

    private void saveImage(modelUji model, Bitmap rgb, Bitmap edges, String image_name) {

        DatabaseReference dbb = FirebaseDatabase.getInstance().getReference("uji");

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        edges.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteFoto = stream.toByteArray();
        String path = image_name + ".jpg";
        StorageReference ref = FirebaseStorage.getInstance().getReference("edge");
        UploadTask task = ref.child(path).putBytes(byteFoto);
        task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    ByteArrayOutputStream streams = new ByteArrayOutputStream();
                    rgb.compress(Bitmap.CompressFormat.JPEG, 100, streams);
                    byte[] byteImg = streams.toByteArray();
                    StorageReference ref = FirebaseStorage.getInstance().getReference("rgb");
                    UploadTask uploadTask = ref.child(path).putBytes(byteImg);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    model.setUrl(task.getResult().toString());
                                    dbb.child(image_name).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            Toast.makeText(Pengujian.this, "Data berhasil disimpan", Toast.LENGTH_SHORT).show();
                                            alertDialog.cancel();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private Bitmap setGrayscale(Bitmap image) {
        int piksel;
        Bitmap bp = Bitmap.createBitmap(image.getWidth(), image.getHeight(), Bitmap.Config.ARGB_8888);
        double [][] matriks = new double[image.getWidth()][image.getHeight()];
        int R, G, B;

        for (int i = 0; i < image.getWidth(); ++i) {
            for (int j = 0; j < image.getHeight(); ++j) {
                piksel = image.getPixel(i, j);
                R = Color.red(piksel);
                G = Color.green(piksel);
                B = Color.blue(piksel);

                float gray = (R + G + B) / 3f;
                matriks[i][j] = (double) gray;
                bp.setPixel(i, j, Color.rgb((int)gray, (int)gray, (int)gray));
            }
        }
        gbrGS.setImageBitmap(bp);

        gray_ny.setText("" + bp.getHeight());    gray_xn.setText("" + bp.getWidth());
        int y = bp.getHeight() - 1;              int x = bp.getWidth() - 1;

        gray_11.setText("" + convFloatToInt(matriks[0][0]));   gray_12.setText("" + convFloatToInt(matriks[0][1]));
        gray_13.setText("" + convFloatToInt(matriks[0][2]));   gray_14.setText("" + convFloatToInt(matriks[0][3]));
        gray_14.setText("" + convFloatToInt(matriks[0][4]));   gray_1n.setText("" + convFloatToInt(matriks[0][y]));

        gray_21.setText("" + convFloatToInt(matriks[1][0]));   gray_22.setText("" + convFloatToInt(matriks[1][1]));
        gray_23.setText("" + convFloatToInt(matriks[1][2]));   gray_24.setText("" + convFloatToInt(matriks[1][3]));
        gray_24.setText("" + convFloatToInt(matriks[1][4]));   gray_2n.setText("" + convFloatToInt(matriks[1][y]));

        gray_31.setText("" + convFloatToInt(matriks[2][0]));   gray_32.setText("" + convFloatToInt(matriks[2][1]));
        gray_33.setText("" + convFloatToInt(matriks[2][2]));   gray_34.setText("" + convFloatToInt(matriks[2][3]));
        gray_34.setText("" + convFloatToInt(matriks[2][4]));   gray_3n.setText("" + convFloatToInt(matriks[2][y]));

        gray_41.setText("" + convFloatToInt(matriks[3][0]));   gray_42.setText("" + convFloatToInt(matriks[3][1]));
        gray_43.setText("" + convFloatToInt(matriks[3][2]));   gray_44.setText("" + convFloatToInt(matriks[3][3]));
        gray_44.setText("" + convFloatToInt(matriks[3][4]));   gray_4n.setText("" + convFloatToInt(matriks[3][y]));

        gray_n1.setText("" + convFloatToInt(matriks[x][0]));   gray_n2.setText("" + convFloatToInt(matriks[x][1]));
        gray_n3.setText("" + convFloatToInt(matriks[x][2]));   gray_n4.setText("" + convFloatToInt(matriks[x][3]));
        gray_n4.setText("" + convFloatToInt(matriks[x][4]));   gray_nn.setText("" + convFloatToInt(matriks[x][y]));
        return bp;
    }

    private double doubleKoma2(double a) {
        return a;
        //String s = String.format("%2.f", a);
        //return Double.parseDouble(s);
    }

    private int convFloatToInt(double a) {
        return (int)a;
    }
}