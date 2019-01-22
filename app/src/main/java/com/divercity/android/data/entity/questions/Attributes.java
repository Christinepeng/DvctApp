package com.divercity.android.data.entity.questions;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Attributes{

	@SerializedName("heat")
	private int heat;

	@SerializedName("latest_answerers_info")
	private List<LatestAnswerersInfoItem> latestAnswerersInfo;

	@SerializedName("aggregated_sentiment_counts")
	private List<Object> aggregatedSentimentCounts;

	@SerializedName("report_count")
	private int reportCount;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("description")
	private String description;

	@SerializedName("voted")
	private String voted;

	@SerializedName("sent_as_most_heated")
	private boolean sentAsMostHeated;

	@SerializedName("location_display_name")
	private String locationDisplayName;

	@SerializedName("sb_channel_url")
	private String sbChannelUrl;

	@SerializedName("category_id")
	private int categoryId;

	@SerializedName("updated_at")
	private String updatedAt;

	@SerializedName("last_activity_info")
	private LastActivityInfo lastActivityInfo;

	@SerializedName("answers_count")
	private int answersCount;

	@SerializedName("text")
	private String text;

	@SerializedName("author_info")
	private AuthorInfo authorInfo;

	@SerializedName("group")
	private List<GroupItem> group;

	@SerializedName("is_bookmarked_by_current")
	private boolean isBookmarkedByCurrent;

	@SerializedName("address")
	private String address;

	@SerializedName("question_type")
	private String questionType;

	@SerializedName("current_user_sentiment_id")
	private int currentUserSentimentId;

	@SerializedName("votesdown")
	private int votesdown;

	@SerializedName("anonymous")
	private boolean anonymous;

	@SerializedName("sentiment_type")
	private String sentimentType;

	@SerializedName("author_id")
	private int authorId;

	@SerializedName("is_flagged")
	private boolean isFlagged;

	@SerializedName("votesup")
	private int votesup;

	@SerializedName("picture_main")
	private String pictureMain;

	public void setHeat(int heat){
		this.heat = heat;
	}

	public int getHeat(){
		return heat;
	}

	public void setLatestAnswerersInfo(List<LatestAnswerersInfoItem> latestAnswerersInfo){
		this.latestAnswerersInfo = latestAnswerersInfo;
	}

	public List<LatestAnswerersInfoItem> getLatestAnswerersInfo(){
		return latestAnswerersInfo;
	}

	public void setAggregatedSentimentCounts(List<Object> aggregatedSentimentCounts){
		this.aggregatedSentimentCounts = aggregatedSentimentCounts;
	}

	public List<Object> getAggregatedSentimentCounts(){
		return aggregatedSentimentCounts;
	}

	public void setReportCount(int reportCount){
		this.reportCount = reportCount;
	}

	public int getReportCount(){
		return reportCount;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setVoted(String voted){
		this.voted = voted;
	}

	public String getVoted(){
		return voted;
	}

	public void setSentAsMostHeated(boolean sentAsMostHeated){
		this.sentAsMostHeated = sentAsMostHeated;
	}

	public boolean isSentAsMostHeated(){
		return sentAsMostHeated;
	}

	public void setLocationDisplayName(String locationDisplayName){
		this.locationDisplayName = locationDisplayName;
	}

	public String getLocationDisplayName(){
		return locationDisplayName;
	}

	public void setSbChannelUrl(String sbChannelUrl){
		this.sbChannelUrl = sbChannelUrl;
	}

	public String getSbChannelUrl(){
		return sbChannelUrl;
	}

	public void setCategoryId(int categoryId){
		this.categoryId = categoryId;
	}

	public int getCategoryId(){
		return categoryId;
	}

	public void setUpdatedAt(String updatedAt){
		this.updatedAt = updatedAt;
	}

	public String getUpdatedAt(){
		return updatedAt;
	}

	public void setLastActivityInfo(LastActivityInfo lastActivityInfo){
		this.lastActivityInfo = lastActivityInfo;
	}

	public LastActivityInfo getLastActivityInfo(){
		return lastActivityInfo;
	}

	public void setAnswersCount(int answersCount){
		this.answersCount = answersCount;
	}

	public int getAnswersCount(){
		return answersCount;
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

	public void setGroup(List<GroupItem> group){
		this.group = group;
	}

	public List<GroupItem> getGroup(){
		return group;
	}

	public void setIsBookmarkedByCurrent(boolean isBookmarkedByCurrent){
		this.isBookmarkedByCurrent = isBookmarkedByCurrent;
	}

	public boolean isIsBookmarkedByCurrent(){
		return isBookmarkedByCurrent;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setQuestionType(String questionType){
		this.questionType = questionType;
	}

	public String getQuestionType(){
		return questionType;
	}

	public void setCurrentUserSentimentId(int currentUserSentimentId){
		this.currentUserSentimentId = currentUserSentimentId;
	}

	public int getCurrentUserSentimentId(){
		return currentUserSentimentId;
	}

	public void setVotesdown(int votesdown){
		this.votesdown = votesdown;
	}

	public int getVotesdown(){
		return votesdown;
	}

	public void setAnonymous(boolean anonymous){
		this.anonymous = anonymous;
	}

	public boolean isAnonymous(){
		return anonymous;
	}

	public void setSentimentType(String sentimentType){
		this.sentimentType = sentimentType;
	}

	public String getSentimentType(){
		return sentimentType;
	}

	public void setAuthorId(int authorId){
		this.authorId = authorId;
	}

	public int getAuthorId(){
		return authorId;
	}

	public void setIsFlagged(boolean isFlagged){
		this.isFlagged = isFlagged;
	}

	public boolean isIsFlagged(){
		return isFlagged;
	}

	public void setVotesup(int votesup){
		this.votesup = votesup;
	}

	public int getVotesup(){
		return votesup;
	}

	public void setPictureMain(String pictureMain){
		this.pictureMain = pictureMain;
	}

	public String getPictureMain(){
		if(pictureMain.equals("https://apinew.pincapp.com/images/default_avatar.png"))
			return null;
		else
			return pictureMain;
	}

	@Override
 	public String toString(){
		return 
			"UserAttributes{" +
			"heat = '" + heat + '\'' + 
			",latest_answerers_info = '" + latestAnswerersInfo + '\'' + 
			",aggregated_sentiment_counts = '" + aggregatedSentimentCounts + '\'' + 
			",report_count = '" + reportCount + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",description = '" + description + '\'' + 
			",voted = '" + voted + '\'' + 
			",sent_as_most_heated = '" + sentAsMostHeated + '\'' + 
			",location_display_name = '" + locationDisplayName + '\'' + 
			",sb_channel_url = '" + sbChannelUrl + '\'' + 
			",category_id = '" + categoryId + '\'' + 
			",updated_at = '" + updatedAt + '\'' + 
			",last_activity_info = '" + lastActivityInfo + '\'' + 
			",answers_count = '" + answersCount + '\'' + 
			",text = '" + text + '\'' + 
			",author_info = '" + authorInfo + '\'' + 
			",group = '" + group + '\'' + 
			",is_bookmarked_by_current = '" + isBookmarkedByCurrent + '\'' + 
			",address = '" + address + '\'' + 
			",question_type = '" + questionType + '\'' + 
			",current_user_sentiment_id = '" + currentUserSentimentId + '\'' + 
			",votesdown = '" + votesdown + '\'' + 
			",anonymous = '" + anonymous + '\'' + 
			",sentiment_type = '" + sentimentType + '\'' + 
			",author_id = '" + authorId + '\'' + 
			",is_flagged = '" + isFlagged + '\'' + 
			",votesup = '" + votesup + '\'' + 
			",picture_main = '" + pictureMain + '\'' + 
			"}";
		}
}