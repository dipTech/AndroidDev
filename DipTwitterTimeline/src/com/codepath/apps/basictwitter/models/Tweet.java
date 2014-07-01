package com.codepath.apps.basictwitter.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.format.DateUtils;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Column.ForeignKeyAction;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.util.Log;

@Table(name = "tweets")
public class Tweet extends Model implements Serializable {
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy");

	private static final long serialVersionUID = 1L;

	@Column(name = "body")
	private String body;
	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
    public static SimpleDateFormat getSimpledateformat() {
		return simpleDateFormat;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	@Column(name = "retweetCount")
	private int retweetCount;
    @Column(name = "favorited")
	private boolean favorited;
    @Column(name = "mediaUrl")
	private String mediaUrl;

	@Column(name = "createdAt", index = true)
	private String createdAt;

	@Column(name = "User", onUpdate = ForeignKeyAction.CASCADE, onDelete = ForeignKeyAction.CASCADE)
	private User user;
	@Column
	private Long userId;

	public Tweet() {
		super();
	}
	
	public Tweet(long uid, String body, String createdAt, User user) {
		super();
		this.body = body;
		this.uid = uid;
		this.createdAt = createdAt;
		this.user = user;
	}

	public static Tweet fromJSON(JSONObject json) {
		Tweet tweet = new Tweet();
		// extract values from json to populate the member variables
		try {
			tweet.body = json.getString("text");
			tweet.uid = json.getLong("id");
			tweet.createdAt = json.getString("created_at");
			tweet.user = User.fromJSON(json.getJSONObject("user"));
			tweet.userId = tweet.user.getUid();
			tweet.retweetCount = json.getInt("retweet_count");
			tweet.favorited = json.getBoolean("favorited");
			
			try {
				tweet.mediaUrl = json.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
			} catch (JSONException je_media) {
				tweet.mediaUrl = null;
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return tweet;		
	}

	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
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
				Log.d("TWEET::" + tweet.getBody());
				tweets.add(tweet);
			}
		}
		return tweets;
	}
	
	public static List<Tweet> findAll() {
		return new Select().from(Tweet.class).orderBy("uid DESC").execute();
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

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public String getBody() {
		return body;
	}

	public long getUid() {
		return uid;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public User getUser() {
		return user;
	}

	public String toString(){
		return body;
	}

	public Long getUserId(){
		return userId;
	}

	public String getRelativeTimeAgo() {
		String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
		SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
		sf.setLenient(true);

		String relativeDate = "";
		try {
			long dateMillis = sf.parse(createdAt).getTime();
			relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
					System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_ALL).toString();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return relativeDate;
	}
	
	public static Tweet findById(Long id){
		return new Select().from(Tweet.class).where("uid = ?", id).executeSingle();
	}

	public static void saveAll(List<Tweet> tweets){
		ActiveAndroid.beginTransaction();
		try {
			for (Tweet tweet: tweets) {
				// save user first and then the parent Tweet
				tweet.user.save();
				tweet.save();
			}
			ActiveAndroid.setTransactionSuccessful();
		} finally {
			ActiveAndroid.endTransaction();
		}
	}
}
