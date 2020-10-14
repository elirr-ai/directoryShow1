package com.example.directoryshow1;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebHistoryItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MP3SongListCreate extends Activity implements View.OnClickListener,
OnSharedPreferenceChangeListener, MediaPlayer.OnCompletionListener,
MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnSeekCompleteListener,
MediaPlayer.OnInfoListener, MediaPlayer.OnBufferingUpdateListener{

	private ArrayList<String> mp3Alist =new ArrayList<String>();
	private TextView tv;
	private ListView lv;
	private Button bup,bdown,bdelete,bbrowse;
	private Button bstart,bstop,bpause;
	
	private ArrayAdapter<MediaDataRetriever1MP3list> adapter;
	private int selectedposition=0;
	private int controlPlay=0; // 0 dont play, 1 play
	   public static final String MyPREFERENCES = "MyPrefs" ;
	   public static final String SONGLIST = "SONGLIST";
	   public static final String SONGID = "SONGID";
	   public static final String TEST = "TEST";
	   public static final String SERVICEACK = "SERVICEACK";
	   public static final String EXPORTFOLDER = "MP3_EXPORTFOLDER";
	   
		final static int mHeight_=120;
		final static int mWidth_=120;  
    	boolean flag=false;
    	boolean firstTimeresume=true;
 //////////   	int dispatch=0;
    	int elapsedTime=0;
    	boolean pauseFlag=false;
    	
	   SharedPreferences sharedpreferences;
	   MediaPlayer   mediaPlayer;
	    private Handler customHandler1 = new Handler();
	    
	    String elapsedTimeSong="";	    
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	     sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
	     sharedpreferences.registerOnSharedPreferenceChangeListener(this);

		setContentView(R.layout.activity_mp3_song_list_create);
		bup =(Button)findViewById(R.id.bup);
		bdown =(Button)findViewById(R.id.bdown);
		bdelete =(Button)findViewById(R.id.bdelete);
		bbrowse =(Button)findViewById(R.id.bbrowse);

		bstart =(Button)findViewById(R.id.bstart);
		bstop =(Button)findViewById(R.id.bstop);
		bpause =(Button)findViewById(R.id.bpause);

		
		bup.setOnClickListener(this);
		bdown.setOnClickListener(this);
		bdelete.setOnClickListener(this);
		bbrowse.setOnClickListener(this);

		bstart.setOnClickListener(this);
		bstop.setOnClickListener(this);
		bpause.setOnClickListener(this);

		bbrowse.setBackgroundColor(Color.parseColor("#FFFACD"));
		
		tv=(TextView)findViewById(R.id.tv1);
		lv=(ListView)findViewById(R.id.lv1);
		
		String s=sharedpreferences.getString(SONGLIST, "");
		String s2[]=s.split(",");
		for (int i=0;i<s2.length;i++){
			mp3Alist.add(s2[i]);
		}
		tv.setText(s);
	            updatePosition(); 
	            AsyncTaskMediaRetriever asyncTask=new AsyncTaskMediaRetriever();
	            asyncTask.execute();
	            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	            	 
	                @Override
	                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	                	selectedposition=position;
	                    updatePosition();
	                    checkTopButtons();
	                    adapter.notifyDataSetInvalidated();	
	                	}
	                });   
	            checkTopButtons();
	            checkLowButtons(0);
