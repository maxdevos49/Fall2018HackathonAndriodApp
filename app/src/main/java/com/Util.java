package com;

import android.os.Handler;
import android.os.Looper;

public class Util {
    public static void runonUiThread(Runnable runnable) {
        final Handler UIHandler = new Handler(Looper.getMainLooper());
        UIHandler.post(runnable);
    }

    public int indexOf(String[] arr, String val) {
        for (int i = 0; i < arr.length; i++) {
            if (val == null) {
                if (arr[i] == null) {
                    return i;
                }
            } else if (val.equals(arr[i])) {
                return i;
            }
        }

        return -1;

    }
}
