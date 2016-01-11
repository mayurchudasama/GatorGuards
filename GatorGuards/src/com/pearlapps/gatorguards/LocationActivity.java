package com.pearlapps.gatorguards;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.pearlapps.gatorguards.utils.GPSTracker;
import com.pearlapps.gatorguards.utils.MyLocation;
import com.pearlapps.gatorguards.utils.MyLocation.LocationResult;
import com.pearlapps.gatorguards.utils.NetworkConnection;

public class LocationActivity extends Activity implements OnClickListener,OnItemClickListener{

	TextView tvCancel,tvSync;
	EditText edtSearch;
	ListView lvData;
	ArrayList<String> aListLocation;
	ArrayList<String> aListSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_location);
		
		edtSearch=(EditText)findViewById(R.id.edtSearch);
		aListLocation=new ArrayList<String>();
		aListSearch=new ArrayList<String>();
		aListLocation.add("Webb Plaza");
		aListLocation.add("The Getaway");
		aListLocation.add("Urban Style Flats");
		aListLocation.add("Burlington Gardens");
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.row_adapter, R.id.tvRow, aListLocation);
		lvData=(ListView)findViewById(R.id.lvData);
		lvData.setAdapter(adapter);
		lvData.setOnItemClickListener(this);
		tvCancel=(TextView)findViewById(R.id.tvCancel);
		tvSync=(TextView)findViewById(R.id.tvSync);
		tvCancel.setOnClickListener(this);
		tvSync.setOnClickListener(this);
		
		edtSearch.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String text = edtSearch.getText().toString().toLowerCase(Locale.getDefault());
				text=text.toLowerCase();
				
				if(text.length()>0)
				{
					aListSearch.clear();
					for(int i=0;i<aListLocation.size();i++)
					{
						String strSearch=aListLocation.get(i);
						strSearch=strSearch.toLowerCase();
						if(strSearch.contains(text))
						{
							aListSearch.add(strSearch);
						}
					}
					
					ArrayAdapter<String> adapter=new ArrayAdapter<String>(LocationActivity.this, R.layout.row_adapter, R.id.tvRow, aListSearch);
					lvData.setAdapter(adapter);
				}
				else
				{
					ArrayAdapter<String> adapter=new ArrayAdapter<String>(LocationActivity.this, R.layout.row_adapter, R.id.tvRow, aListLocation);
					lvData.setAdapter(adapter);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		com.pearlapps.gatorguards.utils.MyLocation myLocation = new MyLocation();
		myLocation.getLocation(this, locationResult);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvSync:
			Toast.makeText(this, "sync", Toast.LENGTH_LONG).show();
			break;
		case R.id.tvCancel:
			goToPreviousActivity();
			break;

		default:
			break;
		}
	}
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
//			locationText.setText(addressis);
			Toast.makeText(LocationActivity.this, addressis, Toast.LENGTH_LONG).show();
		}
	};
	private Handler handler = new Handler();
	String addressis="";
	public LocationResult locationResult = new LocationResult() {

		@Override
		public void gotLocation(Location location) {
			if (location != null) {

				GPSTracker gps=new GPSTracker(LocationActivity.this);
				double longi = gps.getLatitude();
				double lati = gps.getLongitude();


				Geocoder geocoder = new Geocoder(LocationActivity.this,
						Locale.getDefault());
				try {
					if (NetworkConnection.hasConnection(LocationActivity.this)) {
						List<Address> addresses = geocoder.getFromLocation(
								longi, lati, 1);

						if (addresses.size() > 0) {
							Address address = addresses.get(0);

//							addressis = address.getLocality() + ","+ address.getAdminArea();
							addressis=address.getAdminArea()+", "+address.getCountryName();
							System.out.println("Const : " + addressis);
							handler.post(mUpdateTimeTask);
							
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
			}
		}

	};
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		goToPreviousActivity();
	}
	private void goToPreviousActivity() {
		Intent intent=new Intent(this,ClockInActivity.class);
		startActivity(new Intent(this,ClockInActivity.class));
		overridePendingTransition(R.anim.l_to_r_in,R.anim.l_to_r_out);
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String location="";
		if(edtSearch.length()>0)
		{
			location=aListSearch.get(position);
		}
		else
		{
			location=aListLocation.get(position);
		}
		location+=", "+addressis;
		goToNextActivity(location);
		
	}
	private void goToNextActivity(String location) {
		Intent intent=new Intent(this,CostCodesActivity.class);
		intent.putExtra("dtNow", getIntent().getSerializableExtra("dtNow"));
		intent.putExtra("location", location);
		startActivity(intent);
		overridePendingTransition(R.anim.r_to_l_in,R.anim.r_to_l_out);
	}
}
