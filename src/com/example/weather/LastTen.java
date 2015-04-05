package com.example.weather;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.anup.weather.maps.R;

public class LastTen extends Fragment {
	ProgressDialog mDialog;
	String url;
	String today;
	JSONParserTask mTask;
	Activity mActivity;
	ListView listView;
	ArrayAdapter<String> str;
	JSONArray tempLabel,temperature,weather,link,days;
	public LastTen(Activity mActivity) {
		this.mActivity = mActivity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_lastten, container,
				false);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
		listView=(ListView) rootView.findViewById(R.id.listView1);
		mDialog=new ProgressDialog(mActivity);
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
							location = locationManager.getLastKnownLocation
							(LocationManager.NETWORK_PROVIDER);
						if(location!=null){
							Log.d("lat",location.getLatitude()+"");
							Log.d("lat",location.getLongitude()+""); 	
							String str="http://forecast.weather.gov/MapClick.php?lat="+location.getLatitude()+"&lon="+location.getLongitude()+"&site=okx&unit=0&lg=en&FcstType=json";
							Log.d("asd",str);  
							System.out.println(str);
							//Toast.makeText(getActivity(), str,Toast.LENGTH_LONG).show();
							mTask.execute(str);
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
			String mData = null;
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
					String[] text=new String[tempLabel.length()+1];
					String[] image=new String[tempLabel.length()+1];

					for (int i = 0; i < tempLabel.length(); i++) {

						try {
							text[i]=days.get(i)+" "+tempLabel.get(i)+" temperature is "+temperature.get(i)+"";

							image[i]=(String) link.get(i);
							Log.d("asdf",text[i]);
							Log.d("asdf",image[i]);

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					listView.setAdapter(new CustomAdapter((MainActivity) mActivity, text, image));

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
		tempLabel=(mResponseobject.getJSONObject("time").getJSONArray("tempLabel"));
		Log.d("label",tempLabel.get(0)+"");
		temperature=(mResponseobject.getJSONObject("data").getJSONArray("temperature"));
		Log.d("temp",temperature+"");
		weather=(mResponseobject.getJSONObject("data").getJSONArray("weather"));
		Log.d("weather",weather+"");
		days=(mResponseobject.getJSONObject("time").getJSONArray("startPeriodName"));
		Log.d("time",days+"");
		link=(mResponseobject.getJSONObject("data").getJSONArray("iconLink"));
		Log.d("link",link+"");
		return "";

	}

}