package com.pearlapps.gatorguards.networkhandler;

import java.util.List;

import org.apache.http.NameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.widget.Toast;

public class AsyncHttpRequest extends AsyncTask<String, Void, String> {
	ProgressDialog dialog;
	Context context;
	CompleteTaskListner listener;
	int response_code;
	int request_method;
	List<NameValuePair> listParams;
	public AsyncHttpRequest(Context context,List<NameValuePair> listParams,Object class_name,int res_code,int request_method) {
		this.context = context;
		listener = (CompleteTaskListner) class_name;
		this.response_code=res_code;
		this.request_method=request_method;
		this.listParams=listParams;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		if (HttpRequest.hasConnection(context)) {
			dialog = new ProgressDialog(context);
			dialog.setMessage("loading");
			dialog.setOnCancelListener(new OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					dialog.dismiss();
					cancel(true);
					Toast.makeText(context, "request cancelled by user!",
							Toast.LENGTH_LONG).show();
				}
			});
			dialog.show();
		} else {
			cancel(true);
			Toast.makeText(context, HttpRequest.NETWORK_MESSAGE,
					Toast.LENGTH_LONG).show();
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		String result = null;
		try {
			HttpRequest handler = new HttpRequest();
			System.out.println("Response : url : "+params[0]);
			result = handler.makeServiceCall(params[0],request_method,listParams);
		} catch (final Exception e) {
			((Activity) context).runOnUiThread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					cancel(true);
					Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG)
							.show();
				}
			});
		}
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		if (dialog.isShowing()) {
			dialog.dismiss();
		}
		if (result != null) {
			System.out.println("Response from Async: " + result);
			listener.completeTask(result,response_code);
		}
		super.onPostExecute(result);
	}
}
