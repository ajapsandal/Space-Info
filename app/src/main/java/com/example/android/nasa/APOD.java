package com.example.android.nasa;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.android.nasa.api.APODData;
import com.example.android.nasa.api.ApiAPOD;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APOD extends AppCompatActivity {
    private TextView info;
    private TextView title;
    private TextView date;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private String imageURL;


    private SubsamplingScaleImageView image;
    private Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apod);

        toolbar = findViewById(R.id.toolbarApod);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Фотография дня");

        title = findViewById(R.id.titleText);
        info = findViewById(R.id.apodExplanationText);
        date = findViewById(R.id.dateText);
        image = findViewById(R.id.imageApod);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiAPOD api = retrofit.create(ApiAPOD.class);

        Call<APODData> call = api.getAPODData();

        call.enqueue(new Callback<APODData>() {
            @Override
            public void onResponse(Call<APODData> call, Response<APODData> response) {
                if (!response.isSuccessful()) {
                    info.setText("Ошибка!");
                    return;
                }
                APODData data = response.body();
                title.setText(data.getTitle());
                date.setText(data.getDate().replaceAll("-", "/"));
                info.setText(data.getExplanation());

                ImageLoader.getInstance().loadImage(data.getUrl(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        if (!isFinishing()) {
                            photo = loadedImage;
                            image.setImage(ImageSource.cachedBitmap(loadedImage));
                            findViewById(R.id.progressApod).setVisibility(View.GONE);
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<APODData> call, Throwable t) {
                info.setText(t.getMessage() + "Ошибка");
            }
        });
    }
}

