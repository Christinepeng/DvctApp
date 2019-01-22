package com.divercity.android.data.entity.school;

import com.google.gson.annotations.SerializedName;

public class SchoolResponse{

	@SerializedName("attributes")
	private Attributes attributes;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	public void setAttributes(Attributes attributes){
		this.attributes = attributes;
	}

	public Attributes getAttributes(){
		return attributes;
	}

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
			"SchoolResponse{" + 
			"userAttributes = '" + attributes + '\'' +
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}