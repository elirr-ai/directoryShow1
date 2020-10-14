package com.example.directoryshow1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class MainActivitySambaShare2 extends Activity {
	
	View vv;
	PopupMenu popup;
	Context context1=this;
	int width=0,height=0;
	public static final int ACTIVITY_REQUEST_CODE = 1777;
	final static int VIB=100;
	
	File Path1;
	String MP3_OPTYPE;
	String currentDir1="/";
	TextView enterDirName;
	Button dir_up,go,new_file1,exit,paste1000,back,favor1000;
	ProgressDialog pd;
	ListView listView;
	ImageView iv1;
    private List<SMBDirectoryFilesHolder> myDirectoryFilesHolder = new  ArrayList<SMBDirectoryFilesHolder>();
    
    public boolean inTaskProgerss=false;

 //   LoadBitmaps loadbmp;   
    ArrayAdapter<SMBDirectoryFilesHolder> adapter;
	
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefsGridSMB" ;  // my pref internal folder name
 // keys
    public static final String Initialized = "Initialized";
    public static final String size_x = "size_x";
    public static final String size_y = "size_y";
    public static final String directory = "Directory";
    public static final String file_name = "File_name";
    public static final String sort_type = "sort";  // 0=no sort, 1 by name asc, 2 by name desc, 3 by date asc,  4 by name dsc
    public static final String position_location = "POSIION";
    public static final String sort_type_Main_Activity = "sort_Main_Act"; 	// 0=no sort, 1 by name asc, 2 by name desc, 3 by date asc,  4 by name dsc
    public static final String myVeryFavouriteDir = "MYVeryFavouriteDir";
    public static final String fitFullScreen = "FITFULL";
    public static final String volume = "VOLUME";
    public static final String showFullScreenMode = "showFullScreenMode";
    public static final String EMAILADDRESS = "EMAILADDRESS";
    public static final String EMAILADDRESS1 = "EMAILADDRESS1";
    public static final String EMAILCHECK = "EMAILCHECK";
    public static final String EMAILCHECK1 = "EMAILCHECK1";
    public static final String COPYSOURCESTRING = "COPYSOURCESTRING";
    private static final int thumbNailX=240,thumbNailY=240;
    private static final boolean cancelBmpLoad = true;
    private static final int MAXPICTURESIZETOSHOW = 2200*1000;
    // self file operations
    private static final String CREATEFILE="1";
    private static final String CREATEDIR="2";
    private static final String DELETEFILE="3";
    private static final String RENAMEFILE="4";
    private static final String COPYFILE="5";
    private static final String MOVEFILE="6";
    private static final String COPYFILELOCAL2SMB="7";
    private static final String COPYFILESMBFAR="8";
    
    
    
    private boolean fileOPFlag=false;
    // 5 by size asc, 6 by size dsc
       
	//  set of params for  4X4 ///////////////////////////////////////////
	String size_x_="4",size_y_="4"; 	  // matrix of images size to be stored in preferenecs 
	String Fname="",sort_="",position_="",sort_main_="";
	
	String[] file_types = new String[]{"folder1", "imagemp3","imagepng","imagejpg","imagebmp", "imagegif","imagetext",
			"imageapk","pdf1",
			"imageother","imageother","imagemp4","image3gp","imageavi","imagemov","imageflv","imagempg",
			"imagemidi","imagewav","imagemkv","imagexml","imagevcf","imagezip","imagerar",
			"imagecsv",
			"imageogg"
			,"imagewmv"
			,"json1"
			,"epub80x80"
	};
	
	int[] resid_ = new int[file_types.length];
	
	String[] f_types = new String[]{"*", ".MP3",".PNG",".JPG",".BMP", ".GIF",".TXT",  //6
			".APK",".PDF","**",".LOG",".MP4",".3GP",".AVI",".MOV",".FLV",".MPG"       //16
			,".MID",".WAV",".MKV", //19
			"XML","VCF","ZIP","RAR",  //20,21,22,23
			"CSV",  //24
			"OGG"//25
			,"WMV"// 26
			,"JSON"//27
			,"EPUB"//28
	};  
	
	ArrayList<String> CopyMoveArray = new ArrayList<String>();
	ArrayList<SMBCurrentDirectoryLastHolderMP3list> goBackDir = new ArrayList<SMBCurrentDirectoryLastHolderMP3list>();
    private ActionBar actionBar;
//	private int exitCounter=0;
	
	
    // screen params
    private double display_screen_size;
	int limit1=21;
    
	String smb="smb://";
	String ip="192.168.1.116//"; //no michse		
	String username= "elirr";
	String password= "elirr";		
	String sharedFolder= "try//";//no michse
//	String sharedFolder= "try//newDirectry//";//no michse
    String path;
    int originalPathLength;
	
	SmbFile[] smbFiles=null;
    SmbFile smbFile = null;
    AsyncTaskgetAllBitmaps     GetBMPtask;
    
    private String copyMoveFileFromLocal="";
    private String sourceStringpath1="";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pd = new ProgressDialog(this);
		limit1=getScreenLimits();    
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		String data = sharedpreferences.getString("Initialized", "");
		if (data.equalsIgnoreCase("")) {
			Toast.makeText(getApplicationContext(), "String not found!!!!", Toast.LENGTH_SHORT).show();
		init_all_prefs();		
		}
		read_all_prefs();
 //       intent.putExtra("OPTYPE", "3");// 2=browse, 3=import list of mp3
//        intent.putExtra("LISTMP3FOLDER", EXPORTFOLDER);// Not needed - just for integrity        

        
//		MP3_OPTYPE = getIntent().getStringExtra("OPTYPE");
//		if (MP3_OPTYPE.equals("3")){
//			String dirMP3=getIntent().getStringExtra("LISTMP3FOLDER");
//			File rPath = new File(Environment.getExternalStorageDirectory(), dirMP3);
//			currentDir1=rPath.toString();
//		}

		Intent intent = getIntent();
	    path=intent.getStringExtra("url");
	    originalPathLength=path.split("/{2}").length;
	    username=intent.getStringExtra("username");    
	    password=intent.getStringExtra("password");
	    copyMoveFileFromLocal=intent.getStringExtra("COPYMOVE");
		
		setContentView(R.layout.smbactivity_main2mp3list);
		
		vv=(View)findViewById(R.id.vv);
		favor1000 = (Button) findViewById(R.id.favor1000);
		back = (Button) findViewById(R.id.dir_back);
		dir_up = (Button) findViewById(R.id.dir_up);
		go = (Button) findViewById(R.id.go);
		exit = (Button) findViewById(R.id.exit1000);
		new_file1=(Button) findViewById(R.id.new1000);
		paste1000=(Button) findViewById(R.id.paste1000);
		enterDirName=(TextView)findViewById(R.id.enterguess);
		iv1=(ImageView)findViewById(R.id.iview1);
		listView = (ListView) findViewById(R.id.listView);  

	
		
		adapter = new MyListAdapter();
		listView.setAdapter(adapter);
		registerForContextMenu(listView);
		
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		String PACKAGE_NAME = getApplicationContext().getPackageName();
	    for (int i=0;i<file_types.length;i++) {
	    	resid_[i] = getResources().getIdentifier(PACKAGE_NAME+":drawable/"+file_types[i] , null, null);
	    }
	
		enterDirName.setBackgroundColor(Color.YELLOW);
		enterDirName.setGravity((Gravity.BOTTOM | Gravity.START));
//		enterDirName.setTextSize(16.0f);
		enterDirName.setTextColor(Color.BLACK);
		
		//dir_up.setBackgroundColor(Color.BLUE);
		dir_up.setBackgroundResource(R.drawable.dirup100);
		//dir_up.setTextSize(18.0f);
				
		go.setBackgroundResource(R.drawable.grid11);
		//go.setTextSize(18.0f);
				
		exit.setBackgroundResource(R.drawable.exitprogram);
		//exit.setTextSize(18.0f);
		
		paste1000.setBackgroundResource(R.drawable.paste1000);
		//paste1000.setTextSize(18.0f);
		
		
		
		if (copyMoveFileFromLocal.contains(",")  ||
				sourceStringpath1.length()>1 ){
			paste1000.setVisibility(View.VISIBLE);			
		}
		else {
		paste1000.setVisibility(View.INVISIBLE);
		}
		
		new_file1.setBackgroundResource(R.drawable.addnewfile);
		//new_file1.setTextSize(18.0f);
		back.setBackgroundResource(R.drawable.back1);	
		favor1000.setBackgroundResource(R.drawable.heart1);	
		
		actionBar = getActionBar();
		actionBar.setIcon(R.drawable.sambaicon);
		actionBar.setDisplayShowTitleEnabled(true);
		//actionBar.setTitle("View "+Integer.toString(files.length)+" "+Integer.toString(filex.size()) );
		goBackDir.clear();
		goBackDir.add(new SMBCurrentDirectoryLastHolderMP3list(currentDir1, "")); 
		new LoadFilesFromNetwork().execute();		
		
//		get_directory();
//		sort(sharedpreferences.getString(sort_type_Main_Activity, ""));
///////////////////////////////
//		parse_current_dir();
//		add_prefs_key(directory, currentDir1);
//		get_directory();
//		sort(sharedpreferences.getString(sort_type_Main_Activity, ""));
		////////////////////////////////////////////
		
back.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
//				GetBMPtask.myCancel();
//				if (goBackDir.isEmpty()) {
					Toast.makeText(getApplicationContext(), "Not implemenetd   ", Toast.LENGTH_SHORT).show();
//				}
//				else {
//					try {
////						loadbmp.myCancel();	
//						currentDir1 = goBackDir.remove(goBackDir.size()-1).getCurrentDir(); 
//						add_prefs_key(directory, currentDir1);
//						get_directory();
////					sort(sharedpreferences.getString(sort_type_Main_Activity, ""));
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//
//						}
				}
		
		});
		
dir_up.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {	
				try {
					GetBMPtask.myCancel();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				try {
					 String[] s0=path.split("/{2}");  // get fr0m net
					 StringBuilder sb=new StringBuilder();
						if (s0.length>originalPathLength){
								for (int i=0;i<s0.length-1;i++){
									sb.append(s0[i]);
									sb.append("//");
								}				
								path=sb.toString();
								new LoadFilesFromNetwork().execute();
						}	
						else {
							  	 Toast.makeText(MainActivitySambaShare2.this,"At the top ... ",
							  			 Toast.LENGTH_SHORT).show();  
}
/*					
	//				loadbmp.myCancel();
					parse_current_dir();
					add_prefs_key(directory, currentDir1);
					get_directory();
//				sort(sharedpreferences.getString(sort_type_Main_Activity, ""));/////////////////////////////    		
*/
				
				
				} catch (Exception e) {
					e.printStackTrace();
					}
				}
		
		});
	
		go.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GetBMPtask.myCancel();
				go_grid();
						
			}
		});
	
		new_file1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				//Creating the instance of PopupMenu  
				GetBMPtask.myCancel();
				popup = new PopupMenu(MainActivitySambaShare2.this, new_file1);  
	            popup.getMenu().add(1, 1, 1, "New File");
	            popup.getMenu().add(1,2,2,"New Dir");
	            popup.show();
	           	            
	            //registering popup with OnMenuItemClickListener  
	            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
	             public boolean onMenuItemClick(MenuItem item) {  
	             int a = item.getItemId();
	            	 Toast.makeText(MainActivitySambaShare2.this,"You Clicked : " + item.getTitle()+" "+a,Toast.LENGTH_SHORT).show();  
	              if (item.getItemId()==1) make_new_file();
	              if (item.getItemId()==2) make_new_dir();
	          
	              return true;  
	             }  
	            });  
	              //popup.show();//showing popup menu  
	           }  
	          });//closing the setOnClickListener method  
				
				
		favor1000.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Creating the instance of PopupMenu  
				GetBMPtask.myCancel();
				popup = new PopupMenu(context1, back); 
				final String fv=sharedpreferences.getString(myVeryFavouriteDir, "");
				final String[] fv1=fv.split(",");				

				for (int i=1;i<fv1.length;i++){
		            SpannableString sc = new SpannableString("Load Favourite "+
		            		" ("+fv1[i]+" )");
		            sc.setSpan(new ForegroundColorSpan(Color.CYAN), 0, 14, 0);				
		            popup.getMenu().add(1, i, 1, sc);
	            }
	            SpannableString sb = new SpannableString("Set Favourite"
	            		+ " ("+currentDir1+")");
	            sb.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 13, 0);	            
	            popup.getMenu().add(2,2,2,sb);
	            SpannableString sa = new SpannableString("Clear Favorites");
	            sa.setSpan(new ForegroundColorSpan(Color.RED), 0, sa.length(), 0);
	            popup.getMenu().add(3,1,3,sa);
	            
	            popup.show();	           	            
	            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  
	             public boolean onMenuItemClick(MenuItem item) {  
	             int a = item.getItemId();
	            	 Toast.makeText(MainActivitySambaShare2.this,"You Clicked : " + item.getTitle()+" "+a,Toast.LENGTH_SHORT).show();  
	              if (item.getGroupId()==1) {
	            	  currentDir1=fv1[item.getItemId()];
	            	  get_directory();
	              }
	              if (item.getGroupId()==2) {
	            	  add_prefs_key(myVeryFavouriteDir, fv+","+currentDir1);
	            	  }
	              if (item.getGroupId()==3) {	            	
	            	  add_prefs_key(myVeryFavouriteDir, "");
	            	  }	              
	              
	                     return true;  
	             }  
	            });  
	            
	            
	           }  
	          });//closing the setOnClickListener method  			

		paste1000.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GetBMPtask.myCancel();
				
				 if (sourceStringpath1.length()>1){
					 String aaa=sourceStringpath1; //full source
					 String[] s1=aaa.split(",");
					 String[] s0=s1[0].split("/{2}");  // get fr0m net
					 String s3=s1[1]; //username 
					 String s4=s1[2];//password
					 CopyMoveArray.clear();
					 CopyMoveArray.add(s1[0]);
					 CopyMoveArray.add("1234");
					 CopyMoveArray.add(s0[s0.length-1]);
					 CopyMoveArray.add(s3);
					 CopyMoveArray.add(s4);
					 
					 ArrayList<String> vvv =new ArrayList<>();
					 vvv.addAll(CopyMoveArray); //empty
					 String pathw=path;//target path
					 int y=pathw.length();
					 int yy=y*2;
					 copy_fileSMBfar();
					
				}	
				
				 else	if (CopyMoveArray.size()!=0 && CopyMoveArray.get(3).equals("c")		)
				{
					Toast.makeText(getBaseContext(), "copy pressed    ", Toast.LENGTH_SHORT).show(); 
					copy_fileSMB();
					} // end if
				
				
				
				else	if (CopyMoveArray.size()!=0 && CopyMoveArray.get(3).equals("m") ){
					Toast.makeText(getBaseContext(), "move pressed    ", Toast.LENGTH_SHORT).show(); 
//					copy_file();
//					delete_file_1();										
					}		// end else
				

				
				
				else if (copyMoveFileFromLocal.contains(",")){
					copy_fileLOCAL2SMB_();					
				}
				
				
				
				
				
				
				paste1000.setVisibility(View.INVISIBLE);

			}

			
		});		
		
		
