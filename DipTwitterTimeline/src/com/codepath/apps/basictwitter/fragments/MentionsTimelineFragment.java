package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.EndlessScrollListener;
import com.codepath.apps.basictwitter.TweetAutoLink;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.util.TweetUtility;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class MentionsTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
		// dipu setupViews();
		populateTimeline();
	}

	public void fetchMentionsTimeLine(final int page, long l) {

		getActivity().setProgressBarIndeterminateVisibility(true);
		client.getHomeTimelineSinceId(l, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(int code, JSONArray body) {
				jsonHandlerSuccessFunction(page, body);
			}

			@Override
			public void onFailure(Throwable e, JSONObject error) {
				// Handle the failure and alert the user to retry
				jsonHandlerFailureFunction(e.getLocalizedMessage());
			}
		});
	}
	
	public void populateTimeline() {
		client.getMentionsTimeline(new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(getActivity(), "Network Connection Error\nLoading from database schema....", Toast.LENGTH_SHORT).show();
				dbFetch();
			}

			@Override
			public void onSuccess(JSONArray json) {
				addAll(Tweet.fromJSONArray(json));
				// dipankar db.addAllTweet(tweets);
			}
		});
	}
	
	private void setupViews() {
		// Attach the listener to the AdapterView onCreate
/*		lvTweets.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), TweetAutoLink.class);
				startActivity(i);				
			}
			
		});		
*/
	}


/*	public void populateTimeline() {
		client.getMentionsTimeline(new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(getActivity(), "Network Connection Error\nLoading from database schema....", Toast.LENGTH_SHORT).show();
				dbFetch();
			}

			@Override
			public void onSuccess(JSONArray json) {
				fragmentTweetsList.clear();
				aTweets.notifyDataSetInvalidated();
				List<Tweet> dbTweeets = Tweet.findAll();
				aTweets.addAll(dbTweeets);
				((PullToRefreshListView) lvTweets).onRefreshComplete();

				// dipu recent addAll(Tweet.fromJSONArray(json));				
				// dipankar db.addAllTweet(tweets);
			}
		});
	}
*/	
	private void dbFetch() {
		addAll(Tweet.findAll());
	}
	
	public void populateTimelineMaxId(long offset) {
		client.getMentionsTimelineMaxId(offset, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}

			@Override
			public void onSuccess(JSONArray json) {
				List<Tweet> sinceTweet = Tweet.fromJSONArray(json);
				//tweets.addAll(0, sinceTweet);
				//aTweets.notifyDataSetChanged();
			}
		});
	}
	
	
	
	
	
	public void populateTimeLine() {
		showProgressBar();
		client.getMentionsTimeline(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONArray jsonArray) {				
				clearAdapter();
				clearDB();				
				addAll(Tweet.fromJSONArray(jsonArray,""));
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(context, "Load from database failed", Toast.LENGTH_SHORT).show();
				populateHomeLineFromDB();
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				clearProgressBar();
			}
		});		
	}

	@Override
	public void populateTimeLineSinceLatest(String uid) {
		Log.d("twit", "add all "+ uid);
		client.getMentionsTimelineSinceId(Long.valueOf(uid), new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONArray jsonArray) {
				Log.d("twit", "mention refresh "+jsonArray.length());
				ArrayList<Tweet> latestT = Tweet.fromJSONArray(jsonArray,"");
				Log.d("twit", "mention refresh "+latestT.size());
				addLatestTweets(latestT);
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(context, "Tweet retreive failed", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onFinish() {
				lvTweets.onRefreshComplete();
				super.onFinish();
			}
		});

	}

	@Override
	public void loadMore(final String maxId) {
		client.getMentionsTimelineMaxId(Long.valueOf(maxId), new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONArray jsonArray) {
				addAll(Tweet.fromJSONArray(jsonArray, maxId));						
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(context, "Tweet retreive failed", Toast.LENGTH_SHORT).show();
			}
		});
	}
}
