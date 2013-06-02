package com.example.buildings;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;

public class ImageAdapter extends BaseAdapter {

	private Context mContext;
	
	ImageAdapter(Context c)
	{
		mContext = c;
		Log.i("ImageAdapter", "ADAPTER CREATED!");
		Log.i("BuildingContent", "Items="+getCount());
	}

	@Override
	public int getCount()
	{
		return 10;
	}

	@Override
	public Object getItem(int position)
	{
		return BuildingContent.ITEMS.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return BuildingContent.ITEMS.get(position).get_id();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ComboView imageView;
		if (convertView == null)
		{
			imageView = new ComboView(mContext, BuildingContent.ITEMS.get(position));
			imageView.setLayoutParams(new GridView.LayoutParams(170,170));
			imageView.setPadding(8, 8, 8, 8);
		} else {
			imageView = (ComboView) convertView;
		}
		imageView.setBuilding(BuildingContent.ITEMS.get(position));
		
		return imageView;
	}

}
