package com.divercity.app.data.entity.questions;

import com.google.gson.annotations.SerializedName;

public class Data{

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	@Override
 	public String toString(){
		return 
			"LoginResponse{" +
			"id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}