package com.example.buildings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class BuildingListActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_building_list);
		
		GridView gridView = (GridView) findViewById(R.id.building_list);
		gridView.setAdapter(new ImageAdapter(this));
		gridView.setOnItemClickListener( new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id){
				Intent detailIntent = new Intent(BuildingListActivity.this, BuildingDetailActivity.class);
				detailIntent.putExtra(BuildingDetailActivity.ARG_ITEM_ID, BuildingContent.ITEMS.get(position).get_id());
				startActivity(detailIntent);
			}
		});
		Log.i("BuildingListActivity", "BuildingListActivity Initialized!");
	}
}
