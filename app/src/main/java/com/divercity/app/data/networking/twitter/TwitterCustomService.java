package com.divercity.app.data.networking.twitter;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TwitterCustomService {

    @GET("/1.1/account/verify_credentials.json?include_entities=false&skip_status=true&include_email=true")
    Call<JsonObject> getUserData();

}
