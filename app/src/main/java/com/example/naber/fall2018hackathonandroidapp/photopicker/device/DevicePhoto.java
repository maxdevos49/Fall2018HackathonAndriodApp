package com.example.naber.fall2018hackathonandroidapp.photopicker.device;

public class DevicePhoto {

    private String photoId;
    private String photoUri;

    public DevicePhoto(String photoId, String photoUri) {
        this.photoId = photoId;
        this.photoUri = photoUri;
    }

    public String getPhotoId() {
        return photoId;
    }

    public String getPhotoUri() {
        return photoUri;
    }
}
