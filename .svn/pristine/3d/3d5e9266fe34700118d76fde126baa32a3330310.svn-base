package com.vritti.petrosoft;

import java.util.ArrayList;

import com.vritti.database.DatabaseHelper;
import com.vritti.petrosoft.PumpListAdapter.ViewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BankDepoAdapter extends BaseAdapter {
	private Context parent;
	private ArrayList<BankDepoBean> list;
	private ArrayList<BankDepoBean> arraylist;
	private LayoutInflater mInflater;

	public BankDepoAdapter(Context parent, ArrayList<BankDepoBean> bankDepoBeanList) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
		this.list = bankDepoBeanList;
		arraylist = new ArrayList<BankDepoBean>();
		arraylist.addAll(bankDepoBeanList);
		
		mInflater = LayoutInflater.from(parent);
		DatabaseHelper db =  new DatabaseHelper(parent);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		final ViewHolder holder;
		/*if (convertView == null){*/
			convertView = mInflater.inflate(R.layout.productlist_item, null);
			holder = new ViewHolder();
			
			holder.tvItemName = (TextView) convertView.findViewById(R.id.tvItemName);
			holder.tvSaleQuantity = (TextView) convertView.findViewById(R.id.tvSaleQuantity);
		/*}else{
			holder = (ViewHolder) convertView.getTag();
		}*/
		holder.tvItemName.setText(list.get(position).getName());
		holder.tvSaleQuantity.setText(list.get(position).getAmount());
		
		return convertView;
	}
	
	static class ViewHolder {
		public TextView tvItemName, tvSaleQuantity;
	}
	

}
