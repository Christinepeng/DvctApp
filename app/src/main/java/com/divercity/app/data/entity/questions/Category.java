package com.divercity.app.data.entity.questions;

import com.google.gson.annotations.SerializedName;

public class Category{

	@SerializedName("data")
	private String data;

	public void setData(String data){
		this.data = data;
	}

	public String getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"Category{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}