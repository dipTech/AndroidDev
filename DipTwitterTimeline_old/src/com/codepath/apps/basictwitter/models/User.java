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
	
	private static final long serialVersionUID = 1L;

	@Column(name = "name")
	private String name;
	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column(name = "screenName")
	private String screenName;
	@Column(name = "profileImageUrl")
	private String profileImageUrl;
	@Column(name = "profileBannerUrl")
	private String profileBannerUrl;
	@Column(name = "description")
	private String description;
	@Column(name = "followers_count")
	private int followers_count;
	@Column(name = "friends_count")
	private int friends_count;
	@Column(name = "favourites_count")
	private int favourites_count;
	@Column(name = "listed_count")
	private int listed_count;
	@Column(name = "statuses_count")
	private int statuses_count;
	@Column(name = "location")
	private String location;


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
            user.description = jsonObject.getString("description");
            user.location = jsonObject.getString("location");
            user.favourites_count = jsonObject.getInt("favourites_count");
            user.followers_count = jsonObject.getInt("followers_count");
            user.friends_count = jsonObject.getInt("friends_count");
            user.listed_count = jsonObject.getInt("listed_count");
            user.statuses_count = jsonObject.getInt("statuses_count");
			user.profileImageUrl = jsonObject.getString("profile_image_url");

            try {
            	user.profileBannerUrl = jsonObject.getString("profile_banner_url");
            }
            catch (JSONException profileBannerEx) {
            	user.profileBannerUrl = null;
            	profileBannerEx.printStackTrace();
            }
            
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user;
		
	}

	public static List<User> findById(long userId) {
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

	public String getTagline() {
		return ("description"); // do toString dipu
	}

	public int getFriends_count() {
		return friends_count;
	}
	public void setFriends_count(int friends_count) {
		this.friends_count = friends_count;
	}
	public int getFavourites_count() {
		return favourites_count;
	}
	public void setFavourites_count(int favourites_count) {
		this.favourites_count = favourites_count;
	}
	public int getListed_count() {
		return listed_count;
	}
	public void setListed_count(int listed_count) {
		this.listed_count = listed_count;
	}
	public int getStatuses_count() {
		return statuses_count;
	}
	public void setStatuses_count(int statuses_count) {
		this.statuses_count = statuses_count;
	}
	
	public static void deleteAllUsers(){
		Delete s = new Delete();
		From f = s.from(User.class);
		if (f != null) {
			f.execute();
		}
	}
	
	public static List<User> findAll() {
		return new Select().from(User.class).orderBy("uid").execute();
	}
	
	public int getFollowers_count() {
		return followers_count;
	}
	
	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}
	
	public String getProfileBannerUrl() {
		return profileBannerUrl;
	}
	public void setProfileBannerUrl(String backgroundImage) {
		this.profileBannerUrl = backgroundImage;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
