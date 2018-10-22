package com.divercity.app.data.entity.company;

import com.google.gson.annotations.SerializedName;

public class CoverPhotos{

	@SerializedName("small")
	private String small;

	@SerializedName("original")
	private String original;

	@SerializedName("large")
	private String large;

	@SerializedName("thumb")
	private String thumb;

	@SerializedName("main")
	private String main;

	@SerializedName("medium")
	private String medium;

	public void setSmall(String small){
		this.small = small;
	}

	public String getSmall(){
		return small;
	}

	public void setOriginal(String original){
		this.original = original;
	}

	public String getOriginal(){
		return original;
	}

	public void setLarge(String large){
		this.large = large;
	}

	public String getLarge(){
		return large;
	}

	public void setThumb(String thumb){
		this.thumb = thumb;
	}

	public String getThumb(){
		return thumb;
	}

	public void setMain(String main){
		this.main = main;
	}

	public String getMain(){
		return main;
	}

	public void setMedium(String medium){
		this.medium = medium;
	}

	public String getMedium(){
		return medium;
	}

	@Override
 	public String toString(){
		return 
			"CoverPhotos{" + 
			"small = '" + small + '\'' + 
			",original = '" + original + '\'' + 
			",large = '" + large + '\'' + 
			",thumb = '" + thumb + '\'' + 
			",main = '" + main + '\'' + 
			",medium = '" + medium + '\'' + 
			"}";
		}
}