//	   initMediaPlayer();     	            
//	    		boolean b=checkAllFilesExist();        		
	}

	
	
	





	@Override
	protected void onResume() {//  ???????????????
		controlPlay=0;
		initMediaPlayer();       
		super.onResume();
	}

	@Override
	protected void onPause() {
		customHandler1.removeCallbacks(updateTimerThread1);
		flag=false;
		selectedposition=0;
		controlPlay=0; // 0 dont play, 1 play
		initMediaPlayer();
//		mediaPlayer.stop();
		adapter.notifyDataSetChanged();
		super.onPause();
	}


	
	
	


	@Override
	public void onBackPressed() {
		if (mediaPlayer!=null){
			if (mediaPlayer.isPlaying()){
				mediaPlayer.stop();
				mediaPlayer.release();
			}
			else {
				mediaPlayer.release();
			}
		
		}
		finish();
		super.onBackPressed();
	}









	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mp3_song_list_create, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		if (id == R.id.clear) {
            mp3Alist.clear();
            selectedposition=-1;
            updatePosition();  
            AsyncTaskMediaRetriever asyncTask=new AsyncTaskMediaRetriever();
            asyncTask.execute();	
			return true;
		}
		
		if (id==R.id.musicon){
			if (mediaPlayer!=null){
				if (!mediaPlayer.isPlaying()) {
					if (controlPlay==0 && !mediaPlayer.isPlaying()) controlPlay=1;
					customHandler1.postDelayed(updateTimerThread1, 0);
//					initMediaPlayer();

		    	}
			}
			return true;
		}
		if (id==R.id.musicoff){
			flag=false;
			selectedposition=0;
			controlPlay=0; // 0 dont play, 1 play
			mediaPlayer.stop();
			adapter.notifyDataSetChanged();
			return true;
		}
		
		if (id==R.id.export1){
			ViewDialog alert = new ViewDialog();
			alert.showDialog(MP3SongListCreate.this, "Error de conexi al servidor");
			
			
//			String s1=(""+System.currentTimeMillis())  ;
//			String s2=s1.substring(s1.length()-5, s1.length()-1);
//			FileWriteString.setFileString(EXPORTFOLDER, "MP3_list_"+s2+".txt",
//					sharedpreferences.getString(SONGLIST, ""));

			
			return true;
		}
		
		if (id==R.id.import1){
	        Intent intent=new Intent(MP3SongListCreate.this,MainActivityDirShow11mp3list.class);
	        intent.putExtra("OPTYPE", "3");// 2=browse, 3=import list of mp3
	        intent.putExtra("LISTMP3FOLDER", EXPORTFOLDER);// Not needed - just for integrity        
	        startActivityForResult(intent, 3);// Activity is started with requestCode 3   	
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	@Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data)   {  
              super.onActivityResult(requestCode, resultCode, data);  
                if(requestCode==2) {               	 
                	if (data.getStringExtra("MESSAGE").equals("EMPTY")){ 
                		Toast.makeText(this,"No valid data added !!!"	,Toast.LENGTH_LONG).show();       
                	}	
                	else{
                         mp3Alist.add(data.getStringExtra("MESSAGE"));
                         updatePosition(); 
                        
         	            AsyncTaskMediaRetriever asyncTask=new AsyncTaskMediaRetriever();
        	            asyncTask.execute();                	  
                				}
                	selectedposition=0;
                	checkTopButtons();           	
            		flag=false;
            		controlPlay=0; // 0 dont play, 1 play
             		pauseFlag=false;
            		checkLowButtons(0);     	               	
                      }  
                
                if(requestCode==3) {               	 
                	if (data.getStringExtra("MESSAGE").equals("EMPTY")){ 
                		Toast.makeText(this,"No valid data added !!!"	,Toast.LENGTH_LONG).show();       
                	}	
                	else{
                		mp3Alist.clear();
                        SharedPreferences.Editor editor = sharedpreferences.edit();	 
                        String str=data.getStringExtra("MESSAGE").replace("\r","").replace("\n","");                        
                        editor.putString(SONGLIST, str).commit();
                		String s2[]=str.split(",");
                		for (int i=0;i<s2.length;i++){
                			mp3Alist.add(s2[i]);
                		}
         	            AsyncTaskMediaRetriever asyncTask=new AsyncTaskMediaRetriever();
        	            asyncTask.execute();                
                				}
                	selectedposition=0;
                	checkTopButtons();
               		flag=false;
            		controlPlay=0; // 0 dont play, 1 play
             		pauseFlag=false;
            		checkLowButtons(0);     	               	
                      }  
  }  
	
	private void updatePosition(){
        SharedPreferences.Editor editor = sharedpreferences.edit();	      
	if (mp3Alist.isEmpty()){
		editor.putString(SONGLIST, "").commit();
	}
	else {
		StringBuilder sb=new StringBuilder();
		for (int i=0;i<mp3Alist.size();i++){	
			if (mp3Alist.get(i)!=null && mp3Alist.get(i).length()>0){
				sb.append(mp3Alist.get(i)+",");
			}	
		}      
		if (sb.toString().length()>0 ){
			editor.putString(SONGLIST, sb.toString().substring(0, sb.toString().length()-1)).commit();
        		}
		else {	
			editor.putString(SONGLIST, "").commit();
			}       
		}
}// end of update.....
		
	
private ArrayList<MediaDataRetriever1MP3list> makeMediaDataRetriever(){
	Bitmap songImage1=null;
	ArrayList<MediaDataRetriever1MP3list> mret =new ArrayList<MediaDataRetriever1MP3list>();
	mret.clear();
	MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();   
	for (int i=0;i<mp3Alist.size();i++){
		if (mp3Alist!=null && mp3Alist.get(i).length()>2){
			metaRetriver.setDataSource(new File(mp3Alist.get(i)).toString());
			byte[] art1 = metaRetriver.getEmbeddedPicture();	
				if (art1!=null){ 
					if (art1.length>0){
						songImage1=Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(art1,
								0, art1.length),mWidth_,mHeight_,false);
						}
				}
	else {
		songImage1=BitmapFactory.decodeResource(getResources(), R.drawable.imagemp3);
	}
//	if (songImage1==null){
//		songImage1=BitmapFactory.decodeResource(getResources(), R.drawable.audioplay);
//			}//
	String artistName=metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
	String songName=metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
	String duration = metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
	if (songName==null) songName=getSong(mp3Alist.get(i));
	if (artistName==null) artistName="***";
	mret.add(new MediaDataRetriever1MP3list(songImage1, songName, artistName,duration));
			}
		}//  for loop
	return mret;
}	
	

