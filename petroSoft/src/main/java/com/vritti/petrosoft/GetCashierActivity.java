package com.vritti.petrosoft;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.squareup.picasso.Picasso;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;

public class GetCashierActivity extends Activity {
	private Context parent;
	public ListView list;
	
	private DatabaseHelper dbi;
	 ArrayAdapter<String> adapter;
	 ArrayList<String> nameList;
	 ImageView ivlogo;
	
	private EditText editTextFilter;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_list_choose_result);
		
		initialize();
		list = (ListView) findViewById(R.id.listview_choose_result_list);
		events();
		updateList();
		setdata();
		filterList();
	}
	
	private void initialize() {
		parent = GetCashierActivity.this;
		nameList = new ArrayList<String>();
		dbi = new DatabaseHelper(parent);
		editTextFilter = (EditText) findViewById(R.id.edittext_choose_result_filter);
		ivlogo = (ImageView) findViewById(R.id.iconlogo);
		if(PetroSoftData.imgPath!=null){
			Picasso.with(parent).load(PetroSoftData.imgPath)/*
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)*/
					.into(ivlogo);
		}
		else{
			ivlogo.setImageResource(R.drawable.petro_soft_logo);
		}
	}
	
	private void setdata() {
//		Collections.sort(nameList, String.CASE_INSENSITIVE_ORDER);
		//	String[] items1 = nameList.toArray(new String[nameList.size()]);
			 adapter = new ArrayAdapter<String>(GetCashierActivity.this,
					android.R.layout.simple_list_item_1, nameList);
			adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
			list.setAdapter(adapter);
		
	}

	private void filterList() {
		editTextFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				setListFilteredItems();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}

		
		});
	}

	protected void setListFilteredItems() {
		int textlength = editTextFilter.getText().length();
		ArrayList<String> filteredArray = new ArrayList<String>();
		filteredArray.clear();
		for (int i = 0; i < nameList.size(); i++) {
			if (textlength <= nameList.get(i).length()) {

				String s2 = editTextFilter.getText().toString();
				if (nameList.get(i).toString().toLowerCase()
						.contains(editTextFilter.getText().toString())) {
					filteredArray.add(nameList.get(i));
				}

			}
		}

		list.setAdapter(new ArrayAdapter<String>(parent,
				android.R.layout.simple_list_item_1, filteredArray));
	}

	private void events() {
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();

				String cashiername = list.getItemAtPosition(position)
						.toString();

			PetroSoftData.Cashier_acno=getCashieracno(cashiername);
				intent.putExtra("MESSAGE", list.getItemAtPosition(position)
						.toString());

				setResult(PetroSoftData.REQUEST_GET_CASHIER_DETAILS,
						intent);
				finish();
			}

			
		});
	}
	protected String getCashieracno(String cashiername) {
		String id = null;
		try {
			DatabaseHelper db1 = new DatabaseHelper(parent);
			SQLiteDatabase db = db1.getWritableDatabase();

			Cursor cursor = db.rawQuery(
					"SELECT Acno FROM CashierMaster where Name=? ",
					new String[] { cashiername });

			cursor.moveToFirst();
			do {
				id = cursor.getString(0);
			} while (cursor.moveToNext());

			cursor.close();
			db.close();
			db1.close();
		} catch (Exception e) {
			id = "0";
		}
		return id;
	}
	
	private void updateList() {
		nameList.add("Cashier List");
		nameList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();

		Cursor c2 = db.rawQuery(
				"SELECT * FROM CashierMaster where AcType='Cashier' ",
				null);

		if (c2.getCount() == 0) {
			nameList.add("No Cashier added");
			c2.close();
			db.close();
			db1.close();
		}

		else {
			c2.moveToFirst();
			do {
				// nameList.add(c2.getString(0));
				nameList.add(c2.getString(c2.getColumnIndex("Name")));
			} while (c2.moveToNext());

			c2.close();
			db.close();
			db1.close();
		}

	
	}
		
	
}
