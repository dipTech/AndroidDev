package com.codepath.apps.basictwitter.fragments;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.Toast;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.activeandroid.util.Log;
import com.codepath.apps.basictwitter.EndlessScrollListener;
import com.codepath.apps.basictwitter.TweetAutoLink;
import com.codepath.apps.basictwitter.TweetClickDialog;
import com.codepath.apps.basictwitter.TwitterApplication;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.basictwitter.util.TweetUtility;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class HomeTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		client = TwitterApplication.getRestClient();
		// check network for user info
		if (TweetUtility.isNetworkAvailable(getActivity())) {
			getDefaultUser();
		}
		populateTimeline();
		//setupViews();
	}
	
	public void fetchHomeTimeLine(final int page, long l) {

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

		/*
	protected void customLoadMoreData(long since_id, long max_id, int totalItemsCount) {
		if(TweetUtility.isNetworkAvailable(getActivity()) ) {

            if(totalItemsCount > 0) {
				Toast.makeText(getActivity(), "load1....", Toast.LENGTH_SHORT).show();

            	Tweet oldestTweet = (Tweet) ((AdapterView<ListAdapter>) lvTweets).getItemAtPosition(totalItemsCount - 1);
				if (oldestTweet != null) {
					client.getHomeTimeline(0, oldestTweet.getUid() - 1, new JsonHttpResponseHandler());
				}
			} else {
				Toast.makeText(getActivity(), "load 2....", Toast.LENGTH_SHORT).show();
				// first time pull, no data on schema
				Tweet.deleteAllTweets();
				aTweets.clear();
				aTweets.notifyDataSetInvalidated();
				client.getHomeTimeline(new JsonHttpResponseHandler());
			}
		} else {
			Toast.makeText(getActivity(), "load 3 network down....", Toast.LENGTH_SHORT).show();
			// dipu show Network Unavailable;
			// load from DB
			aTweets.clear();
			aTweets.notifyDataSetInvalidated();
			List<Tweet> dbTweeets = Tweet.findAll();
			aTweets.addAll(dbTweeets);
			((PullToRefreshListView) lvTweets).onRefreshComplete();
			//dipu Util.hideProgressBar(this);
		}
	}
*/
	
	private void setupViews() {
		// Attach the listener to the AdapterView onCreate
				
		/*
		// Set a listener to be invoked when the list should be refreshed.
        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.
            	
				Toast.makeText(getActivity(), "on refresh....", Toast.LENGTH_SHORT).show();
				customLoadMoreData(aTweets.getItem(0).getUid(), 0, 0);
            	//  dipu now populateTimelineMaxId(aTweets.getItem(0).getUid());
            }
            
        });
        
        /*
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				android.app.FragmentManager fragmentManager_for_tweet_click_dialog = getActivity().getFragmentManager();
				Tweet tweet = tweets.get(position);
				TweetClickDialog tweetClickDialog = TweetClickDialog.newInstance(tweet, TweetUtility.defaultUser);
				tweetClickDialog.show(fragmentManager_for_tweet_click_dialog, "dialog_click_tweet");
			}			
		});
		*/

	}

	public void populateTimeline() {
		client.getHomeTimeline(new JsonHttpResponseHandler() {

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
	
	private void dbFetch() {
		addAll(Tweet.findAll());
	}
	
	/*
	public void populateTimelineMaxId(long offset) {
		client.getHomeTimelineMaxId(offset, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}

			@Override
			public void onSuccess(JSONArray json) {
				List<Tweet> sinceTweet = Tweet.fromJSONArray(json);
				tweets.addAll(0, sinceTweet);
				aTweets.notifyDataSetChanged();
			}
		});
	}
	*/
	
	public void getDefaultUser() {
		client.getUser(new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject json) {						
				TweetUtility.defaultUser = User.fromJSON(json);
				if (TweetUtility.DEBUG) {
					showToast("User Identified....");
				}
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
				showToast("Unidentified User....");
			}
			
		});
	}

	public void populateTimeLine(){		
		
		client.getHomeTimeline(new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONArray jsonArray) {				
				clearAdapter();
				clearDB();				
				addAll(Tweet.fromJSONArray(jsonArray,""));
				
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				// dipu Toast.makeText(getActivity(), "Connection error..loading from database", Toast.LENGTH_SHORT).show();
				populateHomeLineFromDB();
			}
			
			@Override
			public void onFinish() {
				super.onFinish();
				clearProgressBar();
			}
		});
	}

}