exit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!fileOPFlag){
					finish();
		}
		else {
			try {
	Toast.makeText(context1, "Can't exit - Pls wait !!!!!! ", Toast.LENGTH_LONG).show();					
			if (GetBMPtask!=null){
				if(	GetBMPtask.getStatus()==AsyncTask.Status.RUNNING ||  
						GetBMPtask.getStatus()==AsyncTask.Status.PENDING ){
					GetBMPtask.cancel(true);
				}				
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
			}

		});
		
	       
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, final View v, int position, long id) { 
                ArrayList<SMBDirectoryFilesHolder> tmp1DirectoryFilesHolder = new  ArrayList<SMBDirectoryFilesHolder>();
                tmp1DirectoryFilesHolder.addAll(myDirectoryFilesHolder);           
       
        		try {
        			
        			if (GetBMPtask!=null){
        				if(	GetBMPtask.getStatus()==AsyncTask.Status.RUNNING ||  
        						GetBMPtask.getStatus()==AsyncTask.Status.PENDING ){
        					GetBMPtask.cancel(true);
        				}				
        			}
        		} catch (Exception e1) {
        			e1.printStackTrace();
        		}
        		if (!fileOPFlag)   {   
       
////////////////////////////////////////////////////////
//        		Toast.makeText(getBaseContext(), "Position:  "+Integer.toString(position)+"&& "+ 
//          				""+myDirectoryFilesHolder.get(position).getDir_image() +
//          				myDirectoryFilesHolder.get(position).getFullFileName()               				
//          				, Toast.LENGTH_SHORT).show();            	
            	add_prefs_key(position_location, Integer.toString(position));
           		add_prefs_key(directory, currentDir1);
           		add_prefs_key(file_name, myDirectoryFilesHolder.get(position).getFullFileName()); 
//           		sort(sharedpreferences.getString(sort_type_Main_Activity, ""));

		if (myDirectoryFilesHolder.get(position).getDir_image()==0 ){  // is dir			
//    		path = smb + ip + sharedFolder+"newDirectry/";// inner ok 
//    		path = "smb://" + ip + "//" + sharedFolder+"newDirectry//";// inner ok
//////////  path="smb://ASPIRE-PC//try//10//";  //  ok
String a4 = "";
String a3=myDirectoryFilesHolder.get(position).getFullPathFileNameSMB();
if (a3.length()>6){
String a6=a3.substring(a3.length()-3, a3.length());
//Toast.makeText(getBaseContext(), "up...."+a3+"\n"+a6, Toast.LENGTH_SHORT).show();
if (a6.equals("///")) a4=a3.substring(0,    a3.length()-1);
else a4=a3;
path=a4;	
	}
else {
Toast.makeText(getBaseContext(), "Wrong path....", Toast.LENGTH_LONG).show();
path=a4;	
}
enterDirName.setText(myDirectoryFilesHolder.get(position).getDir_image() +"   "+ path );    	     		  
    		  new LoadFilesFromNetwork().execute();    		      		  
                	}
                 
                	else if (myDirectoryFilesHolder.get(position).getDir_image()==1
                	|| myDirectoryFilesHolder.get(position).getDir_image()==17 
                	|| myDirectoryFilesHolder.get(position).getDir_image()==18)               		
                	{  //  MP3, WAV , MID
                		String fName=myDirectoryFilesHolder.get(position).getDir_name();           	
                		String[] s0=fName.split("/{2}");  // get fr0m net
                		String url = path+ s0[s0.length-1];	  // get fr0m net	
                		Intent audioPlay = new Intent(getApplicationContext(), SMBaudioPlay1.class);
                		audioPlay.putExtra("username", username);
                		audioPlay.putExtra("password", password);
                		audioPlay.putExtra("url", url);
                		startActivity(audioPlay);		
  	
                	}
                	
                   	else if (myDirectoryFilesHolder.get(position).getDir_image()==6
                   			|| myDirectoryFilesHolder.get(position).getDir_image()==10
                   			|| myDirectoryFilesHolder.get(position).getDir_image()==20
                   			|| myDirectoryFilesHolder.get(position).getDir_image()==21
                   			|| myDirectoryFilesHolder.get(position).getDir_image()==24////////////// text
                   					|| myDirectoryFilesHolder.get(position).getDir_image()==27// json
                   			) // txt
                               	{ 
                       		String fName=myDirectoryFilesHolder.get(position).getDir_name();           	
                    		String[] s0=fName.split("/{2}");  // get fr0m net
                    		String url = path+ s0[s0.length-1];	  // get fr0m net	
                    		Intent textIntent = new Intent(getApplicationContext(), SMBshowText1.class);
                    		textIntent.putExtra("username", username);
                    		textIntent.putExtra("password", password);
                    		textIntent.putExtra("url", url);
//                    		startActivity(audioPlay);	
//startActivityForResult(new Intent(getApplicationContext(), showFullScreen1.class),3);
           startActivityForResult(textIntent, 6);// Activity is started with requestCode 6  
                   		}// end if
                	
/////////////////////////////////////////////////////////////////////////////////////////////////////////////		
else	if (myDirectoryFilesHolder.get(position).getDir_image()==28){///////// epub
//Toast.makeText(getBaseContext(), "epub... ", Toast.LENGTH_SHORT).show();
//Intent i1990 = new Intent(getApplicationContext(), EpubHandel1_MainActivity.class);
//i1990.putExtra("dir", currentDir1);
//i1990.putExtra("file", myDirectoryFilesHolder.get(position).getFullFileName());
//startActivity(i1990);

}
/////////////////////////////////////////////////////////////////////////////////////////////////////	
		
		
                	else if (myDirectoryFilesHolder.get(position).getDir_image()==8){  ////////pdf
 String dd=currentDir1;
 String ff=myDirectoryFilesHolder.get(position).getFullFileName();
//	Intent i199 = new Intent(getApplicationContext(), ReadPdf_MainActivity.class);

// Bundle mBundle = new Bundle();
// mBundle.putString("dir", dd);
// mBundle.putString("file", ff);
// i199.putExtras(mBundle);
   //           		startActivity(i199);
                	                	
                	}

                	else	if (myDirectoryFilesHolder.get(position).getDir_image()==7){ //////////apk
                		Toast.makeText(getBaseContext(), "End with apk !!!!!!!!!   "+myDirectoryFilesHolder.get(position).getFullFileName() , Toast.LENGTH_SHORT).show();
   		File file = new File(currentDir1+File.separator+myDirectoryFilesHolder.get(position).getFullFileName());
  
                		Intent intentapk = new Intent(Intent.ACTION_VIEW);
intentapk.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                		intentapk.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                		startActivity(intentapk);
                		
                		
                		
                		
                	}	
        	
                	else	if (myDirectoryFilesHolder.get(position).getDir_image()==9){
                		Toast.makeText(getBaseContext(), "File no DOT   ????? ", Toast.LENGTH_SHORT).show();
                	}
                	//  picture
              
                	else if (myDirectoryFilesHolder.get(position).getDir_image()>=2 &&
                			myDirectoryFilesHolder.get(position).getDir_image()<=5)  {  // is picture
 //               		Toast.makeText(getBaseContext(), "position= "+Integer.toString(position)+" last position= ", Toast.LENGTH_SHORT).show();
                		add_prefs_key(showFullScreenMode, "0");
  //              		startActivityForResult(new Intent(getApplicationContext(), showFullScreen1.class),3);
     String fName=myDirectoryFilesHolder.get(position).getDir_name();           	
	 String[] s0=fName.split("/{2}");  // get fr0m net
	 String url = path+ s0[s0.length-1];	  // get fr0m net	
	Intent showImage = new Intent(getApplicationContext(), SMBshowFullScreen1.class);
	showImage.putExtra("username", username);
	showImage.putExtra("password", password);
	showImage.putExtra("url", url);
//	showImage.putExtra("thumbNailX", (int)dis.getScreenWidth());
//	showImage.putExtra("thumbNailY", (int)dis.getScreenheight());
    startActivityForResult(showImage, 6);// Activity is started with requestCode 6  
//	showImage.putExtra("file", myDirectoryFilesHolder.get(position).getFullFileName());

	 
//	 bitmap=GetSBitMapfromSMBFile.getBitmapFromSMBFile(url,
//				username,password,
//				BitmapFactory.decodeResource(getResources(), resid_[type]), thumbNailX,thumbNailY);					 
	 
	 
                       	}
           
                	else if ((myDirectoryFilesHolder.get(position).getDir_image()>=11 &&  // is movie
                			myDirectoryFilesHolder.get(position).getDir_image()<=16) || 
                			myDirectoryFilesHolder.get(position).getDir_image()==19 //mkv   
                	||		myDirectoryFilesHolder.get(position).getDir_image()==25  // ogg
                ||			myDirectoryFilesHolder.get(position).getDir_image()==26 // WMV
                			)  {  
                 		add_prefs_key(showFullScreenMode, "0");
   //      		   		startActivity(new Intent(getApplicationContext(), showFullMovie.class));
                   		String fName=myDirectoryFilesHolder.get(position).getDir_name();           	
                		String[] s0=fName.split("/{2}");  // get fr0m net
                		String url = path+ s0[s0.length-1];	  // get fr0m net	
                		Intent showFullMovie = new Intent(getApplicationContext(), SMBshowFullMovie.class);
                		showFullMovie.putExtra("username", username);
                		showFullMovie.putExtra("password", password);
                		showFullMovie.putExtra("url", url);
                		startActivity(showFullMovie);		
  
                	}

                
                	else if ((myDirectoryFilesHolder.get(position).getDir_image()==22 )
                	|| (myDirectoryFilesHolder.get(position).getDir_image()==23 ) ) {  //zip
File file = new File(	currentDir1+File.separator+myDirectoryFilesHolder.get(position).getFullFileName());
Intent intent = new Intent(Intent.ACTION_VIEW);
intent.setDataAndType(Uri.fromFile(file), "application/zip");
intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
startActivity(intent);              
                	}
               	else  {
                           		Toast.makeText(getBaseContext(), "End with ????   QQQQQ   !!  ", Toast.LENGTH_SHORT).show();    
               			}          	
                		
        			}
        		else {
        Toast.makeText(getBaseContext(), "Pls wait ....End with ????   QQQQQ   !!  ", Toast.LENGTH_SHORT).show();   
              		 
        		}
        		}	
            });

	}   //////////////////////////////////////////////////////////////////////////////////////////////////// end of on create method

	
	
	private double get_screen_params() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		
		double wp=dm.widthPixels;
//			display_width=dm.widthPixels;
		double xdpi=dm.xdpi;
		double x = Math.pow(wp/xdpi,2);
		
		double hp=dm.heightPixels;
//			display_height=dm.heightPixels;
		double ydpi=dm.ydpi;
		double y = Math.pow(hp/ydpi,2);
		double screenInches = Math.sqrt(x+y);     
		return screenInches;
	}

	public void make_new_file()
	{
///////////////////	  file create	
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivitySambaShare2.this);
		alert.setTitle("Create file");
		alert.setMessage("Enter new file name");
		// Set an EditText view to get user input 
		final EditText input = new EditText(MainActivitySambaShare2.this);
		alert.setView(input);
		input.setFocusable(true);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			new AsyncTaskCreateFile().execute(CREATEFILE,path,input.getText().toString());
		  }
		});	
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});
		
		alert.show();
//////////////////////////////////////////////////////////file create	
	}	
	
	
	public void make_new_dir()
	{
///////////////////	  file dir	
		AlertDialog.Builder alert = new AlertDialog.Builder(MainActivitySambaShare2.this);
		alert.setTitle("Create directory");
		alert.setMessage("Enter new directory name");
		// Set an EditText view to get user input 
		final EditText input = new EditText(MainActivitySambaShare2.this);
		alert.setView(input);
		input.setFocusable(true);
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
			new AsyncTaskCreateFile().execute(CREATEDIR,path,input.getText().toString());				
		  }
		});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});
		
		alert.show();
