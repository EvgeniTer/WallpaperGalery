package com.ownsoft.narutowallpaper.utility;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class BitmapUtility {

	private static String externalStoragePath = "";
	private static String getExternalStoragePath()
	{
		if (externalStoragePath.length() == 0)
		{
			externalStoragePath = Environment.getExternalStorageDirectory().toString() + "/NarutoWP/";
		}
		return externalStoragePath;
	}
	
	public static final String DEFAULT_FILE_EXTENSION = "png";
	
	public static void saveBitmap(Bitmap bm, String fileName)
	{
		saveBitmap(bm, fileName, DEFAULT_FILE_EXTENSION);
	}
	public static void saveBitmap(Bitmap bm, String fileName, String fileExtension)
	{
		File dir = new File(getExternalStoragePath());
		if (dir.exists() == false)
			dir.mkdirs();
		
		
		File file = new File(getExternalStoragePath(), fileName + "." +fileExtension.replace(".", ""));
		if (file.exists())
		{
			file.delete();
		}
		
		OutputStream fOut;
		try {
			fOut = new FileOutputStream(file);
			bm.compress(Bitmap.CompressFormat.PNG, 90, fOut);
			fOut.flush();
			fOut.close();	
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Bitmap getSavedBitmap(String fileName, String fileExtension)
	{
		File file = new File(getExternalStoragePath(), fileName + "." +fileExtension.replace(".", ""));
		if (file.exists() == false)
			return null;
		
		return BitmapFactory.decodeFile(file.getAbsolutePath());
	}

	public static Bitmap getSavedBitmap(String fileName) {
		return getSavedBitmap(fileName, DEFAULT_FILE_EXTENSION);
	}
	
	
	public static String getSavedFilePath(String fileName)
	{
		return getSavedFilePath(fileName, DEFAULT_FILE_EXTENSION);
	}
	public static String getSavedFilePath(String fileName, String fileExtension)
	{
		File file = new File(getExternalStoragePath(), fileName + "." +fileExtension.replace(".", ""));
		if (file.exists() == false)
			return null;
		
		return file.getAbsolutePath();
	}
	
}
