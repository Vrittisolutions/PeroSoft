package com.vritti.petrosoft;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;

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
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class CashActivity1 extends Activity {
	EditText editTextAmt2000,editTextAmt1000,editTextAmt500,editTextAmt200,editTextAmt100,editTextAmt50,
	editTextAmt20,editTextAmt10,editTextAmt5,editTextAmt2,editTextAmt1,editTextAmtCoins,editTextAmttotal;
	Button btnCashSave;
	Context parent;
	ImageView ivlogo;
	DatabaseHelper databaseHelper;
	String totalcount,AcName, Narration;
	String dbvalue="0",TId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.prompt_cash);
		initView();
		if(TId.equals("")){
			if(dbvalueCash()){
				setfieldsCash();		
			}
		}else{
			if(dbvalueBank()){
				setfieldsBank();		
			}
		}
		setListener();
	}
	private void setfieldsBank() {
		// TODO Auto-generated method stub
		DatabaseHelper db1 = new DatabaseHelper(getBaseContext());
		SQLiteDatabase sql = db1.getWritableDatabase();
		Cursor c = sql.rawQuery("SELECT * FROM BankDepoTable where TId ='" + Integer.parseInt(TId)+"'", null);
		if (c.getCount()>0){
			double total = 0.00, Amt;
			c.moveToFirst();
			int column;
			do{
				editTextAmt2000.setText(c.getString(c.getColumnIndex("Rs2000")));
				editTextAmt1000.setText(c.getString(c.getColumnIndex("Rs1000")));
				editTextAmt500.setText(c.getString(c.getColumnIndex("Rs500")));
				editTextAmt200.setText(c.getString(c.getColumnIndex("Rs200")));
				editTextAmt100.setText(c.getString(c.getColumnIndex("Rs100")));
				editTextAmt50.setText(c.getString(c.getColumnIndex("Rs50")));
				editTextAmt20.setText(c.getString(c.getColumnIndex("Rs20")));
				editTextAmt10.setText(c.getString(c.getColumnIndex("Rs10")));
				editTextAmt5.setText(c.getString(c.getColumnIndex("Rs5")));
				editTextAmt2.setText(c.getString(c.getColumnIndex("Rs2")));
				editTextAmt1.setText(c.getString(c.getColumnIndex("Rs1")));
				editTextAmtCoins.setText(c.getString(c.getColumnIndex("Coins")));
				editTextAmttotal.setText(c.getString(c.getColumnIndex("RpDt_Total")));
			}while(c.moveToNext());
			c.close();
			sql.close();
			db1.close();
		}
		
	}

	private void setfieldsCash() {
		// TODO Auto-generated method stub
		DatabaseHelper db1 = new DatabaseHelper(getBaseContext());
		SQLiteDatabase sql = db1.getWritableDatabase();
		Cursor c = sql.rawQuery("SELECT * FROM CashTable", null);
		if (c.getCount()>0){
			double total = 0.00, Amt;
			c.moveToFirst();
			int column;
			do{
				editTextAmt2000.setText(c.getString(c.getColumnIndex("Rs2000")));
				editTextAmt1000.setText(c.getString(c.getColumnIndex("Rs1000")));
				editTextAmt500.setText(c.getString(c.getColumnIndex("Rs500")));
				editTextAmt200.setText(c.getString(c.getColumnIndex("Rs200")));
				editTextAmt100.setText(c.getString(c.getColumnIndex("Rs100")));
				editTextAmt50.setText(c.getString(c.getColumnIndex("Rs50")));
				editTextAmt20.setText(c.getString(c.getColumnIndex("Rs20")));
				editTextAmt10.setText(c.getString(c.getColumnIndex("Rs10")));
				editTextAmt5.setText(c.getString(c.getColumnIndex("Rs5")));
				editTextAmt2.setText(c.getString(c.getColumnIndex("Rs2")));
				editTextAmt1.setText(c.getString(c.getColumnIndex("Rs1")));
				editTextAmtCoins.setText(c.getString(c.getColumnIndex("Coins")));
				editTextAmttotal.setText(c.getString(c.getColumnIndex("RpDt_Total")));
			}while(c.moveToNext());
			c.close();
			sql.close();
			db1.close();
		}
		
	}
	private boolean dbvalueCash() {
		try {
			DatabaseHelper db1 = new DatabaseHelper(getBaseContext());
			SQLiteDatabase sql = db1.getWritableDatabase();
			Cursor cursor = sql.rawQuery("SELECT * FROM CashTable", null);
			if (cursor != null && cursor.getCount() > 0) {
					cursor.close();	sql.close();	db1.close();
					dbvalue="1";
					return true;
			} else {
				cursor.close();	sql.close();	db1.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean dbvalueBank() {
		try {
			DatabaseHelper db1 = new DatabaseHelper(getBaseContext());
			SQLiteDatabase sql = db1.getWritableDatabase();
			Cursor cursor = sql.rawQuery("SELECT * FROM BankDepoTable", null);
			if (cursor != null && cursor.getCount() > 0) {
					cursor.close();	sql.close();	db1.close();
					return true;
			} else {
				cursor.close();	sql.close();	db1.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void setListener() {
		// TODO Auto-generated method stub
		
		btnCashSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (dbvalue.equals("1")) {
					
					calctotal();
					totalcount=editTextAmttotal.getText().toString();
	                if(!(totalcount.equals("0"))){
					databaseHelper.UpdateCashTable(1,
							editTextAmt2000.getText().toString(),
							editTextAmt1000.getText().toString(), 
							editTextAmt500.getText().toString(),
							editTextAmt200.getText().toString(),
							editTextAmt100.getText().toString(), 
							editTextAmt50.getText().toString(), 
							editTextAmt20.getText().toString(),
							editTextAmt10.getText().toString(),
							editTextAmt5.getText().toString(), 
							editTextAmt2.getText().toString(), 
							editTextAmt1.getText().toString(), 
							editTextAmtCoins.getText().toString(), 
							editTextAmttotal.getText().toString());
					Toast.makeText(parent, "Data Updated Successfully", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent(CashActivity1.this,DSRGetBankDepoActivity.class);
					intent.putExtra("diff", "Cash");
					intent.putExtra("TId",TId);
					intent.putExtra("total", totalcount);
					intent.putExtra("AcName", AcName);
					intent.putExtra("Narration", Narration);
					startActivity(intent);
					finish();
	                }else{
	                	Toast.makeText(parent, "Invalid data", Toast.LENGTH_SHORT).show();
	                }
					
				}else {				
				calctotal();
				totalcount=editTextAmttotal.getText().toString();
                if(!(totalcount.equals("0"))){
                	databaseHelper.CreateCashTable();
				databaseHelper.AddCashTable(
						editTextAmt2000.getText().toString(),
						editTextAmt1000.getText().toString(), 
						editTextAmt500.getText().toString(),
						editTextAmt200.getText().toString(),
						editTextAmt100.getText().toString(), 
						editTextAmt50.getText().toString(), 
						editTextAmt20.getText().toString(),
						editTextAmt10.getText().toString(),
						editTextAmt5.getText().toString(), 
						editTextAmt2.getText().toString(), 
						editTextAmt1.getText().toString(), 
						editTextAmtCoins.getText().toString(), 
						editTextAmttotal.getText().toString());
				Toast.makeText(parent, "Data Added Successfully", Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(CashActivity1.this,DSRGetBankDepoActivity.class);
				intent.putExtra("diff", "Cash");
				intent.putExtra("TId",TId);
				intent.putExtra("total", editTextAmttotal.getText().toString());
				intent.putExtra("AcName", AcName);
				intent.putExtra("Narration", Narration);
				startActivity(intent);
				finish();
                }else{
                	Toast.makeText(parent, "Invalid data", Toast.LENGTH_SHORT).show();
                }
                
				
			}
			}
		});			
		
		//editTextAmttotal.setOnClickListener(null);
		editTextAmttotal.setFocusable(false);
		
		editTextAmt1.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				calctotal();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editTextAmt2.addTextChangedListener(new TextWatcher() {
					
					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						// TODO Auto-generated method stub
						calctotal();			
					}
					
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void afterTextChanged(Editable s) {
						// TODO Auto-generated method stub
						
					}
				});
		editTextAmt5.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				calctotal();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editTextAmt10.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				calctotal();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editTextAmt20.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				calctotal();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editTextAmt50.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				calctotal();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editTextAmt100.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				calctotal();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editTextAmt200.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				calctotal();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		editTextAmt500.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				calctotal();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editTextAmt1000.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				calctotal();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editTextAmt2000.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				calctotal();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		editTextAmtCoins.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				calctotal();			
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		parent = CashActivity1.this;
		Intent i = getIntent();
		TId =i.getStringExtra("TId");
		System.out.println("StrTId-1 :"+TId);
		AcName = i.getStringExtra("AcName");
		Narration = i.getStringExtra("Narration");
		editTextAmt2000 = (EditText) findViewById(R.id.editTextAmt2000);
		editTextAmt2000.setText("0");
		editTextAmt1000 = (EditText) findViewById(R.id.editTextAmt1000);
		editTextAmt1000.setText("0");
		editTextAmt500 = (EditText) findViewById(R.id.editTextAmt500);
		editTextAmt500.setText("0");
		editTextAmt200 = (EditText) findViewById(R.id.editTextAmt200);
		editTextAmt200.setText("0");
		editTextAmt100 = (EditText) findViewById(R.id.editTextAmt100);
		editTextAmt100.setText("0");
		editTextAmt50 = (EditText) findViewById(R.id.editTextAmt50);
		editTextAmt50.setText("0");
		editTextAmt20 = (EditText) findViewById(R.id.editTextAmt20);
		editTextAmt20.setText("0");
		editTextAmt10 = (EditText) findViewById(R.id.editTextAmt10);
		editTextAmt10.setText("0");
		editTextAmt5 = (EditText) findViewById(R.id.editTextAmt5);
		editTextAmt5.setText("0");
		editTextAmt2 = (EditText) findViewById(R.id.editTextAmt2);
		editTextAmt2.setText("0");
		editTextAmt1 = (EditText) findViewById(R.id.editTextAmt1);
		editTextAmt1.setText("0");
		editTextAmtCoins = (EditText) findViewById(R.id.editTextAmtCoins);
		editTextAmtCoins.setText("0");
		editTextAmttotal = (EditText) findViewById(R.id.editTextAmttotal);
		
		btnCashSave = (Button) findViewById(R.id.btnCashSave);

		databaseHelper = new DatabaseHelper(parent);
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

	private void calctotal(){
		long total = 0, total1 = 0, total100 = 0, total1000 =0 , total2 = 0, total10 =0 , total20 =0,
				total200=0 ,total2000 =0, total5=0, total50=0, total500=0 , totalCoins = 0;
		int qty;
		if((editTextAmt2000.getText().toString()!=null)&&(!(editTextAmt2000.getText().toString().equals("")))){
		 total2000 = Integer.parseInt(editTextAmt2000.getText().toString()) * 2000;
		}
		if((editTextAmt1000.getText().toString()!=null)&&(!(editTextAmt1000.getText().toString().equals("")))){
		 total1000 = Integer.parseInt(editTextAmt1000.getText().toString()) * 1000;
		}
		if((editTextAmt500.getText().toString()!=null)&&(!(editTextAmt500.getText().toString().equals("")))){
		 total500 = Integer.parseInt(editTextAmt500.getText().toString()) * 500;
		}
		if((editTextAmt200.getText().toString()!=null)&&(!(editTextAmt200.getText().toString().equals("")))){
			total200 = Integer.parseInt(editTextAmt200.getText().toString()) * 200;
		}
		if((editTextAmt100.getText().toString()!=null)&&(!(editTextAmt100.getText().toString().equals("")))){
		 total100 = Integer.parseInt(editTextAmt100.getText().toString()) * 100;
		}
		if((editTextAmt50.getText().toString()!=null)&&(!(editTextAmt50.getText().toString().equals("")))){
		 total50 = Integer.parseInt(editTextAmt50.getText().toString()) * 50;
		}
		if((editTextAmt20.getText().toString()!=null)&&(!(editTextAmt20.getText().toString().equals("")))){
		 total20 = Integer.parseInt(editTextAmt20.getText().toString()) * 20;
		}
		if((editTextAmt10.getText().toString()!=null)&&(!(editTextAmt10.getText().toString().equals("")))){
		 total10 = Integer.parseInt(editTextAmt10.getText().toString()) * 10;
		}
		if((editTextAmt5.getText().toString()!=null)&&(!(editTextAmt5.getText().toString().equals("")))){
		 total5 = Integer.parseInt(editTextAmt5.getText().toString()) * 5;
		}
		if((editTextAmt2.getText().toString()!=null)&&(!(editTextAmt2.getText().toString().equals("")))){
		 total2 = Integer.parseInt(editTextAmt2.getText().toString()) * 2;
		}
		if((editTextAmt1.getText().toString()!=null)&&(!(editTextAmt1.getText().toString().equals("")))){
		 total1 = Integer.parseInt(editTextAmt1.getText().toString()) * 1;
		}
		if((editTextAmtCoins.getText().toString()!=null)&&(!(editTextAmtCoins.getText().toString().equals("")))){
			 totalCoins = Integer.parseInt(editTextAmtCoins.getText().toString()) * 1;
			}
		 total = total1 + total2 + total5 + total10 + total20 + total50 + total100 + total200 + total500 + total1000 + total2000 + totalCoins;
		 editTextAmttotal.setText(String.valueOf(total));
		 Log.e("Total", String.valueOf(total));
		
	}

}
