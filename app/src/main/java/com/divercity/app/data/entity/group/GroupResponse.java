package com.divercity.app.data.entity.group;

import com.google.gson.annotations.SerializedName;

public class GroupResponse{

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