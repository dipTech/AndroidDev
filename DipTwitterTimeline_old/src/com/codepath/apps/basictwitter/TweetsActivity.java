package com.codepath.apps.basictwitter;

import org.json.JSONArray;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.activeandroid.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TweetsActivity extends FragmentActivity implements TabListener {
	private TwitterClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tweets);
		setupNavigationTabs();
	}

	
	private void setupNavigationTabs() {
		ActionBar actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00ABF0"));     
        actionBar.setBackgroundDrawable(colorDrawable);

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		
		Tab tabHome = actionBar.newTab().setText("Home").setTag("HomeTimelineFragment").setIcon(R.drawable.ic_home).setTabListener(this);
		Tab tabMentions = actionBar.newTab().setText("Mentions").setTag("MentionsTimelineFragment").setIcon(R.drawable.ic_four_square).setTabListener(this);
		
		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
	}

	public void onProfileView(MenuItem m) {
		Intent iProfileActivity = new Intent(this, ProfileActivity.class);
		startActivity(iProfileActivity);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabUnselected(Tab tb, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tweets, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
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
// dipu			            	populateTimelineMaxId(aTweets.getItem(0).getUid());
			            }
			            
			            public void onFailure(Throwable e) {
			                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
			            }
			            
						public void onFinish() {
// dipu							lvTweets.onRefreshComplete();
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
