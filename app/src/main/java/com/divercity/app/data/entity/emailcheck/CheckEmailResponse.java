package com.divercity.app.data.entity.emailcheck;

import com.google.gson.annotations.SerializedName;

public class CheckEmailResponse{

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
			"CheckEmailResponse{" + 
			"status = '" + status + '\'' + 
			"}";
		}
}