private class MyListAdapter extends ArrayAdapter<MediaDataRetriever1MP3list> {
	ArrayList<MediaDataRetriever1MP3list> mret;  
	public MyListAdapter(ArrayList<MediaDataRetriever1MP3list> mret1) {
		
		super(MP3SongListCreate.this, R.layout.item_viewmp3list, mret1);
		 mret= mret1; 
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Make sure we have a view to work with (may have been given null)
		View itemView = convertView;
		if (itemView == null) {
			itemView = getLayoutInflater().inflate(R.layout.mylistmp3, parent, false);
		}
		
		MediaDataRetriever1MP3list currentM = mret.get(position);
//		ImageView last=(ImageView)itemView.findViewById(R.id.item_last);
//		if (selectedposition==position) last.setVisibility(View.VISIBLE);
//		else last.setVisibility(View.GONE);
		
		RelativeLayout rl =(RelativeLayout)itemView.findViewById(R.id.rl);
		ImageView iv=(ImageView)itemView.findViewById(R.id.item_icon);
		TextView songText = (TextView) itemView.findViewById(R.id.songname);
		TextView artistyearText = (TextView) itemView.findViewById(R.id.songartist);
		TextView duartion = (TextView) itemView.findViewById(R.id.item_txtCondition);
		TextView ser_num = (TextView) itemView.findViewById(R.id.guess1);

		if (selectedposition==position){ 
			duartion.setText(  convertDuration2String(elapsedTimeSong)+"/"+
					convertDuration2String(currentM.getDuration()));
			rl.setBackgroundColor(Color.GREEN);
		}
		else{
			duartion.setText(convertDuration2String(""+currentM.getDuration()));
			rl.setBackgroundColor(Color.TRANSPARENT);
		}
		
		iv.setImageBitmap(currentM.getB());
		songText.setText(""+currentM.getSongName());
		artistyearText.setText("" + currentM.getArtistName());		
		
		
		
		String seq=""+(1+position);
		if (seq.length()==1) seq="0"+seq;
		ser_num.setText(seq);
		ser_num.setBackgroundColor(Color.YELLOW);
					
		return itemView;
	}				
}

