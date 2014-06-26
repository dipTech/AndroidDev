package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

@Table(name = "tweets")
public class Tweet extends Model implements Serializable {
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

	@Column(name = "body")
	private String body;
	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	
	@Column(name = "createdAt", index = true)
	private Date createdAt;


	@Column(name = "user")
	private User user;

	public Tweet() {
		super();
	}
	
	public Tweet(long uid, String body, Date createdAt, User user) {
		super();
		this.body = body;
		this.uid = uid;
		this.createdAt = createdAt;
		this.user = user;
	}

	public static Tweet fromJSON(JSONObject jsonObject) {
		Tweet tweet = new Tweet();
		// extract values from json to populate the member variables
		try {
			tweet.body = jsonObject.getString("text");
			tweet.uid = jsonObject.getLong("id");
			tweet.createdAt = getDate(jsonObject.getString("created_at"));
			tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;		
	}

	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray, boolean persist) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());
		
		for (int i=0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			Tweet tweet = Tweet.fromJSON(tweetJson);
			if (tweet != null) {
				tweets.add(tweet);
				if (persist) {
					tweet.user.save();
					tweet.save();
				}
			}
		}
		return tweets;
	}
	
	public static List<Tweet> findAll() {
		return new Select().from(Tweet.class).execute();
	}

	public static void deleteAllTweets() {
		new Delete().from(Tweet.class).execute();
	}


	public void setBody(String body) {
		this.body = body;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	private static Date getDate(String strDate) {
		Date dtDate = new Date();
		try {
			dtDate= simpleDateFormat.parse(strDate);
		} catch (ParseException e) {
			dtDate = null;
			e.printStackTrace();
		}
		return dtDate;
	}

	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

}
