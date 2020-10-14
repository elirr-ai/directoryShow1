package com.example.directoryshow1;

import java.util.List;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public class MSTGetSensors {

	public static String getSensors1(Context c)	{
		final String nl="\n";
		String SENSOR_SERVICE="sensor";
		StringBuilder sb=new StringBuilder();
	    SensorManager sm = null;  ;
	      sm = (SensorManager)c.getSystemService(SENSOR_SERVICE); 
	      
    int[] types = {Sensor.TYPE_ACCELEROMETER,
    					
    		Sensor.TYPE_AMBIENT_TEMPERATURE,
    		Sensor.TYPE_GEOMAGNETIC_ROTATION_VECTOR,
    		Sensor.TYPE_GRAVITY,
    		Sensor.TYPE_GYROSCOPE,
    		Sensor.TYPE_LIGHT,
    		Sensor.TYPE_LINEAR_ACCELERATION,
    		Sensor.TYPE_MAGNETIC_FIELD,
    		Sensor.TYPE_ORIENTATION,
    		Sensor.TYPE_PRESSURE,
    		Sensor.TYPE_PROXIMITY,
    		Sensor.TYPE_RELATIVE_HUMIDITY,
    		Sensor.TYPE_ROTATION_VECTOR,
    		Sensor.TYPE_STEP_COUNTER,
    		Sensor.TYPE_STEP_DETECTOR,
    		Sensor.TYPE_TEMPERATURE    		
    };
	      
	      

    for (int j=0;j<types.length;j++){
    	List<Sensor>    list = sm.getSensorList(types[j]);
	    if (list.size()>0){
	    	for (int i=0;i<list.size();i++){
	    		sb.append("\t\t\tSensor name: "+list.get(i).getName()+nl);
	    		sb.append("Sensor vendor: "+list.get(i).getVendor()+nl);
	    		sb.append("Sensor versionr: "+list.get(i).getVersion()+nl);
	    		sb.append("Sensor type: "+list.get(i).getType()+nl);
	    		sb.append("Sensor maxRange: "+list.get(i).getMaximumRange()+nl);
	    		sb.append("Sensor resolution: "+list.get(i).getResolution()+nl);
	    		sb.append("Sensor power: "+list.get(i).getPower()+nl);
	    		sb.append("Sensor mindelay: "+list.get(i).getMinDelay()+nl);
	    	}
	    sb.append(" -----------------------------------------------------------------------------------------------------  \n ");
	    }
	}    
	      
		return sb.toString();  	
	}	
}
//[{Sensor name="MPL Accelerometer", vendor="Invensense", version=1, 
//type=1, maxRange=19.6133, resolution=0.039226603, power=0.5, minDelay=5000}]