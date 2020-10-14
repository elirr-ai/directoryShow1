package com.example.directoryshow1;

import android.content.Context;
import android.os.Vibrator;

public class MSTGetVibrator {

	public static String  getVibrator1(Context c){
		Vibrator vibrator = (Vibrator) c.getSystemService(Context.VIBRATOR_SERVICE);
		return "Has Vibrator: "+vibrator.hasVibrator()+"\n";
	}
	
	
}
