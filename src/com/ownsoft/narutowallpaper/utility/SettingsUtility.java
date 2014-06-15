package com.ownsoft.narutowallpaper.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsUtility 
{
	SharedPreferences preferences;
	
	private static final String PREFERENCE_NAME = "WALLPAPER_PREFERENCE";
	private static final String LAST_WALLPAPER_ID_FROM_SERVICE = "LAST_WALLPAPER_ID_FROM_SERVICE";
	private static final String LAST_DATE_SET_WALLPAPER_FROM_SERVICE = "LAST_DATE_SET_WALLPAPER_FROM_SERVICE";
	private static final String LAST_SHOW_CATEGORY_ID = "LAST_SHOW_CATEGORY_ID";
	
	public SettingsUtility(Context context)
	{
		this.preferences = context.getSharedPreferences(PREFERENCE_NAME, 0);
	}
	
	public void setLastDateSetWallpaperFromService(String date)
	{
		SharedPreferences.Editor editor = preferences.edit();
        editor.putString(LAST_DATE_SET_WALLPAPER_FROM_SERVICE, date);
        editor.commit();
	}
	
	public String getLastDateSetWallpaperFromService()
	{
		return preferences.getString(LAST_DATE_SET_WALLPAPER_FROM_SERVICE, "");
	}
	
	public void setLastWallpaperIdFromService(int wallpaperId)
	{
		SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LAST_WALLPAPER_ID_FROM_SERVICE, wallpaperId);
        editor.commit();
	}
	
	public int getLastWallpaperIdFromService(int defaultValue)
	{
		return preferences.getInt(LAST_WALLPAPER_ID_FROM_SERVICE, defaultValue);
	}
	
	public void setLastShowCategoryId(int categoryId)
	{
		SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(LAST_SHOW_CATEGORY_ID, categoryId);
        editor.commit();
	}
	public int getLastShowCategoryId(int defaultCategoryId)
	{
		return preferences.getInt(LAST_SHOW_CATEGORY_ID, defaultCategoryId);
	}
}
