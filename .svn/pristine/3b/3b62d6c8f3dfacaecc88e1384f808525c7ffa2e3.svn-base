package com.vritti.petrosoft;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vritti.database.DatabaseHelper;

import java.util.ArrayList;

public class SwapCardAdapter extends BaseAdapter {
	private Context parent;
	private ArrayList<SwapCardBean> list;
	private ArrayList<SwapCardBean> arraylist;
	private LayoutInflater mInflater;

	public SwapCardAdapter(Context parent, ArrayList<SwapCardBean> bankDepoBeanList) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
		this.list = bankDepoBeanList;
		arraylist = new ArrayList<SwapCardBean>();
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
		if(list.get(position).getMode().equalsIgnoreCase("Receivable")) {
			holder.tvSaleQuantity.setText(list.get(position).getBamount());
			holder.tvSaleQuantity.setTextColor(Color.parseColor("#006400"));
		}else if(list.get(position).getMode().equalsIgnoreCase("Payable")) {
			holder.tvSaleQuantity.setText(list.get(position).getBamount());
			holder.tvSaleQuantity.setTextColor(Color.parseColor("#ff0000"));
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		public TextView tvItemName, tvSaleQuantity;
	}
	

}
