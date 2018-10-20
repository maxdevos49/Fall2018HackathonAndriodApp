package com.example.naber.fall2018hackathonandroidapp.photopicker.device;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeviceAlbum {

    private String albumName;
    private int albumId;

    private DevicePhoto coverPhoto;
    private List<DevicePhoto> photos;

    Set<PhotoLoadedListener> listeners;

    public DeviceAlbum(String albumName, int albumId) {
        this.albumName = albumName;
        this.albumId = albumId;
        this.coverPhoto = null;

        photos = new ArrayList<>();
        listeners = new HashSet<>();
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

    public void addPhoto(final DevicePhoto photo) {

        if (coverPhoto == null) {
            coverPhoto = photo;
        }

        for (PhotoLoadedListener listener : listeners) {
            listener.photoLoaded(photo);
        }

        this.photos.add(photo);
    }

}
