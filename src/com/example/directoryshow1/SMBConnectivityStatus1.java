package com.example.directoryshow1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class SMBConnectivityStatus1 {

	static String s="";
	private boolean[] bStatus =new boolean[4];
	private String[] sStatus=new String[4];
	Context c;
		
	public SMBConnectivityStatus1 (Context c){
	this.c=c;
		for (int i=0;i<bStatus.length;i++){
			bStatus[i]=false;
			sStatus[i]="";
					}	
	}
	
	
	
	public boolean[]   getBooleanConnectionStatus () {
	    
		ConnectivityManager connectivityMgr =
	    (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo wifi = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    NetworkInfo mobile = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    // Check if wifi or mobile network is available or not. If any of them is
	    // available or connected then it will return true, otherwise false;
	    if (wifi != null) {
	       if (wifi.isAvailable()) bStatus[0]=true; // wifi available
	       if (wifi.isConnected()) bStatus[1]=true; // wifi connected
	  	    }
	    if (mobile != null) {
	       if (mobile.isAvailable()) bStatus[2]=true; // mobile available
	       if (mobile.isConnected()) bStatus[3]=true; // mobile connected  
	    }
	    return bStatus;
	}

	public String[] getStringConnectionStatus () {
	    
		ConnectivityManager connectivityMgr =
	    (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo wifi = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	    NetworkInfo mobile = connectivityMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
	    // Check if wifi or mobile network is available or not. If any of them is
	    // available or connected then it will return true, otherwise false;
	    if (wifi != null) {
	       if (wifi.isAvailable()) sStatus[0]="wifi available"; // wifi available
	       if (wifi.isConnected()) sStatus[1]="wifi connected"; // wifi connected
	  	    }
	    if (mobile != null) {
	       if (mobile.isAvailable()) sStatus[2]="mobile available"; // mobile available
	       if (mobile.isConnected()) sStatus[3]="mobile connected"; // mobile connected  
	    }
	    return sStatus;
	}
	
	public void wifiConnect (){
		WifiManager wm =
			    (WifiManager) c.getSystemService(Context.WIFI_SERVICE);	
	
		   if (wm.isWifiEnabled() == false) {
//		if (bStatus[0] && !bStatus[1]) {  
		wm.setWifiEnabled(true);
        Toast.makeText((c), "wifi is disabled... making it enabled", 
        Toast.LENGTH_LONG).show();
	        }
		
	//	return wm.getWifiState();
	}
	
	public String[] getWifiConnectionproperties () {
	String[] w=new String[7];
	String ipii="",ipni="";
	
		WifiManager wm =
			    (WifiManager) c.getSystemService(Context.WIFI_SERVICE);	
	if (wm.isWifiEnabled()==true){
		WifiInfo wifiinfo=wm.getConnectionInfo();
		w[0]="\nBSSID= "+wifiinfo.getBSSID();
		w[1]="\nMAC ADDRESS= "+wifiinfo.getMacAddress();
		w[2]="\nSSID= "+wifiinfo.getSSID();
		             
		int ip=wifiinfo.getIpAddress();
//String hhgggg=(( ip & 0xFF)+ "."+
//		((ip >> 8 ) & 0xFF)+"."+
//		((ip >> 16 ) & 0xFF)+"."+
//		((ip >> 24 ) & 0xFF));
		
		
	w[3]="\nIP ADDRESS= "+(( ip & 0xFF)+ "."+
			((ip >> 8 ) & 0xFF)+"."+
			((ip >> 16 ) & 0xFF)+"."+
			((ip >> 24 ) & 0xFF));	
	w[4]="\nLINK SPEED= "+String.valueOf(wifiinfo.getLinkSpeed());	
	w[5]="\nNETWORK ID= "+String.valueOf(wifiinfo.getNetworkId());	
	w[6]="\nRSSI= "+String.valueOf(wifiinfo.getRssi());
showAlertDialog(w);
	}
		
		
		return w;
		
	}	
	
	private void showAlertDialog (String[] w){
	    String sts="";
	    for (int i=0;i<w.length;i++) {
	    	sts+=w[i];
	    }
	    String sts1="";
	    for (int i=0;i<sStatus.length;i++) {
	    	sts1+="\n"+sStatus[i];
	    }    
		AlertDialog.Builder builder1 = new AlertDialog.Builder(c);
		builder1.setTitle("Nwtwork Connections");
		builder1.setMessage(sts1+"\n"+sts);
		builder1.setCancelable(true);

		builder1.setNeutralButton(
		    "OK",
		    new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int id) {
		            dialog.cancel();
		        }
		    });



		AlertDialog alert11 = builder1.create();
		alert11.show();
		
		
		
		
		
		
	}
	
	
	
	
	
	
	
}
