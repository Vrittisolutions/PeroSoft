package com.vritti.petrosoft;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.print.PrintManager;
//import android.support.v4.print.PrintHelper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

/*import com.hp.mss.hpprint.activity.PrintHelp;
import com.hp.mss.hpprint.model.ImagePrintItem;
import com.hp.mss.hpprint.model.PrintItem;
import com.hp.mss.hpprint.model.PrintJobData;
import com.hp.mss.hpprint.model.asset.ImageAsset;
import com.hp.mss.hpprint.util.PrintPluginStatusHelper;
import com.hp.mss.hpprint.util.PrintUtil;*/
import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;
import com.zj.btsdk.BluetoothService;

@SuppressLint("DefaultLocale")
public class BillDetails extends Activity implements OnClickListener {
	Typeface font;
	EditText edCustName, edVehNo, edProductCode, edRate, edQty, edAmt, edVehKm,
			edDescription;
	TextView  edCustBalance,edCredlim, edrewrdspts;
	private Button button_save, button_exit_print, button_cancel,
			button_product_code, button_scan, btnchangeVehicle, btnCashiername,
			btndate;
	static int year, month, day;
	private Context parent;
	// private ArrayList<String> list;
	private Intent intent;
	private double amount = 0, quantity = 0, rate = 0;
	private int billId;
	private DatabaseHelper databaseHelper;
	static Calendar myCalendar;
	String formattedDate;

	BluetoothService mService = null;
	BluetoothDevice con_dev = null;
	private boolean deviceConnected = false;

	public String xml;
	private TextView textviewShift,textviewDate;
	String formattedBillId, responsemsg,outstanding="";
	String trnDate;
	float paperht, paperwt;
	private String firmname, address1, address2, address3, phone, mobile,email,GSTNo,
			vehKm,billIDstr;
	Dialog dialog;
	ImageView ivlogo;
	String intentFrom;

	// ProgressDialog pd_scan;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_billdetails);
		font = Typeface.createFromAsset(getAssets(), "font/BOOKOS.TTF");
		initView();
		checkShift();
		setListeners();

