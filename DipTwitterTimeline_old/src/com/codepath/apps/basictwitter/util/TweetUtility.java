package com.codepath.apps.basictwitter.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.models.User;


public class TweetUtility extends Activity { // extends Activity to get the Connectivity Manager's System Service

	private static final String TWITTER = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
	public static final boolean DEBUG = false;
	public static User defaultUser; // default user of the application
	public static int TWEET_REQUEST_CODE = 11;

	public static void showSoftKeyboard(View view,Activity activity){
	    if(view.requestFocus()){
	        InputMethodManager imm =(InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
	        imm.showSoftInput(view,InputMethodManager.SHOW_IMPLICIT);
	    }
	}
	
	public boolean networkAvailable() {
	    ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
	    if (networkInfo == null) {
	    	return false;
	    }
	    else if (networkInfo.isConnectedOrConnecting())  {
	    		return true;
	    }
	    else {
		    return false;
	    }
	}
	
	public static void hideSoftKeyboard(View view,Activity activity){
		  InputMethodManager imm =(InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		  imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}
	
	public static void hideProgressBar(Activity activity) {
		setProgressBarVisibility(activity, false); 
    }
	public static void showProgressBar(Activity activity) {
     setProgressBarVisibility(activity, true);   
    }
	
	private String getCountString(long count) {
		StringBuilder countStr=new StringBuilder();
		float val=0.0f;
		if(count > 999) {
			val=count/1000.00f;
		}
		return countStr.toString();
	}
	/*
	public static void showNetworkUnavailable(Activity activity) {
		 LayoutInflater inflater = activity.getLayoutInflater();
		    View view = inflater.inflate(R.layout.network_not_available,
		                                   (ViewGroup) activity.findViewById(R.id.nwunavailable));
		    Toast toast=new Toast(activity);
		    toast.setView(view);
		    toast.setDuration(Toast.LENGTH_LONG);
		    toast.show();

	}
	*/
	
	private static void setProgressBarVisibility(Activity activity,boolean show) {
		activity.setProgressBarIndeterminate(show);
	}
	
	public static Boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivityManager 
        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		
		boolean isNetworkAvailable=true;
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    isNetworkAvailable=( activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting());
	    return isNetworkAvailable;
	}
	

	public static long getMaxSinceId(ArrayList<Tweet> tweets) {
		long maxSinceId = 1L;
		long tweetUid = 0L;
		for (Tweet tweet : tweets) {
			tweetUid = tweet.getUid();
			if (tweetUid > maxSinceId) {
				maxSinceId = tweet.getUid();
			}
		}
		return maxSinceId;
	}

	public static String formatForDisplay(long count) {
	    if (count < 1000) return "" + count;
	    int exp = (int) (Math.log(count) / Math.log(1000));
	    return String.format("%.1f %c", count / Math.pow(1000, exp), "KMGTPE".charAt(exp-1));
	}

	public static Date getTwitterDate(String date) {
		Date dt=new Date();
		SimpleDateFormat sf = new SimpleDateFormat(TWITTER);
		sf.setLenient(true);
		try {
			dt= sf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dt;
	}

}
