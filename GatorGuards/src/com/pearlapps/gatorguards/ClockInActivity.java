package com.pearlapps.gatorguards;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.pearlapps.gatorguards.networkhandler.CompleteTaskListner;
import com.pearlapps.gatorguards.networkhandler.HttpRequest;
import com.pearlapps.gatorguards.utils.Cons;
import com.pearlapps.gatorguards.utils.DbHelperHG;
import com.pearlapps.gatorguards.utils.PrefHandler;

public class ClockInActivity extends Activity implements OnClickListener{
	// EmployerBean bean;
	TextView tvTime, tvDate, tvGo, tvStop, tvName, tvRecords, tvSync;
	Date dtNow;
	PrefHandler pref;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clock_in);
		dtNow = new Date();
		pref = new PrefHandler(this);

		tvTime = (TextView) findViewById(R.id.tvTime);
		tvDate = (TextView) findViewById(R.id.tvDate);
		tvGo = (TextView) findViewById(R.id.tvGo);
		tvStop = (TextView) findViewById(R.id.tvStop);
		tvSync = (TextView) findViewById(R.id.tvSync);
		tvGo.setOnClickListener(this);
		tvStop.setOnClickListener(this);
		tvSync.setOnClickListener(this);
		tvName = (TextView) findViewById(R.id.tvName);
		tvName.setText(pref.getUserName());
		tvRecords = (TextView) findViewById(R.id.tvRecords);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		SimpleDateFormat dateFormat = new SimpleDateFormat(Cons.DFSTRING_HM);
		String timeNow = dateFormat.format(dtNow);
		tvTime.setText(timeNow);

		SimpleDateFormat df = new SimpleDateFormat(Cons.DFSTRING_DISPLAY);
		String dateNow = df.format(dtNow);
		tvDate.setText(dateNow);
		super.onResume();
	}

	private class AsyncPost extends AsyncTask<Void, Void, Void> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(ClockInActivity.this);
			pDialog.setCancelable(false);
			pDialog.setMessage("Syncing...");
			if (HttpRequest.hasConnection(ClockInActivity.this)) {
				pDialog.show();
			} else {
				cancel(true);
			}
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			DbHelperHG helper = new DbHelperHG(ClockInActivity.this);
			Cursor cursor = helper.getAllRecords();
			try {
				if (cursor.getCount() > 0) {

					do {
						String id, name, location, cost_code, time_in, time_out,total_time;
						id = cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GID));
						name = cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GNAME));
						location = cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GLOCATION));
						cost_code = cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GCOST_CODES));
						time_in = Cons.changeDateFormat(cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GTIME_IN)));
						time_out = Cons.changeDateFormat(cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GTIME_OUT)));
						total_time = cursor.getString(cursor.getColumnIndex(DbHelperHG.KEY_GTOTAL_TIME));
						
						System.out.println(time_in+" , "+time_out);
						
						try {
							String postBody = Cons.getBody(id, name, location,cost_code, time_in, time_out,total_time);
							HttpRequest request = new HttpRequest();
							String result = request.makeServiceCall(Cons.URL + "?"
									+ postBody, HttpRequest.POST);
							System.out.println("Result : " + result);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} while (cursor.moveToNext());
				}
			} catch (final Exception e) {
				ClockInActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(ClockInActivity.this, e.getMessage(),
								Toast.LENGTH_LONG).show();
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
		this.finish();
		overridePendingTransition(R.anim.l_to_r_in, R.anim.l_to_r_out);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.tvGo:
			Intent intent = new Intent(this, LocationActivity.class);
			intent.putExtra("dtNow", dtNow);
			startActivity(intent);
			overridePendingTransition(R.anim.r_to_l_in, R.anim.r_to_l_out);
			break;
		case R.id.tvStop:
			PrefHandler pref = new PrefHandler(this);
			DbHelperHG helper = new DbHelperHG(this);
			dtNow = new Date();
			SimpleDateFormat df = new SimpleDateFormat(Cons.DFSTRING_HM+Cons.DFSTRING_DISPLAY);
			String dateNow = df.format(dtNow);
			int lastId = helper.getLastRecordId();
			String inTime=helper.getLastInTime(""+lastId);
			int hours=0;
			try
			{
				Date dtIn=df.parse(inTime);
				long diff=dtNow.getTime()-dtIn.getTime();
				hours=(int) (diff/(1000 * 60 * 60));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
			if (lastId >= 0) {
				int success = helper.insertTimeOut(dateNow, lastId + "",""+hours);
				if (success == 1) {
					Toast.makeText(this, "Clocked Out Successfully",Toast.LENGTH_LONG).show();
					/*pref.getUserId();
					pref.getUserName();
					*/
//					pref.clearData();
					/*startActivity(new Intent(this, MainActivity.class));
					this.finish();*/
				} else {
					Toast.makeText(this, "fail", Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.tvSync:
			// Toast.makeText(this, "sync", Toast.LENGTH_LONG).show();
			AsyncPost post = new AsyncPost();
			post.execute();
			break;
		default:
			break;
		}

	}
}
