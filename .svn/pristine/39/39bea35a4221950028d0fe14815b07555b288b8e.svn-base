package com.vritti.petrosoft;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;

public class DSRGetOtherAddRsActivity extends Activity implements OnClickListener{
	Context parent;
	TextView tvAccountName,tvAmount;
	ListView list;
	ImageView btnAdd, ivlogo;
	Button btnbankdepo;
	DatabaseHelper databaseHelper;
	ArrayList<BankDepoBean> bankDepoBeanList;
	BankDepoBean bankDepoBean;
	BankDepoAdapter bankDepoAdapter;
	private ArrayList<String> NameList;
	int TId;
	String Name, Acno=null, AcType=null, Amount, Narration; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dsr_bank_deposit);

		initViews();
		if (dbvalue()) {
			updatelist();
		}
		//updateCustomerSpinner();
		SetListener();
	}
	/*
	private void updateCustomerSpinner() {
		NameList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		// db.execSQL("CREATE TABLE CustomerMaster(AcNo TEXT,Name TEXT,City TEXT)");

		Cursor cursor = db.rawQuery(
				"Select Name from CashierMaster where NOT(AcType = 'Bank') order by Name ASC", null);

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
	*/
	private void initViews() {
		// TODO Auto-generated method stub
		parent = DSRGetOtherAddRsActivity.this;
		tvAccountName = (TextView) findViewById(R.id.tvAccountName);
		tvAccountName.setText("Narration");
		tvAmount = (TextView) findViewById(R.id.tvAmount);
		btnAdd = (ImageView) findViewById(R.id.button_add);
		ivlogo = (ImageView) findViewById(R.id.iconlogo);
		if(PetroSoftData.imgPath!=null){
			Picasso.with(DSRGetOtherAddRsActivity.this).load(PetroSoftData.imgPath)/*
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)*/
					.into(ivlogo);
		}
		else{
			ivlogo.setImageResource(R.drawable.petro_soft_logo);
		}
		list = (ListView) findViewById(R.id.listview_dsr_other_list);
		btnbankdepo = (Button) findViewById(R.id.btnbankdepo);
		databaseHelper = new DatabaseHelper(parent);
		NameList = new ArrayList<String>();
		btnbankdepo.setText("Other Add(Rs.): "+PetroSoftData.OTHER_ADD_Rs);

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
		TId = bankDepoBeanList.get(position).getTId();/*
		Name = bankDepoBeanList.get(position).getName();
		Acno = bankDepoBeanList.get(position).getAcno();
		AcType = bankDepoBeanList.get(position).getAcno();*/
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
		//editTextAccName.setText(Name);
		editTextAccName.setVisibility(View.GONE);
		final LinearLayout llb = (LinearLayout) promptsView
				.findViewById(R.id.llforbank);
		llb.setVisibility(View.GONE);
		final LinearLayout llo = (LinearLayout) promptsView
				.findViewById(R.id.llforother);
		llo.setVisibility(View.VISIBLE);
		final EditText editTextAmt = (EditText) promptsView
				.findViewById(R.id.editTextAmt2);
		editTextAmt.setText(Amount);
		final EditText editTextNarration = (EditText) promptsView
				.findViewById(R.id.editTextNarration);
		editTextNarration.setText(Narration);
		
		/*ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
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
			});*/
		
		// set dialog message
					alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton("Save",
						  new DialogInterface.OnClickListener() {
						    public void onClick(DialogInterface dialog,int id) {
							// get user input and set it to result
						    	//Name = editTextAccName.getText().toString();
						    	Amount = editTextAmt.getText().toString();
						    	Narration = editTextNarration.getText().toString();
						    	/*if(Name==null||editTextAccName.getText().toString().equalsIgnoreCase("")){
						    		editTextAccName.setError("Please Select Name");
						    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
						    	} else*/ if(Amount==null||editTextAmt.getText().toString().equalsIgnoreCase("")){
									editTextAmt.setError("Please Enter Amount");
						    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
						    	} else {
						    	//getAcCode(Name);
						    	//if(Acno!=null&&AcType!=null){
						    		databaseHelper.UpdateOtherAddRsTable(TId,/* Acno, Name, AcType,*/ Amount, Narration);
						    		Toast.makeText(parent, "Data Updated Successfully", Toast.LENGTH_LONG).show();
						    	//}
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


	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnbankdepo:
			String data = btnbankdepo.getText().toString();
			PetroSoftData.OTHER_ADD_Rs = splitDatabyspace(data);
			Intent intent = new Intent();
			setResult(PetroSoftData.REQUEST_GET_OTHER_ADD_Rs_DETAILS_FOR_DSR,
					intent);
			finish();
			break;
		case R.id.button_add:
			LayoutInflater li = LayoutInflater.from(parent);
			View promptsView = li.inflate(R.layout.prompt_bank, null);			

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parent);

			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final AutoCompleteTextView editTextAccName = (AutoCompleteTextView) promptsView
					.findViewById(R.id.editTextAccName);
			editTextAccName.setVisibility(View.GONE);
			final LinearLayout llb = (LinearLayout) promptsView
					.findViewById(R.id.llforbank);
			llb.setVisibility(View.GONE);
			final LinearLayout llo = (LinearLayout) promptsView
					.findViewById(R.id.llforother);
			llo.setVisibility(View.VISIBLE);
			final EditText editTextAmt = (EditText) promptsView
					.findViewById(R.id.editTextAmt2);
			final EditText editTextNarration = (EditText) promptsView
					.findViewById(R.id.editTextNarration);
			
			/*ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
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
				});*/
			
			// set dialog message
						alertDialogBuilder
							.setCancelable(false)
							.setPositiveButton("Save",
							  new DialogInterface.OnClickListener() {
							    public void onClick(DialogInterface dialog,int id) {
								// get user input and set it to result
							    	//Name = editTextAccName.getText().toString();
							    	Amount = editTextAmt.getText().toString();
							    	Narration = editTextNarration.getText().toString();
							    	/*if(Name==null||editTextAccName.getText().toString().equalsIgnoreCase("")){
							    		editTextAccName.setError("Please Select Name");
							    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
							    	} else*/ if(Amount==null||editTextAmt.getText().toString().equalsIgnoreCase("")){
							    		editTextAmt.setError("Please Enter Amount");
							    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
							    	} else {
							    	//getAcCode(Name);
							    	//if(Acno!=null&&AcType!=null){
								    databaseHelper.AddOtherAddRsTable( Amount, Narration);
								    Toast.makeText(parent, "Data Inserted Successfully", Toast.LENGTH_LONG).show();
							    	//}
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

			break;
		}
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		String data = btnbankdepo.getText().toString();
		PetroSoftData.OTHER_ADD_Rs = splitDatabyspace(data);
		Intent intent = new Intent();
		setResult(PetroSoftData.REQUEST_GET_OTHER_ADD_Rs_DETAILS_FOR_DSR,
				intent);
		finish();
	}
	
	private String splitDatabyspace(String data) {
		// TODO Auto-generated method stub
		String mny[] = data.split("\\s+");
		data = mny[2];
		return (data);		
	}
	
	private boolean dbvalue() {
		try {
			DatabaseHelper db1 = new DatabaseHelper(getBaseContext());
			SQLiteDatabase sql = db1.getWritableDatabase();
			Cursor cursor = sql.rawQuery("SELECT * FROM OtherAddRsTable", null);
			if (cursor != null && cursor.getCount() > 0) {
				/*if (cursor.getColumnIndex("NetworkCode") < 0) {
					cursor.close();
					sql.close();
					db1.close();
					return false;
				} else {*/
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

	public void updatelist() {
		// TODO Auto-generated method stub
		bankDepoBeanList = new ArrayList<BankDepoBean>();
		bankDepoBeanList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		
		Cursor c = db.rawQuery("Select * from OtherAddRsTable",null);
		if (c.getCount()>0){
			double total = 0.00, Amt;
			c.moveToFirst();
			int column;
			do{
				bankDepoBean = new BankDepoBean();
				bankDepoBean.setTId(c.getInt(c.getColumnIndex("TId")));
				//bankDepoBean.setAcno(c.getString(c.getColumnIndex("Acno")));
				bankDepoBean.setName(c.getString(c.getColumnIndex("Narration")));
				//bankDepoBean.setAcType(c.getString(c.getColumnIndex("AcType")));
				bankDepoBean.setAmount(c.getString(c.getColumnIndex("Amount")));
				bankDepoBean.setNarration(c.getString(c.getColumnIndex("Narration")));
				Amt= Double.parseDouble(c.getString(c.getColumnIndex("Amount")));
				total = total + Amt;
				bankDepoBeanList.add(bankDepoBean);
			}while(c.moveToNext());
			String money = CommonUtilities.convertMoney(total);
			btnbankdepo.setText("Other Add(Rs.):   "+ money);
			c.close();
			db.close();
			db1.close();
		}
		bankDepoAdapter = new BankDepoAdapter(parent,bankDepoBeanList);
		list.setAdapter(bankDepoAdapter);
		
	}
	

}
