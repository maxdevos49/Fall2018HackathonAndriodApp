package com.example.naber.fall2018hackathonandroidapp.photopicker.RemoteView;

import com.example.naber.fall2018hackathonandroidapp.photopicker.RemoteView.RemotePhoto;
import com.example.naber.fall2018hackathonandroidapp.photopicker.device.PhotoChangeListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class RemoteAlbum {

    private String name;
    private List<RemotePhoto> photos;
    private RemotePhoto coverPhoto;

    private Set<RemotePhotoChangeListener> listeners;

    public RemoteAlbum(String name) {
        this.name = name;
        photos = new ArrayList<>();

        listeners = new HashSet<>();
    }

    public void add(RemotePhoto photo) {

        if (coverPhoto == null) {
            coverPhoto = photo;
        }

        for (RemotePhotoChangeListener listener : listeners) listener.photoLoaded(photo);

        photos.add(photo);
    }

    public void addListener(RemotePhotoChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RemotePhotoChangeListener listener) {
        listeners.remove(listener);
    }

    public void setCoverPhoto(RemotePhoto coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public RemotePhoto getCoverPhoto() {
        return coverPhoto;
    }

    public List<RemotePhoto> getPhotos() {
        return photos;
    }

    public void clear() {
        for (RemotePhotoChangeListener listener : listeners) listener.photosCleared();
        photos.clear();
    }

    public String getName() {
        return name;
    }
}