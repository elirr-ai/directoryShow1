package com.example.directoryshow1;

import java.util.ArrayList;
import java.util.Locale;
import android.app.Activity;

public class MSTGetLocale1 {

	static final String nl="\n",hdr="System Locale# ";
	
	static ArrayList<String>  getLocale(Activity a)	{
		ArrayList<String> al = new ArrayList<String>();
	    Locale l=a.getResources().getConfiguration().locale;
	    if (l!=null){
	al.add("Country: "+l.getCountry()+"\n");
	al.add("Disaply Country: "+l.getDisplayCountry()+"\n");
	al.add("Display Language: "+l.getDisplayLanguage()+"\n");
	al.add("Display Name: "+l.getDisplayName()+"\n");
	al.add("Display Variant: "+l.getDisplayVariant()+"\n");
	al.add("ISO3 Country:  "+l.getISO3Country()+"\n");
	al.add("ISO3 Language: "+l.getISO3Language()+"\n");
	al.add("get Language: "+l.getLanguage()+"\n");
	al.add("GetVariant: "+l.getVariant()+"\n");
	al.add("\n-----------------------  Avaliable Locals  ------------------------\n");
	Locale[] ls=Locale.getAvailableLocales();

	for (int i=0;i<ls.length;i++){
		al.add(hdr+String.valueOf(i)+"  "+ls[i].toString()+nl);
	}

	    }
	   return al;
	}	
	
	
	public static String convArray2String (ArrayList<String> alc ){
		StringBuilder sbb=new StringBuilder();
		for (int i=0;i<alc.size();i++){
			sbb.append(alc.get(i));
		}
		return sbb.toString();
			}	
	
}
