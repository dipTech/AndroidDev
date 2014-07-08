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

	@Column(name = "tweet_id")
	private long tweetId;
	@Column(name = "name")
	private String name;
	@Column(name = "handle")
	private String handle;
	@Column(name = "body")
	private String body;
	@Column(name = "content")
	private String content;

	@Column(name = "uid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private long uid;
	@Column(name = "retweet_count")
	private long retweet_count;
	@Column(name = "favourites_count")
	private int favourites_count;
	@Column(name = "retweeted")
	private boolean retweeted;
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
            tweet.name = json.getJSONObject("user").getString("name");
            tweet.handle = "@" + json.getJSONObject("user").getString("screen_name");
            tweet.tweetId = json.getLong("id");
			tweet.body = json.getString("text");
			tweet.uid = json.getLong("id");
			tweet.createdAt = json.getString("created_at");
			tweet.user = User.fromJSON(json.getJSONObject("user"));
			tweet.userId = tweet.user.getUid();
			tweet.retweet_count = json.getLong("retweet_count");
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
	
	public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray, String maxId){
		ArrayList<Tweet> tweetResults = new ArrayList<Tweet>();
		for(int i=0; i< jsonArray.length(); i++){
			JSONObject json = null;
			try {
				json = jsonArray.getJSONObject(i);
			}catch (JSONException e) {
				continue;
			}

			if(json!=null){				
				if(maxId.isEmpty())
					tweetResults.add(Tweet.fromJSON(json));
				else{
					Tweet t = Tweet.fromJSON(json, false);
					if(!String.valueOf(t.getUid()).equals(maxId))
						tweetResults.add(t);
				}
			}
			
		}
		return tweetResults;
	}
	
	public static Tweet fromJSON(JSONObject jsonObj, boolean persistInDb){
		Tweet tweet = new Tweet();
		
		try {
			tweet.body = jsonObj.getString("text");
			tweet.createdAt = jsonObj.getString("created_at");
			tweet.uid = jsonObj.getLong("id");
			tweet.user = User.fromJSON(jsonObj.getJSONObject("user"));
			tweet.userId = tweet.user.getUid();
			if(persistInDb){
				tweet.user.save();
				tweet.save();
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweet;
		
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public void setFavorited(boolean favorited) {
		this.favorited = favorited;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getRelativeTimeAgo() {
		try {
			long currentTime = System.currentTimeMillis();
			long diff = currentTime
					- simpleDateFormat.parse(createdAt).getTime();
			if (diff > 0) {
				int time = (int) (diff / 1000);
				String t = simpleDateFormat.format(createdAt);

				if (time < 60) {
					t = String.valueOf(time) + "s";
				} else if (time < 60 * 60) {
					t = String.valueOf(time / 60) + "m";
				} else if (time < 60 * 60 * 24) {
					t = String.valueOf(time / 3600) + "h";
				} else if (time < 60 * 60 * 24 * 10) {
					t = String.valueOf(time / (3600 * 24)) + "d";
				} else {
					long millis = simpleDateFormat.parse(createdAt).getTime();
					t = DateUtils.getRelativeTimeSpanString(millis,
							System.currentTimeMillis(),
							DateUtils.SECOND_IN_MILLIS).toString();
				}
				return t;
			} 
		} catch (ParseException e) {
			Log.d("twit", e.getMessage());
		}
		return ("Now");
	}
	
	public static Tweet getTweetById(long tweetId){
		return (Tweet) new Select().from(Tweet.class).where("tweet_id", tweetId).execute();
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
	
	public long getTweetId() {
		return tweetId;
	}
	public void setTweetId(long id) {
		this.tweetId = id;
	}

	public String getName() {
		return name;
	}

	public String getHandle() {
		return handle;
	}

	public boolean isFavorited() {
		return favorited;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

    public static SimpleDateFormat getSimpledateformat() {
		return simpleDateFormat;
	}

	public long getRetweet_count() {
		return retweet_count;
	}

	public void setRetweet_count(long retweet_count) {
		this.retweet_count = retweet_count;
	}

	public int getFavourites_count() {
		return favourites_count;
	}

	public void setFavourites_count(int favourites_count) {
		this.favourites_count = favourites_count;
	}

	public boolean isRetweeted() {
		return retweeted;
	}

	public void setRetweeted(boolean retweeted) {
		this.retweeted = retweeted;
	}

}
