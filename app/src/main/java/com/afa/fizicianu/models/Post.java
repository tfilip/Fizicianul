package com.afa.fizicianu.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by filip on 5/26/2016.
 */
public class Post {

    @SerializedName("permalink")
    private String permalink;
    @SerializedName("thumbnail")
    private String thumbnail;
    @SerializedName("title")
    private String title;

    public String getPermalink() {
        return permalink;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }
}
