package com.citra.mujairnila;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class adpSampel extends BaseAdapter {
    private Context mContext;
    private ArrayList<modelSampel> myList=  new ArrayList<>();

    public void setMyList(ArrayList<modelSampel> myList) {
        this.myList = myList;
    }

    public adpSampel(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int i) {
        return myList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = view;

        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_sampel, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);
        modelSampel model = (modelSampel) getItem(i);
        viewHolder.bind(model);
        return itemView;
    }

    private class ViewHolder {
        private ImageView foto;
        TextView id, jenis;

        ViewHolder(View view) {
            foto = view.findViewById(R.id.itemSampel_img);
            id = view.findViewById(R.id.itemSampel_kode);
            jenis = view.findViewById(R.id.itemSampel_nama);
        }

        void bind(modelSampel model) {
            id.setText(model.getId());
            jenis.setText(model.getJenis());
            Glide.with(mContext).load(model.getUrl()).into(foto);
        }
    }
}
