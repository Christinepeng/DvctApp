package com.divercity.app.data.entity.questions;

import com.google.gson.annotations.SerializedName;

public class LastActivityInfo{

	@SerializedName("answer_info")
	private AnswerInfo answerInfo;

	@SerializedName("author_info")
	private AuthorInfo authorInfo;

	public void setAnswerInfo(AnswerInfo answerInfo){
		this.answerInfo = answerInfo;
	}

	public AnswerInfo getAnswerInfo(){
		return answerInfo;
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
			"LastActivityInfo{" + 
			"answer_info = '" + answerInfo + '\'' + 
			",author_info = '" + authorInfo + '\'' + 
			"}";
		}
}