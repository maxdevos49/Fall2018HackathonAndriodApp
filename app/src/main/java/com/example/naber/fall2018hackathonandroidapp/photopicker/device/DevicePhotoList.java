package com.example.naber.fall2018hackathonandroidapp.photopicker.device;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DevicePhotoList {

    private static final String LOG_ID = DevicePhotoList.class.getSimpleName();

    private static final String[] projection = new String[] {
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA
    };

    private Context photoListContext;

    private List<DeviceAlbum> albums;

    public DevicePhotoList(Context context) {
        photoListContext = context;
        albums = new ArrayList<>();
    }

    public void refreshList(final AlbumLoadedListener listener) {
        albums.clear();
    }

    public void loadList(final AlbumLoadedListener listener) {
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

            Log.i(LOG_ID, "Parsing through image query response");
            cur.moveToFirst();
            do {

                String bucketName = cur.getString(bucketNameIndex);
                //String bucketId = cur.getString(bucketIdColumn);
                String bucketId = "Test";
                String imageUri = cur.getString(imageUriIndex);
                String imageId = cur.getString(imageIdColumn);

                DeviceAlbum album = getAlbum(bucketName, bucketId);

                album.addPhoto(new DevicePhoto(imageId, imageUri));

                listener.albumLoaded(album);

            } while (cur.moveToNext());

            Log.i(LOG_ID, "Image parsing finnished");

        }

    }

    private DeviceAlbum getAlbum(String name, String id) {
        for (DeviceAlbum album : albums) {
            if (album.getAlbumName().equals(name)) {
                return album;
            }
        }
        return new DeviceAlbum(name, id);
    }

    public List<DeviceAlbum> getAlbums() {
        return albums;
    }

}
