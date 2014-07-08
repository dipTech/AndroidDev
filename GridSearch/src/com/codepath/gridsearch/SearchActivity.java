package com.codepath.gridsearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

/** Main starting Activity class
 * @author Dipankar
 * @version 1
 */
public class SearchActivity extends ActionBarActivity {
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	private ArrayList<String> customizedSettings;
	final private String filename = "customizedSettings.txt";
	int scrollCounter = 0;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setupView();
	}

	private void setupView() {
    	etQuery = (EditText) findViewById(R.id.etQuery);
    	gvResults = (GridView) findViewById(R.id.gvResults);
    	btnSearch = (Button) findViewById(R.id.btnSearch);

//		MenuItem menuItem = (MenuItem)findViewById(R.id.action_settings);
//		menuItem.setIcon(getResources().getDrawable(R.drawable.ic_action_settings));
		// enables the activity icon as a 'home' button. required if "android:targetSdkVersion" > 14
		this.getActionBar().setHomeButtonEnabled(true);
		
        imageAdapter = new ImageResultArrayAdapter(this, imageResults);
        gvResults.setAdapter(imageAdapter);
        
        gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long rowid) {
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				i.putExtra("url", imageResult);
				startActivity(i);				
			}
        	
        });
  
        // Attach the listener to the AdapterView onCreate
        gvResults.setOnScrollListener(new EndlessScrollListener() {
	    @Override
	    public void onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView
	        customLoadMoreDataFromApi(totalItemsCount); 
                // or customLoadMoreDataFromApi(page); 
	    }
        });
        readItems();
	}
	
	// Append more data into the adapter
    public void customLoadMoreDataFromApi(int offset) {
      // This method probably sends out a network request and appends new data items to your adapter. 
      // Use the offset value and add it as a parameter to your API request to retrieve paginated data.
      // Deserialize API response and then construct new objects to append to the adapter
    	getDataForURI(offset);
    }
	private void getDataForURI(int offset) {
		triggerSearch(offset);
	}

	private void triggerSearch(int offset) {
    	final String query = etQuery.getText().toString();
    	AsyncHttpClient client = new AsyncHttpClient();
    	String url = setupURIParam(offset) + "&q="+ Uri.encode(query);
        
        client.get(url, new JsonHttpResponseHandler(){
    		public void onSuccess(JSONObject response) {
    			JSONArray imageJSONResult = null;
    			try {
    				imageJSONResult = response.getJSONObject("responseData").getJSONArray("results");
    				imageAdapter.addAll(ImageResult.fromJSONArray(imageJSONResult) );
    				Log.d("DEBUG", imageResults.toString());
    			}
    			catch (JSONException jse) {
    				jse.printStackTrace();
    			}
    		}
    		
    		@Override
    	    public void onFailure(Throwable t, String error)
    	    {
    	    	Toast.makeText(getApplicationContext(), "Search for " + query + " failed with ERROR[" + error + "]", Toast.LENGTH_LONG).show();;
    	    }
    	});
		
	}

	public void setupSettings() {
		Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
		startActivity(i);				
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
	    case android.R.id.home:
	    	Toast.makeText(this, "Click the gear icon on the action bar for settings", Toast.LENGTH_LONG).show();
	    	break;
		case R.id.action_settings:
			setupSettings();
			break;
		}

		
		return super.onOptionsItemSelected(item);
	}
    public void onImageSearch(View v) {
    	scrollCounter = 0;
		imageResults.clear();
		imageAdapter.clear();
    	triggerSearch(0);
    	//gvResults.smoothScrollByOffset(1);
    }

    private String setupURIParam(int startPosition) {
        readItems();

        return 
        		"https://ajax.googleapis.com/ajax/services/search/images?&v=1.0&rsz=8&start=" +
        		startPosition + 
        		"&imgsz=" + Uri.encode(customizedSettings.get(0).toString()) +
        		"&imgc=" + Uri.encode(customizedSettings.get(1).toString())  +
        		"&imgtype=" + Uri.encode(customizedSettings.get(2).toString()) +
        		"&as_sitesearch=" + Uri.encode(customizedSettings.get(3).toString()) 

        		;
    }
    
 	protected void readItems() {
 		File filesDir = getFilesDir();
 		File todoFile = new File(filesDir, filename);
 		try {
 			customizedSettings = new ArrayList<String>(FileUtils.readLines(todoFile));
 		}
 		catch (IOException ex) {
 			customizedSettings = new ArrayList<String>();
 			ex.printStackTrace();
 		}
 	}
}