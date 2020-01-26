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
import android.util.Log;
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

public class DSRGetSwapCardActivity extends Activity implements OnClickListener{
	Context parent;
	TextView tvAccountName,tvAmount;
	ListView list;
	ImageView btnAdd, ivlogo;
	Button btnbankdepo;
	DatabaseHelper databaseHelper;
	private ArrayList<String> NameList;
	private ArrayList<String> ModeList;
	ArrayList<SwapCardBean> bankDepoBeanList;
	SwapCardBean bankDepoBean;
	SwapCardAdapter bankDepoAdapter;
	int TId;
	String Name, Acno=null, AcType=null, tid, BatchNo, Mode, Amount, Narration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dsr_bank_deposit);
		initViews();
		//CommonUtilities.clearTable(parent, "OtherExpTable");
		if (dbvalue()) {
			updatelist();
		}
		updateCustomerSpinner();
		SetListener();
	}
	
	private String splitDatabyspace(String data) {
		// TODO Auto-generated method stub
		String mny[] = data.split("\\s+");
		data = mny[2];
		return (data);		
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
		tid = bankDepoBeanList.get(position).gettid();
		BatchNo = bankDepoBeanList.get(position).getBat_no();
		Mode = bankDepoBeanList.get(position).getMode();
		Amount = bankDepoBeanList.get(position).getBamount();
		Narration = bankDepoBeanList.get(position).getRemark();
	}
	
	protected void setPrompt() {
		LayoutInflater li = LayoutInflater.from(parent);
		View promptsView = li.inflate(R.layout.prompt_swap_card, null);

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parent);

		// set prompts.xml to alertdialog builder
		alertDialogBuilder.setView(promptsView);

		final AutoCompleteTextView editTextAccName = (AutoCompleteTextView) promptsView
				.findViewById(R.id.editTextAccName);
		editTextAccName.setText(Name);
		final AutoCompleteTextView editTextMode = (AutoCompleteTextView) promptsView
				.findViewById(R.id.editTextMode);
		editTextMode.setText(Mode);

		final LinearLayout llo = (LinearLayout) promptsView
				.findViewById(R.id.llforother);
		llo.setVisibility(View.VISIBLE);
		final EditText editTextAmt = (EditText) promptsView
				.findViewById(R.id.editTextAmt2);
		editTextAmt.setText(Amount);
		final EditText editTextBatchNo = (EditText) promptsView
				.findViewById(R.id.editTextBatchNo);
		editTextBatchNo.setText(BatchNo);
		final EditText editTexttid = (EditText) promptsView
				.findViewById(R.id.editTexttid);
		editTexttid.setText(tid);
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

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(parent,
                android.R.layout.simple_list_item_1, ModeList);
        //adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
        editTextMode.setThreshold(1);
        editTextMode.setAdapter(adapter2);

        editTextMode.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                editTextMode.showDropDown();
                return false;
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
								tid = editTexttid.getText().toString();
								BatchNo = editTextBatchNo.getText().toString();
								Mode = editTextMode.getText().toString();
								Amount = editTextAmt.getText().toString();
								Narration = editTextNarration.getText().toString();
								if(editTextAccName.getText().toString().equalsIgnoreCase("")){
									editTextAccName.setError("Please Select Name");
									Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
								} else if(editTextAmt.getText().toString().equalsIgnoreCase("")){
									editTextAmt.setError("Please Enter Amount");
									Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
								} else if(editTexttid.getText().toString().equalsIgnoreCase("")){
									editTexttid.setError("Please Enter Tid");
									Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
								} else if(editTextBatchNo.getText().toString().equalsIgnoreCase("")){
									editTextBatchNo.setError("Please Enter Batch No.");
									Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
								} else if(editTextMode.getText().toString().equalsIgnoreCase("")){
									editTextMode.setError("Please Select Mode.");
									Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
								} else{
						    	getAcCode(Name);
						    	if(Acno!=null&&AcType!=null){
						    		databaseHelper.UpdateSwapCardTable(TId, Acno, Name, AcType, tid, BatchNo, Mode, Amount, Narration);
						    		Toast.makeText(parent, "Data Updated Successfully", Toast.LENGTH_LONG).show();
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


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnbankdepo:
			String data = btnbankdepo.getText().toString();
			PetroSoftData.SWAP_CARD = splitDatabyspace(data);
			Intent intent = new Intent();
			setResult(PetroSoftData.REQUEST_GET_SWAP_CARD_DETAILS_FOR_DSR,
					intent);
			finish();
			break;
		case R.id.button_add:
			
			LayoutInflater li = LayoutInflater.from(parent);
			View promptsView = li.inflate(R.layout.prompt_swap_card, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(parent);

			// set prompts.xml to alertdialog builder
			alertDialogBuilder.setView(promptsView);

			final AutoCompleteTextView editTextAccName = (AutoCompleteTextView) promptsView
					.findViewById(R.id.editTextAccName);
			final AutoCompleteTextView editTextMode = (AutoCompleteTextView) promptsView
					.findViewById(R.id.editTextMode);

			final LinearLayout llo = (LinearLayout) promptsView
					.findViewById(R.id.llforother);
			llo.setVisibility(View.VISIBLE);
			final EditText editTextAmt = (EditText) promptsView
					.findViewById(R.id.editTextAmt2);
			final EditText editTextBatchNo = (EditText) promptsView
					.findViewById(R.id.editTextBatchNo);
			final EditText editTexttid = (EditText) promptsView
					.findViewById(R.id.editTexttid);
			final EditText editTextNarration = (EditText) promptsView
					.findViewById(R.id.editTextNarration);
			
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

            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(parent,
                    android.R.layout.simple_list_item_1, ModeList);
            //adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
            editTextMode.setThreshold(1);
            editTextMode.setAdapter(adapter2);

            editTextMode.setOnTouchListener(new View.OnTouchListener(){
                @Override
                public boolean onTouch(View v, MotionEvent event){
                    editTextMode.showDropDown();
                    return false;
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
									tid = editTexttid.getText().toString();
									BatchNo = editTextBatchNo.getText().toString();
									Mode = editTextMode.getText().toString();
							    	Amount = editTextAmt.getText().toString();
							    	Narration = editTextNarration.getText().toString();
							    	if(editTextAccName.getText().toString().equalsIgnoreCase("")){
							    		editTextAccName.setError("Please Select Name");
							    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
							    	} else if(editTextAmt.getText().toString().equalsIgnoreCase("")){
							    		editTextAmt.setError("Please Enter Amount");
							    		Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
							    	} else if(editTexttid.getText().toString().equalsIgnoreCase("")){
										editTexttid.setError("Please Enter Tid");
										Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
									} else if(editTextBatchNo.getText().toString().equalsIgnoreCase("")){
										editTextBatchNo.setError("Please Enter Batch No.");
										Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
									} else if(editTextMode.getText().toString().equalsIgnoreCase("")){
										editTextMode.setError("Please Select Mode.");
										Toast.makeText(parent, "Incorrect Data", Toast.LENGTH_LONG).show();
									} else{
							    		getAcCode(Name);
							    		if(Acno!=null&&AcType!=null){
							    		databaseHelper.AddSwapCardTable(Acno, Name, AcType, tid, BatchNo, Mode, Amount, Narration);
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

			break;
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		String data = btnbankdepo.getText().toString();
		PetroSoftData.SWAP_CARD = splitDatabyspace(data);
		Intent intent = new Intent();
		setResult(PetroSoftData.REQUEST_GET_SWAP_CARD_DETAILS_FOR_DSR,intent);
		finish();
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
	
	private void updateCustomerSpinner() {
		NameList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		// db.execSQL("CREATE TABLE CustomerMaster(AcNo TEXT,Name TEXT,City TEXT)");

		Cursor cursor = db.rawQuery(
				"Select Name from CashierMaster where AcType = 'Card' order by Name ASC", null);

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
	
	private boolean dbvalue() {
		try {
			DatabaseHelper db1 = new DatabaseHelper(getBaseContext());
			SQLiteDatabase sql = db1.getWritableDatabase();
			Cursor cursor = sql.rawQuery("SELECT * FROM SwapCardTable", null);
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
		bankDepoBeanList = new ArrayList<SwapCardBean>();
		bankDepoBeanList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		
		Cursor c = db.rawQuery("Select * from SwapCardTable",null);
		if (c.getCount()>0){
			double total = 0.00, Amt;
			c.moveToFirst();
			int column;
			do{
				bankDepoBean = new SwapCardBean();
				bankDepoBean.setTId(c.getInt(c.getColumnIndex("TId")));
				bankDepoBean.setAcno(c.getString(c.getColumnIndex("Acno")));
				bankDepoBean.setName(c.getString(c.getColumnIndex("Name")));
				bankDepoBean.setAcType(c.getString(c.getColumnIndex("AcType")));
				bankDepoBean.settid(c.getString(c.getColumnIndex("transid")));
				bankDepoBean.setBat_no(c.getString(c.getColumnIndex("Bat_no")));
				bankDepoBean.setMode(c.getString(c.getColumnIndex("Mode")));
				bankDepoBean.setBamount(c.getString(c.getColumnIndex("Bamount")));
				bankDepoBean.setRemark(c.getString(c.getColumnIndex("Remark")));
				Amt= Double.parseDouble(c.getString(c.getColumnIndex("Bamount")));
				//total = total + Amt;
				bankDepoBeanList.add(bankDepoBean);
			}while(c.moveToNext());
			total = getBalanceAmt();
			String money = CommonUtilities.convertMoney(total);
			btnbankdepo.setText("Swap Card:   "+ money);
			c.close();
			db.close();
			db1.close();
		}
		bankDepoAdapter = new SwapCardAdapter(parent,bankDepoBeanList);
		list.setAdapter(bankDepoAdapter);
		
	}
	
	private void initViews() {
		// TODO Auto-generated method stub
		parent = DSRGetSwapCardActivity.this;
		tvAccountName = (TextView) findViewById(R.id.tvAccountName);
		tvAmount = (TextView) findViewById(R.id.tvAmount);
		btnAdd = (ImageView) findViewById(R.id.button_add);
		ivlogo = (ImageView) findViewById(R.id.iconlogo);
		list = (ListView) findViewById(R.id.listview_dsr_other_list);
		btnbankdepo = (Button) findViewById(R.id.btnbankdepo);
		databaseHelper = new DatabaseHelper(parent);
		NameList = new ArrayList<String>();
        ModeList = new ArrayList<String>();
        ModeList.add("Receivable");
        ModeList.add("Payable");

		btnbankdepo.setText("Swap Card: "+PetroSoftData.SWAP_CARD);
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

	private double getBalanceAmt() {
		float SumCr = (float) 0.00;        float SumDr = (float) 0.00;        float SumT = (float) 0.00;
		String Scr, Sdr, St;
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();

		Cursor cursor1 = db.rawQuery("Select CAST(Sum(Bamount) as float) as PayAmount from SwapCardTable where Mode ='Payable' "+
				" order by Name", null);
		Log.d("test", "" + cursor1.getCount());
		Log.e("PayAmount", ""+cursor1.getCount());
		if (cursor1.getCount() > 0) {
			cursor1.moveToFirst();
			if (String.valueOf(cursor1.getFloat(cursor1.getColumnIndex("PayAmount"))).equals(null)){
				Log.e("PayAmount", "Rs 0.00");
			}else {
				Scr=String.valueOf(cursor1.getFloat(cursor1.getColumnIndex("PayAmount")));
				Log.e("PayAmount", String.valueOf(cursor1.getFloat(cursor1.getColumnIndex("PayAmount"))));
				SumCr = Float.parseFloat(Scr);
			}
		} else {
			Log.e("Cr Sum", "Not found");
		}

		Cursor cursor2 = db.rawQuery("Select CAST(Sum(Bamount) as float) as RecAmount from SwapCardTable where Mode ='Receivable' "+
				" order by Name", null);
		Log.d("test", "" + cursor2.getCount());
		Log.e("RecAmount", ""+cursor2.getCount());
		if (cursor2.getCount() > 0) {
			cursor2.moveToFirst();
			if (String.valueOf(cursor2.getFloat(cursor2.getColumnIndex("RecAmount"))).equals(null)){
				Log.e("RecAmount", "Rs 0.00");
			}else {
				Sdr=String.valueOf(cursor2.getFloat(cursor2.getColumnIndex("RecAmount")));
				Log.e("RecAmount", String.valueOf(cursor2.getFloat(cursor2.getColumnIndex("RecAmount"))));
				SumDr = Float.parseFloat(Sdr);
			}
		} else {
			Log.e("Dr Sum", "Not found");
		}

		SumT = ((SumDr - SumCr));///100000);
		//SumT = df.setRoundingMode(SumT);
		St = String.format("%.2f",SumT);
		SumT = Float.parseFloat(St);
		Log.e("set text", "Rs "+SumT);
		/*if (SumT>0){
			//Log.e("set text", "Rs "+SumT+" Debit");
			//tvcash.setText("Rs "+SumT+" Lakh Dr");
			return SumT;
		} else if (SumT<0){
			//Log.e("set text", "Rs "+Math.abs(SumT)+" Credit");
			//tvcash.setText("Rs "+Math.abs(SumT)+" Lakh Cr");
			return SumT;
		} else if (SumT==0){
			//Log.e("set text", "Rs "+"0.00");
			return SumT;
		}*/
		return SumT;
	}

}
