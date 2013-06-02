package com.example.buildings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher.ViewFactory;

public class GalleryActivity extends Activity implements ViewFactory {
	private Integer				mImages[];
	private int					mIndex;
	private ImageSwitcher		iSwitcher;
	private float				mOrigX;

	@SuppressLint("NewApi")
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gallery);

		mIndex = 0;
		mImages = BuildingContent.ITEM_MAP.get(getIntent().getIntExtra("buildingID", -1)).get_images();
		

		iSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher1);
		iSwitcher.setFactory(this);
		iSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.slide_in_left));
		iSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
				android.R.anim.slide_out_right));
		iSwitcher.setImageResource(mImages[mIndex]);
		
		iSwitcher.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1)
			{
				if (arg1.getAction() == MotionEvent.ACTION_UP)
				{
					if (arg1.getX() - mOrigX > 0)
					{
						iSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_in_left));
						iSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.slide_out_right));
						if (--mIndex > 0)
							iSwitcher.setImageResource(mImages[mIndex]);
						else {
							mIndex = mImages.length - 1;
							while (mImages[mIndex] == null)
								mIndex--;
							iSwitcher.setImageResource(mImages[mIndex]);
						}
					}
					else
					{
						iSwitcher.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right));
						iSwitcher.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_out_left));
						if (++mIndex < mImages.length && mImages[mIndex] != null)
							iSwitcher.setImageResource(mImages[mIndex]);
						else {
							mIndex = 0;
							iSwitcher.setImageResource(mImages[mIndex]);
						}
					}
				}
				else if (arg1.getAction() == MotionEvent.ACTION_DOWN)
					mOrigX = arg1.getX();

				
				return true;
			}
			
		});
//
//		iSwitcher.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0)
//			{
//				if (++mIndex < mImages.length)
//					iSwitcher.setImageResource(mImages[mIndex]);
//				else {
//					mIndex = 0;
//					iSwitcher.setImageResource(mImages[mIndex]);
//				}
//
//			}
//		});

	}

	@Override
	public View makeView()
	{
		ImageView iView = new ImageView(this);
		iView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		iView.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		iView.setBackgroundColor(0xFF000000);
		return iView;
	}

}
