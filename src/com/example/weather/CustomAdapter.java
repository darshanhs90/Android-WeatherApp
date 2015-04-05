package com.example.weather;

import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.anup.weather.maps.R;
public class CustomAdapter extends BaseAdapter{   
	String [] result;
	Context context;
	String [] imageId;
	private static LayoutInflater inflater=null;
	public CustomAdapter(MainActivity mainActivity, String[] prgmNameList, String[] prgmImages) {
		// TODO Auto-generated constructor stub
		result=prgmNameList;
		context=mainActivity;
		imageId=prgmImages;
		inflater = ( LayoutInflater )context.
				getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return result.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class Holder
	{
		TextView tv;
		ImageView img;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder=new Holder();
		View rowView;       
		rowView = inflater.inflate(R.layout.program_list, null);
		holder.tv=(TextView) rowView.findViewById(R.id.textView1);
		holder.img=(ImageView) rowView.findViewById(R.id.imageView1);       
		holder.tv.setText(result[position]);
		URL newurl;
		try {
			newurl = new URL(imageId[position]);
			Bitmap mIcon_val = BitmapFactory.decodeStream(newurl.openConnection() .getInputStream()); 
			holder.img.setImageBitmap(mIcon_val);     
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		rowView.setOnClickListener(new OnClickListener() {            
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(context, ""+result[position], Toast.LENGTH_LONG).show();
			}
		});   
		return rowView;
	}

}