package com.divercity.android.data.entity.base;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lucas on 18/10/2018.
 */

public class DataObject<T> {

    @SerializedName("data")
    T data;

    public T getData() {
        return data;
    }
}
