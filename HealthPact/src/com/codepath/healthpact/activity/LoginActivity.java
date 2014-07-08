package com.codepath.healthpact.activity;

import java.util.List;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.codepath.healthpact.R;
import com.codepath.healthpact.app.HealthPactApp;
import com.codepath.healthpact.models.AppUser;
import com.codepath.healthpact.restclient.RestClient;
import com.codepath.oauth.OAuthLoginActivity;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class LoginActivity extends OAuthLoginActivity<RestClient> {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
	}

	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	// OAuth authenticated successfully, launch primary authenticated activity
	// i.e Display application "homepage"
    @Override
    public void onLoginSuccess() {
    	// Intent i = new Intent(this, PhotosActivity.class);
    	// startActivity(i);
    }
    
    // OAuth authentication flow failed, handle the error
    // i.e Display an error dialog or toast
    @Override
    public void onLoginFailure(Exception e) {
        e.printStackTrace();
    }
    
    // Click handler method for the button used to start OAuth flow
    // Uses the client to initiate OAuth authorization
    // This should be tied to a button used to login
    public void loginToRest(View view) {
        getClient().connect();
    }

	public void parseLogin(View view) {
		String strUser = HealthPactApp.strUser;
		String strPwd = HealthPactApp.strPwd;
		
		ParseUser.logInInBackground(strUser, strPwd, new LogInCallback() {

					public void done(ParseUser user, ParseException e) {
						if (user != null) {
							Toast.makeText(getApplicationContext(), "Logged in successful!", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(getApplicationContext(), "Logged failed", Toast.LENGTH_SHORT).show();
						}
					}
				});
	}
	
	public void queryData(View v) {
		// Define the class we would like to query
		ParseQuery<AppUser> query = ParseQuery.getQuery(AppUser.class);
		query.whereEqualTo("name", "Test");
		query.findInBackground(new FindCallback<AppUser>() {

			@Override
			public void done(List<AppUser> results, ParseException e) {
				for (AppUser u : results) {
					Toast.makeText(getApplicationContext(), u.getExpertise() +  "-" + u.getDescription(), Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}

}
