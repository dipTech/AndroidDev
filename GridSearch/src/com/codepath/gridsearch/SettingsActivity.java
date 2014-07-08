package com.codepath.gridsearch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.apache.commons.io.FileUtils;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/** Class for advanced settings
 * @author Dipankar
 * @version 1
 *
 */
public class SettingsActivity extends ActionBarActivity {
	private ArrayList<String> customizedSettings;
	final private String filename = "customizedSettings.txt";
	private Spinner imagesize;
	private Spinner colorfilter;
	private Spinner imagetype;
	private EditText sitefilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		setupViews();
	}

	private void setupViews() {
		customizedSettings = new ArrayList<String>();

		imagesize = (Spinner) findViewById(R.id.imgSizeSpinner);
		colorfilter = (Spinner) findViewById(R.id.imgColorSpinner);
		imagetype = (Spinner) findViewById(R.id.imagetype);
		sitefilter = (EditText) findViewById(R.id.etSiteFilter);
		
		readItems();
	}

	/* commented for now
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
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
	commented for now */

	public void backToSearch(View v) {
		writeItems();
		finish();
	}
	
	public void cancelSettings(View v) {
		finish();
	}

	private void writeItems() {
 		File filesDir = getFilesDir();
 		File todoFile = new File(filesDir, filename);
 		try {
 			customizedSettings.clear();
 			customizedSettings.add(String.valueOf(imagesize.getSelectedItem()));
 			customizedSettings.add(String.valueOf(colorfilter.getSelectedItem()));
 			customizedSettings.add(String.valueOf(imagetype.getSelectedItem()));
 			customizedSettings.add(sitefilter.getText().toString());
 			FileUtils.writeLines(todoFile, customizedSettings);
	    	//Toast.makeText(getApplicationContext(), customizedSettings.toString(), Toast.LENGTH_LONG).show();;
 		}
 		catch (IOException ex) {
 			ex.printStackTrace();
 		}
 	}
	
 	private void readItems() {
 		File filesDir = getFilesDir();
 		File todoFile = new File(filesDir, filename);
 		try {
 			customizedSettings = new ArrayList<String>(FileUtils.readLines(todoFile));
	    	//Toast.makeText(getApplicationContext(), customizedSettings.toString(), Toast.LENGTH_LONG).show();
	    	setupSpinner(customizedSettings);
	    	sitefilter.setText(customizedSettings.get(3));
 		}
 		catch (IOException ex) {
 			customizedSettings = new ArrayList<String>();
 			ex.printStackTrace();
 		}
 	}
 	
 	private void setupSpinner(ArrayList<String> savedSettings) {
 		Resources res = getResources();
 		String[] arr_img_size = res.getStringArray(R.array.image_size);
 		String[] arr_color_filter = res.getStringArray(R.array.color_filter);
 		String[] arr_img_type = res.getStringArray(R.array.image_type);
 		
 		for (int i = 0; i < arr_img_size.length; i++) {
 			if (arr_img_size[i].equals(savedSettings.get(0))) {
 		 		imagesize.setSelection(i);
 			}
 		}
 		for (int i = 0; i < arr_color_filter.length; i++) {
 			if (arr_color_filter[i].equals(savedSettings.get(1))) {
 				colorfilter.setSelection(i);
 			}
 		}
 		for (int i = 0; i < arr_img_type.length; i++) {
 			if (arr_img_type[i].equals(savedSettings.get(2))) {
 		 		imagetype.setSelection(i);
 			}
 		}
 	}

}
