package com.ownsoft.narutowallpaper.utility;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.ProgressDialog;
import android.os.AsyncTask;


public class DownloadStringTask extends AsyncTask<String, Void, String> 
{
	ProgressDialog dialog;
	TaskComplete taskComplete;
	
	public DownloadStringTask(ProgressDialog progressDialog, TaskComplete taskComplete)
	{
		this.taskComplete = taskComplete;
		this.dialog = progressDialog;
	}
	
	@Override
	protected String doInBackground(String... urls) {
		StringBuilder inputStringBuilder = new StringBuilder();
		try 
		{
			URL url = new URL(urls[0]);
			URLConnection urlConnection = url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			
	        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
	        String line = bufferedReader.readLine();
	        while(line != null){
	            inputStringBuilder.append(line);
	            line = bufferedReader.readLine();
	        }
	        in.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		finally {

	    }
		return inputStringBuilder.toString();

	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if (dialog != null)
		{
			dialog.dismiss();
		}
		taskComplete.onComplete(result);
	}

	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();	
	}

	public interface TaskComplete
	{
		void onComplete(String result);
	}
}
