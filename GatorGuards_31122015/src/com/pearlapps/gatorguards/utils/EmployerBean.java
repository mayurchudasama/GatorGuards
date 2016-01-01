package com.pearlapps.gatorguards.utils;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.csvreader.CsvWriter;

public class EmployerBean implements Serializable {
	public String empName = "";
	public int empId;

	public EmployerBean() {

	}

	public EmployerBean(String empName, int empId) {
		this.empName = empName;
		this.empId = empId;
	}

	
	public void exportToCsv(Context context) {

		
		 File exportDir = new File(Environment.getExternalStorageDirectory()
	                .toString()+File.separator+"SorryMate"+File.separator);

	       

	            exportDir.mkdirs();

	       

	        File file = new File(exportDir, "Filename.csv");

	        try

	        {

	            file.createNewFile();
			CsvWriter csvWrite = new CsvWriter(file.getAbsolutePath());

			DbHelperHG db = new DbHelperHG(context);
			Cursor curCSV = db.getPerticularRecordSize();

			// int c = curCSV.getColumnCount();

			// csvWrite.writeNext(curCSV.getColumnNames());

			// csvWrite.write(curCSV.getColumnNames());

			csvWrite.writeRecord(new String[] {
					DbHelperHG.KEY_GID,
					DbHelperHG.KEY_GNAME, 
					DbHelperHG.KEY_GLOCATION,
					DbHelperHG.KEY_GCOST_CODES,
					DbHelperHG.KEY_GTIME_IN,
					DbHelperHG.KEY_GTIME_OUT});

			while (curCSV.moveToNext()) {
				String arrStr[] = { curCSV.getString(0), curCSV.getString(1),
						curCSV.getString(2), curCSV.getString(3) };
				csvWrite.writeRecord(arrStr);
			}
			csvWrite.close();
			curCSV.close();
			Toast.makeText(context, "File exported to : ", Toast.LENGTH_LONG).show();
//			db.deleteRecord();
			
		} catch (SQLException sqlEx) {
			Log.e("MainActivity", sqlEx.getMessage(), sqlEx);
			Toast.makeText(context, sqlEx.getMessage(), Toast.LENGTH_LONG).show();
		} catch (IOException e) {
			Log.e("MainActivity", e.getMessage(), e);
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
	}
	
}
