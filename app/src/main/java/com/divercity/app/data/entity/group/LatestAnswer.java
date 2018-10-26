package com.divercity.app.data.entity.group;

import com.google.gson.annotations.SerializedName;

public class LatestAnswer{

	@SerializedName("id")
	private int id;

	@SerializedName("text")
	private String text;

	@SerializedName("author_info")
	private AuthorInfo authorInfo;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setText(String text){
		this.text = text;
	}

	public String getText(){
		return text;
	}

	public void setAuthorInfo(AuthorInfo authorInfo){
		this.authorInfo = authorInfo;
	}

	public AuthorInfo getAuthorInfo(){
		return authorInfo;
	}

	@Override
 	public String toString(){
		return 
			"LatestAnswer{" + 
			"id = '" + id + '\'' + 
			",text = '" + text + '\'' + 
			",author_info = '" + authorInfo + '\'' + 
			"}";
		}
}