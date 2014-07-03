package com.codepath.apps.basictwitter.fragments;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.basictwitter.EndlessScrollListener;
import com.codepath.apps.basictwitter.R;
import com.codepath.apps.basictwitter.TweetArrayAdapter;
import com.codepath.apps.basictwitter.TweetClickDialog;
import com.codepath.apps.basictwitter.TweetDetailActivity;
import com.codepath.apps.basictwitter.TwitterClient;
import com.codepath.apps.basictwitter.TwitterFragmentApp;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;


public class TweetsListFragment extends Fragment {
	protected TwitterClient client;
	protected Context context;
	private ProgressBar progressBar;

	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	PullToRefreshListView lvTweets;

	private long oldestTweetId;
	private long latestTweetId;
	
	private User myUser;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		nonViewInit();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		context = getActivity();
		client = new TwitterClient(context);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// inflate the layout
		View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
		// setup
		setupViews(v);
		progressBar = (ProgressBar)v.findViewById(R.id.pbLoading);
		//dipu disabled for now showProgressBar();
		// populate from database schema
		fetchDBSchema();
		addListener(v);
		// return the layout view		
		return v;
	}
	
	private void addListener(View v) {
		lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
		
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //Populate since latest tweet
            	populateTimeLineSinceLatest(String.valueOf(aTweets.getItem(0).getUid()+10));
            }
        });
		
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				Intent i = new Intent(getActivity(), TweetDetailActivity.class);
				Tweet result = tweets.get(position);
				i.putExtra("tweet", result);
				i.putExtra("pos", String.valueOf(position));
				startActivityForResult(i,20);
			}			
		});
	}
	
	public void loadMore(final String maxId){
		client.getHomeTimelineMaxId(Long.valueOf(maxId), new JsonHttpResponseHandler(){

			@Override
			public void onSuccess(JSONArray jsonArray) {
				addAll(Tweet.fromJSONArray(jsonArray, maxId));						
			}

			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(getActivity(), "Tweet Load Failed", Toast.LENGTH_SHORT).show();
			}
		});
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
		aTweets.clear();
		//Toast.makeText(this.getActivity(), "cnt: "+tweets.size(), Toast.LENGTH_SHORT).show();
		aTweets.addAll(tweets);
	}
	
	private void nonViewInit() {
		oldestTweetId = 1;
		latestTweetId = 1;

		client = TwitterFragmentApp.getRestClient();
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(getActivity(), tweets);
	}
	
	private void setupViews(View v) {
		lvTweets = (PullToRefreshListView) v.findViewById(R.id.lvTweets);
		lvTweets.setAdapter(aTweets);
	}

	public void showToast(String msg) {
		Toast.makeText(this.getActivity(), msg, Toast.LENGTH_SHORT).show();
	}

	protected void jsonHandlerSuccessFunction(int page, JSONArray body){
		if(page <= 1){
			getAdapter().clear();
			getAdapter().notifyDataSetInvalidated();			
		}
        JSONArray items = null;
		items = body;
		// Parse json array into array of model objects
		ArrayList<Tweet> tweets = Tweet.fromJSONArray(items);
		// Load model objects into the adapter
		for (Tweet tweet : tweets) {
		   getAdapter().add(tweet);
		   tweet.save();
		}
		lvTweets.onRefreshComplete();
        getActivity().setProgressBarIndeterminateVisibility(false); 
	}
	
	protected void jsonHandlerFailureFunction(String err){
		if(err.trim().equals("Client Error (429)")){
			err = "Rate limiting, try later";
		}
	   // dipu check this Toast.makeText(context, "Exception : "+err, Toast.LENGTH_SHORT).show();
       getActivity().setProgressBarIndeterminateVisibility(false); 
	}
	
	public void showProgressBar(){
		progressBar.setVisibility(ProgressBar.VISIBLE);
	}
	
	public void clearProgressBar(){
		progressBar.setVisibility(ProgressBar.INVISIBLE);
	}
	
	public void populateTimeLineSinceLatest(String uid){
		client.getHomeTimelineSinceId(Long.valueOf(uid), new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				ArrayList<Tweet> latestT = Tweet.fromJSONArray(jsonArray);
				addLatestTweets(latestT);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(getActivity(), "Load Tweet Failed...", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFinish() {
				lvTweets.onRefreshComplete();
				super.onFinish();
			}
		});
	}
	
	public void populateHomeTimeLineSinceLatest(){
		client.getHomeTimelineSinceId(getLatestTweetId(), new JsonHttpResponseHandler(){
			
			@Override
			public void onSuccess(JSONArray jsonArray) {
				ArrayList<Tweet> latestT = Tweet.fromJSONArray(jsonArray);
				addLatestTweets(latestT);
			}
			
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(getActivity(), "Load Tweet Failed...", Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onFinish() {
				lvTweets.onRefreshComplete();
				super.onFinish();
			}
		});
	}
	
	public Long getLatestTweetId(){
		return aTweets.getItem(0).getUid();
	}
	
	public void clearAdapter(){
		aTweets.clear();
	}

	public void addLatestTweets(ArrayList<Tweet> tweets){		
		this.tweets.addAll(0, tweets);
		aTweets.notifyDataSetChanged();
	}
	
	public void populateHomeLineFromDB(){
		List<Tweet> tweets = Tweet.findAll();
		for(Tweet t : tweets){
			long userId = t.getUserId();
			User u = User.findById(userId).get(0);
			t.setUser(u);
		}
		aTweets.addAll(tweets);
	}
	public void clearDB() {
		Tweet.deleteAllTweets();
		User.deleteAllUsers();
	}
}
