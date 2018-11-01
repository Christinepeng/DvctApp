package com.divercity.app.data.entity.profile.profile;

import com.google.gson.annotations.SerializedName;

public class UserProfileBody {

	@SerializedName("user")
	private User user;

	public void setUser(User user){
		this.user = user;
	}

	public User getUser(){
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