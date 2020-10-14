package com.example.directoryshow1;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SMBCustomAdapter extends BaseAdapter{
Context context;
ArrayList<SMBHolder1> arrayList;


public SMBCustomAdapter(Context c, ArrayList<SMBHolder1> al ) {
	this.context=c;
	this.arrayList=al;
	
}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return arrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
LayoutInflater li= (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
View row =li.inflate(R.layout.smbdialog1a, parent,false);
//TextView tv1=(TextView)row.findViewById(R.id.dialog_txt111	);
TextView tv2=(TextView)row.findViewById(R.id.dialog_txt112	);
//tv1.setText(arrayList.get(position).getSmb_location_());
tv2.setText(arrayList.get(position).getUserName_());
return row;
		
	}

}
