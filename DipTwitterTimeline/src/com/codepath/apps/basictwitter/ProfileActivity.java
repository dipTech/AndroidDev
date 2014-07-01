package com.codepath.apps.basictwitter;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				getActionBar().setTitle("@" + u.getScreenName());
				populateProfileHeader(u);
			}

			private void populateProfileHeader(User user) {
				TextView tvName = (TextView) findViewById(R.id.tvName);
				TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
				TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
				TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
				ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

				tvName.setText(user.getName());
				// dipu tvTagline.setText(user.getTagline);
				// dipu tvFollowers.setText(user.getFollowersCount() + " Followers");
				// dipu tvFollowing.setText(user.getFollowingCount() + " Following");
				ImageLoader.getInstance().displayImage(user.getProfileImageUrl(), ivProfileImage);
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