		if( intentFrom.equalsIgnoreCase("CardSaleTab") || intentFrom.equalsIgnoreCase("CreditSaleTab")){
			events();
		}else if(intentFrom.equalsIgnoreCase("RedeemPointsTab")) {
			events_redemPts();
		}
		connectDevice();
	}

	private String gettodaydate() {
		String result= null;
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy");
		try {
			//LocalDate localDate = LocalDate.now();
			Date date2 = new Date();
			result = dateFormat1.format(date2);
		}catch( Exception e){
			e.printStackTrace();
		}
		return result;
	}

	private void initView() {
		parent = BillDetails.this;
		databaseHelper = new DatabaseHelper(parent);
		myCalendar = Calendar.getInstance();

		((TextView) findViewById(R.id.tvpTitle)).setTypeface(font);
		textviewDate = (TextView) findViewById(R.id.textview_date);

		edCustName = (EditText) findViewById(R.id.edCustName);
		edCustBalance = (TextView) findViewById(R.id.edCustBalance);
		edrewrdspts = (TextView)findViewById(R.id.edrewrdspts);
		edCredlim = (TextView) findViewById(R.id.edCredlim);
		edVehNo = (EditText) findViewById(R.id.edVechNo);
		edVehKm = (EditText) findViewById(R.id.edVechKm);
		edProductCode = (EditText) findViewById(R.id.edProductCode);

		edRate = (EditText) findViewById(R.id.edRate);
		edQty = (EditText) findViewById(R.id.edQuantity);
		edAmt = (EditText) findViewById(R.id.edAmount);
		edDescription = (EditText) findViewById(R.id.edDescription);
		ivlogo = (ImageView) findViewById(R.id.iconlogo);

		edCustName.setTypeface(font);
		edCredlim.setTypeface(font);
		edCustBalance.setTypeface(font);
		edrewrdspts.setTypeface(font);
		edVehNo.setTypeface(font);
		edVehKm.setTypeface(font);
		edProductCode.setTypeface(font);
		edRate.setTypeface(font);
		edAmt.setTypeface(font);
		edQty.setTypeface(font);

		btnCashiername = (Button) findViewById(R.id.btngetCashier);
		button_cancel = (Button) findViewById(R.id.btnCancel);
		button_exit_print = (Button) findViewById(R.id.btnExitPrint);
		button_save = (Button) findViewById(R.id.btnSave);
		button_product_code = (Button) findViewById(R.id.btnProductCode);
		button_scan = (Button) findViewById(R.id.btnScan);
		btnchangeVehicle = (Button) findViewById(R.id.btnchangeVehicle);
		btndate = (Button) findViewById(R.id.btndate);

		btndate.setText(gettodaydate());
		button_cancel.setTypeface(font);
		button_exit_print.setTypeface(font);
		button_save.setTypeface(font);
		button_product_code.setTypeface(font);
		button_scan.setTypeface(font);
		btnCashiername.setTypeface(font);
		btnchangeVehicle.setTypeface(font);
		btndate.setTypeface(font);
		textviewShift = (TextView) findViewById(R.id.textview_shift);

		Intent intent = getIntent();
		intentFrom = intent.getStringExtra("CallFrom");
		Toast.makeText(parent,"Intent from "+intentFrom,Toast.LENGTH_LONG).show();

		if(PetroSoftData.imgPath!=null){
			Picasso.with(BillDetails.this).load(PetroSoftData.imgPath)/*
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)*/
					.into(ivlogo);
		}
		else{
			ivlogo.setImageResource(R.drawable.petro_soft_logo);
		}

		SharedPreferences sp = getSharedPreferences("SetupPref" ,Context.MODE_PRIVATE);
		String sps  = sp.getString("SetupPrinterSetting",null);
		String snrqac  = sp.getString("SetupNeedRQAC", "No");
		String snveh  = sp.getString("SetupNeedVehKm", "No");
		String sdi  = sp.getString("SetupDefaultItem", null);
		String sarp = sp.getString("SetupRewardsPoints", null);

		PetroSoftData.DefaultItem = sdi;
		PetroSoftData.RemoveRQAC = snrqac;
		PetroSoftData.NeedVehKm = snveh;
		PetroSoftData.PRINTERSETTING =sps;
		PetroSoftData.AllowRewardsPoints = sarp;

		if (PetroSoftData.WORKINGDATE==null){
			System.out.println("Current time => " + myCalendar.getTime());
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			formattedDate = df.format(myCalendar.getTime());
			PetroSoftData.WORKINGDATE = formattedDate;
			textviewDate.setText("Date : "+formattedDate);
		} else {
			textviewDate.setText("Date : "+PetroSoftData.WORKINGDATE);
		}

		TextDrawable d_dutyNo = new TextDrawable(parent);
		d_dutyNo.setText("Name: ");
		d_dutyNo.setTypeface(font);
		d_dutyNo.setTextSize(14);
		edCustName.setCompoundDrawables(d_dutyNo, null, null, null);

		TextDrawable d_BusNo = new TextDrawable(parent);
		d_BusNo.setText("Vehicle: ");
		d_BusNo.setTypeface(font);
		d_BusNo.setTextSize(14);
		edVehNo.setCompoundDrawables(d_BusNo, null, null, null);

		TextDrawable d_BusKm = new TextDrawable(parent);
		d_BusKm.setText("Km : ");
		d_BusKm.setTypeface(font);
		d_BusKm.setTextSize(14);
		edVehKm.setCompoundDrawables(d_BusKm, null, null, null);

		TextDrawable d_Time = new TextDrawable(parent);
		d_Time.setText("Product: ");
		d_Time.setTypeface(font);
		d_Time.setTextSize(14);
		edProductCode.setCompoundDrawables(d_Time, null, null, null);

		TextDrawable d_Desc = new TextDrawable(parent);
		d_Desc.setText("Description: ");
		d_Desc.setTypeface(font);
		d_Desc.setTextSize(14);
		edDescription.setCompoundDrawables(d_Desc, null, null, null);

		TextDrawable d_PlatformNo = new TextDrawable(parent);
		d_PlatformNo.setText("Rate: ");
		d_PlatformNo.setTypeface(font);
		d_PlatformNo.setTextSize(14);
		edRate.setCompoundDrawables(d_PlatformNo, null, null, null);

		TextDrawable d_Driver = new TextDrawable(parent);
		d_Driver.setText("Quantity: ");
		d_Driver.setTypeface(font);
		d_Driver.setTextSize(14);
		edQty.setCompoundDrawables(d_Driver, null, null, null);

		TextDrawable d_Conductornew = new TextDrawable(parent);
		d_Conductornew.setText("Amount: ");
		d_Conductornew.setTypeface(font);
		d_Conductornew.setTextSize(14);
		edAmt.setCompoundDrawables(d_Conductornew, null, null, null);

		if(PetroSoftData.veh_no==null){
			PetroSoftData.veh_no="";
		}

		edCustName.setText(PetroSoftData.name);
		if(PetroSoftData.cust_balance==null){
			PetroSoftData.cust_balance="0.0";
		}
		if(PetroSoftData.cred_lim==null){
			PetroSoftData.cred_lim="0.0";
		}

		if(PetroSoftData.Reward_Points == null){
			PetroSoftData.Reward_Points="0.0";
		}

		  edCustBalance.setText("  Balance:             "+PetroSoftData.cust_balance);
			  edCredlim.setText("  Credit Limit:       "+PetroSoftData.cred_lim);

		if(PetroSoftData.AllowRewardsPoints != null){
			if(PetroSoftData.AllowRewardsPoints.equalsIgnoreCase("Yes")){
			edrewrdspts.setText("   Balance points:  "+PetroSoftData.Reward_Points);
			}else if(PetroSoftData.AllowRewardsPoints.equalsIgnoreCase("No")) {
				edrewrdspts.setVisibility(View.GONE);
			}
		}else {
			PetroSoftData.AllowRewardsPoints = "No";
		}

		edVehNo.setText(PetroSoftData.veh_no);
		if(PetroSoftData.DefaultItem!=null&&!(PetroSoftData.DefaultItem.equalsIgnoreCase(""))){
			edDescription.setText(PetroSoftData.DefaultItem);
			getITemCodeONITemName();
		}else if(PetroSoftData.item_desc!=null){
			edDescription.setText(PetroSoftData.item_desc);
			getITemCodeONITemName();
		}
		if(PetroSoftData.NeedVehKm.equalsIgnoreCase("No")){
			edVehKm.setVisibility(View.GONE);
			vehKm = "";
		}
		if(PetroSoftData.item_desc==null){
			edProductCode.setText(PetroSoftData.item_code);
		}
		if(PetroSoftData.qty!=null){
			edQty.setText(PetroSoftData.qty);
		}
		new GetBillNo().execute();
		mService = new BluetoothService(parent, mHandler);

		if (mService.isAvailable() == false) {

			Toast.makeText(parent, "Bluetooth is not available",
					Toast.LENGTH_LONG).show();
		}

		getFirmDetails();
		checkDatabaseForData();
		getAmount();
        getQuantity("0.0");

		if(PetroSoftData.RemoveRQAC.equalsIgnoreCase("Yes")){
			doOnYesRQAC();
		}
	}

	private void setListeners() {
		button_product_code.setOnClickListener(this);
		button_save.setOnClickListener(this);
		button_cancel.setOnClickListener(this);
		button_exit_print.setOnClickListener(this);
		button_scan.setOnClickListener(this);
		btnchangeVehicle.setOnClickListener(this);
		btnCashiername.setOnClickListener(this);
		btndate.setOnClickListener(this);

		textviewShift.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				try {
					AlertDialog.Builder builder = new AlertDialog.Builder(parent);

					builder.setMessage("Are you sure you want to change Shift");

					builder.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									showListDialog();
									dialog.dismiss();
								}

								private void showListDialog() {
									// TODO Auto-generated method stub
									PopupMenu popup = new PopupMenu(parent,
											textviewShift);
									popup.getMenuInflater().inflate(
											R.menu.shift_menu, popup.getMenu());
									popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
										public boolean onMenuItemClick(
												MenuItem item) {
											String data[] = item.getTitle()
													.toString().split(" ");
											CommonUtilities.clearTable(parent,
													"Shift");
											databaseHelper.AddShift(data[1]);
											checkShift();
											return true;
										}
									});

									popup.show(); // showing popup menu

								}
							});

					builder.setNegativeButton("No",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.show();
				} catch (Exception e) {
				}

				return false;
			}
		});
	}

	private void checkShift() {

		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase sql = db1.getWritableDatabase();
		Cursor cursor = sql.rawQuery("Select * from Shift", null);

		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			String str = cursor.getString(0);
			cursor.close();
			sql.close();
			db1.close();
			PetroSoftData.SHIFT = str;
			textviewShift.setText("Shift " + str);

		} else {
			textviewShift.setText("Shift I");
			PetroSoftData.SHIFT = "I";
			cursor.close();
			sql.close();
			db1.close();
		}
	}

	private void events() {
		edProductCode.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					checkDatabaseForData();
				}
			}
		});

		edQty.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
                		getAmount();
			}
		});

		edRate.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
                getAmount();
			}
		});

	}

	private void events_redemPts() {
		edProductCode.setOnFocusChangeListener(new OnFocusChangeListener() {

			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					checkDatabaseForData();
				}
			}
		});

		edAmt.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				String s1 = String.valueOf(s);
				getQuantity(s1);

			}
		});

		edRate.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
										  int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				getAmount();
			}
		});

	}

	private void doOnYesRQAC() {
		edProductCode.setClickable(false);
		edProductCode.setEnabled(false);
		edProductCode.setFocusable(false);
		edProductCode.setFocusableInTouchMode(false);
		edDescription.setClickable(false);
		edDescription.setEnabled(false);
		edDescription.setFocusable(false);
		edDescription.setFocusableInTouchMode(false);
		button_product_code.setVisibility(View.GONE);
		edQty.setVisibility(View.GONE);
		edRate.setVisibility(View.GONE);
		edAmt.setVisibility(View.GONE);
		button_save.setVisibility(View.GONE);
		btnCashiername.setVisibility(View.GONE);
		btnchangeVehicle.setVisibility(View.GONE);
	}

	private void getAmount() {
		if (!edQty.getText().toString().trim().equalsIgnoreCase("")) {
			quantity = Double.parseDouble(edQty.getText().toString().trim());
		} else {
			quantity = 0;
		}

		if (!edRate.getText().toString().trim().equalsIgnoreCase("")) {
			rate = Double.parseDouble(edRate.getText().toString().trim());
		} else {
			rate = 0;
		}

		if (!edAmt.getText().toString().trim().equalsIgnoreCase("")) {
			amount = Double.parseDouble(edAmt.getText().toString().trim());
		} else {
			amount = 0;
		}

		amount = rate * quantity;
		// Log.e("amount",amount+" : "+String.format("%f", amount));
		//String a = String.valueOf(amount);
		//String b = String.format("%2f",amount);

		//edAmt.setText(String.valueOf(amount));
		edAmt.setText(new DecimalFormat("##.##").format(amount));
		//edAmt.setText(String.format("%f",amount));
	}

	private void getQtyByEditAmount() {
		amount = Double.parseDouble(edAmt.getText().toString().trim());
		rate = Double.parseDouble(edRate.getText().toString().trim());

		quantity = amount  * rate ;
		// Log.e("amount",amount+" : "+String.format("%f", amount));
		edQty.setText(String.valueOf(quantity));
		//edAmt.setText(String.format("%f",amount));
	}

	private void getAmountByEditQty() {
		quantity = Double.parseDouble(edQty.getText().toString().trim());
		rate = Double.parseDouble(edRate.getText().toString().trim());

		amount = rate * quantity;
		// Log.e("amount",amount+" : "+String.format("%f", amount));
		edAmt.setText(String.valueOf(amount));
		//edAmt.setText(String.format("%f",amount));
	}

	private void getAmountByEditRate() {
			quantity = Double.parseDouble(edQty.getText().toString().trim());
			rate = Double.parseDouble(edRate.getText().toString().trim());

		amount = rate * quantity;
		// Log.e("amount",amount+" : "+String.format("%f", amount));
		edAmt.setText(String.valueOf(amount));
		//edAmt.setText(String.format("%f",amount));
	}

    private void getQuantity(String amt) {
		quantity = 0;
        if (!edAmt.getText().toString().trim().equalsIgnoreCase("")) {
            amount = Double.parseDouble(amt);
        } else {
            amount = 0;
        }

        if (!edRate.getText().toString().trim().equalsIgnoreCase("")) {
            rate = Double.parseDouble(edRate.getText().toString().trim());
        } else {
            rate = 0;
        }

		if((rate == 0) || (rate == 0.0)){
			edQty.setText("0.0");
		}else if(rate <= amount){
           quantity = amount/rate ;
		   edQty.setText(new DecimalFormat("##.##").format(quantity));
       }else if(amount == 0){
           quantity = 0 ;
       }else if(rate > amount){
			quantity = amount/rate ;
			edQty.setText(new DecimalFormat("##.##").format(quantity));
		   //Toast.makeText(parent,"Amount should be greater than rate",Toast.LENGTH_SHORT).show();
	   }
    }

	private void checkDatabaseForData() {
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();

		Cursor cursor = db.rawQuery("Select * from Item where ItemCode='"+ edProductCode.getText().toString()+"'",null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				edDescription.setText(cursor.getString(0));
				rate = Double.parseDouble(cursor.getString(2));
				edRate.setText(Double.toString(rate));
			} while (cursor.moveToNext());

			cursor.close();
			db.close();
			db1.close();
		}
	}
	
	private void getITemCodeONITemName() {
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();

		Cursor cursor = db.rawQuery("Select * from Item where ItemName='"+edDescription.getText().toString()+"'",
				null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				edProductCode.setText(cursor.getString(1));
				PetroSoftData.item_code=cursor.getString(1);
				rate = Double.parseDouble(cursor.getString(2));
				edRate.setText(Double.toString(rate));
			} while (cursor.moveToNext());

			cursor.close();
			db.close();
			db1.close();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnProductCode:
			intent = new Intent(parent, ProductSelectionActivity.class);
			startActivityForResult(intent,PetroSoftData.REQUEST_GET_PRODUCT_DETAILS);
			break;
		case R.id.btnSave:
			if (validate()) {
				databaseHelper.AddBillDetails(billId, edProductCode.getText()
						.toString(), edRate.getText().toString(), edQty
						.getText().toString(), edAmt.getText().toString(),
						PetroSoftData.SHIFT, vehKm,PetroSoftData.sodetailid,PetroSoftData.veh_no);
				clearViews();
			}
			break;
		case R.id.btnExitPrint:

			if (validate()) {
				dialog = new Dialog(parent);
				dialog.setContentView(R.layout.dialog_message);
				TextView txtMsg = (TextView) dialog.findViewById(R.id.textMsg);
				Button btnyes = (Button) dialog.findViewById(R.id.btn_yes);
				Button btnno = (Button) dialog.findViewById(R.id.btn_no);
				if (deviceConnected) {
					txtMsg.setText("Your device is connected to printer?");
				} else {
					txtMsg.setText("Your device is not connected to printer, do you want to save?");
				}
				btnyes.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (deviceConnected) {
							if (validate()) {
								databaseHelper.AddBillDetails(billId, edProductCode
												.getText().toString(), edRate.getText()
												.toString(), edQty.getText().toString(),
										edAmt.getText().toString(),
										PetroSoftData.SHIFT, vehKm, PetroSoftData.sodetailid,PetroSoftData.veh_no);
								createXml();
								new SaveBillDetails().execute();
								dialog.dismiss();
							}
						} else {
							if (validate()) {
								databaseHelper.AddBillDetails(billId, edProductCode
												.getText().toString(), edRate.getText()
												.toString(), edQty.getText().toString(),
										edAmt.getText().toString(),
										PetroSoftData.SHIFT, vehKm, PetroSoftData.sodetailid,PetroSoftData.veh_no);
								createXml();
								new SaveBillDetails().execute();
								dialog.dismiss();
							}
						}
					}
				});

				btnno.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (validate()) {
							databaseHelper.AddBillDetails(billId, edProductCode
											.getText().toString(), edRate.getText()
											.toString(), edQty.getText().toString(), edAmt
											.getText().toString(), PetroSoftData.SHIFT,
									vehKm, PetroSoftData.sodetailid,PetroSoftData.veh_no);
							createXml();
							new SaveBillDetails().execute();
							dialog.dismiss();
						}
					}
				});
				dialog.show();
			}
			break;
		case R.id.btnCancel:
			try {
				showDialog("Are you sure you want to cancel?");
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.btnScan:
			scanBluetooth();
			break;
		case R.id.btnchangeVehicle:

			Intent intent = new Intent(getApplicationContext(),
					GetVehicleActivity.class);
			startActivityForResult(intent,
					PetroSoftData.REQUEST_GET_VEHICLE_DETAILS);
			break;
		case R.id.btngetCashier:
			Intent intent_CASHIER = new Intent(getApplicationContext(),
					GetCashierActivity.class);
			startActivityForResult(intent_CASHIER,
					PetroSoftData.REQUEST_GET_CASHIER_DETAILS);
			break;
		case R.id.btndate:

			Date date = new Date();
			final Calendar c = Calendar.getInstance();

			year = c.get(Calendar.YEAR);
			month = c.get(Calendar.MONTH);
			day = c.get(Calendar.DAY_OF_MONTH);

			// Launch Date Picker Dialog
			DatePickerDialog datePickerDialog = new DatePickerDialog(parent,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker datePicker, int year,
								int monthOfYear, int dayOfMonth) {
							// Display Selected date in textbox
							btndate.setText(dayOfMonth + "-"
									+ (monthOfYear + 1) + "-" + year);
							trnDate = dayOfMonth + "-" + (monthOfYear + 1)
									+ "-" + year;
						}
					}, year, month, day);
			datePickerDialog.show();
			break;
		}
	}

	private void createXml() {
		xml = null;
		xml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
		xml += "<Vritti>";
		try {
			DatabaseHelper db1 = new DatabaseHelper(parent);
			SQLiteDatabase db = db1.getWritableDatabase();
			billIDstr = Integer.toString(billId);

			Cursor cursor = db.rawQuery(
					"Select * from BillDetails where BillId='"+Integer.toString(billId)+"'",
					null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					xml += "<Sale>";
					xml += "<cust_code>" + PetroSoftData.cust_code+ "</cust_code>";
					xml += "<rfid_card>" + PetroSoftData.rfid_card+ "</rfid_card>";

					xml += "<coupon_no>" + PetroSoftData.Coupon+ "</coupon_no>";

					xml += "<item_Code>"
							+ cursor.getString(cursor
									.getColumnIndex("ItemCode"))
							+ "</item_Code>";
					xml += "<rate>"
							+ cursor.getString(cursor.getColumnIndex("Rate"))
							+ "</rate>";
					xml += "<qty>"
							+ cursor.getString(cursor.getColumnIndex("Qty"))
							+ "</qty>";
					xml += "<amount>"
							+ cursor.getString(cursor.getColumnIndex("Amount"))
							+ "</amount>";

					xml += "<km_read>" + vehKm + "</km_read>";
					xml += "<shift>" + PetroSoftData.SHIFT + "</shift>";
					xml += "<srno>" + formattedBillId + "</srno>";
					xml += "<working_date>" + PetroSoftData.WORKINGDATE + "</working_date>";
					xml += "<trn_date>" + trnDate + "</trn_date>";
					xml += "<cashier_cd>" + PetroSoftData.Cashier_acno+ "</cashier_cd>";
					xml += "<sodetailid>" + PetroSoftData.sodetailid+ "</sodetailid>";
					xml += "<vehno>" + PetroSoftData.veh_no	+ "</vehno>";

					if(intentFrom.equalsIgnoreCase("RedeemPointsTab")){
						xml += "<pointsused>" +"Yes" + "</pointsused>";
					}else {
						xml += "<pointsused>" +"No" + "</pointsused>";
					}
					xml += "</Sale>";

				} while (cursor.moveToNext());

				cursor.close();
				db.close();
				db1.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		xml += "</Vritti>";
	}

	private void getFirmDetails() {
		// TODO Auto-generated method stub
		try {

			DatabaseHelper db1 = new DatabaseHelper(parent);
			SQLiteDatabase db = db1.getWritableDatabase();

			Cursor cursor = db.rawQuery("Select * from FirmDetails", null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();

				firmname = cursor.getString(cursor.getColumnIndex("FirmName"));
				address1 = cursor.getString(cursor.getColumnIndex("Address1"));
				address2 = cursor.getString(cursor.getColumnIndex("Address2"));
				address3 = cursor.getString(cursor.getColumnIndex("Address3"));
				phone = cursor.getString(cursor.getColumnIndex("Phone"));
				mobile = cursor.getString(cursor.getColumnIndex("Mobile"));
				email = cursor.getString(cursor.getColumnIndex("email"));
				GSTNo = cursor.getString(cursor.getColumnIndex("GSTNo"));

				if (firmname.length() > 32) {
					firmname = firmname.substring(0, 32);
				} else if (firmname.length() <= 32) {
					int diff = 32 - firmname.length();
					for (int i = 0; i < diff / 2; i++) {
						firmname = " " + firmname;
					}
				}
				if (address1.length() > 32) {
					address1 = address1.substring(0, 32);
				} else if (address1.length() <= 32) {
					int diff = 32 - address1.length();
					for (int i = 0; i < diff / 2; i++) {
						address1 = " " + address1;
					}
				}
				if (address2.length() > 32) {
					address2 = address2.substring(0, 32);
				} else if (address2.length() <= 32) {
					int diff = 32 - address2.length();
					for (int i = 0; i < diff / 2; i++) {
						address2 = " " + address2;
					}
				}
				if (address3.length() > 32) {
					address3 = address3.substring(0, 32);
				} else if (address3.length() <= 32) {
					int diff = 32 - address3.length();
					for (int i = 0; i < diff / 2; i++) {
						address3 = " " + address3;
					}
				}
				if (mobile.length() > 32) {
					mobile = mobile.substring(0, 32);
				} else if (mobile.length() <= 32) {
					int diff = 32 - mobile.length();
					for (int i = 0; i < diff / 2; i++) {
						mobile = " " + mobile;
					}
				}
				if (email.length() > 32) {
					email = email.substring(0, 32);
				} else if (email.length() <= 32) {
					int diff = 32 - email.length();
					for (int i = 0; i < diff / 2; i++) {
						email = " " + email;
					}
				}
				if (GSTNo.length() > 32) {
					GSTNo = GSTNo.substring(0, 32);
				} else if (GSTNo.length() <= 32) {
					int diff = 32 - GSTNo.length();
					for (int i = 0; i < diff / 2; i++) {
						GSTNo = " " + GSTNo;
					}
				}

				cursor.close();
				db.close();
				db1.close();
			}

		} catch (Exception e) {
			Toast.makeText(parent, "Database error...", Toast.LENGTH_LONG)
					.show();
		}
	}

	private void print() {
		if(PetroSoftData.PRINTERSETTING.equals("System")){
			//Toast.makeText(parent, "Work in Progress",Toast.LENGTH_SHORT).show();
			new PrintBill().execute();
			//printSys();
			return;
		}
		
		double total = 0;
		String datetime = CommonUtilities.getCurrentDate();
		String username = PetroSoftData.username;
		String msg = null;
		msg = "\n\n" + firmname + "\n\n";
		msg += address1 + "\n";
		msg += address2 + "\n";
		msg += address3 + "\n";
		msg += email + "\n";
		msg += GSTNo + "\n\n";

		msg += "Bill No. : " + formattedBillId + "\n";
			msg += "Name : " + PetroSoftData.name.trim() + "\n";
			msg += "Vehicle : " + PetroSoftData.veh_no.trim() + "\n";
		if (vehKm!=null && !(vehKm.equals(""))) {
			msg += "Vehicle Km : " + vehKm + "\n";
		}

		msg += "User : " + username + "\n\n";
		msg += "Date : " + datetime + "\n";
		msg += "--------------------------------\n";
		msg += "Item-HSN    Qty   Rate    Amount\n";
		msg += "--------------------------------\n";

		try {
			DatabaseHelper db1 = new DatabaseHelper(parent);
			SQLiteDatabase db = db1.getWritableDatabase();

			Cursor cursor = db
					.rawQuery(
							"Select ItemName,Qty,Rate,Amount,HSNCode from Item,BillDetails "
							+ "where Item.ItemCode=BillDetails.ItemCode AND BillDetails.BillId='"+Integer.toString(billId)+"'",
							null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				do {
					String itemNameToPrint = cursor.getString(0)/*+" "+cursor.getString(4)*/;

					if (itemNameToPrint.length() > 10) {
						itemNameToPrint = itemNameToPrint.substring(0, 10);
					} else if (itemNameToPrint.length() <= 10) {
						int diff = 10 - itemNameToPrint.length();
						for (int i = 0; i < diff; i++) {
							itemNameToPrint += " ";
						}
					}
					String itemQtyToPrint = cursor.getString(1);
					itemQtyToPrint = itemQtyToPrint
							.replaceFirst("^0+(?!$)", "");

					if (itemQtyToPrint.contains(".")) {
						{
							itemQtyToPrint += "000";
							itemQtyToPrint = itemQtyToPrint.substring(0,
									itemQtyToPrint.lastIndexOf(".") + 3);
						}
					} else {
						itemQtyToPrint += ".00";
					}

					if (itemQtyToPrint.length() <= 5) {
						int diff = 5 - itemQtyToPrint.length();
						if (diff > 0) {
							for (int i = 0; i < diff; i++) {
								itemQtyToPrint = " " + itemQtyToPrint;
							}
						}
					}
					String itemRateToPrint = cursor.getString(2);
					itemRateToPrint = itemRateToPrint.replaceFirst("^0+(?!$)",
							"");
					if (itemRateToPrint.contains(".")) {
						itemRateToPrint += "000";
						itemRateToPrint = itemRateToPrint.substring(0,
								itemRateToPrint.lastIndexOf(".") + 3);
					} else {
						itemRateToPrint += ".00";
					}

					if (itemRateToPrint.length() <= 7) {
						int diff = 7 - itemRateToPrint.length();
						if (diff > 0) {
							for (int i = 0; i < diff; i++) {
								itemRateToPrint = " " + itemRateToPrint;
							}
						}
					}
					String itemAmountToPrint = cursor.getString(3);
					itemAmountToPrint = itemAmountToPrint.replaceFirst(
							"^0+(?!$)", "");

					if (itemAmountToPrint.contains(".")) {
						itemAmountToPrint += "000";
						itemAmountToPrint = itemAmountToPrint.substring(0,
								itemAmountToPrint.lastIndexOf(".") + 3);
					} else {
						itemAmountToPrint += ".00";
					}

					total += Double.parseDouble(itemAmountToPrint.trim());
					if (itemAmountToPrint.length() <= 10) {
						int diff = 10 - itemAmountToPrint.length();
						if (diff > 0) {
							for (int i = 0; i < diff; i++) {
								itemAmountToPrint = " " + itemAmountToPrint;
							}
						}
					}

					msg += itemNameToPrint + itemQtyToPrint + itemRateToPrint
							+ itemAmountToPrint + "\n"
							+cursor.getString(4)+ "\n";
				} while (cursor.moveToNext());

				cursor.close();
				db.close();
				db1.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		msg += "--------------------------------\n";

		String amountToPrint = String.format("%.2f", /*input)Double.toString(*/total);
		if (amountToPrint.length() <= 26) {
			int diff = 26 - amountToPrint.length();
			if (diff > 0) {
				for (int i = 0; i <= diff; i++) {
					amountToPrint = " " + amountToPrint;
				}
			}
		}

		msg += "Total" + amountToPrint + "\n";
		msg += "--------------------------------\n\n";


		if ((phone!=null && !(phone.trim().equalsIgnoreCase("")))||(mobile!=null && !(mobile.trim().equalsIgnoreCase("")))) {
			String contact = "Contact : " + phone;
			if (contact.length() > 32) {
				contact = contact.substring(0, 32);
			} else if (contact.length() <= 32) {
				int diff = 32 - contact.length();
				for (int i = 0; i < diff / 2; i++) {
					contact = " " + contact;
				}
			}
			msg += contact + "\n";
			msg += mobile + "\n\n";
		}
		msg += "\n\n                 Authorised Sign\n\n";
		if (PetroSoftData.TnC==null|| PetroSoftData.TnC.equalsIgnoreCase("")){
			msg += "       --- Thank You --- \n";
		} else {
			msg += "   --- Terms & Conditions --- \n";
			String text2[] = PetroSoftData.TnC.split("\n");

			List<String> strings = new ArrayList<String>();
			for (int k = 0; k < text2.length; k++) {
				int index = 0;
				while (index < text2[k].length()) {
					strings.add(text2[k].substring(index, Math.min(index + 31, text2[k].length())));
					index += 31;
				}
			}
			for (int j = 0; j < strings.size(); j++) {
				msg += strings.get(j)+"\n";
			}
		}



		if (msg.length() > 0) {
			mService.sendMessage(msg + "\n", "GBK");
		}
	}

	private boolean validate() {
		// TODO Auto-generated method stub

		if (PetroSoftData.NeedVehKm.equals("No")){
			vehKm="";
		}else {
			vehKm = edVehKm.getText().toString().trim();
		}
		float cred = Float.parseFloat(PetroSoftData.cred_lim);
		float RewardsPt = Float.parseFloat(PetroSoftData.Reward_Points);
		float bal = Float.parseFloat(PetroSoftData.cust_balance);
		//float amt = Float.parseFloat(edAmt.getText().toString());
		double amt = Double.parseDouble(edAmt.getText().toString());
		if (cred > 0 && (cred < (bal+amt)) ){
			dialog = new Dialog(parent);
			dialog.setContentView(R.layout.dialog_message_ok);
			TextView txtMsg = (TextView) dialog.findViewById(R.id.textMsg);
			Button btnOK = (Button) dialog.findViewById(R.id.btn_yes_ok);
				txtMsg.setText("Credit Limit is Exhausted");
			btnOK.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
			}
		});
			dialog.show();
			return false;
		}

		if (true) {
			return true;
		}
		return false;
	}

	private void clearViews() {
		edAmt.setText("");
		edVehKm.setText("");
		edProductCode.setText("");
		edDescription.setText("");
		edQty.setText("");
		edRate.setText("");
		btnCashiername.setText("");
	}

	public void showDialog(String message) throws Exception {
		AlertDialog.Builder builder = new AlertDialog.Builder(parent);

		builder.setMessage(message);

		builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				removeData();
				dialog.dismiss();
				finish();
			}
		});

		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.show();
	}

	private void removeData() {
		try {
			DatabaseHelper db1 = new DatabaseHelper(parent);
			SQLiteDatabase db = db1.getWritableDatabase();
			db.delete("Bill", "BillId=?",
					new String[] { Integer.toString(billId) });
			db.delete("BillDetails", "BillId=?",
					new String[] { Integer.toString(billId) });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void scanBluetooth() {

		startActivityForResult(new Intent(parent, DeviceListActivity.class),
				PetroSoftData.REQUEST_CONNECT_DEVICE);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// count = 0;

		if (mService.isBTopen() == false) {
			Intent enableIntent = new Intent(
					BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivityForResult(enableIntent,
					PetroSoftData.REQUEST_ENABLE_BT);

		}
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg1) {
			switch (msg1.what) {
			case BluetoothService.MESSAGE_STATE_CHANGE:
				switch (msg1.arg1) {
				case BluetoothService.STATE_CONNECTED:
					Toast.makeText(parent, "Connect successful",
							Toast.LENGTH_SHORT).show();
					deviceConnected = true;

					break;
				case BluetoothService.STATE_CONNECTING: // ��������
					//Log.d("��������", "��������.....");
					break;
				case BluetoothService.STATE_LISTEN: // �������ӵĵ���
				case BluetoothService.STATE_NONE:
					//Log.d("��������", "�ȴ�����.....");
					break;
				}
				break;
			case BluetoothService.MESSAGE_CONNECTION_LOST: // �����ѶϿ�����
				/*Toast.makeText(parent, "Device connection was lost",
						Toast.LENGTH_SHORT).show();*/
				deviceConnected = false;
				break;
			case BluetoothService.MESSAGE_UNABLE_CONNECT: // �޷������豸
				Toast.makeText(parent, "Unable to connect device",
						Toast.LENGTH_SHORT).show();
				deviceConnected = false;
				break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void connectDevice() {
		// TODO
		if(mService.isBTopen()==false) {
			SharedPreferences LoginPref = getApplicationContext()
					.getSharedPreferences("BTPref",Context.MODE_PRIVATE); // 0 - for private mode
			SharedPreferences.Editor edtcv = LoginPref.edit();
			edtcv.putString("BTAddress", null);
			edtcv.commit();
		}
			SharedPreferences sp = getSharedPreferences("BTPref", Context.MODE_PRIVATE);
			String address = sp.getString("BTAddress", null);

			//String address = CommonUtilities.getBluetoothAddress(parent);
			if (address != null) {
				con_dev = mService.getDevByMac(address);
				mService.connect(con_dev);
				Log.e("Auto connected", "state : " + mService.getState());
			}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case PetroSoftData.REQUEST_ENABLE_BT:
			if (resultCode == Activity.RESULT_OK) {
				Toast.makeText(parent, "Bluetooth open successful",
						Toast.LENGTH_LONG).show();
			} else {
				// finish();
			}
			break;
		case PetroSoftData.REQUEST_CONNECT_DEVICE:
			if (resultCode == Activity.RESULT_OK) {
				String address = data.getExtras().getString(
						DeviceListActivity.EXTRA_DEVICE_ADDRESS);
				CommonUtilities.clearTable(parent, "Bluetooth_Address");
				databaseHelper.AddBluetooth(address);
				con_dev = mService.getDevByMac(address);
				mService.connect(con_dev);
				SharedPreferences LoginPref = getApplicationContext()
						.getSharedPreferences("BTPref",Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor edtcv = LoginPref.edit();
				edtcv.putString("BTAddress", address);
				edtcv.commit();
				Log.e("bluetooth state", "state : " + mService.getState());
			}

			break;
		case PetroSoftData.REQUEST_GET_PRODUCT_DETAILS:
			if (!data.getStringExtra("MESSAGE").equals("-NA-")) {
				String message = data.getStringExtra("MESSAGE");
				String str[] = message.split("  - ");
				edProductCode.setText(str[0]);
				checkDatabaseForData();
			}
			break;

		case PetroSoftData.REQUEST_GET_CASHIER_DETAILS:
			String message = data.getStringExtra("MESSAGE");
			btnCashiername.setText(message);

			break;
		case PetroSoftData.REQUEST_GET_VEHICLE_DETAILS:
			String messages = data.getStringExtra("MESSAGE");
			edVehNo.setText(messages);

			break;
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mService != null)
			mService.stop();
		mService = null;
		//dialog.dismiss();
	}

	@Override
	public void onBackPressed() {
		try {
			showDialog("Are you sure you want to cancel?");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class GetBillNo extends AsyncTask<Void, Void, String> {
		String responseString = null;

		@Override
		protected String doInBackground(Void... params) {
			try {
				// String fullURL = getResources().getString(R.string.url);
				String fullURL = PetroSoftData.URL;
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_BILL_ID);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_BILL_ID, envelope);
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
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Toast.makeText(parent, result, Toast.LENGTH_LONG).show();
			super.onPostExecute(result);
			if (!responseString.equals("error")) {
				if(responseString.contains(":")) {
					String mny[] = responseString.split(":");
					responseString = mny[0];
					outstanding = mny[1];
				}
				billId = Integer.parseInt(responseString);
				formattedBillId = String.format("%05d", billId);
			} else {
				Toast.makeText(parent, "Bill Id generation error",
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public class SaveBillDetails extends AsyncTask<String, Void, String> {
		ProgressDialog progressDialog;

		String fullURL = PetroSoftData.URL;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("Saving Data on Server...");
			progressDialog.show();
		}

		protected String doInBackground(String... params) {
			try {
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_UPDATE_SALE_XML);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;
				// adding parameters
				request.addProperty("xmlstr", xml.toString());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_UPDATE_SALE_XML, envelope);

				/*******************************/
                if (envelope.bodyIn instanceof SoapFault) {
                    String str= ((SoapFault) envelope.bodyIn).faultstring;
                    Log.i("", str);

                    // Another way to travers through the SoapFault object
    /*  Node detailsString =str= ((SoapFault) envelope.bodyIn).detail;
        Element detailElem = (Element) details.getElement(0)
                     .getChild(0);
        Element e = (Element) detailElem.getChild(2);faultstring;
        Log.i("", e.getName() + " " + e.getText(0)str); */
                } else {
                    SoapObject response = (SoapObject) envelope.bodyIn;
                    responsemsg = response.getProperty(0).toString();
                    Log.d("WS", String.valueOf(response));
                }
				/********************************/
				/*SoapObject response = (SoapObject) envelope.bodyIn;
				responsemsg = response.getProperty(0).toString();*/
			} catch (Exception e) {
				responsemsg = "error";
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {

			super.onPostExecute(result);

			try {
				if ((progressDialog != null) && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			} catch (final IllegalArgumentException e) {
				// Handle or log or ignore
			} catch (final Exception e) {
				// Handle or log or ignore
			} finally {
				progressDialog = null;
			}

			//progressDialog.dismiss();
			try{

				if (responsemsg.equalsIgnoreCase("ok")) {
					Toast.makeText(parent, "Data Saved Successfully..!",
							Toast.LENGTH_LONG).show();
					if(PetroSoftData.PRINTERSETTING==null){
						PetroSoftData.PRINTERSETTING ="System";
					}

					if (deviceConnected || PetroSoftData.PRINTERSETTING.equals("System")) {

						print();
						AlertDialog.Builder builder = new AlertDialog.Builder(
								parent);
						builder.setTitle("PetroSoft");
						builder.setMessage("Do you want to take another printout?");
						builder.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										// TODO
										dialog.dismiss();
										print();
										//dialog.dismiss();
										clearViews();
										Intent intent=new Intent(getApplicationContext(), MainActivity.class);
										startActivity(intent);
										finish();
									}
								});
						builder.setNegativeButton(android.R.string.cancel,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
														int id) {
										// TODO
										dialog.dismiss();
										clearViews();
										Intent intent=new Intent(getApplicationContext(), MainActivity.class);
										startActivity(intent);
										finish();
									}
								});
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				}else if(responsemsg == null){
					Toast.makeText(parent, "Server Error..", Toast.LENGTH_LONG)
							.show();
				}else {
					Toast.makeText(parent, "Server Error..", Toast.LENGTH_LONG)
							.show();
				}

			} catch (NullPointerException ne){
				ne.printStackTrace();
			} catch (Exception e){
				e.printStackTrace();
			}

		}
	}

	public class PrintBill extends AsyncTask<String, Void, String> {
		ProgressDialog progressDialog;
		String fullURL = PetroSoftData.URL;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("Printing from Server...");
			progressDialog.show();
		}

		protected String doInBackground(String... params) {
			try {
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_PRINT_BILL);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;
				// adding parameters
				request.addProperty("bill_No", formattedBillId);

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_PRINT_BILL, envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;
				responsemsg = response.getProperty(0).toString();
			} catch (Exception e) {
				responsemsg = "error";
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if(progressDialog != null || progressDialog.isShowing()){
				progressDialog.dismiss();
			}else {
			}
		//	progressDialog.dismiss();
			progressDialog.dismiss();

			try {
				if ((progressDialog != null) && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
			} catch (final IllegalArgumentException e) {
				// Handle or log or ignore
			} catch (final Exception e) {
				// Handle or log or ignore
			} finally {
				progressDialog = null;
			}

			//progressDialog.dismiss();
			if (responsemsg.equalsIgnoreCase("ok")) {
				Toast.makeText(parent, "Bill Printed Successfully..!",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(parent, "Server Error..", Toast.LENGTH_LONG)
						.show();
			}
		}
	}
}