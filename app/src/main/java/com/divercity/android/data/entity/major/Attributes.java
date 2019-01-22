package com.divercity.android.data.entity.major;

import com.google.gson.annotations.SerializedName;

public class Attributes{

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("people_count")
	private int peopleCount;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setPeopleCount(int peopleCount){
		this.peopleCount = peopleCount;
	}

	public int getPeopleCount(){
		return peopleCount;
	}

	@Override
 	public String toString(){
		return 
			"UserAttributes{" +
			"name = '" + name + '\'' + 
			",description = '" + description + '\'' + 
			",people_count = '" + peopleCount + '\'' + 
			"}";
		}
}