//////////////////////////////////////////////////////////file create	
	}	
	
	@Override
    public void onCreateContextMenu(ContextMenu menu, View v,
    		ContextMenuInfo menuInfo) {
    	if (v.getId()==R.id.listView) {
    	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
    		//menu.setHeaderTitle(Countries[info.position]);
    	    menu.setHeaderTitle(myDirectoryFilesHolder.get(info.position).getFullFileName());
    		String[] menuItems = new String[]{"Copy", "Move","rename","Delete",
    				"Properties","Open with...","Zip","Send to...","Play backGnd...","BT file Xfer"};
    		
    		for (int i = 0; i<menuItems.length; i++) {
    			menu.add(Menu.NONE, i, i, menuItems[i]);
			}
    	}
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
	    AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
	    int menuItemIndex = item.getItemId();
		String[] menuItems = new String[]{"Copy", "Move","rename","Delete",
				"Properties","Open with...","Zip","Send to..."};
//		String menuItemName = menuItems[menuItemIndex];
//    String listItemName = (myDirectoryFilesHolder.get(info.position).getFullFileName());
	    
	   // TextView text = (TextView)findViewById(R.id.footer);
	   // text.setText(String.format("Selected %s for item %s", menuItemName, listItemName));
//	    Toast.makeText(getBaseContext(), "just "+menuItemName+"  "+listItemName+" *** "+Integer.toString(info.position)+"X"+Integer.toString(menuItemIndex), Toast.LENGTH_SHORT).show();           

	    vibrate(VIB);
	    
	    if (menuItemIndex==0){
	    	copy_file_cotext_menu((myDirectoryFilesHolder.get(info.position).getFullFileName()));
	    }	
	    
	    if (menuItemIndex==1){
	    	move_file_cotext_menu((myDirectoryFilesHolder.get(info.position).getFullFileName()));
	    }
	    
	    if (menuItemIndex==2){
	    	rename_file_cotext_menu((myDirectoryFilesHolder.get(info.position).getFullFileName()));
	    }	
	    
	    if (menuItemIndex==3){
	    	delete_file_cotext_menu((myDirectoryFilesHolder.get(info.position).getFullFileName()));
	    }	  
	    	    
	    if (menuItemIndex==4){
	      	
	    	property_file_cotext_menu(   info.position     );
	    }
	    if (menuItemIndex==5){
	    	open_with_file_cotext_menu(   info.position     );
	    }
	    
	    if (menuItemIndex==6){  // zip
	    	if (myDirectoryFilesHolder.get(info.position).getDir_image()==0){  //is dir
	    	zip_context_menu( ((myDirectoryFilesHolder.get(info.position).getFullFileName()))   );}
	    	else {  // is file
	    		zip_context_menu1( ((myDirectoryFilesHolder.get(info.position).getFullFileName()))   );	    		 	    		
	    	}
	    }	    
	    if (menuItemIndex==7){
	    	send_with_file_cotext_menu(   info.position     );

	    }    
	    
	    if (menuItemIndex==8){
//	    	play_in_background(info.position);

	    }  
	    
	    if (menuItemIndex==9){
	    	BT_file_xfer(info.position);

	    }  
	    
	    return true;
    }
	
    private void BT_file_xfer(int position){
    	String file2BT=currentDir1+File.separator+myDirectoryFilesHolder.get(position).getFullFileName();
   	 Toast.makeText(getBaseContext(), "BT  "+file2BT, Toast.LENGTH_SHORT).show();   
 //  	Intent intent = new Intent(getBaseContext(), BlueToothTest1.class);
 //  	intent.putExtra("FILE", file2BT);
 //  	startActivity(intent);
   	 
   	 
    }
    

    /*
    private void play_in_background(int position){
 int pbackMP3=myDirectoryFilesHolder.get(position).getDir_image();
 String file2Play=currentDir1+File.separator+myDirectoryFilesHolder.get(position).getFullFileName();
 if (pbackMP3==1){
 		setIntent1(file2Play);
 		}
 else {
	 Toast.makeText(getBaseContext(), "Can't play non MP3", Toast.LENGTH_SHORT).show();   
 }
    }
*/
    
    
    private void send_with_file_cotext_menu(int position) {
    	if (myDirectoryFilesHolder.get(position).getDir_image()>1){

String email= sharedpreferences.getString(EMAILADDRESS, "");
String email1= sharedpreferences.getString(EMAILADDRESS1, "");

 android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context1.getSystemService(Context.CLIPBOARD_SERVICE);
 
 if (sharedpreferences.getBoolean(EMAILCHECK, false)){
 android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", email);
 clipboard.setPrimaryClip(clip);  // get mail address if needed in clip board 
 }
 else if (sharedpreferences.getBoolean(EMAILCHECK1, false) && !sharedpreferences.getBoolean(EMAILCHECK, false)
		 
		 ){
 android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", email1);
 clipboard.setPrimaryClip(clip);  // get mail address if needed in clip board 
 }
 
 
  
    	final Intent shareIntent = new Intent(Intent.ACTION_SEND);
    	shareIntent.setType("image/jpg");
File photoFile = new File(currentDir1+File.separator+myDirectoryFilesHolder.get(position).getFullFileName());
//    	final File photoFile = new File(getFilesDir(), "foo.jpg");

    	shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));

    	startActivity(Intent.createChooser(shareIntent, "Share image using"));
  	
