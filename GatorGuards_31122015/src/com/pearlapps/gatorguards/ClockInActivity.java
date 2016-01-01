package com.pearlapps.gatorguards;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
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
import com.pearlapps.gatorguards.utils.DbHelperHG;
import com.pearlapps.gatorguards.utils.PrefHandler;

public class ClockInActivity extends Activity implements OnClickListener,
		CompleteTaskListner {
	// EmployerBean bean;
	TextView tvTime, tvDate, tvGo, tvStop, tvName, tvRecords, tvSync;
	Date dtNow;
	PrefHandler pref;

	public static final String URL = "https://docs.google.com/forms/d/12vCP6mUN6CnKRKbk3ghVlstUW8RYeYj8ugITXSzIbEo/formResponse";
	public static final String KEY_ID = "entry_1276988803";
	public static final String KEY_NAME = "entry_1833377572";
	public static final String KEY_LOCATION = "entry_1974637109";
	public static final String KEY_COST_CODES = "entry_1711576972";
	public static final String KEY_TIME_IN = "entry_960510735";
	public static final String KEY_TIME_OUR = "entry_1884756332";
	public static final String KEY_TOTAL_TIME = "entry_160537164";

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
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm");
		String timeNow = dateFormat.format(dtNow);
		tvTime.setText(timeNow);

		SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMM yy");
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
						id = cursor.getString(cursor
								.getColumnIndex(DbHelperHG.KEY_GID));
						name = cursor.getString(cursor
								.getColumnIndex(DbHelperHG.KEY_GNAME));
						location = cursor.getString(cursor
								.getColumnIndex(DbHelperHG.KEY_GLOCATION));
						cost_code = cursor.getString(cursor
								.getColumnIndex(DbHelperHG.KEY_GCOST_CODES));
						time_in = cursor.getString(cursor
								.getColumnIndex(DbHelperHG.KEY_GTIME_IN));
						time_out = cursor.getString(cursor
								.getColumnIndex(DbHelperHG.KEY_GTIME_OUT));
						total_time = cursor.getString(cursor
								.getColumnIndex(DbHelperHG.KEY_GTOTAL_TIME));
						try {
							
							HttpRequest request = new HttpRequest();
							String result = request.makeServiceCall(URL + "?"
									+ getBody(id, name, location, cost_code, time_in, time_out, total_time), HttpRequest.POST);
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

	@SuppressLint("NewApi")
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
			SimpleDateFormat df = new SimpleDateFormat("hh:mm EEEE, dd MMM yyyy");
			String dateNow = df.format(dtNow);
			int lastId = helper.getLastRecordId();
			if (lastId >= 0) {
				int success1 = helper.insertTimeOut(dateNow, lastId + "");
				int success2=-1;
				HashMap<String, String> inOut=helper.getLastTimeINOUT();
				if(inOut!=null)
				{
					Date dtIn = null,dtOut = null;
					try {
						dtIn=df.parse(inOut.get("in"));
						dtOut=df.parse(inOut.get("out"));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					long diff = dtOut.getTime() - dtIn.getTime();
					String totalTime=TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS)+"";
				    Toast.makeText(ClockInActivity.this,totalTime,Toast.LENGTH_LONG).show();
				    success2=helper.insertTotalTime(totalTime, lastId+"");
				}
				
				
				if (success1 == 1 && success2==1) {
					Toast.makeText(this, "Clocked Out Successfully",
							Toast.LENGTH_LONG).show();
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

	@Override
	public void completeTask(String result, int response_code) {
		// TODO Auto-generated method stub
		if (result != null && response_code == 1) {
			System.out.println("Respone : " + result);

		}
	}
	public static String getBody(String id,String name,String location,String cost_code,String time_in,String time_out,String total_time)
	{
		String postBody=null;
		try
		{
			postBody = KEY_ID + "="+ URLEncoder.encode(id, "UTF-8") + "&"
					+ KEY_NAME + "="+ URLEncoder.encode(name, "UTF-8") + "&"
					+ KEY_LOCATION + "="+ URLEncoder.encode(location, "UTF-8")+ "&" 
					+ KEY_COST_CODES + "="+ URLEncoder.encode(cost_code, "UTF-8")+ "&" 
					+ KEY_TIME_IN + "="+ URLEncoder.encode(time_in, "UTF-8") + "&"
					+ KEY_TIME_OUR + "="+ URLEncoder.encode(time_out, "UTF-8")+ "&"
					+ KEY_TOTAL_TIME + "="+ URLEncoder.encode(total_time, "UTF-8");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
			
		return postBody;
	}
}
