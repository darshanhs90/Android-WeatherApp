package com.example.weather;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.anup.weather.maps.R;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Search extends Fragment implements OnClickListener{
	TextView tvLocation,mScore;
	ProgressBar mProgressBar;
	ProgressDialog mDialog;
	String url;
	String today,location,temp, mData;

	JSONParserTask mTask;
	Activity mActivity;
	EditText etLocation;
	Button bnWeatherSearch;


	public Search(Activity activity) {
		// TODO Auto-generated constructor stub
		this.mActivity=activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_search, container, false);
		//when user enters submit
		//send api request to yahoo server to get weather of that location 
		//and display it down
		etLocation=(EditText) rootView.findViewById(R.id.etLocation);
		mDialog = new ProgressDialog(mActivity);  
		tvLocation=(TextView) rootView.findViewById(R.id.tvLocation);
		bnWeatherSearch=(Button) rootView.findViewById(R.id.bnWeatherSearch);
		mProgressBar = (ProgressBar) rootView 
				.findViewById(R.id.progressBar1);
		mScore=(TextView) rootView.findViewById(R.id.score);
		bnWeatherSearch.setOnClickListener(this);
		return rootView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v.getId()==R.id.bnWeatherSearch){
			if(etLocation.getText()!=null ||!etLocation.getText().equals("")||!etLocation.getText().equals("Enter Location")){

				Thread mThread = new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						mActivity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub
								String str="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20weather.forecast%20where%20woeid%20in%20(select%20woeid%20from%20geo.places(1)%20where%20text%3D%22"+etLocation.getText().toString()+"%22)&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys";
								Log.d("asd",str);  
								mTask.execute(str);

							}
						});

					}  
				});
				mThread.start();

				mTask = new JSONParserTask();
				//Log.d("asddd",mData); 
			}

		}
	}
	class JSONParserTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			mDialog.setCancelable(false);
			mDialog.setMessage("Fetching data...");
			mDialog.show();

		}

		@Override
		protected String doInBackground(String... params) {
			String jsonString = null;
			mData = null;
			try {

				URL obj = new URL(params[0]);
				HttpURLConnection con = (HttpURLConnection) obj
						.openConnection();

				con.setRequestMethod("GET");
				con.setReadTimeout(15 * 1000);
				con.connect();


				BufferedReader reader = new BufferedReader(
						new InputStreamReader(con.getInputStream(), "utf-8"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
					Log.d("asd",line);
				}
				jsonString = sb.toString();

				if (true) {
					try {
						mData = parseJsonNews(jsonString);
						mActivity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub

								mScore.setText(mData);
							}
						});
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();

					}
				} else {
					mData = null;
				}

			} catch (Exception e) {
				e.printStackTrace();

			} finally {

			}
			return mData;
		}

		@Override
		protected void onPostExecute(final String data) {
			if(data!=null)
			{
				mDialog.dismiss();
				try {
					Log.d("asd","sc"+data); 
					tvLocation.setText("Your Location is :"+location);
					new Progress().execute(temp);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				mDialog.dismiss(); 
				Toast.makeText(mActivity, "Today's data not availiable", Toast.LENGTH_LONG).show();
			}

		}

	}

	public String parseJsonNews(String result) throws JSONException {
		JSONObject mResponseobject = new JSONObject(result);
		location=(mResponseobject.getJSONObject("query").getJSONObject("results")
				.getJSONObject("channel").getJSONObject("location").getString("city"));
		temp=(mResponseobject.getJSONObject("query").getJSONObject("results")
				.getJSONObject("channel").getJSONObject("item")
				.getJSONObject("condition").getString("temp"));
		Log.d("asdd",location);
		Log.d("asdd",temp);

		//this.mScore.setText(temp);
		return temp;

	}



	public class Progress extends AsyncTask<String, String, Void> {

		@Override
		protected Void doInBackground(String... params) {

			if (mProgressBar.getProgress() > Integer.parseInt(params[0])) {
				while (mProgressBar.getProgress() > Float.parseFloat(params[0])) {
					publishProgress("1");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				while (mProgressBar.getProgress() < Integer.parseInt(params[0])) {
					publishProgress("-1");
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			if (Integer.parseInt(values[0]) > 0) {
				mProgressBar.incrementProgressBy(-1);
			} else {
				mProgressBar.incrementProgressBy(1);
			}

		}
	}
}


