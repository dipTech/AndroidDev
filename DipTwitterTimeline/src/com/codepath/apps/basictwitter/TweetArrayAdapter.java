package com.codepath.apps.basictwitter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.basictwitter.models.Tweet;
import com.nostra13.universalimageloader.core.ImageLoader;

public class TweetArrayAdapter extends ArrayAdapter<Tweet> {
	private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.ENGLISH);
	TextView tvRelativeTime;
	
	public TweetArrayAdapter(Context context, List<Tweet> tweets) {
		super(context, 0, tweets);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tweet tweet = getItem(position);
		View v;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			v = inflator.inflate(R.layout.tweet_item, parent, false);
		} else {
			v = convertView;
		}
		ImageView ivProfileImage = (ImageView) v.findViewById(R.id.ivProfileImage);
		TextView tvUserScreenName = (TextView) v.findViewById(R.id.tvUserScreenName);
		TextView tvBody = (TextView) v.findViewById(R.id.tvBody);
		tvRelativeTime = (TextView) v.findViewById(R.id.tvRelativeTime);
		TextView tvUserName = (TextView) v.findViewById(R.id.tvUserName);
		ivProfileImage.setImageResource(android.R.color.transparent);
		
		// Populate view with tweet data
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(tweet.getUser().getProfileImageUrl(), ivProfileImage);
		tvUserScreenName.setText("@"+tweet.getUser().getScreenName());
		tvUserName.setText(tweet.getUser().getName() + "  ");
		tvBody.setText(tweet.getBody());
		//uncomment getRelativeTime(tweet.getCreatedAt());
		
		return v;
	}

	private void getRelativeTime(Date createdAt) {
		//try {
			long currentTime = System.currentTimeMillis();
			long diff = currentTime - createdAt.getTime();
			if (diff > 0) {
				int time = (int) (diff / 1000);
				String t = simpleDateFormat.format(createdAt);

				if (time < 60) {
					t = String.valueOf(time) + "s";
				} else if (time < 60 * 60) {
					t = String.valueOf(time / 60) + "m";
				} else if (time < 60 * 60 * 24) {
					t = String.valueOf(time / 3600) + "h";
				} else if (time < 60 * 60 * 24 * 10) {
					t = String.valueOf(time / (3600 * 24)) + "d";
				} else {
					long millis = createdAt.getTime();
					t = DateUtils.getRelativeTimeSpanString(millis,
							System.currentTimeMillis(),
							DateUtils.SECOND_IN_MILLIS).toString();
				}
				tvRelativeTime.setText(t);
			} else {
				tvRelativeTime.setText("Now");
			}

		//} catch (ParseException e) {
			//Log.d("twit", e.getMessage());
		//}
	}
	
}
