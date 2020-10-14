package com.example.directoryshow1;

import java.util.Locale;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Environment;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by EliR on 7/11/2020.
 */
public final class  Button_listenerNotifPlayer1 extends BroadcastReceiver {  // added final
public static 	MediaPlayer mp=null;
//public static 	MediaRecorder mediaRecorder ;
public static int counterOps=0;
private int err=0;
private Context ctx=null;
private static String AudioPlayPath=Environment.getExternalStorageDirectory().getPath()+"/g/Kalimba.mp3";
private static long startRecTimeLong;
private static boolean pauseFlag=false;
private static int seekTo;
private static String nameSong,artistSong;
private static final String ZERO = "0";
private  Handler timerHandler = new Handler();


    @Override
    public void onReceive(Context context, Intent intent) {
    	this.ctx=context;      
        String sid=""+intent.getExtras().getInt("id");
        String key=intent.getExtras().getString("key");
        AudioPlayPath=intent.getExtras().getString("file"); 
        nameSong=intent.getExtras().getString("songname");
        artistSong=intent.getExtras().getString("songartist");
        
            Toast.makeText(context, "ButtonPPP  "+sid+" "+key, Toast.LENGTH_SHORT).show();
            if (key.equals("a")){
            	counterOps++;           
            	if (counterOps==1){ 
//                   pauseFlag=false;
                    mp=new MediaPlayer();                
                    
                    mp.setOnCompletionListener(new OnCompletionListener() {
						
						@Override
						public void onCompletion(MediaPlayer mp) {
				      	counterOps=0;   
					    	pauseFlag=false;	
//					        Toast.makeText(ctx, "Ended...  ", Toast.LENGTH_SHORT).show();
			            	sendNotification("Song ended",AudioPlayPath,
			            			makeTimeString((int)(System.currentTimeMillis()-startRecTimeLong)),
			            			R.drawable.iconstop);
							
						}
					});
                 // When song is ended then media player automatically called onCompletion method.
                    mp.setOnErrorListener(new OnErrorListener() {						
						@Override
						public boolean onError(MediaPlayer mp, int what, int extra) {
			            	sendNotification("Error in Song play",AudioPlayPath,
			            			makeTimeString((int)(System.currentTimeMillis()-startRecTimeLong)),
			            			R.drawable.iconplayerror);							
							return false;
						}
					});
                     
                     
/*                    mp.setOnCompletionListener(this);
                 // When song is ended then media player automatically called onCompletion method.
                     public void onCompletion(MediaPlayer arg0) 
                 {
                     // Write your code
                 }
*/                    
                    try{  
                   	mp.setDataSource(AudioPlayPath);
                    mp.prepare();  
                    }catch(Exception e){e.printStackTrace();
                    }  
            		startRecTimeLong=System.currentTimeMillis();	
            		try {
                    if (pauseFlag) {//  paused
                    	pauseFlag=false;
                    	mp.seekTo(seekTo);            	
                 	   }
					mp.start(); 
					timerHandler.postDelayed(timerRunnable, 0);
				} catch (Exception e) {
					e.printStackTrace();
				}          	
                	sendNotification("Start Play",AudioPlayPath,
                			makeTimeString(mp.getDuration()),
                			R.drawable.iconplay);          	
            	}       
            }
            
            else   if (key.equals("b")){  // stop
	            timerHandler.removeCallbacks(timerRunnable);
            	counterOps=0;   
            	pauseFlag=false;
            	try {	
            		mp.stop();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
            	sendNotification("Stop play",AudioPlayPath,
            			makeTimeString(mp.getDuration()),
            			R.drawable.iconstop);
            		}     
            
            else if (key.equals("d")) {// pause
	            timerHandler.removeCallbacks(timerRunnable);
            	if (!pauseFlag){
            	pauseFlag=true;
            	counterOps=0;
            	try {
            		seekTo=	mp.getCurrentPosition();
            		mp.pause();
            	} catch (IllegalStateException e) {
            		e.printStackTrace();
            	}
            	sendNotification("Pause play",AudioPlayPath,
            			makeTimeString(mp.getDuration()),
            			R.drawable.iconpause);
            	
            	Toast.makeText(context, "pause   Player...  "+sid+" "+key, Toast.LENGTH_SHORT).show();
//                  NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//                  manager.cancelAll();     	
            }           
    }     
            else if (key.equals("c")) {  // exit
            	timerHandler.removeCallbacks(timerRunnable);
               	counterOps=0;   
            	pauseFlag=false;
            	try {
					mp.stop();
					mp.release();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
            	catch (Exception e) {
					e.printStackTrace();
				}
            	
            	Toast.makeText(context, "Exiting Player...  "+sid+" "+key, Toast.LENGTH_SHORT).show();
                  NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                  manager.cancelAll();     	
            }
            
 
            
    }
    
    private void initPlayer() {
        mp=new MediaPlayer(); 
        try{  
        mp.setDataSource(AudioPlayPath);                        
        mp.prepare();  
        }catch(Exception e){
        	e.printStackTrace();
        	}		
	}

	public void sendNotification(String strTitle,String strContent,String timeDuration, int icn) {
    	String s1="ERR";
    	String s[]=strContent.split("/");
    		if (s.length>0) s1=s[s.length-1];
        //Get an instance of NotificationManager//
//    	String diffStr10=dateFrom1970.toDateStrfromStringdayShort(timeDuration);
String str8=nameSong;
if (str8.equals("----") )  str8=s1;
        Notification.Builder mBuilder =
            new Notification.Builder(ctx)
            .setSmallIcon(icn)
            .setContentTitle(str8)
    //        .setSubText(timeDuration)
            .setSubText(makeTimeString(mp.getCurrentPosition()))
            .setContentText(artistSong);//  s1 file name
        
        // Gets an instance of the NotificationManager service//
        NotificationManager mNotificationManager =
            (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        // When you issue multiple notifications about the same type of event,
        // it’s best practice for your app to try to update an existing notification
        // with this new information, rather than immediately creating a new notification.
        // If you want to update this notification at a later date, you need to assign it an ID.
        // You can then use this ID whenever you issue a subsequent notification.
        // If the previous notification is still visible, the system will update this existing notification,
        // rather than create a new one. In this example, the notification’s ID is 001//

        //////NotificationManager.notify().

        mNotificationManager.notify(001, mBuilder.build());
    }
    
	  private void mediaReady(){
//	  AudioPlayPath = 
//	                Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + 
//	                        "REC_" +dateFrom1970.toDateStr()+ ".3gp";

//	      mediaRecorder=new MediaRecorder();
//	      mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//	      mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//	      mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
//	      mediaRecorder.setOutputFile(AudioPlayPath);
//	      mediaRecorder.setAudioChannels(2);	      
//	      mediaRecorder.setMaxDuration(1000*60*60);	      

//	      mediaRecorder.setAudioSamplingRate(8000);
//	      mediaRecorder.setAudioEncodingBitRate(8000);
	   }
	
//	  private void mediaRecorderStart(){
//          try {   
//        	  err=0;
//              mediaRecorder.prepare();
//              mediaRecorder.start();              
//           } catch (IllegalStateException e) {
//          	  e.printStackTrace();err=1;
//           } catch (IOException e) {
//          	 e.printStackTrace();err+=10;
//           }catch (Exception e) {
//               e.printStackTrace();err+=100;
//            }  
//          finally {
//        	  if (err!=0){
//        	  Toast.makeText(ctx, "err "+err, Toast.LENGTH_LONG).show();
//        	  }
//          }
//	  }
	
	  private String makeTimeString(int i1000){
        int s100=i1000%1000;
      	int sec1=i1000/1000;
      	int sec=sec1%60;
      	int min=sec1/60;
      	int hour =(i1000/ (60*60*1000) )    ;
      	String secLead="";
      	if (sec<10) secLead=ZERO;
      		String minLead="";
      	if (min<10) minLead=ZERO;
         	String hourLead="";
      	if (hour<10) hourLead=ZERO;
 	
    	return "Duration: " +hourLead+hour+":"+minLead+min+":"+secLead+sec+":"+s100;
		  
	  }

	  
	    Runnable timerRunnable = new Runnable() {

	        @Override
	        public void run() {
	            long millis = System.currentTimeMillis() - startRecTimeLong;

		if (counterOps>0) {    	
	    	sendNotification("Playing...",AudioPlayPath,makeTimeString((int)millis),R.drawable.iconplay);      
	    	timerHandler.postDelayed(timerRunnable, 1000);
    	}  
 	
    else {
    	timerHandler.removeCallbacks(timerRunnable);
    }
    
//	            timerTextView.setText(String.format("%d:%02d", minutes, seconds));
//	            if (mp.isPlaying())  timerHandler.postDelayed(this, 1000);
	            
	        }
	    };
	    
	  
	  
	  
	  
//	@Override
//	public void onCompletion(MediaPlayer mp) {
//      	counterOps=0;   
//    	pauseFlag=false;	
//        Toast.makeText(ctx, "Ended...  ", Toast.LENGTH_SHORT).show();
//
//	}
    
}
