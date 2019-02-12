package com.mylivn.imageloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

class ImageLoaderImpl implements ImageLoader, InternalCallback {

    private static WeakReference<Context> weakContext;
    private static ImageLoader imageLoader;

    private ImageDownloader imageDownloadTask;
    private Callback callback = null;
    private WeakReference<ImageView> weakImageView;
    private boolean isCircular = false;
    private Bitmap bitmapLoadingPlaceholder = null;
    private Bitmap bitmapErrorPlaceholder = null;

    static ImageLoader get(Context context) {
        weakContext = new WeakReference<>(context);
        if (imageLoader == null) {
            imageLoader = new ImageLoaderImpl();
        }
        return imageLoader;
    }

    @Override
    public ImageLoader isCircular(boolean isCircular) {
        this.isCircular = isCircular;
        return imageLoader;
    }

    @Override
    public ImageLoader into(ImageView imageView) {
        weakImageView = new WeakReference<>(imageView);
        return imageLoader;
    }

    @Override
    public ImageLoader loadingPlaceholder(int loadingPlaceholderRes) {
        this.bitmapLoadingPlaceholder =
                BitmapFactory.decodeResource(weakContext.get().getResources(), loadingPlaceholderRes);
        return imageLoader;
    }

    @Override
    public ImageLoader errorPlaceholder(int errorPlaceholderRes) {
        this.bitmapErrorPlaceholder =
                BitmapFactory.decodeResource(weakContext.get().getResources(), errorPlaceholderRes);
        return imageLoader;
    }

    @Override
    public ImageLoader callback(Callback callback) {
        this.callback = callback;
        return imageLoader;
    }

    @Override
    public void load(String imageUrl) {
        downloadImageAsBitmap(imageUrl);
    }

    @Override
    public void onProgress(int progress) {
        if (callback != null) {
            callback.onProgress(progress);
        }
    }

    @Override
    public void onStart() {
        loadBitmap(bitmapLoadingPlaceholder);
    }

    @Override
    public void onImageReady(Bitmap bitmap) {
        if (callback != null) {
            callback.onImageReady(bitmap);
        }
        loadBitmap(bitmap);
    }

    @Override
    public void onError(Exception e) {
        if (callback != null) {
            callback.onError(e);
        }
        loadBitmap(bitmapErrorPlaceholder);
    }

    @Override
    public void onCancelled() {
        if (callback != null) {
            callback.onCancelled();
        }
    }

    private void loadBitmap(Bitmap bitmap) {
        ImageView imageView;
        if ((imageView = weakImageView.get()) != null
                && weakContext.get() != null) {
            if (isCircular && bitmap != null) {
                imageView.setImageDrawable(cropToCircle(cropToSquare(bitmap)));
            } else {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    private void downloadImageAsBitmap(String imageUrl) {
        if (imageDownloadTask != null) {
            imageDownloadTask.cancel(true);
            imageDownloadTask = null;
        }
        imageDownloadTask = new ImageDownloader(this).execute(imageUrl);
    }

    private Bitmap cropToSquare(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width) ? height - (height - width) : height;

        int cropW = (width - height) / 2;
        cropW = (cropW < 0) ? 0 : cropW;

        int cropH = (height - width) / 2;
        cropH = (cropH < 0) ? 0 : cropH;

        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
    }

    private Drawable cropToCircle(Bitmap bitmap) {
        RoundedBitmapDrawable bitmapDrawable =
                RoundedBitmapDrawableFactory.create(weakContext.get().getResources(), bitmap);
        bitmapDrawable.setCircular(true);

        return bitmapDrawable;
    }
}
