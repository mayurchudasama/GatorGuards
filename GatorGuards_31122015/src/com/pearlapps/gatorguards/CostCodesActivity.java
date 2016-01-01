package com.pearlapps.gatorguards;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CostCodesActivity extends Activity implements OnClickListener ,OnItemClickListener{

	LinearLayout rlBack;
	TextView tvSync;
	ListView lvData;
	ArrayList<String> aListLocation;
	ArrayList<String> aListSearch;
	EditText edtSearch;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cost_codes);

		
		aListLocation=new ArrayList<String>();
		aListSearch=new ArrayList<String>();
		rlBack = (LinearLayout) findViewById(R.id.rlBack);
		rlBack.setOnClickListener(this);
		tvSync = (TextView) findViewById(R.id.tvSync);
		tvSync.setOnClickListener(this);
		
		lvData=(ListView)findViewById(R.id.lvData1);
		
		aListLocation=new ArrayList<String>();
		aListLocation.add("Security");
		aListLocation.add("Event Staff");
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, R.layout.row_adapter, R.id.tvRow, aListLocation);
		lvData.setAdapter(adapter);
		
		lvData.setOnItemClickListener(this);
		
		
		edtSearch=(EditText)findViewById(R.id.edtSearch);
		
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
					
					ArrayAdapter<String> adapter=new ArrayAdapter<String>(CostCodesActivity.this, R.layout.row_adapter, R.id.tvRow, aListSearch);
					lvData.setAdapter(adapter);
				}
				else
				{
					ArrayAdapter<String> adapter=new ArrayAdapter<String>(CostCodesActivity.this, R.layout.row_adapter, R.id.tvRow, aListLocation);
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
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tvSync:
			Toast.makeText(this, "sync", Toast.LENGTH_LONG).show();
			break;
		case R.id.rlBack:
			Intent intent=new Intent(this,LocationActivity.class);
			intent.putExtra("dtNow", getIntent().getSerializableExtra("dtNow"));
			startActivity(intent);
			overridePendingTransition(R.anim.l_to_r_in, R.anim.l_to_r_out);
			break;
		default:
			break;
		}

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		Intent intent=new Intent(this,LocationActivity.class);
		intent.putExtra("dtNow", getIntent().getSerializableExtra("dtNow"));
		startActivity(intent);
		overridePendingTransition(R.anim.l_to_r_in, R.anim.l_to_r_out);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		
		String costcode="";
		if(edtSearch.length()>0)
		{
			costcode=aListSearch.get(position);
		}
		else
		{
			costcode=aListLocation.get(position);
		}
		
		Intent intent=new Intent(this,ClockingInActivity.class);
		intent.putExtra("dtNow", getIntent().getSerializableExtra("dtNow"));
		intent.putExtra("location", getIntent().getSerializableExtra("location"));
		intent.putExtra("costcode", costcode);
		startActivity(intent);
		overridePendingTransition(R.anim.r_to_l_in,R.anim.r_to_l_out);
		
	}
}
