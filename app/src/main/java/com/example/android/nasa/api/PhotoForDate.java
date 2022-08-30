package com.example.android.nasa.api;

public class PhotoForDate {
    private String date;
    private String caption;
    private String image;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    private String identifier;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl(){
        StringBuilder sb = new StringBuilder();
        sb.append("https://api.nasa.gov/EPIC/archive/natural/");
        String[] dateComponents = date.split(" ")[0].split("-");
        sb.append(dateComponents[0]).append('/');
        sb.append(dateComponents[1]).append('/');
        sb.append(dateComponents[2]).append("/png/");
        sb.append(image).append(".png?api_key=").append(ControllerNASA.KEY);

        return sb.toString();
    }
}
