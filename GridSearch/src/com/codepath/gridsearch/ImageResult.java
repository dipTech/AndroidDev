package com.codepath.gridsearch;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/** Class to store image result,
 * serialized for storage requirement
 * @author Dipankar
 * @version 1
 *
 */
public class ImageResult implements Serializable {
	private String fullUrl;
	private String thumbUrl;

	public String toString() {
		return thumbUrl;
	}

	public ImageResult(JSONObject json) {
		try {
			this.fullUrl = json.getString("url");
			this.thumbUrl = json.getString("tbUrl");
		} catch (JSONException e) {
			this.fullUrl = null;
			this.thumbUrl = null;
		}
	}

	public String getFullUrl() {
		return fullUrl;
	}

	public String getThumbUrl() {
		return thumbUrl;
	}

	public static Collection<? extends ImageResult> fromJSONArray(
			JSONArray array) {
		ArrayList<ImageResult> results = new ArrayList<ImageResult>();
		for (int x = 0; x < array.length(); x++) {
			try {
				results.add(new ImageResult(array.getJSONObject(x)));
			} catch (JSONException jse) {
				jse.printStackTrace();
			}
		}
		return results;
	}
}
