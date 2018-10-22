package com.divercity.app.features.signup.usecase;

import android.app.Activity;
import android.widget.Toast;

import com.divercity.app.core.utils.Util;
import com.divercity.app.data.networking.twitter.MyTwitterApiClient;
import com.divercity.app.features.signup.entity.SocialLoginEntity;
import com.google.gson.JsonObject;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;

public class ConnectTwitterApiHelper {

    private Activity mActivity;
    private TwitterAuthClient mTwitterAuthClient;
    private OnTwitterDataListener mListener;

    public ConnectTwitterApiHelper(Activity activity){
        mActivity = activity;
        mTwitterAuthClient = new TwitterAuthClient();
    }

    public interface OnTwitterDataListener {
        void onTwitterDataGet(SocialLoginEntity socialLoginEntity);
    }

    public void connect(){
        Util.clearCookies(mActivity.getApplicationContext());
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
        mTwitterAuthClient.authorize(mActivity, new Callback<TwitterSession>() {
            @Override
            public void success(final Result<TwitterSession> result) {
                final TwitterSession sessionData = result.data;
                MyTwitterApiClient myTwitterApiClient = new MyTwitterApiClient(sessionData);
                myTwitterApiClient.getCustomService().getUserData().enqueue(new Callback<JsonObject>() {
                    @Override
                    public void success(Result<JsonObject> result) {
                        SocialLoginEntity socialLoginEntity = new SocialLoginEntity();
                        socialLoginEntity.setProvider("twitter");
                        socialLoginEntity.setEmail(result.data.get("email").getAsString());
                        socialLoginEntity.setName(result.data.get("name").getAsString());
                        socialLoginEntity.setProviderId(result.data.get("id_str").getAsString());
                        socialLoginEntity.setAccessToken(sessionData.getAuthToken().toString());
                        mListener.onTwitterDataGet(socialLoginEntity);
                    }

                    @Override
                    public void failure(TwitterException e) {
                        Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void failure(final TwitterException e) {
                if(!e.getMessage().equals("Authorization failed, request was canceled."))
                    Toast.makeText(mActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public TwitterAuthClient getTwitterAuthClient() {
        return mTwitterAuthClient;
    }

    public void setListener(OnTwitterDataListener mListener) {
        this.mListener = mListener;
    }

}
