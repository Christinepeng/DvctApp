package com.divercity.app.data.entity.profile;

import com.google.gson.annotations.SerializedName;

public class User{

	@SerializedName("account_type")
	private String accountType;

	@SerializedName("birthdate")
	private String birthdate;

	@SerializedName("occupation")
	private String occupation;

	@SerializedName("school_id")
	private int schoolId;

	@SerializedName("id")
	private int id;

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

	public void setSchoolId(int schoolId){
		this.schoolId = schoolId;
	}

	public int getSchoolId(){
		return schoolId;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"User{" + 
			"account_type = '" + accountType + '\'' + 
			",birthdate = '" + birthdate + '\'' + 
			",occupation = '" + occupation + '\'' + 
			",school_id = '" + schoolId + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}