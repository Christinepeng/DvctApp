package com.divercity.app.data.entity.company;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Attributes implements Parcelable {

	@SerializedName("cover_photos")
	private CoverPhotos coverPhotos;

	@SerializedName("address")
	private String address;

	@SerializedName("name")
	private String name;

	@SerializedName("photos")
	private Photos photos;

	protected Attributes(Parcel in) {
		address = in.readString();
		name = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(address);
		dest.writeString(name);
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

	public void setCoverPhotos(CoverPhotos coverPhotos){
		this.coverPhotos = coverPhotos;
	}

	public CoverPhotos getCoverPhotos(){
		return coverPhotos;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public String getAddress(){
		return address;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setPhotos(Photos photos){
		this.photos = photos;
	}

	public Photos getPhotos(){
		return photos;
	}

	@Override
 	public String toString(){
		return 
			"Attributes{" + 
			"cover_photos = '" + coverPhotos + '\'' + 
			",address = '" + address + '\'' + 
			",name = '" + name + '\'' + 
			",photos = '" + photos + '\'' + 
			"}";
		}
}