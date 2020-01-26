package com.vritti.petrosoft;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.ksoap2.SoapEnvelope;
import org.jsoup.nodes.Document;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;
import com.vritti.database.ItemHelper;

public class LoginActivity extends Activity {
	private Context parent;
	private Button loginButton, updateButton;
	private AutoCompleteTextView edittext_username;
	private EditText edittext_password;
	private String password, dialogopen="no";
	private DatabaseHelper databaseHelper;
	private ArrayList<String> NameList;
	private String fullURL = PetroSoftData.URL;
	private ImageView imgLogout,ivlogo, iconlogo;
    Dialog dialog;

	public static final int RequestPermissionCode = 7;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		initViews();

		if (PetroSoftData.username!=null&&PetroSoftData.password!=null){
			loginButton.setText("Continue");
			imgLogout.setVisibility(View.VISIBLE);
			edittext_username.setText(PetroSoftData.username);
			edittext_password.setText(PetroSoftData.password);
			edittext_username.setFocusable(false);
			edittext_username.setFocusableInTouchMode(false);
			edittext_password.setFocusable(false);
			edittext_password.setFocusableInTouchMode(false);
		}else {
			imgLogout.setVisibility(View.GONE);
			updateCustomerSpinner();
			ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
					android.R.layout.simple_list_item_1, NameList);
			edittext_username.setThreshold(1);
			edittext_username.setAdapter(adapter1);
		}
		setListeners();
	}
	
	private void updateCustomerSpinner() {
		NameList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();

		Cursor cursor = db.rawQuery(
				"Select Name from CashierMaster where AcType = 'Cashier' order by Name ASC", null);

		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			do {
				NameList.add(cursor.getString(0));
			} while (cursor.moveToNext());
			cursor.close();
			db.close();
			db1.close();
		}
		
	}	
	
	private void initViews() {
		parent = LoginActivity.this;
		databaseHelper = new DatabaseHelper(parent);

		loginButton = (Button) findViewById(R.id.btnLogin);
		//loginButton.setClickable(false);
		updateButton = (Button) findViewById(R.id.btnUpdate);
		edittext_username = (AutoCompleteTextView) findViewById(R.id.edUserId);
		NameList = new ArrayList<String>();
		edittext_password = (EditText) findViewById(R.id.edPassword);
		imgLogout = (ImageView) findViewById(R.id.imgLogout);
        ivlogo = (ImageView) findViewById(R.id.ivlogo);
        iconlogo = (ImageView) findViewById(R.id.iconlogo);

		SharedPreferences sp = getSharedPreferences("LoginPref" ,Context.MODE_PRIVATE);
		String sun  = sp.getString("LoginUserName",null);
		String spwd  = sp.getString("LoginPassword",null);
		PetroSoftData.username =sun;
		PetroSoftData.password = spwd;
        SharedPreferences spref = getSharedPreferences("SetupPref" ,Context.MODE_PRIVATE);
        String simgpath = spref.getString("SetupImgPath",null);
        PetroSoftData.imgPath =simgpath;

			if(PetroSoftData.imgPath!=null) {

				//edfilepath.setText(PetroSoftData.imgPath);
				Picasso.with(LoginActivity.this).load(PetroSoftData.imgPath)/*
                    .placeholder(R.drawable.petro_soft_logo)
                    .error(R.drawable.petro_soft_logo)*/
						.into(ivlogo);
				Picasso.with(LoginActivity.this).load(PetroSoftData.imgPath)/*
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)*/
						.into(iconlogo);
			}
			else{
				ivlogo.setImageResource(R.drawable.petro_soft_logo);
				iconlogo.setImageResource(R.drawable.petro_soft_logo);
			}

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		/*SharedPreferences sp1 = getSharedPreferences("SetupPref" ,Context.MODE_PRIVATE);
		String diayn  = sp1.getString("Dialog","NoDialog");
		if(diayn.equalsIgnoreCase("YesDialog")) {*/
			callforplayStore();
		//}

	}

	private void callforplayStore() {
		String PlayStoreVersion = null;
		String MyAppVersion = null;
		if(CommonUtilities.isInternetAvailable(parent)) {
			try {
				MyAppVersion = (getPackageManager().getPackageInfo(getPackageName(), 0).versionName);

				Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id="//com.stavigilmonitoring
						+ "com.vritti.petrosoft").get();
				String AllStr = doc.text();
				String parts[] = AllStr.split("Current Version");
				String newparts[] = parts[1].split("Requires Android");
				PlayStoreVersion = newparts[0].trim();
				if(!MyAppVersion.equals(PlayStoreVersion)){
					if(dialogopen.equalsIgnoreCase("no")) {

						SharedPreferences LoginPref = getApplicationContext()
								.getSharedPreferences("SetupPref",Context.MODE_PRIVATE); // 0 - for private mode
						SharedPreferences.Editor edtcv = LoginPref.edit();
						edtcv.putString("Dialog", "NoDialog");
						edtcv.commit();

						showUpdateDialog(PlayStoreVersion);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (PackageManager.NameNotFoundException e) {
				e.printStackTrace();
			}catch (NullPointerException e){
				e.printStackTrace();
			}catch (Exception e){
				e.printStackTrace();
			}
		}
	}

    private void showUpdateDialog(String PSVersion) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New Update Available");
            builder.setMessage("New Petrosoft version " + PSVersion + " is on Playstore, Please update it to access new features of it."
                    /*"(Note: In playstore 'OPEN' button is visible instead of 'UPDATE', Uninstall and Install app)"*/);

            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                            ("market://details?id=com.vritti.petrosoft")));
                    dialogopen = "no";
                    dialog.dismiss();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //background.start();
                    dialogopen = "no";
                    dialog.dismiss();
                }
            });

            builder.setCancelable(false);

            dialog = builder.show();
            dialogopen = "yes";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	private void setListeners() {
		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (validate()) {
					PetroSoftData.username = edittext_username.getText()
							.toString();
					PetroSoftData.Cashier_name = edittext_username.getText()
							.toString();
					password = edittext_password.getText().toString();
					if (loginSuccessful()) {
						startActivity(new Intent(parent, MainActivity.class));
						finish();
					}
				} else {
					Toast.makeText(parent,
							"Please fill userid & password first..",
							Toast.LENGTH_LONG).show();
				}
			}
		});

		imgLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					PetroSoftData.username = null;
					PetroSoftData.Cashier_name = null;
					PetroSoftData.password= null;
					edittext_username.setText("");
					edittext_password.setText("");
					password = "";
				SharedPreferences LoginPref = getApplicationContext()
						.getSharedPreferences("LoginPref",Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor edtcv = LoginPref.edit();
				edtcv.putString("LoginUserName", PetroSoftData.username);
				edtcv.putString("LoginPassword", PetroSoftData.password);
				edtcv.commit();
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		});

		updateButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (CommonUtilities.isInternetAvailable(parent)) {
					new UpdateItemRateMaster().execute();
				} else {
					Toast.makeText(parent, "No internet connection found..",Toast.LENGTH_LONG).show();
				}
			}
		});
		
		edittext_username.setOnTouchListener(new View.OnTouchListener(){
			   @Override
			   public boolean onTouch(View v, MotionEvent event){
			      edittext_username.showDropDown();
			      return false;
			   }
			});
	}

	protected boolean loginSuccessful() {
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();

		Cursor cursor = db.rawQuery(
				"Select pwd,Acno,usergr from CashierMaster where Name='"+PetroSoftData.username+"'",null);

		if (cursor.getCount() == 0) {
			Toast.makeText(parent, "Username not found..", Toast.LENGTH_SHORT)
					.show();
		} else {
			cursor.moveToFirst();
			String pwd = cursor.getString(0);
			if ((password.equals(pwd))&&(!(pwd.equals(null)))&&(!(pwd.equals("")))) {
				PetroSoftData.username = edittext_username.getText()
						.toString();
				PetroSoftData.password = edittext_password.getText()
						.toString();
				//PetroSoftData.isOperator = cursor.getString(2);
				SharedPreferences LoginPref = getApplicationContext()
						.getSharedPreferences("LoginPref",Context.MODE_PRIVATE); // 0 - for private mode
				SharedPreferences.Editor edtcv = LoginPref.edit();
				edtcv.putString("LoginUserName", PetroSoftData.username);
				edtcv.putString("LoginPassword", PetroSoftData.password);
				//edtcv.putString("isOperator",PetroSoftData.isOperator);
				edtcv.commit();
				PetroSoftData.Cashier_acno = cursor.getString(1);
				PetroSoftData.isOperator = cursor.getString(2);
				return true;
			} else {
				Toast.makeText(parent, "Incorrect Password..",
						Toast.LENGTH_SHORT).show();
			}
		}

		return false;
	}

	private boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	class UpdateItemRateMaster extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		String exceptionString = "ok";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("Updating database...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {
				// String fullURL = getResources().getString(R.string.url);
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_ITEM_RATE_MASTER);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_ITEM_RATE_MASTER, envelope);
				Log.e("UpdateDatabase", "7 : ");
				SoapObject response = (SoapObject) envelope.bodyIn;
				Log.e("UpdateDatabase", "8 : ");
				SoapObject GetItemRateMasterResult = (SoapObject) response
						.getProperty(0);
				SoapObject NewDataSet = (SoapObject) GetItemRateMasterResult
						.getProperty(0);
				CommonUtilities.clearTable(parent, "Item");
				for (int i = 0; i < NewDataSet.getPropertyCount(); i++) {
					SoapObject Table = (SoapObject) NewDataSet.getProperty(i);
					databaseHelper.AddItems(new ItemHelper(Table
							.getProperty("item_desc").toString().trim(), Table
							.getProperty("item_code").toString().trim(), Table
							.getProperty("pl_rate").toString().trim(), Table
							.getProperty("validfrom").toString().trim(), Table
							.getProperty("item_group").toString().trim(),Table
							.getProperty("gst_code").toString().trim()));
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
						"Item Rate Master Sync Failed. Please try after some time..",
						Toast.LENGTH_LONG).show();
				//loginButton.setClickable(false);

			} else {
				Toast.makeText(parent, "ItemRateMaster Sync Successful..",
						Toast.LENGTH_LONG).show();
				//loginButton.setClickable(true);
				new UpdateFirmDetails().execute();

			}
		}
	}

	class UpdateFirmDetails extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		String exceptionString = "ok";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("Updating database...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				// String fullURL = getResources().getString(R.string.url);
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_FIRM_NAME_ADD);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_FIRM_NAME_ADD, envelope);

				SoapObject response = (SoapObject) envelope.bodyIn;

				SoapObject getFirmResult = (SoapObject) response.getProperty(0);
				SoapObject newDataSet = (SoapObject) getFirmResult
						.getProperty(0);
				CommonUtilities.clearTable(parent, "FirmDetails");
				for (int i = 0; i < newDataSet.getPropertyCount(); i++) {
					SoapObject table = (SoapObject) newDataSet.getProperty(i);
					databaseHelper.AddFirmDetails(table.getProperty("firmname")
							.toString().trim(), table.getProperty("add1")
							.toString().trim(), table.getProperty("add2")
							.toString().trim(), table.getProperty("add3")
							.toString().trim(), table.getProperty("phone")
							.toString().trim(), table.getProperty("mobile")
							.toString().trim(), table.getProperty("email_id")
							.toString().trim(), table.getProperty("gst_no")
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
			// if (exceptionString == "error") {
			// Toast.makeText(parent,
			// "User Data Sync Failed. Please try after some time..",
			// Toast.LENGTH_LONG).show();
			// } else {
			// Toast.makeText(parent, "Firm Details Sync Successful..",
			// Toast.LENGTH_LONG).show();
			// }

			if (exceptionString == "error") {
				Toast.makeText(parent,
						"Firm Details Sync Failed. Please try after some time..",
						Toast.LENGTH_LONG).show();
			//	new UpdateCashierDetails().execute();
			} else {
				Toast.makeText(parent, "Firm Details Sync Successful..",
						Toast.LENGTH_LONG).show();
				new UpdateCashierDetails().execute();
			}
		}
	}

	class UpdateCashierDetails extends AsyncTask<Void, Void, Void> {
		ProgressDialog progressDialog;
		String exceptionString = "ok";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("Updating database...");
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {
			try {

				// String fullURL = getResources().getString(R.string.url);
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_CASHIER);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_CASHIER, envelope);

				SoapObject response = (SoapObject) envelope.bodyIn;

				SoapObject getCashierResult = (SoapObject) response.getProperty(0);
				SoapObject newDataSet = (SoapObject) getCashierResult
						.getProperty(0);
				CommonUtilities.clearTable(parent, "CashierMaster");
				for (int i = 0; i < newDataSet.getPropertyCount(); i++) {
					SoapObject table = (SoapObject) newDataSet.getProperty(i);
					databaseHelper.AddCashier(table.getProperty("acno")
							.toString().trim(), table.getProperty("name")
							.toString().trim(),table.getProperty("pwd")
							.toString().trim(),table.getProperty("usergr")
							.toString().trim(), table.getProperty("actype")
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
				Toast.makeText(parent, "CashierMaster Sync Successful..",
						Toast.LENGTH_LONG).show();
				updateCustomerSpinner();

			}
		}
	}
}