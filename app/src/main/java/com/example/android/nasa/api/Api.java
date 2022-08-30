package com.example.android.nasa.api;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    @GET("natural/all")
    Single<List<DatePhotos>> getDate();

    @GET("natural/date/{date}")
    Single<List<PhotoForDate>> getPhotos(@Path("date") String date);

}
