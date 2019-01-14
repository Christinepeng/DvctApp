package com.divercity.app.data.entity.group;

import android.os.Parcel;
import android.os.Parcelable;

import com.divercity.app.data.entity.home.RecommendedItem;
import com.google.gson.annotations.SerializedName;

public class GroupResponse implements Parcelable, RecommendedItem {

	@SerializedName("attributes")
	private Attributes attributes;

	@SerializedName("id")
	private String id;

	@SerializedName("type")
	private String type;

    //	For ViewHolder
    private boolean isSelected = false;

	public GroupResponse(String id) {
		this.id = id;
	}

	protected GroupResponse(Parcel in) {
		attributes = in.readParcelable(Attributes.class.getClassLoader());
		id = in.readString();
		type = in.readString();
		isSelected = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(attributes, flags);
		dest.writeString(id);
		dest.writeString(type);
		dest.writeByte((byte) (isSelected ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<GroupResponse> CREATOR = new Creator<GroupResponse>() {
		@Override
		public GroupResponse createFromParcel(Parcel in) {
			return new GroupResponse(in);
		}

		@Override
		public GroupResponse[] newArray(int size) {
			return new GroupResponse[size];
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

	public boolean getSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}

	public boolean isPublic(){
		return attributes.getGroupType().equals("public");
	}

	public boolean isJoinRequestPending(){
		return attributes.getRequestToJoinStatus().equals("pending");
	}

	public boolean isJoinRequestNotSend(){
		return attributes.getRequestToJoinStatus().equals("none");
	}

	@Override
 	public String toString(){
		return 
			"GroupResponse{" + 
			"attributes = '" + attributes + '\'' + 
			",id = '" + id + '\'' + 
			",type = '" + type + '\'' + 
			"}";
		}
}