private String convertDuration2String(String rawDuration){
	if (rawDuration==null || rawDuration.length()<1) return "00:00";
	else {
	String secs= ""+(Integer.valueOf(rawDuration)/1000)%60;
	if (secs.length()==1) secs="0"+secs;
	String mins=""+(Integer.valueOf(rawDuration)/1000)/60;
	if (mins.length()==1) mins="0"+mins;
	return mins+":"+secs;
	}
	
}

@Override
public void onClick(View v) {
	int id=v.getId();
	if (id==R.id.bup){
		if (selectedposition!=0 && selectedposition>-1){
			String sth=mp3Alist.get(selectedposition);
			String stl=mp3Alist.get(selectedposition-1);				
			mp3Alist.set(selectedposition-1, sth);
			mp3Alist.set(selectedposition, stl);
			selectedposition--;
			updatePosition();
            AsyncTaskMediaRetriever asyncTask=new AsyncTaskMediaRetriever();
            asyncTask.execute();
			}
		checkTopButtons();
		}
	if (id==R.id.bdown){
		if (selectedposition>-1 && selectedposition<mp3Alist.size()-1){
			String stl=mp3Alist.get(selectedposition);
			String sth=mp3Alist.get(selectedposition+1);				
			mp3Alist.set(selectedposition, sth);
			mp3Alist.set(selectedposition+1, stl);
			selectedposition++;
			updatePosition();
            AsyncTaskMediaRetriever asyncTask=new AsyncTaskMediaRetriever();
            asyncTask.execute();
		}
		checkTopButtons();
	}
	if (id==R.id.bdelete){
		if (mp3Alist.size()>0 && selectedposition>-1){
			mp3Alist.remove(selectedposition);
			selectedposition--;
//if (selectedposition<0) selectedposition=0;
			updatePosition(); 
			checkTopButtons();
			AsyncTaskMediaRetriever asyncTask=new AsyncTaskMediaRetriever();
			asyncTask.execute();
}
		
	}
	if (id==R.id.bbrowse){
		if (mediaPlayer!=null){
			if (mediaPlayer.isPlaying()){
				mediaPlayer.stop();
				mediaPlayer.release();
			}
			else {
				mediaPlayer.release();
			}
		
		}
        Intent intent=new Intent(MP3SongListCreate.this,MainActivityDirShow11mp3list.class);
        intent.putExtra("OPTYPE", "2");// 2=browse, 3=import list of mp3
        intent.putExtra("LISTMP3FOLDER", EXPORTFOLDER);// Not needed - just for integrity        
        startActivityForResult(intent, 2);// Activity is started with requestCode 2  	
	}
	
	if (id==R.id.bstart){
		if (mediaPlayer!=null){
			if (!mediaPlayer.isPlaying()) {
				if (controlPlay==0 && !mediaPlayer.isPlaying()) {
					controlPlay=1;
				}
				customHandler1.postDelayed(updateTimerThread1, 0);
	    	}
		}
		checkLowButtons(1);
	}

	if (id==R.id.bstop){
		flag=false;
		selectedposition=0;
		controlPlay=0; // 0 dont play, 1 play
		mediaPlayer.stop();
		adapter.notifyDataSetChanged();
		checkLowButtons(2);
	}
	
	if (id==R.id.bpause){
		Toast.makeText(this, "paused ", Toast.LENGTH_SHORT).show();
		elapsedTime=mediaPlayer.getCurrentPosition();
		flag=false;
////////		selectedposition=0;
		controlPlay=0; // 0 dont play, 1 play
		pauseFlag=true;
		mediaPlayer.pause();
		adapter.notifyDataSetChanged();
		checkLowButtons(3);
	}
	
	
}// end      on click



