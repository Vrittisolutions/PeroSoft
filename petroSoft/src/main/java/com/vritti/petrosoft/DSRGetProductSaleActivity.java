package com.vritti.petrosoft;

import java.io.File;
import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;
import com.vritti.petrosoft.BillDetails.GetBillNo;
import com.vritti.petrosoft.DSRGetPetroSaleActivity.UpdatePumpList;
import com.vritti.petrosoft.LoginActivity.UpdateItemRateMaster;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DSRGetProductSaleActivity extends Activity implements OnClickListener {
	ImageView btnAdd, ivlogo;
	static Button btnProdSale;
	static ListView list;
	static Context parent;
	public static String xml3;
	static ArrayList<ProdSaleBean> prodSaleBeanList;
	static ProdSaleBean prodSaleBean;
	static ProdSaleAdapter prodSaleAdapter;
	private DatabaseHelper databaseHelper;
	private static StringBuilder sb;
	private String fullURL = PetroSoftData.URL;
	private ArrayList<String> ItemNameList, ItemCodeList;
	int TId;
	String date,shift,acno;
	String strItemName, strItemCode=null, strEndQty;
	Float fltItemRate, fltItemQty, fltItemAmt, fltSaleQTY;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dsr_product_sale);
		
		initViews();

		if (CommonUtilities.isInternetAvailable(parent)) {
			new UpdateProduct().execute();
		} else {
			Toast.makeText(parent, "No internet connection found..",
					Toast.LENGTH_LONG).show();
		}
		if (dbvalue()) {
			updatelist();
		}
		updateCustomerSpinner();
		SetListener();
		
	}
	
	private boolean dbvalue() {
		try {
			DatabaseHelper db1 = new DatabaseHelper(getBaseContext());
			SQLiteDatabase sql = db1.getWritableDatabase();
			Cursor cursor = sql.rawQuery(
					"SELECT *   FROM ProdSaleTable", null);
			if (cursor != null && cursor.getCount() > 0) {

					cursor.close();
					sql.close();
					db1.close();
					return true;

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

	private void initViews() {
		// TODO Auto-generated method stub
		parent = DSRGetProductSaleActivity.this;
		makedate(PetroSoftData.DATEnTIME);
		shift = String.valueOf(PetroSoftData.SHIFT);
		acno = PetroSoftData.Cashier_acno;
		btnProdSale = (Button) findViewById(R.id.btnProductSale);
		ivlogo = (ImageView) findViewById(R.id.iconlogo);
		
		btnProdSale.setText("Product Sale:   "+ PetroSoftData.PROD_SALE);
		btnAdd = (ImageView) findViewById(R.id.button_add);
		list = (ListView) findViewById(R.id.listview_dsr_product_sale_list);
		ItemNameList = new ArrayList<String>();
		ItemCodeList = new ArrayList<String>();
		databaseHelper = new DatabaseHelper(parent);
		if(PetroSoftData.imgPath!=null){
			Picasso.with(DSRGetProductSaleActivity.this).load(PetroSoftData.imgPath)/*
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)*/
					.into(ivlogo);
		}
		else{
			ivlogo.setImageResource(R.drawable.petro_soft_logo);
		}
		//databaseHelper.CreateProdSaleTable();
		}
	
	private void makedate(String dATEnTIME) {
		// TODO Auto-generated method stub
		Log.e("Date",dATEnTIME);
			 	String xx[] = dATEnTIME.split("-");
			 	String dt = xx[1] +"/"+ xx[2]+"/" + xx[0];		 	
			 Log.e("Date",dt);
			    date=dt;
	}

	
	private void SetListener() {
		// TODO Auto-generated method stub
		btnProdSale.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				getfirstData(position);
				
					setPrompt();
				
			}
		});
	}
	
	protected void getfirstData(int position) {
		// TODO Auto-generated method stub
		TId = prodSaleBeanList.get(position).getTId();
		strItemName = prodSaleBeanList.get(position).getItemName();
		strItemCode = prodSaleBeanList.get(position).getItemCode();
		fltSaleQTY = Float.parseFloat(prodSaleBeanList.get(position).getItemQty());
		fltItemAmt = Float.parseFloat(prodSaleBeanList.get(position).getItemAmt());
		fltItemRate = Float.parseFloat(prodSaleBeanList.get(position).getItemRate());    	
	}
	
	protected void setPrompt() {
		// get prompts.xml view
					LayoutInflater li = LayoutInflater.from(parent);
					View promptsView = li.inflate(R.layout.prompt_prodlist, null);
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							parent);
					
					alertDialogBuilder.setCancelable(false);

					// set prompts.xml to alertdialog builder
					alertDialogBuilder.setView(promptsView);

					final AutoCompleteTextView ItemName = (AutoCompleteTextView) promptsView
							.findViewById(R.id.editTextItemNameinProd);
					ItemName.setText(strItemName);
					strItemCode= getItemCode(strItemName);
					final TextView EndQty = (TextView) promptsView
							.findViewById(R.id.tvEndQty);
					strEndQty = getend_qty(strItemCode);
	            	EndQty.setText("Opening: "+strEndQty);
					final EditText QTY = (EditText) promptsView
							.findViewById(R.id.editTextQTYinProd);
					fltItemQty = (Float.parseFloat(strEndQty))-fltSaleQTY;
					QTY.setText(fltItemQty.toString());
					final TextView SaleQTY = (TextView) promptsView
							.findViewById(R.id.tvSaleofProd);
					SaleQTY.setText("Sale : "+ fltSaleQTY.toString());

					final EditText Rate = (EditText) promptsView
							.findViewById(R.id.editTextRateinProd);
					Rate.setText(fltItemRate.toString());
					Rate.setClickable(false);
					Rate.setOnClickListener(null);
					
					final EditText Amt = (EditText) promptsView
							.findViewById(R.id.editTextAmtinProd);
					Amt.setText(fltItemAmt.toString());
					
					ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
							android.R.layout.simple_list_item_1, ItemNameList);
					//adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
					ItemName.setThreshold(1);
					ItemName.setAdapter(adapter1);
					ItemName.setOnTouchListener(new View.OnTouchListener(){
						   @Override
						   public boolean onTouch(View v, MotionEvent event){
						      ItemName.showDropDown();
						      return false;
						   }
						});
					ItemName.addTextChangedListener(new TextWatcher() {

			            @Override
			            public void onTextChanged(CharSequence s, int start, int before, int count) {	            }

			            @Override
			            public void beforeTextChanged(CharSequence s, int start, int count,
			                    int after) {	                // TODO Auto-generated method stub	     
			            }

			            @Override
			            public void afterTextChanged(Editable s) {
			            	strItemName = ItemName.getText().toString();
			            	strItemCode= getItemCode(strItemName);
			            	fltItemRate = getRate(strItemName);
			            	strEndQty = getend_qty(strItemCode);
			            	EndQty.setText("Opening: "+strEndQty);
			            	Rate.setText(fltItemRate.toString());
			            }
			        });
					
					QTY.addTextChangedListener(new TextWatcher() {

			            @Override
			            public void onTextChanged(CharSequence s, int start, int before, int count) {	
			            	
			            	if(QTY.getText().toString().equals("")||QTY.equals(null)){
			            		fltItemQty =(float) 0;
			            	}else{
			            	fltItemQty =Float.parseFloat(QTY.getText().toString());
			            	}
			            	fltSaleQTY = (Float.parseFloat(strEndQty))-fltItemQty;
			            	
			            	SaleQTY.setText("Sale : "+fltSaleQTY);
			            	fltItemAmt = fltItemRate*fltSaleQTY;
			            	
			            	Amt.setText(fltItemAmt.toString());
			            	
			            	
			            }

			            @Override
			            public void beforeTextChanged(CharSequence s, int start, int count,
			                    int after) {	                // TODO Auto-generated method stub	     
			            }

			            @Override
			            public void afterTextChanged(Editable s) {	            }
			        });
				

					// set dialog message
					alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Save",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
						    
							// get user input and set it to result
						    	if(strItemName==null||ItemName.getText().toString().equalsIgnoreCase("")){
						    		ItemName.setError("Please Select Item Name");
						    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
						    	} else if(fltItemQty==null||fltItemQty==0||QTY.getText().toString().equalsIgnoreCase("")){
						    		QTY.setError("Please Enter Quantity");
						    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
						    	} else {
							strItemCode= getItemCode(strItemName);
							if(strItemCode!=null){
							databaseHelper.UpdateProdSaleTable(TId,strItemName, strItemCode, fltSaleQTY.toString(), fltItemRate.toString(), fltItemAmt.toString());
							Toast.makeText(parent, "Data Updated Successfully", Toast.LENGTH_LONG).show();
							}
							updatelist();
							//dialog.dismiss();
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
								alertDialog.setCancelable(false);

					// show it
					alertDialog.show();

	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnProductSale:
			String data = btnProdSale.getText().toString();
			PetroSoftData.PROD_SALE = splitDatabyspace(data);
			Intent intent = new Intent();
			setResult(PetroSoftData.REQUEST_GET_PRODUCT_SALE_DETAILS_FOR_DSR,
					intent);
			finish();
			break;
		case R.id.button_add:
			// get prompts.xml view
			LayoutInflater li = LayoutInflater.from(parent);
			View promptsView = li.inflate(R.layout.prompt_prodlist, null);
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					parent);
			
			alertDialogBuilder.setCancelable(false);

			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final AutoCompleteTextView ItemName = (AutoCompleteTextView) promptsView
					.findViewById(R.id.editTextItemNameinProd);
			//ItemName.setText(strItemName);
			final EditText QTY = (EditText) promptsView
					.findViewById(R.id.editTextQTYinProd);
			//QTY.setText(fltItemQty.toString());
			final EditText Rate = (EditText) promptsView
					.findViewById(R.id.editTextRateinProd);
			//Rate.setText(fltItemRate.toString());
			Rate.setClickable(false);
			Rate.setOnClickListener(null);
			
			final EditText Amt = (EditText) promptsView
					.findViewById(R.id.editTextAmtinProd);
			//Amt.setText(fltItemAmt.toString());
			final TextView EndQty = (TextView) promptsView
					.findViewById(R.id.tvEndQty);
			
			final TextView SaleQTY = (TextView) promptsView
					.findViewById(R.id.tvSaleofProd);
			
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
					android.R.layout.simple_list_item_1, ItemNameList);
			//adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
			ItemName.setThreshold(1);
			ItemName.setAdapter(adapter1);
			ItemName.setOnTouchListener(new View.OnTouchListener(){
				   @Override
				   public boolean onTouch(View v, MotionEvent event){
				      ItemName.showDropDown();
				      return false;
				   }
				});
			ItemName.addTextChangedListener(new TextWatcher() {

	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {	            }

	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count,
	                    int after) {	                // TODO Auto-generated method stub	     
	            }

	            @Override
	            public void afterTextChanged(Editable s) {
	            	strItemName = ItemName.getText().toString();
	            	strItemCode= getItemCode(strItemName);
	            	fltItemRate = getRate(strItemName);
	            	strEndQty = getend_qty(strItemCode);
	            	EndQty.setText("Opening: "+strEndQty);
	            	Rate.setText(fltItemRate.toString());
	            }
	        });
			ItemName.setOnTouchListener(new View.OnTouchListener(){
				   @Override
				   public boolean onTouch(View v, MotionEvent event){
				      ItemName.showDropDown();
				      return false;
				   }
				});
			QTY.addTextChangedListener(new TextWatcher() {

	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {
	            	if(QTY.getText().toString().equals("")||QTY.equals(null)){
	            		fltItemQty =(float) 0;
	            	}else{
	            	fltItemQty =Float.parseFloat(QTY.getText().toString());
	            	}
	            	fltSaleQTY = (Float.parseFloat(strEndQty))-fltItemQty;
	            	
	            	SaleQTY.setText("Sale : "+fltSaleQTY);
	            	fltItemAmt = fltItemRate*fltSaleQTY;
	            	Amt.setText(fltItemAmt.toString());
	            	
	            }

	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count,
	                    int after) {	                // TODO Auto-generated method stub	     
	            }

	            @Override
	            public void afterTextChanged(Editable s) {	            }
	        });
		

			// set dialog message
			alertDialogBuilder
				.setCancelable(false)
				.setPositiveButton("Save",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
				    
					// get user input and set it to result
				    	if(strItemName==null||ItemName.getText().toString().equalsIgnoreCase("")){
				    		ItemName.setError("Please Select Item Name");
				    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
				    	} else if(fltItemQty==null||fltItemQty==0||QTY.getText().toString().equalsIgnoreCase("")){
				    		QTY.setError("Please Enter Quantity");
				    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
				    	} else {
					strItemCode= getItemCode(strItemName);
					if(strItemCode!=null){
					databaseHelper.AddProdSaleTable(strItemName, strItemCode, fltSaleQTY.toString(), fltItemRate.toString(), fltItemAmt.toString());
					Toast.makeText(parent, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
					}
					updatelist();
					//dialog.dismiss();
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
						alertDialog.setCancelable(false);

			// show it
			alertDialog.show();

			break;
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		String data = btnProdSale.getText().toString();
		PetroSoftData.PROD_SALE = splitDatabyspace(data);
		Intent intent = new Intent();
		setResult(PetroSoftData.REQUEST_GET_PRODUCT_SALE_DETAILS_FOR_DSR,
				intent);
		finish();
	}
	
	private String splitDatabyspace(String data) {
		// TODO Auto-generated method stub
		String mny[] = data.split("\\s+");
		data = mny[2];
		return (data);		
	}

	public static void updatelist() {
		// TODO Auto-generated method stub
		prodSaleBeanList = new ArrayList<ProdSaleBean>();
		prodSaleBeanList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		
		Cursor c = db.rawQuery("Select * from ProdSaleTable",null);
		if (c.getCount()>0){
			double total = 0.00, Amt;
			c.moveToFirst();
			int column;
			do{
				prodSaleBean = new ProdSaleBean();
				prodSaleBean.setTId(c.getInt(c.getColumnIndex("TId")));
				prodSaleBean.setItemName(c.getString(c.getColumnIndex("ItemName")));
				prodSaleBean.setItemCode(c.getString(c.getColumnIndex("ItemCode")));
				prodSaleBean.setItemQty(c.getString(c.getColumnIndex("ItemQty")));
				prodSaleBean.setItemRate(c.getString(c.getColumnIndex("ItemRate")));
				prodSaleBean.setItemAmt(c.getString(c.getColumnIndex("ItemAmt")));
				Amt= Double.parseDouble(c.getString(c.getColumnIndex("ItemAmt")));
				total = total + Amt;
				prodSaleBeanList.add(prodSaleBean);
			}while(c.moveToNext());
			String money = CommonUtilities.convertMoney(total);
			btnProdSale.setText("Product Sale:   "+ money);
			c.close();
			db.close();
			db1.close();
		}
		prodSaleAdapter = new ProdSaleAdapter(parent,prodSaleBeanList);
		list.setAdapter(prodSaleAdapter);
		
	}
	
	private float getRate(String strItemName) {
		// TODO Auto-generated method stub
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		float Rate=0;
		
		Cursor cursor = db.rawQuery(
				"Select ItemRate from Item where ItemName = '"+strItemName+"'", null);
		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
		Rate = Float.parseFloat(cursor.getString(0));	
		cursor.close();
		db.close();
		db1.close();
		}
		return Rate;

	}
	
	private String getItemCode(String strItemName) {
		// TODO Auto-generated method stub
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		String ItemCode=null;
		
		Cursor cursor = db.rawQuery(
				"Select ItemCode from Item where ItemName = '"+strItemName+"'", null);
		if (cursor.getCount()==0){
			Toast.makeText(parent, "Item not available", Toast.LENGTH_SHORT).show();
		}
		else if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			ItemCode = cursor.getString(0);	
		cursor.close();
		db.close();
		db1.close();
		}
		return ItemCode;

	}
	
	private String getend_qty(String strItemCode) {
		// TODO Auto-generated method stub
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		String end_qty=null;
		
		Cursor cursor = db.rawQuery(
				"Select end_qty from Product where item_code = '"+strItemCode+"'", null);
		if (cursor.getCount()==0){
			Toast.makeText(parent, "Item not available", Toast.LENGTH_SHORT).show();
		}
		else if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			end_qty = cursor.getString(0);	
		cursor.close();
		db.close();
		db1.close();
		}
		return end_qty;

	}

	private void updateCustomerSpinner() {
		ItemNameList.clear();
		ItemCodeList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		// db.execSQL("CREATE TABLE CustomerMaster(AcNo TEXT,Name TEXT,City TEXT)");
		Cursor cursor = db.rawQuery("SELECT a.ItemName,a.ItemCode,a.ItemRate FROM Item a INNER JOIN Product b ON a.ItemCode=b.item_code where NOT(a.ItemGroup = 'PETRO') order by a.ItemName ASC", null);
		//Cursor cursor = db.rawQuery("Select ItemName,ItemCode,ItemRate from Item where NOT(ItemGroup = 'PETRO') order by ItemName ASC", null);

		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			do {
				ItemNameList.add(cursor.getString(0));
				ItemCodeList.add(cursor.getString(1));
			} while (cursor.moveToNext());

			cursor.close();
			db.close();
			db1.close();
			//custCode = ItemCodeList.get(0);
		}
		
	}

	class UpdateProduct extends AsyncTask<Void, Void, Void> {

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
			// Log.e("UpdateDatabase", "1");
			// if (!fullURL.equals(null)) {
			// // Toast.makeText(getApplicationContext(), "IP address not set",
			// // Toast.LENGTH_LONG).show();
			// return null;
			// }
			// Log.e("UpdateDatabase", "2 : " + fullURL);
			try {
				// String fullURL = getResources().getString(R.string.url);
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_PRODUCT);
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
						+ PetroSoftData.METHOD_GET_PRODUCT, envelope);
				Log.e("UpdateDatabase", "3 : ");
				SoapObject response = (SoapObject) envelope.bodyIn;
				Log.e("UpdateDatabase", "4 : ");
				SoapObject GetUserDetailsResult = (SoapObject) response
						.getProperty(0);
				SoapObject NewDataSet = (SoapObject) GetUserDetailsResult
						.getProperty(0);
				CommonUtilities.clearTable(parent, "Product");
				Log.e("UpdateDatabase", "5 : " + NewDataSet.getPropertyCount());
				for (int i = 0; i < NewDataSet.getPropertyCount(); i++) {
					SoapObject Table = (SoapObject) NewDataSet.getProperty(i);
					databaseHelper.AddProduct(Table.getProperty("item_code")
							.toString().trim(), Table.getProperty("end_qty")
							.toString().trim());
					Log.e("UpdateDatabase",
							"6 : " + Table.getProperty("item_code").toString()
									+ ":"
									+ Table.getProperty("end_qty").toString());
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
						"Product Sync Failed. Please try after some time..",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(parent, "Product Sync Successful..",
						Toast.LENGTH_LONG).show();
				//new UpdateItemRateMaster().execute();
			}
		}
	}

}
