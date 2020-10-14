package com.example.directoryshow1;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class MSTMemoryStatus1MainActivity extends Activity {

	private static final String CRLF="\n\r";
  ArrayList<String> al1 = new ArrayList<String>();
	public Activity act=this;
	private TextView tv;
	private Menu mMenu;
	Context context=this;
	ProgressDialog p;	
	StringBuilder sbbbbb=new StringBuilder();
	int function=0;
	String batteryRecievedString=null;
	String wifiAPsReciveString=null;
	String myWIFIStatus= null;
	
	WifiManager  wifiManager;
	   ConnectivityManager connectivityManager;
	    NetworkInfo mobileInfo;
	  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mstactivity_memory_status1_main);
		tv=(TextView)findViewById(R.id.tv);


		
        AsyncTaskExample asyncTask=new AsyncTaskExample();
        asyncTask.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mstmemory_status1_main, menu);
		mMenu=menu;
		setActionIconFunct();
		return true;
	}

	private void setActionIconFunct()
	{
	 
	    MenuItem itemf = mMenu.findItem(R.id.fselect);
	    if(mMenu != null)
	    {
	        if (function==0) itemf.setIcon(R.drawable.memory);
	        if (function==1) itemf.setIcon(R.drawable.language);
	        if (function==2) itemf.setIcon(R.drawable.software);
	        if (function==3) itemf.setIcon(R.drawable.battery);
	        if (function==4) itemf.setIcon(R.drawable.camera1);
	        if (function==5) itemf.setIcon(R.drawable.cpu);
	        if (function==6) itemf.setIcon(R.drawable.sensor2);
	        if (function==7) itemf.setIcon(R.drawable.display1);
	        if (function==8) itemf.setIcon(R.drawable.vib1);
	        if (function==9) itemf.setIcon(R.drawable.gps1);
	        if (function==10) itemf.setIcon(R.drawable.ap72x72);
	        if (function==11) itemf.setIcon(R.drawable.mywifi);
	        	    }

	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();

		if (id == R.id.refresh) {
			setActionIconFunct();
	        AsyncTaskExample asyncTask=new AsyncTaskExample();
	        asyncTask.execute();
			return true;
		}		
		
		if (id == R.id.savefile) {
			MSTFileWriteString.setFileString("HW_LOG","DATA.TXT",prepFile());
			return true;
		}	
		
		
		
		if (id == R.id.fselect) {
			function++;
			if (function==12) function=0;
			setActionIconFunct();
			if (function==0 && al1!=null)	tv.setText(al1.get(0));
			if (function==1 && al1!=null) 	tv.setText(al1.get(1));
			if (function==2 && al1!=null) 	tv.setText(al1.get(2));	
			if (function==3 )	tv.setText(batteryRecievedString); // battery
			if (function==4&& al1!=null) 	tv.setText(al1.get(3));	
			if (function==5&& al1!=null) 	tv.setText(al1.get(4));	
			if (function==6&& al1!=null) 	tv.setText(al1.get(5));		
			if (function==7&& al1!=null) 	tv.setText(al1.get(6));		
			if (function==8&& al1!=null) 	tv.setText(al1.get(7));		
			if (function==9&& al1!=null) 	tv.setText(al1.get(8));		
			if (function==10&& al1!=null) 	tv.setText(wifiAPsReciveString);	//////  AP's
			if (function==11&& myWIFIStatus!=null) 	tv.setText(myWIFIStatus);	

			return true;
		}	
		
		
		return super.onOptionsItemSelected(item);
	}
	


	public void onPause() {
		    super.onPause();

//		    if (batteryInfoReceiver!= null) unregisterReceiver(batteryInfoReceiver);
		  }
	
	 public void onStop() {
		    super.onStop();

//		    if (batteryInfoReceiver!= null) unregisterReceiver(batteryInfoReceiver);
		  }
	 
	
	BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		 
		@Override
		public void onReceive(Context context, Intent intent) {
				batteryRecievedString=null;
				batteryRecievedString=MSTGetBatteryStatus.convArray2String(MSTGetBatteryStatus.getBattery(context, intent));
				if (batteryRecievedString==null)
					batteryRecievedString="BATTERY STRING NOT READY !!!!!!!!";
		      
				if (batteryInfoReceiver!= null) unregisterReceiver(batteryInfoReceiver);
		     }
		   };
	
	
