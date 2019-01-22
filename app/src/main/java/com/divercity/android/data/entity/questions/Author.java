package com.divercity.android.data.entity.questions;

import com.google.gson.annotations.SerializedName;

public class Author{

	@SerializedName("data")
	private Data data;

	public void setData(Data data){
		this.data = data;
	}

	public Data getData(){
		return data;
	}

	@Override
 	public String toString(){
		return 
			"Author{" + 
			"data = '" + data + '\'' + 
			"}";
		}
}