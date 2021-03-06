package com.divercity.app.data.entity.questions;

import com.google.gson.annotations.SerializedName;

public class QuestionResponse{

	@SerializedName("relationships")
	private Relationships relationships;

	@SerializedName("attributes")
	private Attributes attributes;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	public void setRelationships(Relationships relationships){
		this.relationships = relationships;
	}

	public Relationships getRelationships(){
		return relationships;
	}

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
			"QuestionResponse{" + 
			"relationships = '" + relationships + '\'' + 
			",attributes = '" + attributes + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}