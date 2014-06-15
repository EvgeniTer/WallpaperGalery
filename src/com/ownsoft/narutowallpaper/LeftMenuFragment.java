package com.ownsoft.narutowallpaper;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.ownsoft.narutowallpaper.entity.CategoryEntity;
import com.ownsoft.narutowallpaper.store.WallpaperStore;
import com.ownsoft.narutowallpaper.utility.DownloadStringTask;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LeftMenuFragment extends Fragment {
	
	View currentView;

	
	ListView list;
	
	DownloadStringTask downloadTask;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		currentView = inflater.inflate(R.layout.left_menu, null);
		return currentView;
	}
	
	public void startDownloadCategories()
	{

		downloadTask = new DownloadStringTask(null, new DownloadStringTask.TaskComplete() {
			
			@Override
			public void onComplete(String result) {
				
				ArrayList<CategoryEntity> categories = CategoryEntity.parseJsonString(result);
				
				for(int i =0; i < categories.size(); i++)
				{
					WallpaperStore.getStore().addCategory(categories.get(i));
				}
				
				setAdapterData(categories);
			}
		});
		
		downloadTask.execute(getString(R.string.download_categories_url));
	}

	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		

		list = (ListView)currentView.findViewById(R.id.list);
		TextView favoriteTitle = (TextView)currentView.findViewById(R.id.favoriteTitle);
		favoriteTitle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				loadFavoriteWallpapers();
			}
		});
		
		ArrayList<CategoryEntity> categories = WallpaperStore.getStore().getCategories();
		if (categories.size() == 0)
		{
			startDownloadCategories();
		}
		else
		{
			setAdapterData(categories);
		}
		
		list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				loadWallpapersByCategory(
						((CategoryEntity)view.getTag()).getId(),
						((CategoryEntity)view.getTag()).getLocaleName(getResources().getConfiguration().locale)
						);
			}
		});
	}
	
	private void loadFavoriteWallpapers()
	{
		((MainActivity)this.getActivity()).loadFavorite();
	}
	private void loadWallpapersByCategory(int categoryId, String categoryName)
	{
		((MainActivity)this.getActivity()).loadAdapterByCategory(categoryId, categoryName);
	}
	
	private void setAdapterData(ArrayList<CategoryEntity> objects)
	{
		SampleAdapter adapter = new SampleAdapter(getActivity());
		for (int i = 0; i < objects.size(); i++) {
			adapter.add(objects.get(i));
		}
		list.setAdapter(adapter);
	}

	public class SampleAdapter extends ArrayAdapter<CategoryEntity> {

		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.left_menu_item, null);
			}
			convertView.setTag(getItem(position));
			TextView title = (TextView) convertView.findViewById(R.id.title);
			title.setText(getItem(position).getLocaleName(getResources().getConfiguration().locale));

			ImageView img = (ImageView)convertView.findViewById(R.id.img);
			
			if (getItem(position).getImageUrl().length() > 0)
			{
				ImageLoader.getInstance().displayImage(getItem(position).getImageUrl(), img);
			}
			else
			{
				img.setVisibility(View.INVISIBLE);
			}
			
			return convertView;
		}

	}


}
