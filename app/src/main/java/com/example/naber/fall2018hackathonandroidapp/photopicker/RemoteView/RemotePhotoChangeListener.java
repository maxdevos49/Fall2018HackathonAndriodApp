package com.example.naber.fall2018hackathonandroidapp.photopicker.RemoteView;

public interface RemotePhotoChangeListener {
    void photoLoaded(RemotePhoto photo);
    void photosCleared();
}
