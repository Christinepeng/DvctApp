package com.divercity.app.data.entity.company.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class CompanyResponse implements Parcelable {

	@SerializedName("attributes")
	private Attributes attributes;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

	public CompanyResponse(String id) {
		this.id = id;
	}

	protected CompanyResponse(Parcel in) {
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

	public static final Creator<CompanyResponse> CREATOR = new Creator<CompanyResponse>() {
		@Override
		public CompanyResponse createFromParcel(Parcel in) {
			return new CompanyResponse(in);
		}

		@Override
		public CompanyResponse[] newArray(int size) {
			return new CompanyResponse[size];
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

	@Override
 	public String toString(){
		return 
			"CompanyResponse{" + 
			"attributes = '" + attributes + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}