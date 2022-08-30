package com.example.android.nasa.api;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiAPOD {
    @GET("planetary/apod?api_key=zZXtZTelvg1a28CpHQef2jCfYUT3ecAJhVxUM0B6")
    Call<APODData> getAPODData();
}
