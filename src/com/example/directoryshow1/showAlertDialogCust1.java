package com.example.directoryshow1;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class showAlertDialogCust1 implements OnItemSelectedListener {
	SharedPreferences sharedpreferences;
	Context context;
	double screenPARAM;
	String result=null;
	public static final String MyPREFERENCES = "SVPrefs" ;
	public static final String Text = "TEXT";	
Bitmap bm;
ImageView iv;
List<String> categoriesCOL = new ArrayList<String>();
List<String> categoriesSIZ = new ArrayList<String>();
Integer[] images = { R.drawable.white, R.drawable.yellow,
		R.drawable.green, R.drawable.blue, R.drawable.black, R.drawable.red };
int Tcolor=Color.WHITE;
float Tsize=20.0f;


   public 	showAlertDialogCust1(Context c, double screenPARAM){
		this.context=c;
		this.screenPARAM=screenPARAM;
sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

	}
	
	public void showDial(String s, final String path1,String file, final Bitmap myBitmap){
	
		final Dialog dialog = new Dialog(context);
		float sz;
		if (screenPARAM>6){
		dialog.setContentView(R.layout.custdialog7);
		sz=18.0f;
		}
		else {
			dialog.setContentView(R.layout.custdialog7);
			sz=14.0f;
		}
		dialog.setTitle("please enter file name to save");	
		TextView text = (TextView) dialog.findViewById(R.id.text);
		TextView dir = (TextView) dialog.findViewById(R.id.dir);

		final EditText et = (EditText) dialog.findViewById(R.id.te);
		et.setText(file);
		text.setTextSize(sz);
		text.setText(s);
		
		dir.setText("Directory is: "+path1);
		dir.setTextSize(sz);
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		image.setImageResource(R.drawable.save);

		Button ok = (Button) dialog.findViewById(R.id.dialogButtonOK);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String h1="DEFAULT";
				if (et.getText().toString().length()>0 && 
						!et.getText().toString().equals("")  ){
	            h1=et.getText().toString();
				}
				SaveBitmapFile SBF = new SaveBitmapFile();
				SBF.saveBitmap(myBitmap, path1, h1);
		dialog.dismiss();
			}
		});

		dialog.show();
		
	  }

	public void getText(final ImageView iv,	final GraphicsOps1 GO2,
			final float upx, final float upy) {

		Toast.makeText(context,"x "+String.valueOf(upx)+
		"y "+String.valueOf(upy),Toast.LENGTH_SHORT).show();

		final Dialog dialog = new Dialog(context);
		float sz;
		if (screenPARAM>6){
		dialog.setContentView(R.layout.custdialog7a);
		sz=18.0f;
		}
		else {
			dialog.setContentView(R.layout.custdialog7a);
			sz=14.0f;
		}
		dialog.setTitle("please enter TEXT to add");	
		TextView text = (TextView) dialog.findViewById(R.id.text);
		ImageView image = (ImageView) dialog.findViewById(R.id.image);
		Spinner spinnerCOLOR = (Spinner) dialog.findViewById(R.id.spinnerCOLOR);
		Spinner spinnerSIZE = (Spinner) dialog.findViewById(R.id.spinnerSIZE);
		
		final EditText et = (EditText) dialog.findViewById(R.id.te);
		text.setTextSize(sz);
		text.setText("TEXT  BMPs");
		
		image.setImageResource(R.drawable.addtext);
		
		spinnerCOLOR.setOnItemSelectedListener(this);
		spinnerSIZE.setOnItemSelectedListener(this);
		categoriesCOL.add("WHITE");
		categoriesCOL.add("YELLOW");
		categoriesCOL.add("GREEN");
		categoriesCOL.add("BLUE");
		categoriesCOL.add("WHITE");
		categoriesCOL.add("RED");
		categoriesSIZ.add("MINI");
		categoriesSIZ.add("SMALL");
		categoriesSIZ.add("MEDIUM");
		categoriesSIZ.add("LARGE");
		categoriesSIZ.add("HUGE");

	
		// Setting a Custom Adapter to the Spinner
spinnerCOLOR.setAdapter(new MyAdapter(context, R.layout.custdialog7acustcolor,
		categoriesCOL));
		
		
		
		
		
		
//ArrayAdapter<String> dataAdapterCOL =
//new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categoriesCOL);
ArrayAdapter<String> dataAdapterSIZ =
new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, categoriesSIZ);

