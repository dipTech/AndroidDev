package com.codepath.apps.basictwitter;

import java.io.ByteArrayInputStream;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
    public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
    public static final String REST_CONSUMER_KEY = "m54KxSCBMLO60S8Lzt55vxpLm";       // Change this
    public static final String REST_CONSUMER_SECRET = "3HTMsf1dYQRCxKY3rlwMMzKvmKDamCdPHhmgYojjPCy7qygfUG"; // Change this
    public static final String REST_CALLBACK_URL = "oauth://cpbasictweets"; // Change this (here and in manifest)
    //private static final int REC_COUNT = 20;

    public TwitterClient(Context context) {
        super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
    }
    
    public void getHomeTimeline(AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/home_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("since_id", "1");
    	client.get(apiUrl, params, handler);
    }
    
    public void getHomeTimeline(long sinceId, long maxId, AsyncHttpResponseHandler handler) {
    	if (sinceId <= 0) {
    		getHomeTimelineMaxId(maxId, handler);
    	}
    }
    
    public void getHomeTimelineSinceId(long sinceId, AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/home_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("since_id", String.valueOf(sinceId));
    	client.get(apiUrl, params, handler);
    }    
    
    public void getHomeTimelineMaxId(long offset, AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/home_timeline.json");
    	RequestParams params = new RequestParams();
    	if (offset > 0) {
    		params.put("max_id", String.valueOf(offset));
    	}
		//params.put("count", String.valueOf(REC_COUNT));
    	client.get(apiUrl, params, handler);
    }

    public void postTweet(String content, AsyncHttpResponseHandler handler) {
		postTweet(content, null, handler);
	}
	
    public void postTweet(String msg, String inReplyToUser, AsyncHttpResponseHandler handler) {
    	//String apiUrl = getApiUrl("direct_messages/new.json");
    	String apiUrl = getApiUrl("statuses/update.json");
    	RequestParams params = new RequestParams();
		params.put("status", msg);
    	//params.put("text", msg);
		if (inReplyToUser != null) {
			params.put("in_reply_to_status_id", inReplyToUser);
		}

    	client.post(apiUrl, params, handler);
    }

	public void getMentionsTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    	RequestParams params = new RequestParams();
    	params.put("since_id", "1");
    	client.get(apiUrl, null, handler);
    }

	public void getMentionsTimelineMaxId(long offset, JsonHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    	RequestParams params = new RequestParams();
    	if (offset > 0) {
    		params.put("max_id", String.valueOf(offset));
    	}
		//params.put("count", String.valueOf(REC_COUNT));
    	client.get(apiUrl, params, handler);		
	}
    
	public void getMentionsTimelineSinceId(long offset, JsonHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/mentions_timeline.json");
    	RequestParams params = new RequestParams();
    	if (offset > 0) {
    		params.put("since_id", String.valueOf(offset));
    	}
		//params.put("count", String.valueOf(REC_COUNT));
    	client.get(apiUrl, params, handler);		
	}
    
	// dipu rename it to getMyInfo
    public void getUser (AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("account/verify_credentials.json");
        client.get(apiUrl, null, handler);    	
    }

    public void getUserTimeline(AsyncHttpResponseHandler handler) {
    	String apiUrl = getApiUrl("statuses/user_timeline.json");
        client.get(apiUrl, null, handler);    	
    }
    
	public void postTweet(String content, String inReplyTo,
			ByteArrayInputStream image, AsyncHttpResponseHandler handler) {
		String apiUrl;
		RequestParams params = new RequestParams();

		// 64 bit encoding not required
		if (image != null) {
			apiUrl = getApiUrl("statuses/update_with_media.json");
			params.put("media[]", image);
		} else {
			apiUrl = getApiUrl("statuses/update.json");
		}
		params.put("status", content);
		if (inReplyTo != null) {
			params.put("in_reply_to_status_id", content);
		}
		getClient().post(apiUrl, params, handler);
	}
	
	public void deleteTweet(AsyncHttpResponseHandler handler, String statusId){
		String url = getApiUrl("statuses/destroy/"+statusId+".json");
		client.post(url, handler);
	}

	public void reTweet(AsyncHttpResponseHandler handler, String statusId){
		String url = getApiUrl("statuses/retweet/"+statusId+".json");
		client.post(url, handler);
	}
	
	public void replyTweet(AsyncHttpResponseHandler handler, String msg, String statusId){
		String url = getApiUrl("statuses/update.json");
		RequestParams params = new RequestParams();
		params.put("status", msg);
		params.put("in_reply_to_status_id", statusId);
		client.post(url, params, handler);
	}
}
