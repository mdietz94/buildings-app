package com.example.buildings;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.util.AttributeSet;
import android.view.View;

@SuppressLint("ViewConstructor")
public class ComboView extends View {

	String mDistance;
	int mBackgroundColor;
	int mTextColor;
	String mText; // some of these fields are extraneous...
	Bitmap mDrawable;
	Building mBuilding;
	Bitmap mDistanceBackground;
	int mTextAlignment;
	
	Paint mTextPaint; // for drawing text
	Paint mBackgroundTextPaint; // for drawing background rectangle
	Paint mImagePaint; // for drawing images
	
	Context context;
	
	public int getTextColor() { return mTextColor; }
	public void setTextColor(int color)
	{
		mTextColor = color;
		invalidate();
		requestLayout();
	}
	
	
	
	public void setBuilding(Building building)
	{
		mDrawable = ((BitmapDrawable)context.getResources().getDrawable(building.get_images()[0])).getBitmap();
		if (getWidth() > 0 && getHeight() > 0)
			mDrawable = Bitmap.createScaledBitmap(mDrawable, getWidth(), getHeight(), false);
		mText = building.get_name();
		mBuilding = building;
		invalidate();
		requestLayout();
	}
	
	public int getmBackgroundColor() { return mBackgroundColor; }
	public void setmBackgroundColor(int mBackgroundColor)
	{
		this.mBackgroundColor = mBackgroundColor;
		invalidate();
		requestLayout();
	}

	public String getmText() { return mText; }
	public void setmText(String mText)
	{
		this.mText = mText;
		invalidate();
		requestLayout();
	}

	public Bitmap getmDrawable() { return mDrawable; }
	public void setmDrawable(Bitmap mDrawable)
	{
		this.mDrawable = mDrawable;
		invalidate();
		requestLayout();
	}

	public Bitmap getmDistanceBackground() { return mDistanceBackground; }
	public void setmDistanceBackground(Bitmap mDistanceBackground)
	{
		this.mDistanceBackground = mDistanceBackground;
		invalidate();
		requestLayout();
	}

	public int getmTextAlignment() { return mTextAlignment; }
	public void setmTextAlignment(int mTextAlignment)
	{
		this.mTextAlignment = mTextAlignment;
		invalidate();
		requestLayout();
	}
	
	public ComboView(Context context, AttributeSet attrs, Building building)
	{
		this(context, building);
	}

	public ComboView(Context context, Building building)
	{
		super(context);
		mBuilding = building;
		mBackgroundColor = Color.argb(175, 200, 200, 200);
		// THIS IMAGE MUST BE FOUND!
		//mDistanceBackground = ((BitmapDrawable)a.getDrawable(R.styleable.ComboView_distanceBackground)).getBitmap();
		this.context = context;
		mTextColor = Color.WHITE;
		mDrawable = ((BitmapDrawable)context.getResources().getDrawable(building.get_images()[0])).getBitmap();
		mText = building.get_name();
		mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setColor(mTextColor);
		mTextPaint.setStyle(Paint.Style.FILL);
		mTextPaint.setTextAlign(Align.LEFT);
		mTextPaint.setTextSize(12);

		mBackgroundTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mBackgroundTextPaint.setColor(mBackgroundColor);
		mBackgroundTextPaint.setStyle(Paint.Style.FILL);
		
		mImagePaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
		mImagePaint.setStyle(Paint.Style.FILL);
		
		float[] results = new float[3];
		Location.distanceBetween(BuildingContent.LATITUDE, BuildingContent.LONGITUDE, mBuilding.get_latitude(), mBuilding.get_longitude(), results);
		Float distance = (float)((double)results[0] * 0.000621371);
		int dist = distance.toString().indexOf('.');
		if (dist != -1 && dist+2 <= distance.toString().length())
			mDistance = distance.toString().substring(0, dist+2);
		else
			mDistance = distance.toString();
		
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);
		if (w > 0 && h > 0)
			mDrawable = Bitmap.createScaledBitmap(mDrawable, w, h, true);
	}
	@Override
	protected void onDraw(Canvas canvas)
	{
		canvas.drawBitmap(mDrawable, 0, 0, mImagePaint);
		canvas.drawRect(0, getHeight() - 30, getWidth(), getHeight(), mBackgroundTextPaint);
		canvas.drawText(mText, 1, getHeight() - 10, mTextPaint);
		canvas.drawRect(getWidth() - 50, 0, getWidth(), 20, mBackgroundTextPaint); // ugly circle needs a picture
		canvas.drawText( mDistance + "mi", getWidth()-50, 18, mTextPaint);
	}

}
