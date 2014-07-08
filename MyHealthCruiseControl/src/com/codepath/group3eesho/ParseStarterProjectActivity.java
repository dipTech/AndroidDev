package com.codepath.group3eesho;

import android.app.Activity;
import android.os.Bundle;

import com.codepath.group3eesho.R;
import com.parse.ParseAnalytics;
import com.parse.ParseObject;


public class ParseStarterProjectActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setParseModel();
		setContentView(R.layout.main);

		ParseAnalytics.trackAppOpened(getIntent());
	}

	private void setParseModel() {
		ParseObject testObject = new ParseObject("TestObject");
		testObject.put("Name", "Dipankar");
		testObject.saveInBackground();		
	}
}
