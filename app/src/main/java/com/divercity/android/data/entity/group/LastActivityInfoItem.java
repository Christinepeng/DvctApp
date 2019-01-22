package com.divercity.android.data.entity.group;

import com.google.gson.annotations.SerializedName;

public class LastActivityInfoItem{

	@SerializedName("question_info")
	private QuestionInfo questionInfo;

	@SerializedName("author_info")
	private AuthorInfo authorInfo;

	public void setQuestionInfo(QuestionInfo questionInfo){
		this.questionInfo = questionInfo;
	}

	public QuestionInfo getQuestionInfo(){
		return questionInfo;
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
			"LastActivityInfoItem{" + 
			"question_info = '" + questionInfo + '\'' + 
			",author_info = '" + authorInfo + '\'' + 
			"}";
		}
}