/////////////////////////////
private class AsyncTaskMediaRetriever extends AsyncTask<Void, Void, ArrayList<MediaDataRetriever1MP3list>> {
    private int filesCoherent=-1;
	
	@Override
protected void onPreExecute() {
super.onPreExecute();
}
@Override
protected ArrayList<MediaDataRetriever1MP3list> doInBackground(Void...  i88) {
	filesCoherent=checkAllFilesExist();  // check all files exist !!!!!!!!
	ArrayList<MediaDataRetriever1MP3list> mret2 =new ArrayList<MediaDataRetriever1MP3list>();
try {
	mret2=	makeMediaDataRetriever();
} catch (Exception e) {
	e.printStackTrace();
}
return mret2;
}
@Override
protected void onPostExecute(ArrayList<MediaDataRetriever1MP3list> mret3) {
super.onPostExecute(mret3);

if (filesCoherent!=0){
Toast.makeText(MP3SongListCreate.this,"Invalid file(s) found !!!  ("+filesCoherent+")"	
		,Toast.LENGTH_LONG).show();       
finish();
}
else {
Toast.makeText(MP3SongListCreate.this,"All files passed check !!! ("+filesCoherent+")"	
		,Toast.LENGTH_LONG).show();       
}





adapter=new MyListAdapter(mret3);	  
lv.setAdapter(adapter); 
	adapter.notifyDataSetChanged();
	checkTopButtons();
	}
}

private String getSong(String str){
	String[] st=str.split("/");
	return st[st.length-1];
	
	
	
}
////////////////////////////////////////////////
///////////////////////



@Override
public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//	Intent service = new Intent(getApplicationContext(), MyService.class);
//   getApplicationContext().stopService(service);	

//	String s=sharedPreferences.getString(SERVICEACK, "");
//	Toast.makeText(this, "shared "+s, Toast.LENGTH_SHORT).show();

	
}


private void initMediaPlayer() {
	String name=sharedpreferences.getString(SONGLIST, "");
	String[] nameL=name.split(",");
	
    mediaPlayer = new MediaPlayer();
    mediaPlayer.setOnCompletionListener(this);
    mediaPlayer.setOnErrorListener(this);
    mediaPlayer.setOnPreparedListener(this);
    mediaPlayer.setOnBufferingUpdateListener(this);
    mediaPlayer.setOnSeekCompleteListener(this);
    mediaPlayer.setOnInfoListener(this);
    mediaPlayer.reset();
    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    try {
       mediaPlayer.setDataSource(nameL[selectedposition]);
    } catch (IOException e) {
        e.printStackTrace();
//        stopSelf();
    }
    try {
mediaPlayer.prepare();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
	} catch (IllegalStateException e) {
		e.printStackTrace();
	} 
}

@Override
public void onBufferingUpdate(MediaPlayer mp, int percent) {	
}

@Override
public boolean onInfo(MediaPlayer mp, int what, int extra) {
	return false;
}

@Override
public void onSeekComplete(MediaPlayer mp) {	

	
}

@Override
public boolean onError(MediaPlayer mp, int what, int extra) {
	return false;
}

@Override
public void onPrepared(MediaPlayer mp) {
if (controlPlay==1) {
	mediaPlayer.seekTo(elapsedTime);
	elapsedTime=0;
	pauseFlag=false;
	mediaPlayer.start();
		}
}

@Override
public void onCompletion(MediaPlayer mp) {
controlPlay=0;// shows we finished song or singing song
  mediaPlayer.stop();
}

