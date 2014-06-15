package com.ownsoft.narutowallpaper.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.ownsoft.narutowallpaper.entity.WallpaperInfo;
import com.ownsoft.narutowallpaper.store.WallpaperStore;
import com.ownsoft.narutowallpaper.utility.BitmapUtility;
import com.ownsoft.narutowallpaper.utility.SettingsUtility;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;

public class WallpaperBroadcastReceiver extends BroadcastReceiver {

	SettingsUtility settings;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		settings = new SettingsUtility(context);
		
		if (isTodayChangeWallpaper())
		{
			return;
		}
		
		int lastShowWPId = settings.getLastWallpaperIdFromService(-1);
		
		if (WallpaperStore.getStore() == null)
			WallpaperStore.Initialize(context);
		
		ArrayList<WallpaperInfo> wallpapers = WallpaperStore.getStore().getFavoriteWallpapers(lastShowWPId, 1);
		if (wallpapers.size() == 0)
		{
			wallpapers = WallpaperStore.getStore().getFavoriteWallpapers(-1, 1);
		}
		
		if (wallpapers.size() > 0)
		{
			WallpaperInfo wp = wallpapers.get(0);
			Bitmap bm = BitmapUtility.getSavedBitmap(wp.getFileNameForSave());
			
			
			if (wp.getMatrix() != null && wp.getMatrix().length() > 0)
			{
				Matrix matrix = new Matrix();
				matrix.setValues(WallpaperInfo.getMatrixFromString(wp.getMatrix()));
				bm =  Bitmap.createBitmap(bm, 0, 0,
                        bm.getWidth(), bm.getHeight(), matrix, true);
			}
			if (bm != null)
			{
				try {
					WallpaperManager.getInstance(context).setBitmap(bm);
					bm.recycle();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			settings.setLastWallpaperIdFromService(wallpapers.get(0).getId());
			setTodayWallpaperChanged();
		}
	}

	private boolean isTodayChangeWallpaper()
	{
		Calendar c = Calendar.getInstance(); 
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		String date = Integer.valueOf(day).toString() + "." + Integer.valueOf(month).toString();
		
		String lastCheckDate = settings.getLastDateSetWallpaperFromService();
		return date.equals(lastCheckDate);
	}
	
	private void setTodayWallpaperChanged()
	{
		Calendar c = Calendar.getInstance(); 
		int day = c.get(Calendar.DAY_OF_MONTH);
		int month = c.get(Calendar.MONTH);
		String date = Integer.valueOf(day).toString() + "." + Integer.valueOf(month).toString();
		settings.setLastDateSetWallpaperFromService(date);
	}

}
