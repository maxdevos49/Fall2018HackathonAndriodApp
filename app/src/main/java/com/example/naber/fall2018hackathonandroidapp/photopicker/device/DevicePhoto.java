package com.example.naber.fall2018hackathonandroidapp.photopicker.device;

import java.io.Serializable;

public class DevicePhoto implements Serializable {

    private static final long serialVersionUID = 1L;

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
