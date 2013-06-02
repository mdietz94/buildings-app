package com.example.buildings;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;

public class LocationLoader extends Service implements LocationListener
{
	private static double latitude;
	private static double longitude;
	
	public static double Latitude() { return latitude; }
	public static double Longitude() { return longitude; }
	
	@Override
	public void onLocationChanged(Location arg0)
	{
		latitude = arg0.getLatitude();
		longitude = arg0.getLongitude();
	}

	@Override
	public void onProviderDisabled(String arg0) {}
	@Override
	public void onProviderEnabled(String arg0) {}
	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {}
	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
