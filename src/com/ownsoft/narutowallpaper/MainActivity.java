package com.ownsoft.narutowallpaper;

import java.util.ArrayList;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.ownsoft.narutowallpaper.entity.WallpaperInfo;
import com.ownsoft.narutowallpaper.services.WallpaperBroadcastReceiver;
import com.ownsoft.narutowallpaper.store.WallpaperStore;
import com.ownsoft.narutowallpaper.utility.DownloadStringTask;
import com.ownsoft.narutowallpaper.utility.MessageUtility;
import com.ownsoft.narutowallpaper.utility.SettingsUtility;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;


public class MainActivity extends ActionBarActivity  {
	
	ListView list;
	GridView listGrid;
	DownloadStringTask downloadTask;
	SimpleWallpaperAdapter adapter;
	ProgressBar progressBar;
	SlidingMenu menu;
	SettingsUtility settings;
	MessageUtility messageUtil;
	
	int currentCategoryId = -1;
	boolean isLoadFavorite = false;
	
	public static final int REQUEST_CODE_WALLPAPER_ACTIVITY = 1;
	
	protected void startWallpaperChangeService()
	{
		Intent alarmIntent = new Intent(getBaseContext(), WallpaperBroadcastReceiver.class);
    	PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    	AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    	alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis() + 1000 * 60 * 30, 1000 * 60 * 30  , pendingIntent);
	}
	
	protected void initImageLoader()
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .resetViewBeforeLoading(false)  // default
        .delayBeforeLoading(1000)
        .cacheInMemory(true) // default
        .cacheOnDisk(true) // default
        .build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
		.defaultDisplayImageOptions(options)
        .build();
		ImageLoader.getInstance().init(config);
	}
	
	protected void initControls()
	{
		list = (ListView)findViewById(R.id.list);
		listGrid = (GridView)findViewById(R.id.listGrid);
		progressBar = (ProgressBar)findViewById(R.id.progressLoad);
		
		menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setFadeDegree(0.35f);
        menu.setBehindOffset(50);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.left_menu_fragment);
        
        settings = new SettingsUtility(getApplicationContext());
        messageUtil = new MessageUtility(getApplicationContext());
	}
	
	protected boolean isInternetAvailable()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	
	public void loadFavorite()
	{
		isLoadFavorite = true;
		this.getSupportActionBar().setTitle(getString(R.string.favorite));
		if (downloadTask != null)
		{
			downloadTask.cancel(true);
			downloadTask = null;
		}
		progressBar.setVisibility(View.GONE);
		
		adapter.clear();
		
		ArrayList<WallpaperInfo> savedItems = WallpaperStore.getStore().getFavoriteWallpapers(-1, 100);
        for(int i =0; i < savedItems.size(); i++)
		{
			adapter.add(savedItems.get(i));
		}
		adapter.notifyDataSetChanged();
	}
	
	public void loadAdapterByCategory(int categoryId, String categoryName)
	{
		isLoadFavorite = false;
		settings.setLastShowCategoryId(categoryId);
		
		getSupportActionBar().setTitle(categoryName);
		
		if (downloadTask != null)
		{
			downloadTask.cancel(true);
			downloadTask = null;
		}
		
		if (menu.isMenuShowing())
		{
			menu.toggle();
		}
		
		progressBar.setVisibility(View.VISIBLE);
		
		adapter.clear();
		
		currentCategoryId = categoryId;
		ArrayList<WallpaperInfo> savedItems = WallpaperStore.getStore().getWallpapers(categoryId, -1, 100);
        for(int i =0; i < savedItems.size(); i++)
		{
			adapter.add(savedItems.get(i));
		}
		if (savedItems.size() == 0)
		{
			if (isInternetAvailable())
			{
				//ѕытаемс€ скачать из сети 
				downloadTask = new DownloadStringTask(null, new DownloadStringTask.TaskComplete() {
					
					@Override
					public void onComplete(String result) {
						progressBar.setVisibility(View.GONE);
						ArrayList<WallpaperInfo> items = WallpaperInfo.parseJsonString(result);
						if (items.size() > 0)
						{
							for(int i =0; i < items.size(); i++)
							{
								adapter.add(items.get(i));
								WallpaperStore.getStore().addWallpaper(items.get(i));
							}
							adapter.notifyDataSetChanged();
						}
					}
				});
				progressBar.setVisibility(View.VISIBLE);
				downloadTask.execute(getString(R.string.download_url).replace("#category_id#", String.valueOf(currentCategoryId)));
			}
			else
			{
				messageUtil.showShortMessage(getString(R.string.need_internet));
			}
		}
		else
		{
			adapter.notifyDataSetChanged();
			progressBar.setVisibility(View.GONE);
		}
		
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initControls();
        initImageLoader();
        startWallpaperChangeService();
         
        WallpaperStore.Initialize(getApplicationContext());
        
        int lastWallpaperId = WallpaperStore.getStore().getLastWallpaperId();
        
        adapter = new SimpleWallpaperAdapter(getApplicationContext());
        list.setAdapter(adapter);
		listGrid.setAdapter(adapter);
		
        if (lastWallpaperId > 0)
        {
			currentCategoryId = settings.getLastShowCategoryId(-1);
			
			loadAdapterByCategory(currentCategoryId, "");
        }
        else
        {
        	if (menu.isMenuShowing() == false)
    		{
    			menu.toggle();
    		}
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch(item.getItemId())
    	{
    	case R.id.show_as_list:
    		if (list.getVisibility() == View.GONE)
    		{
    			list.setVisibility(View.VISIBLE);
    			listGrid.setVisibility(View.GONE);
    		}
    		break;
    	case R.id.show_as_grid:
    		if (listGrid.getVisibility() == View.GONE)
    		{
    			listGrid.setVisibility(View.VISIBLE);
    			list.setVisibility(View.GONE);
    		}
    		break;
    	}
    	return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
    	super.onActivityResult(requestCode, resultCode, arg2);
    	
    	switch(requestCode)
    	{
	    	case REQUEST_CODE_WALLPAPER_ACTIVITY:
	    		switch(resultCode)
	    		{
	    		case WallpaperActivity.RESULT_CODE_CHANGE_FAVORITE:
	    			if (isLoadFavorite)
		    		{
		    			loadFavorite();
		    		}
	    			break;
	    		}
	    		break;
    	}
    }
    
    private class SimpleWallpaperAdapter extends ArrayAdapter<WallpaperInfo>
    {
		public SimpleWallpaperAdapter(Context context) {
			super(context, R.layout.list_item_view);
		}
    	
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_view, null);
			}
			ImageView imgThumb = (ImageView) convertView.findViewById(R.id.imgThumb);
			imgThumb.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					WallpaperInfo wp = (WallpaperInfo)v.getTag();
					Intent intent = new Intent(getApplicationContext(), WallpaperActivity.class);
					intent.putExtra("id", wp.getId());
					intent.putExtra("url", wp.getUrl());
					startActivityForResult(intent, REQUEST_CODE_WALLPAPER_ACTIVITY);
				}
			});
			WallpaperInfo wp = getItem(position);
			imgThumb.setTag(wp);
			ImageLoader.getInstance().displayImage(wp.getUrl(), imgThumb);
			
			return convertView;

		}
    }
}
