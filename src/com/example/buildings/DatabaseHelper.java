package com.example.buildings;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private static String DB_PATH;
	private static String DB_NAME = "Buildings.db";
	
	SQLiteDatabase database;
	private final Context context;
	
	public DatabaseHelper(Context context)
	{
		super(context, DB_NAME, null, 1);
		this.context = context;
		DB_PATH = "/data/data/com.example.buildings/databases/";
	}

	public void createDatabase()
	{
		if (checkDatabase()) { return; }
		else
		{
			this.getReadableDatabase();
			copyDatabase();
		}
	}
	
	private boolean checkDatabase()
	{
		SQLiteDatabase checkDB = null;
		try{
		checkDB = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
		}catch (Exception e) { checkDB = null; }
		if (checkDB != null) { checkDB.close(); }
		return checkDB != null;
	}
	
	private void copyDatabase()
	{
		try {
		InputStream input = context.getAssets().open(DB_NAME);
		String filename = DB_PATH + DB_NAME;
		OutputStream out = new FileOutputStream(filename);
		
		byte[] buffer = new byte[1024];
		int length;
		while ((length = input.read(buffer)) > 0)
		{
			out.write(buffer, 0, length);
		}
		
		out.flush();
		out.close();
		input.close();
		} catch (IOException e) { e.printStackTrace(); }
	}
	
	public void openDatabase()
	{
		database = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.OPEN_READONLY);
	}
	
	@Override
	public synchronized void close()
	{
		if (database != null) database.close();
		super.close();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
}