package com.example.naber.fall2018hackathonandroidapp.photopicker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;

import com.example.naber.fall2018hackathonandroidapp.R;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.AlbumLoadedListener;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.DeviceAlbum;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.DevicePhotoList;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.PhotoPicker;

public class AlbumPicker extends AppCompatActivity implements AlbumLoadedListener {

    private static final String LOG_ID = AlbumPicker.class.getSimpleName();

    private DevicePhotoList photoList;
    private static final String[] PERMISSIONS_NEEDED = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private static int PERMISSION_REQUEST_ID = 35326425;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_picker);

        photoList = new DevicePhotoList(this);

        insureAdequatePermission();
    }

    private void loadAlbums() {
        photoList.loadList(this);
    }

    @Override
    public void albumLoaded(final DeviceAlbum album) {
        Utils.runonUiThread(new Runnable() {
            @Override
            public void run() {
                addAlbumButton(album);
            }
        });
    }

    private void addAlbumButton(DeviceAlbum album) {

        ScrollView albumButtonSView = (ScrollView) findViewById(R.id.AlbumButtonView);
        TableLayout albumButtonTL = (TableLayout) albumButtonSView.getChildAt(0);

        Button newAlbumButton = new Button(this);
        newAlbumButton.setOnClickListener(new AlbumButtonClickListener(album.getAlbumName(), album.getAlbumId()));

        newAlbumButton.setText(album.getAlbumName());
        albumButtonTL.addView(newAlbumButton);

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
        intent.putExtra("Album", photoList.getAlbum(id));
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
