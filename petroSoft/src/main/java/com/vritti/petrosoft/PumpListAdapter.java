package com.vritti.petrosoft;

import java.util.ArrayList;

import com.vritti.database.DatabaseHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PumpListAdapter extends BaseAdapter {
	private Context parent;
	private ArrayList<PumpListBean> list;
	private ArrayList<PumpListBean> arraylist;
	private LayoutInflater mInflater;

	public PumpListAdapter(Context parent, ArrayList<PumpListBean> pumplistBeanList) {
		// TODO Auto-generated constructor stub
		this.parent = parent;
		this.list = pumplistBeanList;
		arraylist = new ArrayList<PumpListBean>();
		arraylist.addAll(pumplistBeanList);
		
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		String closing = "0.00";
		final ViewHolder holder;
		/*if (convertView == null){*/
			convertView = mInflater.inflate(R.layout.pumplist_item, null);
			holder = new ViewHolder();
			
			holder.tvpumpno = (TextView) convertView.findViewById(R.id.tvpumpno);
			holder.tvpumpname = (TextView) convertView.findViewById(R.id.tvpumpname);
			holder.tvclosing = (TextView) convertView.findViewById(R.id.tvclosing);
		/*}else{
			holder = (ViewHolder) convertView.getTag();
		}*/
		holder.tvpumpno.setText(list.get(position).getpump_no());
		holder.tvpumpname.setText(list.get(position).getpump_name());
		if(list.get(position).getclosing().toString()==""||list.get(position).getclosing()==null){
		holder.tvclosing.setText(closing);
		}else {
			holder.tvclosing.setText(list.get(position).getclosing());
		}
		return convertView;
	}
	
	static class ViewHolder {
		public TextView tvpumpno, tvpumpname,tvclosing;
	}


}
