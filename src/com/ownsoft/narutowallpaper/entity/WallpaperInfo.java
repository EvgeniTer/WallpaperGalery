package com.ownsoft.narutowallpaper.entity;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class WallpaperInfo 
{
	private int id;
	private String name;
	private String thumb;
	private String url;
	private Boolean isFavorite;
	private int categoryId;
	private String categoryName;
	private String matrix;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public static final String TABLE_NAME = "wallpaper";
	
	
	public static ArrayList<WallpaperInfo> parseJsonString(String json)
	{
		ArrayList<WallpaperInfo> list = new ArrayList<WallpaperInfo>();
		
		try {
			JSONArray jsonArray = new JSONArray(json);

			for(int i =0; i < jsonArray.length(); i++)
			{
				WallpaperInfo item = new WallpaperInfo();
				item.setId(jsonArray.getJSONObject(i).getInt("id"));
				item.setName(jsonArray.getJSONObject(i).getString("name"));
				item.setThumb(jsonArray.getJSONObject(i).getString("thumb"));
				item.setUrl(jsonArray.getJSONObject(i).getString("full"));
				item.setCategoryId(jsonArray.getJSONObject(i).getInt("category_id"));
				
				list.add(item);
			}
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME = "name";
	public static final String COLUMN_THUMB = "thumb";
	public static final String COLUMN_URL = "url";
	public static final String COLUMN_CATEGORY_ID = "category_id";
	public static final String COLUMN_IS_FAVORITE = "is_favorite";
	public static final String COLUMN_MATRIX = "matrix";
	
	
	public static String getCreateTableSqlScript() {
		return "create table "
				  + TABLE_NAME + "( " 
				  + COLUMN_ID + "  integer primary key, " 
				  + COLUMN_NAME +" nvarchar(500) not null, " 
				  + COLUMN_THUMB +" nvarchar(500) not null, " 
				  + COLUMN_URL +" nvarchar(500) not null," 
				  + COLUMN_IS_FAVORITE +" int not null default 0, "
				  + COLUMN_CATEGORY_ID +" int not null," 
				  + COLUMN_MATRIX + " nvarchar(500) null )";

	}
	public Boolean getIsFavorite() {
		return isFavorite;
	}
	public void setIsFavorite(Boolean isFavorite) {
		this.isFavorite = isFavorite;
	}
	public String getFileNameForSave() {
		return String.valueOf(this.categoryId) + "_" + String.valueOf(this.id);
	}
	public String getMatrix() {
		return this.matrix;
	}
	
	public static float[] getMatrixFromString(String value)
	{
		if (value == null || value.length() == 0)
			return null;
		
		String[] items = value.split("|");
		
		float[] result = new float[9];
		for(int i = 0; i < items.length && i < 9; i++)
		{
			result[i] = Float.parseFloat(items[i]);
		}
		return result;
	}
	
	public static String getStringFromMatrix(float[] matrix)
	{
		if (matrix == null || matrix.length == 0)
			return "";
		
		String result = "";
		
		for(int i = 0; i < matrix.length; i++)
		{
			if (result.length() > 0)
				result +="|";
			
			result += String.valueOf(matrix[i]);
		}
		
		return result;
	}
	
	public void setMatrix(String matrix) {
		this.matrix = matrix;
	}
	
	

}
