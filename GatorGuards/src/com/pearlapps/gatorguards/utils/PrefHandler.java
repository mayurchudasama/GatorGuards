package com.pearlapps.gatorguards.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

@SuppressLint("NewApi")
public class PrefHandler {
	SharedPreferences pref;

	Editor editor;

	Context _context;

	int PRIVATE_MODE = 0;

	private static final String PREF_NAME = "settings_pref";
	public static final String PREF_USER = "user";
	public static final String PREF_USER_ID = "user_id";
	public static final String PREF_USER_NAME = "user_name";
	public static final String PREF_USER_ClOCKED_IN = "clocked_in";

	@SuppressLint("CommitPrefEdits")
	public PrefHandler(Context context) {
		this._context = context;
		pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
		editor = pref.edit();
	}

	public void setUserLoggedIn(boolean flag) {
		editor.putBoolean(PREF_USER, flag);
		editor.commit();
	}

	public boolean isUserLoggedIn() {
		return pref.getBoolean(PREF_USER, false);
	}
	
	public void setUserClockedIn(boolean flag) {
		editor.putBoolean(PREF_USER_ClOCKED_IN, flag);
		editor.commit();
	}

	public boolean isUserClockedIn() {
		return pref.getBoolean(PREF_USER_ClOCKED_IN, false);
	}

	public void setUserId(int id) {
		editor.putInt(PREF_USER_ID, id);
		editor.commit();
	}

	public int getUserId() {
		return pref.getInt(PREF_USER_ID, -1);
	}

	public void setUserName(String id) {
		editor.putString(PREF_USER_NAME, id);
		editor.commit();
	}

	public String getUserName() {
		return pref.getString(PREF_USER_NAME,"");
	}
	/*public static final String PREF_TIME_IN="time_in";
	public void setTimeIn(String timeIn) {
		editor.putString(PREF_TIME_IN, timeIn);
		editor.commit();
	}

	public String getTimeIn() {
		return pref.getString(PREF_TIME_IN,"");
	}*/
	
	
	public void clearData()
	{
		editor.clear();
		editor.commit();
	}
	
	
}
