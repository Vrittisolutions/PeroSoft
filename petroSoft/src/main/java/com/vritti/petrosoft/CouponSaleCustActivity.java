package com.vritti.petrosoft;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ndeftools.Message;
import org.ndeftools.util.activity.NfcReaderActivity;

import java.util.ArrayList;

public class CouponSaleCustActivity extends Activity {
	private Context parent;
	private Button buttonRegister, buttonfetchveh, button_fetch_cust;
	private EditText  et_for_custname, et_for_custmob;
	private Spinner  spVehNo;
	private AutoCompleteTextView spCustName;
	private String vehNo, custCode, custName, et_custName="", pager_mobile;
	private ProgressDialog progressDialog;
	ImageView ivlogo;
	String Mobile;
	String intentFrom;

	private ArrayList<String> customerNameList =null, customerCodeList = null, vehNoList = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_coupon_sale_cust);
		initViews();
		//scanCard();
		updateCustomerSpinner();

		/*if(vehNo.equalsIgnoreCase(null) || vehNo.equalsIgnoreCase("")){
			Toast.makeText(parent,"Please select Vehicle",Toast.LENGTH_SHORT).show();
			buttonRegister.setClickable(false);
		}else{
			buttonRegister.setClickable(true);
		}*/

		setListeners();
		//checkautoComplete();
	}

	private void checkautoComplete() {
		// TODO Auto-generated method stub
		custName = spCustName.getText().toString();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		Cursor cursor;
		if (spCustName!=null&&custName!=""){
			cursor = db.rawQuery("Select AcNo from CashierMaster where Name = '"+custName+"'", null);
			if(cursor.getCount()>0){
					cursor.moveToFirst();
					custCode = cursor.getString(0);
			}
			cursor.close();db.close();db1.close();
					//custName = spCustName.getText().toString();
					if (CommonUtilities.isInternetAvailable(parent)) {
						new UpdateVehicleMaster().execute();
					} else {
						Toast.makeText(parent,"Internet connection not available..",Toast.LENGTH_LONG).show();
					}
		}else {
			Toast.makeText(CouponSaleCustActivity.this,"Please select customer.", Toast.LENGTH_SHORT).show();
		}
	}

	private void initViews() {
		parent = CouponSaleCustActivity.this;

		buttonRegister = (Button) findViewById(R.id.button_couponsalecust_register);
		buttonfetchveh = (Button) findViewById(R.id.button_fetch_veh);
		button_fetch_cust = (Button)findViewById(R.id.button_fetch_cust);

		et_for_custmob = (EditText)findViewById(R.id.et_for_custmob);

		//edtRfidNo = (EditText) findViewById(R.id.edittext_couponsalecust_rfid);
		//et_for_custname = (EditText) findViewById(R.id.et_for_custname);
		spCustName = (AutoCompleteTextView) findViewById(R.id.spinner_couponsalecust_custname);
		spCustName.setClickable(false);

		spVehNo = (Spinner) findViewById(R.id.spinner_couponsalecust_vehicle);

		customerNameList = new ArrayList<String>();
		customerCodeList = new ArrayList<String>();

		vehNoList = new ArrayList<String>();

		ivlogo = (ImageView) findViewById(R.id.iconlogo);

		Intent intent = getIntent();
		intentFrom = intent.getStringExtra("CallFrom");

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

	/*private void scanCard() {
		progressDialog = new ProgressDialog(parent);
		progressDialog.setMessage("Detecting...");
		progressDialog.setCancelable(true);
		progressDialog.show();
		progressDialog.setOnCancelListener(new OnCancelListener() {

			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		setDetecting(true);
	}*/

	private void updateCustomerSpinner() {
		customerNameList.clear();
		customerCodeList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		Cursor cursor;
//if (et_custName==null||et_custName==""){
	 cursor = db.rawQuery(
			"Select AcNo,Name from CashierMaster where AcType = 'Cust' order by Name ASC", null);
/*} else{
		 cursor = db.rawQuery(
				"Select AcNo,Name from CustomerMaster where Name like %"+et_custName+"% order by Name ASC", null);
}*/
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			do {
				customerCodeList.add(cursor.getString(0));
				customerNameList.add(cursor.getString(1));
			} while (cursor.moveToNext());

			cursor.close();
			db.close();
			db1.close();
		}
		custCode = customerCodeList.get(0);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
				android.R.layout.simple_list_item_1, customerNameList);
		//adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

		spCustName.setThreshold(0);
		spCustName.setAdapter(adapter1);

		}

	private void setListeners() {

		buttonRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonUtilities.isInternetAvailable(parent)) {
					//rfidNo = edtRfidNo.getText().toString();
					vehNo = spVehNo.getSelectedItem().toString();
					new GetCustDetails().execute();
				} else {
					Toast.makeText(parent,
							"Internet connection not available..",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		
		buttonfetchveh.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				checkautoComplete();
			}
		});
		
				
		spVehNo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				vehNo = spVehNo.getSelectedItem().toString();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		button_fetch_cust.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Toast.makeText(CouponSaleCustActivity.this, "btn get cust clicked", Toast.LENGTH_SHORT).show();

				Mobile = et_for_custmob.getText().toString();

				if(Mobile.equalsIgnoreCase(null) || Mobile.equalsIgnoreCase("")){
					Toast.makeText(CouponSaleCustActivity.this,"Please enter mobile number.", Toast.LENGTH_SHORT).show();
					spCustName.setClickable(false);
				}else {
					new UpdateCustomer_eneredMobile().execute();
				}
			}
		});
	}

	/*private void endOfCouponSaleCustActivity() {
		Toast.makeText(parent,"Check Check Check",Toast.LENGTH_SHORT).show();
	}*/

	class RegisterCard extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		String responseString = null;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("Saving Data on Server...");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {
				// String fullURL = getResources().getString(R.string.url);
				String fullURL = PetroSoftData.URL;
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_REGISTER_CARD);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;
				// adding parameters
				request.addProperty("cust_code", custCode);
				request.addProperty("veh_no", vehNo);
				//request.addProperty("rfid_card", rfidNo);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_REGISTER_CARD, envelope);
				if (envelope.bodyIn instanceof SoapFault) {
				    String str= ((SoapFault) envelope.bodyIn).faultstring;
				    responseString = "error";
				    Log.i("SoapFault", str);}
				else{
				SoapObject response = (SoapObject) envelope.bodyIn;
				responseString = response.getProperty(0).toString();
				}
			} catch (Exception e) {
				responseString = "error";
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			super.onPostExecute(result);
			progressDialog.dismiss();
			Toast.makeText(parent, responseString, Toast.LENGTH_LONG).show();
		}
	}

	class UpdateVehicleMaster extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		String exceptionString = "ok";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("getting Vehicle List...");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				// String fullURL = getResources().getString(R.string.url);
				String fullURL = PetroSoftData.URL;
				
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_VEHICLE_LIST);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;
				// adding parameters
				request.addProperty("Cust_code", custCode.toString());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_VEHICLE_LIST, envelope);

				SoapObject response = (SoapObject) envelope.bodyIn;

				SoapObject getVehicleResult = (SoapObject) response
						.getProperty(0);
				SoapObject newDataSet = (SoapObject) getVehicleResult
						.getProperty(0);

				vehNoList.clear();
				// CommonUtilities.clearTable(parent, "VehicleMaster");
				for (int i = 0; i < newDataSet.getPropertyCount(); i++) {
					SoapObject table = (SoapObject) newDataSet.getProperty(i);
					vehNoList.add(table.getProperty("veh_no").toString().trim());
					// databaseHelper.AddVehicleMaster(
					// table.getProperty("cust_code").toString().trim(),
					// table.getProperty("veh_no").toString().trim(),
					// table.getProperty("rfid_card").toString().trim());
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
						"Data Sync Failed. Please try after some time..",
						Toast.LENGTH_LONG).show();
			} else {
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_list_item_1, vehNoList);
				adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
				spVehNo.setAdapter(adapter);
				if (vehNoList.size() > 0)
					vehNo = vehNoList.get(0).toString();
				else
					vehNo = null;
			}
		}
	}

	class GetCustDetails extends AsyncTask<Void, Void, Void> {
		String exceptionString = "ok";
		String rfidstr;

		@Override
		protected Void doInBackground(Void... params) {

			try {

				// String fullURL = getResources().getString(R.string.url);
				String fullURL = PetroSoftData.URL;
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_CUST_DETAILS_WITHOUT_CARD);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;
				// adding parameters
				request.addProperty("cust_code",custCode);
				request.addProperty("veh_no",vehNo);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_CUST_DETAILS_WITHOUT_CARD, envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				exceptionString = response.toString();
				SoapObject GetCustDetailsResponse = (SoapObject) response
						.getProperty(0);
				SoapObject GetCustDetailsResult = (SoapObject) GetCustDetailsResponse
						.getProperty(0);
				SoapObject Table = (SoapObject) GetCustDetailsResult
						.getProperty(0);

				PetroSoftData.cust_code = Table.getProperty("cust_code")
						.toString().trim();
				PetroSoftData.veh_no = Table.getProperty("veh_no").toString()
						.trim();
				PetroSoftData.rfid_card = Table.getProperty("rfid_card")
						.toString().trim();
				PetroSoftData.name = Table.getProperty("name").toString()
						.trim();
				PetroSoftData.item_desc = Table.getProperty("itemname").toString()
						.trim();
				PetroSoftData.qty = Table.getProperty("qty").toString()
						.trim();
				PetroSoftData.sodetailid = Table.getProperty("sodetailid").toString().trim();
				PetroSoftData.cust_balance = Table.getProperty("balance").toString().trim();
				PetroSoftData.cred_lim = Table.getProperty("credlim").toString().trim();
				PetroSoftData.Reward_Points = Table.getProperty("Bal_Point").toString().trim();
				PetroSoftData.vv = Table.getProperty("vv").toString().trim();

			} catch (Exception e) {
				exceptionString = "error";
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//rfidstr = rfidNumberEdittext.getText().toString();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("Processing...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			if (PetroSoftData.cust_code == null) {
				Toast.makeText(parent, "Customer details not available..",
						Toast.LENGTH_LONG).show();
				PetroSoftData.cust_code = null;
				PetroSoftData.veh_no = null;
				PetroSoftData.rfid_card = null;
				PetroSoftData.name = null;
				PetroSoftData.item_desc = null;
				PetroSoftData.qty = null;
				PetroSoftData.vv = null;
				PetroSoftData.sodetailid = null;
				PetroSoftData.cust_balance = null;
				PetroSoftData.cred_lim = null;
				PetroSoftData.Reward_Points = null;

				finish();
			} else {

				Intent ibtnrgstr = new Intent(parent,BillDetails.class);
				ibtnrgstr.putExtra("CallFrom",intentFrom);
				startActivity(ibtnrgstr);
				//startActivity(new Intent(parent, BillDetails.class));
				finish();
			}
		}
	}

	class UpdateCustomer_eneredMobile extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		String exceptionString = "ok";
		int propcount;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("getting customer ...");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				// String fullURL = getResources().getString(R.string.url);
				String fullURL = PetroSoftData.URL;

				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_CUST_OF_REG_MOB);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;
				// adding parameters
				request.addProperty("mobile", Mobile.toString());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_CUST_OF_REG_MOB, envelope);

				SoapObject response = (SoapObject) envelope.bodyIn;

				SoapObject GetCustFromMobResult = (SoapObject) response	.getProperty(0);
				SoapObject newDataSet = (SoapObject) GetCustFromMobResult.getProperty(0);

				propcount = newDataSet.getPropertyCount();

				// CommonUtilities.clearTable(parent, "VehicleMaster");
				for (int i = 0; i < newDataSet.getPropertyCount(); i++) {
					SoapObject table = (SoapObject) newDataSet.getProperty(i);
					//custList.add(table.getProperty("veh_no").toString().trim());
					custCode = table.getProperty("cust_code").toString().trim();
					custName = table.getProperty("name").toString().trim();
					pager_mobile = table.getProperty("pager").toString().trim();
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
				Toast.makeText(parent,	"Data Sync Failed. Please try after some time..",
						Toast.LENGTH_LONG).show();
			} else {
			    if(propcount > 1){
			        Toast.makeText(CouponSaleCustActivity.this, "Invalid number! This mobile number have multiple customer records", Toast.LENGTH_SHORT).show();
                    spCustName.setClickable(false);
                }else {
                    spCustName.setClickable(true);
                    spCustName.setText(custName);
					checkautoComplete();
                }
				/*ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,android.R.layout.simple_list_item_1, custList);
				adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
				spVehNo.setAdapter(adapter);
				if (custList.size() > 0)
					vehNo = custList.get(0).toString();
				else
					vehNo = null;*/
			}
		}
	}
}