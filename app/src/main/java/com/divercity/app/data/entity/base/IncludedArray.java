package com.divercity.app.data.entity.base;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by lucas on 18/10/2018.
 */

public class IncludedArray<T> {

    @SerializedName("included")
    List<T> data;

    public List<T> getData() {
        return data;
    }
}
