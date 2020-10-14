package com.example.directoryshow1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.ActionBar;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class BlueToothTest1 extends Activity implements View.OnClickListener{

	private Button b1,b2,b4,xfer1;
	private ImageView iv;
	private BluetoothAdapter BA;
	private ListView lv;
	private String fileName1;
	private static final String myDevice="My device: ";
	private static final String pairedDevicesAll="Paired device: ";   
////	private static final String fileName="tgr2.png";  
	
	private static final int intentON=11;
	private static final int intentVISIBLE=17;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	      getActiviesAll();
	      fileName1 = getIntent().getStringExtra("FILE");
	      updateActionBar(fileName1);

	      
		setContentView(R.layout.activity_blue_tooth_test1);
	      b1 = (Button) findViewById(R.id.button);
	      b2=(Button)findViewById(R.id.button2);

	      b4=(Button)findViewById(R.id.button4);
	      xfer1=(Button)findViewById(R.id.xfer1);
//	      textView1=(TextView)findViewById(R.id.textview);
	      iv = (ImageView) findViewById(R.id.imageView);

	      
	      xfer1.setOnClickListener(this);//xfer
	      b1.setOnClickListener(this);//on
	      b4.setOnClickListener(this);//off
	      b2.setOnClickListener(this);//off

	      lv = (ListView)findViewById(R.id.listView);
	      lv.setOnItemClickListener(new OnItemClickListener() {
	    	  
	    	    @Override
	    	    public void onItemClick(AdapterView<?> parent, View view,
	    	            int position, long id) {
	    		      Toast.makeText(getApplicationContext(), "qqqqqqqqq "+position ,Toast.LENGTH_LONG).show();
	    	    }
	    	});
	
	      BA = BluetoothAdapter.getDefaultAdapter();	      
	      if (BA==null){
		      Toast.makeText(getApplicationContext(), "BT not supported ",Toast.LENGTH_LONG).show();
	    	  finish();
	      }
	      else {  
if (BA.isEnabled()){	    	  
		      iv.setBackgroundResource(R.drawable.btblue);
	}
else {
    iv.setBackgroundResource(R.drawable.btblack);

}
  listClear();
//			 textView1.setText("BlueTooth File Transfer");
		}	
	}

/////////////////////////////////////////////////
	
	
	//////////////////////////////////////////////////////////
	




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.blue_tooth_test1, menu);
		return true;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {	
		super.onActivityResult(requestCode, resultCode, data);
	    if (requestCode == intentON ) {
	        if (resultCode == RESULT_OK) {
//	            final String result = data.getStringExtra(SecondActivity.Result_DATA);
	            Toast.makeText(this, "Result  on: ", Toast.LENGTH_LONG).show();
	        }
	    }
	
	    if (requestCode == intentVISIBLE ) {
//	        if (resultCode == RESULT_OK) {
//	            final String result = data.getStringExtra(SecondActivity.Result_DATA);
	            Toast.makeText(this, "Result  visible: ", Toast.LENGTH_LONG).show();
	            list();
//	        }
	    }
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	    

	@Override
	public void onClick(View v) {
		int id = v.getId();

		if (id==R.id.xfer1){
			xfer1();
		}
//		if (id==R.id.button3){
//			list();
//		}
		
		if (id==R.id.button){
			on();
		}		
		if (id==R.id.button4){
			off();
		}
		if (id==R.id.button2){
			visible();
		}
	}
	
	   private  void xfer1(){
	   Intent intent = new Intent();
	   intent.setAction(Intent.ACTION_SEND);
	   intent.setType("text/plain");	   
	  if (getActiviesAll()) intent.setPackage("com.android.bluetooth");//????	   
	   File file=new File(fileName1);
	   intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file) );
	   startActivity(intent);
	   }	 
	
	   private void list(){
		 ArrayList<BluetoothDevice> alb = new ArrayList<BluetoothDevice>();
		 Set<BluetoothDevice>	 pairedDevices = BA.getBondedDevices();
		 
		 if (pairedDevices.size()==0){
	         Toast.makeText(getApplicationContext(), "No Paired devices", Toast.LENGTH_LONG).show();
//finish();
		 }
		 else {		 
	     alb.addAll(pairedDevices);
	      ArrayList<String> list = new ArrayList<String>();
			 list.add(myDevice+BA.getName()+"\t\t"+BA.getAddress()+"\t\t"+BA.getState());
	      for(int i=0;i<alb.size();i++){
	    	  list.add(pairedDevicesAll+alb.get(i).getName()+" "+
	    			  alb.get(i).getAddress()+" / "+alb.get(i).getType());	    	  
	      }
	      Toast.makeText(getApplicationContext(), "Showing Paired Devices",Toast.LENGTH_SHORT).show();
//	      str=alb.get(0).getAddress();
	      final ArrayAdapter<String> adapter = 
	    		  new  ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
	      lv.setAdapter(adapter);
	   }
}
	
	   private void listClear(){
		      ArrayList<String> list = new ArrayList<String>();
		      list.add("No items");
		      final ArrayAdapter<String> adapter = 
		    		  new  ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
		      lv.setAdapter(adapter);
		   
		   
	   }
	   
	   
		  private void on(){
		      if (!BA.isEnabled()) {
		         Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
		         startActivityForResult(turnOn, intentON);
		         Toast.makeText(getApplicationContext(), "Turned on",Toast.LENGTH_LONG).show();
		      } else {
		         Toast.makeText(getApplicationContext(), "Already on", Toast.LENGTH_LONG).show();
		      }
		      iv.setBackgroundResource(R.drawable.btblue);

		   }
		  
		   private void off(){
			      BA.disable();
			      iv.setBackgroundResource(R.drawable.btblack);
			      listClear();
			      Toast.makeText(getApplicationContext(), "Turned off" ,Toast.LENGTH_LONG).show();
			   }

		   public  void visible(){
			   
			      Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
			      startActivityForResult(getVisible, intentVISIBLE);
			      iv.setBackgroundResource(R.drawable.btgreen);

			   }
		   
		   private boolean getActiviesAll(){
			   Intent intent = new Intent();
	           intent.setAction(Intent.ACTION_SEND);
	           intent.setType("*/*");
		   
		         PackageManager pm = getPackageManager();
		            List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);
		            String packageName = null;
		            String className = null;
		             boolean found = false;
		             	if (appsList.size() > 0) {
		             		for (ResolveInfo info : appsList) {
		             			packageName = info.activityInfo.packageName;
		             				if (packageName.equals("com.android.bluetooth")) {
		             					className = info.activityInfo.name;
		             					found = true;
		             					break;
		                    }
		                }
			   
		   }
					return found;
		   
		   
		   
		   }  
		   
			private void updateActionBar(String string) {
			      ActionBar actionBar = getActionBar();
			      actionBar.setSubtitle(string);
			      actionBar.setTitle("BT file Xfer");
			      actionBar.setIcon(R.drawable.btactionbar);		
			}
		   
			
			
			
			
			
			
			
}// end activity class
