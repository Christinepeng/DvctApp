package com.divercity.app.data.entity.school;

import com.google.gson.annotations.SerializedName;

public class Attributes{

	@SerializedName("name")
	private String name;

	@SerializedName("questions_count")
	private int questionsCount;

	@SerializedName("members_count")
	private int membersCount;

	@SerializedName("top_users")
	private String topUsers;

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

	public void setTopUsers(String topUsers){
		this.topUsers = topUsers;
	}

	public String getTopUsers(){
		return topUsers;
	}

	@Override
 	public String toString(){
		return 
			"Attributes{" + 
			"name = '" + name + '\'' + 
			",questions_count = '" + questionsCount + '\'' + 
			",members_count = '" + membersCount + '\'' + 
			",top_users = '" + topUsers + '\'' + 
			"}";
		}
}