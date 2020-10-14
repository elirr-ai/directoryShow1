package com.example.directoryshow1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

//import org.apache.commons.net.time.TimeTCPClient;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class SMBGetFromInternetSiteLyrics1 {

	public static String getText (String site){
		String line1="";
		HttpsURLConnection urlConnection;
		StringBuilder stringBuilder1=new StringBuilder();
		try{	
		    URL url1=new  URL(site);
		    urlConnection=(HttpsURLConnection) url1.openConnection();
BufferedReader reader1=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
		   while ((line1=reader1.readLine()) !=null){ stringBuilder1.append(line1 ); }  
		 }catch (Exception e){
		    e.printStackTrace();
		 }	
		urlConnection=null;
		return stringBuilder1.toString();
	}




public static String getTime12 (){
	String dateStr = "";
	HttpResponse response = null;
try{
    HttpClient httpclient = new DefaultHttpClient();
   response = httpclient.execute(new HttpGet("https://google.com/"));
    StatusLine statusLine = response.getStatusLine();
    Header[] h=response.getAllHeaders();
    if(statusLine.getStatusCode() == HttpStatus.SC_OK){
        dateStr = response.getFirstHeader("Date").getValue();//Here I do something with the Date String
    } else{
        //Closes the connection.
//        response.getEntity().getContent().close();
//        throw new IOException(statusLine.getReasonPhrase());
    }
}catch (ClientProtocolException e) {
    Log.d("Response", e.getMessage());
}catch (IOException e) {
    Log.d("Response", e.getMessage());
}

try {
	response.getEntity().getContent().close();
} catch (IllegalStateException e) {
	e.printStackTrace();
} catch (IOException e) {
	e.printStackTrace();
}
return dateStr ;
	}



/*
 * 
 * 
 * 
 https://www.abbreviations.com/services/v2/lyrics.php?uid=7414&tokenid=5kKEWwAWR5owfCyq&term=forever%20young&format=json
 
 * 
 * 
 * 
 */

public static String getLyrics (String s99){
	String response = "";
 
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(s99);
        try {
            HttpResponse execute = client.execute(httpGet);
            InputStream content = execute.getEntity().getContent();

            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(content));
            String s = "";
            while ((s = buffer.readLine()) != null) {
                response += s;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
return response ;
	}



}
