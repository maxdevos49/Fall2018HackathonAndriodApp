package com.example.naber.fall2018hackathonandroidapp.photopicker.device;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeviceAlbum implements Serializable {

    private static final long serialVersionUID = 1L;

    private String albumName;
    private int albumId;

    private DevicePhoto coverPhoto;
    private List<DevicePhoto> photos;

    Set<PhotoChangeListener> listeners;

    public DeviceAlbum(String albumName, int albumId) {
        this.albumName = albumName;
        this.albumId = albumId;
        this.coverPhoto = null;

        photos = new ArrayList<>();
        listeners = new HashSet<>();
    }

    public void addListener(PhotoChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PhotoChangeListener listener) {
        listeners.remove(listener);
    }

    public String getAlbumName() {
        return albumName;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setCoverPhoto(DevicePhoto coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public DevicePhoto getCoverPhoto() {
        return coverPhoto;
    }

    public void clear() {
        photos.clear();
        coverPhoto = null;

        for (PhotoChangeListener listener : listeners) {
            listener.photosCleared();
        }

    }

    public void addPhoto(final DevicePhoto photo) {

        if (coverPhoto == null) {
            coverPhoto = photo;
        }

        for (PhotoChangeListener listener : listeners) {
            listener.photoLoaded(photo);
        }

        this.photos.add(photo);
    }

    public List<DevicePhoto> getPhotos() {
        return photos;
    }

}
