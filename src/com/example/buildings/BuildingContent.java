package com.example.buildings;

import java.util.List;
import android.util.SparseArray;

public class BuildingContent{

	public static List<Building> ITEMS;

	public static SparseArray<Building> ITEM_MAP;

	public static Float LATITUDE = null;
	public static Float LONGITUDE = null;
	
	public static void addItem(Building item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.get_id(), item);
	}
}
