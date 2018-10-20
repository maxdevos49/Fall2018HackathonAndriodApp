package com.example.naber.fall2018hackathonandroidapp.photopicker.device;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DevicePhotoList {

    private DevicePhotoList() {
        albums = new ArrayList<>();
    }

    private static final DevicePhotoList instance = new DevicePhotoList();
    public static DevicePhotoList getInstance() {
        return instance;
    }

    private static final String LOG_ID = DevicePhotoList.class.getSimpleName();

    private static final String[] projection = new String[] {
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Thumbnails.DATA
    };

    private Context photoListContext;

    private List<DeviceAlbum> albums;

    public void setContext(Context context) {
        this.photoListContext = context;
    }

    public void refreshList(final AlbumLoadedListener listener) {
        clearAlbums();
    }

    public void clearAlbums() {
        for (DeviceAlbum album : albums) {
            album.clear();
        }
    }

    public void loadList(final AlbumLoadedListener listener) {
        if (photoListContext == null) {
            throw new IllegalStateException("Must provide a context for the photolist");
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadImages(photoListContext, listener);
            }
        }).start();
    }

    private void loadImages(Context context, AlbumLoadedListener listener) {

        Log.i("DevicePhotoList", "Loading images");
        Uri images = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        Cursor cur = context.getContentResolver().query(images, projection, null, null, null);

        if (cur != null && cur.getCount() > 0) {

            int bucketNameIndex = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int bucketIdColumn = cur.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
            int imageUriIndex = cur.getColumnIndex(MediaStore.Images.Media.DATA);
            int imageIdColumn = cur.getColumnIndex(MediaStore.Images.Media._ID);
            int thumbnailUriIndex = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);

            Log.i(LOG_ID, "Parsing through image query response");
            cur.moveToFirst();
            do {

                String bucketName = cur.getString(bucketNameIndex);
                int bucketId = cur.getInt(bucketIdColumn);
                String imageUri = cur.getString(imageUriIndex);
                String imageId = cur.getString(imageIdColumn);
                String thumbnailUri = cur.getString(thumbnailUriIndex);

                DeviceAlbum album = getAlbum(bucketId);

                if (album == null) {
                    Log.i(LOG_ID, "Creating new Device Album with name: " + bucketName + " id: " + bucketId);
                    album = new DeviceAlbum(bucketName, bucketId);
                    albums.add(album);

                    listener.albumLoaded(album);
                }

                album.addPhoto(new DevicePhoto(imageId, imageUri, thumbnailUri));

            } while (cur.moveToNext());

            Log.i(LOG_ID, "Image parsing finnished");

        }

    }

    public DeviceAlbum getAlbum(int id) {

        for (DeviceAlbum album : albums) {
            if (id == album.getAlbumId()) {
                return album;
            }
        }
        return null;
    }

    public List<DeviceAlbum> getAlbums() {
        return albums;
    }

}
