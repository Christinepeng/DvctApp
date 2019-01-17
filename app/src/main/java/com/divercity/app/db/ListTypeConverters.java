package com.divercity.app.db;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/**
 * Created by lucas on 16/01/2019.
 */

public class ListTypeConverters {

    private Gson gson = new Gson();

    @TypeConverter
    public List<Object> stringToSomeObjectList(String data) {
        if (data == null)
            return Collections.emptyList();
        Type listType = new TypeToken<List<Object>>() {}.getType();
        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public String someObjectListToString(List<Object> someObjects) {
        return gson.toJson(someObjects);
    }
}