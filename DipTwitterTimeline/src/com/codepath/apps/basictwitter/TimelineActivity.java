package com.codepath.apps.basictwitter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.codepath.apps.basictwitter.fragments.HomeTimelineFragment;
import com.codepath.apps.basictwitter.fragments.MentionsTimelineFragment;
import com.codepath.apps.basictwitter.listeners.FragmentTabListener;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.util.TweetUtility;

//import com.codepath.apps.basictwitter.db.DatabaseHandler; // using ORM for now

public class TimelineActivity extends FragmentActivity implements TabListener {
	private Context context;
	private HomeTimelineFragment homeTimelineFragment;
	private MentionsTimelineFragment mentionsTimelineFragment;
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context = this;
		
		actionBar = getActionBar();
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00ABF0"));     
        actionBar.setBackgroundDrawable(colorDrawable);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timeline);
		
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

				
/*	dipu	ActionBar abTimeLine = getActionBar(); 
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#00ABF0"));     
        abTimeLine.setBackgroundDrawable(colorDrawable);
*/        
		setupTabs();
	}

	public void onProfileView(MenuItem m) {
		Intent iProfileActivity = new Intent(this, ProfileActivity.class);
		startActivity(iProfileActivity);
	}
	
	private void setupTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tab1 = actionBar
			.newTab()
			.setText("Home")
			.setIcon(R.drawable.ic_home)
			.setTag("HomeTimelineFragment")
			.setTabListener(
				new FragmentTabListener<HomeTimelineFragment>(R.id.flContainer, this, "home", HomeTimelineFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		Tab tab2 = actionBar
			.newTab()
			.setText("Mentions")
			.setIcon(R.drawable.ic_mentions)
			.setTag("MentionsTimelineFragment")
			.setTabListener(
			    new FragmentTabListener<MentionsTimelineFragment>(R.id.flContainer, this, "mentions", MentionsTimelineFragment.class));

		actionBar.addTab(tab2);
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		
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
	                 showNewTweetDialoge();
	         }
	         return super.onOptionsItemSelected(item);
	 }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TweetUtility.TWEET_REQUEST_CODE && resultCode == RESULT_OK) {
			String openTab = getActionBar().getSelectedTab().getTag()
					.toString();
			if (openTab.equals("HomeTimelineFragment")) {
				homeTimelineFragment = (HomeTimelineFragment) getSupportFragmentManager()
						.findFragmentById(R.id.flContainer);
				homeTimelineFragment.fetchHomeTimeLine(1, 0);
			} else {
				mentionsTimelineFragment = (MentionsTimelineFragment) getSupportFragmentManager()
						.findFragmentById(R.id.flContainer);
				mentionsTimelineFragment.fetchMentionsTimeLine(1, 0);
			}
		}
	}
	
	private void showNewTweetDialoge() {
		if(TweetUtility.isNetworkAvailable(this)) {
			Intent i = new Intent(context, AddTweet.class);
			startActivityForResult(i, TweetUtility.TWEET_REQUEST_CODE);
		} else {
			Toast.makeText(context, "@string/no_network_connection", Toast.LENGTH_SHORT).show();
		}
	}

	public void finishToActivity() {
		Fragment currentFragment = getSupportFragmentManager().findFragmentByTag("Home");
		if(currentFragment!=null && currentFragment instanceof HomeTimelineFragment){
			((HomeTimelineFragment)currentFragment).populateHomeTimeLineSinceLatest();
		}else{
			Log.d("twit", "something");
		}
	}
	
	public void refresh(){
		String tabTag = (String)actionBar.getTabAt(actionBar.getSelectedNavigationIndex()).getTag();
		Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(tabTag);
		if(currentFragment!=null && currentFragment instanceof HomeTimelineFragment){
			((HomeTimelineFragment)currentFragment).populateTimeLine();
		}else if(currentFragment!=null && currentFragment instanceof MentionsTimelineFragment) {
			((MentionsTimelineFragment)currentFragment).populateTimeLine();
			
		}else{
			Log.d("twit", "something");
		}
	}
}
