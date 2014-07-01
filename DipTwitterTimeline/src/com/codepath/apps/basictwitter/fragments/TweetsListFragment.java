package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TweetArrayAdapter;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;

public class TweetsListFragment extends Fragment {
	protected TwitterClient client;

	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private ListView lvTweets;

	private long oldestTweetId;
	private long latestTweetId;
	
	private User myUser;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nonViewInit();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// inflate the layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		// setup
		setupViews(v);
		// populate from database schema
		fetchDBSchema();
		// return the layout view		
		return v;
	}
	
	private void fetchDBSchema() {
		aTweets.clear();			
		List<Tweet> listTweetsFromSchema = (List<Tweet>) Tweet.findAll();
		aTweets.addAll(listTweetsFromSchema);		
	}

	// return the adapter to the activity
	public TweetArrayAdapter getAdapter() {
		return (TweetArrayAdapter) aTweets;
	}
	
	// Delegate the add to internal adapter
	public void addAll(List<Tweet> tweets) {
		aTweets.addAll(tweets);
	}
	
	private void nonViewInit() {
		oldestTweetId = 1;
		latestTweetId = 1;

		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);
	}
	
	private void setupViews(View v) {
		lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		((ListView) lvTweets).setAdapter(aTweets);
	}

	public void showToast(String msg) {
		Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

}
