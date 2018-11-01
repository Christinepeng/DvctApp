package com.divercity.app.data.entity.emailusernamecheck.usernamebody;

import com.google.gson.annotations.SerializedName;

public class CheckUsernameBody {

	@SerializedName("username")
	private String username;

	public CheckUsernameBody(String username) {
		this.username = username;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"CheckUsernameBody{" +
			"username = '" + username + '\'' +
			"}";
		}
}