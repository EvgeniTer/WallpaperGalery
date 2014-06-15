package com.ownsoft.narutowallpaper.store;

import java.util.ArrayList;

import com.ownsoft.narutowallpaper.entity.CategoryEntity;
import com.ownsoft.narutowallpaper.entity.WallpaperInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WallpaperStore {

	private DbHelper dbHelper;
	
	private static WallpaperStore store = null;
	
	public static void Initialize(Context context)
	{
		store = new WallpaperStore(context);
	}
	
	public static WallpaperStore getStore()
	{
		return store;
	}
	
	private WallpaperStore(Context context)
	{
		this.dbHelper = new DbHelper(context);
	}
	
	public ArrayList<WallpaperInfo> getWallpapers(int categoryId)
	{
		return getWallpapers(categoryId, -1, 30);
	}
	
	public ArrayList<WallpaperInfo> getWallpapers(int categoryId, int count)
	{
		return getWallpapers(categoryId, -1, count);
	}
	
	public ArrayList<CategoryEntity> getCategories()
	{
		ArrayList<CategoryEntity> result = new ArrayList<CategoryEntity>();
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
	
		
		Cursor cursor = db.query(CategoryEntity.TABLE_NAME, 
				new String[] { 
					CategoryEntity.COLUMN_ID,
					CategoryEntity.COLUMN_NAME_RU,
					CategoryEntity.COLUMN_NAME_EN,
					CategoryEntity.COLUMN_IMAGE_URL
				}, 
				null, 
				null, 
				null,
				null,
				CategoryEntity.COLUMN_ID + " asc ");

		
		if (cursor.moveToFirst())
		{
			int colIdx_Id = cursor.getColumnIndex(CategoryEntity.COLUMN_ID);
			int colIdx_NameRu = cursor.getColumnIndex(CategoryEntity.COLUMN_NAME_RU);
			int colIdx_NameEn = cursor.getColumnIndex(CategoryEntity.COLUMN_NAME_EN);
			int colIdx_Url = cursor.getColumnIndex(CategoryEntity.COLUMN_IMAGE_URL);
			
			do
			{
				CategoryEntity item = new CategoryEntity();
				item.setId(cursor.getInt(colIdx_Id));
				item.setNameRu(cursor.getString(colIdx_NameRu));
				item.setNameEn(cursor.getString(colIdx_NameEn));
				item.setImageUrl(cursor.getString(colIdx_Url));
				result.add(item);
			}
			while(cursor.moveToNext() == true);
		}
		cursor.close();
		db.close();
		
		return result;
	}
	
	public ArrayList<WallpaperInfo> getFavoriteWallpapers(int lastWallpaperId, int count)
	{
		ArrayList<WallpaperInfo> result = new ArrayList<WallpaperInfo>();
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
	
		
		Cursor cursor = db.query(WallpaperInfo.TABLE_NAME, 
				new String[] { 
					WallpaperInfo.COLUMN_ID,
					WallpaperInfo.COLUMN_NAME,
					WallpaperInfo.COLUMN_THUMB,
					WallpaperInfo.COLUMN_URL,
					WallpaperInfo.COLUMN_CATEGORY_ID,
					WallpaperInfo.COLUMN_IS_FAVORITE
				}, 
				WallpaperInfo.COLUMN_IS_FAVORITE + " = 1 and " +  WallpaperInfo.COLUMN_ID + " > ? ", 
				new String[] { String.valueOf(lastWallpaperId)}, 
				null,
				null,
				WallpaperInfo.COLUMN_ID + " asc ", String.valueOf(count));

		
		if (cursor.moveToFirst())
		{
			int colIdx_Id = cursor.getColumnIndex(WallpaperInfo.COLUMN_ID);
			int colIdx_Name = cursor.getColumnIndex(WallpaperInfo.COLUMN_NAME);
			int colIdx_Thumb = cursor.getColumnIndex(WallpaperInfo.COLUMN_THUMB);
			int colIdx_URL = cursor.getColumnIndex(WallpaperInfo.COLUMN_URL);
			int colIdx_Favorite = cursor.getColumnIndex(WallpaperInfo.COLUMN_IS_FAVORITE);
			int colIdx_CategoryId = cursor.getColumnIndex(WallpaperInfo.COLUMN_CATEGORY_ID);
			
			do
			{
				WallpaperInfo item = new WallpaperInfo();
				item.setId(cursor.getInt(colIdx_Id));
				item.setName(cursor.getString(colIdx_Name));
				item.setThumb(cursor.getString(colIdx_Thumb));
				item.setUrl(cursor.getString(colIdx_URL));
				item.setIsFavorite(cursor.getInt(colIdx_Favorite) == 1);
				item.setCategoryId(cursor.getInt(colIdx_CategoryId));
				result.add(item);
			}
			while(cursor.moveToNext() == true);
		}
		cursor.close();
		db.close();
		
		return result;
	}
	
	public ArrayList<WallpaperInfo> getWallpapers(int categoryId, int lastWallpaperId, int count)
	{
		ArrayList<WallpaperInfo> result = new ArrayList<WallpaperInfo>();
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
	
		
		Cursor cursor = db.query(WallpaperInfo.TABLE_NAME, 
				new String[] { 
					WallpaperInfo.COLUMN_ID,
					WallpaperInfo.COLUMN_NAME,
					WallpaperInfo.COLUMN_THUMB,
					WallpaperInfo.COLUMN_URL,
					WallpaperInfo.COLUMN_IS_FAVORITE
				}, 
				WallpaperInfo.COLUMN_ID + " > ? " +  ( categoryId == -1 ? "" : " and " + (WallpaperInfo.COLUMN_CATEGORY_ID + " = ? ")), 
				categoryId == -1 ? new String[] { String.valueOf(lastWallpaperId) } : new String[] { String.valueOf(lastWallpaperId), String.valueOf(categoryId) }, 
				null,
				null,
				WallpaperInfo.COLUMN_ID + " asc ", String.valueOf(count));

		
		if (cursor.moveToFirst())
		{
			int colIdx_Id = cursor.getColumnIndex(WallpaperInfo.COLUMN_ID);
			int colIdx_Name = cursor.getColumnIndex(WallpaperInfo.COLUMN_NAME);
			int colIdx_Thumb = cursor.getColumnIndex(WallpaperInfo.COLUMN_THUMB);
			int colIdx_URL = cursor.getColumnIndex(WallpaperInfo.COLUMN_URL);
			int colIdx_Favorite = cursor.getColumnIndex(WallpaperInfo.COLUMN_IS_FAVORITE);
			
			do
			{
				WallpaperInfo item = new WallpaperInfo();
				item.setId(cursor.getInt(colIdx_Id));
				item.setName(cursor.getString(colIdx_Name));
				item.setThumb(cursor.getString(colIdx_Thumb));
				item.setUrl(cursor.getString(colIdx_URL));
				item.setIsFavorite(cursor.getInt(colIdx_Favorite) == 1);
				result.add(item);
			}
			while(cursor.moveToNext() == true);
		}
		cursor.close();
		db.close();
		
		return result;
	}
	
	public int getLastWallpaperId()
	{
		int result = -1;
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
	
		
		Cursor cursor = db.query(WallpaperInfo.TABLE_NAME, 
				new String[] { WallpaperInfo.COLUMN_ID }, 
				null, 
				null, 
				null,
				null,
				WallpaperInfo.COLUMN_ID + " DESC ", "1");

		
		if (cursor.moveToFirst())
		{
			int colIdx_Id = cursor.getColumnIndex(WallpaperInfo.COLUMN_ID);
			
			result = cursor.getInt(colIdx_Id);
		}
		cursor.close();
		db.close();

		
		return result;
	}
	
	public void addWallpaper(WallpaperInfo wallpaper)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(WallpaperInfo.COLUMN_ID, wallpaper.getId());
		values.put(WallpaperInfo.COLUMN_NAME, wallpaper.getName());
		values.put(WallpaperInfo.COLUMN_THUMB, wallpaper.getThumb());
		values.put(WallpaperInfo.COLUMN_URL, wallpaper.getUrl());
		values.put(WallpaperInfo.COLUMN_CATEGORY_ID, wallpaper.getCategoryId());
		
		db.insert(WallpaperInfo.TABLE_NAME, null, values);
		
		db.close();
	}
	
	public void addCategory(CategoryEntity category)
	{
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(CategoryEntity.COLUMN_ID, category.getId());
		values.put(CategoryEntity.COLUMN_NAME_RU, category.getNameRu());
		values.put(CategoryEntity.COLUMN_NAME_EN, category.getNameEn());
		values.put(CategoryEntity.COLUMN_IMAGE_URL, category.getImageUrl());

		db.insert(CategoryEntity.TABLE_NAME, null, values);
		
		db.close();
	}

	public WallpaperInfo getWallpaper(int id) {
		WallpaperInfo result = new WallpaperInfo();
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
	
		
		Cursor cursor = db.query(WallpaperInfo.TABLE_NAME, 
				new String[] { 
					WallpaperInfo.COLUMN_ID,
					WallpaperInfo.COLUMN_NAME,
					WallpaperInfo.COLUMN_THUMB,
					WallpaperInfo.COLUMN_URL,
					WallpaperInfo.COLUMN_CATEGORY_ID,
					WallpaperInfo.COLUMN_IS_FAVORITE,
					WallpaperInfo.COLUMN_MATRIX
				}, 
				WallpaperInfo.COLUMN_ID + " = ? ", 
				new String[] { String.valueOf(id) } ,
				null,
				null,
				null
				);

		
		if (cursor.moveToFirst())
		{
			int colIdx_Id = cursor.getColumnIndex(WallpaperInfo.COLUMN_ID);
			int colIdx_Name = cursor.getColumnIndex(WallpaperInfo.COLUMN_NAME);
			int colIdx_Thumb = cursor.getColumnIndex(WallpaperInfo.COLUMN_THUMB);
			int colIdx_URL = cursor.getColumnIndex(WallpaperInfo.COLUMN_URL);
			int colIdx_Favorite = cursor.getColumnIndex(WallpaperInfo.COLUMN_IS_FAVORITE);
			int colIdx_CategoryId = cursor.getColumnIndex(WallpaperInfo.COLUMN_CATEGORY_ID);
			int colIdx_Matrix = cursor.getColumnIndex(WallpaperInfo.COLUMN_MATRIX);
			

			result.setId(cursor.getInt(colIdx_Id));
			result.setName(cursor.getString(colIdx_Name));
			result.setThumb(cursor.getString(colIdx_Thumb));
			result.setUrl(cursor.getString(colIdx_URL));
			result.setCategoryId(cursor.getInt(colIdx_CategoryId));
			result.setIsFavorite(cursor.getInt(colIdx_Favorite) == 1);
			result.setMatrix(cursor.getString(colIdx_Matrix));
		}
		cursor.close();
		db.close();
		
		return result;
	}

	public void addToFavorite(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(WallpaperInfo.COLUMN_IS_FAVORITE, 1);

		db.update(WallpaperInfo.TABLE_NAME, values, WallpaperInfo.COLUMN_ID + " = ? ", new String[]{String.valueOf(id)});
		
		db.close();
	}

	public void removeFromFavorite(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(WallpaperInfo.COLUMN_IS_FAVORITE, 0);

		db.update(WallpaperInfo.TABLE_NAME, values, WallpaperInfo.COLUMN_ID + " = ? ", new String[]{String.valueOf(id)});
		
		db.close();
	}

	public void saveWallpaperMatrix(int id, float[] matrixArray) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(WallpaperInfo.COLUMN_MATRIX, WallpaperInfo.getStringFromMatrix(matrixArray));

		db.update(WallpaperInfo.TABLE_NAME, values, WallpaperInfo.COLUMN_ID + " = ? ", new String[]{String.valueOf(id)});
		
		db.close();
	}
}
