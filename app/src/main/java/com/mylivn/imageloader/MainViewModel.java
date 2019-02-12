package com.mylivn.imageloader;

import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private MainView view;

    void setView(MainView view) {
        this.view = view;
    }

    public void onNextImageButtonClicked() {
        if (view == null)
            return;
        view.loadNewImage();
    }
}
