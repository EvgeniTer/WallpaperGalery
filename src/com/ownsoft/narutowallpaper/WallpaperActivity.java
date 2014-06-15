package com.ownsoft.narutowallpaper;

import java.io.IOException;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ownsoft.narutowallpaper.entity.WallpaperInfo;
import com.ownsoft.narutowallpaper.store.WallpaperStore;
import com.ownsoft.narutowallpaper.utility.BitmapUtility;

import android.os.Bundle;
import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import uk.co.senab.photoview.*;

public class WallpaperActivity extends ActionBarActivity {

	ImageView img;
	ProgressBar progressBar;
	Bitmap bm;
	PhotoViewAttacher mAttacher;
	WallpaperInfo wallpaperInfo;
	Menu optionMenu;
	
	public static final int RESULT_CODE_CHANGE_FAVORITE = 1;
	
	boolean changeFavorite = false;
	@Override
	public void finish() {
		super.finish();
		
		if (changeFavorite)
			this.setResult(RESULT_CODE_CHANGE_FAVORITE);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wallpaper);
		
		
		wallpaperInfo = WallpaperStore.getStore().getWallpaper(getIntent().getIntExtra("id", -1));
		
		img = (ImageView)findViewById(R.id.img);
		progressBar = (ProgressBar)findViewById(R.id.progressBarLoad);
		progressBar.setVisibility(View.VISIBLE);
		setTitle(getString(R.string.wallpaper));
		
		String imageUri = BitmapUtility.getSavedFilePath(wallpaperInfo.getFileNameForSave());
		if (imageUri == null || imageUri.length() == 0)
			imageUri = wallpaperInfo.getUrl();
		
		ImageLoader.getInstance().displayImage(imageUri, img, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				bm = loadedImage;
				progressBar.setVisibility(View.GONE);
				((ImageView)view).setImageBitmap(loadedImage);
				mAttacher = new PhotoViewAttacher(((ImageView)view));
				mAttacher.setOnMatrixChangeListener(new PhotoViewAttacher.OnMatrixChangedListener() {
					
					@Override
					public void onMatrixChanged(RectF rect) {
						float[] matrixArray = new float[9];
						mAttacher.getDisplayMatrix().getValues(matrixArray);
						WallpaperStore.getStore().saveWallpaperMatrix(wallpaperInfo.getId(), matrixArray);
					}
				});
				mAttacher.setZoomable(true);
				if (wallpaperInfo.getMatrix() != null && wallpaperInfo.getMatrix().length() > 0)
				{
					Matrix matrix = new Matrix();
					matrix.setValues(WallpaperInfo.getMatrixFromString(wallpaperInfo.getMatrix()));
					mAttacher.setDisplayMatrix(matrix);
				}
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void updateOptionsMenu()
	{
		optionMenu.findItem(R.id.add_to_favorite).setVisible(wallpaperInfo.getIsFavorite() == false);
		optionMenu.findItem(R.id.remove_from_favorite).setVisible(wallpaperInfo.getIsFavorite() == true);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.wallpaper, menu);
		this.optionMenu = menu;
		updateOptionsMenu();
		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId())
		{
		case R.id.set_as_wallpaper:

			if (bm != null)
			{
				try {
					
					Bitmap bm1 =  Bitmap.createBitmap(bm, 0, 0,
	                          bm.getWidth(), bm.getHeight(), mAttacher.getDisplayMatrix(), true);
					
					WallpaperManager.getInstance(getApplicationContext()).setBitmap(bm1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case R.id.add_to_favorite:
			wallpaperInfo.setIsFavorite(true);
			WallpaperStore.getStore().addToFavorite(wallpaperInfo.getId());
			BitmapUtility.saveBitmap(bm, wallpaperInfo.getFileNameForSave());
			changeFavorite = true;
			break;
		case R.id.remove_from_favorite:
			WallpaperStore.getStore().removeFromFavorite(wallpaperInfo.getId());
			wallpaperInfo.setIsFavorite(false);
			changeFavorite = true;
			break;
		}
		updateOptionsMenu();
		return super.onContextItemSelected(item);
	}
}
