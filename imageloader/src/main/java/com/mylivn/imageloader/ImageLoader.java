package com.mylivn.imageloader;

import android.content.Context;
import android.widget.ImageView;

public interface ImageLoader {

    static ImageLoader of(Context context){
       return ImageLoaderImpl.get(context);
    }

    ImageLoader into(ImageView imageView);

    ImageLoader loadingPlaceholder(int loadingPlaceholderRes);

    ImageLoader errorPlaceholder(int errorPlaceholderRes);

    ImageLoader isCircular(boolean isCircular);

    ImageLoader callback(Callback callback);

    ImageLoader enableCaching(int cacheSizeInMB);

    void load(String imageUrl);

}
