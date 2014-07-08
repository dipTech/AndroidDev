package com.codepath.group3eesho;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

import android.app.Application;

public class ParseApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "X2cwEBc9lxhjpVmKSB8XgaDvFZeQ7zZ2vFMfFtGb", "qq94BwV02ZevJqOyry8sDzZQrhUIgjfHAq4S77QE");

		ParseUser.enableAutomaticUser();
		ParseACL defaultACL = new ParseACL();
	    
		// If you would like all objects to be private by default, remove this line.
		defaultACL.setPublicReadAccess(true);
		
		ParseACL.setDefaultACL(defaultACL, true);
	}

}
