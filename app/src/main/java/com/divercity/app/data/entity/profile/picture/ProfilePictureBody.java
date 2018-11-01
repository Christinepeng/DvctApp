package com.divercity.app.data.entity.profile.picture;

import com.google.gson.annotations.SerializedName;

public class ProfilePictureBody{

	@SerializedName("avatar")
	private String avatar;

	public ProfilePictureBody(String avatar) {
		this.avatar = avatar;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getAvatar(){
		return avatar;
	}

	@Override
 	public String toString(){
		return 
			"ProfilePictureBody{" + 
			"avatar = '" + avatar + '\'' + 
			"}";
		}
}