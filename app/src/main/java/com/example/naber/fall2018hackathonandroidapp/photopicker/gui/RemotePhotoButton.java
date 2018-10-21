package com.example.naber.fall2018hackathonandroidapp.photopicker.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;

import com.Util;
import com.http.HTTPRequest;
import com.http.ImageLoadResponseListener;

public class RemotePhotoButton extends PhotoButton {
    public RemotePhotoButton(Context context, String imageUri) {
        super(context, imageUri);
    }

    @Override
    public void loadImage() {

        if (isImageLoaded() || isImageLoading()) return;

        setImageLoading(true);
        HTTPRequest.loadImage(getImageUri(), new ImageLoadResponseListener() {
            @Override
            public void imageLoaded(Bitmap image) {
                Util.runonUiThread(() -> {
                    RemotePhotoButton.super.setImage(ThumbnailUtils.extractThumbnail(image, getW_pix(), getH_pix()));
                });
            }

            @Override
            public void failure(String message) {
                // Do nothing
            }
        });
    }
}
