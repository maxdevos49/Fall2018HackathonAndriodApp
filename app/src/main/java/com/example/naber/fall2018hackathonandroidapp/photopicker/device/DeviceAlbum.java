package com.example.naber.fall2018hackathonandroidapp.photopicker.device;

import java.util.ArrayList;
import java.util.List;

public class DeviceAlbum {

    private String albumName;
    private String albumId;

    private DevicePhoto coverPhoto;
    private List<DevicePhoto> photos;

    public DeviceAlbum(String albumName, String albumId) {
        this.albumName = albumName;
        this.albumId = albumId;
        this.coverPhoto = null;

        photos = new ArrayList<>();
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setCoverPhoto(DevicePhoto coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public DevicePhoto getCoverPhoto() {
        return coverPhoto;
    }

    public void addPhoto(DevicePhoto photo) {

        if (coverPhoto == null) {
            coverPhoto = photo;
        }

        this.photos.add(photo);
    }

}
