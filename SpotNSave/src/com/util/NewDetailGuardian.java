package com.util;

import java.io.Serializable;

import com.epicelements.spotnsave.DetailGuardian.lists;

@SuppressWarnings("serial")
public class NewDetailGuardian implements Serializable {
	private String id;
	private String userID;
	private String name;
	private String image;
	private String phoneno;
	private String permisssion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getPhoneno() {
		return phoneno;
	}

	public void setPhoneno(String phoneno) {
		this.phoneno = phoneno;
	}

	public String getPermisssion() {
		return permisssion;
	}

	public void setPermisssion(String permisssion) {
		this.permisssion = permisssion;
	}

	public void getGuardianDetailFor(lists lists, int i) {
		this.id = lists.id.get(i);
		this.userID = lists.userID.get(i);
		this.name = lists.name.get(i);
		this.image = lists.image.get(i);
		this.phoneno = lists.phoneno.get(i);
		this.permisssion = lists.permisssion.get(i);
	}

}
