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

    private boolean imageLoaded;
    private boolean imageLoading;

    private int w_pix;
    private int h_pix;

    private String imageUri;

    public PhotoButton(Context context, String imageUri) {
        super(context);
        super.setImageBitmap(BLANK_BITMAP);

        this.imageUri = imageUri;

        imageLoaded = false;
        imageLoading = false;

        w_pix = DEFAULT_W_PIX;
        h_pix = DEFAULT_H_PIX;
    }

    public void setSize(int w_pix, int h_pix) {
        this.w_pix = w_pix;
        this.h_pix = h_pix;
    }

    public void loadImage() {
        setImageBitmap(ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(imageUri), w_pix, h_pix, MediaStore.Images.Thumbnails.MICRO_KIND));
        imageLoaded = true;
        imageLoading = false;
    }

    public void setImage(Bitmap image) {
        setImageBitmap(image);
        imageLoaded = true;
        imageLoading = false;
    }

    public boolean isImageLoaded() {
        return imageLoaded;
    }

    public boolean isImageLoading() {
        return imageLoading;
    }

    public void setImageLoading(boolean isloading) {
        this.imageLoading = isloading;
    }

    public String getImageUri() {
        return imageUri;
    }

    public int getW_pix() {
        return w_pix;
    }

    public int getH_pix() {
        return h_pix;
    }

    public void unloadImage() {
        if (imageLoaded) {
            setImageBitmap(BLANK_BITMAP);
            imageLoaded = false;
        }
    }

}
