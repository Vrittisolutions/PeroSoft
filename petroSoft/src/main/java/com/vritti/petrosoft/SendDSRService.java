package com.vritti.petrosoft;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class SendDSRService extends Service {
    SendDSRBean sendDSRBean;
    private DatabaseHelper databaseHelper;
    List<SendDSRBean> list;
    static int flag = 0;

    @Override
    public IBinder onBind(Intent intent) {        return null;    }
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        list = new ArrayList<SendDSRBean>();
        databaseHelper = new DatabaseHelper(SendDSRService.this);
        getRowFromDatabase();

        return super.onStartCommand(intent, flags, startId);
    }
    
    private void getRowFromDatabase() {

        list.clear();
        DatabaseHelper db1 = new DatabaseHelper(SendDSRService.this);
        SQLiteDatabase db = db1.getWritableDatabase();
        
        int cnt = 2;
        do {
        	Cursor c1 = db.rawQuery("Select * from SendDSR"
                + " where isUploaded=?", new String[]{"No"});        
        	if (c1.getCount() > 0) {
        		c1.moveToFirst();
        		do {
	                sendDSRBean = new SendDSRBean();
	                sendDSRBean.setdate(c1.getString(c1.getColumnIndex("date")));
	                sendDSRBean.setshift(c1.getString(c1.getColumnIndex("shift")));
	                sendDSRBean.setcashier_cd(c1.getString(c1.getColumnIndex("cashier_cd")));
	                if (cnt==2){
	                	sendDSRBean.setxmlstr(c1.getString(c1.getColumnIndex("xml2")));
	                	sendDSRBean.setKey("Pump");
	                }else if (cnt==3){
	                	sendDSRBean.setxmlstr(c1.getString(c1.getColumnIndex("xml3")));
	                	sendDSRBean.setKey("Product");
	                }else if (cnt==4){
	                	sendDSRBean.setxmlstr(c1.getString(c1.getColumnIndex("xml4")));
	                	sendDSRBean.setKey("Rcpt");
	                }else if (cnt==5){
	                	sendDSRBean.setxmlstr(c1.getString(c1.getColumnIndex("xml5")));
	                	sendDSRBean.setKey("Exp");
	                }else if (cnt==6){
	                	sendDSRBean.setxmlstr(c1.getString(c1.getColumnIndex("xml6")));
	                	sendDSRBean.setKey("Bank");
	                }else if (cnt==7){
                        sendDSRBean.setxmlstr(c1.getString(c1.getColumnIndex("xml7")));
                        sendDSRBean.setKey("TmpRcpt");
                    }else if (cnt==8){
                        sendDSRBean.setxmlstr(c1.getString(c1.getColumnIndex("xml8")));
                        sendDSRBean.setKey("CCSwap");
                    }
	                list.add(sendDSRBean);
        		} while (c1.moveToNext());
        	} else {
            db.close();
            c1.close();            
        	}
        	cnt++;
        } while (cnt<=8);
        
        if (CommonUtilities.isInternetAvailable(SendDSRService.this)) {
        	if (list.size() > 0) {
        		String lcnt = String.valueOf(list.size()-1);
                for (int i = 0; i <= list.size(); i++) { 
                	if (i==list.size()){
                		if(flag != 0){

                     	CommonUtilities.clearTable(SendDSRService.this, "SendDSR");
                     	CommonUtilities.clearTable(SendDSRService.this, "PumpList");
                        PetroSoftData.PETRO_SALE = "0.00";
                        CommonUtilities.clearTable(SendDSRService.this, "ProdSaleTable");
                        PetroSoftData.PROD_SALE = "0.00";
                        CommonUtilities.clearTable(SendDSRService.this, "OtherRcptTable");
                    	PetroSoftData.OTHER_RCPT = "0.00";
                    	CommonUtilities.clearTable(SendDSRService.this, "OtherExpTable");
                    	PetroSoftData.OTHER_EXP = "0.00";
                    	CommonUtilities.clearTable(SendDSRService.this, "BankDepoTable");
                    	PetroSoftData.BANK_DEPO = "0.00";
                        CommonUtilities.clearTable(SendDSRService.this, "OtherAddRsTable");
                        PetroSoftData.OTHER_ADD_Rs = "0.00";
                        CommonUtilities.clearTable(SendDSRService.this, "SwapCardTable");
                        PetroSoftData.SWAP_CARD = "0.00";
                     	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                     	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                         startActivity(intent);
                         Toast.makeText(SendDSRService.this, "Saved all Successfully",
                                 Toast.LENGTH_LONG).show();
                		}else {
                			Toast.makeText(SendDSRService.this, "No data Available",
                                    Toast.LENGTH_LONG).show();
                		}
                    	
                    }else{
	                	String Value = String.valueOf(i);
                        Log.e("Value of i", Value);
	                    String date = list.get(i).getdate();
	                    String shift = list.get(i).getshift();
	                    String cashier_cd = list.get(i).getcashier_cd();
	                    String xmlstr = list.get(i).getxmlstr();
	                    String key = list.get(i).getKey();
	                    if (!(xmlstr.equalsIgnoreCase("no data"))){
		                    if (key.equalsIgnoreCase("Pump")){
		                    	flag=1;
		                    new SendDSRPump().execute(date, shift, cashier_cd, xmlstr, key,Value,lcnt);
		                    }else if (key.equalsIgnoreCase("Product")){
		                    	flag=1;
		                        new SendDSRProduct().execute(date, shift, cashier_cd, xmlstr, key,Value,lcnt);
		                    }else if (key.equalsIgnoreCase("Rcpt")){
		                    	flag=1;
		                        new SendDSRRcpt().execute(date, shift, cashier_cd, xmlstr, key,Value,lcnt);
		                    }else if (key.equalsIgnoreCase("Exp")){
		                    	flag=1;
		                        new SendDSRExp().execute(date, shift, cashier_cd, xmlstr, key,Value,lcnt);
		                    }else if (key.equalsIgnoreCase("Bank")){
		                    	flag=1;
		                        new SendDSRBank().execute(date, shift, cashier_cd, xmlstr, key,Value,lcnt);
		                    }else if (key.equalsIgnoreCase("TmpRcpt")){
                                flag=1;
                                new SendDSRTmpRcpt().execute(date, shift, cashier_cd, xmlstr, key,Value,lcnt);
                            }else if (key.equalsIgnoreCase("CCSwap")){
                                flag=1;
                                new SendDSRCCSwap().execute(date, shift, cashier_cd, xmlstr, key,Value,lcnt);
                            }
	                    }
                    }                    
                }
            }
        }
        
        /*PetroSoftData.Cashier_name =null;
    	PetroSoftData.Cashier_acno = null;

    	CommonUtilities.clearTable(SendDSRService.this, "SendDSR");
    	Intent intent = new Intent(getApplicationContext(), MainActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Toast.makeText(SendDSRService.this, "Saved all Successfully",
                Toast.LENGTH_LONG).show();*/
        
    }
    
    class SendDSRPump extends AsyncTask<String, Void, String> {
        // ProgressDialog progressDialog;
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                SoapObject request = new SoapObject(
                        PetroSoftData.NAMESPACE,
                        PetroSoftData.METHOD_SEND_DSR);
                PropertyInfo propInfo = new PropertyInfo();
                propInfo.type = PropertyInfo.STRING_CLASS;

                request.addProperty("date", params[0]);
                request.addProperty("shift", params[1]);
                request.addProperty("cashier_cd", params[2]);
                request.addProperty("xmlstr", params[3]);
                request.addProperty("key", params[4]);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(PetroSoftData.URL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_SEND_DSR, envelope);

                SoapObject response = (SoapObject) envelope.bodyIn;
                responseString = response.getProperty(0).toString() + "," + params[5] + "," + params[6]+ "," + params[2];
                Log.e("responseString", responseString);
                
            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

             if (responseString.equalsIgnoreCase("error")) {
                Toast.makeText(SendDSRService.this, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            }else {
                
            	String id[] = result.split(",");
            	//Toast.makeText(SendDSRService.this,result+"   "+ id[1], Toast.LENGTH_LONG).show();                
                DatabaseHelper db1 = new DatabaseHelper(SendDSRService.this);
                SQLiteDatabase db = db1.getWritableDatabase();
                ContentValues newValues = new ContentValues();
                newValues.put("xml2", "no data");
                flag = 1;

                db.update("SendDSR", newValues, "cashier_cd='"+PetroSoftData.Cashier_acno+"'", null);
                //db.execSQL("UPDATE SendDSR SET xml2 ='' WHERE id=1 ");
                CommonUtilities.clearTable(SendDSRService.this, "PumpList");
                PetroSoftData.PETRO_SALE = "0.00";
                     
                //finish(getApplicationContext());
            }
             
        }
    }

    class SendDSRProduct extends AsyncTask<String, Void, String> {
        // ProgressDialog progressDialog;
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                SoapObject request = new SoapObject(
                        PetroSoftData.NAMESPACE,
                        PetroSoftData.METHOD_SEND_DSR);
                PropertyInfo propInfo = new PropertyInfo();
                propInfo.type = PropertyInfo.STRING_CLASS;

                request.addProperty("date", params[0]);
                request.addProperty("shift", params[1]);
                request.addProperty("cashier_cd", params[2]);
                request.addProperty("xmlstr", params[3]);
                request.addProperty("key", params[4]);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(PetroSoftData.URL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_SEND_DSR, envelope);

                SoapObject response = (SoapObject) envelope.bodyIn;
                responseString = response.getProperty(0).toString() + "," + params[5] + "," + params[6]+ "," + params[2];
                Log.e("responseString", responseString);
                
            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

             if (responseString.equalsIgnoreCase("error")) {
                Toast.makeText(SendDSRService.this, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            }else {
                
            	String id[] = result.split(",");
            	//Toast.makeText(SendDSRService.this,result+"   "+ id[1], Toast.LENGTH_LONG).show();                
                DatabaseHelper db1 = new DatabaseHelper(SendDSRService.this);
                SQLiteDatabase db = db1.getWritableDatabase();ContentValues newValues = new ContentValues();
                newValues.put("xml3", "no data");
                flag = 2;
                db.update("SendDSR", newValues, "cashier_cd='"+PetroSoftData.Cashier_acno+"'", null);
            	CommonUtilities.clearTable(SendDSRService.this, "ProdSaleTable");
                PetroSoftData.PROD_SALE = "0.00";
                     
                //finish(getApplicationContext());
            }
             
        }
    }

    class SendDSRRcpt extends AsyncTask<String, Void, String> {
        // ProgressDialog progressDialog;
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                SoapObject request = new SoapObject(
                        PetroSoftData.NAMESPACE,
                        PetroSoftData.METHOD_SEND_DSR);
                PropertyInfo propInfo = new PropertyInfo();
                propInfo.type = PropertyInfo.STRING_CLASS;

                request.addProperty("date", params[0]);
                request.addProperty("shift", params[1]);
                request.addProperty("cashier_cd", params[2]);
                request.addProperty("xmlstr", params[3]);
                request.addProperty("key", params[4]);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(PetroSoftData.URL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_SEND_DSR, envelope);

                SoapObject response = (SoapObject) envelope.bodyIn;
                responseString = response.getProperty(0).toString() + "," + params[5] + "," + params[6]+ "," + params[2];
                Log.e("responseString", responseString);
                
            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

             if (responseString.equalsIgnoreCase("error")) {
                Toast.makeText(SendDSRService.this, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            }else {
                
            	String id[] = result.split(",");
            	//Toast.makeText(SendDSRService.this,result+"   "+ id[1], Toast.LENGTH_LONG).show();                
                DatabaseHelper db1 = new DatabaseHelper(SendDSRService.this);
                SQLiteDatabase db = db1.getWritableDatabase();ContentValues newValues = new ContentValues();
                newValues.put("xml4", "no data");
                flag = 3;

                db.update("SendDSR", newValues, "cashier_cd='"+PetroSoftData.Cashier_acno+"'", null);
            	CommonUtilities.clearTable(SendDSRService.this, "OtherRcptTable");
            	PetroSoftData.OTHER_RCPT = "0.00";
                     
                //finish(getApplicationContext());
            }
             //flag = 3;
        }
    }

    class SendDSRTmpRcpt extends AsyncTask<String, Void, String> {
        // ProgressDialog progressDialog;
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                SoapObject request = new SoapObject(
                        PetroSoftData.NAMESPACE,
                        PetroSoftData.METHOD_SEND_DSR);
                PropertyInfo propInfo = new PropertyInfo();
                propInfo.type = PropertyInfo.STRING_CLASS;

                request.addProperty("date", params[0]);
                request.addProperty("shift", params[1]);
                request.addProperty("cashier_cd", params[2]);
                request.addProperty("xmlstr", params[3]);
                request.addProperty("key", params[4]);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(PetroSoftData.URL);
                androidHttpTransport.call(PetroSoftData.NAMESPACE
                        + PetroSoftData.METHOD_SEND_DSR, envelope);

                SoapObject response = (SoapObject) envelope.bodyIn;
                responseString = response.getProperty(0).toString() + "," + params[5] + "," + params[6]+ "," + params[2];
                Log.e("responseString", responseString);

            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if (responseString.equalsIgnoreCase("error")) {
                Toast.makeText(SendDSRService.this, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            }else {

                String id[] = result.split(",");
                //Toast.makeText(SendDSRService.this,result+"   "+ id[1], Toast.LENGTH_LONG).show();
                DatabaseHelper db1 = new DatabaseHelper(SendDSRService.this);
                SQLiteDatabase db = db1.getWritableDatabase();ContentValues newValues = new ContentValues();
                newValues.put("xml7", "no data");
                flag = 6;

                db.update("SendDSR", newValues, "cashier_cd='"+PetroSoftData.Cashier_acno+"'", null);
                CommonUtilities.clearTable(SendDSRService.this, "OtherAddRsTable");
                PetroSoftData.OTHER_ADD_Rs = "0.00";

                //finish(getApplicationContext());
            }
            //flag = 3;
        }
    }

    class SendDSRExp extends AsyncTask<String, Void, String> {
        // ProgressDialog progressDialog;
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                SoapObject request = new SoapObject(
                        PetroSoftData.NAMESPACE,
                        PetroSoftData.METHOD_SEND_DSR);
                PropertyInfo propInfo = new PropertyInfo();
                propInfo.type = PropertyInfo.STRING_CLASS;

                request.addProperty("date", params[0]);
                request.addProperty("shift", params[1]);
                request.addProperty("cashier_cd", params[2]);
                request.addProperty("xmlstr", params[3]);
                request.addProperty("key", params[4]);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(PetroSoftData.URL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_SEND_DSR, envelope);

                SoapObject response = (SoapObject) envelope.bodyIn;
                responseString = response.getProperty(0).toString() + "," + params[5] + "," + params[6]+ "," + params[2];
                Log.e("responseString", responseString);
                
            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

             if (responseString.equalsIgnoreCase("error")) {
                Toast.makeText(SendDSRService.this, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            }else {
                
            	String id[] = result.split(",");
            	//Toast.makeText(SendDSRService.this,result+"   "+ id[1], Toast.LENGTH_LONG).show();                
                DatabaseHelper db1 = new DatabaseHelper(SendDSRService.this);
                SQLiteDatabase db = db1.getWritableDatabase();ContentValues newValues = new ContentValues();
                newValues.put("xml5", "no data");
                flag = 4;
                db.update("SendDSR", newValues, "cashier_cd='"+PetroSoftData.Cashier_acno+"'", null);
            	CommonUtilities.clearTable(SendDSRService.this, "OtherExpTable");
            	PetroSoftData.OTHER_EXP = "0.00";
                     
                //finish(getApplicationContext());
            }
            
        }
    }

    class SendDSRCCSwap extends AsyncTask<String, Void, String> {
        // ProgressDialog progressDialog;
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                SoapObject request = new SoapObject(
                        PetroSoftData.NAMESPACE,
                        PetroSoftData.METHOD_SEND_DSR);
                PropertyInfo propInfo = new PropertyInfo();
                propInfo.type = PropertyInfo.STRING_CLASS;

                request.addProperty("date", params[0]);
                request.addProperty("shift", params[1]);
                request.addProperty("cashier_cd", params[2]);
                request.addProperty("xmlstr", params[3]);
                request.addProperty("key", params[4]);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(PetroSoftData.URL);
                androidHttpTransport.call(PetroSoftData.NAMESPACE
                        + PetroSoftData.METHOD_SEND_DSR, envelope);

                SoapObject response = (SoapObject) envelope.bodyIn;
                responseString = response.getProperty(0).toString() + "," + params[5] + "," + params[6]+ "," + params[2];
                Log.e("responseString", responseString);

            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

            if (responseString.equalsIgnoreCase("error")) {
                Toast.makeText(SendDSRService.this, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            }else {

                String id[] = result.split(",");
                //Toast.makeText(SendDSRService.this,result+"   "+ id[1], Toast.LENGTH_LONG).show();
                DatabaseHelper db1 = new DatabaseHelper(SendDSRService.this);
                SQLiteDatabase db = db1.getWritableDatabase();ContentValues newValues = new ContentValues();
                newValues.put("xml8", "no data");
                flag = 7;
                db.update("SendDSR", newValues, "cashier_cd='"+PetroSoftData.Cashier_acno+"'", null);
                CommonUtilities.clearTable(SendDSRService.this, "SwapCardTable");
                PetroSoftData.SWAP_CARD = "0.00";

                //finish(getApplicationContext());
            }

        }
    }

    class SendDSRBank extends AsyncTask<String, Void, String> {
        // ProgressDialog progressDialog;
        String responseString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                SoapObject request = new SoapObject(
                        PetroSoftData.NAMESPACE,
                        PetroSoftData.METHOD_SEND_DSR);
                PropertyInfo propInfo = new PropertyInfo();
                propInfo.type = PropertyInfo.STRING_CLASS;

                request.addProperty("date", params[0]);
                request.addProperty("shift", params[1]);
                request.addProperty("cashier_cd", params[2]);
                request.addProperty("xmlstr", params[3]);
                request.addProperty("key", params[4]);
                Log.e("Params", params[0]+","+params[1]+","+params[2]+","+params[3]+","+params[4]);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);

                HttpTransportSE androidHttpTransport = new HttpTransportSE(PetroSoftData.URL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_SEND_DSR, envelope);

                SoapObject response = (SoapObject) envelope.bodyIn;
                responseString = response.getProperty(0).toString() + "," + params[5] + "," + params[6]+ "," + params[2];
                Log.e("responseString", responseString);
                
            } catch (Exception e) {
                responseString = "error";
                e.printStackTrace();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);

             if (responseString.equalsIgnoreCase("error")) {
                Toast.makeText(SendDSRService.this, "The server is taking too long to respond OR something is wrong with your iternet connection. Please try again later.", Toast.LENGTH_LONG)
                        .show();
            }else {
                
            	String id[] = result.split(",");
            	//Toast.makeText(SendDSRService.this,result+"   "+ id[1], Toast.LENGTH_LONG).show();                
                DatabaseHelper db1 = new DatabaseHelper(SendDSRService.this);
                SQLiteDatabase db = db1.getWritableDatabase();ContentValues newValues = new ContentValues();
                newValues.put("xml6", "no data");
                flag = 5;
                db.update("SendDSR", newValues, "cashier_cd='"+PetroSoftData.Cashier_acno+"'", null);
            	CommonUtilities.clearTable(SendDSRService.this, "BankDepoTable");
            	PetroSoftData.BANK_DEPO = "0.00";

            	                     
                //finish(getApplicationContext());
            }
             
             
             
        }
    }

}

