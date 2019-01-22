package com.divercity.android.data.entity.base;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

/**
 * Created by lucas on 18/10/2018.
 */

public class IncludedArray<T> {

    @SerializedName("included")
    List<T> data;

    public List<T> getData() {
        if(data != null)
            return data;
        else
            return Collections.emptyList();
    }
}
