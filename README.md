# ImageLoader

This project contains the implementation of an image loader library (`imageloader` module) and sample app (`app` module) that shows how to use this library.



### How to use ImageLoader:

```java
ImageLoader.of(this) // this = a Context instance
    .into(imageView)
    .isCircular(true) // OPTIONAL
    .enableCaching(10) // CACHE SIZE IN MB - OPTIONAL
    .loadingPlaceholder(R.drawable.placeholder_loading) // OPTIONAL
    .errorPlaceholder(R.drawable.placeholder_error) // OPTIONAL
    .callback(new Callback() { // OPTIONAL
        // ...
    })
    .load(imageUrl);
```

