package com.example.naber.fall2018hackathonandroidapp.photopicker.gui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;

import com.Util;
import com.http.HTTPRequest;

import java.util.LinkedList;
import java.util.Queue;

public class PhotoButtonImageLoader {

    Queue<PhotoButton> buttons;

    public PhotoButtonImageLoader() {
        buttons = new LinkedList<>();
    }

    public void addButton(PhotoButton button) {
        buttons.add(button);
    }

    public void performLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (buttons.peek() != null) {
                    final PhotoButton button = buttons.remove();

                    final Bitmap image = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(button.getImageUri()), button.getW_pix(), button.getH_pix(), MediaStore.Images.Thumbnails.MICRO_KIND);

                    Util.runonUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button.setImage(image);
                        }
                    });
                }
            }
        }).start();
    }

}
