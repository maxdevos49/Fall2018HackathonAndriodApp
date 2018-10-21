package com.example.naber.fall2018hackathonandroidapp.photopicker.RemoteView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.Util;
import com.example.naber.fall2018hackathonandroidapp.R;
import com.example.naber.fall2018hackathonandroidapp.photopicker.gui.TitledPhotoButton;
import com.http.HTTPRequest;
import com.http.ImageLoadResponseListener;

public class RemoteAlbumPicker extends AppCompatActivity implements RemoteAlbumListener, SwipeRefreshLayout.OnRefreshListener {

    private int NUM_BUTTONS_PER_ROW = 3;

    private static final String LOG_ID = RemoteAlbumPicker.class.getSimpleName();

    private RemoteAlbumPhotoList photoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(R.drawable.background);
        setContentView(R.layout.activity_remote_album_picker);

        photoList = RemoteAlbumPhotoList.getInstance();
        photoList.loadPhotos(this);

        ((SwipeRefreshLayout) findViewById(R.id.pullToRefreshRemoteAlbums)).setOnRefreshListener(this);

    }

    private void addAlbumButton(Bitmap image, RemoteAlbum album) {

        Log.i(LOG_ID, "Adding album button for album: " + album);

        LinearLayout albumPickerLL = findViewById(R.id.RemoteAlbumPickerLL);
        LinearLayout lastRow = (LinearLayout) albumPickerLL.getChildAt(albumPickerLL.getChildCount() - 1);
        if (lastRow == null || lastRow.getChildCount() >= NUM_BUTTONS_PER_ROW) {
            lastRow = new LinearLayout(this);
            lastRow.setOrientation(LinearLayout.HORIZONTAL);
            lastRow.setGravity(Gravity.CENTER);
            albumPickerLL.addView(lastRow);
        }

        TitledPhotoButton button = new TitledPhotoButton(this, null);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - 100;

        button.setTitle(album.getName());
        button.setImage(ThumbnailUtils.extractThumbnail(image, width / 3, width / 3));
        button.setSize(width / 3,width / 3);
        button.setPadding(10,10,10,10);
        button.setOnClickListener(new AlbumButtonOnClick(album.getName()));
        lastRow.addView(button);

    }

    @Override
    public void albumLoaded(RemoteAlbum album) {

        Log.i(LOG_ID, "Loading image for: " + album.getCoverPhoto().getFilePath());
        HTTPRequest.loadImage(album.getCoverPhoto().getFilePath(), new ImageLoadResponseListener() {

            @Override
            public void imageLoaded(Bitmap image) {
                Util.runonUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addAlbumButton(image, album);
                    }
                });
            }

            @Override
            public void failure(String message) {

            }
        });

    }

    @Override
    public void onRefresh() {
        ((LinearLayout) findViewById(R.id.RemoteAlbumPickerLL)).removeAllViews();
        RemoteAlbumPhotoList.getInstance().loadPhotos(this);

        new Thread(() -> {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Util.runonUiThread(() -> {
                ((SwipeRefreshLayout) findViewById(R.id.pullToRefreshRemoteAlbums)).setRefreshing(false);
            });
        }).start();
    }

    private void openAlbum(String s) {
        Intent intent = new Intent(this, RemoteAlbumViewer.class);
        intent.putExtra("albumName", s);
        startActivity(intent);
    }

    private class AlbumButtonOnClick implements View.OnClickListener {

        private String albumName;
        public AlbumButtonOnClick(String albumName) {
            this.albumName = albumName;
        }

        @Override
        public void onClick(View v) {
            openAlbum(albumName);
        }

    }

}
