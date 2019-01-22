package com.divercity.android.data.entity.school;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Attributes{

	@SerializedName("name")
	private String name;

	@SerializedName("questions_count")
	private int questionsCount;

	@SerializedName("members_count")
	private int membersCount;

	@SerializedName("top_users")
	private List<Object> topUsers;

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setQuestionsCount(int questionsCount){
		this.questionsCount = questionsCount;
	}

	public int getQuestionsCount(){
		return questionsCount;
	}

	public void setMembersCount(int membersCount){
		this.membersCount = membersCount;
	}

	public int getMembersCount(){
		return membersCount;
	}

	public void setTopUsers(List<Object> topUsers){
		this.topUsers = topUsers;
	}

	public List<Object> getTopUsers(){
		return topUsers;
	}

	@Override
 	public String toString(){
		return 
			"UserAttributes{" +
			"name = '" + name + '\'' + 
			",questions_count = '" + questionsCount + '\'' + 
			",members_count = '" + membersCount + '\'' + 
			",top_users = '" + topUsers + '\'' + 
			"}";
		}
}