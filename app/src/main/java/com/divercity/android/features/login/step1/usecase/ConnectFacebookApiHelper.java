package com.divercity.android.features.login.step1.usecase;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import com.divercity.android.features.signup.entity.SocialLoginEntity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class ConnectFacebookApiHelper {

    private Fragment fragment;
    private OnFacebookDataListener mListener;
    private CallbackManager mFacebookCallbackManager;

    public interface OnFacebookDataListener {
        void onFacebookDataGet(SocialLoginEntity socialLoginEntity);
    }

    public ConnectFacebookApiHelper(Fragment fragment) {
        this.fragment = fragment;
        attachFacebookCallback();
    }

    private void attachFacebookCallback() {
        mFacebookCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mFacebookCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("FacebookHelper", response.toString());
                                        SocialLoginEntity socialLoginEntity = new SocialLoginEntity();
                                        try {
                                            socialLoginEntity.setProvider("facebook");
//                                            socialLoginEntity.setProviderId(object.getString("id"));
//                                            socialLoginEntity.setName(object.getString("first_name"));
//                                            socialLoginEntity.setLastname(object.getString("last_name"));
                                            socialLoginEntity.setEmail(object.getString("email"));
                                            socialLoginEntity.setAccessToken(AccessToken.getCurrentAccessToken().getToken());
//                                            socialLoginEntity.setBirthday(Utilities.formatDateForServer(facebookDateStringToDate(object.getString("birthday"))));
                                            mListener.onFacebookDataGet(socialLoginEntity);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "first_name,last_name,email, picture, id");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        logOut();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(fragment.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logOut(){
        if (AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            LoginManager.getInstance().logOut();
        }
    }

    public CallbackManager getFacebookCallbackManager() {
        return mFacebookCallbackManager;
    }

    public void connectToFacebook() {
        logOut();
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);
        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList("public_profile", "email"));
    }

    public void setListener(OnFacebookDataListener listener) {
        mListener = listener;
    }

    public Date facebookDateStringToDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date d = null;
        try {
            d = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
}
