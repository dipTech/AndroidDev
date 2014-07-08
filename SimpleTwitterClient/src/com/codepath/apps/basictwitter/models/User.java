package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.From;
import com.activeandroid.query.Select;

@Table(name = "users")
public class User extends Model implements Serializable {
	@Column(name = "name")
	private String name;
	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column(name = "screenName")
	private String screenName;
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	
	public User(){
		super();
	}

	public User(long uid, String name, String profileUrl, String screenName, String profileImageUrl) {
		super();
		this.uid = uid;
		this.name = name;
		this.profileImageUrl = profileUrl;
		this.screenName = screenName;
		this.profileImageUrl = profileImageUrl;
	}

	public static User fromJSON(JSONObject jsonObject) {
		User user = new User();
		try {
			user.name = jsonObject.getString("name");
			user.uid = jsonObject.getLong("id");
			user.screenName = jsonObject.getString("screen_name");
			user.profileImageUrl = jsonObject.getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return user;
		
	}

	public static List<User> getUser(long userId) {
	    return new Select().from(User.class).where("uid = ?", userId).execute();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getName() {
		return name;
	}

	public long getUid() {
		return uid;
	}

	public String getScreenName() {
		return screenName;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public static void deleteAllUsers(){
		Delete s = new Delete();
		From f = s.from(User.class);
		if (f != null) {
			f.execute();
		}
	}
	
	public static List<User> getAllUsers() {
		Select s = new Select();
		From f = s.from(User.class);
		if (f != null) {
			return f.execute();
		}
		return null;
	}
	
}
