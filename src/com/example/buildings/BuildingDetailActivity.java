package com.example.buildings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class BuildingDetailActivity extends Activity {

	public static final String	ARG_ITEM_ID	= "item_id";	// for the Intent
	private Building			mItem;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_building_detail);
		if (Build.VERSION.SDK_INT > 11)
			getActionBar().setDisplayHomeAsUpEnabled(true);
		
		mItem = BuildingContent.ITEM_MAP.get(getIntent().getIntExtra(ARG_ITEM_ID, -1));
		
		ImageButton iButton = (ImageButton)findViewById(R.id.buildingImage);
		iButton.setImageDrawable(mItem.get_images()[0]);
		iButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0)
			{
				Intent i = new Intent(BuildingDetailActivity.this, GalleryActivity.class);
				i.putExtra("images", mItem.get_images());
				i.putExtra("buildingID", mItem.get_id().intValue());
				startActivity(i);
			}
			
		});
		((TextView)findViewById(R.id.buildingText)).setText(mItem.toString());
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpTo(this, new Intent(this,
						BuildingListActivity.class));
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
}
