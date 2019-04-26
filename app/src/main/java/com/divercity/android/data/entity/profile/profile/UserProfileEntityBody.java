package com.divercity.android.data.entity.profile.profile;

import com.google.gson.annotations.SerializedName;

public class UserProfileEntityBody {

	@SerializedName("user")
	private UserProfileEntity user;

	public void setUser(UserProfileEntity user){
		this.user = user;
	}

	public UserProfileEntity getUser(){
		return user;
	}

	@Override
 	public String toString(){
		return 
			"UserProfileBody{" +
			"user = '" + user + '\'' + 
			"}";
		}
}