package com.pearlapps.gatorguards;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pearlapps.gatorguards.networkhandler.HttpRequest;
import com.pearlapps.gatorguards.utils.Cons;
import com.pearlapps.gatorguards.utils.DbHelperHG;

public class AddUser extends Activity implements OnClickListener{

	EditText edtUserId,edtUserName,edtLocation;
	Button btnAdd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);
		
		edtUserId=(EditText)findViewById(R.id.edtUserId);
		edtUserName=(EditText)findViewById(R.id.edtUserName);
		edtLocation=(EditText)findViewById(R.id.edtLocation);
		
		btnAdd=(Button)findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		AsyncPost execute=new AsyncPost();
		execute.execute();
		
	}
	private class AsyncPost extends AsyncTask<Void, Void, Void> {
		ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pDialog = new ProgressDialog(AddUser.this);
			pDialog.setCancelable(false);
			pDialog.setMessage("Syncing...");
			if (HttpRequest.hasConnection(AddUser.this)) {
				pDialog.show();
			} else {
				cancel(true);
			}
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub

			try {
						String user_id, user_name, user_location;
						user_id = edtUserId.getText().toString();
						user_name= edtUserName.getText().toString();
						user_location= edtLocation.getText().toString();
						try {
							String postBody = Cons.getUserBody(user_id, user_name, user_location);
							HttpRequest request = new HttpRequest();
							String result = request.makeServiceCall(Cons.URL_USER + "?"
									+ postBody, HttpRequest.POST);
							System.out.println("Result User: " + result);
						} catch (Exception e) {
							e.printStackTrace();
				}
			} catch (final Exception e) {
				AddUser.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(AddUser.this, e.getMessage(),
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
	
}
