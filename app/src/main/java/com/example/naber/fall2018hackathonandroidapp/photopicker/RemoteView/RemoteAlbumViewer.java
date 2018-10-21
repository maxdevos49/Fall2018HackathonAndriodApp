package com.example.naber.fall2018hackathonandroidapp.photopicker.RemoteView;

import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.Util;
import com.example.naber.fall2018hackathonandroidapp.R;
import com.example.naber.fall2018hackathonandroidapp.photopicker.gui.PhotoButton;
import com.example.naber.fall2018hackathonandroidapp.photopicker.gui.PhotoButtonImageLoader;
import com.example.naber.fall2018hackathonandroidapp.photopicker.gui.RemotePhotoButton;
import com.example.naber.fall2018hackathonandroidapp.photopicker.gui.TitledPhotoButton;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class RemoteAlbumViewer extends AppCompatActivity implements RemotePhotoChangeListener {

    private static String LOG_ID = RemoteAlbumViewer.class.getSimpleName();

    private RemoteAlbum album;

    List<RemotePhotoButton> buttons;

    private long timeOfLastScroll = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_album_viewer);
        getWindow().setBackgroundDrawableResource(R.drawable.background);

        buttons = new ArrayList<>();

        findViewById(R.id.RemoteAlbumViewerScroll).getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                long now = System.currentTimeMillis();
                if (now - timeOfLastScroll > 200) {
                    limitLoadedThumbnails();
                    timeOfLastScroll = now;
                } else {
                    timeOfLastScroll = now;
                }
            }
        });

        album = RemoteAlbumPhotoList.getInstance().getAlbum(getIntent().getStringExtra("albumName"));
        album.addListener(this);
        addExistingPhotos();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                limitLoadedThumbnails();
            }
        }).start();

    }

    private void limitLoadedThumbnails() {

        Rect scrollBounds = new Rect();
        findViewById(R.id.RemoteAlbumViewerLL).getHitRect(scrollBounds);
        Log.i(LOG_ID, scrollBounds.toString());

        for (int i = 0; i < buttons.size(); i++) {

            if (buttons.get(i).getLocalVisibleRect(new Rect(scrollBounds))) {

                if (buttons.get(i).isImageLoaded() || buttons.get(i).isImageLoading()) continue;

                Log.i(LOG_ID, "Loading image from button and neighbors: " + buttons.get(i));
                buttons.get(i).loadImage();
                if (i - 2 > 0) {
                    buttons.get(i - 1).loadImage();
                    buttons.get(i - 2).loadImage();
                }

                if (i + 2 < buttons.size()) {
                    buttons.get(i + 1).loadImage();
                    buttons.get(i + 2).loadImage();
                }

            } else {
                if (buttons.get(i).isImageLoaded()) {
                    Log.i(LOG_ID, "Unloading image from button : " + buttons.get(i));
                    final RemotePhotoButton bt = buttons.get(i);
                    Util.runonUiThread(() -> bt.unloadImage());
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        album.addListener(this);
        addExistingPhotos();
    }

    @Override
    protected void onPause() {
        super.onPause();
        album.removeListener(this);
    }

    private void addExistingPhotos() {
        ((LinearLayout) findViewById(R.id.RemoteAlbumViewerLL)).removeAllViews();
        buttons.clear();
        for (RemotePhoto photo : album.getPhotos()) {
            addPhoto(photo);
        }
        limitLoadedThumbnails();
    }

    private void addPhoto(RemotePhoto photo) {
        LinearLayout albumViewerLL = findViewById(R.id.RemoteAlbumViewerLL);
        LinearLayout lastRow = (LinearLayout) albumViewerLL.getChildAt(albumViewerLL.getChildCount() - 1);
        if (lastRow == null || lastRow.getChildCount() >= 1) {
            lastRow = new LinearLayout(this);
            lastRow.setOrientation(LinearLayout.HORIZONTAL);
            lastRow.setGravity(Gravity.CENTER);
            albumViewerLL.addView(lastRow);
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels - 100;

        RemotePhotoButton button = new RemotePhotoButton(this, photo.getFilePath());
        button.setSize(width,width);
        button.setPadding(10,10,10,10);
        lastRow.addView(button);

        buttons.add(button);

    }

    @Override
    public void photoLoaded(RemotePhoto photo) {
        addPhoto(photo);
        limitLoadedThumbnails();
    }

    @Override
    public void photosCleared() {
        ((LinearLayout) findViewById(R.id.RemoteAlbumViewerLL)).removeAllViews();
        buttons.clear();
    }
}
