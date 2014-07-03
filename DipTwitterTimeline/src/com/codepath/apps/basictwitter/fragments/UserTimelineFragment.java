package com.codepath.apps.basictwitter.fragments;

import org.json.JSONArray;

import android.os.Bundle;

import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//populateTimeline();
	}

	public void populateTimeline() {
		TwitterApplication.getRestClient().getUserTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				getAdapter().addAll(Tweet.fromJSONArray(jsonTweets));
			}
		});

	}
	
	public void populateTimeline(User user) {
		TwitterApplication.getRestClient().userTimeline(new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				getAdapter().addAll(Tweet.fromJSONArray(jsonTweets));
			}
		}, user.getScreenName());

	}
	

}
