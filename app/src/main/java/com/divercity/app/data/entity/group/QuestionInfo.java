package com.divercity.app.data.entity.group;

import com.google.gson.annotations.SerializedName;

public class QuestionInfo{

	@SerializedName("latest_answer")
	private LatestAnswer latestAnswer;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("id")
	private int id;

	@SerializedName("text")
	private String text;

	@SerializedName("picture_main")
	private String pictureMain;

	public void setLatestAnswer(LatestAnswer latestAnswer){
		this.latestAnswer = latestAnswer;
	}

	public LatestAnswer getLatestAnswer(){
		return latestAnswer;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
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
			"QuestionInfo{" + 
			"latest_answer = '" + latestAnswer + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",id = '" + id + '\'' + 
			",text = '" + text + '\'' + 
			",picture_main = '" + pictureMain + '\'' + 
			"}";
		}
}