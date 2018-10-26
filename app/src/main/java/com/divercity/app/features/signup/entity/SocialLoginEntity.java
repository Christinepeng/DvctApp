package com.divercity.app.features.signup.entity;

import com.google.gson.annotations.SerializedName;

public class SocialLoginEntity {

    @SerializedName("access_token")
    private String accessToken;

    @SerializedName("provider")
    private String provider;

    @SerializedName("name")
    private String name;

    @SerializedName("lastname")
    private String lastname;

    @SerializedName("provider_id")
    private String providerId;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("email")
    private String email;

    @SerializedName("birthday")
    private String birthday;

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProvider() {
        return provider;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return
                "SocialLoginEntity{" +
                        "access_token = '" + accessToken + '\'' +
                        ",provider = '" + provider + '\'' +
                        ",name = '" + name + '\'' +
                        ",provider_id = '" + providerId + '\'' +
                        ",avatar = '" + avatar + '\'' +
                        ",email = '" + email + '\'' +
                        ",lastname = '" + lastname + '\'' +
                        ",birthday = '" + birthday + '\'' +
                        "}";
    }
}