package com.vritti.common;

import android.graphics.Bitmap;
import android.net.Uri;

public class PetroSoftData {
	public static String IP;
	public static String URL;
	public static String isOperator;
	public static final String NAMESPACE = "http://tempuri.org/";
	public static final String METHOD_GET_PRODUCT = "GetProduct";
	public static final String METHOD_ITEM_RATE_MASTER = "GetItemRateMaster";
	public static final String METHOD_GET_FIRM_NAME_ADD = "GetFirmNameAdd";
	public static final String METHOD_GET_CASHIER = "Getcashier";
	public static final String METHOD_GET_CLOSING = "GetClosingReading";
	public static final String METHOD_GET_PUMPLIST = "GetPumpList";
	public static final String METHOD_GET_VEHICLE = "GetVehList";
	public static final String METHOD_SEND_DSR = "SaveDSR";
	//Getcashier
	public static final String METHOD_UPDATE_SALE_XML = "UpdateSaleXml";
	public static final String METHOD_REGISTER_CARD = "RegisterCard";
	public static final String METHOD_GET_VEHICLE_LIST = "GetVehList";
	public static final String METHOD_GET_CUST_OF_REG_MOB = "GetCustFromMob";
	public static final String METHOD_GET_CUST_LIST = "GetCustList";
	public static final String METHOD_GET_ITEM_RATE_MASTER = "GetItemRateMaster";
	public static final String METHOD_GET_CUST_DETAILS = "GetCustDetails";
	public static final String METHOD_GET_CUST_DETAILS_WITHOUT_CARD = "GetCustDetailsWithoutCard";
	public static final String METHOD_REGISTER_VEHICLE = "RegisterVehicle";
	public static final String METHOD_GET_BILL_ID = "Getmaxbillno";
	public static final String METHOD_PRINT_BILL = "PrintBill";
	
	public static final String METHOD_GET_CUST_DETAILS_bycoupon= "GetCustDetailsbycoupon";

	public static final int REQUEST_ENABLE_BT = 11;
	public static final int REQUEST_CONNECT_DEVICE = 12;
	public static final int REQUEST_GET_PRODUCT_DETAILS = 13;
	public static final int REQUEST_GET_CASHIER_DETAILS = 14;
	public static final int REQUEST_GET_VEHICLE_DETAILS = 15;
	
	public static final int REQUEST_GET_CASHIER_DETAILS_FOR_DSR = 21;
	public static final int REQUEST_GET_PETRO_SALE_DETAILS_FOR_DSR = 22;	
	public static final int REQUEST_GET_PRODUCT_SALE_DETAILS_FOR_DSR = 23;
	public static final int REQUEST_GET_OTHER_RCPT_DETAILS_FOR_DSR = 24;
	public static final int REQUEST_GET_OTHER_EXP_DETAILS_FOR_DSR = 25;
	public static final int REQUEST_GET_BANK_DEPOSIT_FOR_DSR = 26;
	public static final int REQUEST_GET_OTHER_ADD_Rs_DETAILS_FOR_DSR = 27;
	public static final int REQUEST_GET_SWAP_CARD_DETAILS_FOR_DSR = 28;

	public static String SHIFT;
	public static String PETRO_SALE = "0.00";
	public static String PROD_SALE = "0.00";
	public static String OTHER_RCPT = "0.00";
	public static String OTHER_ADD_Rs = "0.00";
	public static String OTHER_EXP = "0.00";
	public static String SWAP_CARD = "0.00";
	public static String BANK_DEPO = "0.00";
	public static String CREDIT_SALE = "0.00";

	public static String username;
	public static String password;
	
	public static String PRINTERSETTING=null;
	public static String NeedVehKm="No";
	public static String RemoveRQAC="No";
	public static String OnlyRFIDSale="No";
	public static String AllowRewardsPoints="No";
	public static String ScanMode_RFID_QRCODE = "RFID";
    public static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

	public static String cust_code;
	public static String veh_no;
	public static String rfid_card;
	public static String name;
	public static String itemname;
	public static String qty;
	public static String vv;
	public static String Coupon;
	public static String sodetailid;
	public static String cust_balance;
	public static String cred_lim;
	public static String Reward_Points;

	public static String item_code;
	public static String pl_rate;
	public static String item_desc;
	public static String validfrom;
	
	public static String Cashier_acno;
	public static String Cashier_name = null;
	public static String vehicle_no;
	public static String DATEnTIME = null;
	public static String WORKINGDATE = null;
	public static String TnC = null;
	public static String DefaultItem = null;

	public static String imgPath = null;
	public static String urlpath = null;
	public static Uri imgUri;
	public static Bitmap imgBitmap;

}