package com.codepath.gridsearch;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView.ScaleType;

import com.loopj.android.image.SmartImageView;

/** Implement Image Display Activity for the Image Viewer Grid
 * @author Dipankar
 * @version 1
 *
 */
public class ImageDisplayActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		ImageResult result = (ImageResult) getIntent().getSerializableExtra("url");
		final SmartImageView ivImage = (SmartImageView) findViewById(R.id.ivResult);
		ivImage.setImageUrl(result.getFullUrl());
		
		// Hook up clicks on the thumbnail views.
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            	ivImage.setScaleType(ScaleType.FIT_XY);
            }
        });
	}
}

