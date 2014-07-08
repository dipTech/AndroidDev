package com.codepath.gridsearch;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

/** The adapter class for the image display
 * @author Dipankar
 * @version 1
 */
public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
		super(context, android.R.layout.simple_list_item_1, images);
		//super(context, R.layout.item_image_result, images);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = this.getItem(position);
		SmartImageView ivImage;
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			ivImage = (SmartImageView) inflator.inflate(R.layout.item_image_result, parent, false);
		}
		else {
			ivImage = (SmartImageView) convertView;
			ivImage.setImageResource(android.R.color.transparent);
		}
		ivImage.setImageUrl(imageInfo.getThumbUrl());
		return ivImage;
	}

}

