package com.divercity.app.data.entity.group;

import com.google.gson.annotations.SerializedName;

public class AuthorInfo{

	@SerializedName("avatar_medium")
	private String avatarMedium;

	@SerializedName("avatar_thumb")
	private String avatarThumb;

	@SerializedName("name")
	private String name;

	@SerializedName("nickname")
	private String nickname;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("id")
	private int id;

	@SerializedName("present")
	private boolean present;

	public void setAvatarMedium(String avatarMedium){
		this.avatarMedium = avatarMedium;
	}

	public String getAvatarMedium(){
		return avatarMedium;
	}

	public void setAvatarThumb(String avatarThumb){
		this.avatarThumb = avatarThumb;
	}

	public String getAvatarThumb(){
		return avatarThumb;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setNickname(String nickname){
		this.nickname = nickname;
	}

	public String getNickname(){
		return nickname;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPresent(boolean present){
		this.present = present;
	}

	public boolean isPresent(){
		return present;
	}

	@Override
 	public String toString(){
		return 
			"AuthorInfo{" + 
			"avatar_medium = '" + avatarMedium + '\'' + 
			",avatar_thumb = '" + avatarThumb + '\'' + 
			",name = '" + name + '\'' + 
			",nickname = '" + nickname + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",id = '" + id + '\'' + 
			",present = '" + present + '\'' + 
			"}";
		}
}