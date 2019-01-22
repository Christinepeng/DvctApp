package com.divercity.android.data.entity.questions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Violations{

	@SerializedName("data")
	private List<Object> data;

	public void setData(List<Object> data){
		this.data = data;
	}

	public List<Object> getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"Violations{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}