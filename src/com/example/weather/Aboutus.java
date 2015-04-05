package com.example.weather;
import com.android.anup.weather.maps.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Aboutus extends Fragment {
	Activity mActivity;
        public Aboutus(Activity activity) {
			// TODO Auto-generated constructor stub
        	this.mActivity=activity;
		}

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_aboutus, container, false);
            return rootView;
        }
    }
