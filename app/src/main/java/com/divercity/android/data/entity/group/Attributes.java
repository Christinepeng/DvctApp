package com.divercity.android.data.entity.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Attributes implements Parcelable {

	@SerializedName("current_user_admin_level")
	private String currentUserAdminLevel;

	@SerializedName("color")
	private String color;

	@SerializedName("questions_count")
	private int questionsCount;

	@SerializedName("description")
	private String description;

	@SerializedName("created_at")
	private int createdAt;

	@SerializedName("group_type")
	private String groupType;

	@SerializedName("title")
	private String title;

	@SerializedName("is_current_user_admin")
	private boolean isCurrentUserAdmin;

	@SerializedName("last_question_activity_date")
	private String lastQuestionActivityDate;

	@SerializedName("last_activity_info")
	private List<LastActivityInfoItem> lastActivityInfo;

	@SerializedName("answers_count")
	private int answersCount;

	@SerializedName("followers_count")
	private int followersCount;

	@SerializedName("unseen_message_count")
	private int unseenMessageCount;

	@SerializedName("state")
	private String state;

	@SerializedName("is_followed_by_current")
	private boolean isFollowedByCurrent;

	@SerializedName("picture_main")
	private String pictureMain;

	@SerializedName("author_info")
	private AuthorInfo authorInfo;

	@SerializedName("request_to_join_status")
	private String requestToJoinStatus;

	protected Attributes(Parcel in) {
		currentUserAdminLevel = in.readString();
		color = in.readString();
		questionsCount = in.readInt();
		description = in.readString();
		createdAt = in.readInt();
		groupType = in.readString();
		title = in.readString();
		isCurrentUserAdmin = in.readByte() != 0;
		lastQuestionActivityDate = in.readString();
		answersCount = in.readInt();
		followersCount = in.readInt();
		unseenMessageCount = in.readInt();
		state = in.readString();
		isFollowedByCurrent = in.readByte() != 0;
		pictureMain = in.readString();
		requestToJoinStatus = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(currentUserAdminLevel);
		dest.writeString(color);
		dest.writeInt(questionsCount);
		dest.writeString(description);
		dest.writeInt(createdAt);
		dest.writeString(groupType);
		dest.writeString(title);
		dest.writeByte((byte) (isCurrentUserAdmin ? 1 : 0));
		dest.writeString(lastQuestionActivityDate);
		dest.writeInt(answersCount);
		dest.writeInt(followersCount);
		dest.writeInt(unseenMessageCount);
		dest.writeString(state);
		dest.writeByte((byte) (isFollowedByCurrent ? 1 : 0));
		dest.writeString(pictureMain);
		dest.writeString(requestToJoinStatus);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
		@Override
		public Attributes createFromParcel(Parcel in) {
			return new Attributes(in);
		}

		@Override
		public Attributes[] newArray(int size) {
			return new Attributes[size];
		}
	};

	public void setCurrentUserAdminLevel(String currentUserAdminLevel){
		this.currentUserAdminLevel = currentUserAdminLevel;
	}

	public String getCurrentUserAdminLevel(){
		return currentUserAdminLevel;
	}

	public void setColor(String color){
		this.color = color;
	}

	public String getColor(){
		return color;
	}

	public void setQuestionsCount(int questionsCount){
		this.questionsCount = questionsCount;
	}

	public int getQuestionsCount(){
		return questionsCount;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setCreatedAt(int createdAt){
		this.createdAt = createdAt;
	}

	public int getCreatedAt(){
		return createdAt;
	}

	public void setGroupType(String groupType){
		this.groupType = groupType;
	}

	public String getGroupType(){
		return groupType;
	}

	public void setTitle(String title){
		this.title = title;
	}

	public String getTitle(){
		return title;
	}

	public void setIsCurrentUserAdmin(boolean isCurrentUserAdmin){
		this.isCurrentUserAdmin = isCurrentUserAdmin;
	}

	public boolean isIsCurrentUserAdmin(){
		return isCurrentUserAdmin;
	}

	public void setLastQuestionActivityDate(String lastQuestionActivityDate){
		this.lastQuestionActivityDate = lastQuestionActivityDate;
	}

	public String getLastQuestionActivityDate(){
		return lastQuestionActivityDate;
	}

	public void setLastActivityInfo(List<LastActivityInfoItem> lastActivityInfo){
		this.lastActivityInfo = lastActivityInfo;
	}

	public List<LastActivityInfoItem> getLastActivityInfo(){
		return lastActivityInfo;
	}

	public void setAnswersCount(int answersCount){
		this.answersCount = answersCount;
	}

	public int getAnswersCount(){
		return answersCount;
	}

	public void setFollowersCount(int followersCount){
		this.followersCount = followersCount;
	}

	public int getFollowersCount(){
		return followersCount;
	}

	public void setUnseenMessageCount(int unseenMessageCount){
		this.unseenMessageCount = unseenMessageCount;
	}

	public int getUnseenMessageCount(){
		return unseenMessageCount;
	}

	public void setState(String state){
		this.state = state;
	}

	public String getState(){
		return state;
	}

	public void setIsFollowedByCurrent(boolean isFollowedByCurrent){
		this.isFollowedByCurrent = isFollowedByCurrent;
	}

	public boolean isIsFollowedByCurrent(){
		return isFollowedByCurrent;
	}

	public void setPictureMain(String pictureMain){
		this.pictureMain = pictureMain;
	}

	public String getPictureMain(){
		return pictureMain;
	}

	public void setAuthorInfo(AuthorInfo authorInfo){
		this.authorInfo = authorInfo;
	}

	public AuthorInfo getAuthorInfo(){
		return authorInfo;
	}

	public void setRequestToJoinStatus(String requestToJoinStatus){
		this.requestToJoinStatus = requestToJoinStatus;
	}

	public String getRequestToJoinStatus(){
		return requestToJoinStatus;
	}

	@Override
 	public String toString(){
		return 
			"UserAttributes{" +
			"current_user_admin_level = '" + currentUserAdminLevel + '\'' + 
			",color = '" + color + '\'' + 
			",questions_count = '" + questionsCount + '\'' + 
			",description = '" + description + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",group_type = '" + groupType + '\'' + 
			",title = '" + title + '\'' + 
			",is_current_user_admin = '" + isCurrentUserAdmin + '\'' + 
			",last_question_activity_date = '" + lastQuestionActivityDate + '\'' + 
			",last_activity_info = '" + lastActivityInfo + '\'' + 
			",answers_count = '" + answersCount + '\'' + 
			",followers_count = '" + followersCount + '\'' + 
			",unseen_message_count = '" + unseenMessageCount + '\'' + 
			",state = '" + state + '\'' + 
			",is_followed_by_current = '" + isFollowedByCurrent + '\'' + 
			",picture_main = '" + pictureMain + '\'' + 
			",author_info = '" + authorInfo + '\'' + 
			",request_to_join_status = '" + requestToJoinStatus + '\'' + 
			"}";
		}
}