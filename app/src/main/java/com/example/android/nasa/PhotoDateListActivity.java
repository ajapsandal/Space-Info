package com.example.android.nasa;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.nasa.api.DatePhotos;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;




public class PhotoDateListActivity extends AppCompatActivity {
    private CompositeDisposable disposable;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private Adapter adapter;

    private static final String EXTRA_URL = "date_url";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_photo);
        disposable = new CompositeDisposable();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Даты фотографий");

        recyclerView = findViewById(R.id.list);

        adapter = new Adapter();

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        App app = (App) getApplication();

        disposable.add(app.getController().getApi().getDate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BiConsumer<List<DatePhotos>, Throwable>() {
                    @Override
                    public void accept(List<DatePhotos> datePhotos, Throwable throwable) throws Exception{
                        if(throwable != null) {
                            Toast.makeText(PhotoDateListActivity.this, "Ошибка", Toast.LENGTH_LONG);
                        }
                        else {
                            adapter.setDates(datePhotos);
                        }
                    }
                }));
    }

    private class Adapter extends RecyclerView.Adapter<ViewHolder>{
        ArrayList<DatePhotos> dates = new ArrayList<>();

        public void setDates (List<DatePhotos> dates){
            this.dates.clear();
            this.dates.addAll(dates);
            notifyDataSetChanged();
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new ViewHolder(LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.item_date, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(dates.get(position));
        }

        @Override
        public int getItemCount() {
            return dates.size();
        }
    }

    @Override
    protected void onDestroy() {
        disposable.dispose();
        super.onDestroy();
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        DatePhotos date;

        TextView text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PhotoDateListActivity.this, PhotoDateActivity.class);
                    intent.putExtra(EXTRA_URL, date.getDate());
                    startActivity(intent);

                }
            });
        }

        public void bind(DatePhotos date){
            this.date = date;
            text.setText(date.getDate());
        }
    }
}