//dataAdapterCOL.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
dataAdapterSIZ.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
//spinnerCOLOR.setAdapter(dataAdapterCOL);
spinnerSIZE.setAdapter(dataAdapterSIZ);
		
		Button ok = (Button) dialog.findViewById(R.id.dialogButtonOK);
		ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences.Editor editor = sharedpreferences.edit();
				if (et.getText().toString().length()>0 && 
						!et.getText().toString().equals("")  ){
		            editor.putString(Text, et.getText().toString()).apply();
				}
				else {
			    editor.putString(Text, "DEFAULT").apply();	
				}
				
							
bm=GO2.mark(context,et.getText().toString(), upx,upy,
		Tcolor,Tsize,iv);
				if (null!=bm) {
			    iv.setImageBitmap(bm);
			    GO2.putBitmap(bm);
			    }
				Toast.makeText(context,"y======"+et.getText().toString()+" "+
			    String.valueOf(Tcolor)+"/// "+String.valueOf(Tsize)
						
						,Toast.LENGTH_SHORT).show();
			
		dialog.dismiss();
			}
		});
		
		dialog.show();
		
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
	if (parent.getId()==R.id.spinnerCOLOR){
		if (position==0) Tcolor=Color.WHITE;
		if (position==1) Tcolor=Color.YELLOW;
		if (position==2) Tcolor=Color.GREEN;
		if (position==3) Tcolor=Color.BLUE;
		if (position==4) Tcolor=Color.BLACK;
		if (position==5) Tcolor=Color.RED;
		}
	
	if (parent.getId()==R.id.spinnerSIZE){
		if (position==0) Tsize=16.0f;
		if (position==1) Tsize=20.0f;
		if (position==2) Tsize=26.0f;
		if (position==3) Tsize=34.0f;
		if (position==4) Tsize=46.0f;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		
	}	
	
	
	
	////////////////////////////////////////////////////////////////////
	// Creating an Adapter Class
	public class MyAdapter extends ArrayAdapter {
	 
	public MyAdapter(Context context, int textViewResourceId,
	List<String> categoriesCOL) {
	super(context, textViewResourceId, categoriesCOL);
	}
	 
	public View getCustomView(int position, View convertView,
	ViewGroup parent) {
	// Inflating the layout for the custom Spinner
	 
LayoutInflater inflater = 
(LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	 
//	LayoutInflater inflater = getLayoutInflater();
	View layout = inflater.inflate(R.layout.custdialog7acustcolor, parent, false);
	 
	TextView tvLanguage = (TextView) layout
	.findViewById(R.id.tvLanguage);
	 
	// Setting the text using the array
	tvLanguage.setText(categoriesCOL.get(position));
	 
	// Setting the color of the text
	tvLanguage.setTextColor(Color.rgb(75, 180, 225));
	 
	// Declaring and Typecasting the imageView in the inflated layout
	ImageView img = (ImageView) layout.findViewById(R.id.imgLanguage);
	 
	// Setting an image using the id's in the array
	img.setImageResource(images[position]);
	 
	// Setting Special atrributes for 1st element
	if (position == 0) {
	// Removing the image view
	img.setVisibility(View.GONE);
	// Setting the size of the text
	tvLanguage.setTextSize(20f);
	// Setting the text Color
	tvLanguage.setTextColor(Color.BLACK);
	 
	}
	 
	return layout;
	}
	 
	// It gets a View that displays in the drop down popup the data at the specified position
	@Override
	public View getDropDownView(int position, View convertView,
	ViewGroup parent) {
	return getCustomView(position, convertView, parent);
	}
	 
	// It gets a View that displays the data at the specified position
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	return getCustomView(position, convertView, parent);
	}
	}
	
	
}
	
	
	

