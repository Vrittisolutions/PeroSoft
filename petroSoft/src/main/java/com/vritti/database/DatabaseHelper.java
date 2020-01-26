package com.vritti.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final int DATABASE_VERSION = 4;
	private static final String DATABASE_NAME = "PetroSoftDB";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL("CREATE TABLE Item(ItemName TEXT,ItemCode TEXT,ItemRate TEXT,ValidFrom TEXT, ItemGroup TEXT,HSNCode TEXT)");
		db.execSQL("CREATE TABLE Bill(BillId INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1 ,CustName TEXT,VechNo TEXT,RFIDNo TEXT)");
		db.execSQL("CREATE TABLE BillDetails(BillId INTEGER, ItemCode TEXT,Rate TEXT,Qty TEXT,Amount TEXT,shift TEXT,kmReading TEXT,sodetailid TEXT,VehNo TEXT)");
		db.execSQL("CREATE TABLE Product(item_code TEXT,end_qty TEXT)");
		db.execSQL("CREATE TABLE FirmDetails(FirmName TEXT,Address1 TEXT,Address2 TEXT,Address3 TEXT,Mobile TEXT,Phone TEXT,email TEXT,GSTNo TEXT)");
		db.execSQL("CREATE TABLE BankDepoTable(TId INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, Acno TEXT,Name TEXT, AcType TEXT,Amount TEXT, Narration TEXT,Rs2000 TEXT,Rs1000 TEXT,Rs500 TEXT,Rs200 TEXT,Rs100 TEXT,Rs50 TEXT,Rs20 TEXT,Rs10 TEXT,Rs5 TEXT,Rs2 TEXT,Rs1 TEXT,Coins TEXT,RpDt_Total TEXT)");
		db.execSQL("CREATE TABLE OtherExpTable(TId INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, Acno TEXT,Name TEXT, AcType TEXT,Amount TEXT, Narration TEXT)");
		db.execSQL("CREATE TABLE SwapCardTable(TId INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, Acno TEXT,Name TEXT, AcType TEXT, transid TEXT,Bat_no TEXT, Mode TEXT,Bamount TEXT, Remark TEXT)");
		db.execSQL("CREATE TABLE OtherRcptTable(TId INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, Acno TEXT,Name TEXT, AcType TEXT,Amount TEXT, Narration TEXT)");
		db.execSQL("CREATE TABLE OtherAddRsTable(TId INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1,Amount TEXT, Narration TEXT)");
		db.execSQL("CREATE TABLE CashierMaster(Acno TEXT,Name TEXT,pwd TEXT,usergr TEXT, AcType TEXT)");
		db.execSQL("CREATE TABLE VehicleMaster(CustCode TEXT,VechNo TEXT,RFIDNo TEXT)");
		db.execSQL("CREATE TABLE ProdSaleTable(TId INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, ItemName TEXT,ItemCode TEXT,ItemQty TEXT,ItemRate TEXT,ItemAmt TEXT)");

		db.execSQL("CREATE TABLE IPAddress(IP TEXT)");
		db.execSQL("CREATE TABLE IsOperator(Operator TEXT)");
		db.execSQL("CREATE TABLE Shift(ShiftNo TEXT)");
		db.execSQL("CREATE TABLE Bluetooth_Address(Address TEXT)");
		db.execSQL("CREATE TABLE PumpList(pump_no TEXT, pump_name TEXT, item_code TEXT, item_desc TEXT, pump_seq TEXT, opening TEXT, closing TEXT, testing TEXT, SaleLtrs TEXT, Rate TEXT, Amount TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS Item");
		db.execSQL("DROP TABLE IF EXISTS Bill");
		db.execSQL("DROP TABLE IF EXISTS BillDetails");
		db.execSQL("DROP TABLE IF EXISTS Product");
		//db.execSQL("DROP TABLE IF EXISTS CashTable");
		db.execSQL("DROP TABLE IF EXISTS FirmDetails");
		db.execSQL("DROP TABLE IF EXISTS PumpList");
		db.execSQL("DROP TABLE IF EXISTS ProdSaleTable");
		db.execSQL("DROP TABLE IF EXISTS BankDepoTable");
		db.execSQL("DROP TABLE IF EXISTS OtherRcptTable");
		db.execSQL("DROP TABLE IF EXISTS OtherAddRsTable");
		db.execSQL("DROP TABLE IF EXISTS OtherExpTable");
		db.execSQL("DROP TABLE IF EXISTS SwapCardTable");
		db.execSQL("DROP TABLE IF EXISTS VehicleMaster");
		db.execSQL("DROP TABLE IF EXISTS CashierMaster");
		db.execSQL("DROP TABLE IF EXISTS IPAddress");
		db.execSQL("DROP TABLE IF EXISTS IsOperator");
		db.execSQL("DROP TABLE IF EXISTS Shift");
		db.execSQL("DROP TABLE IF EXISTS Bluetooth_Address");
		onCreate(db);
	}

	public void CreateProdSaleTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS ProdSaleTable");
		db.execSQL("CREATE TABLE ProdSaleTable(TId INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1, ItemName TEXT,ItemCode TEXT,ItemQty TEXT,ItemRate TEXT,ItemAmt TEXT)");
	}
	
	public void CreateCashTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS CashTable");
		db.execSQL("CREATE TABLE CashTable(CashId INTEGER PRIMARY KEY AUTOINCREMENT DEFAULT 1 ,Rs2000 TEXT,Rs1000 TEXT,Rs500 TEXT,Rs200 TEXT,Rs100 TEXT,Rs50 TEXT,Rs20 TEXT,Rs10 TEXT,Rs5 TEXT,Rs2 TEXT,Rs1 TEXT,Coins TEXT,RpDt_Total TEXT)");
		}
	
	public void DropCashTable(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS CashTable");		
	}
	
	public void CreateSendDSR(){
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL("DROP TABLE IF EXISTS SendDSR");
		db.execSQL("CREATE TABLE IF NOT EXISTS SendDSR(date TEXT, shift TEXT, cashier_cd TEXT, xml2 TEXT, xml3 TEXT, xml4 TEXT, xml5 TEXT, xml6 TEXT, xml7 TEXT, xml8 TEXT, isUploaded TEXT)");
	}
	 public void addSendDSR(String date, String shift, String cashier_cd, String xml2,
             String xml3,String xml4,String xml5,String xml6,String xml7,String xml8, String isUploaded) {
		 SQLiteDatabase db = this.getWritableDatabase();
		 ContentValues cv = new ContentValues();
		 cv.put("date", date);
		 cv.put("shift", shift);
		 cv.put("cashier_cd", cashier_cd);
		 cv.put("xml2", xml2);
		 cv.put("xml3", xml3);
		 cv.put("xml4", xml4);
		 cv.put("xml5", xml5);
		 cv.put("xml6", xml6);
		 cv.put("xml7", xml7);
		 cv.put("xml8", xml8);
		 cv.put("isUploaded", isUploaded);

		 long a = db.insert("SendDSR", null, cv);
		 Log.e("Long a output of db", String.valueOf(a));
	 }
	
	public void UpdatePumpList(String pump_no ,String  pump_name ,String  item_code ,String  item_desc ,String pump_seq,String  opening ,String  closing ,String  testing ,String  SaleLtrs ,String  Rate ,String  Amount) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("pump_no", pump_no);
		cv.put("pump_name", pump_name);
		cv.put("item_code", item_code);
		cv.put("item_desc", item_desc);
		cv.put("pump_seq", pump_seq);
		cv.put("opening", opening);
		cv.put("closing", closing);
		cv.put("testing", testing);
		cv.put("SaleLtrs", SaleLtrs);
		cv.put("Rate", Rate);
		cv.put("Amount", Amount);
		db.update("PumpList", cv, "pump_no" + "='" + pump_no+"'", null);
	}
	
	public void AddProdSaleTable(String ItemName ,String ItemCode ,String ItemQty ,String ItemRate ,String ItemAmt) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("ItemName", ItemName);
		cv.put("ItemCode", ItemCode);
		cv.put("ItemQty", ItemQty);
		cv.put("ItemRate", ItemRate);
		cv.put("ItemAmt", ItemAmt);
		db.insert("ProdSaleTable", null, cv);
	}
	
	public void UpdateProdSaleTable(int TId, String ItemName ,String ItemCode ,String ItemQty ,String ItemRate ,String ItemAmt) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("TId", TId);
		cv.put("ItemName", ItemName);
		cv.put("ItemCode", ItemCode);
		cv.put("ItemQty", ItemQty);
		cv.put("ItemRate", ItemRate);
		cv.put("ItemAmt", ItemAmt);
		db.update("ProdSaleTable", cv, "TId" + "='" + TId+"'", null);
	}
	
	
	public void AddBankDepoTable(String Acno,String Name,String AcType,String Amount,String Narration,
			String Rs2000,String Rs1000,String Rs500,String Rs200,String Rs100,String Rs50,String Rs20,String Rs10,String Rs5,String Rs2,String Rs1,String Coins,String RpDt_Total) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Acno", Acno);
		cv.put("Name", Name);
		cv.put("AcType", AcType);
		cv.put("Amount", Amount);
		cv.put("Narration", Narration);
		cv.put("Rs2000", Rs2000);
		cv.put("Rs1000", Rs1000);
		cv.put("Rs500", Rs500);
		cv.put("Rs200", Rs200);
		cv.put("Rs100", Rs100);
		cv.put("Rs50", Rs50);
		cv.put("Rs20", Rs20);
		cv.put("Rs10", Rs10);
		cv.put("Rs5", Rs5);
		cv.put("Rs2", Rs2);
		cv.put("Rs1", Rs1);
		cv.put("Coins", Coins);
		cv.put("RpDt_Total", RpDt_Total);
		db.insert("BankDepoTable", null, cv);
	}
	
	public void UpdateBankDepoTable(int TId,String Acno,String Name,String AcType,String Amount,String Narration,
			String Rs2000,String Rs1000,String Rs500,String Rs200,String Rs100,String Rs50,String Rs20,String Rs10,String Rs5,String Rs2,String Rs1,String Coins,String RpDt_Total) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("TId", TId);
		cv.put("Acno", Acno);
		cv.put("Name", Name);
		cv.put("AcType", AcType);
		cv.put("Amount", Amount);
		cv.put("Narration", Narration);
		cv.put("Rs2000", Rs2000);
		cv.put("Rs1000", Rs1000);
		cv.put("Rs500", Rs500);
		cv.put("Rs200", Rs200);
		cv.put("Rs100", Rs100);
		cv.put("Rs50", Rs50);
		cv.put("Rs20", Rs20);
		cv.put("Rs10", Rs10);
		cv.put("Rs5", Rs5);
		cv.put("Rs2", Rs2);
		cv.put("Rs1", Rs1);
		cv.put("Coins", Coins);
		cv.put("RpDt_Total", RpDt_Total);
		System.out.println("Total bank" +RpDt_Total);
		System.out.println("Total bank_1" +Amount);
		System.out.println("Tid"+TId);

		db.update("BankDepoTable", cv, "TId" + "='" + TId+"'", null);
	}
	
	public void AddCashTable(String Rs2000,String Rs1000,String Rs500,String Rs200,String Rs100,String Rs50,String Rs20,String Rs10,String Rs5,String Rs2,String Rs1,String Coins,String RpDt_Total) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Rs2000", Rs2000);
		cv.put("Rs1000", Rs1000);
		cv.put("Rs500", Rs500);
		cv.put("Rs200", Rs200);
		cv.put("Rs100", Rs100);
		cv.put("Rs50", Rs50);
		cv.put("Rs20", Rs20);
		cv.put("Rs10", Rs10);
		cv.put("Rs5", Rs5);
		cv.put("Rs2", Rs2);
		cv.put("Rs1", Rs1);
		cv.put("Coins", Coins);
		cv.put("RpDt_Total", RpDt_Total);
		db.insert("CashTable", null, cv);
	}
	
	public void UpdateCashTable(int CashId,String Rs2000,String Rs1000,String Rs500,String Rs200,String Rs100,String Rs50,String Rs20,String Rs10,String Rs5,String Rs2,String Rs1,String Coins,String RpDt_Total) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("CashId", CashId);
		cv.put("Rs2000", Rs2000);
		cv.put("Rs1000", Rs1000);
		cv.put("Rs500", Rs500);
		cv.put("Rs200", Rs200);
		cv.put("Rs100", Rs100);
		cv.put("Rs50", Rs50);
		cv.put("Rs20", Rs20);
		cv.put("Rs10", Rs10);
		cv.put("Rs5", Rs5);
		cv.put("Rs2", Rs2);
		cv.put("Rs1", Rs1);
		cv.put("Coins", Coins);
		cv.put("RpDt_Total", RpDt_Total);
		db.update("CashTable", cv, "CashId" + "='" + CashId+"'", null);
	}


	public void AddOtherAddRsTable(/*String Acno,String Name,String AcType,*/String Amount,String Narration) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		/*cv.put("Acno", Acno);
		cv.put("Name", Name);
		cv.put("AcType", AcType);*/
		cv.put("Amount", Amount);
		cv.put("Narration", Narration);
		db.insert("OtherAddRsTable", null, cv);
	}

	public void UpdateOtherAddRsTable(int TId,/*String Acno,String Name,String AcType,*/String Amount,String Narration) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("TId", TId);
		cv.put("Amount", Amount);
		cv.put("Narration", Narration);
		db.update("OtherAddRsTable", cv, "TId" + "='" + TId+"'", null);
	}


	public void AddOtherRcptTable(String Acno,String Name,String AcType,String Amount,String Narration) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Acno", Acno);
		cv.put("Name", Name);
		cv.put("AcType", AcType);
		cv.put("Amount", Amount);
		cv.put("Narration", Narration);
		db.insert("OtherRcptTable", null, cv);
	}
	
	public void UpdateOtherRcptTable(int TId,String Acno,String Name,String AcType,String Amount,String Narration) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("TId", TId);
		cv.put("Acno", Acno);
		cv.put("Name", Name);
		cv.put("AcType", AcType);
		cv.put("Amount", Amount);
		cv.put("Narration", Narration);
		db.update("OtherRcptTable", cv, "TId" + "='" + TId+"'", null);
	}

	public void AddSwapCardTable(String Acno,String Name,String AcType,String tid,String Bat_no,String Mode,String Bamount,String Remark) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Acno", Acno);
		cv.put("Name", Name);
		cv.put("AcType", AcType);
		cv.put("transid", tid);
		cv.put("Bat_no", Bat_no);
		cv.put("Mode", Mode);
		cv.put("Bamount", Bamount);
		cv.put("Remark", Remark);
		db.insert("SwapCardTable", null, cv);
	}

	public void UpdateSwapCardTable(int TId,String Acno,String Name,String AcType,String tid,String Bat_no,String Mode,String Bamount,String Remark) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("TId", TId);
		cv.put("Acno", Acno);
		cv.put("Name", Name);
		cv.put("AcType", AcType);
		cv.put("transid", tid);
		cv.put("Bat_no", Bat_no);
		cv.put("Mode", Mode);
		cv.put("Bamount", Bamount);
		cv.put("Remark", Remark);
		db.update("SwapCardTable", cv, "TId" + "='" + TId+"'", null);
	}

	public void AddOtherExpTable(String Acno,String Name,String AcType,String Amount,String Narration) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Acno", Acno);
		cv.put("Name", Name);
		cv.put("AcType", AcType);
		cv.put("Amount", Amount);
		cv.put("Narration", Narration);
		db.insert("OtherExpTable", null, cv);
	}
	
	public void UpdateOtherExpTable(int TId,String Acno,String Name,String AcType,String Amount,String Narration) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("TId", TId);
		cv.put("Acno", Acno);
		cv.put("Name", Name);
		cv.put("AcType", AcType);
		cv.put("Amount", Amount);
		cv.put("Narration", Narration);
		db.update("OtherExpTable", cv, "TId" + "='" + TId+"'", null);
	}
	
	public void AddItems(ItemHelper data) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("ItemName", data.getItemName());
		cv.put("ItemCode", data.getItemCode());
		cv.put("ItemRate", data.getItemRate());
		cv.put("ValidFrom", data.getValidFrom());
		cv.put("ItemGroup", data.getItemGroup());
		cv.put("HSNCode",data.getHSNCode());
		db.insert("Item", null, cv);
	}

	public void AddCashier(String acno, String name,String pwd,String usergr,String AcType) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Acno", acno);
		cv.put("Name", name);
		cv.put("pwd", pwd);
		cv.put("usergr", usergr);
		cv.put("AcType", AcType);
		Log.e("Insert Cashier",cv.toString());
		db.insert("CashierMaster", null, cv);
	}
	
	public void AddPumpList(String pump_no, String pump_name, String item_code, String item_desc, String pump_seq) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("pump_no", pump_no);
		cv.put("pump_name", pump_name);
		cv.put("item_code", item_code);
		cv.put("item_desc", item_desc);
		cv.put("pump_seq", pump_seq);
		cv.put("opening", "0.00");
		cv.put("closing", "0.00");
		cv.put("testing", "0.00");
		cv.put("SaleLtrs", "0.00");
		cv.put("Rate", "0.00");
		cv.put("Amount", "0.00");
		
		db.insert("PumpList", null, cv);
	}
	
	

	public long AddBill(String CustName, String VechNo, String RFIDNo) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues cv = new ContentValues();
		cv.put("CustName", CustName);
		cv.put("VechNo", VechNo);
		cv.put("RFIDNo", RFIDNo);
		return db.insert("Bill", null, cv);
	}

	public void AddBillDetails(int BillId, String ItemCode, String Rate,
			String Qty, String Amount, String shift, String kmReading, String sodetailid, String VehNo) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("BillId", BillId);
		cv.put("ItemCode", ItemCode);
		cv.put("Rate", Rate);
		cv.put("Qty", Qty);
		cv.put("Amount", Amount);
		cv.put("shift", shift);
		cv.put("kmReading", kmReading);
		cv.put("sodetailid", sodetailid);
		cv.put("VehNo", VehNo);
		db.insert("BillDetails", null, cv);
	}

	public void AddProduct(String item_code, String end_qty) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("item_code", item_code);
		cv.put("end_qty", end_qty);
		Log.e("Product",cv.toString());
		db.insert("Product", null, cv);
	}

	public void AddFirmDetails(String FirmName, String Address1,
			String Address2, String Address3, String Mobile, String Phone, String email, String GSTNo) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("FirmName", FirmName);
		cv.put("Address1", Address1);
		cv.put("Address2", Address2);
		cv.put("Address3", Address3);
		cv.put("Mobile", Mobile);
		cv.put("Phone", Phone);
		cv.put("email", email);
		cv.put("GSTNo", GSTNo);
		db.insert("FirmDetails", null, cv);
	}

	/*public void AddCustomerMaster(String AcNo, String Name, String City) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("AcNo", AcNo);
		cv.put("Name", Name);
		cv.put("City", City);
		db.insert("CustomerMaster", null, cv);
	}*/

	public void AddVehicleMasters(String CustCode, String VechNo, String RFIDNo) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("CustCode", CustCode);
		cv.put("VechNo", VechNo);
		cv.put("RFIDNo", RFIDNo);
		db.insert("VehicleMaster", null, cv);
	}
	
	public void AddOperator(String operator) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("IsOperator", null, null);

		ContentValues cv = new ContentValues();
		cv.put("Operator", operator);
		db.insert("IsOperator", null, cv);
	}
	
	public String GetOperator() {
		String Operator = "";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT Operator FROM IsOperator", null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			Operator = c.getString(0).trim();
		}
		return Operator;
	}

	public void AddIPAddress(String ip) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("IPAddress", null, null);

		ContentValues cv = new ContentValues();
		cv.put("IP", ip);
		db.insert("IPAddress", null, cv);
	}

	public String GetIPAddress() {
		String IP = "";
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor c = db.rawQuery("SELECT IP FROM IPAddress", null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			IP = c.getString(0).trim();
		}
		return IP;
	}

	public void AddShift(String shift) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("ShiftNo", shift);
		db.insert("Shift", null, cv);
	}

	public void AddBluetooth(String address) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("Address", address);
		db.insert("Bluetooth_Address", null, cv);
	}
}