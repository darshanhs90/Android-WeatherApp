/*package com.example.weather;

import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.anup.weather.maps.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class CopyOftempd extends Fragment {

	// Google Map
	private GoogleMap googleMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
				Toast.makeText(getApplicationContext(), "" + value.latitude + "   "
						+ value.longitude, Toast.LENGTH_LONG).show();

			}
		});

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
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						LocationManager locationManager=(LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
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
						}
					}
				});

			}  
		});
		mThread.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initilizeMap();
	}

	*//**
	 * function to load map If map is not created it will create it for you
	 * *//*
	private void initilizeMap() {
		if (googleMap == null) {
			googleMap = ((MapFragment) getFragmentManager().findFragmentById(
					R.id.map)).getMap();

			// check if map is created successfully or not
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(),
						"Sorry! unable to create maps", Toast.LENGTH_SHORT)
						.show();
			}
		}
	}

	
	 * creating random postion around a location for testing purpose only
	 
	private double[] createRandLocation(double latitude, double longitude) {

		return new double[] { latitude + ((Math.random() - 0.5) / 500),
				longitude + ((Math.random() - 0.5) / 500),
				150 + ((Math.random() - 0.5) * 10) };
	}
}
*/