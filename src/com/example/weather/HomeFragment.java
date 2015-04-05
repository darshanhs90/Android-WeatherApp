package com.example.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;

import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anup.weather.maps.R;

public class HomeFragment extends Fragment  {

	TextView mScore, mDaily, mWeekly, mMonthly;
	ProgressBar mProgressBar;
	ProgressDialog mDialog;
	String url;
	String today;
	JSONParserTask mTask;
	Activity mActivity;
	String mData;
	public HomeFragment(Activity mActivity) {
		this.mActivity = mActivity;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		mDialog = new ProgressDialog(mActivity);  

		mProgressBar = (ProgressBar) rootView 
				.findViewById(R.id.circularProgressbar);

		mScore = (TextView) rootView.findViewById(R.id.score);
		mDaily = (TextView) rootView.findViewById(R.id.daily);



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
						LocationManager locationManager=(LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
						Location location = locationManager.getLastKnownLocation
								(LocationManager.GPS_PROVIDER);
						if(location==null)
							location=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if(location!=null){
							Log.d("lat",location.getLatitude()+"");
							Log.d("lat",location.getLongitude()+""); 	 
							String str="http://forecast.weather.gov/MapClick.php?lat="+location.getLatitude()+"&lon="+location.getLongitude()+"&site=okx&unit=0&lg=en&FcstType=json";
							Log.d("asd",str);  
							System.out.println(str);
							//Toast.makeText(getActivity(), str,Toast.LENGTH_LONG).show();
							mTask.execute(str);
							Log.d("qwe",mData+"");
						}
						else{
							Toast.makeText(getActivity(), "Enable Location Services",Toast.LENGTH_LONG).show();
						}
					}
				});

			}  
		});
		mThread.start();


		mTask = new JSONParserTask();

		return rootView;
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
								mDaily.setText("Today's Weather @ "+mData.split("/")[1]+" is "+mData.split("/")[0]);

							}
						});
					} 
					catch (Exception e) {
						e.printStackTrace();

					} finally {
						System.out.println("asd");
					}
				}
				else {
					mData = null;
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			finally{

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
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							mScore.setText(data.split("/")[0]+"");
						}
					});
					//
					new Progress().execute(today);
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
		String mScore = new String(); 
		JSONObject mResponseobject = new JSONObject(result);
		mScore=(mResponseobject.getJSONObject("currentobservation").getString("Temp"));
		String str=(mResponseobject.getJSONObject("currentobservation").getString("name"));
		Log.d("asd",str);
		today=mScore;
		return mScore+"/"+str;

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
