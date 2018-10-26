package com.divercity.app.data.entity.login.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Attributes{

	@SerializedName("country")
	private String country;

	@SerializedName("avatar_medium")
	private String avatarMedium;

	@SerializedName("account_type")
	private String accountType;

	@SerializedName("occupation")
	private String occupation;

	@SerializedName("birthdate")
	private String birthdate;

	@SerializedName("role")
	private String role;

	@SerializedName("gender")
	private String gender;

	@SerializedName("ethnicity")
	private String ethnicity;

	@SerializedName("city")
	private String city;

	@SerializedName("timezone")
	private String timezone;

	@SerializedName("phonenumber")
	private String phonenumber;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("questions_count")
	private int questionsCount;

	@SerializedName("occupation_of_interests")
	private List<String> occupationOfInterests;

	@SerializedName("group_of_interest_following_count")
	private int groupOfInterestFollowingCount;

	@SerializedName("uid")
	private String uid;

	@SerializedName("answers_count")
	private int answersCount;

	@SerializedName("nickname")
	private String nickname;

	@SerializedName("is_followed_by_current")
	private boolean isFollowedByCurrent;

	@SerializedName("email")
	private String email;

	@SerializedName("lat")
	private String lat;

	@SerializedName("no_password_set")
	private String noPasswordSet;

	@SerializedName("lng")
	private String lng;

	@SerializedName("avatar_thumb")
	private String avatarThumb;

	@SerializedName("last_name")
	private String lastName;

	@SerializedName("school_name")
	private String schoolName;

	@SerializedName("is_default_avatar")
	private boolean isDefaultAvatar;

	@SerializedName("interest_ids")
	private List<Integer> interestIds;

	@SerializedName("following_count")
	private int followingCount;

	@SerializedName("followers_count")
	private int followersCount;

	@SerializedName("industries")
	private List<String> industries;

	@SerializedName("student_majors")
	private List<String> studentMajors;

	@SerializedName("name")
	private String name;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setAvatarMedium(String avatarMedium){
		this.avatarMedium = avatarMedium;
	}

	public String getAvatarMedium(){
		return avatarMedium;
	}

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	public String getAccountType(){
		return accountType;
	}

	public void setOccupation(String occupation){
		this.occupation = occupation;
	}

	public String getOccupation(){
		return occupation;
	}

	public void setBirthdate(String birthdate){
		this.birthdate = birthdate;
	}

	public String getBirthdate(){
		return birthdate;
	}

	public void setRole(String role){
		this.role = role;
	}

	public String getRole(){
		return role;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setEthnicity(String ethnicity){
		this.ethnicity = ethnicity;
	}

	public String getEthnicity(){
		return ethnicity;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getCity(){
		return city;
	}

	public void setTimezone(String timezone){
		this.timezone = timezone;
	}

	public String getTimezone(){
		return timezone;
	}

	public void setPhonenumber(String phonenumber){
		this.phonenumber = phonenumber;
	}

	public String getPhonenumber(){
		return phonenumber;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setQuestionsCount(int questionsCount){
		this.questionsCount = questionsCount;
	}

	public int getQuestionsCount(){
		return questionsCount;
	}

	public void setOccupationOfInterests(List<String> occupationOfInterests){
		this.occupationOfInterests = occupationOfInterests;
	}

	public List<String> getOccupationOfInterests(){
		return occupationOfInterests;
	}

	public void setGroupOfInterestFollowingCount(int groupOfInterestFollowingCount){
		this.groupOfInterestFollowingCount = groupOfInterestFollowingCount;
	}

	public int getGroupOfInterestFollowingCount(){
		return groupOfInterestFollowingCount;
	}

	public void setUid(String uid){
		this.uid = uid;
	}

	public String getUid(){
		return uid;
	}

	public void setAnswersCount(int answersCount){
		this.answersCount = answersCount;
	}

	public int getAnswersCount(){
		return answersCount;
	}

	public void setNickname(String nickname){
		this.nickname = nickname;
	}

	public String getNickname(){
		return nickname;
	}

	public void setIsFollowedByCurrent(boolean isFollowedByCurrent){
		this.isFollowedByCurrent = isFollowedByCurrent;
	}

	public boolean isIsFollowedByCurrent(){
		return isFollowedByCurrent;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setLat(String lat){
		this.lat = lat;
	}

	public String getLat(){
		return lat;
	}

	public void setNoPasswordSet(String noPasswordSet){
		this.noPasswordSet = noPasswordSet;
	}

	public String getNoPasswordSet(){
		return noPasswordSet;
	}

	public void setLng(String lng){
		this.lng = lng;
	}

	public String getLng(){
		return lng;
	}

	public void setAvatarThumb(String avatarThumb){
		this.avatarThumb = avatarThumb;
	}

	public String getAvatarThumb(){
		return avatarThumb;
	}

	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	public String getLastName(){
		return lastName;
	}

	public void setSchoolName(String schoolName){
		this.schoolName = schoolName;
	}

	public String getSchoolName(){
		return schoolName;
	}

	public void setIsDefaultAvatar(boolean isDefaultAvatar){
		this.isDefaultAvatar = isDefaultAvatar;
	}

	public boolean isIsDefaultAvatar(){
		return isDefaultAvatar;
	}

	public void setInterestIds(List<Integer> interestIds){
		this.interestIds = interestIds;
	}

	public List<Integer> getInterestIds(){
		return interestIds;
	}

	public void setFollowingCount(int followingCount){
		this.followingCount = followingCount;
	}

	public int getFollowingCount(){
		return followingCount;
	}

	public void setFollowersCount(int followersCount){
		this.followersCount = followersCount;
	}

	public int getFollowersCount(){
		return followersCount;
	}

	public void setIndustries(List<String> industries){
		this.industries = industries;
	}

	public List<String> getIndustries(){
		return industries;
	}

	public void setStudentMajors(List<String> studentMajors){
		this.studentMajors = studentMajors;
	}

	public List<String> getStudentMajors(){
		return studentMajors;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	@Override
 	public String toString(){
		return 
			"Attributes{" + 
			"country = '" + country + '\'' + 
			",avatar_medium = '" + avatarMedium + '\'' + 
			",account_type = '" + accountType + '\'' + 
			",occupation = '" + occupation + '\'' + 
			",birthdate = '" + birthdate + '\'' + 
			",role = '" + role + '\'' + 
			",gender = '" + gender + '\'' + 
			",ethnicity = '" + ethnicity + '\'' + 
			",city = '" + city + '\'' + 
			",timezone = '" + timezone + '\'' + 
			",phonenumber = '" + phonenumber + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",questions_count = '" + questionsCount + '\'' + 
			",occupation_of_interests = '" + occupationOfInterests + '\'' + 
			",group_of_interest_following_count = '" + groupOfInterestFollowingCount + '\'' + 
			",uid = '" + uid + '\'' + 
			",answers_count = '" + answersCount + '\'' + 
			",nickname = '" + nickname + '\'' + 
			",is_followed_by_current = '" + isFollowedByCurrent + '\'' + 
			",email = '" + email + '\'' + 
			",lat = '" + lat + '\'' + 
			",no_password_set = '" + noPasswordSet + '\'' + 
			",lng = '" + lng + '\'' + 
			",avatar_thumb = '" + avatarThumb + '\'' + 
			",last_name = '" + lastName + '\'' + 
			",school_name = '" + schoolName + '\'' + 
			",is_default_avatar = '" + isDefaultAvatar + '\'' + 
			",interest_ids = '" + interestIds + '\'' + 
			",following_count = '" + followingCount + '\'' + 
			",followers_count = '" + followersCount + '\'' + 
			",industries = '" + industries + '\'' + 
			",student_majors = '" + studentMajors + '\'' + 
			",name = '" + name + '\'' + 
			"}";
		}
}