Toast.makeText(getBaseContext(), "send to ", Toast.LENGTH_SHORT).show();  
	
    	}
    	else {
    		Toast.makeText(getBaseContext(), "can not send...", Toast.LENGTH_SHORT).show();  
  		
    	}
	}



	private void zip_context_menu(String string) {  // zip dir
Toast.makeText(getBaseContext(), "zip dir***** "+string, Toast.LENGTH_SHORT).show();  

//MakeZipFile mz=new MakeZipFile(context1, string, string, 2);
//mz.delegate=this;
//mz.execute();

	}
    private void zip_context_menu1(String string) {  //zip file
Toast.makeText(getBaseContext(), "zip file***** "+string, Toast.LENGTH_SHORT).show();  

//MakeZipFile mz=new MakeZipFile(context1, currentDir1, string, 1);
//mz.delegate=this;
//mz.execute();
	}
	
	private void open_with_file_cotext_menu(int position) {
	int yyy=myDirectoryFilesHolder.get(position).getDir_image();
    if (yyy!=9 && yyy!=7) {
		File flr = new File(currentDir1+File.separator+myDirectoryFilesHolder.get(position).getFullFileName());				
        Intent intentx = new Intent();
        intentx.setAction(android.content.Intent.ACTION_VIEW);
        String ext=myDirectoryFilesHolder.get(position).getext();      
        Toast.makeText(getBaseContext(), "EXT=  "+ext+
        		"   file  "+flr.toString()+"   yyy   "+yyy
        		, Toast.LENGTH_SHORT).show(); 
        
		        if (flr!=null && ext!=null){ 
		        	intentx.setDataAndType(Uri.fromFile(flr),ext);
		        	startActivity(intentx);
		        			}
		    	else	{
		    		Toast.makeText(getBaseContext(), "associated type error", Toast.LENGTH_LONG).show();  
		   			}	
    	}
	else
		Toast.makeText(getBaseContext(), "Nothing associated with this file type", Toast.LENGTH_LONG).show();  
	
	}



	private void copy_file_cotext_menu(String string) {
		paste1000.setVisibility(View.VISIBLE);		
		String[] s0=string.split("/{2}");  // get fr0m net
		final String url = path+ s0[s0.length-1];	  // get fr0m net		
		CopyMoveArray.clear();
		CopyMoveArray.add(url);
		CopyMoveArray.add(path);
		CopyMoveArray.add(s0[s0.length-1]);
		CopyMoveArray.add("c");
		add_prefs_key(COPYSOURCESTRING, url+","+username+","+password);
		ArrayList<String> vvv=new ArrayList<>();
		vvv.addAll(CopyMoveArray);
		int ccccc=vvv.size();
	}

	private void move_file_cotext_menu(String string) {
		paste1000.setVisibility(View.VISIBLE);
		String[] s0=string.split("/{2}");  // get fr0m net
		final String url = path+ s0[s0.length-1];	  // get fr0m net		
		CopyMoveArray.clear();
		CopyMoveArray.add(url);
		CopyMoveArray.add(path);
		CopyMoveArray.add(s0[s0.length-1]);
		CopyMoveArray.add("m");
	}
	
	private void rename_file_cotext_menu(String string4) {
//		final String t5=string4;
		String[] s0=string4.split("/{2}");  // get fr0m net
		final String url = path+ s0[s0.length-1];	  // get fr0m net	
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Rename file");
		alert.setMessage("From "+url);
		// Set an EditText view to get user input 
		final EditText input = new EditText(this);
		alert.setView(input);
		input.setFocusable(true);
			input.setText(url);	
		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int whichButton) {
	new AsyncTaskCreateFile().execute(RENAMEFILE,url,input.getText().toString());				
//			     get_directory();
			}
		});
		
		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		  public void onClick(DialogInterface dialog, int whichButton) {
		    // Canceled.
		  }
		});
		alert.show();
	}

	//////////////////////////////////////// used for move - after copy delete file/folder no user prompt
	private void delete_file_1() { 
		File file = new File(CopyMoveArray.get(0)+File.separator+CopyMoveArray.get(1));
//        		File Path = new File(currentDir1);
//
//        		if(!Path.exists()) {
//        		    Path.mkdirs();
//        		}
//        		File file = new File(currentDir1+File.separatorChar+ stringn);
/////////        	
        	if (file.isFile()){
        		if (file.exists()) {
        	    	boolean b1=file.delete();
        	    	get_directory();
        	    	Toast.makeText(getBaseContext(), "File was deleted ", Toast.LENGTH_SHORT).show();  
        	    }        	    
        	    else {
        	    	Toast.makeText(getBaseContext(), "File not deleted ", Toast.LENGTH_SHORT).show();           
        	    	}
        		}
        	
        	if (file.isDirectory()){
        		deleteDir(file);
        		currentDir1="/"; ///////  temporary check  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        		get_directory();
        	}
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////
	
	
	
	
	private void delete_file_cotext_menu(final String stringn) {

		 String[] s0=stringn.split("/{2}");  // get fr0m net
		 final String url = path+ s0[s0.length-1];	  // get fr0m net	

    	AlertDialog.Builder adb=new AlertDialog.Builder(MainActivitySambaShare2.this);
        adb.setTitle("Delete/Cancel?");
        adb.setMessage("Are you sure you want to Delete file/dir:  "+url+"   ?" );
        adb.setNegativeButton("Cancel", null);
        
        adb.setNeutralButton("Delete", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
new AsyncTaskCreateFile().execute(DELETEFILE,url,"");				
/*  		
        		File Path = new File(currentDir1);
        		if(!Path.exists()) {
        		    Path.mkdirs();
        		}
        		File file = new File(currentDir1+File.separatorChar+ stringn);     	
        	if (file.isFile()){
        		if (file.exists()) {
        	    	boolean b1=file.delete();
        	    	get_directory();
        	    	Toast.makeText(getBaseContext(), "File was deleted ", Toast.LENGTH_SHORT).show();  
        	    }        	    
        	    else {
        	    	Toast.makeText(getBaseContext(), "File not deleted ", Toast.LENGTH_SHORT).show();           
        	    	}
        		}     	
        	if (file.isDirectory()){
        		deleteDir(file);
        		currentDir1="/"; ///////  temporary check  !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        		get_directory();
        	}
*/ 	        
                }
            });
       
        adb.show();	
	
	}

	private void property_file_cotext_menu( int positionm) {

		AlertDialog.Builder adb=new AlertDialog.Builder(MainActivitySambaShare2.this);
        adb.setTitle("File properties ");
		String isDir1="";
        if (myDirectoryFilesHolder.get(positionm).getAttrib().contains("d")) {
   	isDir1="Dir size:		"+myDirectoryFilesHolder.get(positionm).getParsedSize()+" \n";
        			}
		else{
isDir1="File size:		"+myDirectoryFilesHolder.get(positionm).getParsedSize()+" \n";
		}
        adb.setMessage(
        "File name:		"+myDirectoryFilesHolder.get(positionm).getFullFileName()+"\n"+
        "File date:		"+myDirectoryFilesHolder.get(positionm).getDate_created()+"\n"+
        "File attrib:	"+myDirectoryFilesHolder.get(positionm).getAttrib()+" \n"+	
        isDir1+
        "LEN= "+Integer.toString(myDirectoryFilesHolder.get(positionm).getFullFileName().length())
        +" "+Double.toString((double) get_screen_params())
        
        		);
  
        adb.setNegativeButton("OK", null);
  
        adb.show();	
	
	}

	@Override
	protected void onResume() {
		super.onResume();
//		enableBroadcastReceiver();						
	}

	@Override
	protected void onStart() {
//enableBroadcastReceiver();
		super.onStart();
	}



	@Override
	protected void onPause() {
//disableBroadcastReceiver();
		super.onPause();
	}



	@Override
	protected void onStop() {
//disableBroadcastReceiver();
		super.onStop();
	}



	private void get_directory() {
		String correct_separated;
		int size1=0,dir_type=0 ;
		myDirectoryFilesHolder.clear();
		
		String ip="192.168.1.116"; //no michse
		
		String sername= "elirr";
		String password= "elirr";		
		String sharedFolder= "try//";//no michse
		
		if (currentDir1.length()==0 || currentDir1.equals("")){
			currentDir1="/";
		}
Path1 = new File(currentDir1);  //26_9_
			
		if(!Path1.exists()) {
		    Toast.makeText(getBaseContext(), "path does not exist.", Toast.LENGTH_SHORT).show();
		    currentDir1="/";
		    Path1 = new File(currentDir1);
//		    finish();
		}
File[] files=null;		
		if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			
			Toast.makeText(getBaseContext(), "Cannot use storage.", Toast.LENGTH_SHORT).show();
		    finish();
		    return;
		    		}
		else {
			
			SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
	 	    formatter.setLenient(false); 
	 	    File file1=null;
			enterDirName.setText("path:"+currentDir1);
			add_prefs_key(directory, currentDir1);
			files = Path1.listFiles();
						
			for (int i=0;i<files.length;i++){				
					dir_type = 9;  // un-known file tyep
					String[] separated = files[i].toString().split("/");
					String fileAttrib="";
					correct_separated=separated[separated.length-1];	
					file1 = new File(Path1, correct_separated);
 		try{	      
					    Date lastModified = new Date(file1.lastModified());
					    long curMillis = lastModified.getTime();
					    String lastModified1 = formatter.format(lastModified); 
				   
		    
					    if (!file1.isDirectory()) {
					       	size1 = (int) (file1.length());  // file size
					       	fileAttrib+=" ";	 
				        	dir_type=getFileTypeID(correct_separated) ;  
					    			}
					    else {
					    		dir_type = 0;  // file type is dir
								File childfile[] = file1.listFiles();
								size1=childfile.length;
								fileAttrib+="d";
					    	    }
					    
					   if (file1.canRead()) {
					    	fileAttrib+="r";
					    }
					    else {
					    	fileAttrib+=" ";
					    }
					   if (file1.canWrite()) {
						  fileAttrib+="w";
					    }
					    else {
					    	fileAttrib+=" ";
					    }
					 if (file1.canExecute()) {
						 fileAttrib+="e";
					    }
					 else {
					    	fileAttrib+=" ";
					    }
					     
//						myDirectoryFilesHolder.add(new DirectoryFilesHolder(dir_type, size1,
//								  files.length, correct_separated, lastModified1,	correct_separated, i+1,
//								  fileAttrib , curMillis , null ));					        
				} catch (Exception e) {
					e.printStackTrace();		
				}

		
		// Field 1 --file type:
 	             			//0 non empty dir
 	             			// 1 empty dir
 	             			// 2 PNG file
 	             			//3 JPG file
 	             			//4 BMP file
 	             			//5 GIF file
 	             			// 6 TXT file
 	             // 7 APK
 	             //8 .file
 	             			//9 other type - non handled
	//77 system file
 	             	// Field 2 --file size
 	             	// Field 3 --file size   optional
 	             	// Field 4 --file name (truncated)
 	             	// Field 5 --file date
 	             
 	         	}
			
//sort(sharedpreferences.getString(sort_type_Main_Activity, ""));			
//			loadbmp = new LoadBitmaps();
//            loadbmp.execute();	
	
		}
			
	}
	

	private int getFileTypeID(String correct_separated2) {
//		int ret=9;
		for (int i=0;i<f_types.length;i++){
			if ( correct_separated2.toUpperCase(Locale.US).endsWith(f_types[i]) &&
					!correct_separated2.startsWith(".")
					){
				return i;
				}	
			} 
		return 9;
	}

	private void init_all_prefs() {
	
		add_prefs_key(Initialized, Initialized);
		add_prefs_key(size_x, "4");
		add_prefs_key(size_y, "4"); 
		add_prefs_key(directory, "");
		add_prefs_key(file_name, "NULL");
		add_prefs_key(sort_type, "1");
		add_prefs_key(position_location, "1"); 
		add_prefs_key(sort_type_Main_Activity, "1");
		add_prefs_key(myVeryFavouriteDir, "");
		add_prefs_key(fitFullScreen, "YES");
		add_prefs_key(volume, "0.4");
		add_prefs_key(EMAILADDRESS, "noname@gmail.com");
		add_prefs_key(EMAILADDRESS1, "noname1@gmail.com");
		add_prefs_keyboolean(EMAILCHECK, false);
		add_prefs_keyboolean(EMAILCHECK1, false);

	}

	private void read_all_prefs() {
	
		size_x_ = sharedpreferences.getString(size_x, "");
		size_y_ = sharedpreferences.getString(size_y, "");
		currentDir1	= sharedpreferences.getString(directory, "");
		Fname	= sharedpreferences.getString(file_name, "");
		sort_ 	= sharedpreferences.getString(sort_type, "");
		position_ 	= sharedpreferences.getString(position_location, "");
		sort_main_ 	= sharedpreferences.getString(sort_type_Main_Activity, "");
		sourceStringpath1 	= sharedpreferences.getString(COPYSOURCESTRING, "");
//		favour_ 	= sharedpreferences.getString(myVeryFavouriteDir, "");
			}
    
   
	private void add_prefs_key(String string, String string2) {
		SharedPreferences.Editor editor = sharedpreferences.edit();  
		editor.putString(string, string2).apply();
		}
	
	private void add_prefs_keyboolean(String emailaddress2, boolean b) {
		SharedPreferences.Editor editor = sharedpreferences.edit();  
		editor.putBoolean(emailaddress2, b).apply();
		
	}	
	
	
	
	private void parse_current_dir() {
		String[] separated_up = currentDir1.split("/");
		currentDir1="";
			for (int ind=0;ind<separated_up.length-1;ind++){
			if (!separated_up[ind].equals("") && !separated_up[ind].equals("/"))
			currentDir1+=separated_up[ind]+"/";
			
			}
			if (currentDir1.endsWith("/") ) {
				currentDir1=currentDir1.substring(0, currentDir1.length()-1);
				}
			
if (currentDir1.length()>0){
if (!currentDir1.substring(0, 1).equals("/") && !currentDir1.substring(0,1).equals(""))	
		currentDir1="/"+currentDir1;
}
else if (currentDir1.length()==0){
	currentDir1="/";
}
//			Toast.makeText(getApplicationContext(), "from parse: current "+currentDir1, Toast.LENGTH_LONG).show();

		goBackDir.add(new SMBCurrentDirectoryLastHolderMP3list(currentDir1, ""));
		add_prefs_key(directory, currentDir1);
		get_directory();

	}
	
	private void go_grid() {
//		loadbmp.myCancel();
		add_prefs_key(directory, currentDir1);
 //   	Intent i1 = new Intent(getApplicationContext(), showGrid1.class);
  //  	startActivityForResult(i1,4);
		get_directory();
	}
	
	public static void folderdel(File path, String string){
	    File f= new File(path,string);
	    if(f.exists()){
	        String[] list= f.list();
	        if(list.length==0){
	            if(f.delete()){
	            	return;
	            }
	        }
	        else {
	            for(int i=0; i<list.length ;i++){
	                File f1= new File(path,list[i]);
	                if(f1.isFile()&& f1.exists()){
	                    f1.delete();
	                }
	                if(f1.isDirectory()){
	                    folderdel(path,f1.toString());
	                }
	            }    	    	
	        }
	    }
	}
	//getBaseContext
	
	private void copy_fileSMBfar(){
		ArrayList<String> aaaa = new ArrayList<String>();
		aaaa.addAll(CopyMoveArray);
		String ppppp=path.toString();
		int y=0;
		new AsyncTaskCreateFile().execute(COPYFILESMBFAR,"","");				

	}
	
	private void copy_fileSMB(){
		ArrayList<String> aaaa = new ArrayList<String>();
		aaaa.addAll(CopyMoveArray);
		String ppppp=path.toString();
		int y=0;
		new AsyncTaskCreateFile().execute(COPYFILE,"","");				

	}
	
	private void copy_fileLOCAL2SMB_(){
		new AsyncTaskCreateFile().execute(COPYFILELOCAL2SMB,"","");				

	}
	
	
		private void copy_file(){
	
		File to =new File (currentDir1+File.separator+CopyMoveArray.get(1));    
		File from = new File(CopyMoveArray.get(0)+File.separator+CopyMoveArray.get(1));
		InputStream in = null;
		
Toast.makeText(getBaseContext(), "QQQQQ "+from.toString()+" /  "+to.toString(), Toast.LENGTH_LONG).show();  

/////////////////////	start	
if (!to.isDirectory() && !from.isDirectory()){
		try {
			in = new FileInputStream(from);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		OutputStream out = null;
		try {
			out = new FileOutputStream(to);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// Copy the bits from instream to outstream
		byte[] buf = new byte[1024];
		int len;
		try {
			while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		finally 
		{
			get_directory();
		}
} /////////////// end	

else if ( from.isDirectory()){
try { 
	File	from1 = new File(CopyMoveArray.get(0)+File.separator+CopyMoveArray.get(1));// same from

	copyDirectoryOneLocationToAnotherLocation(from1,to);
} catch (IOException e) {
	e.printStackTrace();
	}
}
get_directory();
	}
///////////////////////////////////////////////////////////////
		public static void copyDirectoryOneLocationToAnotherLocation(File sourceLocation, File targetLocation)
			    throws IOException {

			if (sourceLocation.isDirectory()) {
			    if (!targetLocation.exists()) {
			        targetLocation.mkdir();
			    }

			    String[] children = sourceLocation.list();
			    for (int i = 0; i < sourceLocation.listFiles().length; i++) {

			        copyDirectoryOneLocationToAnotherLocation(new File(sourceLocation, children[i]),
			                new File(targetLocation, children[i]));
			    }
			} else {

			    InputStream in = new FileInputStream(sourceLocation);
			    OutputStream out = new FileOutputStream(targetLocation);

			    // Copy the bits from instream to outstream
			    byte[] buf = new byte[1024];
			    int len;
			    while ((len = in.read(buf)) > 0) {
			        out.write(buf, 0, len);
			    }
			    in.close();
			    out.close();
			}

			}
		///////////////////////////////////////////////////////////////////////////
	private void delete___file() {
		File Path = new File(CopyMoveArray.get(0));

		if(!Path.exists()) {
		    Path.mkdirs();
		}
		File file = new File(CopyMoveArray.get(0)+File.separator+ CopyMoveArray.get(1));
		
		if (file.exists()) {
	    	boolean b1=file.delete();
	    	get_directory();
	    	Toast.makeText(getBaseContext(), "File was deleted ", Toast.LENGTH_SHORT).show();  
	    }
	    
	    else {
	    	Toast.makeText(getBaseContext(), "File not deleted ", Toast.LENGTH_SHORT).show();           

	    	}
	}
	
	private class MyListAdapter extends ArrayAdapter<SMBDirectoryFilesHolder> {
		public MyListAdapter() {
			super(MainActivitySambaShare2.this, R.layout.smbitem_viewmp3list, myDirectoryFilesHolder);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// Make sure we have a view to work with (may have been given null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.smbitem_viewmp3list, parent, false);
			}
			
			SMBDirectoryFilesHolder currentDir = myDirectoryFilesHolder.get(position);
			
			ImageView imageView = (ImageView)itemView.findViewById(R.id.item_icon);
			ImageView imageView1 = (ImageView)itemView.findViewById(R.id.last_icon);
			TextView item_attrib = (TextView) itemView.findViewById(R.id.item_attrib);
			TextView file_name = (TextView) itemView.findViewById(R.id.name);
			TextView indexText = (TextView) itemView.findViewById(R.id.item_txtCondition);
			TextView size = (TextView) itemView.findViewById(R.id.size);
			TextView date1 = (TextView) itemView.findViewById(R.id.date1);
			
			android.view.ViewGroup.LayoutParams params = imageView.getLayoutParams();
			params.height = getResources().getDimensionPixelSize(R.dimen.height);
			imageView.setLayoutParams(params);
			
			android.view.ViewGroup.LayoutParams paramsv = imageView1.getLayoutParams();
			paramsv.height = getResources().getDimensionPixelSize(R.dimen.height4);
			imageView1.setLayoutParams(paramsv);

			android.view.ViewGroup.LayoutParams params1 = file_name.getLayoutParams();
			params1.height = getResources().getDimensionPixelSize(R.dimen.height2);

			file_name.setTextSize(TypedValue.COMPLEX_UNIT_PX,
		getResources().getDimension(R.dimen.font_size1));			
//file_name.setBackgroundColor(Color.YELLOW);
file_name.setTypeface(null, Typeface.BOLD);
file_name.setLayoutParams(params1);

			android.view.ViewGroup.LayoutParams params2 = date1.getLayoutParams();
			params2.height = getResources().getDimensionPixelSize(R.dimen.height3);
			date1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimension(R.dimen.font_size3));
			date1.setLayoutParams(params2);
	
			android.view.ViewGroup.LayoutParams params3 = size.getLayoutParams();
			params3.height = getResources().getDimensionPixelSize(R.dimen.height3);
size.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimension(R.dimen.font_size4));
size.setLayoutParams(params3);

			android.view.ViewGroup.LayoutParams params4 = item_attrib.getLayoutParams();
			params4.height = getResources().getDimensionPixelSize(R.dimen.height4);
			item_attrib.setTextSize(TypedValue.COMPLEX_UNIT_PX,
					getResources().getDimension(R.dimen.font_size4));
			item_attrib.setLayoutParams(params4);			
				
			item_attrib.setText(currentDir.getAttrib());
			int img=currentDir.getDir_image();
		
			//0 non empty dir
  			// 1 empty dir
  			// 2 PNG file
  			//3 JPG file
  			//4 BMP file
  			//5 GIF file
  			// 6 TXT file
			// 7 apk file
			// .file
  			//9 other type
			
				
			if (currentDir.getBMP()!=null){
				imageView.setImageBitmap(currentDir.getBMP());	
			}
			
			file_name.setTextColor(Color.BLACK);


//			if (display_screen_size>5 && display_screen_size <6){
//				limit1=22;
//			}
//			else if (display_screen_size>7 && display_screen_size <8){
//				limit1=40;
//			}
//			else if (display_screen_size>7.9 && display_screen_size <9.9){
//				limit1=60;
//			}
//			else {
//				limit1=58;
//			}
			
			if (currentDir.getFullFileName().length()<limit1){
		
				file_name.setText(currentDir.getFullFileName());
			}
			
			else {
				file_name.setText(currentDir.getFullFileName().substring(0, limit1));
			}
			
			// total files:
			if (currentDir.getAttrib().contains("d")){
				size.setText(currentDir.getParsedSize());
			}
			else {			
			size.setText(currentDir.getTotalFileSizeString());
			}
			// image files:
			String a5t=String.valueOf(currentDir.getIndex())  ;
			if (a5t.length()==1){
				a5t="  "+a5t;
			}
			else if (a5t.length()==2){
				a5t=" "+a5t;
			}
			indexText.setText(a5t+"/"+myDirectoryFilesHolder.size());
			
			date1.setText(currentDir.getDate_created());

			imageView1 .setVisibility(View.INVISIBLE);
		
			
for (int u=goBackDir.size()-1;u>=0;u--){
if (currentDir1.equals(goBackDir.get(u).getCurrentDir())	&& 
		goBackDir.get(u).getLast().equals(currentDir.getFullFileName()) )
		{imageView1 .setVisibility(View.VISIBLE);file_name.setTextColor(Color.BLUE);break;}
	
}			 
			return itemView;
		}				
	}
    
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.smbmainmp3list, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {
			case R.id.copyrt:
			//act_copyright();
			init_all_prefs();
			read_all_prefs(); 
			currentDir1="";
			goBackDir.clear();
			goBackDir.add(new SMBCurrentDirectoryLastHolderMP3list(currentDir1, ""));
			get_directory();
			sort(sharedpreferences.getString(sort_type_Main_Activity, ""));
			showDialog();		
			break;
		
		case R.id.about:
			StringBuilder sb=new StringBuilder();
//			sb.append(GetMemoryStatistics.getInternalMemory(context1)+"\n");
//			sb.append("Toatl free RAM "+GetMemoryStatistics.freeRamMemorySize(context1)+" Mbytes\n");
//			sb.append("Toatl available RAM "+GetMemoryStatistics.totalRamMemorySize(context1)+" Mbytes\n");
		     AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		     alertDialogBuilder.setMessage(sb.toString()+"\n");
		      alertDialogBuilder.setPositiveButton("OK", 
		         new DialogInterface.OnClickListener() {
		         @Override
		         public void onClick(DialogInterface arg0, int arg1) {
		        	 arg0.dismiss();
		         }
		      });



		      AlertDialog alertDialog = alertDialogBuilder.create();
		      alertDialog.show();
			
			break;
			
		case R.id.getsmb:	
			Toast.makeText(getApplicationContext(), "get smb...  ", Toast.LENGTH_SHORT).show(); 
			startActivity(new Intent(getApplicationContext(), GetSMBTree.class));            		
			break;

		case R.id.refresh:
			new LoadFilesFromNetwork().execute();	
			Toast.makeText(getApplicationContext(), "refreshed...  ", Toast.LENGTH_SHORT).show(); 
			break;
			
		case R.id.exit:
//			loadbmp.myCancel();
			MainActivitySambaShare2.this.finish();
			break;
			
		case R.id.go:
			go_grid();
			break;

		case R.id.rec1:
		
			if (currentDir1.length()==0 || currentDir1.equals("") || currentDir1.equals("/")){
				Toast.makeText(getApplicationContext(), "Can not record to root folder", Toast.LENGTH_LONG).show(); 
			}
			else rec1();
			break;
			
		case R.id.goup:
			parse_current_dir();
			break;
			
		case R.id.SortTitleAsc:  // new !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			Toast.makeText(getApplicationContext(), "pressed 1", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "1");  // old
			sort("1");	 
//			for (int i=0;i<myDirectoryFilesHolder.size();i++){
//				myDirectoryFilesHolder.get(i).setIndex(i+1);
//				}			
//			     adapter.notifyDataSetChanged();				     
			break;
			
		case R.id.SortTitleDsc:
			Toast.makeText(getApplicationContext(), "pressed 2", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "2");
			sort("2");
//			for (int i=0;i<myDirectoryFilesHolder.size();i++){
//				myDirectoryFilesHolder.get(i).setIndex(i+1);
//				}			
//			     adapter.notifyDataSetChanged();				
			break;
		
			
		case R.id.SortDateAsc:
			Toast.makeText(getApplicationContext(), "pressed 3", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "3");
			sort("3");
//			for (int i=0;i<myDirectoryFilesHolder.size();i++){
//				myDirectoryFilesHolder.get(i).setIndex(i+1);
//				}			
//			     adapter.notifyDataSetChanged();							
			break;
			
		case R.id.SortDateDsc:
			Toast.makeText(getApplicationContext(), "pressed 4", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "4"); 
			sort("4");
//			Collections.sort(myDirectoryFilesHolder, compareByCreationTimeDSC);		
//			for (int i=0;i<myDirectoryFilesHolder.size();i++){
//				myDirectoryFilesHolder.get(i).setIndex(i+1);
//				}			
//			     adapter.notifyDataSetChanged();	
			break;
		case R.id.SortSizeAsc:
			Toast.makeText(getApplicationContext(), "pressed 5", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "5");
			sort("5");
//			for (int i=0;i<myDirectoryFilesHolder.size();i++){
//				myDirectoryFilesHolder.get(i).setIndex(i+1);
//				}			
//			     adapter.notifyDataSetChanged();	

			
			break;
			
		case R.id.SortSizeDsc:
			Toast.makeText(getApplicationContext(), "pressed 6", Toast.LENGTH_SHORT).show();
			add_prefs_key(sort_type_Main_Activity, "6");
			sort("6");
//			Collections.sort(myDirectoryFilesHolder, compareBySizeDSC);		
//			for (int i=0;i<myDirectoryFilesHolder.size();i++){
//				myDirectoryFilesHolder.get(i).setIndex(i+1);
//				}			
//			     adapter.notifyDataSetChanged();	

			break;
	
		case R.id.pdf:
			Toast.makeText(getApplicationContext(), "pdf", Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.setemail:
//			loadbmp.myCancel();
//			getEMAILAddress();
			break;
	
		case R.id.device:
	//		startActivity(new Intent(getApplicationContext(), MSTMemoryStatus1MainActivity.class));            	
			break;
			
				
		}
		return super.onOptionsItemSelected(item);
	}
	

	private void rec1() {
//		loadbmp.myCancel();
		add_prefs_key(directory, currentDir1);
		Toast.makeText(getBaseContext(), "GO:    "+currentDir1, Toast.LENGTH_SHORT).show();
 //   	Intent i199 = new Intent(getApplicationContext(), audiorec1.class);
//startActivityForResult(i199, 9);
	}



	private void sort(String string) {
		
		int sort1=1;			
		if (string.length()==0 || string.length()>1 || string.equals("0") ) {
			add_prefs_key(sort_type_Main_Activity, "1");
			sort1=1;
			}
		
		else {
			add_prefs_key(sort_type_Main_Activity, string);
			sort1=Integer.valueOf(string);
			}
			
//		boolean flag_sort=true;
//		DirectoryFilesHolder temp_holder_sort;
		enterDirName.setText("path:"+currentDir1);			
		
		if (sort1==1) {  // sort by name ASC
			Collections.sort(myDirectoryFilesHolder, compareByFullFileNameASC);		
		}
		else if (sort1==2) {  // sort by name DSC
			Collections.sort(myDirectoryFilesHolder, compareByFullFileNameDSC);		
		}
		else if (sort1==3) {  // sort by Time ASC
			Collections.sort(myDirectoryFilesHolder, compareByCreationTimeASC);		
		}	
		else if (sort1==4) {  // sort by Time DSC
			Collections.sort(myDirectoryFilesHolder, compareByCreationTimeDSC);		
		}	
		else if (sort1==5) {  // sort by 
			Collections.sort(myDirectoryFilesHolder, compareBySizeASC);		
		}		
		else if (sort1==6) {  // sort by 
			Collections.sort(myDirectoryFilesHolder, compareBySizeDSC);
		}	
		
		
		
		
//		if (sort1==1) {  // sort by name ASC
//		     while ( flag_sort ) 	     {
//		            flag_sort= false;    //set flag to false awaiting a possible swap
//		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
//		          		            	
//		                 	if (myDirectoryFilesHolder.get(j_sort).getFullFileName().compareToIgnoreCase(myDirectoryFilesHolder.get(j_sort+1).getFullFileName())>0) {
//		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
//		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
//		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
//		                 		           flag_sort = true;              //shows a swap occurred 
//		                  }
//		            }
//		            		           
//		      } 
//		     		     
//		    				
//		}
//		
//		else if (sort1==2) {  // sort by name DSC
//			
//		     while ( flag_sort ) 	     {
//		            flag_sort= false;    //set flag to false awaiting a possible swap
//		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
//		          		            	
//		                 	if (myDirectoryFilesHolder.get(j_sort).getFullFileName().compareToIgnoreCase(myDirectoryFilesHolder.get(j_sort+1).getFullFileName())<0) {
//		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
//		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
//		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
//		                 		           flag_sort = true;              //shows a swap occurred 
//		                  }
//		            }
//		            
//		      } 
//		     			
//		}
		
//		else if (sort1==3) {  // sort by date  ASC
//			
//		     while ( flag_sort ) 	     {
//		            flag_sort= false;    //set flag to false awaiting a possible swap
//		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
//		          		            	
//		                 	if (myDirectoryFilesHolder.get(j_sort).getParsedSeparatedDate_in_millis()>(myDirectoryFilesHolder.get(j_sort+1).getParsedSeparatedDate_in_millis())) {
//		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
//		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
//		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
//		                 		           flag_sort = true;              //shows a swap occurred 
//		                  }
//		            }
//		            
//		      } 
//		     		
//		}
//		
//		else if (sort1==4) {  // sort by date  DSC
//			
//		     while ( flag_sort ) 	     {
//		            flag_sort= false;    //set flag to false awaiting a possible swap
//		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
//		            		
//		            	if (myDirectoryFilesHolder.get(j_sort).getParsedSeparatedDate_in_millis()<(myDirectoryFilesHolder.get(j_sort+1).getParsedSeparatedDate_in_millis())) {
//		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
//		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
//		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
//		                 		           flag_sort = true;              //shows a swap occurred 
//		                  }
//		            }
//		            
//		      } 
//		     			
//		}
//		
//		////////// 
//		else if (sort1==5) {  // sort by size  ASC   
//			
//		     while ( flag_sort ) 	     {
//		            flag_sort= false;    //set flag to false awaiting a possible swap
//		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
//		          		         	
//		                 	if (myDirectoryFilesHolder.get(j_sort).getTotal_files()>myDirectoryFilesHolder.get(j_sort+1).getTotal_files()) {
//		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
//		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
//		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
//		                 		           flag_sort = true;              //shows a swap occurred 
//		                  }
//		            }
//		            
//		      }
//		     			
//		}
		
//		else if (sort1==6) {  // sort by size  DSC
//			
//		     while ( flag_sort ) 	     {
//		            flag_sort= false;    //set flag to false awaiting a possible swap
//		            for(int j_sort=0;  j_sort < myDirectoryFilesHolder.size() -1;  j_sort++ )  {
//		          		            	
//		            	if (myDirectoryFilesHolder.get(j_sort).getTotal_files()<myDirectoryFilesHolder.get(j_sort+1).getTotal_files()) {
//		                 		temp_holder_sort = myDirectoryFilesHolder.get(j_sort);                //swap elements
//		                   		myDirectoryFilesHolder.set(j_sort, myDirectoryFilesHolder.get(j_sort+1));
//		                   		myDirectoryFilesHolder.set(j_sort+1, temp_holder_sort);
//		                 		           flag_sort = true;              //shows a swap occurred 
//		                  }
//		            }
//		            
//		      }
//		     		
//		}
		
//	for (int i=0;i<myDirectoryFilesHolder.size();i++){
//		myDirectoryFilesHolder.get(i).setIndex(i+1);
//		}
//	
//	     adapter.notifyDataSetChanged();	
//	     showTitleActionBar(0);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	        if(requestCode==6)  {
				new LoadFilesFromNetwork().execute();	
//	                currentDir1	= sharedpreferences.getString(directory, "");
//	      			get_directory();
//	      			sort(sharedpreferences.getString(sort_type_Main_Activity, ""));/////////////////////////////
	                  //Toast.makeText(getApplicationContext(), "text edit returned!"+message, Toast.LENGTH_SHORT).show();  	                  
	              }
	        if(requestCode==3)     {
//                String message=data.getStringExtra("MESSAGE");             
            }
	        if(requestCode==4)     {
             
            }
	        
	        if(requestCode==9)      {
             
            }
	    }

	public void onBackPressed() {
		if (!fileOPFlag){
					finish();
		}
		else {
			try {
//			if(	GetBMPtask.getStatus()==AsyncTask.Status.RUNNING ||  
//					GetBMPtask.getStatus()==AsyncTask.Status.PENDING ){
//			GetBMPtask.cancel(true);
	Toast.makeText(this, "Can't exit - Pls wait !!!!!! ", Toast.LENGTH_LONG).show();					
			if (GetBMPtask!=null){
				if(	GetBMPtask.getStatus()==AsyncTask.Status.RUNNING ||  
						GetBMPtask.getStatus()==AsyncTask.Status.PENDING ){
					GetBMPtask.cancel(true);
				}				
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}		
		
		
//		if (inTaskProgerss){
//			loadbmp.myCancel(); // can cancel task by tak status query	
//			Toast.makeText(this, "Pls wait !!!!!! ", Toast.LENGTH_SHORT).show();			
//			}
//		else {
//		try {
//			loadbmp.myCancel();
//			parse_current_dir();
//			add_prefs_key(directory, currentDir1);
//			get_directory();
//			sort(sharedpreferences.getString(sort_type_Main_Activity, ""));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		}
//		if (!fileOPFlag){
//			finish();	
//			}
//		else {
//			Toast.makeText(this, "File Op WIP - Pls wait !!!!!! ", Toast.LENGTH_LONG).show();
//		}


	
	

	

	@Override
	public boolean onMenuOpened(int featureId, Menu menu)
	{
		if(featureId == Window.FEATURE_ACTION_BAR && menu != null){
	        if(menu.getClass().getSimpleName().equals("MenuBuilder")){
	            try{
	                Method m = menu.getClass().getDeclaredMethod(
	                    "setOptionalIconsVisible", Boolean.TYPE);
	                m.setAccessible(true);
	                m.invoke(menu, true);
	            }
	            catch(NoSuchMethodException e){
	                Log.e("TAG", "onMenuOpened", e);
	            }
	            catch(Exception e){
	                throw new RuntimeException(e);
	            }
	        }
	    }
	    return super.onMenuOpened(featureId, menu);
	}
	
	private String getModification (){
	String s="*";
	ApplicationInfo ai = null;
		try{
		     ai = getPackageManager().getApplicationInfo(getPackageName(), 0);
		     ZipFile zf = new ZipFile(ai.sourceDir);
		     ZipEntry ze = zf.getEntry("AndroidManifest.xml");
		     long time = ze.getTime();
		     s = SimpleDateFormat.getInstance().format(new java.util.Date(time));
		     zf.close();
		  }catch(Exception e){
		  }
		
		final PackageManager pm = context1.getPackageManager();
		try {
			ai = pm.getApplicationInfo(ai.packageName, 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		File file = new File(ai.publicSourceDir);
		String[] s1=file.toString().split("/");
		long size = file.length();
		return "Veresion 1.0 - Copyright Eli Rajchert \n"+
		"\nPackage:\t "+s1[s1.length-1]+
		"\n\nCreated date:\t "+s+""+
		"\nFileSize:\t\t\t "+String.valueOf(size)+" bytes";
	}
	
	private void showDialog(){
		AlertDialog alertDialog = new AlertDialog.Builder(MainActivitySambaShare2.this).create();
		alertDialog.setTitle("About DirectoryShow...");
		alertDialog.setMessage(getModification());
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
		    new DialogInterface.OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		            dialog.dismiss();
		        }
		    });
		alertDialog.show();
	
	}
	
	
	
	
	/////// async task
/*
	class LoadBitmaps extends AsyncTask<Void, Void, Void>    {
		boolean isCanceled = false;

        public void myCancel()
        {
            isCanceled = true;
        }
        
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			inTaskProgerss=true;
//			imageLoadErrorCounter=0;
		}

        @Override
        protected Void doInBackground(Void... params) {
        	MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();   
        	
        	inTaskProgerss=true;
        	int mHeight_= 60;//(int) ((tablet_width-3*margin_left-3*margin_right)/3);  // overrides the cooeff table
			int mWidth_= 60;//(int) ((tablet_width-3*margin_left-3*margin_right)/3);
			if (height!=0 && width!=0){
				mHeight_=width/12;mWidth_=width/12;
			}			

	for (int ip1=0;ip1<myDirectoryFilesHolder.size();ip1++){
       	if (isCanceled) {
            return null;
        }
       	Bitmap songImage1 = null;
		DirectoryFilesHolder cur1 = myDirectoryFilesHolder.get(ip1);          		
			if (cur1.getDir_image()==2 || cur1.getDir_image()==3 || cur1.getDir_image()==4 || cur1.getDir_image()==5){      		
        		try {
					cur1.setBMP(getResizedBitmap(mWidth_,mHeight_,
							new File(Path1, cur1.getFullFileName().toString()).toString()));
					myDirectoryFilesHolder.set(ip1, cur1);
				} catch (Exception e) {
					e.printStackTrace();
				}	
			}
			else	if (cur1.getDir_image()==1){  // MP3
				try {
					metaRetriver.setDataSource(new File(Path1,cur1.getFullFileName()).toString());
					byte[] art1 = metaRetriver.getEmbeddedPicture();
					
					if (art1.length>0){ 
						songImage1=Bitmap.createScaledBitmap(BitmapFactory.decodeByteArray(art1, 0, art1.length),
								mWidth_,
								mHeight_,
								false);
					}
			
					if (songImage1!=null){
						cur1.setBMP(songImage1);
						myDirectoryFilesHolder.set(ip1, cur1);
							}
	} catch (Exception e) {
		e.printStackTrace();
	}
}
			
			else	if (cur1.getDir_image()==11 ||  // mp4, mov,wmv
						cur1.getDir_image()==13 ||  
						cur1.getDir_image()==14 ||
						cur1.getDir_image()==25 ||
						cur1.getDir_image()==16 ||
						cur1.getDir_image()==19 ||				
						cur1.getDir_image()==26 ){
				try {
					metaRetriver.setDataSource(new File(Path1,cur1.getFullFileName()).toString());					
//					String qqq=(metaRetriver
//							.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));												
					
					songImage1=null;
					songImage1 = Bitmap.createScaledBitmap(metaRetriver.getFrameAtTime(), mWidth_, mHeight_, false);
					if (songImage1!=null){
						cur1.setBMP(songImage1);
						myDirectoryFilesHolder.set(ip1, cur1);
							}
	} catch (Exception e) {
		e.printStackTrace();
	}
}

			
			else	if (cur1.getDir_image()==28){// ePUB
//try {
//	songImage1=null;
//	EpubReader epubReader =new EpubReader();
//	Book book = epubReader.readEpub(new FileInputStream(Path1+"/"+cur1.getFullFileName().toString()));
//	songImage1 = BitmapFactory.decodeStream(book.getCoverImage().getInputStream());
//	if (songImage1!=null) {
//		cur1.setBMP(songImage1);
//		myDirectoryFilesHolder.set(ip1, cur1);
//	}
//} catch (IOException e1) {
//	e1.printStackTrace();
//}	
//catch (Exception e1) {
//	e1.printStackTrace();
}					
				
				



					




//				finally{
//
//				}
			}	
			
			
			
//			if (myDirectoryFilesHolder.size()>10 && ip1%3==0)  publishProgress();	
//		}
 /////////////////////////////////////////////////////////           	
//            	for (int ip=0;ip<myDirectoryFilesHolder.size();ip++){
//                   	if (isCanceled) {
//                        return null;
//                    }
//            		DirectoryFilesHolder cur = myDirectoryFilesHolder.get(ip);          		
//            			if (cur.getDir_image()==1){
//            				try {
//            					File fd=new File(Path1,cur.getFullFileName());
//            					metaRetriver.setDataSource(fd.toString());
//            					byte[] art1 = metaRetriver.getEmbeddedPicture();
//            					Bitmap songImage = null;
//            					if (art1.length>0) songImage = BitmapFactory.decodeByteArray(art1, 0, art1.length);
//            					if (songImage!=null){
//            						cur.setBMP(songImage);
//            						myDirectoryFilesHolder.set(ip, cur);
//            							}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//            }
//           } 
  //////////////////////////////////////////////////////////////// add song picture          	
	 	
   	       
	if (metaRetriver!=null) metaRetriver.release();	

			return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            inTaskProgerss=false;
//            imageLoadErrorCounter=0;
//            showTitleActionBar();           
//            adapter.notifyDataSetChanged();             
///			adapter.notifyDataSetChanged();
			listView.invalidate();		
			listView.smoothScrollToPosition(Integer.valueOf(sharedpreferences.getString(position_location, ""))+
		(  listView.getChildCount()  )/2);					
			showTitleActionBar(0);
            
        } 

        
        protected void onProgressUpdate(Void... values) {       	
//////			adapter.notifyDataSetChanged();
			showTitleActionBar(0);	
            }

}  // end of class
*/	

	public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) 
	   { // BEST QUALITY MATCH
	       
	       //First decode with inJustDecodeBounds=true to check dimensions
	       final BitmapFactory.Options options = new BitmapFactory.Options();
	       options.inJustDecodeBounds = true;
	       BitmapFactory.decodeFile(path, options);

	       // Calculate inSampleSize, Raw height and width of image
	       final int height = options.outHeight;
	       final int width = options.outWidth;
	       options.inPreferredConfig = Bitmap.Config.RGB_565;
	       int inSampleSize = 1;

	       if (height > reqHeight) 
	       {
	           inSampleSize = Math.round((float)height / (float)reqHeight);
	       }
	       int expectedWidth = width / inSampleSize;

	       if (expectedWidth > reqWidth) 
	       {
	           //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
	           inSampleSize = Math.round((float)width / (float)reqWidth);
	       }

	       options.inSampleSize = inSampleSize;

	       // Decode bitmap with inSampleSize set
	       options.inJustDecodeBounds = false;

	       return BitmapFactory.decodeFile(path, options);
	   }
	
	public Bitmap getResizedBitmap(int targetW, int targetH,  String imagePath) {
	    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
	    //inJustDecodeBounds = true <-- will not load the bitmap into memory
	    bmOptions.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(imagePath, bmOptions);
	    int photoW = bmOptions.outWidth;
	    int photoH = bmOptions.outHeight;
	    // Determine how much to scale down the image
	    int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
	    // Decode the image file into a Bitmap sized to fill the View
	    bmOptions.inJustDecodeBounds = false;
	    bmOptions.inSampleSize = scaleFactor;
	    bmOptions.inPurgeable = true;
	    return(BitmapFactory.decodeFile(imagePath, bmOptions));
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		if (width==0 || height==0) {	
			 height=vv.getHeight();
	         width=vv.getWidth();
ViewGroup.MarginLayoutParams p =(ViewGroup.MarginLayoutParams) enterDirName.getLayoutParams();
p.setMargins(width/80,0,width/80,0);
p.height=height/33;
p.width=(70*width)/100;
//enterDirName.setTextSize(height/110);
enterDirName.setTextSize(TypedValue.COMPLEX_UNIT_PX,
		getResources().getDimensionPixelSize(R.dimen.myFontSize2));
enterDirName.requestLayout();	      

p = (ViewGroup.MarginLayoutParams) iv1.getLayoutParams();
p.setMargins(width/80,height/240,width/80,0);
p.height=height/33;
p.width=(20*width)/100;
iv1.requestLayout();
iv1.setImageResource(R.drawable.greencircle32x32);







		       
p = (ViewGroup.MarginLayoutParams) favor1000.getLayoutParams();
p.setMargins(width/80,height/240,width/80,0);
p.height=height/20;
p.width=width/13;
favor1000.requestLayout();
           
 p = (ViewGroup.MarginLayoutParams) exit.getLayoutParams();
p.setMargins(width/80,height/240,width/80,0);
p.height=height/20;
p.width=width/13;
exit.requestLayout();
       		              
p = (ViewGroup.MarginLayoutParams) paste1000.getLayoutParams();
p.setMargins(width/80,height/240,width/80,0);
p.height=height/20;
p.width=width/13;
paste1000.requestLayout();        		              
               		           		
p =(ViewGroup.MarginLayoutParams) go.getLayoutParams();
p.setMargins(width/80,height/240,width/80,0);
p.height=height/20;
p.width=width/13;
//p4.height=100;p4.width=300;
go.requestLayout();
       		              
p = (ViewGroup.MarginLayoutParams) dir_up.getLayoutParams();
p.setMargins(width/80,height/240,width/80,0);
p.height=height/20;
p.width=width/13;
dir_up.requestLayout();   
        		           		
p = (ViewGroup.MarginLayoutParams) back.getLayoutParams();
p.setMargins(width/80,height/240,width/80,0);
p.height=height/20;
p.width=width/13;
back.requestLayout(); 

p = (ViewGroup.MarginLayoutParams) new_file1.getLayoutParams();
p.setMargins(width/80,height/240,width/80,0);
p.height=height/20;
p.width=width/13;
new_file1.requestLayout(); 

	}
super.onWindowFocusChanged(hasFocus);
	}
	
private void showTitleActionBar(int k){
//	actionBar.setTitle("View "+" sort: "+sharedpreferences.getString(sort_type_Main_Activity, ""));
	actionBar.setTitle("View  "+k);

}	


private String saveToInternalStorage(Bitmap bitmapImage){
    ContextWrapper cw = new ContextWrapper(getApplicationContext());
     // path to /data/data/yourapp/app_data/imageDir
    File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
    // Create imageDir
    File mypath=new File(directory,"profile.jpg");

    FileOutputStream fos = null;
    try {           
        fos = new FileOutputStream(mypath);
   // Use the compress method on the BitMap object to write image to the OutputStream
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
    } catch (Exception e) {
          e.printStackTrace();
    } finally {
        try {
          fos.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
    } 
    return directory.getAbsolutePath();
}

private Bitmap loadImageFromStorage(String path)
{
	Bitmap b=null;
    ContextWrapper cw = new ContextWrapper(getApplicationContext());
    // path to /data/data/yourapp/app_data/imageDir
   File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
   // Create imageDir
   File mypath=new File(directory,"profile.jpg");
    try {
//        File f=new File(path, "profile.jpg");
        b = BitmapFactory.decodeStream(new FileInputStream(mypath));
    } 
    catch (FileNotFoundException e) 
    {
        e.printStackTrace();
    }
return b;
}



//@Override
//public void onTaskDone(Integer y) {
//Toast.makeText(this, "@@@!!!!!!!!!!!!!!!!!! "+String.valueOf(y), Toast.LENGTH_SHORT).show();
//get_directory();
//}

/*
private void getEMAILAddress(){
	// get prompts.xml view
	LayoutInflater li = LayoutInflater.from(context1);
	View promptsView = li.inflate(R.layout.prompts, null);

	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
			context1);

	// set prompts.xml to alertdialog builder
	alertDialogBuilder.setView(promptsView);

	final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
	final EditText userInput1 = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput1);
	 final CheckBox chkbox = (CheckBox) promptsView.findViewById(R.id.chkbox);
	 final CheckBox chkbox1 = (CheckBox) promptsView.findViewById(R.id.chkbox1);

	 chkbox.setChecked(sharedpreferences.getBoolean(EMAILCHECK, false));
	 chkbox1.setChecked(sharedpreferences.getBoolean(EMAILCHECK1, false));
	 chkbox.setOnClickListener(new OnClickListener() {

		  @Override
		  public void onClick(View v) {
	                //is chkIos checked?
			if (((CheckBox) v).isChecked()) {
				add_prefs_keyboolean(EMAILCHECK, true);
				add_prefs_keyboolean(EMAILCHECK1, false);
				 chkbox.setChecked(sharedpreferences.getBoolean(EMAILCHECK, false));
				 chkbox1.setChecked(sharedpreferences.getBoolean(EMAILCHECK1, false));
				Toast.makeText(context1,"checked", Toast.LENGTH_LONG).show();
			}
			else {
				add_prefs_keyboolean(EMAILCHECK, false);
				 chkbox.setChecked(sharedpreferences.getBoolean(EMAILCHECK, false));
				 chkbox1.setChecked(sharedpreferences.getBoolean(EMAILCHECK1, false));	
				Toast.makeText(context1,"not checked", Toast.LENGTH_LONG).show();

			}
		  }
		});
	
	 chkbox1.setOnClickListener(new OnClickListener() {

		  @Override
		  public void onClick(View v) {
	                //is chkIos checked?
			if (((CheckBox) v).isChecked()) {
				add_prefs_keyboolean(EMAILCHECK1, true);
				add_prefs_keyboolean(EMAILCHECK, false);
				 chkbox.setChecked(sharedpreferences.getBoolean(EMAILCHECK, false));
				 chkbox1.setChecked(sharedpreferences.getBoolean(EMAILCHECK1, false));				
				
				Toast.makeText(context1,"checked 1", Toast.LENGTH_LONG).show();
			}
			else {
				add_prefs_keyboolean(EMAILCHECK1, false);
				 chkbox.setChecked(sharedpreferences.getBoolean(EMAILCHECK, false));
				 chkbox1.setChecked(sharedpreferences.getBoolean(EMAILCHECK1, false));	
				Toast.makeText(context1,"not checked 1", Toast.LENGTH_LONG).show();

			}
		  }
		});
	 
	userInput.setText(sharedpreferences.getString(EMAILADDRESS, ""));
	userInput1.setText(sharedpreferences.getString(EMAILADDRESS1, ""));
	// set dialog message
	alertDialogBuilder
			.setCancelable(false)
			.setPositiveButton("Save",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
							// get user input and set it to result
							// edit text
Toast.makeText(context1, "EMAIL   "+"   "+userInput.getText().toString()+"  saved...", Toast.LENGTH_SHORT).show();
add_prefs_key(EMAILADDRESS, userInput.getText().toString());
add_prefs_key(EMAILADDRESS1, userInput1.getText().toString());
//							result.setText(userInput.getText());
						}
					})
			.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int id) {
							dialog.cancel();
						}
					});

	// create alert dialog
	AlertDialog alertDialog = alertDialogBuilder.create();

	// show it
	alertDialog.show();

}
*/

@Override
public boolean onKeyLongPress(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) 
    {
    	Toast.makeText(context1, "pressed long back   ", Toast.LENGTH_SHORT).show();
	//	loadbmp.myCancel();
		add_prefs_key(directory, currentDir1);
        Intent intent=new Intent();  
        intent.putExtra("MESSAGE","EMPTY");  
        setResult(2,intent);  
		finish();
        return true;
    }
    return super.onKeyLongPress(keyCode, event);
}

private void vibrate(int v1){
Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
v.vibrate(v1);
	}


//For to Delete the directory inside list of files and inner Directory
public static boolean deleteDir(File dir) {
 if (dir.isDirectory()) {
     String[] children = dir.list();
     for (int i=0; i<children.length; i++) {
         boolean success = deleteDir(new File(dir, children[i]));
         if (!success) {
             return false;
         }
     }
 }
 // The directory is now empty so delete it
 
 
 return dir.delete();
}


Comparator<SMBDirectoryFilesHolder> compareByFullFileNameASC = new Comparator<SMBDirectoryFilesHolder>() {
    @Override
    public int compare(SMBDirectoryFilesHolder o1, SMBDirectoryFilesHolder o2) {
        return o1.getFullFileName().compareTo(o2.getFullFileName());
    }
};

Comparator<SMBDirectoryFilesHolder> compareByFullFileNameDSC = new Comparator<SMBDirectoryFilesHolder>() {
    @Override
    public int compare(SMBDirectoryFilesHolder o1, SMBDirectoryFilesHolder o2) {
        return o2.getFullFileName().compareTo(o1.getFullFileName());
    }
};




Comparator<SMBDirectoryFilesHolder> compareByCreationTimeASC = new Comparator<SMBDirectoryFilesHolder>() {
    @Override
    public int compare(SMBDirectoryFilesHolder o1, SMBDirectoryFilesHolder o2) {
String o10=String.valueOf(o1.getParsedSeparatedDate_in_millis());
String o11=String.valueOf(o2.getParsedSeparatedDate_in_millis());
       return o10.compareTo(o11);
    }
};
//
Comparator<SMBDirectoryFilesHolder> compareByCreationTimeDSC = new Comparator<SMBDirectoryFilesHolder>() {
    @Override
   public int compare(SMBDirectoryFilesHolder o1, SMBDirectoryFilesHolder o2) {
    	String o10=String.valueOf(o1.getParsedSeparatedDate_in_millis());
    	String o11=String.valueOf(o2.getParsedSeparatedDate_in_millis());
    	       return o11.compareTo(o10);
    }
};

Comparator<SMBDirectoryFilesHolder> compareBySizeASC = new Comparator<SMBDirectoryFilesHolder>() {
    @Override
    public int compare(SMBDirectoryFilesHolder o1p, SMBDirectoryFilesHolder o2p) {
    	return (o1p.getTotal_files() - o2p.getTotal_files()   );    
    }
};
//
Comparator<SMBDirectoryFilesHolder> compareBySizeDSC = new Comparator<SMBDirectoryFilesHolder>() {
    @Override
   public int compare(SMBDirectoryFilesHolder o1, SMBDirectoryFilesHolder o2) {
    	return (o2.getTotal_files() - o1.getTotal_files()   );    

    }
};


/*
////////////////////////////////////////
private void setIntent1 (String file2Play) {
	String s18="ERR";
	String s[]=file2Play.split("/");
		if (s.length>0) s18=s[s.length-1];
		
	//setContentView(R.layout.activity_main_activity_notofication_player1);
    notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    builder = new Notification.Builder(this);
    remoteViews = new RemoteViews(getPackageName(),R.layout.custom_notificationllplay);
    MediaDataRetriever1 md=getMidiaretriever(file2Play);
    Bitmap b=md.getB();
    String songName=md.getSongName();
    String artistName=md.getArtistName();
    remoteViews.setImageViewBitmap(R.id.notif_icon, b);  
//    remoteViews.setImageViewResource(R.id.notif_icon,R.drawable.audioplayer);
    if (songName.equals("----")) songName=s18;
    remoteViews.setTextViewText(R.id.notif_title,songName+"\n"+artistName);
    
    remoteViews.setTextColor(R.id.notif_title,Color.RED);
    remoteViews.setTextViewTextSize(R.id.notif_title, TypedValue.COMPLEX_UNIT_DIP, 24);

    notification_id = (int) System.currentTimeMillis();
    notification_id1 = (int) System.currentTimeMillis()/2;
    notification_id2 = (int) System.currentTimeMillis()/12; 
    notification_id3 = (int) System.currentTimeMillis()/7; 
    notification_pause=(int) System.currentTimeMillis()/17; 
    Intent button_intent = new Intent("button_play1dirShowma12");
//    button_intent.putExtra("id",notification_id);
    button_intent.putExtra("key","a");
    button_intent.putExtra("file",file2Play);   
    button_intent.putExtra("songname",songName);
    button_intent.putExtra("songartist",artistName);     
    PendingIntent button_pending_event = PendingIntent.getBroadcast(context1,notification_id3,
            button_intent,0);	                
    remoteViews.setOnClickPendingIntent(R.id.button,button_pending_event);
    
    Intent button_intent1 = new Intent("button_play1dirShowma12");
//       button_intent1.putExtra("id",notification_id1);
    button_intent1.putExtra("key","b");
    button_intent1.putExtra("file",file2Play);
    button_intent1.putExtra("songname",songName);
    button_intent1.putExtra("songartist",artistName);   
 
    PendingIntent button_pending_event1 = PendingIntent.getBroadcast(context1,notification_id1,
            button_intent1,0);	                
    remoteViews.setOnClickPendingIntent(R.id.button1,button_pending_event1);


	Intent button_intent2 = new Intent("button_play1dirShowma12");
	button_intent2.putExtra("key","c");
	   button_intent2.putExtra("file",file2Play); 
	    button_intent2.putExtra("songname",songName);
	    button_intent2.putExtra("songartist",artistName);   

	   PendingIntent button_pending_event2 = PendingIntent.getBroadcast(context1,notification_id2,
	           button_intent2,0);	                
	   remoteViews.setOnClickPendingIntent(R.id.button2,button_pending_event2);
	   
		Intent button_intent38 = new Intent("button_play1dirShowma12");
		button_intent38.putExtra("key","d");
		button_intent38.putExtra("file",file2Play);  
		button_intent38.putExtra("songname",songName);
		button_intent38.putExtra("songartist",artistName);   
		 
		   PendingIntent button_pending_event28 = PendingIntent.getBroadcast(context1,notification_pause,
		           button_intent38,0);	                
		   remoteViews.setOnClickPendingIntent(R.id.pause,button_pending_event28);
    
    Intent notification_intent = new Intent(context1,MainActivityDirShow1.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context1,0,notification_intent,0);
    
    builder.setSmallIcon(R.drawable.ic_launcher)
            .setAutoCancel(true)	                        
            .setContent(remoteViews)
//         .setCustomBigContentView(remoteViews)                      
            .setContentIntent(pendingIntent).build();
    Notification n=builder.build();	                
    notificationManager.notify(notification_id,n);
//////////////////////////
    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivityDirShow1.this);
    builder.setCancelable(false);
    builder.setTitle("Hidden player");
    builder.setMessage("Operates from notification bar");
    builder.setPositiveButton("OK!!!", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int id) {
          Toast.makeText(context1, "Started - open notification!!!  ", Toast.LENGTH_LONG).show();
          finish();
        }
    })  ;
//    dialog.getWindow().setGravity(Gravity.BOTTOM);
    // Create the AlertDialog object and return it
    builder.create();
AlertDialog ad = builder.create();
ad.getWindow().setGravity(Gravity.TOP);
//ad.show();
}
/////////////////////////////////////////


*/
//public void disableBroadcastReceiver(){
//    ComponentName receiver = new ComponentName(this, Button_listenerNotifPlayer1.class);
//    PackageManager pm = this.getPackageManager();
//    pm.setComponentEnabledSetting(receiver,
//            PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//            PackageManager.DONT_KILL_APP);
//    Toast.makeText(this, "Disabled broadcst receiver", Toast.LENGTH_SHORT).show();
//   }   

////////////////////////////
//public void enableBroadcastReceiver(){
//    ComponentName receiver = new ComponentName(this, Button_listenerNotifPlayer1.class);
//    PackageManager pm = this.getPackageManager();
// 
//    pm.setComponentEnabledSetting(receiver,
//            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//            PackageManager.DONT_KILL_APP);
//    Toast.makeText(this, "Enabled broadcast receiver", Toast.LENGTH_SHORT).show();
//   }


private  SMBMediaDataRetriever1MP3list getMidiaretriever(String filePlay) {
    Bitmap bitmap = null;
    String songName="----";
    String artistName="----";
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
    try {
        retriever.setDataSource(filePlay); 
        byte[] bytes = retriever.getEmbeddedPicture(); 
        if(bytes!=null){
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); 
        }
        
        if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)!=null){
        	songName=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);}

        if (retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)!=null){
        	artistName=retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);}

        
    } catch (Exception e) {
    	bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.audioplayer);
		songName="----";
		artistName="----";
        e.printStackTrace();
    } finally {
    	if (songName.length()<2) songName="----";
    	if (artistName.length()<2) songName="----";
    	if (bitmap==null || bitmap.getByteCount()<100){
    		bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.audioplayer);
    	}
        try {
            retriever.release();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
    return new SMBMediaDataRetriever1MP3list(bitmap, songName, artistName);
}









