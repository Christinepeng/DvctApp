package com.divercity.app.data.entity.profile;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("country")
	private String country;

	@SerializedName("account_type")
	private String accountType;

	@SerializedName("birthdate")
	private String birthdate;

	@SerializedName("occupation")
	private String occupation;

	@SerializedName("gender")
	private String gender;

	@SerializedName("ethnicity")
	private String ethnicity;

	@SerializedName("school_id")
	private int schoolId;

	public void setCountry(String country){
		this.country = country;
	}

	public String getCountry(){
		return country;
	}

	public void setAccountType(String accountType){
		this.accountType = accountType;
	}

	public String getAccountType(){
		return accountType;
	}

	public void setBirthdate(String birthdate){
		this.birthdate = birthdate;
	}

	public String getBirthdate(){
		return birthdate;
	}

	public void setOccupation(String occupation){
		this.occupation = occupation;
	}

	public String getOccupation(){
		return occupation;
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

	public void setSchoolId(int schoolId){
		this.schoolId = schoolId;
	}

	public int getSchoolId(){
		return schoolId;
	}

	@Override
 	public String toString(){
		return 
			"User{" + 
			"country = '" + country + '\'' + 
			",account_type = '" + accountType + '\'' + 
			",birthdate = '" + birthdate + '\'' + 
			",occupation = '" + occupation + '\'' + 
			",gender = '" + gender + '\'' + 
			",ethnicity = '" + ethnicity + '\'' + 
			",school_id = '" + schoolId + '\'' + 
			"}";
		}
}