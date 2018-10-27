package com.example.naber.fall2018hackathonandroidapp.photopicker;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.example.naber.fall2018hackathonandroidapp.R;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.DeviceAlbum;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.DevicePhoto;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.DevicePhotoList;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.PhotoChangeListener;
import com.example.naber.fall2018hackathonandroidapp.photopicker.gui.PhotoButton;
import com.example.naber.fall2018hackathonandroidapp.photopicker.gui.PhotoButtonImageLoader;
import com.example.naber.fall2018hackathonandroidapp.photopicker.gui.TogglablePhotoButton;
import com.http.HTTPRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhotoPicker extends AppCompatActivity implements PhotoChangeListener, ViewTreeObserver.OnScrollChangedListener {

    private static final String LOG_ID = PhotoPicker.class.getSimpleName();

    private static final int NUM_BUTTONS_PER_ROW = 3;

    private DeviceAlbum album;
    private List<TogglablePhotoButton> buttons;
    private Map<TogglablePhotoButton, DevicePhoto> buttonToPhoto;

    private boolean canUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        setContentView(R.layout.activity_photo_picker);

        Log.i(LOG_ID, "Creating photo picker from album with id: " + getIntent().getIntExtra("albumId", Integer.MAX_VALUE));

        album = DevicePhotoList.getInstance().getAlbum(getIntent().getIntExtra("albumId", Integer.MAX_VALUE));
        buttons = new ArrayList<>();
        buttonToPhoto = new HashMap<>();

        if (album == null) {
            throw new IllegalStateException("Invalid album number");
        }

        findViewById(R.id.PhotoPickerScrollView).getViewTreeObserver().addOnScrollChangedListener(this);

        album.addListener(this);

        displayPhotos();

        // Dirty hack that Timothy Steward endorsed
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                limitLoadedThumbnails();

            }
        }).start();

        checkIfCanUpload();
        findViewById(R.id.SubmitFilesButton).setOnClickListener(new UploadButtonOnClickListener());

    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        limitLoadedThumbnails();
    }

    @Override
    protected void onPause() {
        super.onPause();
        album.removeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        album.addListener(this);
    }

    private void displayPhotos() {
        buttons.clear();
        buttonToPhoto.clear();
        for (DevicePhoto photo : album.getPhotos()) {
            addPhoto(photo);
        }
    }

    private void addPhoto(DevicePhoto photo) {

        LinearLayout photoPickerLL = findViewById(R.id.PhotoPickerLL);
        LinearLayout lastRow = (LinearLayout) photoPickerLL.getChildAt(photoPickerLL.getChildCount() - 1);
        if (lastRow == null || lastRow.getChildCount() >= NUM_BUTTONS_PER_ROW) {
            lastRow = new LinearLayout(this);
            lastRow.setOrientation(LinearLayout.HORIZONTAL);
            lastRow.setGravity(Gravity.CENTER);
            photoPickerLL.addView(lastRow);
        }

        TogglablePhotoButton button = new TogglablePhotoButton(this, photo.getThumbnailUri());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - 100;
        button.setSize(width / NUM_BUTTONS_PER_ROW,width / NUM_BUTTONS_PER_ROW);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Fix this ugly piece of garbage
                if (v instanceof TogglablePhotoButton) {
                    ((TogglablePhotoButton) v).toggle();
                }
                checkIfCanUpload();
            }
        });
        buttons.add(button);
        buttonToPhoto.put(button, photo);
        button.setPadding(10,10,10,10);
        lastRow.addView(button);

    }

    private void limitLoadedThumbnails() {

        PhotoButtonImageLoader buttonImageLoader = new PhotoButtonImageLoader();

        Rect scrollBounds = new Rect();
        findViewById(R.id.PhotoPickerScrollView).getHitRect(scrollBounds);

        for (PhotoButton button : buttons) {

            if (button.getLocalVisibleRect(new Rect(scrollBounds))) {

                if (button.isImageLoaded() || button.isImageLoading()) continue;

                Log.i(LOG_ID, "Loading image from button: " + button);
                button.setImageLoading(true);
                buttonImageLoader.addButton(button);
            } else {
                Log.i(LOG_ID, "Unloading image from button : " + button);
                button.unloadImage();
            }
        }
        buttonImageLoader.performLoad();
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

    public void disableFloatingButton() {

        FloatingActionButton button = findViewById(R.id.SubmitFilesButton);
        button.setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
        button.invalidate();
        canUpload = false;

    }

    public void enableFloatingButton() {

        FloatingActionButton button = findViewById(R.id.SubmitFilesButton);
        button.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#303f9f")));
        button.invalidate();
        canUpload = true;

    }

    public void checkIfCanUpload() {
        boolean oneClicked = false;
        for (TogglablePhotoButton toggleButton : buttons) {
            if (toggleButton.isClicked()) {
                oneClicked = true;
                break;
            }
        }

        if (!oneClicked) {
            disableFloatingButton();
        } else {
            enableFloatingButton();
        }
    }

    public void uploadSelected() {

        List<String> photosToUpload = new ArrayList<>();
        for (TogglablePhotoButton button : buttons) {
            if (button.isClicked()) {
                photosToUpload.add(buttonToPhoto.get(button).getPhotoUri());
                button.toggle();
            }
        }

        HTTPRequest.upload(photosToUpload);

    }

    private class UploadButtonOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (canUpload) {
                uploadSelected();
            }
        }

    }

}
