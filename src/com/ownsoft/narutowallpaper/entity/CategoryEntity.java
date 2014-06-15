package com.ownsoft.narutowallpaper.entity;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;

public class CategoryEntity 
{
	private int id;
	private String nameRu;
	private String nameEn;
	private String imageUrl;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNameRu() {
		return nameRu;
	}
	public void setNameRu(String nameRu) {
		this.nameRu = nameRu;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	
	public String getLocaleName(Locale locale)
	{
		if ((locale.getDisplayLanguage(Locale.US)).toLowerCase(Locale.US).indexOf("russian") > -1)
		{
			return getNameRu();
		}
		return getNameEn();
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public static final String TABLE_NAME = "category";
	
	
	public static ArrayList<CategoryEntity> parseJsonString(String json)
	{
		ArrayList<CategoryEntity> list = new ArrayList<CategoryEntity>();
		
		try {
			JSONArray jsonArray = new JSONArray(json);

			for(int i =0; i < jsonArray.length(); i++)
			{
				CategoryEntity item = new CategoryEntity();
				item.setId(jsonArray.getJSONObject(i).getInt("id"));
				item.setNameRu(jsonArray.getJSONObject(i).getString("name_ru"));
				item.setNameEn(jsonArray.getJSONObject(i).getString("name_en"));
				item.setImageUrl(jsonArray.getJSONObject(i).getString("url"));
				list.add(item);
			}
		} catch (JSONException e) 
		{
			e.printStackTrace();
		}
		
		return list;
	}
	
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NAME_RU = "name_ru";
	public static final String COLUMN_NAME_EN = "name_en";
	public static final String COLUMN_IMAGE_URL = "url";
	
	
	public static String getCreateTableSqlScript() {
		return "create table "
				  + TABLE_NAME + "( " 
				  + COLUMN_ID + "  integer primary key, " 
				  + COLUMN_NAME_RU +" nvarchar(500) not null, "
				  + COLUMN_NAME_EN +" nvarchar(500) not null, "
				  + COLUMN_IMAGE_URL + " nvarchar(500) null )";

	}

}
