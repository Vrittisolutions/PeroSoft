package com.vritti.petrosoft;

import java.io.File;
import java.util.ArrayList;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DSRGetBankDepoActivity extends Activity implements OnClickListener{
	Context parent;
	TextView tvAccountName,tvAmount;
	ListView list;
	ImageView btnAdd,ivlogo;
	Button btnbankdepo;
	DatabaseHelper databaseHelper;
	private ArrayList<String> NameList;
	ArrayList<BankDepoBean> bankDepoBeanList;
	BankDepoBean bankDepoBean;
	BankDepoAdapter bankDepoAdapter;
	int TId;
	String Name, Acno=null, AcType=null, Amount, Narration,total,StrTId,AddClick,diff=null;
	String RpDt_Total="0",Rs2000="0",Rs1000="0",Rs500="0",Rs200="0",Rs100="0",Rs50="0",Rs20="0",Rs10="0",Rs5="0",Rs2="0",Rs1="0",Coins="0";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dsr_bank_deposit);
		initViews();
		//CommonUtilities.clearTable(parent, "BankDepoTable");
		if (dbvalue()) {
			updatelist();
		}
		updateCustomerSpinner();
		SetListener();
	}
	
	private void initViews() {
		// TODO Auto-generated method stub
		parent = DSRGetBankDepoActivity.this;
		tvAccountName = (TextView) findViewById(R.id.tvAccountName);
		tvAmount = (TextView) findViewById(R.id.tvAmount);
		btnAdd = (ImageView) findViewById(R.id.button_add);
		ivlogo = (ImageView) findViewById(R.id.iconlogo);
				list = (ListView) findViewById(R.id.listview_dsr_other_list);
		btnbankdepo = (Button) findViewById(R.id.btnbankdepo);
		databaseHelper = new DatabaseHelper(parent);
		NameList = new ArrayList<String>();
		btnbankdepo.setText("Cash Deposit: "+PetroSoftData.BANK_DEPO);
		if(PetroSoftData.imgPath!=null){
			Picasso.with(parent).load(PetroSoftData.imgPath)/*
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)*/
					.into(ivlogo);
		}
		else{
			ivlogo.setImageResource(R.drawable.petro_soft_logo);
		}
		Intent intent=getIntent();
		total =intent.getStringExtra("total");
		diff = intent.getStringExtra("diff");
		StrTId = intent.getStringExtra("TId");
		System.out.println("diff"+diff);
		System.out.println("StrTId"+StrTId);
		System.out.println("total"+total);
		 if((StrTId!=null)&&(!(StrTId.equals("")))){	
			 if(total!=null){
					Name=intent.getStringExtra("AcName");
					Narration=intent.getStringExtra("Narration");
					Amount = total;
					TId = Integer.parseInt(StrTId);
					getfields();
					setPrompt();
				}
		 }else {
			if(total!=null){
				Name=intent.getStringExtra("AcName");
				Narration=intent.getStringExtra("Narration");
				AddClick="Cash";
				getfields();
				Adddialog();
			}
		}

	}

	private boolean dbvalue() {
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

	public void updatelist() {
		// TODO Auto-generated method stub
		bankDepoBeanList = new ArrayList<BankDepoBean>();
		bankDepoBeanList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		
		Cursor c = db.rawQuery("Select * from BankDepoTable",null);
		if (c.getCount()>0){
			double total = 0.00, Amt;
			c.moveToFirst();
			int column;
			do{
				bankDepoBean = new BankDepoBean();
				bankDepoBean.setTId(c.getInt(c.getColumnIndex("TId")));
				bankDepoBean.setAcno(c.getString(c.getColumnIndex("Acno")));
				bankDepoBean.setName(c.getString(c.getColumnIndex("Name")));
				bankDepoBean.setAcType(c.getString(c.getColumnIndex("AcType")));
				bankDepoBean.setAmount(c.getString(c.getColumnIndex("Amount")));
				bankDepoBean.setNarration(c.getString(c.getColumnIndex("Narration")));
				bankDepoBean.setRs2000(c.getString(c.getColumnIndex("Rs2000")));
				bankDepoBean.setRs1000(c.getString(c.getColumnIndex("Rs1000")));
				bankDepoBean.setRs500(c.getString(c.getColumnIndex("Rs500")));
				bankDepoBean.setRs200(c.getString(c.getColumnIndex("Rs200")));
				bankDepoBean.setRs100(c.getString(c.getColumnIndex("Rs100")));
				bankDepoBean.setRs50(c.getString(c.getColumnIndex("Rs50")));
				bankDepoBean.setRs20(c.getString(c.getColumnIndex("Rs20")));
				bankDepoBean.setRs10(c.getString(c.getColumnIndex("Rs10")));
				bankDepoBean.setRs5(c.getString(c.getColumnIndex("Rs5")));
				bankDepoBean.setRs2(c.getString(c.getColumnIndex("Rs2")));
				bankDepoBean.setRs1(c.getString(c.getColumnIndex("Rs1")));
				bankDepoBean.setCoins(c.getString(c.getColumnIndex("Coins")));
				bankDepoBean.setRpDt_Total(c.getString(c.getColumnIndex("RpDt_Total")));
				Amt= Double.parseDouble(c.getString(c.getColumnIndex("Amount")));
				System.out.println("Amount"+Amt);
				total = total + Amt;
				bankDepoBeanList.add(bankDepoBean);
			}while(c.moveToNext());
			String money = CommonUtilities.convertMoney(total);
			btnbankdepo.setText("Cash Deposit:   "+ money);
			c.close();
			db.close();
			db1.close();
		}
		bankDepoAdapter = new BankDepoAdapter(parent,bankDepoBeanList);
		list.setAdapter(bankDepoAdapter);
		
	}

	private void updateCustomerSpinner() {
		NameList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		// db.execSQL("CREATE TABLE CustomerMaster(AcNo TEXT,Name TEXT,City TEXT)");

		Cursor cursor = db.rawQuery(
				"Select Name from CashierMaster where AcType = 'Bank' order by Name ASC", null);

		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			do {
				NameList.add(cursor.getString(0));
				//ItemCodeList.add(cursor.getString(1));
			} while (cursor.moveToNext());

			cursor.close();
			db.close();
			db1.close();
			//custCode = ItemCodeList.get(0);
		}
		
	}

	private void SetListener() {
		// TODO Auto-generated method stub
		btnbankdepo.setOnClickListener(this);
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
		TId = bankDepoBeanList.get(position).getTId();
		Name = bankDepoBeanList.get(position).getName();
		Acno = bankDepoBeanList.get(position).getAcno();
		AcType = bankDepoBeanList.get(position).getAcno();
		Amount = bankDepoBeanList.get(position).getAmount();
		Narration = bankDepoBeanList.get(position).getNarration();    	
	}
	
	protected void setPrompt() {
		LayoutInflater li = LayoutInflater.from(parent);
		View promptsView = li.inflate(R.layout.prompt_bank, null);			

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parent);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final AutoCompleteTextView editTextAccName = (AutoCompleteTextView) promptsView
				.findViewById(R.id.editTextAccName);
		editTextAccName.setText(Name);
		final LinearLayout llb = (LinearLayout) promptsView
				.findViewById(R.id.llforbank);
		llb.setVisibility(View.VISIBLE);
		final Button btncash = (Button) promptsView
				.findViewById(R.id.btncash);
		final EditText editTextAmt = (EditText) promptsView
				.findViewById(R.id.editTextAmt1);
		editTextAmt.setText(Amount);
		final LinearLayout llo = (LinearLayout) promptsView
				.findViewById(R.id.llforother);
		llo.setVisibility(View.GONE);
		final EditText editTextNarration = (EditText) promptsView
				.findViewById(R.id.editTextNarration);
		editTextNarration.setText(Narration);
		
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
				android.R.layout.simple_list_item_1, NameList);
		//adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
		editTextAccName.setThreshold(1);
		editTextAccName.setAdapter(adapter1);
		
		editTextAccName.setOnTouchListener(new View.OnTouchListener(){
			   @Override
			   public boolean onTouch(View v, MotionEvent event){
			      editTextAccName.showDropDown();
			      return false;
			   }
			});
		
		btncash.setOnClickListener(new OnClickListener() {
		    public void onClick(View v)
		    {
		    	Intent i = new Intent(getBaseContext(),CashActivity1.class);
		    	i.putExtra("TId",String.valueOf(TId));
		    	i.putExtra("AcName",editTextAccName.getText().toString());
		    	i.putExtra("Narration",editTextNarration.getText().toString());
		    	startActivity(i);
		    } 
		});
		
		// set dialog message
					alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Save",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							// get user input and set it to result
						    	Name = editTextAccName.getText().toString();
						    	Amount = editTextAmt.getText().toString();
						    	Narration = editTextNarration.getText().toString();
						    	if(Name==null||editTextAccName.getText().toString().equalsIgnoreCase("")){
						    		editTextAccName.setError("Please Select Name");
						    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
						    	} else if(Amount==null||editTextAmt.getText().toString().equalsIgnoreCase("")){
						    		editTextAccName.setError("Please Enter Amount");
						    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
						    	} else {
						    	getAcCode(Name);
							    	if(Acno!=null&&AcType!=null){
							    		/*if(dbvalueCash()){
							    		getfields();
							    		}*/
							    		databaseHelper.UpdateBankDepoTable(TId, Acno, Name, AcType, Amount, Narration,Rs2000,Rs1000,Rs500,Rs200,Rs100,Rs50,Rs20,Rs10,Rs5,Rs2,Rs1,Coins,RpDt_Total);
							    		databaseHelper.DropCashTable();
							    		Toast.makeText(parent, "Data Updated Successfully", Toast.LENGTH_LONG).show();
							    	}
							    	System.out.println("in if");
						    		updatelist();							    	
						    	}
						    }
						  })
						.setNegativeButton("Cancel",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {

								dialog.cancel();
								//onResume();
						    }
						  });

					// create alert dialog
					AlertDialog alertDialog = alertDialogBuilder.create();

					// show it
					alertDialog.show();
					
					


		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnbankdepo:
			String data = btnbankdepo.getText().toString();
			PetroSoftData.BANK_DEPO = splitDatabyspace(data);
			Intent intent = new Intent();
			setResult(PetroSoftData.REQUEST_GET_BANK_DEPOSIT_FOR_DSR,
					intent);
			finish();
			break;
		case R.id.button_add:
			AddClick="new";
			
			Adddialog();
			
			break;
		}
	}

	private void Adddialog() {
		// TODO Auto-generated method stub
		LayoutInflater li = LayoutInflater.from(parent);
		View promptsView = li.inflate(R.layout.prompt_bank, null);			

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parent);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final AutoCompleteTextView editTextAccName = (AutoCompleteTextView) promptsView
				.findViewById(R.id.editTextAccName);
		final LinearLayout llb = (LinearLayout) promptsView
				.findViewById(R.id.llforbank);
		llb.setVisibility(View.VISIBLE);
		final Button btncash = (Button) promptsView
						.findViewById(R.id.btncash);
		final EditText editTextAmt = (EditText) promptsView
				.findViewById(R.id.editTextAmt1);
		final LinearLayout llo = (LinearLayout) promptsView
				.findViewById(R.id.llforother);
		llo.setVisibility(View.GONE);
		final EditText editTextNarration = (EditText) promptsView
				.findViewById(R.id.editTextNarration);
		
		if(!(AddClick.equals("new"))){
			editTextAccName.setText(Name);
			editTextNarration.setText(Narration);
			editTextAmt.setText(total);
		}else if(AddClick.equals("new")){
			Rs2000 = "0";Rs1000 = "0";Rs500 ="0";Rs200 ="0";Rs100 ="0";Rs50 ="0";Rs20 ="0";Rs10 ="0";Rs5 ="0";Rs2 ="0";Rs1 ="0";Coins ="0";RpDt_Total ="0";
		}
		
		
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
				android.R.layout.simple_list_item_1, NameList);
		//adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
		editTextAccName.setThreshold(1);
		editTextAccName.setAdapter(adapter1);
		
		editTextAccName.setOnTouchListener(new View.OnTouchListener(){
			   @Override
			   public boolean onTouch(View v, MotionEvent event){
			      editTextAccName.showDropDown();
			      return false;
			   }
			});
		
		btncash.setOnClickListener(new OnClickListener() {
		    public void onClick(View v)
		    {
		    	Intent i = new Intent(getBaseContext(),CashActivity1.class);
		    	i.putExtra("TId","");
		    	i.putExtra("AcName",editTextAccName.getText().toString());
		    	i.putExtra("Narration",editTextNarration.getText().toString());
		    	startActivity(i);
		    	//dialog.dismiss();
		    	finish();
		    } 
		});
		
		// set dialog message
					alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Save",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							// get user input and set it to result
						    	Name = editTextAccName.getText().toString();
						    	Amount = editTextAmt.getText().toString();
						    	Narration = editTextNarration.getText().toString();
						    	if(Name==null||editTextAccName.getText().toString().equalsIgnoreCase("")){
						    		editTextAccName.setError("Please Select Name");
						    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
						    	} else if(Amount==null||editTextAmt.getText().toString().equalsIgnoreCase("")){
						    		editTextAccName.setError("Please Enter Amount");
						    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
						    	} else {
						    	getAcCode(Name);
						    	if(Acno!=null&&AcType!=null){
						    		/*if(dbvalueCash()){
						    		getfields();
						    		}*/
						    		databaseHelper.AddBankDepoTable(Acno, Name, AcType, Amount, Narration,Rs2000,Rs1000,Rs500,Rs200,Rs100,Rs50,Rs20,Rs10,Rs5,Rs2,Rs1,Coins,RpDt_Total);
						    		databaseHelper.DropCashTable();
						    		Toast.makeText(parent, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
						    	}
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
	
	private boolean dbvalueCash() {
		try {
			DatabaseHelper db1 = new DatabaseHelper(getBaseContext());
			SQLiteDatabase sql = db1.getWritableDatabase();
			Cursor cursor = sql.rawQuery("SELECT * FROM CashTable", null);
			if (cursor != null && cursor.getCount() > 0) {
					cursor.close();	sql.close();	db1.close();
					//dbvalue="1";
					return true;
			} else {
				cursor.close();	sql.close();	db1.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	protected void getfields() {
		// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			DatabaseHelper db1 = new DatabaseHelper(getBaseContext());
			SQLiteDatabase sql = db1.getWritableDatabase();
			Cursor c = sql.rawQuery("SELECT * FROM CashTable", null);
			if (c.getCount()>0){
				double total = 0.00, Amt;
				c.moveToFirst();
				int column;
				do{
					Rs2000=(c.getString(c.getColumnIndex("Rs2000")));
					Rs1000=(c.getString(c.getColumnIndex("Rs1000")));
					Rs500=(c.getString(c.getColumnIndex("Rs500")));
					Rs200=(c.getString(c.getColumnIndex("Rs200")));
					Rs100=(c.getString(c.getColumnIndex("Rs100")));
					Rs50=(c.getString(c.getColumnIndex("Rs50")));
					Rs20=(c.getString(c.getColumnIndex("Rs20")));
					Rs10=(c.getString(c.getColumnIndex("Rs10")));
					Rs5=(c.getString(c.getColumnIndex("Rs5")));
					Rs2=(c.getString(c.getColumnIndex("Rs2")));
					Rs1=(c.getString(c.getColumnIndex("Rs1")));
					Coins=(c.getString(c.getColumnIndex("Coins")));
					RpDt_Total=(c.getString(c.getColumnIndex("RpDt_Total")));
				}while(c.moveToNext());
				c.close();
				sql.close();
				db1.close();
			}
			
		
		
	}

	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		String data = btnbankdepo.getText().toString();
		PetroSoftData.BANK_DEPO = splitDatabyspace(data);
		Intent intent = new Intent();
		setResult(PetroSoftData.REQUEST_GET_BANK_DEPOSIT_FOR_DSR,intent);
		finish();
	}
	
	private String splitDatabyspace(String data) {
		// TODO Auto-generated method stub
		String mny[] = data.split("\\s+");
		data = mny[2];
		return (data);		
	}
	
	private void getAcCode(String Name) {
		// TODO Auto-generated method stub
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(
				"Select Acno,AcType from CashierMaster where Name = '"+Name+"'", null);
		if (cursor.getCount()==0){
			Toast.makeText(parent, "Name not available", Toast.LENGTH_SHORT).show();
		}
		else if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			Acno = cursor.getString(0);
			AcType = cursor.getString(1);
		cursor.close();
		db.close();
		db1.close();
		}
		return;

	}
	
}
