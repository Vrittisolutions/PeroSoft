package com.vritti.petrosoft;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;

public class DSRGetPetroSaleActivity extends Activity implements OnClickListener{
	Context parent;
	ImageView refresh, ivlogo;
	ListView pumplistlv;
	Button btnPetroSale;
	ArrayList<PumpListBean> pumplistBeanList;
	PumpListBean pumplistBean;
	PumpListAdapter pumplistAdapter;
	String date,shift,acno;

	private DatabaseHelper databaseHelper;
	private String fullURL = PetroSoftData.URL;
	Float fltItemRate=(float) 0.00, fltItemQty=(float) 0.00, fltItemAmt=(float) 0.00, fltOpening=(float) 0.00, fltClosing=(float) 0.00, fltTesting=(float) 0.00;
	String pump_no, pump_name, item_code, item_desc, pump_seq, opening, closing, testing, SaleLtrs, ItemRate, ItemAmount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dsr_petro_sale);

		initView();

		if (dbvalue()) {
			updatelist();
		}else if (CommonUtilities.isInternetAvailable(parent)) {
			new UpdatePumpList().execute();
		} else {
			Toast.makeText(parent, "No internet connection found..",
					Toast.LENGTH_LONG).show();
		}
		setListener();
	}
	
	private boolean dbvalue() {
		try {
			DatabaseHelper db1 = new DatabaseHelper(getBaseContext());
			SQLiteDatabase sql = db1.getWritableDatabase();
			Cursor cursor = sql.rawQuery(
					"SELECT *   FROM PumpList", null);
			if (cursor != null && cursor.getCount() > 0) {
				
					cursor.close();
					sql.close();
					db1.close();
					return true;
				//}
			} else {
				cursor.close();
				sql.close();
				db1.close();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void setListener() {
		// TODO Auto-generated method stub	
		//refresh.setOnClickListener(this);
		btnPetroSale.setOnClickListener(this);
		pumplistlv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				getfirstData(position);
				if(fltClosing==0){
					getClosing(position);
				}else{
					setPrompt();
				}
			}
		});

	}
	
	protected void setPrompt() {
		// TODO Auto-generated method stub
		// get prompts.xml view
					LayoutInflater li = LayoutInflater.from(parent);
					View promptsView = li.inflate(R.layout.prompt_pumplist, null);					

					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parent);
					// set prompts.xml to alertdialog builder
					alertDialogBuilder.setView(promptsView);

					final EditText editTextPumpNamewithProd = (EditText) promptsView.findViewById(R.id.editTextPumpNamewithProd);
					editTextPumpNamewithProd.setText(pump_name+" : "+item_desc);
					final EditText editTextOpening = (EditText) promptsView.findViewById(R.id.editTextOpening);
					final EditText editTextClosing = (EditText) promptsView.findViewById(R.id.editTextClosing);
					editTextOpening.setText(opening);
					fltOpening = Float.parseFloat(opening);
					editTextClosing.setText(closing);
					final EditText editTextTesting = (EditText) promptsView.findViewById(R.id.editTextTesting);
					editTextTesting.setText(fltTesting.toString());
					final EditText editTextSaleLtrs = (EditText) promptsView.findViewById(R.id.editTextSaleLtrs);
					final EditText editTextRate = (EditText) promptsView.findViewById(R.id.editTextRate);
					editTextRate.setText(ItemRate);					
					final EditText editTextAmount = (EditText) promptsView.findViewById(R.id.editTextAmount);
					if(fltItemQty!=0&&fltItemAmt!=0){
						editTextSaleLtrs.setText(fltItemQty.toString());
						editTextAmount.setText(fltItemAmt.toString());
					}
					
					editTextClosing.addTextChangedListener(new TextWatcher() {

			            @Override
			            public void onTextChanged(CharSequence s, int start, int before, int count) {
			            				            	
			            }
			            @Override
			            public void beforeTextChanged(CharSequence s, int start, int count,
			                    int after) {	                // TODO Auto-generated method stub	     
			            }
			            @Override
			            public void afterTextChanged(Editable s) { 
			            	if(editTextClosing.getText().toString().equalsIgnoreCase("")||editTextClosing.getText()==null){
								fltClosing = (float) 0.0;
								editTextClosing.setError("Closing Cannot be blank");
			            	}else{ if(editTextTesting.getText().toString().equalsIgnoreCase("")||editTextTesting.getText()==null){
								fltTesting = (float) 0.0;
								editTextTesting.setError("Testing Cannot be blank");
			            	}else {
			            		fltClosing = Float.parseFloat(editTextClosing.getText().toString());
								fltTesting = Float.parseFloat(editTextTesting.getText().toString());
								//fltItemQty = Float.parseFloat(editTextSaleLtrs.getText().toString());
								if(Float.parseFloat(editTextClosing.getText().toString())>fltOpening){
				            		fltItemQty=fltClosing-fltOpening;
				            		if(Float.parseFloat(editTextTesting.getText().toString())<fltItemQty){
										fltItemQty=(fltItemQty-fltTesting);
										editTextSaleLtrs.setText(fltItemQty.toString());
									}else{
					            		editTextTesting.setError("Testing Cannot be greater than Sales");
					            	}			            		
				            	}else{
				            		editTextClosing.setError("Closing should be greater than Opening");
				            	}
			            	}
			            	}
			            }
			        });
					
					editTextTesting.addTextChangedListener(new TextWatcher() {
						
						@Override
						public void onTextChanged(CharSequence s, int start, int before, int count) {							// TODO Auto-generated method stub							
						}
						@Override
						public void beforeTextChanged(CharSequence s, int start, int count, int after) {				// TODO Auto-generated method stub	
						}
						@Override
						public void afterTextChanged(Editable s) {							// TODO Auto-generated method stub
							if(editTextClosing.getText().toString().equalsIgnoreCase("")||editTextClosing.getText()==null){
								fltClosing = (float) 0.0;
								editTextClosing.setError("Closing Cannot be blank");
			            	}else{
			            		if(editTextTesting.getText().toString().equalsIgnoreCase("")||editTextTesting.getText()==null){
								fltTesting = (float) 0.0;
								editTextTesting.setError("Testing Cannot be blank");
			            	}else {
			            		fltClosing = Float.parseFloat(editTextClosing.getText().toString());
								fltTesting = Float.parseFloat(editTextTesting.getText().toString());
								//fltItemQty = Float.parseFloat(editTextSaleLtrs.getText().toString());
								if(Float.parseFloat(editTextClosing.getText().toString())>fltOpening){
				            		fltItemQty=fltClosing-fltOpening;
				            		if(Float.parseFloat(editTextTesting.getText().toString())<fltItemQty){
										fltItemQty=(fltItemQty-fltTesting);
										editTextSaleLtrs.setText(fltItemQty.toString());
									}else{
					            		editTextTesting.setError("Testing Cannot be greater than Sales");
					            	}			            		
				            	}else{
				            		editTextClosing.setError("Closing should be greater than Opening");
				            	}
			            	}
						}
						}
					});
					
					editTextSaleLtrs.addTextChangedListener(new TextWatcher() {

			            @Override
			            public void onTextChanged(CharSequence s, int start, int before, int count) {  }
			            @Override
			            public void beforeTextChanged(CharSequence s, int start, int count,int after) {	                // TODO Auto-generated method stub	     
			            }
			            @Override
			            public void afterTextChanged(Editable s) {
			            	fltItemQty =Float.parseFloat(editTextSaleLtrs.getText().toString());
			            	fltItemAmt = fltItemRate*fltItemQty;
			            	editTextAmount.setText(fltItemAmt.toString());
			            }
			        });
					
					// set dialog message
					alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Save",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							// get user input and set it to result
						    	if(fltItemQty==null||fltItemQty==0||editTextSaleLtrs.getText().toString().equalsIgnoreCase("")){
						    		editTextSaleLtrs.setError("Please Enter proper Data");
						    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
						    	} else if(fltItemAmt==null||fltItemAmt==0||editTextAmount.getText().toString().equalsIgnoreCase("")){
						    		editTextAmount.setError("Please Enter proper Data");
						    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
						    	} else {
						    		databaseHelper.UpdatePumpList(pump_no, pump_name, item_code, item_desc, pump_seq, fltOpening.toString(),
									fltClosing.toString(), fltTesting.toString(), fltItemQty.toString(), fltItemRate.toString(), fltItemAmt.toString());
						    		Toast.makeText(parent, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
						    		updatelist();
						    	}
						    }
						  })
						.setNegativeButton("Cancel",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							dialog.cancel();
						    }
						  });

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
	}

	protected void getClosing(int position) {
		// TODO Auto-generated method stub
		if (CommonUtilities.isInternetAvailable(parent)) {
			new UpdateClosing().execute();
		} else {
			Toast.makeText(parent, "No internet connection found..",
					Toast.LENGTH_LONG).show();
		}
	}

	protected void getfirstData(int position) {
		// TODO Auto-generated method stub
		pump_no = pumplistBeanList.get(position).getpump_no();
		pump_name = pumplistBeanList.get(position).getpump_name();
		item_code = pumplistBeanList.get(position).getitem_code();
		item_desc = pumplistBeanList.get(position).getitem_desc();
		pump_seq = pumplistBeanList.get(position).getpump_seq();
		closing = pumplistBeanList.get(position).getclosing();
		opening = pumplistBeanList.get(position).getopening();
		testing = pumplistBeanList.get(position).gettesting();
		fltItemQty=Float.parseFloat(pumplistBeanList.get(position).getSaleLtrs());
		fltItemAmt=Float.parseFloat(pumplistBeanList.get(position).getAmount());
		fltClosing=Float.parseFloat(closing);
		fltOpening=Float.parseFloat(opening);
		fltTesting=Float.parseFloat(testing);		
		fltItemRate = getRate(item_desc);
    	ItemRate = fltItemRate.toString();    	
	}

	
	private String splitDatabyspace(String data) {
		// TODO Auto-generated method stub
		String mny[] = data.split("\\s+");
		data = mny[2];
		return (data);		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnPetroSale:
			String data = btnPetroSale.getText().toString();
			PetroSoftData.PETRO_SALE = splitDatabyspace(data);
			Intent intent = new Intent();
			setResult(PetroSoftData.REQUEST_GET_PETRO_SALE_DETAILS_FOR_DSR,
					intent);
			finish();
			break;
		}
	}
	
	private float getRate(String strItemName) {
		// TODO Auto-generated method stub
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		float Rate=0;
		
		Cursor cursor = db.rawQuery(
				"Select ItemRate from Item where ItemName = '"+item_desc+"'", null);
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
		Rate = Float.parseFloat(cursor.getString(0));	
		cursor.close();
		db.close();
		db1.close();
		}
		return Rate;
	}

	private void initView() {
		// TODO Auto-generated method stub
		parent = DSRGetPetroSaleActivity.this;
		databaseHelper = new DatabaseHelper(parent);
		btnPetroSale = (Button) findViewById(R.id.btnPetroSale);
		ivlogo = (ImageView) findViewById(R.id.iconlogo);
		//refresh = (ImageView) findViewById(R.id.button_refresh);
		pumplistlv = (ListView) findViewById(R.id.listview_choose_result_list);
		makedate(PetroSoftData.DATEnTIME);
		shift = String.valueOf(PetroSoftData.SHIFT);
		acno = PetroSoftData.Cashier_acno;
		
		btnPetroSale.setText("Petro Sale:   "+ PetroSoftData.PETRO_SALE);
		if(PetroSoftData.imgPath!=null){
			Picasso.with(DSRGetPetroSaleActivity.this).load(PetroSoftData.imgPath)/*
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)*/
					.into(ivlogo);
		}
		else{
			ivlogo.setImageResource(R.drawable.petro_soft_logo);
		}
	}
	
	private void makedate(String dATEnTIME) {
		// TODO Auto-generated method stub
		 //try {
		Log.e("Date",dATEnTIME);
			 	String xx[] = dATEnTIME.split("-");
			 	String dt = xx[1] +"/"+ xx[2]+"/" + xx[0];
			 	
			 	
			 Log.e("Date",dt);//2017-03-07
			    /*SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			    //eddatentime.setText(sdf.format(myCalendar.getTime()));		    
				Date date1 = sdf.parse(dATEnTIME);
				//05-11 16:00:32.117: E/Parameters(29567): 00/07/2017 I 017001006
			    SimpleDateFormat sdf2 = new SimpleDateFormat("mm/dd/yyyy", Locale.ENGLISH);*/
			    date=dt;
			    		//sdf2.format(date1);
	     /*} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	     }*/
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		String data = btnPetroSale.getText().toString();
		PetroSoftData.PETRO_SALE = splitDatabyspace(data);
		Intent intent = new Intent();
		setResult(PetroSoftData.REQUEST_GET_PETRO_SALE_DETAILS_FOR_DSR,
				intent);
		finish();
	}
	
	class UpdateClosing extends AsyncTask<Void, Void, Void> {
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
			try {

				// String fullURL = getResources().getString(R.string.url);
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_CLOSING);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;

				// adding parameters
				request.addProperty("pump_no", pump_no);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_CLOSING, envelope);

				SoapObject response = (SoapObject) envelope.bodyIn;
				String Text  = response.toString();
		        Log.e("Text : ","Text : "  + Text);
		        String mny[] = Text.split("=");
				Text = mny[1];
				String oth[] = Text.split(";");
				Text = oth[0];
				
					closing = Text;
					opening = Text;
				
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
						"User Data Sync Failed. Please try after some time..",
						Toast.LENGTH_LONG).show();
			} else {
				setPrompt();
				Toast.makeText(parent, "PumpList Sync Successful..",
						Toast.LENGTH_LONG).show();				
			}

		}
		

	}

	class UpdatePumpList extends AsyncTask<Void, Void, Void> {
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
			try {

				// String fullURL = getResources().getString(R.string.url);
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_PUMPLIST);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;
				Log.e("Parameters",date+" "+shift+" "+acno);
				request.addProperty("date", date);
                request.addProperty("shift", shift);
                request.addProperty("acno", acno);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_PUMPLIST, envelope);

				SoapObject response = (SoapObject) envelope.bodyIn;

				SoapObject getPumpListResult = (SoapObject) response.getProperty(0);
				SoapObject newDataSet = (SoapObject) getPumpListResult
						.getProperty(0);
				
				CommonUtilities.clearTable(parent, "PumpList");
				for (int i = 0; i < newDataSet.getPropertyCount(); i++) {
					SoapObject table = (SoapObject) newDataSet.getProperty(i);
					databaseHelper.AddPumpList(table.getProperty("pump_no")
							.toString().trim(), table.getProperty("pump_name")
							.toString().trim(), table.getProperty("item_code")
							.toString().trim(), table.getProperty("item_desc")
							.toString().trim(), table.getProperty("pump_seq")
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
						"User Data Sync Failed. Please try after some time..",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(parent, "PumpList Sync Successful..",
						Toast.LENGTH_LONG).show();	
				updatelist();
			}
		}
	}

	public void updatelist() {
		// TODO Auto-generated method stub
		pumplistBeanList = new ArrayList<PumpListBean>();
		pumplistBeanList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		
		Cursor c = db.rawQuery("Select * from PumpList order by CAST(pump_no as INT)",null);
		if (c.getCount()>0){
			double total = 0.00, Amt;
			c.moveToFirst();
			int column;
			do{
				pumplistBean = new PumpListBean();
				pumplistBean.setpump_no(c.getString(c.getColumnIndex("pump_no")));
				pumplistBean.setpump_name(c.getString(c.getColumnIndex("pump_name")));
				pumplistBean.setitem_code(c.getString(c.getColumnIndex("item_code")));
				pumplistBean.setitem_desc(c.getString(c.getColumnIndex("item_desc")));
				pumplistBean.setpump_seq(c.getString(c.getColumnIndex("pump_seq")));
				pumplistBean.setclosing(c.getString(c.getColumnIndex("closing")));
				pumplistBean.setopening(c.getString(c.getColumnIndex("opening")));
				pumplistBean.settesting(c.getString(c.getColumnIndex("testing")));
				pumplistBean.setSaleLtrs(c.getString(c.getColumnIndex("SaleLtrs")));
				pumplistBean.setRate(c.getString(c.getColumnIndex("Rate")));
				pumplistBean.setAmount(c.getString(c.getColumnIndex("Amount")));
				Amt= Double.parseDouble(c.getString(c.getColumnIndex("Amount")));
				total = total + Amt;
				pumplistBeanList.add(pumplistBean);
			}while(c.moveToNext());			
			String money = CommonUtilities.convertMoney(total);
			btnPetroSale.setText("Petro Sale:   "+ money);
			
			c.close();
			db.close();
			db1.close();
		}
		pumplistAdapter = new PumpListAdapter(parent,pumplistBeanList);
		pumplistlv.setAdapter(pumplistAdapter);
	}
}
