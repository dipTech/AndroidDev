package com.codepath.healthpact.models;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("AppUser")
public class AppUser extends ParseObject {

	public AppUser() {
		super();
	}
	
	public AppUser(String name) {
		super();
		setName(name);
	}

	public AppUser(String name, String passwd, String expertise,
			long location_id, String desc, Date post_date, Date update_date,
			boolean plan_created) {
		super();
		
		setName(name);
		/*
		setPassword(passwd);
		setExpertise(expertise);
		setLocationId(location_id);
		setDescription(desc);
		setPostDate(post_date);
		setUpdateDate(update_date);
		setPlanCreated(plan_created);
		*/
	}

	public void setPlanCreated(boolean isPlanCreated) {
		put("isPlanCreated", isPlanCreated);
	}
	
	public boolean getPlanCreated() {
		return getBoolean("isPlanCreated");
	}

	private void setUpdateDate(Date update_date) {
		put("update_date", update_date);

	}
	
	public Date getUpdateDate() {
		return getDate("update_date");
	}


	public void setPostDate(Date post_date) {
		put("post_date", post_date);

	}

	public Date getPostDate() {
		return getDate("post_date");
	}

	public void setDescription(String desc) {
		put("description", desc);

	}
	
	public String getDescription() {
		return getString("passwd");
	}

	public void setLocationId(long locationId) {
		put("locationId", locationId);

	}
	
	public long getLocationId() {
		return getLong("locationId");
	}

	public void setExpertise(String expertise) {
		put("expertise", expertise);
	}

	public String getExpertise() {
		return getString("expertise");
	}
	
	public void setPassword(String passwd) {
		put("passwd", passwd);
	}

	public String getPassword() {
		return getString("passwd");
	}
	
	public void setName(String name) {
		put("name", name);
	}

	public String getName() {
		return getString("name");
	}
	
	// Get the user for this item
	public ParseUser getUser() {
		return getParseUser("owner");
	}

	// Associate each item with a user
	public void setOwner(ParseUser user) {
		put("owner", user);
	}
}
