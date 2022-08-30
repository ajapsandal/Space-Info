package com.example.android.nasa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.nasa.api.PhotoForDate;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.functions.BiConsumer;

public class PhotoDateActivity extends AppCompatActivity {
    private CompositeDisposable disposable;
    private RecyclerView recyclerView;
    private Adapter adapter;
    private Toolbar toolbar;

    private static final String EXTRA_URL = "date_url";
    private static final String IMAGE_URL = "image_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_photo);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setTitle("Время фотографий");

        disposable = new CompositeDisposable();
        recyclerView = findViewById(R.id.list);

        adapter = new Adapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,
                false));
        recyclerView.setAdapter(adapter);

        App app = (App) getApplication();

        disposable.add(app.getController().getApi().getPhotos(getIntent().getStringExtra(EXTRA_URL))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new BiConsumer<List<PhotoForDate>, Throwable>(){
                    @Override
                    public void accept(List<PhotoForDate> photoForDates, Throwable throwable) throws Exception {
                        if(throwable != null){
                            Toast.makeText(PhotoDateActivity.this, "Ошибка!", Toast.LENGTH_LONG);
                        }
                        else {
                            adapter.setPhotos(photoForDates);
                        }
                    }
                }));
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder>{
        ArrayList<PhotoForDate> list = new ArrayList<>();

        public void setPhotos(List<PhotoForDate> photos){
            this.list.clear();
            this.list.addAll(photos);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext())
            .inflate(R.layout.item_photo, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
                holder.bind(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        PhotoForDate photo;
        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PhotoDateActivity.this, PhotoActivity.class);
                    intent.putExtra(IMAGE_URL, photo.getImageUrl());
                    startActivity(intent);

                }
            });
        }

        public void bind(PhotoForDate date){
            text.setText(date.getDate());
            photo = date;
        }
    }
}