//  doinbackground starts
		   
		   private class AsyncTaskExample extends AsyncTask<Void, Void, ArrayList<String>> {
			      @Override
			      protected void onPreExecute() {
			         super.onPreExecute();
			         p = new ProgressDialog(MSTMemoryStatus1MainActivity.this);
			         p.setMessage("Please wait..... Collecting data");
			         p.setIndeterminate(false);
			         p.setCancelable(false);
			         p.show();
			      }
			      @Override
			      protected ArrayList<String> doInBackground(Void... params) {
			    	  al1.clear();
			    	  registerReceiver(MSTMemoryStatus1MainActivity.this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
			    	  
  wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

				    if (!wifiManager.isWifiEnabled()) {
			      wifiManager.setWifiEnabled(true);
				    }
			  	    registerReceiver(wifiReceiver,
			  	    		new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

			  	    wifiManager.startScan();
			    	  
			    	  
	  
			    	  
			    	  
			    	  String s1000=MSTGetMemory.getVerboseRAM();
			    	  String s999=MSTGetMemory.getMemorySpecs(act);
			    	  al1.add(s999+"\n\n"+s1000 );  //0=mem
			    	  
			    	  al1.add(MSTGetLocale1.convArray2String(MSTGetLocale1.getLocale(act)));  //1=language
			    	  al1.add(MSTGetSoftwareBuildData.convArray2String(MSTGetSoftwareBuildData.getBuild()));//2-SW
			    	  al1.add(MSTgetCameraParams.getCameraParameters());//4-camera
			    	  al1.add(MSTGetCPUID.getCPUInfo());//5 -cpu
						al1.add(MSTGetSensors.getSensors1(context));	//6-sensor
						al1.add(MSTGetDisplay.getDisplay1(context, act));	//7-disp	
						al1.add(MSTGetVibrator.getVibrator1(context));//8-vib		
						al1.add(MSTGetGPS.getGPSPrividers(context));//9-GPS		

						myWIFIStatus=getMyWIFI();
			         return al1;
			      }
			      @Override
			      protected void onPostExecute(ArrayList<String>  al22) {
			         super.onPostExecute(al22);
//Toast.makeText(context, myWIFIStatus,  Toast.LENGTH_SHORT).show();    

			         if(al22!=null) {
			            p.hide();
//						Toast.makeText(context,al2.get(0)+" "+al2.size(),  Toast.LENGTH_SHORT).show();    
						if (function==0 && al1!=null)	tv.setText(al1.get(0));
						if (function==1 && al1!=null) 	tv.setText(al1.get(1));
						if (function==2 && al1!=null) 	tv.setText(al1.get(2));	
						
//						if (function==3) registerReceiver(this.batteryInfoReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
						if (function==4&& al1!=null) 	tv.setText(al1.get(3));	
						if (function==5&& al1!=null) 	tv.setText(al1.get(4));	
						if (function==6&& al1!=null) 	tv.setText(al1.get(5));		
						if (function==7&& al1!=null) 	tv.setText(al1.get(6));		
						if (function==8&& al1!=null) 	tv.setText(al1.get(7));	
						if (function==9&& al1!=null) 	tv.setText(al1.get(8)	);	
						
			         }else {
			            p.show();
			         }
			      }
			   }
		   
// do in back....  ends...		   

			 private String prepFile() {
				 String nl="\n";
				 StringBuilder sb=new StringBuilder();
				 for (int i=0;i<al1.size();i++){
					 sb.append(al1.get(i)+nl);
				 }
				 
					return sb.toString();
				}
		   

			  BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
				    @Override
				    public void onReceive(Context context, Intent intent) {
				    	ArrayList<String> arrayList = new ArrayList<String>();
						wifiAPsReciveString="APS STRING NOT READY !!!!!!!!";
						List<ScanResult> results = wifiManager.getScanResults();
						unregisterReceiver(this);
						for (ScanResult scanResult : results) {
				    	  StringBuilder sb=new StringBuilder();
				    	  sb.append("SSID  "+scanResult.SSID+CRLF);
				    	  sb.append("Capabilities  "+scanResult.capabilities+CRLF);
				    	  sb.append("BSSID  "+scanResult.BSSID+CRLF);
				    	  sb.append("Frequency  "+scanResult.frequency+" Mhz"+CRLF);
				    	  sb.append("Level  "+scanResult.level+" dBM"+CRLF);
				    	  sb.append("Timestamp  "+scanResult.timestamp+" mSec"+CRLF);
				        arrayList.add(sb.toString());
				       
				      }
				      StringBuilder sb1 = new StringBuilder();
				      for (int i=0;i<arrayList.size();i++){
				    	  sb1.append(""+(i+1)+""+"/"+arrayList.size()+"  ----------------------------------------------"+CRLF);
				    	  sb1.append(arrayList.get(i)+CRLF);
				      }
				      wifiAPsReciveString=sb1.toString();
				    }
				  };
			 
					public String getMyWIFI (){
						
						WifiManager mainWifi;
	mainWifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
						WifiInfo mWifiInfo = mainWifi.getConnectionInfo();
				        StringBuilder sb=new StringBuilder();
				        
						sb.append("IP  "+intToIP(mWifiInfo.getIpAddress())+CRLF);
						sb.append("BSSID  "+mWifiInfo.getBSSID()+CRLF);
						sb.append("SSID  "+mWifiInfo.getSSID()+CRLF);
						sb.append("Link speed  "+mWifiInfo.getLinkSpeed()+CRLF);
						sb.append("MAC address  "+mWifiInfo.getMacAddress()+CRLF);
						sb.append("Rssi  "+mWifiInfo.getRssi()+CRLF);
						sb.append("NWK ID  "+mWifiInfo.getNetworkId()+CRLF);
						sb.append("Hidden SSID  "+mWifiInfo.getHiddenSSID()+CRLF);
				        return sb.toString();
					}
					
					public String intToIP(int i) {
					       return (( i & 0xFF)+ "."+((i >> 8 ) & 0xFF)+
					                          "."+((i >> 16 ) & 0xFF)+"."+((i >> 24 ) & 0xFF));
					} 	  
				  
				  
}


/*
onDestroy() is not guaranteed to be called. When you start Activity from Activty, onPause() and onStop() method called instead of onDestroy().

onDestroy() calls when you hit back button or call finish() method.

Hence, broadcast receiver should be registered in onStart() or onResume(), and unregister receiver in onPause() or onStop().

*/



/*
 
 
     public static Map<String, String> getCPUInfo () throws IOException {

        BufferedReader br = new BufferedReader (new FileReader ("/proc/cpuinfo"));

        String str;

        Map<String, String> output = new HashMap<> ();

        while ((str = br.readLine ()) != null) {

            String[] data = str.split (":");

            if (data.length > 1) {

                String key = data[0].trim ().replace (" ", "_");
                if (key.equals ("model_name")) key = "cpu_model";

                String value = data[1].trim ();

                if (key.equals ("cpu_model"))
                  value = value.replaceAll ("\\s+", " ");

                output.put (key, value);

            }

        }

        br.close ();

        return output;

    }

  
 */
