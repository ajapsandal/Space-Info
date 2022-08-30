package com.example.android.nasa.api;

import com.google.gson.annotations.SerializedName;

public class APODData {
    @SerializedName("title")
    private String title;
    @SerializedName("explanation")
    private String explanation;
    @SerializedName("date")
    private String date;
    @SerializedName("hdurl")
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}