private Runnable updateTimerThread1 = new Runnable() {
    public void run() {

    	if (mediaPlayer.isPlaying() || pauseFlag){
    elapsedTimeSong=""+mediaPlayer.getCurrentPosition();
    	}
    	else {
    		elapsedTimeSong="";
    	}	
    	
    	
if (controlPlay==1 && !flag){  // flag=false disable play even if ready to play
	if (selectedposition<mp3Alist.size()){
	initMediaPlayer();
	flag=true;
				}
	else {
		flag=false;
		selectedposition=0;
		controlPlay=0; // 0 dont play, 1 play
		mediaPlayer.stop();
		pauseFlag=false;
		checkLowButtons(0);
		adapter.notifyDataSetChanged();
	}
	
	
	
	
		}
if (controlPlay==0 && flag){
	selectedposition++;
	flag=false;
	controlPlay=1;// initMediaPlayer();

}
adapter.notifyDataSetChanged();
  //////////
       customHandler1.postDelayed(updateTimerThread1, 500);
    }
	
};

private void checkLowButtons(int dispatch) {
	tv.setText("c="+controlPlay+" flag=  "+flag+" pause "+pauseFlag+" patch "+dispatch);
	if (controlPlay==0 && !flag && ! pauseFlag){  // init
		bstart.setBackgroundColor(Color.GREEN);
		bstop.setBackgroundColor(Color.WHITE);
		bpause.setBackgroundColor(Color.WHITE);
		bstart.setEnabled(true);
		bstop.setEnabled(false);
		bpause.setEnabled(false);
	}
	
	else if (controlPlay==1 && !pauseFlag && !flag){  // started play
		bstart.setBackgroundColor(Color.WHITE);
		bstop.setBackgroundColor(Color.RED);
		bpause.setBackgroundColor(Color.GREEN);
		bstart.setEnabled(false);
		bstop.setEnabled(true);
		bpause.setEnabled(true);
	}
	
	else if (controlPlay==1 && pauseFlag && !flag){  // started play
		bstart.setBackgroundColor(Color.WHITE);
		bstop.setBackgroundColor(Color.RED);
		bpause.setBackgroundColor(Color.GREEN);
		bstart.setEnabled(false);
		bstop.setEnabled(true);
		bpause.setEnabled(true);
	}
	
	else if (controlPlay==0 && pauseFlag && !flag && dispatch==3){  // stopped play
		bstart.setBackgroundColor(Color.GREEN);
		bstop.setBackgroundColor(Color.RED);//////////
		bpause.setBackgroundColor(Color.WHITE);
		bstart.setEnabled(true);
		bstop.setEnabled(true);
		bpause.setEnabled(false);
	}
	
	else if (controlPlay==0 && pauseFlag && !flag && dispatch==2){  // stopped play
		bstart.setBackgroundColor(Color.GREEN);
		bstop.setBackgroundColor(Color.WHITE);//////////
		bpause.setBackgroundColor(Color.WHITE);
		bstart.setEnabled(true);
		bstop.setEnabled(false);
		bpause.setEnabled(false);
	}
	
	else if (controlPlay==0 && !pauseFlag && !flag){  // stopped play ok
		bstart.setBackgroundColor(Color.GREEN);
		bstop.setBackgroundColor(Color.WHITE);
		bpause.setBackgroundColor(Color.WHITE);
		bstart.setEnabled(true);
		bstop.setEnabled(false);
		bpause.setEnabled(false);
	}
	
	else if (controlPlay==0 && !flag && pauseFlag){  // pause play
		bstart.setBackgroundColor(Color.GREEN);
		bstop.setBackgroundColor(Color.WHITE);
		bpause.setBackgroundColor(Color.WHITE);
		bstart.setEnabled(true);
		bstop.setEnabled(false);
		bpause.setEnabled(false);
	}
	
	
//	else {
//		bstart.setBackgroundColor(Color.WHITE);		
//	}
	
	
	
}


