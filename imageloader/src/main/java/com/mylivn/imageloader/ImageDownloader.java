package com.mylivn.imageloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

class ImageDownloader extends AsyncTask<String, Integer, Bitmap> {

    private static final String TAG = "ImageDownloader";

    private InternalCallback callback;

    ImageDownloader(InternalCallback callback) {
        this.callback = callback;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return downloadImage(params[0]);
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        callback.onProgress(values[0]);
    }

    @Override
    protected void onPreExecute() {
        callback.onStart();
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            callback.onImageReady(bitmap);
        } else {
            callback.onError(new IOException("Image could not be loaded!"));
        }
    }

    @Override
    protected void onCancelled() {
        callback.onCancelled();
    }

    ImageDownloader execute(String imageUrl) {
        super.execute(imageUrl);
        return this;
    }

    private Bitmap downloadImage(String imageUrl) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;

        InputStream stream = null;
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            int lengthOfFile = connection.getContentLength();

            stream = connection.getInputStream();

            final int bufLen = 4 * 0x400;
            byte[] bytes = new byte[bufLen];
            int readLen;
            long total = 0;
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                while ((readLen = stream.read(bytes, 0, bufLen)) != -1) {
                    outputStream.write(bytes, 0, readLen);
                    total += readLen;

                    publishProgress((int) ((total * 100) / lengthOfFile));
                }

                bytes = outputStream.toByteArray();
            }

            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }
        }
    }
}