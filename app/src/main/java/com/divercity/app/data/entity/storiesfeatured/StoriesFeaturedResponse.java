package com.divercity.app.data.entity.storiesfeatured;

import com.google.gson.annotations.SerializedName;

public class StoriesFeaturedResponse{

	@SerializedName("story_type")
	private String storyType;

	@SerializedName("answers_count")
	private int answersCount;

	@SerializedName("id")
	private int id;

	@SerializedName("text")
	private String text;

	@SerializedName("picture_main")
	private String pictureMain;

	public void setStoryType(String storyType){
		this.storyType = storyType;
	}

	public String getStoryType(){
		return storyType;
	}

	public void setAnswersCount(int answersCount){
		this.answersCount = answersCount;
	}

	public int getAnswersCount(){
		return answersCount;
	}

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

	public void setPictureMain(String pictureMain){
		this.pictureMain = pictureMain;
	}

	public String getPictureMain(){
		return pictureMain;
	}

	@Override
 	public String toString(){
		return 
			"StoriesFeaturedResponse{" + 
			"story_type = '" + storyType + '\'' + 
			",answers_count = '" + answersCount + '\'' + 
			",id = '" + id + '\'' + 
			",text = '" + text + '\'' + 
			",picture_main = '" + pictureMain + '\'' + 
			"}";
		}
}