private void checkTopButtons(){
	if (selectedposition>=1) {
		bup.setEnabled(true);
		bup.setBackgroundColor(Color.parseColor("#87CEFA"));
	}
	else {
		bup.setEnabled(false);
		bup.setBackgroundColor(Color.WHITE);

	}
	
	if (selectedposition<=mp3Alist.size()-2) {
		bdown.setEnabled(true);
		bdown.setBackgroundColor(Color.parseColor("#98FB98"));
	}
	else {
		bdown.setEnabled(false);
		bdown.setBackgroundColor(Color.WHITE);
	}
	
//	bdelete.setText(""+mp3Alist.isEmpty()+" "+mp3Alist.size());
	
	if (mp3Alist.size()==0 && mp3Alist.isEmpty() ||
			mp3Alist.size()==1 && !mp3Alist.isEmpty() && mp3Alist.get(0).equals("")
			)
	{
		bdelete.setEnabled(false);
		bdelete.setBackgroundColor(Color.WHITE);

	}
	else{
		bdelete.setEnabled(true);
		bdelete.setBackgroundColor(Color.parseColor("#FF7F50"));
	}
	
	
}

public class ViewDialog {

    public void showDialog(Activity activity, String msg){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog1mp3list);
        LinearLayout ll = (LinearLayout) dialog.findViewById(R.id.ll1);
        
 ////       int wh[]=getDisplaymetrics1(); // w, h
//        ViewGroup.MarginLayoutParams p =(ViewGroup.MarginLayoutParams) ll.getLayoutParams();
//////        p.setMargins(width/80,0,width/80,0);
//        p.height=wh[1]/2;
////        p.width=wh[0]/2;
        //enterDirName.setTextSize(height/110);
//        enterDirName.setTextSize(TypedValue.COMPLEX_UNIT_PX,
//        		getResources().getDimensionPixelSize(R.dimen.myFontSize2));
 //       ll.requestLayout();	      
        
        
        
        
        
        TextView text = (TextView) dialog.findViewById(R.id.dialog_txt1);
        text.setBackgroundColor(Color.YELLOW);
        text.setTextColor(Color.BLUE);
        final EditText ed = (EditText) dialog.findViewById(R.id.dialog_edit1);
        ed.setBackgroundColor(Color.YELLOW);
        ed.setTextColor(Color.GREEN);
        
        text.setText(msg);

        Button dialogButtonYES = (Button) dialog.findViewById(R.id.dialog_btn_yes);
        dialogButtonYES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	
    			String s1=(""+System.currentTimeMillis())  ;
    			String s2=s1.substring(s1.length()-5, s1.length()-1);
    			FileWriteStringMP3list.setFileString(EXPORTFOLDER, ed.getText().toString()+"_"+s2+".txt",
    					sharedpreferences.getString(SONGLIST, ""));

           		Toast.makeText(MP3SongListCreate.this,"File exported.... !!"	,Toast.LENGTH_LONG).show();       
           	 
                dialog.dismiss();
            }
        });

        Button dialogButtonNO = (Button) dialog.findViewById(R.id.dialog_btn_no);
        dialogButtonNO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        
        dialog.show();

    }
}

private int[] getDisplaymetrics1 (){
	DisplayMetrics displayMetrics = new DisplayMetrics();
	getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
	int height = displayMetrics.heightPixels;
	int width = displayMetrics.widthPixels;
	return new int[]{width,height};// return array of width and height
}

private int checkAllFilesExist(){
	File rootPath = new File(Environment.getExternalStorageDirectory(), EXPORTFOLDER);
	if(!rootPath.exists()) {
		rootPath.mkdirs();
	}	
	int i=0,j=0;
	String files[]=rootPath.list();
		if (files==null) return 99999;
			for (i=0;i<files.length;i++){
				String f=new File(files[i]).toString(); // get file name from list in dir		
				String list=FileReadStringMP3list.getFileString(rootPath.toString(), f);		
				String[] filesIn=list.toString().split(",");  // hold all songs per single list entry
					for (j=0;j<filesIn.length;j++){
							if (!new File(filesIn[j]).exists()) return 1000*i+j;
		}	
	}
	return 0;
}




	}// end of class
	

//https://www.sitepoint.com/a-step-by-step-guide-to-building-an-android-audio-player-app/