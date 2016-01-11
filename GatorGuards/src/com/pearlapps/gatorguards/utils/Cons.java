package com.pearlapps.gatorguards.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Cons {
	public static final String DFSTRING_DISPLAY=" EEEE, dd MMM yyyy";
	public static final String DFSTRING="dd/MM/yyyy";
	public static final String URL = "https://docs.google.com/forms/d/12vCP6mUN6CnKRKbk3ghVlstUW8RYeYj8ugITXSzIbEo/formResponse";
	public static final String KEY_ID = "entry_1276988803";
	public static final String KEY_NAME = "entry_1833377572";
	public static final String KEY_LOCATION = "entry_1974637109";
	public static final String KEY_COST_CODES = "entry_1711576972";
	public static final String KEY_TIME_IN = "entry_960510735";
	public static final String KEY_TIME_OUR = "entry_1884756332";
	public static final String KEY_TOTAL_TIME = "entry_160537164";
	public  static final String DFSTRING_HM = "hh:mm";
	
	public static final String URL_USER = "https://docs.google.com/forms/d/1J_lRl3D6WCICEIYvKMHLPCQ6V_qb1vRrTJFtjhipR2g/formResponse";
	public static final String KEY_USER_ID="entry_1790711378";
	public static final String KEY_USER_NAME="entry_1964041302";
	public static final String KEY_USER_LOCATION="entry_1671166780";

	public static String changeDateFormat(String time_in) {
		String dt=null;
		try
		{
			SimpleDateFormat dfD = new SimpleDateFormat(Cons.DFSTRING_HM+""+Cons.DFSTRING_DISPLAY);
			Date dtF=dfD.parse(time_in);
			SimpleDateFormat df = new SimpleDateFormat(Cons.DFSTRING);
			dt= df.format(dtF);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return dt;
	}
	public static String getBody(String id, String name, String location,String cost_code, String time_in, String time_out,String total_time) throws UnsupportedEncodingException 
	{
		String postBody 
				= Cons.KEY_ID + "="+ URLEncoder.encode(id, "UTF-8") + "&"
				+ Cons.KEY_NAME + "="+ URLEncoder.encode(name, "UTF-8") + "&"
				+ Cons.KEY_LOCATION + "="+ URLEncoder.encode(location, "UTF-8") + "&" 
				+ Cons.KEY_COST_CODES + "="+ URLEncoder.encode(cost_code, "UTF-8") + "&" 
				+ Cons.KEY_TIME_IN + "="+ URLEncoder.encode(time_in, "UTF-8") + "&"
				+ Cons.KEY_TIME_OUR + "="+ URLEncoder.encode(time_out, "UTF-8") + "&"
				+ Cons.KEY_TOTAL_TIME + "="+ URLEncoder.encode(total_time, "UTF-8");
		return postBody;
	}
	
	public static String getUserBody(String user_id, String user_name, String user_location) throws UnsupportedEncodingException 
	{
		String postBody 
				= Cons.KEY_USER_ID + "="+ URLEncoder.encode(user_id, "UTF-8") + "&"
				+ Cons.KEY_USER_NAME + "="+ URLEncoder.encode(user_name, "UTF-8") + "&"
				+ Cons.KEY_USER_LOCATION + "="+ URLEncoder.encode(user_location, "UTF-8");
		return postBody;
	}
}
