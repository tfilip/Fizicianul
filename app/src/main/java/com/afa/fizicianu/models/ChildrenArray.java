package com.afa.fizicianu.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by filip on 5/26/2016.
 */
public class ChildrenArray {
    @SerializedName("children")
    private List<Children> childrenList;

    public List<Children> getChildrenList(){
        return childrenList;
    }

}
