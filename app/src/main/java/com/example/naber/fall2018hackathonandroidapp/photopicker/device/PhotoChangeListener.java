package com.example.naber.fall2018hackathonandroidapp.photopicker.device;

public interface PhotoChangeListener {
    void photoLoaded(DevicePhoto photo);
    void photosCleared();
}
