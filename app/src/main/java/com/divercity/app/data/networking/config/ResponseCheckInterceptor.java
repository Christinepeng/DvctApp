package com.divercity.app.data.networking.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.divercity.app.R;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ResponseCheckInterceptor implements Interceptor {

    public Context context;

    public ResponseCheckInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
        if (!(netInfo != null && netInfo.isConnected())) {
            throw new NoConnectivityException();
        }

        Response response = chain.proceed(chain.request());
        return response;
    }

    public class NoConnectivityException extends IOException {

        @Override
        public String getMessage() {
            return context.getResources().getString(R.string.error_connection);
        }

    }

}
