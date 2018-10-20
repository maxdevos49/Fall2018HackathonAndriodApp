package com.example.naber.fall2018hackathonandroidapp.photopicker.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

public class PhotoButton extends android.support.v7.widget.AppCompatImageButton {

    private static final int DEFAULT_W_PIX = 100;
    private static final int DEFAULT_H_PIX = 100;

    private static final Bitmap BLANK_BITMAP;
    static {
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        BLANK_BITMAP = Bitmap.createBitmap(DEFAULT_W_PIX, DEFAULT_H_PIX, conf);
    }

    private String imageURI;

    boolean imageLoaded;

    private int w_pix;
    private int h_pix;

    public PhotoButton(Context context, String imageURI) {
        super(context);

        this.imageURI = imageURI;
        super.setImageBitmap(BLANK_BITMAP);

        imageLoaded = false;
        w_pix = DEFAULT_W_PIX;
        h_pix = DEFAULT_H_PIX;
    }

    public void setSize(int w_pix, int h_pix) {
        this.w_pix = w_pix;
        this.h_pix = h_pix;
    }

    public void loadImage() {
        if (!imageLoaded) {
            setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageURI), w_pix, h_pix, MediaStore.Images.Thumbnails.MICRO_KIND));
            imageLoaded = true;
        }
    }

    public void unloadImage() {
        if (imageLoaded) {
            setImageBitmap(BLANK_BITMAP);
            imageLoaded = false;
        }
    }

}
