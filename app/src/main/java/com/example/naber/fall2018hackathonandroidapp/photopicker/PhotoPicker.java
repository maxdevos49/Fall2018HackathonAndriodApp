package com.example.naber.fall2018hackathonandroidapp.photopicker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;

import com.example.naber.fall2018hackathonandroidapp.R;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.DeviceAlbum;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.DevicePhoto;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.DevicePhotoList;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.PhotoChangeListener;
import com.example.naber.fall2018hackathonandroidapp.photopicker.gui.PhotoButton;

import java.util.ArrayList;
import java.util.List;

public class PhotoPicker extends AppCompatActivity implements PhotoChangeListener, ViewTreeObserver.OnScrollChangedListener {

    private static final String LOG_ID = PhotoPicker.class.getSimpleName();

    private DeviceAlbum album;
    List<PhotoButton> buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_picker);

        Log.i(LOG_ID, "Creating photo picker from album with id: " + getIntent().getIntExtra("albumId", Integer.MAX_VALUE));

        album = DevicePhotoList.getInstance().getAlbum(getIntent().getIntExtra("albumId", Integer.MAX_VALUE));
        buttons = new ArrayList<>();
        if (album == null) {
            throw new IllegalStateException("Invalid album number");
        }

        album.addListener(this);
        displayPhotos();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        album.removeListener(this);
    }

    private void displayPhotos() {
        for (DevicePhoto photo : album.getPhotos()) {
            addPhoto(photo);
        }
        limitLoadedThumbnails();
    }

    private void addPhoto(DevicePhoto photo) {

        TableLayout albumButtonTL = findViewById(R.id.PhotoPickerTableLayout);

        PhotoButton newPhotoButton = new PhotoButton(this, photo.getThumbnailUri());
        buttons.add(newPhotoButton);
        albumButtonTL.addView(newPhotoButton);
    }

    private void limitLoadedThumbnails() {
        for (PhotoButton button : buttons) {
            if (button.getVisibility() == View.VISIBLE) {
                button.loadImage();
            } else {
                button.unloadImage();
            }
        }
    }

    @Override
    public void photoLoaded(DevicePhoto photo) {
        addPhoto(photo);
    }

    @Override
    public void photosCleared() {

    }

    @Override
    public void onScrollChanged() {
        limitLoadedThumbnails();
    }
}