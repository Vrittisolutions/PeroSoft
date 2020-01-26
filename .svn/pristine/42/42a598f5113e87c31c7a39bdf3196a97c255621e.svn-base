package com.vritti.petrosoft;

import java.util.ArrayList;
import java.util.Collections;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.vritti.common.CommonUtilities;
import com.vritti.common.PetroSoftData;
import com.vritti.database.DatabaseHelper;
import com.vritti.database.ItemHelper;

public class ProductSelectionActivity extends Activity {
	private Context parent;
	private EditText edittextFilter;
	private ListView listView;
	private DatabaseHelper databaseHelper;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> productList;
	private ProgressDialog progressDialog;
	private ActionBar actionBar;
	ImageView ivlogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_product_list_chooser);
		// getActionBarContents();
		initViews();
		events();
		updateList();
		filterList();
	}

	private void initViews() {
		parent = ProductSelectionActivity.this;
		edittextFilter = (EditText) findViewById(R.id.edittext_products_filter);
		listView = (ListView) findViewById(R.id.listview_products_list);
		productList = new ArrayList<String>();
		ivlogo = (ImageView) findViewById(R.id.iconlogo);
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

	private void events() {
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("MESSAGE", listView.getItemAtPosition(position)
						.toString());
				setResult(PetroSoftData.REQUEST_GET_PRODUCT_DETAILS, intent);
				finish();
			}
		});
	}

	private void filterList() {
		edittextFilter.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				setListFilteredItems();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	protected void setListFilteredItems() {
		int textlength = edittextFilter.getText().length();
		ArrayList<String> filteredArray = new ArrayList<String>();
		filteredArray.clear();
		for (int i = 0; i < productList.size(); i++) {
			if (textlength <= productList.get(i).length()) {

				String s2 = edittextFilter.getText().toString();
				if (productList.get(i).toString().toLowerCase()
						.contains(edittextFilter.getText().toString())) {
					filteredArray.add(productList.get(i));
				}

			}
		}

		listView.setAdapter(new ArrayAdapter<String>(parent,
				android.R.layout.simple_list_item_1, filteredArray));
	}

	private void refreshList() {
		if (CommonUtilities.isInternetAvailable(parent))
			new GetProductList().execute();
		else
			Toast.makeText(parent, "No internet connection available!!!",
					Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		intent.putExtra("MESSAGE", "-NA-");
		setResult(PetroSoftData.REQUEST_GET_PRODUCT_DETAILS, intent);
		finish();
	}

	class GetProductList extends AsyncTask<Void, Void, Void> {
		String exceptionString = "ok";

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(parent);
			progressDialog.setMessage("Refreshing...");
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			try {

				// String fullURL = getResources().getString(R.string.url);
				String fullURL = PetroSoftData.URL;
				SoapObject request = new SoapObject(PetroSoftData.NAMESPACE,
						PetroSoftData.METHOD_GET_ITEM_RATE_MASTER);
				PropertyInfo propInfo = new PropertyInfo();
				propInfo.type = PropertyInfo.STRING_CLASS;
				// adding parameters
				// request.addProperty("rfidstr", rfidNumberEdittext.getText()
				// .toString());

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				envelope.setOutputSoapObject(request);
				HttpTransportSE androidHttpTransport = new HttpTransportSE(
						fullURL);
				androidHttpTransport.call(PetroSoftData.NAMESPACE
						+ PetroSoftData.METHOD_GET_ITEM_RATE_MASTER, envelope);

				SoapObject response = (SoapObject) envelope.bodyIn;

				SoapObject GetItemRateMasterResult = (SoapObject) response
						.getProperty(0);
				SoapObject NewDataSet = (SoapObject) GetItemRateMasterResult
						.getProperty(0);
				for (int i = 0; i < NewDataSet.getPropertyCount(); i++) {
					SoapObject Table = (SoapObject) NewDataSet.getProperty(i);
					databaseHelper.AddItems(new ItemHelper(Table.getProperty(
							"item_desc").toString(), Table.getProperty(
							"item_code").toString(), Table.getProperty(
							"pl_rate").toString(), Table.getProperty(
							"validfrom").toString(), Table
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

			showListData();
			updateList();
		}

	}

	private void showListData() {
		adapter = new ArrayAdapter<String>(parent,
				android.R.layout.simple_list_item_1, productList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		listView.setAdapter(adapter);
	}

	public void updateList() {

		productList.clear();
		DatabaseHelper db1 = new DatabaseHelper(parent);
		SQLiteDatabase db = db1.getWritableDatabase();

		Cursor cursor = db.rawQuery("Select ItemName,ItemCode from Item", null);

		if (cursor.getCount() != 0) {
			cursor.moveToFirst();
			do {
				productList.add(cursor.getString(1) + "  - "
						+ cursor.getString(0));
			} while (cursor.moveToNext());

			cursor.close();
			db.close();
			db1.close();
		}

		Collections.sort(productList, String.CASE_INSENSITIVE_ORDER);
		String[] items1 = productList.toArray(new String[productList.size()]);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(parent,
				android.R.layout.simple_list_item_1, items1);
		adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
		listView.setAdapter(adapter1);
	}
}