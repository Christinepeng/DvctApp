package com.divercity.app.data.entity.emailcheck;

import com.google.gson.annotations.SerializedName;

public class CheckEmailBody{

	@SerializedName("email")
	private String email;

	public CheckEmailBody(String email) {
		this.email = email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"CheckEmailBody{" + 
			"email = '" + email + '\'' + 
			"}";
		}
}