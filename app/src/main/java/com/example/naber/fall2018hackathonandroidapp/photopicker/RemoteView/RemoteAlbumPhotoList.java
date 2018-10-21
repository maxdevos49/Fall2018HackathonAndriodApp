package com.example.naber.fall2018hackathonandroidapp.photopicker.RemoteView;

import android.util.Log;

import com.http.PhotoDataResponseListener;
import com.http.HTTPRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RemoteAlbumPhotoList {

    private static final RemoteAlbumPhotoList instance = new RemoteAlbumPhotoList();

    public static RemoteAlbumPhotoList getInstance() {
        return instance;
    }

    private RemoteAlbumPhotoList() {
        albums = new ArrayList<>();
    }

    private static final String LOG_ID = RemoteAlbumPhotoList.class.getSimpleName();

    private List<RemoteAlbum> albums;
    private int retries;


    public void loadPhotos(RemoteAlbumListener listener) {
        albums.clear();
        retries = 0;
        makeServerCall(listener);
    }

    private void parseAlbumData(JSONArray arr, RemoteAlbumListener listener) {
        for (int i = 0; i < arr.length(); i++) {
            try {
                addPhoto(arr.getJSONObject(i), listener);
            } catch (JSONException e) {
                Log.e(LOG_ID, "Error in photoOBj data from server");
            }
        }
    }

    public void makeServerCall(RemoteAlbumListener listener) {
        HTTPRequest.getFiles(new PhotoDataResponseListener() {
            @Override
            public void response(JSONArray arr) {
                parseAlbumData(arr, listener);
            }

            @Override
            public void failure(String message) {
                retries++;
                if (retries > 3) {
                    Log.e(LOG_ID, "Failure to retrieve photo data " + message);
                } else {
                    makeServerCall(listener);
                }
            }
        });
    }

    private void addPhoto(JSONObject photoObj, RemoteAlbumListener listener) {
        try {
            String albumName = photoObj.getString("album");
            String origFileName = photoObj.getString("origFileName");
            String filePath = photoObj.getString("fileName");

            JSONArray tagsJArr = photoObj.getJSONArray("tag");
            List<String> tags = new ArrayList<>();
            for (int i = 0; i <tagsJArr.length(); i++) {
                tags.add(tagsJArr.getString(i));
            }

            RemoteAlbum album = getAlbum(albumName);
            RemotePhoto photo = new RemotePhoto(albumName,tags,filePath,origFileName);
            if (album == null) {
                album = new RemoteAlbum(albumName);
                album.setCoverPhoto(photo);

                albums.add(album);
                if (listener != null) listener.albumLoaded(album);

            }
            album.add(photo);

        } catch (JSONException e) {
            Log.e(LOG_ID, "Error in photoObj data from server");
        }
    }

    public RemoteAlbum getAlbum(String name) {
        for (RemoteAlbum album : albums) {
            if (name == null) {
                if (album == null) {
                    return album;
                }
            } else {
                if (name.equals(album.getName())) {
                    return album;
                }
            }
        }
        return null;
    }

}
