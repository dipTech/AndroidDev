package com.codepath.apps.basictwitter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.basictwitter.models.User;
import com.codepath.apps.basictwitter.TweetDetailActivity;
import com.codepath.apps.basictwitter.models.Tweet;
import com.codepath.apps.basictwitter.TwitterClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class AddTweet extends Activity {
	private EditText etNewMessage;
	private TextView tvChars;
	private int MAX_CHARS = 140;
	private TwitterClient client;
	private Context context;
	
	private ImageView ivPic;
	private TextView tvName;
	private TextView tvScreenName;
	private User currentUser;
	
	private ImageButton ibGallery;
	private ImageButton ibCamera;
	private ImageButton ibLocation;
	private ImageView ivTwtImage;
	
	private byte[] image;
	private Bitmap photo;
	
	private boolean isReplyAction;
	private int RESULT_LOAD_IMAGE = 123;
	private static final int PICTURE_RESULT = 124;
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_tweet);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		context = this;
		isReplyAction = checkIsInitiatedFromReply();
		//ibPost = (ImageButton) findViewById(R.id.ibPost);
		etNewMessage = (EditText) findViewById(R.id.etNewMessage);
		tvChars = (TextView) findViewById(R.id.tvChars);
		
		ivPic = (ImageView) findViewById(R.id.ivUserImage);
		tvName = (TextView) findViewById(R.id.tvTwtName);
		tvScreenName = (TextView) findViewById(R.id.tvScreenName);
		ivTwtImage = (ImageView) findViewById(R.id.ivTwtImage);
		
		if(isReplyAction){
			String replyScreen = getIntent().getStringExtra("screen_name");
			etNewMessage.setText(replyScreen);
			etNewMessage.setSelection(etNewMessage.getText().length());
		}
		
		etNewMessage.requestFocus();
        getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		verifyCredentials();
		setCharsText();
		setUpStatusListener();
		ivTwtImage.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				ivTwtImage.setImageDrawable(null);
				return false;
			}
		});
		//setUpPostButtonListener();
	}
	
	private void setImageToView(){
		ivTwtImage.setImageBitmap(photo);
		ivTwtImage.invalidate();
	}

	private boolean checkIsInitiatedFromReply() {
		String purpose = getIntent().getStringExtra("purpose");
		if(purpose != null && purpose.equals("reply")){
			return true;
		}
		return false;
	}

	private void setUpProfileInfo() {
		tvName.setText(currentUser.getName());
		tvScreenName.setText(currentUser.getScreenName());		
	}

	private void setCharsText() {
		int rem = MAX_CHARS - etNewMessage.getText().toString().length();
		String limit = rem + " characters remaining";
		tvChars.setText(limit);
		//miChars.setTitle(""+charsRem);
		
	}

	private void setUpStatusListener() {
		etNewMessage.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				setCharsText();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_tweet, menu);
		//miChars = (MenuItem) findViewById(R.id.as_chars);
		//setCharsTextToActionBar(MAX_CHARS);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId() == android.R.id.home){
			setResult(RESULT_OK);
            this.finish();
            return true;
		}else if(item.getItemId() == R.id.add_tweet){
			tweet();
			return true;
		}else{
			return super.onOptionsItemSelected(item);
		}
    }
	
	private void tweet() {
		String st = etNewMessage.getText().toString();
		if(st == null || st.length() == 0 || st.equals("")){
			Toast.makeText(AddTweet.this, "Write something to post", Toast.LENGTH_LONG).show();
		}else if(st.length() > 140){
			Toast.makeText(AddTweet.this, "140 chars only",	Toast.LENGTH_LONG).show();
		}else{
			postTweet(st);
		}
		
	}

	private void postTweet(String status){
		client = new TwitterClient(context);
		String inReplyTo = null;
		if(isReplyAction){
			inReplyTo = String.valueOf(getIntent().getLongExtra("id", 0));
		}
		client.postTweet(status, inReplyTo, getBAISForPhoto(),new JsonHttpResponseHandler() { 
		@Override
	            public void onSuccess(JSONObject body) {
				finishActivity();
					//ComposeTweetActivity.this.finish();
					super.onSuccess(body);
		}
		public void onFailure(Throwable e, JSONObject error) {
		    // Handle the failure and alert the user to retry
		   Toast.makeText(context, "Excepton : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		  }
		});	
	}
	
	private ByteArrayInputStream getBAISForPhoto(){
		if(photo == null){
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        // then flip the stream
        byte[] myTwitterUploadBytes = baos.toByteArray();
        ByteArrayInputStream bis = new ByteArrayInputStream(myTwitterUploadBytes);
        return bis;
	}
	
	private void finishActivity(){
		setResult(RESULT_OK);
        this.finish();
	}
	
	private void verifyCredentials() {
		client = new TwitterClient(context);
		/* dipu
		client.verifyCredentials(new JsonHttpResponseHandler() { 
		@Override
	            public void onSuccess(int code, JSONObject json) {
            currentUser = User.fromJson(json);
            setUpProfileInfo();
		}
		public void onFailure(Throwable e, JSONObject error) {
		    // Handle the failure and alert the user to retry
		   Toast.makeText(context, "Excepton : "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
		  }
		});	
		*/
	}
}
