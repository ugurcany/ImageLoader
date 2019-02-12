package com.mylivn.imageloader;

import android.graphics.Bitmap;

interface InternalCallback {

    void onProgress(int progress);

    void onStart();

    void onImageReady(Bitmap bitmap);

    void onError(Exception e);

    void onCancelled();

}
