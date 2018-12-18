package com.divercity.app.data.entity.industry;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class IndustryResponse implements Parcelable {

	@SerializedName("attributes")
	private Attributes attributes;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	private boolean isSelected = false;

	protected IndustryResponse(Parcel in) {
		attributes = in.readParcelable(Attributes.class.getClassLoader());
		id = in.readString();
		type = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(attributes, flags);
		dest.writeString(id);
		dest.writeString(type);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<IndustryResponse> CREATOR = new Creator<IndustryResponse>() {
		@Override
		public IndustryResponse createFromParcel(Parcel in) {
			return new IndustryResponse(in);
		}

		@Override
		public IndustryResponse[] newArray(int size) {
			return new IndustryResponse[size];
		}
	};

	public void setAttributes(Attributes attributes){
		this.attributes = attributes;
	}

	public Attributes getAttributes(){
		return attributes;
	}

	public void setId(String id){
		this.id = id;
	}

	public String getId(){
		return id;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getType(){
		return type;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	@Override
 	public String toString(){
		return 
			"IndustryResponse{" + 
			"attributes = '" + attributes + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}