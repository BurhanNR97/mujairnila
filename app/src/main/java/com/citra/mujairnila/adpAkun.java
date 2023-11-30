package com.citra.mujairnila;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class adpAkun extends BaseAdapter {
    private Context mContext;
    private ArrayList<modelUser> myList=  new ArrayList<>();

    public void setMyList(ArrayList<modelUser> myList) {
        this.myList = myList;
    }

    public adpAkun(Context mContext) {
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
            itemView = LayoutInflater.from(mContext).inflate(R.layout.item_akun, viewGroup, false);
        }

        ViewHolder viewHolder = new ViewHolder(itemView);
        modelUser model = (modelUser) getItem(i);
        viewHolder.bind(model);
        return itemView;
    }

    private class ViewHolder {
        TextView id, nama, user, pass;

        ViewHolder(View view) {
            id = view.findViewById(R.id.itemAkun_kode);
            nama = view.findViewById(R.id.itemAkun_nama);
            user = view.findViewById(R.id.itemAkun_user);
            pass = view.findViewById(R.id.itemAkun_pass);
        }

        void bind(modelUser model) {
            id.setText(model.getId());
            nama.setText(model.getNama());
            user.setText(model.getUsername());
            pass.setText(model.getPassword());
        }
    }
}
