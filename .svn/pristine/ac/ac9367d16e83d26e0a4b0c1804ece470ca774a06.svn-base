package com.vritti.petrosoft;

import java.util.ArrayList;
import java.util.Collections;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;
import com.vritti.petrosoft.LoginActivity.UpdateItemRateMaster;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;


public class GetVehicleActivity extends Activity {
	private Context parent;
	public ListView list;
	ImageView ivlogo;
	
	private DatabaseHelper dbi;
	 ArrayAdapter<String> adapter;
	 ArrayList<String> nameList;
	
	private EditText editTextFilter;
	
	private String fullURL = PetroSoftData.URL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_getvehicle);
		
		initialize();
		list = (ListView) findViewById(R.id.listview_choose_vehicle_list);
		events();
		
		//setdata();
		//filterList();
	}
	
	private void initialize() {
		parent = GetVehicleActivity.this;
		// refresh = (RelativeLayout)
		// findViewById(R.id.relative_list_choose_result_refresh);
		
	//	da = new DatabaseHelper();
		nameList = new ArrayList<String>();
		dbi = new DatabaseHelper(parent);
	//	mobile_no = dbi.GetPhno();
	//	link = dbi.GetUrl();
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
		
		
		if (CommonUtilities.isInternetAvailable(parent)) {
			refreshList();
		}
		else
			updateList();
	}
	
	private void refreshList() {
		// TODO Auto-generated method stub
	new	GetVehicleMaster().execute();
	}

	private void setdata() {
		Collections.sort(nameList, String.CASE_INSENSITIVE_ORDER);
			String[] items1 = nameList.toArray(new String[nameList.size()]);
			 adapter = new ArrayAdapter<String>(GetVehicleActivity.this,
					android.R.layout.simple_list_item_1, nameList);
			adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
			list.setAdapter(adapter);
		
	}

//	private void filterList() {
//		editTextFilter.addTextChangedListener(new TextWatcher() {
//
//			@Override
//			public void onTextChanged(CharSequence s, int start, int before,
//					int count) {
//				setListFilteredItems();
//			}
//
//			@Override
//			public void beforeTextChanged(CharSequence s, int start, int count,
//					int after) {
//
//			}
//
//			@Override
//			public void afterTextChanged(Editable s) {
//				// TODO Auto-generated method stub
//				
//			}
//
//		
//		});
//	}

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

			
				intent.putExtra("MESSAGE", list.getItemAtPosition(position)
						.toString());

				setResult(PetroSoftData.REQUEST_GET_VEHICLE_DETAILS,
						intent);
				finish();
			}

			
		});
	}
	
	private void updateList() {
		nameList.add("Cashier List");
		nameList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();

		Cursor c2 = db.rawQuery(
				"SELECT VechNo FROM VehicleMaster where CustCode=? ",
				new String[] { PetroSoftData.cust_code });

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
				nameList.add(c2.getString(c2.getColumnIndex("VechNo")));
			} while (c2.moveToNext());

			c2.close();
			db.close();
			db1.close();
		}

	
	}
	
	class GetVehicleMaster extends AsyncTask<Void, Void, Void> {

		ProgressDialog progressDialog;
		String exceptionString = "ok";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("Updating database...");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			DatabaseHelper db = new DatabaseHelper(getApplicationContext());
			
			try {

				
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_VEHICLE);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;
				// adding parameters
				request.addProperty("Cust_code",PetroSoftData.cust_code);


				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);

				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_VEHICLE, envelope);
				
				SoapObject response = (SoapObject) envelope.bodyIn;
				
				SoapObject GetUserDetailsResult = (SoapObject) response
						.getProperty(0);
				SoapObject NewDataSet = (SoapObject) GetUserDetailsResult
						.getProperty(0);
				CommonUtilities.clearTable(parent, "VehicleMaster");
				Log.e("VehicleMaster", "5 : " + NewDataSet.getPropertyCount());
				for (int i = 0; i < NewDataSet.getPropertyCount(); i++) {
					SoapObject Table = (SoapObject) NewDataSet.getProperty(i);
					db.AddVehicleMasters(Table.getProperty("cust_code")
							.toString().trim(), Table.getProperty("veh_no")
							.toString().trim(),Table.getProperty("rfid_card")
							.toString().trim());
					
				}
			} catch (Exception e) {
				exceptionString = "error";
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			progressDialog.dismiss();
			if (exceptionString == "error") {
				Toast.makeText(parent,
						"Vehicle Master Sync Failed. Please try after some time..",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(parent, "Vehicle Master Sync Successful..",
						Toast.LENGTH_LONG).show();
				updateList();
				setdata();
				
			}
		}
	}
		
	
}
