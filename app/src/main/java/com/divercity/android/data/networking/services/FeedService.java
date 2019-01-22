package com.divercity.android.data.networking.services;

import com.google.gson.JsonObject;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by lucas on 10/09/2018.
 */

public interface FeedService {

    @GET("questions?filter_by_user_groups=true")
    Observable<JsonObject> fetchQuestions(@Query("page[number]") int pageNumber,
                                          @Query("page[size]") int size);

    @GET("stories/featured")
    Observable<JsonObject> fetchStoresFeatured();
}
