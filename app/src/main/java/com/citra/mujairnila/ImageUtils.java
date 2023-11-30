package com.citra.mujairnila;

import android.graphics.Bitmap;

public class ImageUtils {
    protected Bitmap img;

    public ImageUtils(Bitmap img) {
        this.img = img;
    }

    public int[] start(){
        for(int i=0;i<img.getHeight(); i++){
            for(int j=0;j<img.getWidth(); j++){
                if(img.getPixel(j, i) != -1) return new int[] {j, i};
            }
        }
        return new int[] {-1, -1};
    }

    public int height(){
        int h=0, aux=0;
        for(int i=0;i<img.getWidth(); i++){
            aux=0;
            for(int j=0;j<img.getHeight(); j++) if(img.getPixel(i,  j) != -1) aux++;
            if(h < aux) h = aux;
        }
        return h;
    }

    public int width(){
        int w=0, aux=0;
        for(int i=0;i<img.getHeight();i++){
            aux=0;
            for(int j=0;j<img.getWidth();j++) if(img.getPixel(j, i) != -1) aux++;
            if(w < aux) w = aux;
        }
        return w;
    }
}