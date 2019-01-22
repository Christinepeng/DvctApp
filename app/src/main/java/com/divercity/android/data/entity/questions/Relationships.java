package com.divercity.android.data.entity.questions;

import com.google.gson.annotations.SerializedName;

public class Relationships{

	@SerializedName("author")
	private Author author;

	@SerializedName("violations")
	private Violations violations;

	@SerializedName("category")
	private Category category;

	public void setAuthor(Author author){
		this.author = author;
	}

	public Author getAuthor(){
		return author;
	}

	public void setViolations(Violations violations){
		this.violations = violations;
	}

	public Violations getViolations(){
		return violations;
	}

	public void setCategory(Category category){
		this.category = category;
	}

	public Category getCategory(){
		return category;
	}

	@Override
 	public String toString(){
		return 
			"Relationships{" + 
			"author = '" + author + '\'' + 
			",violations = '" + violations + '\'' + 
			",category = '" + category + '\'' + 
			"}";
		}
}