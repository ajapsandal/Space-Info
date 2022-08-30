package com.example.android.nasa;

import android.app.Application;
import android.service.controls.Control;

import com.example.android.nasa.api.ControllerNASA;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class App extends Application {
    ControllerNASA controller;
    @Override
    public void onCreate() {
        super.onCreate();

        controller = new ControllerNASA();

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new LruMemoryCache(20 * 1024 * 1024))
                .memoryCacheSize(20 * 1024 * 1024)
                .build();

        ImageLoader.getInstance().init(config);
    }

    public ControllerNASA getController() {
        return controller;
    }
}
