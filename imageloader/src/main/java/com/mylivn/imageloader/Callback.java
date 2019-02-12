package com.mylivn.imageloader;

import android.graphics.Bitmap;

public interface Callback {

    default void onProgress(int progress) {
    }

    default void onImageReady(Bitmap bitmap) {
    }

    default void onError(Exception e) {
    }

    default void onCancelled() {
    }

}
