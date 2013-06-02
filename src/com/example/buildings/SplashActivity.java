package com.example.buildings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;

public class SplashActivity extends Activity {
	TextView	percentComplete;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);

		percentComplete = (TextView) findViewById(R.id.percent);
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		LocationLoader ll = new LocationLoader();
		LoadBuildings lb = new LoadBuildings();
		if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			lm.requestSingleUpdate(LocationManager.GPS_PROVIDER, ll, null);
			lb.execute(this);
		}
		else if (lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
		{
			lm.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, ll, null);
			lb.execute(this);
		}
		else {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
			alertDialog.setTitle("Location Services Unreachable");
			alertDialog
					.setTitle("Both location from wireless networks and GPS sattelites are disabled."
							+ "Would you like to go to the settings menu to enable them?");
			alertDialog.setPositiveButton("Settings",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which)
						{
							SplashActivity.this.startActivity(new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						}
					});
			alertDialog.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						// This should really exit the app. Maybe a browse mode
						// or something.
						// still app is meant for local viewing, not as a
						// database viewer
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.cancel();
							SplashActivity.this.finish();
						}
					});
			alertDialog.show();
		}

	}

	public class LoadBuildings extends AsyncTask<Context, Integer, Void> {
		@Override
		protected Void doInBackground(Context... args)
		{

			Context context = args[0];
			BuildingContent.ITEM_MAP = new SparseArray<Building>();
			BuildingContent.ITEMS = new ArrayList<Building>();
			Log.i("BuildingContent", "BuildingContent Initialized!");
			DatabaseHelper helper = new DatabaseHelper(context);
			helper.createDatabase();
			helper.openDatabase();
			Cursor cursor = helper.database.query("buildings", null, null,
					null, null, null, null, null);
			Resources res = context.getResources();
			int count = 0;
			if (cursor.moveToFirst()) {
				do {
					Building tmp = new Building();
					int column = -1;

					if ((column = cursor.getColumnIndex("_id")) > -1)
						tmp.set_id(cursor.getInt(column));

					Integer[] images = new Integer[5];
					String base = "bldg" + tmp.get_id().toString() + "x";
					boolean hasImages = false;
					for (int i = 0; i < 5; i++) {
						int id = 0;
						if ((id = res.getIdentifier(base + i, "drawable",
								context.getPackageName())) == 0)
							break;
						hasImages = true;
						images[i] = id;
					}

					boolean hasLocation = false;
					if ((column = cursor.getColumnIndex("latitude")) > -1
							&& cursor.getFloat(column) != 0) {
						int col2;
						if ((col2 = cursor.getColumnIndex("longitude")) > -1) {
							tmp.set_location(cursor.getFloat(column),
									cursor.getFloat(col2));
							hasLocation = true;
						}
					}
					if (hasImages && hasLocation) {
						tmp.set_images(images);

						if ((column = cursor.getColumnIndex("name")) > -1)
							tmp.set_name(cursor.getString(column));
						if ((column = cursor.getColumnIndex("architect")) > -1)
							tmp.set_architect(cursor.getString(column));
						if ((column = cursor.getColumnIndex("country")) > -1)
							tmp.set_country(cursor.getString(column));
						if ((column = cursor.getColumnIndex("state")) > -1)
							tmp.set_state(cursor.getString(column));
						if ((column = cursor.getColumnIndex("city")) > -1)
							tmp.set_city(cursor.getString(column));
						if ((column = cursor.getColumnIndex("region")) > -1)
							tmp.set_region(cursor.getString(column));
						if ((column = cursor.getColumnIndex("address")) > -1)
							tmp.set_address(cursor.getString(column));
						if ((column = cursor.getColumnIndex("date")) > -1)
							tmp.set_date(cursor.getString(column));
						if ((column = cursor.getColumnIndex("description")) > -1)
							tmp.set_description(cursor.getString(column));
						if ((column = cursor.getColumnIndex("keywords")) > -1) {
							String str = null;
							if ((str = cursor.getString(column)) != null)
								tmp.set_keywords(str.split(";"));
						}

						BuildingContent.addItem(tmp);
						publishProgress((int) (++count
								/ Math.min(100, (float) cursor.getCount()) * 100.0f));
					}
					if (count >= 100)
						break;
				} while (cursor.moveToNext());
				helper.close();

				publishProgress(101);

				while (BuildingContent.LONGITUDE == null) {
					try {
						wait(500);
					} catch (Exception e) {
					}
				}
				publishProgress(102);

				Collections.sort(BuildingContent.ITEMS,
						new Comparator<Building>() {

							@Override
							public int compare(Building arg0, Building arg1)
							{
								Float dist0 = (float) (Math.pow(
										Math.abs(arg0.get_latitude()
												- BuildingContent.LATITUDE), 2f) + Math
										.pow(Math.abs(arg0.get_longitude()
												- BuildingContent.LONGITUDE),
												2f));
								Float dist1 = (float) (Math.pow(
										Math.abs(arg1.get_latitude()
												- BuildingContent.LATITUDE), 2f) + Math
										.pow(Math.abs(arg1.get_longitude()
												- BuildingContent.LONGITUDE),
												2f));
								return dist0.compareTo(dist1);
							}
						});

			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			if (values[0] < 100)
				percentComplete.setText(values[0].toString());
			else if (values[0] == 101)
				percentComplete.setText("Waiting for location fix!");
			else
				percentComplete.setText("Sorting database by location...");
		}

		@Override
		protected void onPostExecute(Void arg)
		{
			Intent intent = new Intent(SplashActivity.this,
					BuildingListActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			if (Build.VERSION.SDK_INT >= 11)
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}
	}

	class LocationLoader implements LocationListener {
		@Override
		public void onLocationChanged(Location location)
		{
			if (location != null) {
				Log.e("LocationServices", "Location Updated!");
				BuildingContent.LATITUDE = (float) location.getLatitude();
				BuildingContent.LONGITUDE = (float) location.getLongitude();
				Log.e("LocationServices", "BuildingContent.LAT,LONG set!");
			}
		}

		@Override
		public void onProviderDisabled(String provider)
		{
		}

		@Override
		public void onProviderEnabled(String provider)
		{
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras)
		{
		}

	}
}
