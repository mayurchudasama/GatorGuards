package com.pearlapps.gatorguards.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelperHG extends SQLiteOpenHelper {
	// Database Version
	public static final int DATABASE_VERSION = 1;

	// Database Name
	public static final String DATABASE_NAME = "gatorguards.db";

	// Labels table name
	public static final String TABLE_GATOR_GUARDS= "gatorguards";

	// Labels TABLE_MAIN Columns names
	public static final String KEY_AUTO_ID="a_id";
	public static final String KEY_GID= "id";
	public static final String KEY_GNAME= "name";
	public static final String KEY_GLOCATION= "location";
	public static final String KEY_GCOST_CODES= "cost_codes";
	public static final String KEY_GTIME_IN= "time_in";
	public static final String KEY_GTIME_OUT= "time_out";
	
	Context context;
	public DbHelperHG(Context context) {
		super(context, /*Environment.getExternalStorageDirectory().toString()+File.separator+*/DATABASE_NAME, null, DATABASE_VERSION);
		this.context=context;
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Category table create query
		
		String CREATE_GATOR_GAURD_TABLE = "CREATE TABLE " + TABLE_GATOR_GUARDS+ "("
				+ KEY_AUTO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ KEY_GID+ " TEXT," 
				+ KEY_GNAME+ " TEXT,"
				+ KEY_GLOCATION+ " TEXT,"
				+ KEY_GCOST_CODES+ " TEXT,"
				+ KEY_GTIME_IN+ " TEXT,"
				+ KEY_GTIME_OUT+ " TEXT)";
		db.execSQL(CREATE_GATOR_GAURD_TABLE);
		
		
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GATOR_GUARDS);
		onCreate(db);
	}
	public int getLastRecordId()
	{
		String query="SELECT * FROM "+TABLE_GATOR_GUARDS+" WHERE "+KEY_AUTO_ID+" = (SELECT MAX("+KEY_AUTO_ID+")  FROM "+TABLE_GATOR_GUARDS+")";
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		// looping through all rows and adding to list
		System.out.println("Cursor size : "+cursor.getCount());
		if(cursor.getCount()>0)
		{
			if (cursor.moveToFirst()) {
				do {
//					cursor.getString(cursor.getColumnIndex(KEY_RE_IMAGE_PATH))
					String id=cursor.getString(cursor.getColumnIndex(KEY_AUTO_ID));
					cursor.close();
					return Integer.parseInt(id);
				} while (cursor.moveToNext());
			}
			db.close();
		}
		else
		{
			cursor.close();
			return -1;
		}
		cursor.close();
		return -1;
	}
	public int insertAudioRecord(String id,String name,String location,String cost_codes,String time_in,String time_out) {
		SQLiteDatabase db = this.getWritableDatabase();

		int success=0;
			try
			{
				ContentValues values = new ContentValues();
				values.put(KEY_GID, id);
				values.put(KEY_GNAME, name);
				values.put(KEY_GLOCATION, location);
				values.put(KEY_GCOST_CODES, cost_codes);
				values.put(KEY_GTIME_IN, time_in);
				values.put(KEY_GTIME_OUT, time_out);
				db.insert(TABLE_GATOR_GUARDS, null, values);
				success=1;
				System.out.println("Db Logs : Record Inserted Successfull");
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("Db Logs : Error Inserting Record : "+e.getMessage());
				return 0;
			}
		db.close();
		return success;
	}
	
	public Cursor getAllRecords()
	{
		Cursor cursor=null;
		String query="SELECT * FROM "+TABLE_GATOR_GUARDS;
		SQLiteDatabase db = this.getReadableDatabase();
		cursor = db.rawQuery(query, null);
		cursor.moveToFirst();
		return cursor;
	}
	
	public void deleteRecord()
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String query= "DELETE FROM " + TABLE_GATOR_GUARDS;
		db.execSQL(query);
		db.close();
		
	}
	public Cursor getPerticularRecordSize() {

		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_GATOR_GUARDS;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery,null);

		return cursor;
	}
	
	public int insertTimeOut(String timeout,String lastId) {

		// Select All Query
		int success=0;
		try
		{
			/*String selectQuery = "INSERT INTO " + TABLE_GATOR_GUARDS+" ("+KEY_GTIME_OUT+") VALUES ("+timeout+") where "+KEY_AUTO_ID+"=?";
			SQLiteDatabase db = this.getReadableDatabase();
			db.rawQuery(selectQuery,new String[]{lastId});*/
			
			/*SQLiteDatabase db = this.getReadableDatabase();
			ContentValues values = new ContentValues();
			
			values.put(KEY_GTIME_OUT, timeout);
			db.insert(TABLE_GATOR_GUARDS, "where "+KEY_AUTO_ID+"="+lastId, values);*/
			
			SQLiteDatabase db = this.getReadableDatabase();
			ContentValues values = new ContentValues();
			values.put(KEY_GTIME_OUT, timeout);
			db.update(TABLE_GATOR_GUARDS, values, KEY_AUTO_ID+"=?", new String[]{lastId});
			
			
			
			success=1;
			
			
			return success;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return success;
		}
	}
	
}
