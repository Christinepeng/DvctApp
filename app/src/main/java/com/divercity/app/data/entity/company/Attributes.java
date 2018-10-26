package com.divercity.app.data.entity.company;

import com.google.gson.annotations.SerializedName;

public class Attributes{

	@SerializedName("cover_photos")
	private CoverPhotos coverPhotos;

	@SerializedName("address")
	private String address;

	@SerializedName("name")
	private String name;

	@SerializedName("photos")
	private Photos photos;

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