/////////////////////////////////// 2nd async task
/////// async task

class LoadFilesFromNetwork extends AsyncTask<Void, Integer, ArrayList<SMBDirectoryFilesHolder>   >    {
	boolean isCanceled = false;
	int type=0;
	String lastError="",url="";
	int errors=0;
    public void myCancel()
    {
        isCanceled = true;
    }
    
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		inTaskProgerss=true;
	}

    @Override
    protected ArrayList<SMBDirectoryFilesHolder> doInBackground(Void... params) {
        ArrayList<SMBDirectoryFilesHolder> tmpDirectoryFilesHolder = new  ArrayList<SMBDirectoryFilesHolder>();
        tmpDirectoryFilesHolder.clear();
    	Bitmap bitmap=null;
        NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",username,password);
//    	ArrayList<String> alDOMAIN=getAllDomainFiles(auth2);
//        String path = "smb://" + ip + "//" + sharedFolder;
//        path = "smb://" + ip + "//" + sharedFolder+"newDirectry//";// inner

		try {
			smbFile = new SmbFile(path,auth2);
			        try {
//////////						String[] files = smbFile.list();
						smbFiles=smbFile.listFiles();
		 
					} catch (SmbException e) {
						e.printStackTrace();
						errors++;
						lastError=e.getMessage();
					}         
					} catch (MalformedURLException e) {
						e.printStackTrace();
						errors++;
						lastError=e.getMessage();
					}
 if (smbFiles!=null && errors==0) {
		for (int i=0;i<smbFiles.length;i++){
			 try {
			 long createTime=smbFiles[i].createTime();
			 boolean br=smbFiles[i].canRead();
	         boolean bw=smbFiles[i].canWrite();
	         boolean be=smbFiles[i].exists();
	         boolean bdir=smbFiles[i].isDirectory();
	         boolean bfile=smbFiles[i].isFile();
	         boolean bhid=smbFiles[i].isHidden();	
	         String attrib="";
	         if (bdir) attrib+="d";
	         else attrib+=" ";
	         if (bfile) attrib+="f";
	         else attrib+=" ";
	         if (bhid) attrib+="h";
	         else attrib+=" ";
	         if (be) attrib+="x";
	         else attrib+=" ";
	         if (bw) attrib+="w";
	         else attrib+=" ";
	         if (br) attrib+="r";
	         else attrib+=" "; 
	         
	         
	         int attr =smbFiles[i].getAttributes();
	         int  length=smbFiles[i].getContentLength();
	         long date1=smbFiles[i].getDate();
	         long modif=smbFiles[i].lastModified();          
//			 long  length1=smbFiles[i].length();
			 String smbString=smbFiles[i].getURL().getPath();
			 
			 
			    if (bfile) {
		        	type=getFileTypeID(smbString) ;  
			    			}
			    else {
			    		type = 0;  // file type is dir
			    		length=smbFiles[i].listFiles().length;
			    	    }
			    
				String[] s0=smbString.split("/{2}");  // get fr0m net
				url = path+ s0[s0.length-1];	  // get fr0m net	
		    
			    
			 if (type>=2 && type<=5){	 
				 try {
//					 bitmap=GetSBitMapfromSMBFile.getBitmapFromSMBFile(url,
//							username,password,
//							BitmapFactory.decodeResource(getResources(), resid_[type]), thumbNailX,thumbNailY);					 
					 bitmap = BitmapFactory.decodeResource(getResources(), resid_[type]);
					 
					 
/*					 
					 	int inWidth = 0; int inHeight = 0;				    
					    int dstWidth=120; int dstHeight=120;
					    InputStream in = new SmbFileInputStream(new SmbFile(url, auth2));
					    // decode image size (decode metadata only, not the whole image)
					    BitmapFactory.Options options = new BitmapFactory.Options();
					    options.inJustDecodeBounds = true;
					    BitmapFactory.decodeStream(in, null, options);
					    in.close();
					    in = null;

					    // save width and height
					    inWidth = options.outWidth;
					    inHeight = options.outHeight;
					    // decode full image pre-resized
					    in = new SmbFileInputStream(new SmbFile(url, auth2));
					    options = new BitmapFactory.Options();
					    // calc rought re-size (this is no exact resize)
					    options.inSampleSize = Math.max(inWidth/dstWidth, inHeight/dstHeight);
					    // decode full image
					    Bitmap roughBitmap = BitmapFactory.decodeStream(in, null, options);

					    // calc exact destination size
					    Matrix m = new Matrix();
					    RectF inRect = new RectF(0, 0, roughBitmap.getWidth(), roughBitmap.getHeight());
					    RectF outRect = new RectF(0, 0, dstWidth, dstHeight);
					    m.setRectToRect(inRect, outRect, Matrix.ScaleToFit.CENTER);
					    float[] values = new float[9];
					    m.getValues(values);

					    // resize bitmap
					    bitmap = Bitmap.createScaledBitmap(roughBitmap,
					    		(int) (roughBitmap.getWidth() * values[0]),
					    		(int) (roughBitmap.getHeight() * values[4]), true);
*/					    
					    
					    
				} catch (Exception e) {
					 bitmap = BitmapFactory.decodeResource(getResources(), resid_[type]);
					e.printStackTrace();
				}				 
			 }
			 else {
				 bitmap = BitmapFactory.decodeResource(getResources(), resid_[type]);
			 }
///////////////////////////////////////////////////////////////////////////////////////			
				String TotalFileSizeString=null;
				
				if (length<1024) {
					TotalFileSizeString=String.valueOf(length+" b")  ;
				}
				else if (length<1048576) {
					TotalFileSizeString=String.valueOf(length/1024)+" kb"  ;
				}
				
				else {
					TotalFileSizeString=String.valueOf(length/1048576)+" mb"  ;
				}
				SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy  HH:mm", Locale.ENGLISH);
		 	    formatter.setLenient(false); 
		 	    String dateModified=formatter.format(modif);
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd.MM.yyyy  HH:mm", Locale.ENGLISH);
		 	    formatter1.setLenient(false); 
		 	    String dateCreated=formatter1.format(createTime);
				String a1=path;
				String a2=smbString;
				String[] s10=a2.split("/{2}");
				String a3=s10[s10.length-1];
				String a4=a1+a3+"//";
				
				
				tmpDirectoryFilesHolder.add(new SMBDirectoryFilesHolder(type,  //dir type =type of file....
						length,
						length,
						smbString,
						"M "+dateModified + "     C "+dateCreated  ,
						smbString  ,
						i+1,
						attrib ,
						date1 , 
						bitmap,
						TotalFileSizeString,
						dateCreated,
						a4
						));	
			
			publishProgress(  new Integer[]{i,smbFiles.length}    );	 
			} catch (SmbException e) {
				e.printStackTrace();
			}
		}
    }	
// else {
//Toast.makeText(getApplicationContext(), "Directory not found!!!!", Toast.LENGTH_SHORT).show();
// }     
		return tmpDirectoryFilesHolder;
    }

    @Override
    protected void onPostExecute(ArrayList<SMBDirectoryFilesHolder> resultAR) {
        super.onPostExecute(resultAR);
        if (errors==0){
        inTaskProgerss=false;
        myDirectoryFilesHolder.clear();
        myDirectoryFilesHolder.addAll(resultAR);   
		adapter.notifyDataSetChanged();
		listView.invalidate();		
//		listView.smoothScrollToPosition(Integer.valueOf(sharedpreferences.getString(position_location, ""))+
//	(  listView.getChildCount()  )/2);					
//		showTitleActionBar(jj);
        hideProgressDialogWithTitle();
 		  enterDirName.setText(path);  
 		  
//////////		new AsyncTaskgetAllBitmaps().execute();

		GetBMPtask = new AsyncTaskgetAllBitmaps();
		GetBMPtask.execute();	
	
        }
        else if (errors!=0){
        	Toast.makeText(getApplicationContext(),
        			"ERROR...== "+errors+" "+lastError+" "+username+" "+password,
        			Toast.LENGTH_LONG).show();
	
        }
    } 

    
    protected void onProgressUpdate(Integer... values) {       	
//		adapter.notifyDataSetChanged();
//		listView.invalidate();		
		showProgressDialogWithTitle("Loading files in folder.....", "Loading file "+values[0]+" of "+values[1]);
        }

}  // end of class


