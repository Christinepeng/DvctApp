package com.divercity.app.data.networking.twitter;

import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterSession;

public class MyTwitterApiClient extends TwitterApiClient {

    public MyTwitterApiClient(TwitterSession session) {
        super(session);
    }

    public TwitterCustomService getCustomService() {
        return getService(TwitterCustomService.class);
    }

}

