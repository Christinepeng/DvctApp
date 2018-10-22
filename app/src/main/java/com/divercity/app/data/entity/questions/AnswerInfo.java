package com.divercity.app.data.entity.questions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AnswerInfo{

	@SerializedName("images")
	private List<Object> images;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("text")
	private String text;

	public void setImages(List<Object> images){
		this.images = images;
	}

	public List<Object> getImages(){
		return images;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	@Override
 	public String toString(){
		return 
			"AnswerInfo{" + 
			"images = '" + images + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",text = '" + text + '\'' + 
			"}";
		}
}