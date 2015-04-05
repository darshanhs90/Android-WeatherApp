package com.example.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.anup.weather.maps.R;
import com.example.weather.HomeFragment.Progress;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class tempd extends Fragment {

	// Google Map
	private GoogleMap googleMap;
	String mData,str;
	Activity mActivity;
	JSONParserTask mTask;
	public tempd(Activity mActivity) {
		this.mActivity = mActivity;
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_main, container,
				false);

		try {
			// Loading map
			initilizeMap();

			// Changing map type
			googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
			// googleMap.setMapType(GoogleMap.MAP_TYPE_NONE);

			// Showing / hiding your current location
			googleMap.setMyLocationEnabled(true);

			// Enable / Disable zooming controls
			googleMap.getUiSettings().setZoomControlsEnabled(false);

			// Enable / Disable my location button
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);

			// Enable / Disable Compass icon
			googleMap.getUiSettings().setCompassEnabled(true);

			// Enable / Disable Rotate gesture
			googleMap.getUiSettings().setRotateGesturesEnabled(true);

			// Enable / Disable zooming functionality
			googleMap.getUiSettings().setZoomGesturesEnabled(true);

			// lets place some 10 random markers

		} catch (Exception e) {
			e.printStackTrace();
			
			
		}

		googleMap.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng value) {
				// TODO Auto-generated method stub
				Log.i("anup_check", "" + value.latitude + "   "
						+ value.longitude);
				//Toast.makeText(mActivity.getApplicationContext(), "" + value.latitude + "   "+ value.longitude, Toast.LENGTH_LONG).show();
				 str="http://forecast.weather.gov/MapClick.php?lat="+value.latitude+"&lon="+value.longitude+"&site=okx&unit=0&lg=en&FcstType=json";
				 mTask = new JSONParserTask();
				 mTask.execute(str);

			}
		});
		return rootView;
	}
	
	
	void startthread()
	{
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
						/*LocationManager locationManager=(LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
						Location location = locationManager.getLastKnownLocation
								(LocationManager.GPS_PROVIDER);
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
							Toast.makeText(this, "Enable Location Services",Toast.LENGTH_LONG).show();
						}*/
						
					}
				});

			}  
		});
		mThread.start();
	}

	

	/**
	 * function to load map If map is not created it will create it for you
	 * */
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(mActivity.getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	/*
	 * creating random postion around a location for testing purpose only
	 */
	private double[] createRandLocation(double latitude, double longitude) {

		return new double[] { latitude + ((Math.random() - 0.5) / 500),
				longitude + ((Math.random() - 0.5) / 500),
				150 + ((Math.random() - 0.5) * 10) };
	}
	class JSONParserTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {
			
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
								//mDaily.setText("Today's Weather @ "+mData.split("/")[1]+" is "+mData.split("/")[0]);
								Toast.makeText(mActivity.getApplicationContext(), "Weather @ "+mData.split("/")[1]+" is "+mData.split("/")[0], Toast.LENGTH_LONG).show();
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
				//mDialog.dismiss();
				try {
					Log.d("asd","sc"+data); 
					mActivity.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							//mScore.setText(data.split("/")[0]+"");
						}
					});
					//
					//new Progress().execute(today);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				//mDialog.dismiss(); 
				Toast.makeText(mActivity, "Data not availiable", Toast.LENGTH_LONG).show();
			}

		}

	}

	public String parseJsonNews(String result) throws JSONException {
		String mScore = new String(); 
		JSONObject mResponseobject = new JSONObject(result);
		mScore=(mResponseobject.getJSONObject("currentobservation").getString("Temp"));
		String str=(mResponseobject.getJSONObject("currentobservation").getString("name"));
		Log.d("asd",str);
		//today=mScore;
		return mScore+"/"+str;

	}

}
