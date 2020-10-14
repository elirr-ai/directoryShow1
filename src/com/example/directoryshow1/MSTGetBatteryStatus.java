package com.example.directoryshow1;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;

public class MSTGetBatteryStatus {

	public static ArrayList<String> getBattery(Context c, Intent intent){
	      int health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
	      int level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
	      int plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
	      boolean present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
	      int status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
	      String technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
	      int temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
	      String tmp=String.valueOf(temperature);

	      int voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
	      
	      ArrayList<String> al= new ArrayList<String>();
		al.add("Health: "+health+"\n");
		al.add("Level: "+level+"\n");
		al.add("Plugged: "+plugged+"\n");
		al.add("present: "+present+"\n");		
		al.add("Status: "+status+"\n");
		al.add("Technology: "+technology+"\n");
		al.add("Temperature: "+tmp.substring(0,tmp.length()-1)+
				"."+tmp.substring(tmp.length()-1,tmp.length())+" Celcius\n");
		al.add("Voltage: "+voltage+" mVolts\n");		

		return al;
		
	}

	public static String convArray2String (ArrayList<String> alc ){
		StringBuilder sbb=new StringBuilder();
		for (int i=0;i<alc.size();i++){
			sbb.append(alc.get(i)+"");
		}
		return sbb.toString();
			}	
	
	
	
	
}
