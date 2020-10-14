package com.example.directoryshow1;

import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;

public class MyServiceMP3list extends Service {
	MediaPlayer player;
	String[] songArray;
	int ctr=0,max=0;
	int events=0;
	Context ctx=this;
	   public static final String MyPREFERENCES = "MyPrefs" ;
	   public static final String SONGLIST = "SONGLIST";
	   public static final String SONGID = "SONGID";
	   public static final String SERVICEACK = "SERVICEACK";
	   
	   int songID;
//	   private boolean flag=false;
	   SharedPreferences sharedpreferences;

	   
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
//		   MyButton(MyListener ml) {
		        //Setting the listener
//		        this.ml = ml;
//		    }
	     sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	     songID=sharedpreferences.getInt(SONGID, 0);
	     String s10=sharedpreferences.getString(SONGLIST, "");
	     songArray=s10.split(",");
	     max=songArray.length;
	     
//Toast.makeText(this, "MyService= \n"+s10+"\n"+songID, Toast.LENGTH_LONG).show();
  initPlayer(); 	     
/*	     
	       player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	            @Override
	            public void onCompletion(MediaPlayer mediaPlayer) {
	            	ctr++;
	            	if (ctr>max-1){
	            		         player.stop();
//	            		         player.release();
	            	}
	            	else {
	           	     try {
       	    	 player=new MediaPlayer();
	        			player.setDataSource(songArray[ctr]);
//////	        			player.prepare();
//	        			player.setLooping(false); // Set looping
//	        			player.setVolume((float) (0.53),(float) (0.53)); // Set Volume
	        			player.prepare();
	        			player.start();
	        		} catch (IllegalArgumentException e) {
	        			e.printStackTrace();
	        		} catch (SecurityException e) {
	        			e.printStackTrace();
	        		} catch (IllegalStateException e) {
	        			e.printStackTrace();
	        		} catch (IOException e) {
	        			e.printStackTrace();
	        		}
	        	     catch (Exception e) {
	        				
	        				e.printStackTrace();
	        			}
	            	}
	       
	            }
	        });
*/

	     
	}

	@Override
	public void onDestroy() {
//		Toast.makeText(this, "My Service Stopped "+System.currentTimeMillis(), Toast.LENGTH_SHORT).show();
//        SharedPreferences.Editor editor = sharedpreferences.edit();	 
//    	editor.putString(SERVICEACK, "SERVICEX  !!  "+System.currentTimeMillis()).commit();         	          	

		if (player!=null){
//			flag=true;
//			player.stop();
//			player.release();
		}
	}
	
	@Override
	public void onStart(Intent intent, int startid) {
		
		player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			
	
			@Override
			public void onPrepared(MediaPlayer mp) {
				player.start();
			}
		});
		
	       player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	            @Override
	            public void onCompletion(MediaPlayer mediaPlayer) { 
	    	        SharedPreferences.Editor editor = sharedpreferences.edit();	 
//	            	editor.putString(SERVICEACK, "SERVICEX!!  "+(++events)).commit();    
	       	     songID=1+sharedpreferences.getInt(SONGID, 0);
	            	editor.putInt(SONGID, songID).commit();   
	            	songID=sharedpreferences.getInt(SONGID,0);
	            	int h=songID;
if (songID<songArray.length){
	 try {
		 player.stop();
		player.setDataSource(songArray[songID]);
		player.prepare();
	} catch (IllegalArgumentException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalStateException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
//		player.start();
}
else {
	            	
	            				player.stop();
	            		         player.release();}
//	            		         stopSelf();
	            		//         Toast.makeText(this, "MyService= \n", Toast.LENGTH_LONG).show();
//  Toast.makeText(MyService.this, "You are doing this in the right order!", Toast.LENGTH_LONG).show();
	            	}
	        });	          
//		initPlayer();
		player.start();
	}
	
	
	private void initPlayer(){
	     try {
	    	 player=new MediaPlayer();
	    	 if (songID>-1 && songID<songArray.length){
	    		 player.setDataSource(songArray[songID]);
	    	 }
//			player.setDataSource(songArray[ctr]);
//////			player.prepare();
			player.setLooping(false); // Set looping
			player.setVolume((float) (0.8),(float) (0.8)); // Set Volume
			player.prepare();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	     catch (Exception e) {				
				e.printStackTrace();
			}

		
	}
}
