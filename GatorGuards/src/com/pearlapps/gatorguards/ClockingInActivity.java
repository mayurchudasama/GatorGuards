package com.pearlapps.gatorguards;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.pearlapps.gatorguards.networkhandler.HttpRequest;
import com.pearlapps.gatorguards.utils.DbHelperHG;
import com.pearlapps.gatorguards.utils.PrefHandler;

public class ClockingInActivity extends Activity implements OnClickListener{

	TextView tvTimeDate,tvName,tvLocation,tvCostCodes;
	String id;
	DbHelperHG helper;
	PrefHandler pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clocking_in);
		pref=new PrefHandler(this);
		helper=new DbHelperHG(this);
		tvTimeDate=(TextView)findViewById(R.id.tvTimeDate);
		tvName=(TextView)findViewById(R.id.tvName);
		tvLocation=(TextView)findViewById(R.id.tvLocation);
		tvCostCodes=(TextView)findViewById(R.id.tvCostCodes);
		
		Date dtNow=(Date) getIntent().getSerializableExtra("dtNow");
				
		SimpleDateFormat df = new SimpleDateFormat(ClockInActivity.DFSTRING_HM+ClockInActivity.DFSTRING_DISPLAY);
		String timeDate=df.format(dtNow);
		tvTimeDate.setText(timeDate);
		
		tvName.setText(pref.getUserName());
		tvLocation.setText(getIntent().getStringExtra("location"));
		tvCostCodes.setText(getIntent().getStringExtra("costcode"));
		id=String.valueOf(pref.getUserId());
		
		findViewById(R.id.imgDone).setOnClickListener(this);
		
		
		int success=helper.insertAudioRecord(id, tvName.getText().toString(), tvLocation.getText().toString(), tvCostCodes.getText().toString(), tvTimeDate.getText().toString(), "","");
		if(success==1)
		{
			System.out.println("LoggedIn Successfully");
//			Toast.makeText(this, "Clocked In Successfully", Toast.LENGTH_LONG).show();
			pref.setUserClockedIn(true);
		}
		else
		{
			Log.e("Error inserting record in database", "Error");
		}
		
		findViewById(R.id.tvSync).setOnClickListener(onClickListener);
	}
	OnClickListener onClickListener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			AsyncPost post=new AsyncPost();
			post.execute();
		}
	};
	private class AsyncPost extends AsyncTask<Void, Void, Void> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(ClockingInActivity.this);
			pDialog.setCancelable(false);
			pDialog.setMessage("Syncing...");
			if (HttpRequest.hasConnection(ClockingInActivity.this)) {
				pDialog.show();
			} else {
				cancel(true);
			}
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			DbHelperHG helper=new DbHelperHG(ClockingInActivity.this);
			Cursor cursor=helper.getAllRecords();
			try
			{
				if(cursor.getCount()>0)
				{
				
					do
					{
						String id,name,location,cost_code,time_in,time_out,total_time;
						id=cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GID));
						name=cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GNAME));
						location=cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GLOCATION));
						cost_code=cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GCOST_CODES));
						time_in = ClockInActivity.changeDateFormat(cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GTIME_IN)));
						time_out =ClockInActivity.changeDateFormat(cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GTIME_OUT)));
						total_time=cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GTOTAL_TIME));
						try {
								String postBody = ClockInActivity.getBody(id, name, location,cost_code, time_in, time_out,total_time);
								HttpRequest request = new HttpRequest();
								String result = request.makeServiceCall(ClockInActivity.URL + "?" + postBody,
										HttpRequest.POST);
								System.out.println("Result : " + result);
							} catch (Exception e) {
								e.printStackTrace();
							}
					}
					while(cursor.moveToNext());
				}
			}
			catch(final Exception e)
			{
				ClockingInActivity.this.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
					Toast.makeText(ClockingInActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
					}
				});
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (pDialog.isShowing())
				pDialog.dismiss();
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		Intent intent=new Intent(this,CostCodesActivity.class);
		intent.putExtra("dtNow", getIntent().getSerializableExtra("dtNow"));
		intent.putExtra("location", getIntent().getSerializableExtra("location"));
		startActivity(intent);
		overridePendingTransition(R.anim.l_to_r_in, R.anim.l_to_r_out);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imgDone:
			/*int success=helper.insertAudioRecord(id, tvName.getText().toString(), tvLocation.getText().toString(), tvCostCodes.getText().toString(), tvTimeDate.getText().toString(), "");
			if(success==1)
			{
				System.out.println("LoggedIn Successfully");
				Toast.makeText(this, "Clocked In Successfully", Toast.LENGTH_LONG).show();
				pref.setUserClockedIn(true);*/
			Toast.makeText(this, "Clocked In Successfully", Toast.LENGTH_LONG).show();
			/*}
			else
			{
				Log.e("Error inserting record in database", "Error");
			}*/
			break;

		default:
			break;
		}
		
	}
}