// Method to show Progress bar
private void showProgressDialogWithTitle(String title,String substring) {
    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    //Without this user can hide loader by tapping outside screen
    pd.setCancelable(false);
    //Setting Title
    pd.setTitle(title);
    pd.setMessage(substring);
    pd.show();

}

// Method to hide/ dismiss Progress bar
private void hideProgressDialogWithTitle() {
    pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    pd.dismiss();
}

private int getScreenLimits(){
SMBdispl sl =new SMBdispl();
display_screen_size=sl.getScreenInches();
	if (display_screen_size>5 && display_screen_size <6){
		limit1= 22;
	}
	else if (display_screen_size>7 && display_screen_size <8){
		limit1= 40;
	}
	else if (display_screen_size>7.9 && display_screen_size <9.9){
		limit1=60;
	}
	else {
		limit1=58;
	}
	return limit1;
	
	
}


private ArrayList<String> getAllDomainFiles(NtlmPasswordAuthentication auth2){
	ArrayList<String> al =new ArrayList<String>();
    int y=0;
	    SmbFile[] domains,servers = null;
     try {
         domains = (new SmbFile("smb://")).listFiles();
         for (int i = 0; i < domains.length; i++) {
//             System.out.println(domains[i]);
             servers = domains[i].listFiles();
             for (int j = 0; j < servers.length; j++) {
 //                System.out.println("\t"+servers[j]);
             }

         }
     } catch (SmbException e) {
         e.printStackTrace();
     } catch (MalformedURLException e) {
         e.printStackTrace();
     }     
     try {     	
 //        NtlmPasswordAuthentication auth3 = new NtlmPasswordAuthentication("",username,password);
 	     SmbFile sm = null;	
//         SmbFile		smbFile4 = servers[4];  
// 	     	 for (int j=0;j<servers.length;j++){
		 try {
			sm = new SmbFile(servers[4].toString(),auth2);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 SmbFile[] ffa=sm.listFiles(); //  needs auth			
			for (int i=0;i<ffa.length;i++){
				al.add(ffa[i].toString());
			}
//	 }	
 	     	 
 	     
 	     
//  catch (MalformedURLException e) {
//		e.printStackTrace();
//	}
	
		} catch (SmbException e) {
			e.printStackTrace();
		}
	return al;
     
	
	
	
}

private class AsyncTaskCreateFile extends AsyncTask<String, Integer, String> {
	@Override
    protected void onPreExecute() {
       super.onPreExecute();
		  enterDirName.setText(path);  
  		  iv1.setImageResource(R.drawable.redcircle32x32);
       fileOPFlag=true;
    }
    @Override
    protected String doInBackground(String... strings) {
   	    NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, username, password);

   	    
   	    SmbFile file = null;
		if (strings[0].equals(CREATEFILE)){
		try {
			file = new SmbFile(strings[1]+strings[2], auth);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		    try {
		       file.createNewFile();
		        } catch (IOException e) {
		        e.printStackTrace();
		        }  
		}// end of create file
		
		else	if (strings[0].equals(CREATEDIR)){	
			try {
				file = new SmbFile(strings[1]+strings[2], auth);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			    try {
			       file.mkdirs();
			        } catch (IOException e) {
			        e.printStackTrace();
			    }  
		}	
		
		else	if (strings[0].equals(DELETEFILE)){	
			try {
				file = new SmbFile(strings[1]+strings[2], auth);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			    try {
			       file.delete();
			       
			   
			       
			        } catch (IOException e) {
			        e.printStackTrace();
			    }  
		}		
		
		else	if (strings[0].equals(RENAMEFILE)){	
			SmbFile fileOld = null,fileNew = null;
			try {
				fileOld = new SmbFile(strings[1], auth);
				fileNew = new SmbFile(strings[2], auth);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			    try {
			       fileOld.renameTo(fileNew);
			        } catch (IOException e) {
			        e.printStackTrace();
			    }  
		}	
		
		else	if (strings[0].equals(COPYFILE)){	
			SmbFileOutputStream out=null;
			InputStream in=null;
			long total = 0;
			long lenghtOfFile=0;
				try {
					ArrayList<String> asp =new ArrayList<String>();
					asp.addAll(CopyMoveArray);
					String patha=path+CopyMoveArray.get(2);
 					SmbFile	fileOld = new SmbFile(CopyMoveArray.get(0), auth);
					SmbFile	fileNew = new SmbFile(patha, auth);		
					try {
						in = new SmbFileInputStream((fileOld));
						out= new SmbFileOutputStream(fileNew);					
					    lenghtOfFile=fileOld.length();
					    // Copy the bits from Instream to Outstream
					    byte[] buf = new byte[6*1024];
					    int len;
					    while ((len = in.read(buf)) > 0) {
					    	total+=len;
//					    	publishProgress(""+(int)((total*100)/lenghtOfFile),String.valueOf(total),String.valueOf(lenghtOfFile)  );
					    	out.write(buf, 0, len);					    	
							publishProgress(  new Integer[]{(int) lenghtOfFile,(int) total}    );	 
					    }
					    in.close();
					    out.flush();
					    out.close();
					} catch (MalformedURLException e) {
//						exeption1=e.toString();
					    e.printStackTrace();
					} catch (IOException e) {
//						exeption1=e.toString();
					    e.printStackTrace();
						}				
//					fileOld.copyTo(fileNew);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}// end if  		
		
		else	if (strings[0].equals(COPYFILESMBFAR)){	
			SmbFileOutputStream out=null;
			InputStream in=null;
			long total = 0;
			long lenghtOfFile=0;
			
	  	    NtlmPasswordAuthentication authfar = new NtlmPasswordAuthentication(null,
	  	    		CopyMoveArray.get(3), CopyMoveArray.get(4));

			
				try {
					ArrayList<String> asp =new ArrayList<String>();
					asp.addAll(CopyMoveArray);
					String patha=path+CopyMoveArray.get(2);
 					SmbFile	fileOld = new SmbFile(CopyMoveArray.get(0), 
 							authfar);////////
					SmbFile	fileNew = new SmbFile(patha, auth);		
					try {
						in = new SmbFileInputStream((fileOld));
						out= new SmbFileOutputStream(fileNew);					
					    lenghtOfFile=fileOld.length();
					    // Copy the bits from Instream to Outstream
					    byte[] buf = new byte[6*1024];
					    int len;
					    while ((len = in.read(buf)) > 0) {
					    	total+=len;
//					    	publishProgress(""+(int)((total*100)/lenghtOfFile),String.valueOf(total),String.valueOf(lenghtOfFile)  );
					    	out.write(buf, 0, len);					    	
							publishProgress(  new Integer[]{(int) lenghtOfFile,(int) total}    );	 
					    }
					    in.close();
					    out.flush();
					    out.close();
					} catch (MalformedURLException e) {
//						exeption1=e.toString();
					    e.printStackTrace();
					} catch (IOException e) {
//						exeption1=e.toString();
					    e.printStackTrace();
						}				
//					fileOld.copyTo(fileNew);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
		}// end if  		
		
		else	if (strings[0].equals(COPYFILELOCAL2SMB)){	
		SmbFileOutputStream out=null;
		SmbFile	fileNew=null;
	    long total = 0;
		long lenghtOfFile=0;
		String[] st=copyMoveFileFromLocal.split(",");		
		File fromFile = new File(st[0]+File.separator+st[1]);
		try {
//			fileNew = new SmbFile(enterDirName.getText().toString(), auth);
			fileNew = new SmbFile(path+st[1], auth);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}	
 
		try {
			InputStream in = new FileInputStream((fromFile));
			out= new SmbFileOutputStream(fileNew);					
		    lenghtOfFile=fromFile.length();
		    // Copy the bits from Instream to Outstream
		    byte[] buf = new byte[6*1024];
		    int len;

		    while ((len = in.read(buf)) > 0) {
		    	total+=len;
//		    	publishProgress(""+(int)((total*100)/lenghtOfFile),String.valueOf(total),String.valueOf(lenghtOfFile)  );
		    	out.write(buf, 0, len);		    	
				publishProgress(  new Integer[]{(int) lenghtOfFile,(int) total}    );	 
		    }
		    in.close();
		    out.flush();
		    out.close();

		} catch (MalformedURLException e) {
//			exeption1=e.toString();
		    e.printStackTrace();
		} catch (IOException e) {
//			exeption1=e.toString();
		    e.printStackTrace();
			}
		
		
		
		
		//   	String st10=((myDirectoryFilesHolder.get(info.position).getFullFileName()));

		}
		
		
		
		
		
		
		
		
		
		
		
       return strings[0];
    }
    @Override
    protected void onPostExecute(String typeOperation) {
       super.onPostExecute(typeOperation);
		  enterDirName.setText(path);  
  		  iv1.setImageResource(R.drawable.greencircle32x32);
       fileOPFlag=false;
       if (typeOperation.equals(CREATEFILE)){
		Toast.makeText(getApplicationContext(), "done file created", Toast.LENGTH_SHORT).show();
       			}
       else if (typeOperation.equals(CREATEDIR)){
		Toast.makeText(getApplicationContext(), "done dir created", Toast.LENGTH_SHORT).show();
       			}
       else if (typeOperation.equals(DELETEFILE)){
		Toast.makeText(getApplicationContext(), "done file deleted", Toast.LENGTH_SHORT).show();
       			}
       else if (typeOperation.equals(RENAMEFILE)){
   		Toast.makeText(getApplicationContext(), "done file rename", Toast.LENGTH_SHORT).show();
          			}
       else if (typeOperation.equals(COPYFILE)){
   		Toast.makeText(getApplicationContext(), "done file SMB 2 SMB copy", Toast.LENGTH_SHORT).show();
          			}
       else if (typeOperation.equals(COPYFILELOCAL2SMB)){
   		Toast.makeText(getApplicationContext(), "done file Local 2 SMB copy", Toast.LENGTH_SHORT).show();
          			}
       
		new LoadFilesFromNetwork().execute();	

       }
    
    protected void onProgressUpdate(Integer... values) {       	
		showProgressDialogWithTitle("Loading file.....", "Loading file "+
				values[1]+" of "+values[0]+" Bytes");
        }
    
    }

private class AsyncTaskgetAllBitmaps extends AsyncTask<String, Integer, String> {
	
	boolean isCanceled = false;
	boolean colorCircle=false;
    public void myCancel()     {
        isCanceled = true;
    	}
	
	@Override
    protected void onPreExecute() {
       super.onPreExecute();
       fileOPFlag=true;
		  enterDirName.setText(path);  
		  iv1.setImageResource(R.drawable.redcircle32x32);
    }
    @Override
    protected String doInBackground(String... strings) {
    	if (cancelBmpLoad){
		for (int i=0;i<myDirectoryFilesHolder.size();i++){
            if (isCanceled) {
                return null;
            	}			
			int type=myDirectoryFilesHolder.get(i).getDir_image();
			int len=myDirectoryFilesHolder.get(i).getTotal_files();
			if (type>=2 && type<=5 && len<MAXPICTURESIZETOSHOW   ){
				SMBDirectoryFilesHolder dh=myDirectoryFilesHolder.get(i);
				String fp=dh.getFullPathFileNameSMB();
				Bitmap bitmap = null;
				try {
					bitmap=SMBGetSBitMapfromSMBFile.getBitmapFromSMBFile(fp,
					username,password,
					BitmapFactory.decodeResource(getResources(), resid_[type]), thumbNailX,thumbNailY);
//		bitmap=GetSBitMapfromSMBFile.getBitmapSMB(fp,
//			username,password,
//			BitmapFactory.decodeResource(getResources(), resid_[type]), thumbNailX,thumbNailY);

				
				} catch (Exception e) {
					 bitmap = BitmapFactory.decodeResource(getResources(), resid_[type]);
					e.printStackTrace();
				}
				finally {
				dh.setBMP(bitmap);
				myDirectoryFilesHolder.set(i, dh);
				}
			colorCircle=!colorCircle;
				publishProgress(  new Integer[]{i,myDirectoryFilesHolder.size()}    );	 ///////
			}// if type.....
//		}
		}  // end for
    }
       return null;
    }
    @Override
    protected void onPostExecute(String typeOperation) {
       super.onPostExecute(typeOperation);
       fileOPFlag=false;
		  enterDirName.setText(path);  
		  iv1.setImageResource(R.drawable.greencircle32x32);
 //      myDirectoryFilesHolder.addAll(resultAR);   
		adapter.notifyDataSetChanged();
		listView.invalidate();		
//        hideProgressDialogWithTitle();
       }
    protected void onProgressUpdate(Integer... values) {  
    	if (colorCircle){
  		  iv1.setImageResource(R.drawable.orangecircle32x32);
    	}
    	else {
  		  iv1.setImageResource(R.drawable.redcircle32x32);
    	}
		adapter.notifyDataSetChanged();
		listView.invalidate();		
//		showProgressDialogWithTitle("Loading files in folder.....", "Loading file "+values[0]+" of "+values[1]);
        }

	@Override
	protected void onCancelled(String result) {
	      fileOPFlag=false;
		  iv1.setImageResource(R.drawable.greencircle32x32);

	}


    
    
    
    }














//////////////////////////
}  // end of class

//////////////////////////////////////////////////
/*
//-----------------------[code]---------------------------------//
SmbFile[] domains;
      try {
          domains = (new SmbFile("smb://")).listFiles();
          for (int i = 0; i < domains.length; i++) {
              System.out.println(domains[i]);
              SmbFile[] servers = domains[i].listFiles();
              for (int j = 0; j < servers.length; j++) {
                  System.out.println("\t"+servers[j]);
              }
          }
      } catch (SmbException e) {
          e.printStackTrace();
      } catch (MalformedURLException e) {
          e.printStackTrace();
      }
//------------------------[/code]----------------------------------------//
      */

//String str1 = str.replaceFirst("/{2}", "/");


//bitmap=GetSBitMapfromSMBFile.getBitmapFromSMBFile(url,
//username,password,
//BitmapFactory.decodeResource(getResources(), resid_[type]), thumbNailX,thumbNailY);	


//		File from = new File(CopyMoveArray.get(0)+File.separator+CopyMoveArray.get(1));

