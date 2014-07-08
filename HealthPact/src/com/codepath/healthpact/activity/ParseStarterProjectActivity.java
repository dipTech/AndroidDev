package com.codepath.healthpact.activity;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.codepath.healthpact.R;
import com.codepath.healthpact.models.AppUser;
import com.parse.FindCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


public class ParseStarterProjectActivity extends Activity {
	
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		getCurrentUser();
		setParseModel();
		setContentView(R.layout.activity_login);

		ParseAnalytics.trackAppOpened(getIntent());
		
		//insertData();
		//queryData();
	}

	private void init() {
	}

	private void setParseModel() {
		//ParseObject testObject = new ParseObject("TestObject");
		//ParseObject appUser = new ParseObject("AppUser");
		ParseObject.create("AppUser");
		//testObject.put("Name", "Dipankar");
		//testObject.saveInBackground();
		//appUser.put("name", "Aishik");
		//appUser.saveInBackground();
	}
	
	private void getCurrentUser() {
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			Toast.makeText(getApplicationContext(), "Got current User", Toast.LENGTH_SHORT).show();
		} else {
		  // show the signup or login screen
		}
	}
	
	public void insertData() {
		AppUser appUser = new AppUser("dipankar");
		// Set the current user, assuming a user is signed in
		appUser.setOwner(ParseUser.getCurrentUser());
		// Immediately save the data asynchronously
		appUser.saveInBackground();
		// or for a more robust offline save
		// todoItem.saveEventually();
	}

}
