package com.divercity.android.data.entity.emailusernamecheck;

import com.google.gson.annotations.SerializedName;

public class CheckUsernameEmailResponse {

	@SerializedName("status")
	private String status;

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	@Override
 	public String toString(){
		return 
			"CheckUsernameEmailResponse{" +
			"status = '" + status + '\'' + 
			"}";
		}
}