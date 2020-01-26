package com.vritti.petrosoft;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.ndeftools.Message;
import org.ndeftools.util.activity.NfcReaderActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;

public class CardRegistrationActivity extends NfcReaderActivity {
    private Context parent;
    private Button buttonRegister, buttonfetchveh;
    private EditText edtRfidNo, et_for_custname;
    private Spinner spVehNo;
    private AutoCompleteTextView spCustName;
    private String vehNo, custCode, rfidNo, custName, et_custName = "";
    private ProgressDialog progressDialog;
    ImageView ivlogo;
    TextView txtscan;

    private ArrayList<String> customerNameList, customerCodeList, vehNoList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
       // setTheme(android.R.style.Theme_Holo);
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_card_registration);
        initViews();

        //apply validation of RFID/QRCode
        //if QR code then open camera and detect QRCode
        //after detecing get number and set to it

        if(PetroSoftData.ScanMode_RFID_QRCODE != null){
            if (PetroSoftData.ScanMode_RFID_QRCODE.equalsIgnoreCase("RFID")) {
                scanCard();
            } else if (PetroSoftData.ScanMode_RFID_QRCODE.equalsIgnoreCase("QR CODE")) {
                //scan QR code method
                txtscan.setText("Scan QR Code");
                edtRfidNo.setText("Scan QR Code");
                scanQR();
            }
        }else {
            PetroSoftData.ScanMode_RFID_QRCODE = "RFID";
        }

        updateCustomerSpinner();
        setListeners();
        //checkautoComplete();
    }

    private void checkautoComplete() {
        // TODO Auto-generated method stub
        custName = spCustName.getText().toString();
        DatabaseHelper db1 = new DatabaseHelper(parent);
        SQLiteDatabase db = db1.getWritableDatabase();
        Cursor cursor;
        if (spCustName != null && custName != "") {
            cursor = db.rawQuery("Select AcNo from CashierMaster where Name = '" + custName + "'", null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                custCode = cursor.getString(0);
            }
            cursor.close();
            db.close();
            db1.close();
            //custName = spCustName.getText().toString();
            if (CommonUtilities.isInternetAvailable(parent)) {
                new UpdateVehicleMaster().execute();
            } else {
                Toast.makeText(parent, "Internet connection not available..", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initViews() {
        parent = CardRegistrationActivity.this;

        txtscan = (TextView) findViewById(R.id.txtscan);
        buttonRegister = (Button) findViewById(R.id.button_cardreg_register);
        buttonfetchveh = (Button) findViewById(R.id.button_fetch_veh);
        edtRfidNo = (EditText) findViewById(R.id.edittext_cardreg_rfid);
        //et_for_custname = (EditText) findViewById(R.id.et_for_custname);
        spCustName = (AutoCompleteTextView) findViewById(R.id.spinner_cardreg_custname);
        spVehNo = (Spinner) findViewById(R.id.spinner_cardreg_vehicle);
        ivlogo = (ImageView) findViewById(R.id.iconlogo);

        customerNameList = new ArrayList<String>();
        customerCodeList = new ArrayList<String>();

        vehNoList = new ArrayList<String>();
        if (PetroSoftData.imgPath != null) {
            Picasso.with(parent).load(PetroSoftData.imgPath)/*
					.placeholder(R.drawable.petro_soft_logo)
					.error(R.drawable.petro_soft_logo)*/
                    .into(ivlogo);
        } else {
            ivlogo.setImageResource(R.drawable.petro_soft_logo);
        }
    }

    private void scanCard() {
        progressDialog = new ProgressDialog(parent);
        progressDialog.setMessage("Detecting...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        progressDialog.setOnCancelListener(new OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                finish();
            }
        });
        setDetecting(true);
    }

    private void updateCustomerSpinner() {
        customerNameList.clear();
        customerCodeList.clear();
        DatabaseHelper db1 = new DatabaseHelper(parent);
        SQLiteDatabase db = db1.getWritableDatabase();
        Cursor cursor;
//if (et_custName==null||et_custName==""){
        cursor = db.rawQuery(
                "Select AcNo,Name from CashierMaster where AcType = 'Cust' order by Name ASC", null);
/*} else{
		 cursor = db.rawQuery(
				"Select AcNo,Name from CustomerMaster where Name like %"+et_custName+"% order by Name ASC", null);
}*/
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                customerCodeList.add(cursor.getString(0));
                customerNameList.add(cursor.getString(1));
            } while (cursor.moveToNext());

            cursor.close();
            db.close();
            db1.close();
        }

        custCode = customerCodeList.get(0);
       /* ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
                android.R.layout.simple_list_item_1, customerNameList);*/
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
                android.R.layout.simple_list_item_1, customerNameList);
        //adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);

        spCustName.setThreshold(1);
        spCustName.setAdapter(adapter1);

    }

    private void setListeners() {
        buttonRegister.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CommonUtilities.isInternetAvailable(parent)) {
                    rfidNo = edtRfidNo.getText().toString().trim();
                    vehNo = spVehNo.getSelectedItem().toString();
                    new RegisterCard().execute();
                } else {
                    Toast.makeText(parent,
                            "Internet connection not available..",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        buttonfetchveh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                checkautoComplete();
            }
        });


        spVehNo.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                vehNo = spVehNo.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    class RegisterCard extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("Saving Data on Server...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // String fullURL = getResources().getString(R.string.url);
                String fullURL = PetroSoftData.URL;
                SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
                        PetroSoftData.METHOD_REGISTER_CARD);
                PropertyInfo propInfo = new PropertyInfo();
                propInfo.type = PropertyInfo.STRING_CLASS;
                // adding parameters
                request.addProperty("cust_code", custCode);
                request.addProperty("veh_no", vehNo);
                request.addProperty("rfid_card", rfidNo);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        fullURL);
                androidHttpTransport.call(PetroSoftData.NAMESPACE
                        + PetroSoftData.METHOD_REGISTER_CARD, envelope);
                if (envelope.bodyIn instanceof SoapFault) {
                    String str = ((SoapFault) envelope.bodyIn).faultstring;
                    responseString = "error";
                    Log.i("SoapFault", str);
                } else {
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
        protected void onPostExecute(Void result) {

            super.onPostExecute(result);
            progressDialog.dismiss();
            Toast.makeText(parent, responseString, Toast.LENGTH_LONG).show();
        }
    }

    class UpdateVehicleMaster extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog;
        String exceptionString = "ok";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(parent);
            progressDialog.setMessage("getting Vehicle List...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                // String fullURL = getResources().getString(R.string.url);
                String fullURL = PetroSoftData.URL;

                SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
                        PetroSoftData.METHOD_GET_VEHICLE_LIST);
                PropertyInfo propInfo = new PropertyInfo();
                propInfo.type = PropertyInfo.STRING_CLASS;
                // adding parameters
                request.addProperty("Cust_code", custCode.toString());

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
                        SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport = new HttpTransportSE(
                        fullURL);
                androidHttpTransport.call(PetroSoftData.NAMESPACE
                        + PetroSoftData.METHOD_GET_VEHICLE_LIST, envelope);

                SoapObject response = (SoapObject) envelope.bodyIn;


                SoapObject getVehicleResult = (SoapObject) response
                        .getProperty(0);
                SoapObject newDataSet = (SoapObject) getVehicleResult
                        .getProperty(0);

                vehNoList.clear();
                // CommonUtilities.clearTable(parent, "VehicleMaster");
                for (int i = 0; i < newDataSet.getPropertyCount(); i++) {
                    SoapObject table = (SoapObject) newDataSet.getProperty(i);
                    vehNoList
                            .add(table.getProperty("veh_no").toString().trim());
                    // databaseHelper.AddVehicleMaster(
                    // table.getProperty("cust_code").toString().trim(),
                    // table.getProperty("veh_no").toString().trim(),
                    // table.getProperty("rfid_card").toString().trim());
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
                        "Data Sync Failed. Please try after some time..",
                        Toast.LENGTH_LONG).show();
            } else {
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(parent,
                        android.R.layout.simple_list_item_1, vehNoList);
                adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                spVehNo.setAdapter(adapter);
                if (vehNoList.size() > 0)
                    vehNo = vehNoList.get(0).toString();
                else
                    vehNo = null;
            }
        }
    }

    @Override
    protected void readNdefUID(String UID) {
        edtRfidNo.setText(UID);
        progressDialog.dismiss();

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

/*	@Override
	protected void readNdefUID(String UID) {
		String sUID = UID.toString();
		edtRfidNo.setText(sUID);
		progressDialog.dismiss();
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

	@Override
	protected void readNdefUID(String UID) {
		// TODO Auto-generated method stub
		
	}*/

    //on ActivityResult method
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                //get the extras that are returned from the intent
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                Toast toast;
                if (contents.equalsIgnoreCase(" ") || contents.equalsIgnoreCase(null)) {

                    	toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_SHORT);
                } else {
                    edtRfidNo.setText(contents.trim());

                    	toast =  Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_SHORT);
                }
                //toast.show();
            }
        }else {
            if(result != null) {
                if(result.getContents() == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "QR Code Scanned Successfully "  /*+result.getContents()*/, Toast.LENGTH_LONG).show();
                    String scannedvalue = result.getContents();
                    edtRfidNo.setText(scannedvalue.trim());
                }
            } else {
              //  Toast.makeText(parent, "result", Toast.LENGTH_SHORT).show();
               // super.onActivityResult(requestCode, resultCode, intent);
            }
        }
    }

    //product qr code mode
    public void scanQR() {
        try {
            //start the scanning activity from the com.google.zxing.client.android.SCAN intent
            Intent intent = new Intent(PetroSoftData.ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            //on catch, show the download dialog
            new IntentIntegrator(this).initiateScan();

           // Toast.makeText(parent, "test", Toast.LENGTH_SHORT).show();
           // showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    //alert dialog for downloadDialog
    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        @SuppressLint("RestrictedApi") AlertDialog.Builder downloadDialog = new AlertDialog.Builder(new ContextThemeWrapper(act, R.style.myDialog));
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialogInterface, int i) {
                new IntentIntegrator(act).initiateScan();
                //Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
               // Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                   // act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }


}