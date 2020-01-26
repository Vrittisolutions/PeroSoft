package com.vritti.petrosoft;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ndeftools.Message;
import org.ndeftools.util.activity.NfcReaderActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;

import java.io.File;

public class CouponSale extends NfcReaderActivity {

	private Context parent;
	private ProgressDialog progressDialog;
	private LinearLayout baseLayout;
	ImageView ivlogo;

	private String responseString;
	// private boolean nextScreen = false;
	private EditText rfidNumberEdittext;

	Button submit;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_coupon);

		baseLayout = (LinearLayout) findViewById(R.id.linear_sales_rootlayout);
		submit = (Button) findViewById(R.id.btnSubmit);

		Typeface contentTypeface = Typeface.createFromAsset(getAssets(),
				"font/BOOKOS.TTF");
		CommonUtilities.setFont(parent, baseLayout, contentTypeface);
		initialize();

		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (rfidNumberEdittext.getText().toString().trim()
						.equalsIgnoreCase("")
						&& rfidNumberEdittext.getText().toString().trim() == null) {
					Toast.makeText(parent, "Please Enter Coupon Code.",
							Toast.LENGTH_LONG).show();
				} else {
					new GetCustDetails().execute();
				}

			}
		});

		// progressDialog = new ProgressDialog(parent);
		// progressDialog.setMessage("Detecting...");
		// progressDialog.setCancelable(true);
		// progressDialog.show();
		// progressDialog.setOnCancelListener(new OnCancelListener() {
		//
		// public void onCancel(DialogInterface dialog) {
		// // TODO Auto-generated method stub
		// finish();
		// }
		// });
		// setDetecting(true);
	}

	private void initialize() {
		parent = CouponSale.this;
		rfidNumberEdittext = (EditText) findViewById(R.id.edRFIDNo);
		ivlogo = (ImageView) findViewById(R.id.iconlogo);

		PetroSoftData.cust_code = null;
		PetroSoftData.veh_no = null;
		PetroSoftData.rfid_card = null;
		PetroSoftData.name = null;
		if(PetroSoftData.imgPath!=null){
			Picasso.with(CouponSale.this).load(PetroSoftData.imgPath)/*
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)*/
					.into(ivlogo);
		}
		else{
			ivlogo.setImageResource(R.drawable.petro_soft_logo);
		}
	}

	class GetCustDetails extends AsyncTask<Void, Void, Void> {
		String exceptionString = "ok";

		@Override
		protected Void doInBackground(Void... params) {

			try {

				// String fullURL = getResources().getString(R.string.url);
				String fullURL = PetroSoftData.URL;
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_CUST_DETAILS_bycoupon);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;
				// adding parameters
				request.addProperty("coupon", rfidNumberEdittext.getText()
						.toString());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_CUST_DETAILS_bycoupon,
						envelope);
				SoapObject response = (SoapObject) envelope.bodyIn;

				SoapObject GetCustDetailsResponse = (SoapObject) response
						.getProperty(0);
				SoapObject GetCustDetailsResult = (SoapObject) GetCustDetailsResponse
						.getProperty(0);

				SoapObject Table = (SoapObject) GetCustDetailsResult
						.getProperty(0);

				
				PetroSoftData.cust_code = Table.getProperty("cust_code")
						.toString().trim();
				PetroSoftData.veh_no = Table.getProperty("veh_no").toString()
						.trim();

				PetroSoftData.Coupon = rfidNumberEdittext.getText().toString()
						.trim();

				PetroSoftData.rfid_card = " ";
				// Table.getProperty("")
				// .toString().trim();
				PetroSoftData.name = Table.getProperty("name").toString()
						.trim();
				PetroSoftData.item_code = Table.getProperty("item_code").toString()
						.trim();
				// }
			} catch (Exception e) {
				exceptionString = "error";
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("Processing...");
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected void onPostExecute(Void result) {
			progressDialog.dismiss();
			
			if (exceptionString.equalsIgnoreCase("error")) {
				Toast.makeText(parent, "Coupon already used or invalid coupon",
						Toast.LENGTH_LONG).show();
			}
			// else if (exceptionString.equalsIgnoreCase("Invalid Coupon")) {
			// Toast.makeText(parent, "Invalid Coupon", Toast.LENGTH_LONG)
			// .show();
			// }
			// if (PetroSoftData.cust_code == null) {
			// Toast.makeText(parent, "Customer details not available..",
			// Toast.LENGTH_LONG).show();
			// PetroSoftData.cust_code = null;
			// PetroSoftData.veh_no = null;
			// PetroSoftData.rfid_card = null;
			// PetroSoftData.name = null;
			//
			// finish();
			// }
			else {
				startActivity(new Intent(parent, BillDetails.class));
				finish();
			}
		}
	}

	@Override
	protected void readNdefUID(String UID) {
		((EditText) findViewById(R.id.edRFIDNo)).setText(UID);
		progressDialog.dismiss();
		if ((UID != null)) {
			if (!UID.trim().equalsIgnoreCase(""))
				new GetCustDetails().execute();
		}

	}

	@Override
	protected void readNdefMessage(Message message) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readEmptyNdefMessage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void readNonNdefMessage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNfcStateEnabled() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNfcStateDisabled() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNfcStateChange(boolean enabled) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onNfcFeatureNotFound() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onTagLost() {
		// TODO Auto-generated method stub

	}
}
