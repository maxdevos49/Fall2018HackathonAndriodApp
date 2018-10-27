package com.example.naber.fall2018hackathonandroidapp.photopicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.Util;
import com.example.naber.fall2018hackathonandroidapp.R;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.AlbumLoadedListener;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.DeviceAlbum;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.DevicePhotoList;
import com.example.naber.fall2018hackathonandroidapp.photopicker.gui.TitledPhotoButton;
import com.http.HTTPRequest;

public class AlbumPicker extends AppCompatActivity implements AlbumLoadedListener {

    private static final String LOG_ID = AlbumPicker.class.getSimpleName();

    private static final int NUM_BUTTONS_PER_ROW = 3;

    private DevicePhotoList photoList;
    private static final String[] PERMISSIONS_NEEDED = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static int PERMISSION_REQUEST_ID = 35326425;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        setContentView(R.layout.activity_album_picker);

        photoList = DevicePhotoList.getInstance();
        photoList.setContext(this);

        insureAdequatePermission();
    }

    private void loadAlbums() {
        if (!photoList.isLoaded()) {
            photoList.loadList(this);
        }
    }

    @Override
    public void albumLoaded(final DeviceAlbum album) {
        Util.runonUiThread(new Runnable() {
            @Override
            public void run() {
                addAlbumButton(album);
            }
        });
    }

    @Override
    protected void onResume() {
        Log.i(LOG_ID, "Resuming");
        super.onResume();
        ((LinearLayout) findViewById(R.id.AlbumPickerLL)).removeAllViews();
        for (DeviceAlbum album : photoList.getAlbums()) {
            addAlbumButton(album);
        }
    }

    private void addAlbumButton(DeviceAlbum album) {

        LinearLayout albumPickerLL = findViewById(R.id.AlbumPickerLL);
        LinearLayout lastRow = (LinearLayout) albumPickerLL.getChildAt(albumPickerLL.getChildCount() - 1);
        if (lastRow == null || lastRow.getChildCount() >= NUM_BUTTONS_PER_ROW) {
            lastRow = new LinearLayout(this);
            lastRow.setOrientation(LinearLayout.HORIZONTAL);
            lastRow.setGravity(Gravity.CENTER);
            albumPickerLL.addView(lastRow);
        }

        TitledPhotoButton button = new TitledPhotoButton(this, album.getCoverPhoto().getThumbnailUri());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - 100;
        button.setTitle(album.getAlbumName());
        button.setSize(width / 3,width / 3);
        button.setPadding(10,10,10,10);
        Log.i(LOG_ID, width + "");
        button.loadImage();
        button.setOnClickListener(new AlbumButtonClickListener(album.getAlbumName(), album.getAlbumId()));
        lastRow.addView(button);

    }

    private void insureAdequatePermission() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(PERMISSIONS_NEEDED, PERMISSION_REQUEST_ID);
        } else {
            loadAlbums();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_ID) {
            for (int i : grantResults) {
                if (i == PackageManager.PERMISSION_GRANTED) {
                    break;
                }
            }
            // All permissions granted
            loadAlbums();
        }
        // TODO: Handle permissions not granted
    }

    private void openAlbum(int id) {
        Intent intent = new Intent(this, PhotoPicker.class);
        intent.putExtra("albumId", id);
        startActivity(intent);
    }

    private class AlbumButtonClickListener implements View.OnClickListener {

        private String albumName;
        private int albumId;

        public AlbumButtonClickListener(String albumName, int albumId) {
            this.albumName = albumName;
            this.albumId = albumId;
        }

        @Override
        public void onClick(View v) {
            Log.i(LOG_ID, "User has clicked on album name: " + albumName + " id: " + albumId);
            openAlbum(albumId);
        }
    }

}
