package com.mylivn.imageloader.main;

import android.os.Bundle;
import android.widget.Toast;

import com.evernote.android.state.State;
import com.mylivn.imageloader.Callback;
import com.mylivn.imageloader.ImageLoader;
import com.mylivn.imageloader.R;
import com.mylivn.imageloader.databinding.ActivityMainBinding;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

public class MainActivity extends AppCompatActivity implements MainView {

    private ActivityMainBinding binding;

    private final String[] IMAGE_URLS = {
            "https://images.pexels.com/photos/1464143/pexels-photo-1464143.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=1500&w=2500",
            "https://images.pexels.com/photos/335394/pexels-photo-335394.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=1500&w=2500",
            "https://images.pexels.com/photos/1416868/pexels-photo-1416868.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=1500&w=2500",
            "https://images.pexels.com/photos/1308751/pexels-photo-1308751.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=1500&w=2500",
            "https://images.pexels.com/photos/314563/pexels-photo-314563.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=1500&w=2500",
    };

    @State
    public int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainViewModel viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.setView(this);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setViewModel(viewModel);

        loadNewImage();
    }

    @Override
    public void loadNewImage() {
        String imageUrl = IMAGE_URLS[counter++ % IMAGE_URLS.length];

        ImageLoader.of(this)
                .into(binding.imageView)
                .isCircular(true) //OPTIONAL
                .enableCaching(10) //CACHE SIZE IN MB - OPTIONAL
                .loadingPlaceholder(R.drawable.placeholder_loading) //OPTIONAL
                .errorPlaceholder(R.drawable.placeholder_error) //OPTIONAL
                .callback(new Callback() { //OPTIONAL
                    @Override
                    public void onProgress(int progress) {
                        binding.progressBar.setProgress(progress);
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(MainActivity.this,
                                "Error occurred!", Toast.LENGTH_SHORT).show();
                    }
                })
                .load(imageUrl);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        counter--; //TO PREVENT IMAGE CHANGES ON ORIENTATION CHANGES
        super.onSaveInstanceState(outState);
    }
}
