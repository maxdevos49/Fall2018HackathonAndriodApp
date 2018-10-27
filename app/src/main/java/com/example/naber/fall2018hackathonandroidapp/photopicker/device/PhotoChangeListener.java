package com.example.naber.fall2018hackathonandroidapp.photopicker.device;

import com.example.naber.fall2018hackathonandroidapp.photopicker.device.DevicePhoto;

public interface PhotoChangeListener {
    void photoLoaded(DevicePhoto photo);
    void photosCleared();
}
