package com.divercity.app.data.entity.industry;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Attributes implements Parcelable {

	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("people_count")
	private int peopleCount;

	protected Attributes(Parcel in) {
		name = in.readString();
		description = in.readString();
		peopleCount = in.readInt();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(description);
		dest.writeInt(peopleCount);
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

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setPeopleCount(int peopleCount){
		this.peopleCount = peopleCount;
	}

	public int getPeopleCount(){
		return peopleCount;
	}

	@Override
 	public String toString(){
		return 
			"Attributes{" + 
			"name = '" + name + '\'' + 
			",description = '" + description + '\'' + 
			",people_count = '" + peopleCount + '\'' + 
			"}";
		}
}