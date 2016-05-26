package com.afa.fizicianu.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by filip on 5/26/2016.
 */
public class Children {
    @SerializedName("data")
    private Post post;

    public Post getPost(){
        return post;
    }
}
