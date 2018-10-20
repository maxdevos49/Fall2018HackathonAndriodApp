package com.example.naber.fall2018hackathonandroidapp.photopicker.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import java.util.HashMap;
import java.util.Map;

public class PhotoButton extends android.support.v7.widget.AppCompatImageButton {

    private static final int DEFAULT_W_PIX = 100;
    private static final int DEFAULT_H_PIX = 100;

    private static final Map<String, Bitmap> DEFAULTS = new HashMap<>();

    private static Bitmap getDefault(int width, int height) {
        String identifier = width + "x" + height;
        if (DEFAULTS.containsKey(identifier)) {
            return DEFAULTS.get(identifier);
        }

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap newDefault = Bitmap.createBitmap(width, height, conf);
        DEFAULTS.put(identifier, newDefault);
        return newDefault;
    }

    private boolean imageLoaded;
    private boolean imageLoading;

    private int w_pix;
    private int h_pix;

    private String imageUri;

    public PhotoButton(Context context, String imageUri) {
        super(context);
        super.setBackgroundColor(Color.TRANSPARENT);
        super.setScaleType(ScaleType.FIT_XY);

        this.imageUri = imageUri;

        imageLoaded = false;
        imageLoading = false;

        setSize(DEFAULT_W_PIX, DEFAULT_H_PIX);
    }

    public void setSize(int w_pix, int h_pix) {
        this.w_pix = w_pix;
        this.h_pix = h_pix;

        if (!imageLoaded) {
            setImage(getDefault(w_pix, h_pix));
        }

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
            setImageBitmap(getDefault(w_pix, h_pix));
            imageLoaded = false;
        }
    }

}
