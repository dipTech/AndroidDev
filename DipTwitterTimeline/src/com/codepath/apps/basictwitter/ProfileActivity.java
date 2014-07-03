package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.basictwitter.util.TweetUtility;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends FragmentActivity {
	Context context;
	private RelativeLayout rlHeader;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		setContentView(R.layout.activity_profile);
		loadProfileInfo();
	}

	
	private void loadProfileInfo() {
		TwitterApplication.getRestClient().getUser(new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable e, String s) {
				Toast.makeText(ProfileActivity.this, "Error getting user", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(JSONObject json) {
				User u = User.fromJSON(json);				
				getActionBar().setTitle("@" + u.getScreenName() + " Profile");
				populateProfileHeader(u);
			}

			private void populateProfileHeader(User user) {
				TextView tvName = (TextView) findViewById(R.id.tvName);
				TextView tvScreenName = (TextView) findViewById(R.id.tvUserScreenName);
				TextView tvDescription = (TextView) findViewById(R.id.tvDescription);
				TextView tvTweets = (TextView) findViewById(R.id.tvTweets);
				TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
				TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
				ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

				tvName.setText(user.getName());
				String x = user.getScreenName();
				if (x != null) {
					tvScreenName.setText(user.getScreenName());
				}
				if (user.getDescription() != null) {
					tvDescription.setText(user.getDescription());
				}

				tvTweets.setText(TweetUtility.formatForDisplay(user.getStatuses_count()));
				tvFollowers.setText(TweetUtility.formatForDisplay(user.getFollowers_count()));
				tvFollowing.setText(TweetUtility.formatForDisplay(user.getFriends_count()));
				//if(user.getProfileBannerUrl() != null && !user.getProfileBannerUrl().equals("")) {
					ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
				//}
				//Picasso.with(context).load(user.getProfileImageUrl()).into(ivProfileImage);
			}
		});		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
