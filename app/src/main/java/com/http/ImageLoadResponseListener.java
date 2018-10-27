package com.http;

import android.graphics.Bitmap;

public interface ImageLoadResponseListener {

    void imageLoaded(Bitmap image);
    void failure(String message);

}
