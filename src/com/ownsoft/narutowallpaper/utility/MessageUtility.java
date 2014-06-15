package com.ownsoft.narutowallpaper.utility;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class MessageUtility 
{
	Context context;
	public MessageUtility(Context context)
	{
		this.context = context;
	}
	
	public void showShortMessage(String message)
	{
		Toast toast = Toast.makeText(this.context, message, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}

}
