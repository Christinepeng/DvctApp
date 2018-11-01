package com.divercity.app.data.entity.signup;

import com.google.gson.annotations.SerializedName;

public class SignUpBody{

	@SerializedName("password")
	private String password;

	@SerializedName("password_confirmation")
	private String passwordConfirmation;

	@SerializedName("nickname")
	private String nickname;

	@SerializedName("name")
	private String name;

	@SerializedName("email")
	private String email;

	public SignUpBody(String password, String passwordConfirmation, String nickname, String name, String email) {
		this.password = password;
		this.passwordConfirmation = passwordConfirmation;
		this.nickname = nickname;
		this.name = name;
		this.email = email;
	}

	public void setPassword(String password){
		this.password = password;
	}

	public String getPassword(){
		return password;
	}

	public void setPasswordConfirmation(String passwordConfirmation){
		this.passwordConfirmation = passwordConfirmation;
	}

	public String getPasswordConfirmation(){
		return passwordConfirmation;
	}

	public void setNickname(String nickname){
		this.nickname = nickname;
	}

	public String getNickname(){
		return nickname;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	@Override
 	public String toString(){
		return 
			"SignUpBody{" + 
			"password = '" + password + '\'' + 
			",password_confirmation = '" + passwordConfirmation + '\'' + 
			",nickname = '" + nickname + '\'' + 
			",name = '" + name + '\'' + 
			",email = '" + email + '\'' + 
			"}";
		}
}