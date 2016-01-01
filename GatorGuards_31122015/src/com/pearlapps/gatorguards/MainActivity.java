package com.pearlapps.gatorguards;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.pearlapps.gatorguards.utils.EmployerBean;
import com.pearlapps.gatorguards.utils.PrefHandler;

public class MainActivity extends Activity implements OnClickListener{

	TextView tvPassword;
	String str = "";

	ArrayList<EmployerBean> aListEmp;
	PrefHandler pref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pref=new PrefHandler(this);
		aListEmp = new ArrayList<EmployerBean>();
		aListEmp.add(new EmployerBean("Eric Riley", 1001));
		aListEmp.add(new EmployerBean("Doug Platt", 1002));
		aListEmp.add(new EmployerBean("Kevin Martin", 1003));
		
		if(pref.isUserLoggedIn())
		{
			for (int i = 0; i < aListEmp.size(); i++) {
				if (pref.getUserId() == aListEmp.get(i).empId) {
					
					Intent intent=new Intent(this,ClockInActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.r_to_l_in,R.anim.r_to_l_out);
				}
			}
		}
		else
		{
			setContentView(R.layout.activity_main);
			tvPassword = (TextView) findViewById(R.id.tvPassword);
			findViewById(R.id.tvOne).setOnClickListener(this);
			findViewById(R.id.tvTwo).setOnClickListener(this);
			findViewById(R.id.tvThree).setOnClickListener(this);
			findViewById(R.id.tvFour).setOnClickListener(this);
			findViewById(R.id.tvFive).setOnClickListener(this);
			findViewById(R.id.tvSix).setOnClickListener(this);
			findViewById(R.id.tvSeven).setOnClickListener(this);
			findViewById(R.id.tvEight).setOnClickListener(this);
			findViewById(R.id.tvNine).setOnClickListener(this);
			findViewById(R.id.tvCancel).setOnClickListener(this);
			findViewById(R.id.tvZero).setOnClickListener(this);
			findViewById(R.id.tvNext).setOnClickListener(this);
		}

		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		System.out.println(str);
		if (str.length() < 4) {
			switch (v.getId()) {
			case R.id.tvOne:
				str += "1";
				break;
			case R.id.tvTwo:
				str += "2";
				break;
			case R.id.tvThree:
				str += "3";
				break;
			case R.id.tvFour:
				str += "4";
				break;
			case R.id.tvFive:
				str += "5";
				break;
			case R.id.tvSix:
				str += "6";
				break;
			case R.id.tvSeven:
				str += "7";
				break;
			case R.id.tvEight:
				str += "8";
				break;
			case R.id.tvNine:
				str += "9";
				break;

			case R.id.tvZero:
				str += "0";
				break;
			}

		}
		if (v.getId() == R.id.tvCancel) {

			if (str.length() > 0) {
				str = str.substring(0, str.length() - 1);
			}
		}

		if (v.getId() == R.id.tvNext) {
			for (int i = 0; i < aListEmp.size(); i++) {
				if (Integer.parseInt(str) == aListEmp.get(i).empId) {
					goToNextActivity(i);
				}
			}
		}
		tvPassword.setText(str);

	}

	private void goToNextActivity(int i) {
		Intent intent=new Intent(this,ClockInActivity.class);
		startActivity(intent);
		pref.setUserLoggedIn(true);
		pref.setUserId(aListEmp.get(i).empId);
		pref.setUserName(aListEmp.get(i).empName);
		finish();
		overridePendingTransition(R.anim.r_to_l_in,R.anim.r_to_l_out);
	}

	
}
