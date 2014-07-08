package com.codepath.apps.basictwitter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.activeandroid.util.Log;
//import com.codepath.apps.basictwitter.db.DatabaseHandler; // using ORM for now
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimelineActivity extends Activity {
	private TwitterClient client;
	private ArrayList<Tweet> tweets;
	private ArrayAdapter<Tweet> aTweets;
	private PullToRefreshListView lvTweets;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		client = TwitterApplication.getRestClient();
		
		ActionBar abTimeLine = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00ABF0"));     
        abTimeLine.setBackgroundDrawable(colorDrawable);
		// dipankar db = new DatabaseHandler(this);

		setupViews();
		populateTimeline();
	}

	private void setupViews() {
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		tweets = new ArrayList<Tweet>();
		aTweets = new TweetArrayAdapter(this, tweets);
		lvTweets.setAdapter(aTweets);
		
		// Attach the listener to the AdapterView onCreate
		lvTweets.setOnScrollListener(new EndlessScrollListener() {

			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				if (aTweets.getCount() == 0) return;
				populateTimelineMaxId(totalItemsCount);
			}
		});
		
		// Set a listener to be invoked when the list should be refreshed.
        lvTweets.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list contents
                // Make sure you call listView.onRefreshComplete()
                // once the loading is done. This can be done from here or any
                // place such as when the network request has completed successfully.
            	populateTimelineMaxId(aTweets.getItem(0).getUid());
            }
            
        });
        
		lvTweets.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View parent, int position, long arg3) {
				Intent i = new Intent(getApplicationContext(), TweetAutoLink.class);
				startActivity(i);
			}
			
		});		

	}

	public void populateTimeline() {
		client.getHomeTimeline(new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(TimelineActivity.this, "Network Connection Error\nLoading from database schema....", Toast.LENGTH_SHORT).show();
				dbFetch();
			}

			@Override
			public void onSuccess(JSONArray json) {
				aTweets.addAll(Tweet.fromJSONArray(json, true));				
				// dipankar db.addAllTweet(tweets);
			}
		});
	}
	
	private void dbFetch() {
		aTweets.addAll(Tweet.findAll());
	}
	
	public void populateTimelineMaxId(long offset) {
		client.getHomeTimelineMaxId(offset, new JsonHttpResponseHandler() {

			@Override
			public void onFailure(Throwable e, String s) {
				Log.d("debug", e.toString());
				Log.d("debug", s.toString());
			}

			@Override
			public void onSuccess(JSONArray json) {
				List<Tweet> sinceTweet = Tweet.fromJSONArray(json, true);
				tweets.addAll(0, sinceTweet);
				aTweets.notifyDataSetChanged();
			}
		});
	}
		
	@Override
	  public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.timeline, menu);
	    return true;
	  } 
	
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	         if (item.getItemId() == R.id.tweet_timeline) {
	                 showNewTwitDialoge();
	         }
	         return super.onOptionsItemSelected(item);
	 }
	 
	public void showNewTwitDialoge() {
		LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
		View popupView = layoutInflater.inflate(R.layout.new_tweet, null);
		final PopupWindow popupWindow = new PopupWindow(popupView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		
		Button btnSubmitTweet = (Button) popupView.findViewById(R.id.btnSubmitTweet);
		final EditText text_entered = (EditText) popupView.findViewById(R.id.etNewTweet);
		
		btnSubmitTweet.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				if ((text_entered != null) && (text_entered.getText().length() != 0)) {
					String text_value = text_entered.getText().toString();
					
					client.postTweet(text_value, new JsonHttpResponseHandler() {
			            public void onSuccess(JSONArray json) {
			            	populateTimelineMaxId(aTweets.getItem(0).getUid());
			            }
			            
			            public void onFailure(Throwable e) {
			                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
			            }
			            
						public void onFinish() {
							lvTweets.onRefreshComplete();
							super.onFinish();
						}
			        });					
				}
				
				popupWindow.dismiss();
			}
		});

		Button btnCancel = (Button) popupView.findViewById(R.id.btnTweetCancel);
		btnCancel.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});

		popupWindow.setOutsideTouchable(false);
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);

		popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);
	}

}
