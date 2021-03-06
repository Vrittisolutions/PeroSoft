package com.vritti.petrosoft;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyboardShortcutGroup;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.SearchEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;

import java.io.File;
import java.util.List;

public class MainActivity extends Activity implements OnClickListener {
	private Context parent;
	private LinearLayout baseLayout;
	private Button btnSale, btnCardReg, btnVehReg,btncoupon, btnDSR,btnSetup, btnRdmPts;
	private DatabaseHelper databaseHelper;
	private ImageView ivlogo, iconlogo;
	EditText edt_password_1,edt_username_1;
	Button  btn_submit_1;
	AppCompatCheckBox checkBox_show_1;
	AlertDialog alertDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		getSetupSharedPref();
		initViews();
		setListeners();
	}

	private void getSetupSharedPref() {
		SharedPreferences sp = getSharedPreferences("SetupPref" ,Context.MODE_PRIVATE);
		String sps  = sp.getString("SetupPrinterSetting",null);
		String snrqac  = sp.getString("SetupNeedRQAC", "No");
		String snveh  = sp.getString("SetupNeedVehKm", "No");
		String sors  = sp.getString("SetupOnlyRFIDSale", "No");
		String sarp  = sp.getString("SetupRewardsPoints", "No");
		String sdi  = sp.getString("SetupDefaultItem", null);
		//get scanm mode value RFID/QR
        String selscnmode = sp.getString("SetupScanMode", null);

		PetroSoftData.DefaultItem = sdi;
		PetroSoftData.RemoveRQAC = snrqac;
		PetroSoftData.NeedVehKm = snveh;
		PetroSoftData.OnlyRFIDSale = sors;
		PetroSoftData.AllowRewardsPoints = sarp;
		PetroSoftData.PRINTERSETTING =sps;
		// set scanmode value
        PetroSoftData.ScanMode_RFID_QRCODE = selscnmode;
	}

	private void initViews() {
		parent = MainActivity.this;
		databaseHelper = new DatabaseHelper(parent);
		//PetroSoftData.isOperator = CommonUtilities.getIsOperator(parent);

		baseLayout = (LinearLayout) findViewById(R.id.linear_main_base);
		btnSale = (Button) findViewById(R.id.btnSale);
		btnDSR = (Button) findViewById(R.id.btnDSR);
		btnCardReg = (Button) findViewById(R.id.btnCardReg);
		btnVehReg = (Button) findViewById(R.id.btnVehReg);
		btncoupon = (Button) findViewById(R.id.btncoupon);
		btnSetup = (Button) findViewById(R.id.btnSetup);
		btnRdmPts = (Button)findViewById(R.id.btnRdmPts);
		ivlogo = (ImageView) findViewById(R.id.ivlogo);
        iconlogo = (ImageView) findViewById(R.id.iconlogo);

        if(PetroSoftData.isOperator != null){
            if (PetroSoftData.isOperator.equals("Admin")){
                btnSale.setClickable(true);
                btnDSR.setClickable(true);
                btnCardReg.setClickable(true);
                btnVehReg.setClickable(true);
                btncoupon.setClickable(true);
                btnSetup.setClickable(true);
                btnRdmPts.setClickable(true);
            } else if (!(PetroSoftData.isOperator.equals("Admin"))){
                btnSale.setClickable(true);
                btnDSR.setClickable(true);
                btnCardReg.setBackgroundResource(R.drawable.disablebuttonback);
                btnCardReg.setClickable(false);
                btnVehReg.setBackgroundResource(R.drawable.disablebuttonback);
                btnVehReg.setClickable(false);
                btncoupon.setBackgroundResource(R.drawable.disablebuttonback);
                btncoupon.setClickable(false);
                btnSetup.setBackgroundResource(R.drawable.disablebuttonback);
                btnSetup.setClickable(false);
                btnRdmPts.setBackgroundResource(R.drawable.disablebuttonback);
                btnRdmPts.setClickable(false);
            }
        }else {
           PetroSoftData.isOperator = "Admin";
            if (PetroSoftData.isOperator.equals("Admin")){
                btnSale.setClickable(true);
                btnDSR.setClickable(true);
                btnCardReg.setClickable(true);
                btnVehReg.setClickable(true);
                btncoupon.setClickable(true);
                btnSetup.setClickable(true);
                btnRdmPts.setClickable(true);
            } else if (!(PetroSoftData.isOperator.equals("Admin"))){
                btnSale.setClickable(true);
                btnDSR.setClickable(true);
                btnCardReg.setBackgroundResource(R.drawable.disablebuttonback);
                btnCardReg.setClickable(false);
                btnVehReg.setBackgroundResource(R.drawable.disablebuttonback);
                btnVehReg.setClickable(false);
                btncoupon.setBackgroundResource(R.drawable.disablebuttonback);
                btncoupon.setClickable(false);
                btnSetup.setBackgroundResource(R.drawable.disablebuttonback);
                btnSetup.setClickable(false);
                btnRdmPts.setBackgroundResource(R.drawable.disablebuttonback);
                btnRdmPts.setClickable(false);
            }
        }


		if(PetroSoftData.imgPath!=null){
			Picasso.with(MainActivity.this).load(PetroSoftData.imgPath)
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)
					.into(ivlogo);
            Picasso.with(MainActivity.this).load(PetroSoftData.imgPath)
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)
					.into(iconlogo);
		}
		else{
			ivlogo.setImageResource(R.drawable.petro_soft_logo);
			iconlogo.setImageResource(R.drawable.petro_soft_logo);
		}

		Typeface font = Typeface
				.createFromAsset(getAssets(), "font/BOOKOS.TTF");
		CommonUtilities.setFont(parent, baseLayout, font);

		if (PetroSoftData.OnlyRFIDSale.equalsIgnoreCase("Yes")){
			btnSale.setVisibility(View.VISIBLE);
			btnDSR.setVisibility(View.GONE);
			btnCardReg.setVisibility(View.VISIBLE);
			btncoupon.setVisibility(View.GONE);
			btnVehReg.setVisibility(View.VISIBLE);
			btnSetup.setVisibility(View.VISIBLE);
			btnRdmPts.setVisibility(View.VISIBLE);
		}

		/*//add scan mode validation RFID/QR
		if(PetroSoftData.ScanMode_RFID_QRCODE == null){

		}else if(PetroSoftData.ScanMode_RFID_QRCODE.equalsIgnoreCase("QR CODE")){
            btnSale.setText("QR Code Sale");
            btnCardReg.setText("Scan QR Code");
        }*/

	}

	private void setListeners() {
		
		if (PetroSoftData.isOperator.equals("Admin")){
			btnSale.setOnClickListener(this);
			btnCardReg.setOnClickListener(this);
			btnVehReg.setOnClickListener(this);
			btncoupon.setOnClickListener(this);
			btnSetup.setOnClickListener(this);
			btnRdmPts.setOnClickListener(this);
			btnDSR.setOnClickListener(this);
		}else if (!(PetroSoftData.isOperator.equals("Admin"))){
			btnSale.setOnClickListener(this);
			btnCardReg.setOnClickListener(null);
			btnVehReg.setOnClickListener(null);
			btncoupon.setOnClickListener(null);
			btnSetup.setOnClickListener(null);
			btnRdmPts.setOnClickListener(null);
			btnDSR.setOnClickListener(this);
		}
	}

	private void startBluetooth() {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		mBluetoothAdapter.enable();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSetup:
			//Log.e("password_a" ,PetroSoftData.password);
			//();/*
			startActivity(new Intent(parent, SetupActivity.class));
			//startActivity(new Intent(parent, Printtest.class));
			finish();
			break;
		case R.id.btnSale:
			Intent ibtnsale = new Intent(parent,SaleActivity.class);
			ibtnsale.putExtra("CallFrom","CardSaleTab");
			startActivity(ibtnsale);
			//startActivity(new Intent(parent, SaleActivity.class));
			//startActivity(new Intent(parent, BillDetails.class));
			//finish();
			break;
		case R.id.btnDSR:
			startActivity(new Intent(parent, DSRActivity.class));
			break;
		case R.id.btnCardReg:
			if (CommonUtilities.dbTableHasRows(parent,
					"SELECT * FROM CashierMaster where AcType='Cust'")) {
				startActivity(new Intent(parent, CardRegistrationActivity.class));
			} else {
				Toast.makeText(parent, "Please Register vehicle First..",
						Toast.LENGTH_LONG).show();
			}
			//finish();
			break;

		case R.id.btnVehReg:
			startActivity(new Intent(parent,
					VehicleRegistrationActivity.class));
			break;
			
		case R.id.btncoupon:
			//Toast.makeText(parent, "This Function Is Temporary Disabled", Toast.LENGTH_LONG).show();
			Intent ibtnCoupon = new Intent(parent,CouponSaleCustActivity.class);
			ibtnCoupon.putExtra("CallFrom","CreditSaleTab");
			startActivity(ibtnCoupon);
			//startActivity(new Intent(parent, CouponSaleCustActivity.class));
			break;

			case R.id.btnRdmPts:
				Intent iRdmpts = new Intent(parent,SaleActivity.class);
				iRdmpts.putExtra("CallFrom","RedeemPointsTab");
				startActivity(iRdmpts);
				break;
			
		}
	}

	private void PasswordopenDialog() {
		LayoutInflater inflater = LayoutInflater.from(parent);
		View subView1 = inflater.inflate(R.layout.password_dialog, null);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(subView1);


		edt_password_1 = (EditText) subView1.findViewById(R.id.edt_password_1);
		edt_username_1 = (EditText) subView1.findViewById(R.id.edt_username_1);
		btn_submit_1 = (Button) subView1.findViewById(R.id.btn_submit_1);
		//checkBox_show_1 = (AppCompatCheckBox) subView1.findViewById(R.id.checkbox_show_1);
		edt_username_1.setText(PetroSoftData.username);
		edt_username_1.setFocusable(false);
		edt_username_1.setClickable(false);
		edt_username_1.setFocusableInTouchMode(false);
		try {

			btn_submit_1.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					String uname = edt_username_1.getText().toString();
					String pwd = edt_password_1.getText().toString();

					System.out.println("password_a" + PetroSoftData.password);
					System.out.println("password_a" + PetroSoftData.username);

					if (pwd.equals(PetroSoftData.password) && uname.equals(PetroSoftData.username)) {
						Toast.makeText(parent, "Login Successfully", Toast.LENGTH_SHORT).show();
						alertDialog.dismiss();
						// Toast.makeText(parent, "WIP", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(parent, SetupActivity.class));
						//startActivity(new Intent(parent, Printtest.class));
						//finish();
					} else {
						Toast.makeText(parent, "Invalid Password", Toast.LENGTH_SHORT).show();


					}
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
		alertDialog = builder.create();

		alertDialog.show();

		/*checkBox_show_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				int start, end;
				Log.i("inside checkbox chnge", "" + isChecked);
				if (!isChecked) {
					start = edt_password_1.getSelectionStart();
					end = edt_password_1.getSelectionEnd();
					edt_password_1.setTransformationMethod(new PasswordTransformationMethod());
					edt_password_1.setSelection(start, end);
					checkBox_show_1.setText("Show Password");
				} else {
					start = edt_password_1.getSelectionStart();
					end = edt_password_1.getSelectionEnd();
					edt_password_1.setTransformationMethod(null);
					edt_password_1.setSelection(start, end);
					checkBox_show_1.setText("Hide Password");
				}
			}

		});*/
	}

	private void updateCustomerMaster() {
		if (CommonUtilities.isInternetAvailable(parent))
			new UpdateCustomerMaster().execute();
		else
			Toast.makeText(
					parent,
					"Customer Master not updated..No internet connection found..",
					Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onSearchRequested(SearchEvent searchEvent) {
		return false;
	}

	@Nullable
	@Override
	public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int type) {
		return null;
	}


	class UpdateCustomerMaster extends AsyncTask<Void, Void, Void> {
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
				String fullURL = PetroSoftData.URL;
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_CUST_LIST);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_CUST_LIST, envelope);

				SoapObject response = (SoapObject) envelope.bodyIn;

				SoapObject getCustResult = (SoapObject) response.getProperty(0);
				SoapObject newDataSet = (SoapObject) getCustResult
						.getProperty(0);
				CommonUtilities.clearTable(parent, "CustomerMaster");
				for (int i = 0; i < newDataSet.getPropertyCount(); i++) {
					SoapObject table = (SoapObject) newDataSet.getProperty(i);
					/*databaseHelper.AddCustomerMaster(table.getProperty("acno")
							.toString().trim(), table.getProperty("name")
							.toString().trim(), table.getProperty("city")
							.toString().trim());*/
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
				Toast.makeText(parent, "Customer Data Sync Failed!!!!",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(parent, "Sync Successful..", Toast.LENGTH_LONG)
						.show();
				startActivity(new Intent(parent,
						VehicleRegistrationActivity.class));
			}
		}
	}
}