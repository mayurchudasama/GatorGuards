package com.pearlapps.gatorguards;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password and via Google+ sign in.
 * <p/>
 * ************ IMPORTANT SETUP NOTES: ************ In order for Google+ sign in
 * to work with your app, you must first go to:
 * https://developers.google.com/+/mobile
 * /android/getting-started#step_1_enable_the_google_api and follow the steps in
 * "Step 1" to create an OAuth 2.0 client for your package.
 */
public class LoginActivity extends Activity implements OnClickListener{

	EditText edtUserName,edtPassword;
	Button btnSignIn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_screen);
		
		edtUserName=(EditText)findViewById(R.id.edtUserName);
		edtPassword=(EditText)findViewById(R.id.edtpassword);
		btnSignIn=(Button)findViewById(R.id.btnsignin);
		btnSignIn.setOnClickListener(this);

	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(edtUserName.getText().toString().equals("1234") && edtPassword.getText().toString().equals("1234"))
		{
//			Toast.makeText(this, "Login Successfull", Toast.LENGTH_LONG).show();
			startActivity(new Intent(this,AddUser.class));
		}
	}

}
