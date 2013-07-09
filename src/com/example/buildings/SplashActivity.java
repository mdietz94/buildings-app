package com.example.buildings;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
			publishProgress(101);
			while (BuildingContent.LONGITUDE == null) {
				try {
					wait(500);
				} catch (Exception e) {
				}
			}
			publishProgress(0);
			BuildingContent.ITEM_MAP = new SparseArray<Building>();
			BuildingContent.ITEMS = new ArrayList<Building>();
			Log.i("BuildingContent", "BuildingContent Initialized!");
			HttpClient httpClient = new DefaultHttpClient();
			HttpResponse response;
			try {
				response = httpClient.execute(new HttpGet("www.bldg.com/find-by-location/" + BuildingContent.LATITUDE + "/" + BuildingContent.LONGITUDE));
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				JSONArray buildings = new JSONArray(responseString);
				for(int i = 0; i < buildings.length(); i++){
					try {
					Building tmp = new Building();
					JSONObject building = buildings.getJSONObject(i);
					tmp.set_id(building.getInt("_id"));
					tmp.set_name(building.getString("name"));
					tmp.set_architect(building.getString("architect"));
					tmp.set_country(building.getString("country"));
					tmp.set_state(building.getString("state"));
					tmp.set_city(building.getString("city"));
					tmp.set_region(building.getString("region"));
					tmp.set_address(building.getString("address"));
					tmp.set_date(building.getString("date"));
					tmp.set_description(building.getString("date"));
					tmp.set_keywords(building.getString("keywords").split(";"));
					tmp.set_location(Float.parseFloat(building.getString("latitude")), Float.parseFloat(building.getString("longitude")));
					Drawable[] images = new Drawable[5];
					String base = "bldg" + tmp.get_id().toString() + "x";
					boolean hasImages = false;
					for (int x = 0; x < 5; x++){
						HttpResponse resp = httpClient.execute(new HttpGet("www.bldg.com/static/images/" + base + x));
						if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
							images[x] = Drawable.createFromStream(resp.getEntity().getContent(), null);
							hasImages = true;
						} else if (resp.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND){
							break;
						} else {
							Log.e("BuildingContent", "Error trying to load images: " + resp.getStatusLine().getStatusCode());
							break;
						}
					}
					if (hasImages) // if there are no images, not worth it
						BuildingContent.addItem(tmp);
					} catch(JSONException e) {
						Log.d("bldg", "Stub building found... skipping");
					}
					publishProgress((int)((i / (float)buildings.length()) * 100.0f));
				}
			}
			else
			{
				Log.e("BuildingContent", "Error connecting to www.bldg.com");
				response.getEntity().getContent().close();
			}
			} catch (IOException e) {
				Log.e("BuildingContent", "IOException trying to connect to www.bldg.com");
			} catch (JSONException e) {
				Log.e("BuildingContent", "JSONException trying to initialize the buildings array");
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
