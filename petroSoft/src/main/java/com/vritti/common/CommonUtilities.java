package com.vritti.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.vritti.database.DatabaseHelper;

public class CommonUtilities {
	public static boolean isInternetAvailable(Context parent) {
		ConnectivityManager cm = (ConnectivityManager) parent
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static void clearTable(Context parent, String tablename) {
		DatabaseHelper db = new DatabaseHelper(parent);
		SQLiteDatabase sql = db.getWritableDatabase();
		sql.delete(tablename, null, null);

		sql.close();
		db.close();
	}

	public static ViewGroup setFont(Context context, ViewGroup group,
			Typeface font) {
		int count = group.getChildCount();
		View v = null;
		for (int i = 0; i < count; i++) {
			v = group.getChildAt(i);
			if (v instanceof TextView || v instanceof Button
					|| v instanceof EditText/* etc. */)
				((TextView) group.getChildAt(i)).setTypeface(font);
			else if (v instanceof ViewGroup)
				setFont(context, (ViewGroup) v, font);
		}

		return group;
	}

	public static String getCurrentDate() {
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
		String formattedDate = df.format(c.getTime());
		return formattedDate;
	}

	public static String convertMoney(double total){
		DecimalFormat twoPlaces = new DecimalFormat("#.##");
		String money = twoPlaces.format(total);
		return money;		
	}
	
	public static String getIp(Context parent) {
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase sql = db1.getWritableDatabase();
		Cursor cursor = sql.rawQuery("Select * from IPAddress", null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			String str = cursor.getString(0);
			cursor.close();
			sql.close();
			db1.close();
			return str;

		} else {

			cursor.close();
			sql.close();
			db1.close();
			return null;
		}
	}

	public static String getIsOperator(Context parent) {
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase sql = db1.getWritableDatabase();
		Cursor cursor = sql.rawQuery("Select * from IsOperator", null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			String str = cursor.getString(0);
			cursor.close();
			sql.close();
			db1.close();
			return str;

		} else {

			cursor.close();
			sql.close();
			db1.close();
			return null;
		}
	}
	
	public static String getBluetoothAddress(Context parent) {
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase sql = db1.getWritableDatabase();
		Cursor cursor = sql.rawQuery("Select * from Bluetooth_Address", null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			String str = cursor.getString(0);
			cursor.close();
			sql.close();
			db1.close();
			return str;

		} else {

			cursor.close();
			sql.close();
			db1.close();
			return null;
		}
	}

	public static boolean dbTableHasRows(Context parent, String query) {

		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase sql = db1.getWritableDatabase();
		Cursor cursor = sql.rawQuery(query, null);

		if (cursor != null && cursor.getCount() > 0) {

			cursor.close();
			sql.close();
			db1.close();
			return true;

		} else {

			cursor.close();
			sql.close();
			db1.close();
			return false;
		}

	}
	
	public static String httpGet(String urlString) throws IOException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// Check for successful response code or throw error
		if (conn.getResponseCode() != 200) {
			throw new IOException(conn.getResponseMessage());
		}

		// Buffer the result into a string
		BufferedReader buffrd = new BufferedReader(new InputStreamReader(
				conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = buffrd.readLine()) != null) {
			sb.append(line);
		}

		buffrd.close();
		conn.disconnect();
		return sb.toString();
	}

}