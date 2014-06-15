package com.ownsoft.narutowallpaper.store;


import com.ownsoft.narutowallpaper.entity.CategoryEntity;
import com.ownsoft.narutowallpaper.entity.WallpaperInfo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "naruto_wallpaper.db";
	private static final int DATABASE_VERSION = 1;
	
	Context context;

	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	
	
	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(WallpaperInfo.getCreateTableSqlScript());
		database.execSQL(CategoryEntity.getCreateTableSqlScript());
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
	    //db.execSQL("DROP TABLE IF EXISTS " + WallpaperInfo.TABLE_NAME);
	    // Create tables again
	    onCreate(db);
	}

}
