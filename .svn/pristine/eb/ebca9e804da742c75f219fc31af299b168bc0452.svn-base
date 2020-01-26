package com.vritti.petrosoft;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class DSRActivity extends Activity implements OnClickListener {
	private static Context parent;
	ImageView btnAdd;
	static Calendar myCalendar;
	DatePickerDialog.OnDateSetListener date;
	private ImageView ivlogo;
	
	Button btnBankDeposit, btnCreditSale, btnOtherExp, btnSwapCard, btnOtherRcpt, btnOtherAddRs, btnPetroSale, btnProductSale;
	static EditText eddatentime;
	EditText edCashier;
	EditText edTotalRcvd;
	EditText edTotalLess;
	EditText edDifference;
	static Spinner edshift;
	String currentDateTimeString;	
	static String xml1,xml2, xml3, xml4, xml5, xml6,xml7,xml8;
	String[] Shift ={"I","II","III","IV","V","VI"};
	
	ArrayList<String> nameList;
	
	static ArrayList<ProdSaleBean> prodSaleBeanList;
	static ProdSaleBean prodSaleBean;
	static ArrayList<PumpListBean> pumplistBeanList;
	static PumpListBean pumplistBean;
	static ArrayList<BankDepoBean> bankDepoBeanList;
	static BankDepoBean bankDepoBean;
    static ArrayList<SwapCardBean> swapCardBeanList;
    static SwapCardBean swapCardBean;
	private static StringBuilder sb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_dsr_main);	
		
		initViews();
		/*currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
		// textView is the TextView view that should display it
		eddatentime.setText(currentDateTimeString);*/
		
		setListeners();		
		
		date = new DatePickerDialog.OnDateSetListener() {

		    @Override
		    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		        // TODO Auto-generated method stub
		        myCalendar.set(Calendar.YEAR, year);
		        myCalendar.set(Calendar.MONTH, monthOfYear);
		        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		        updateLabel();
		    }
		};	 
			
		ArrayAdapter<String> adapter = new ArrayAdapter<String>		
        (this,android.R.layout.simple_list_item_1,Shift);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);		 
		edshift.setAdapter(adapter);
		      
	}

	private void setListeners() {
		// TODO Auto-generated method stub
		btnBankDeposit.setOnClickListener(this);
		btnCreditSale.setOnClickListener(this);
		btnOtherExp.setOnClickListener(this);
		btnSwapCard.setOnClickListener(this);
		btnOtherAddRs.setOnClickListener(this);
		btnOtherRcpt.setOnClickListener(this);
		btnPetroSale.setOnClickListener(this);
		btnProductSale.setOnClickListener(this);
		edCashier.setOnClickListener(null);
		eddatentime.setOnClickListener(this);
		btnAdd.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnBankDeposit:
			Intent intent_BankDepo = new Intent(getApplicationContext(),
					DSRGetBankDepoActivity.class);
			startActivityForResult(intent_BankDepo,
					PetroSoftData.REQUEST_GET_BANK_DEPOSIT_FOR_DSR);
			break;
		case R.id.btnCreditSale:
			break;
		case R.id.btnOtherExp:
			Intent intent_OtherExp = new Intent(getApplicationContext(),
					DSRGetOtherExpActivity.class);
			startActivityForResult(intent_OtherExp,
					PetroSoftData.REQUEST_GET_OTHER_EXP_DETAILS_FOR_DSR);
			break;
		case R.id.btnSwapCard:
			Intent intent_SwapCard = new Intent(getApplicationContext(),
					DSRGetSwapCardActivity.class);
			startActivityForResult(intent_SwapCard,
					PetroSoftData.REQUEST_GET_SWAP_CARD_DETAILS_FOR_DSR);
			break;
		case R.id.btnOtherRcpt:
			Intent intent_OtherRcpt = new Intent(getApplicationContext(),
					DSRGetOtherRcptActivity.class);
			startActivityForResult(intent_OtherRcpt,
					PetroSoftData.REQUEST_GET_OTHER_RCPT_DETAILS_FOR_DSR);
			break;
		case R.id.btnOtherAddRs:
			Intent intent_OtherAddRs = new Intent(getApplicationContext(),
					DSRGetOtherAddRsActivity.class);
			startActivityForResult(intent_OtherAddRs,
					PetroSoftData.REQUEST_GET_OTHER_ADD_Rs_DETAILS_FOR_DSR);
			break;
		case R.id.btnPetroSale:
			PetroSoftData.SHIFT = Shift[edshift.getSelectedItemPosition()];
			getCashierCode(PetroSoftData.Cashier_name);
			if(PetroSoftData.DATEnTIME!=null){
				if(PetroSoftData.SHIFT!=null){
					Intent intent_PETROSALE = new Intent(getApplicationContext(),
							DSRGetPetroSaleActivity.class);
					startActivityForResult(intent_PETROSALE,
							PetroSoftData.REQUEST_GET_PETRO_SALE_DETAILS_FOR_DSR);
				}else{Toast.makeText(parent, "Please select Shift", Toast.LENGTH_SHORT).show();}
			}else{Toast.makeText(parent, "Please select Date", Toast.LENGTH_SHORT).show();}
			break;
		case R.id.btnProductSale:
			PetroSoftData.SHIFT = Shift[edshift.getSelectedItemPosition()];
			getCashierCode(PetroSoftData.Cashier_name); 
			if(PetroSoftData.DATEnTIME!=null){
				if(PetroSoftData.SHIFT!=null){
			Intent intent_PRODUCTSALE = new Intent(getApplicationContext(),
					DSRGetProductSaleActivity.class);
			startActivityForResult(intent_PRODUCTSALE,
					PetroSoftData.REQUEST_GET_PRODUCT_SALE_DETAILS_FOR_DSR);
		}else{Toast.makeText(parent, "Please select Shift", Toast.LENGTH_SHORT).show();}
	}else{Toast.makeText(parent, "Please select Date", Toast.LENGTH_SHORT).show();}
			break;
		case R.id.edCashier:
			Intent intent_CASHIER = new Intent(getApplicationContext(),
					DSRGetCashierActivity.class);
			startActivityForResult(intent_CASHIER,
					PetroSoftData.REQUEST_GET_CASHIER_DETAILS_FOR_DSR);			
			break;
		case R.id.eddatentime:
			new DatePickerDialog(parent, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
			break;
		case R.id.button_add:
			if(PetroSoftData.DATEnTIME!=null){
				if(PetroSoftData.Cashier_acno!=null){
					getXmls();
					if (CommonUtilities.isInternetAvailable(parent)) {
			            startService(new Intent(parent,SendDSRService.class));
			        } else {
			            Toast.makeText(parent, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG).show();
			        }
				}else{
					Toast.makeText(parent, "Please select Cashier", Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(parent, "Please select Date", Toast.LENGTH_SHORT).show();
			}
			break;
		}
	}
	
	private void getXmls() {
		// TODO Auto-generated method stub
		String Xml1 = generateXml1();
		Log.e("Xml1", Xml1);
		String Xml2 = generateXml2();
		Log.e("Xml2", Xml2);
		String Xml3 = generateXml3();
		Log.e("Xml3", Xml3);
		String Xml4 = generateXml4();
		Log.e("Xml4", Xml4);
		String Xml5 = generateXml5();
		Log.e("Xml5", Xml5);
		String Xml6 = generateXml6();
		Log.e("Xml6", Xml6);
		String Xml7 = generateXml7();
		Log.e("Xml7", Xml7);
		String Xml8 = generateXml8();
		Log.e("Xml8", Xml8);
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		int pos = edshift.getSelectedItemPosition();
		db1.CreateSendDSR();
		if(PetroSoftData.DATEnTIME!=null){
		db1.addSendDSR(PetroSoftData.DATEnTIME, edshift.getItemAtPosition(pos).toString(),PetroSoftData.Cashier_acno, Xml2,Xml3,Xml4,Xml5,Xml6,Xml7,Xml8, "No");
		}else{
			Toast.makeText(parent, "Please select Date", Toast.LENGTH_SHORT).show();
		}
	}

	public static String generateXml1(){
		getCashierCode(PetroSoftData.Cashier_name);
		xml1 = "<Header>";
        xml1 += "<datentime>" + PetroSoftData.DATEnTIME	+ "</datentime>";
		int pos = edshift.getSelectedItemPosition();        
        xml1 += "<shift>" + edshift.getItemAtPosition(pos) + "</shift>";
        xml1 += "<CashierCode>" + PetroSoftData.Cashier_acno + "</CashierCode>";
        xml1 += "</Header>";
        return xml1;		
	}

	public static String generateXml2(){
		pumplistBeanList = new ArrayList<PumpListBean>();
		pumplistBeanList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		Cursor c = db.rawQuery("Select * from PumpList where NOT(closing = '0.00') order by CAST(pump_no as INT)",null);
		if (c.getCount()>0){
			c.moveToFirst();	
			do{
				pumplistBean = new PumpListBean();
				pumplistBean.setpump_no(c.getString(c.getColumnIndex("pump_no")));
				pumplistBean.setitem_code(c.getString(c.getColumnIndex("item_code")));
				pumplistBean.setclosing(c.getString(c.getColumnIndex("closing")));
				pumplistBean.setopening(c.getString(c.getColumnIndex("opening")));
				pumplistBean.settesting(c.getString(c.getColumnIndex("testing")));
				pumplistBean.setSaleLtrs(c.getString(c.getColumnIndex("SaleLtrs")));
				pumplistBean.setRate(c.getString(c.getColumnIndex("Rate")));
				pumplistBean.setAmount(c.getString(c.getColumnIndex("Amount")));
				pumplistBeanList.add(pumplistBean);
			}while(c.moveToNext());	c.close();
			db.close();
			db1.close();
		}
		sb.setLength(0);
		sb.append("<Data>");
        for (int i = 0; i < pumplistBeanList.size(); i++) {
            sb.append("<Table>");
            sb.append("<pump_no>" + pumplistBeanList.get(i).getpump_no() + "</pump_no>");
            sb.append("<item_code>" + pumplistBeanList.get(i).getitem_code() + "</item_code>");
            sb.append("<closing>" + pumplistBeanList.get(i).getclosing() + "</closing>");
            sb.append("<opening>" + pumplistBeanList.get(i).getopening() + "</opening>");
            sb.append("<testing>" + pumplistBeanList.get(i).gettesting() + "</testing>");
            sb.append("<SaleLtrs>" + pumplistBeanList.get(i).getSaleLtrs() + "</SaleLtrs>");
            sb.append("<Rate>" + pumplistBeanList.get(i).getRate() + "</Rate>");
            sb.append("<Amount>" + pumplistBeanList.get(i).getAmount() + "</Amount>");
            sb.append("</Table>");
        }
            sb.append("</Data>");
            xml2 = sb.toString();
            if(xml2.equalsIgnoreCase("<Data>"+"</Data>")){
            	xml2 = "no data";
            }
            return xml2;
	}

	public static String generateXml3(){
		prodSaleBeanList = new ArrayList<ProdSaleBean>();
		prodSaleBeanList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		Cursor c = db.rawQuery("Select * from ProdSaleTable",null);
		if (c.getCount()>0){
			c.moveToFirst();
			do{
				prodSaleBean = new ProdSaleBean();
				prodSaleBean.setItemCode(c.getString(c.getColumnIndex("ItemCode")));
				prodSaleBean.setItemQty(c.getString(c.getColumnIndex("ItemQty")));
				prodSaleBean.setItemRate(c.getString(c.getColumnIndex("ItemRate")));
				prodSaleBean.setItemAmt(c.getString(c.getColumnIndex("ItemAmt")));
				prodSaleBeanList.add(prodSaleBean);
			}while(c.moveToNext());
			c.close();
			db.close();
			db1.close();
		}
		sb.setLength(0);
		sb.append("<Data>");
        for (int i = 0; i < prodSaleBeanList.size(); i++) {
            sb.append("<Table>");
            sb.append("<ItemCode>" + prodSaleBeanList.get(i).getItemCode() + "</ItemCode>");
            sb.append("<ItemQty>" + prodSaleBeanList.get(i).getItemQty() + "</ItemQty>");
            sb.append("<ItemRate>" + prodSaleBeanList.get(i).getItemRate() + "</ItemRate>");
            sb.append("<ItemAmt>" + prodSaleBeanList.get(i).getItemAmt() + "</ItemAmt>");
            sb.append("</Table>");
        }
            sb.append("</Data>");
            xml3 = sb.toString();
            if(xml3.equalsIgnoreCase("<Data>"+"</Data>")){
            	xml3 = "no data";
            }
            return xml3;
	}

	public static String generateXml4(){
		bankDepoBeanList = new ArrayList<BankDepoBean>();
		bankDepoBeanList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		Cursor c = db.rawQuery("Select * from OtherRcptTable",null);
		if (c.getCount()>0){
			c.moveToFirst();
			do{
				bankDepoBean = new BankDepoBean();
				bankDepoBean.setAcno(c.getString(c.getColumnIndex("Acno")));
				bankDepoBean.setAmount(c.getString(c.getColumnIndex("Amount")));
				bankDepoBean.setNarration(c.getString(c.getColumnIndex("Narration")));
				bankDepoBeanList.add(bankDepoBean);
			}while(c.moveToNext());
			c.close();
			db.close();
			db1.close();
		}
		sb.setLength(0);
		sb.append("<Data>");
        for (int i = 0; i < bankDepoBeanList.size(); i++) {
            sb.append("<Table>");
            sb.append("<Acno>" + bankDepoBeanList.get(i).getAcno() + "</Acno>");
            sb.append("<Amount>" + bankDepoBeanList.get(i).getAmount() + "</Amount>");
            sb.append("<Narration>" + bankDepoBeanList.get(i).getNarration() + "</Narration>");            
            sb.append("</Table>");
        }
            sb.append("</Data>");
            xml4 = sb.toString();
            if(xml4.equalsIgnoreCase("<Data>"+"</Data>")){
            	xml4 = "no data";
            }
            return xml4;
	}

	public static String generateXml5(){
		bankDepoBeanList = new ArrayList<BankDepoBean>();
		bankDepoBeanList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		Cursor c = db.rawQuery("Select * from OtherExpTable",null);
		if (c.getCount()>0){
			c.moveToFirst();
			do{
				bankDepoBean = new BankDepoBean();
				bankDepoBean.setAcno(c.getString(c.getColumnIndex("Acno")));
				bankDepoBean.setAmount(c.getString(c.getColumnIndex("Amount")));
				bankDepoBean.setNarration(c.getString(c.getColumnIndex("Narration")));
				bankDepoBeanList.add(bankDepoBean);
			}while(c.moveToNext());
			c.close();
			db.close();
			db1.close();
		}
		sb.setLength(0);
		sb.append("<Data>");
        for (int i = 0; i < bankDepoBeanList.size(); i++) {
            sb.append("<Table>");
            sb.append("<Acno>" + bankDepoBeanList.get(i).getAcno() + "</Acno>");
            sb.append("<Amount>" + bankDepoBeanList.get(i).getAmount() + "</Amount>");
            sb.append("<Narration>" + bankDepoBeanList.get(i).getNarration() + "</Narration>");            
            sb.append("</Table>");
        }
            sb.append("</Data>");
            xml5 = sb.toString();
            if(xml5.equalsIgnoreCase("<Data>"+"</Data>")){
            	xml5 = "no data";
            }
            return xml5;
	}

	public static String generateXml6(){
		bankDepoBeanList = new ArrayList<BankDepoBean>();
		bankDepoBeanList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		Cursor c = db.rawQuery("Select * from BankDepoTable",null);
		if (c.getCount()>0){
			c.moveToFirst();
			do{
				bankDepoBean = new BankDepoBean();
				bankDepoBean.setAcno(c.getString(c.getColumnIndex("Acno")));
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
				
				bankDepoBeanList.add(bankDepoBean);
			}while(c.moveToNext());
			c.close();
			db.close();
			db1.close();
		}
		sb.setLength(0);
		sb.append("<Data>");
        for (int i = 0; i < bankDepoBeanList.size(); i++) {
            sb.append("<Table>");
            sb.append("<Acno>" + bankDepoBeanList.get(i).getAcno() + "</Acno>");
            sb.append("<Amount>" + bankDepoBeanList.get(i).getAmount() + "</Amount>");
            sb.append("<Narration>" + bankDepoBeanList.get(i).getNarration() + "</Narration>");   
            
            sb.append("<Rs2000>" + bankDepoBeanList.get(i).getRs2000() + "</Rs2000>");
            sb.append("<Rs1000>" + bankDepoBeanList.get(i).getRs1000() + "</Rs1000>");
            sb.append("<Rs500>" + bankDepoBeanList.get(i).getRs500() + "</Rs500>");
			sb.append("<Rs200>" + bankDepoBeanList.get(i).getRs200() + "</Rs200>");
            sb.append("<Rs100>" + bankDepoBeanList.get(i).getRs100() + "</Rs100>");
            sb.append("<Rs50>" + bankDepoBeanList.get(i).getRs50() + "</Rs50>");
            sb.append("<Rs20>" + bankDepoBeanList.get(i).getRs20() + "</Rs20>");            
            sb.append("<Rs10>" + bankDepoBeanList.get(i).getRs10() + "</Rs10>");
            sb.append("<Rs5>" + bankDepoBeanList.get(i).getRs5() + "</Rs5>");
            sb.append("<Rs2>" + bankDepoBeanList.get(i).getRs2() + "</Rs2>");
            sb.append("<Rs1>" + bankDepoBeanList.get(i).getRs1() + "</Rs1>");
            sb.append("<Coins>" + bankDepoBeanList.get(i).getCoins() + "</Coins>");
            sb.append("<RpDt_Total>" + bankDepoBeanList.get(i).getRpDt_Total() + "</RpDt_Total>");
            
            sb.append("</Table>");
        }
            sb.append("</Data>");
            xml6 = sb.toString();
            if(xml6.equalsIgnoreCase("<Data>"+"</Data>")){
            	xml6 = "no data";
            }
            return xml6;
	}

	public static String generateXml7(){
		bankDepoBeanList = new ArrayList<BankDepoBean>();
		bankDepoBeanList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		Cursor c = db.rawQuery("Select * from OtherAddRsTable",null);
		if (c.getCount()>0){
			c.moveToFirst();
			do{
				bankDepoBean = new BankDepoBean();
				bankDepoBean.setAmount(c.getString(c.getColumnIndex("Amount")));
				bankDepoBean.setNarration(c.getString(c.getColumnIndex("Narration")));
				bankDepoBeanList.add(bankDepoBean);
			}while(c.moveToNext());
			c.close();
			db.close();
			db1.close();
		}
		sb.setLength(0);
		sb.append("<Data>");
		for (int i = 0; i < bankDepoBeanList.size(); i++) {
			sb.append("<Table>");
			sb.append("<Amount>" + bankDepoBeanList.get(i).getAmount() + "</Amount>");
			sb.append("<Narration>" + bankDepoBeanList.get(i).getNarration() + "</Narration>");
			sb.append("</Table>");
		}
		sb.append("</Data>");
		xml7 = sb.toString();
		if(xml7.equalsIgnoreCase("<Data>"+"</Data>")){
			xml7 = "no data";
		}
		return xml7;
	}

    public static String generateXml8(){
        swapCardBeanList = new ArrayList<SwapCardBean>();
        swapCardBeanList.clear();
        DatabaseHelper db1 = new DatabaseHelper(parent);
        SQLiteDatabase db = db1.getWritableDatabase();
        Cursor c = db.rawQuery("Select * from SwapCardTable",null);
        if (c.getCount()>0){
            c.moveToFirst();
            do{
                swapCardBean = new SwapCardBean();
                swapCardBean.setAcno(c.getString(c.getColumnIndex("Acno")));
                swapCardBean.setAcType(c.getString(c.getColumnIndex("AcType")));
                swapCardBean.setName(c.getString(c.getColumnIndex("Name")));
                swapCardBean.settid(c.getString(c.getColumnIndex("transid")));
                swapCardBean.setBat_no(c.getString(c.getColumnIndex("Bat_no")));
                swapCardBean.setMode(c.getString(c.getColumnIndex("Mode")));
                swapCardBean.setBamount(c.getString(c.getColumnIndex("Bamount")));
                swapCardBean.setRemark(c.getString(c.getColumnIndex("Remark")));
                swapCardBeanList.add(swapCardBean);
            }while(c.moveToNext());
            c.close();
            db.close();
            db1.close();
        }
        sb.setLength(0);
        sb.append("<Data>");
        for (int i = 0; i < swapCardBeanList.size(); i++) {
            sb.append("<Table>");
            sb.append("<Acno>" + swapCardBeanList.get(i).getAcno() + "</Acno>");
            sb.append("<AcType>" + swapCardBeanList.get(i).getAcType() + "</AcType>");
            sb.append("<Name>" + swapCardBeanList.get(i).getName() + "</Name>");
            sb.append("<Tid>" + swapCardBeanList.get(i).gettid() + "</Tid>");
            sb.append("<Bat_no>" + swapCardBeanList.get(i).getBat_no() + "</Bat_no>");
            sb.append("<Mode>" + swapCardBeanList.get(i).getMode() + "</Mode>");
            sb.append("<Bamount>" + swapCardBeanList.get(i).getBamount() + "</Bamount>");
            sb.append("<Remark>" + swapCardBeanList.get(i).getRemark() + "</Remark>");
            sb.append("</Table>");
        }
        sb.append("</Data>");
        xml8 = sb.toString();
        if(xml8.equalsIgnoreCase("<Data>"+"</Data>")){
            xml8 = "no data";
        }
        return xml8;
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PetroSoftData.REQUEST_GET_CASHIER_DETAILS_FOR_DSR:
			edCashier.setText(PetroSoftData.Cashier_name);
			break;
		case PetroSoftData.REQUEST_GET_PRODUCT_SALE_DETAILS_FOR_DSR:
			btnProductSale.setText("Product Sale: "+PetroSoftData.PROD_SALE);
			edTotalRcvd.setText("Total Received: "+getTotalRcvd());
			edDifference.setText("Difference: "+getTotalDiff());
			break;
		case PetroSoftData.REQUEST_GET_PETRO_SALE_DETAILS_FOR_DSR:
			btnPetroSale.setText("Petro Sale: "+PetroSoftData.PETRO_SALE);
			edTotalRcvd.setText("Total Received: "+getTotalRcvd());
			edDifference.setText("Difference: "+getTotalDiff());
			break;
		case PetroSoftData.REQUEST_GET_OTHER_RCPT_DETAILS_FOR_DSR:
			btnOtherRcpt.setText("Other Rcpt: "+PetroSoftData.OTHER_RCPT);
			edTotalRcvd.setText("Total Received: "+getTotalRcvd());
			edDifference.setText("Difference: "+getTotalDiff());
			break;
		case PetroSoftData.REQUEST_GET_OTHER_ADD_Rs_DETAILS_FOR_DSR:
			btnOtherAddRs.setText("Other Add(Rs.): "+PetroSoftData.OTHER_ADD_Rs);
			edTotalRcvd.setText("Total Received: "+getTotalRcvd());
			edDifference.setText("Difference: "+getTotalDiff());
			break;
		case PetroSoftData.REQUEST_GET_OTHER_EXP_DETAILS_FOR_DSR:
			btnOtherExp.setText("Other Exp: "+PetroSoftData.OTHER_EXP);
			edTotalLess.setText("Total Less: "+getTotalLess());
			edDifference.setText("Difference: "+getTotalDiff());
			break;
		case PetroSoftData.REQUEST_GET_SWAP_CARD_DETAILS_FOR_DSR:
			btnSwapCard.setText("Swap Card: "+PetroSoftData.SWAP_CARD);
			edTotalLess.setText("Total Less: "+getTotalLess());
			edDifference.setText("Difference: "+getTotalDiff());
			break;
		case PetroSoftData.REQUEST_GET_BANK_DEPOSIT_FOR_DSR:
			btnBankDeposit.setText("Cash Deposit: "+PetroSoftData.BANK_DEPO);
			edTotalLess.setText("Total Less: "+getTotalLess());
			edDifference.setText("Difference: "+getTotalDiff());
			break;
		}
	}
	
	private static void updateLabel() {
	    String myFormat = "dd-MMM-yyyy hh:mm aa"; //In which you need put here
	    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
	    eddatentime.setText(sdf.format(myCalendar.getTime()));
	    String myFormat2 = "yyyy-MM-dd"; //In which you need put here
	    SimpleDateFormat sdf2 = new SimpleDateFormat(myFormat2, Locale.US);
	    PetroSoftData.DATEnTIME=sdf2.format(myCalendar.getTime());
	    }
	
	private void initViews() {
		// TODO Auto-generated method stub
		parent = DSRActivity.this;
		myCalendar = Calendar.getInstance();
		sb = new StringBuilder();

		ivlogo = (ImageView) findViewById(R.id.iconlogo);

		btnPetroSale = (Button) findViewById(R.id.btnPetroSale);
		btnPetroSale.setText("Petro Sale:   "+ PetroSoftData.PETRO_SALE);
		btnProductSale = (Button) findViewById(R.id.btnProductSale);
		btnProductSale.setText("Product Sale:   "+ PetroSoftData.PROD_SALE);
		btnOtherRcpt = (Button) findViewById(R.id.btnOtherRcpt);
		btnOtherRcpt.setText("Other Rcpt: "+PetroSoftData.OTHER_RCPT);
		btnOtherAddRs = (Button) findViewById(R.id.btnOtherAddRs);
		btnOtherAddRs.setText("Other Add(Rs.): "+PetroSoftData.OTHER_ADD_Rs);

		btnCreditSale = (Button) findViewById(R.id.btnCreditSale);
		btnCreditSale.setText("Credit Sale: "+PetroSoftData.CREDIT_SALE);
		btnOtherExp = (Button) findViewById(R.id.btnOtherExp);
		btnOtherExp.setText("Other Exp: "+PetroSoftData.OTHER_EXP);
		btnBankDeposit = (Button) findViewById(R.id.btnBankDeposit);
		btnBankDeposit.setText("Cash Deposit: "+PetroSoftData.BANK_DEPO);
		btnSwapCard = (Button) findViewById(R.id.btnSwapCard);
		btnSwapCard.setText("Swap Card: "+PetroSoftData.SWAP_CARD);

		eddatentime = (EditText) findViewById(R.id.eddatentime);
		PetroSoftData.DATEnTIME=null;
		edshift = (Spinner) findViewById(R.id.edshift);
		//edshift.setKeyListener(null);
		edCashier = (EditText) findViewById(R.id.edCashier);
		edCashier.setKeyListener(null);
		edCashier.setText(PetroSoftData.Cashier_name);
		edTotalRcvd = (EditText) findViewById(R.id.edTotalRcvd);
		edTotalRcvd.setText("Total Received: "+getTotalRcvd());
		edTotalLess = (EditText) findViewById(R.id.edTotalLess);
		edTotalLess.setText("Total Less: "+getTotalLess());
		edDifference = (EditText) findViewById(R.id.edDifference);
		edDifference.setText("Difference: "+getTotalDiff());
		
		nameList = new ArrayList<String>();

		btnAdd = (ImageView) findViewById(R.id.button_add);
		//btnAdd.setOnClickListener(null);

		if(PetroSoftData.imgPath!=null){
			Picasso.with(DSRActivity.this).load(PetroSoftData.imgPath)/*
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)*/
					.into(ivlogo);
		}
		else{
			ivlogo.setImageResource(R.drawable.petro_soft_logo);
		}
	}

	private String getTotalRcvd() {
		// TODO Auto-generated method stub
		double total = Double.parseDouble(PetroSoftData.PETRO_SALE)+
		Double.parseDouble(PetroSoftData.PROD_SALE)+
				Double.parseDouble(PetroSoftData.OTHER_ADD_Rs)+
		Double.parseDouble(PetroSoftData.OTHER_RCPT);
		String money = CommonUtilities.convertMoney(total);
		return money;		
	}
	private String getTotalLess() {
		// TODO Auto-generated method stub
		double total = Double.parseDouble(PetroSoftData.CREDIT_SALE)+
		Double.parseDouble(PetroSoftData.BANK_DEPO)+
				Double.parseDouble(PetroSoftData.SWAP_CARD)+
		Double.parseDouble(PetroSoftData.OTHER_EXP);
		String money = CommonUtilities.convertMoney(total);
		return money;		
	}
	private String getTotalDiff(){
		double diff = Double.parseDouble(getTotalRcvd()) - Double.parseDouble(getTotalLess());
		String money = CommonUtilities.convertMoney(diff);
		return money;
	}
	
	private static void getCashierCode(String cashier_name) {
		// TODO Auto-generated method stub
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();
		
		Cursor cursor = db.rawQuery(
				"Select Acno from CashierMaster where Name = '"+cashier_name+"'", null);
		if (cursor.getCount()==0){
			Toast.makeText(parent, "Name not available", Toast.LENGTH_SHORT).show();
		}
		else if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			PetroSoftData.Cashier_acno = cursor.getString(0);
		cursor.close();
		db.close();
		db1.close();
		}
		return;
	}
}
