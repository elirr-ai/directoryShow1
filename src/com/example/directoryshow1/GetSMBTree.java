package com.example.directoryshow1;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class GetSMBTree  extends Activity{
	ArrayList<String> al =new ArrayList<String>();
	ArrayList<SMBHolder1> alh=new ArrayList<SMBHolder1>();
	Button btBack,dummy;
	EditText userName,PassWord;
	ListView lv;
	ProgressDialog pd;
    SmbFile[] domains,servers = null;
    GetSMBTreeAsyncTask gbsmb;
    int positionMain;
	String username= "";
	String password= "";		
	int OPtype=0;

	final static String SHARED_PREFS_FILE="SHARED_PREFS_FILE";
	final static String SMBLOCATIONS="SMBLOCATIONS";
	final static String TAG="TAG";

	Context context=this;
	ActionBar actionBar;
	
	private String copyMoveFile="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// check wifi is on
        SMBConnectivityStatus1 cn =new SMBConnectivityStatus1(context);
        boolean[] boll=cn.getBooleanConnectionStatus();
        	if (boll[0] && boll[1]){
		
		actionBar = getActionBar();
		pd = new ProgressDialog(this);
		setContentView(R.layout.smbtree);
		readALH();

		Intent intent = getIntent();
		copyMoveFile=intent.getStringExtra("COPYMOVE");
//	    String copyMoveFile1=intent.getStringExtra("COPYMOVE");
//	    String[]  cm =copyMoveFile1.split(",");
//	    copyMoveFile=cm[0]+File.separator+cm[1];

	    // 		File from = new File(CopyMoveArray.get(0)+File.separator+CopyMoveArray.get(1));

		
		userName = (EditText) findViewById(R.id.smbtreeuser1);	
		PassWord = (EditText) findViewById(R.id.smbtreepassword1);	
		btBack=(Button)findViewById(R.id.smbtreeback);
		dummy=(Button)findViewById(R.id.smbdummy);
		dummy.setVisibility(View.INVISIBLE);
		
		userName.setBackgroundColor(Color.YELLOW);
		PassWord.setBackgroundColor(Color.GREEN);	
		btBack.setBackgroundColor(Color.CYAN);	
		
		lv = (ListView) findViewById(R.id.smbtree1);
		
		btBack.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				finish();
				}	
		});
		
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, final View v, int position, long id) {
            	positionMain=position;
	if (OPtype==1){
		OPtype=2;
		updateActionbar("OP "+OPtype);

		
		gbsmb =new GetSMBTreeAsyncTask();
		gbsmb.execute();
	}	
	else if (OPtype==2){		
				String strA=al.get(position);
				String strB = strA.replaceAll("/{1}", "//");
				String strC=strB.replaceFirst("////","//");
				Toast.makeText(getApplicationContext(), "ALH size= "+alh.size(), Toast.LENGTH_SHORT).show(); 

		Intent browse = new Intent(getApplicationContext(), MainActivitySambaShare2.class);
		browse.putExtra("username", username);
		browse.putExtra("password", password);
		browse.putExtra("url", strC);
		browse.putExtra("COPYMOVE", copyMoveFile);		
		startActivity(browse);	
		
		
	}
				
                }			
            });

        lv.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                    int pos, long id) {
        		Toast.makeText(getApplicationContext(),
        				" long click  "+pos, Toast.LENGTH_SHORT).show(); 

				writeALH(pos);	
//			       hide_kbd();
					userName.setText("");
					PassWord.setText("");
			        gbsmb =new GetSMBTreeAsyncTask();
					gbsmb.execute();				
                return true;
            }
        }); 
        hide_kbd();
		OPtype=1;
		updateActionbar("OP "+OPtype);
		userName.setText("");
		PassWord.setText("");
        gbsmb =new GetSMBTreeAsyncTask();
		gbsmb.execute();
        	}
        	else {
        		Toast.makeText(getApplicationContext(),"No network connected!!", Toast.LENGTH_LONG).show(); 
        		finish(); 		
        	}
		
	}// end of on create

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.smbmenu, menu);
		return true;
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		switch (id) {

			case R.id.show8989:		
				showDialog1();
			break;
			
			case R.id.clear8989:		
				clearALH();
			break;
			
			case R.id.back8989:
				Toast.makeText(getApplicationContext(), "onback...  "+OPtype, Toast.LENGTH_SHORT).show(); 
				if (OPtype==1){
					finish();				}
				if (OPtype==2){
					OPtype=1;
					updateActionbar("OP "+OPtype);
					gbsmb =new GetSMBTreeAsyncTask();
					gbsmb.execute();
					}
				break;
				
			case R.id.save8989:
				backupALH();
				break;
				
			case R.id.restore8989:
				restoreALH();
				break;
				
				
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	
	public class GetSMBTreeAsyncTask extends AsyncTask<String, Integer, ArrayList<String>> {
		ProgressDialog p;
		private boolean mCancel=false;
		
		public GetSMBTreeAsyncTask() {		}
		
		public void myCancel(){
			mCancel=true;
		}
		
		@Override
	      protected void onPreExecute() {
	         super.onPreExecute();
				Toast.makeText(getApplicationContext(), "start..  "+OPtype+" "+username+" "+password, Toast.LENGTH_SHORT).show(); 
				 username=userName.getText().toString();
				 password=PassWord.getText().toString();
//		         p = new ProgressDialog(GetSMBTree.this);
//		         p.setMessage("Please wait...It is downloading");
//		         p.setIndeterminate(false);
//		         p.setCancelable(false);
//		         p.show();
				 showProgressDialogWithTitle("Start", "OpType="+OPtype);
	         readALH();
	      }
	      @Override
	      protected ArrayList<String> doInBackground(String... strings) {
ArrayList<String> iList = new ArrayList<String>();
	    	  if (OPtype==1){
//	    		  getIPAddress(true);
	    		  iList.clear();
	    	     try {
	    	         domains = (new SmbFile("smb://")).listFiles();
	    	         for (int i = 0; i < domains.length; i++) {
	    	        	 if (mCancel){
	    	        		 iList.clear();
	    	        		 return iList;
	    	        	 }
	    	             servers = domains[i].listFiles();
	    	             for (int j = 0; j < servers.length; j++) {	
		    	        	 if (mCancel){
		    	        		 iList.clear();
		    	        		 return iList;
		    	        	 }
	    	            	 iList.add(servers[j].toString() );
	    	     			publishProgress(  new Integer[]{i,j}    );	 

	    	             }
	    	         }
	    	     } catch (SmbException e) {
	    	         e.printStackTrace();
	    	     } catch (MalformedURLException e) {
	    	         e.printStackTrace();
	    	     } 
	      }
	    	  
	    	  
	    	  else if (OPtype==2){
	    		  iList.clear();
	    		   	String[] stra = getUserNameuserPassword(servers[positionMain].toString());
	    		   	username=stra[0]; password=stra[1];
	    		        NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",
	    		        		username, 	password);	      	
	    			    	 	     SmbFile sm = null;	
	    			    			 try {
	    			    				sm = new SmbFile(servers[positionMain].toString(),auth2);
	    			    			} catch (MalformedURLException e) {
	    			    				e.printStackTrace();
	    			    				}
	    			    			 catch (Exception exx) {
	    			    				 exx.printStackTrace();
	    			    			 }
	    			    			 try {
	    			    			 SmbFile[] ffa=sm.listFiles(); //  needs auth			
	    			    				for (int i=0;i<ffa.length;i++){
	    				    	        	 if (mCancel){
	    				    	        		 iList.clear();
	    				    	        		 return iList;
	    				    	        	 }
	    			    					iList.add(ffa[i].toString());
 //   			    	     			publishProgress(  new Integer[]{i,ffa.length}    );	 
	    			    				}   		
	    			    			} catch (SmbException e) {
	    			    				e.printStackTrace();
	    			    				}
	    			    			 catch (Exception exx) {
	    			    				 exx.printStackTrace();
	    			    			 }
	    	  }
	         return iList;
	         
	         
	         
	      }
	      
	      protected void onProgressUpdate(Integer... values) {       		
	  		showProgressDialogWithTitle("Loading files in folder.....", "Loading file "+values[0]+" of "+values[1]);
	          }
	      
	      @Override
	      protected void onPostExecute(ArrayList<String> b) {
	         super.onPostExecute(b);
				Toast.makeText(getApplicationContext(), "end..  ", Toast.LENGTH_SHORT).show(); 
hideProgressDialogWithTitle();
	         al.clear();
	         al.addAll(b);
	         if (OPtype==1){
	        	 password=""; username="";
	        	 PassWord.setText(password);userName.setText(username);
	        	 userName.setVisibility(View.VISIBLE);	
	        	 PassWord.setVisibility(View.VISIBLE);		        	 
	        	 lv.setAdapter(new MyListAdapter(b));
	        	 	        	 
	         }
	         if (OPtype==2){
	        	 userName.setVisibility(View.INVISIBLE);	
	        	 PassWord.setVisibility(View.INVISIBLE);	
	        	 lv.setAdapter(new MyListAdapter(b));
	         }         
	         
	         
	      
	      }
	      
	}   
	      
	      

	@Override
	public void onBackPressed() {	
		Toast.makeText(getApplicationContext(), "onback...  "+OPtype, Toast.LENGTH_SHORT).show(); 
//		gbsmb.myCancel();
		if (OPtype==1){
			try {				
				if (gbsmb!=null){
					if(	gbsmb.getStatus()==AsyncTask.Status.RUNNING ||  
							gbsmb.getStatus()==AsyncTask.Status.PENDING ){
						gbsmb.cancel(true);
					}				
				}
			} catch (Exception e1) {
				Toast.makeText(getApplicationContext(), "f1...  ", Toast.LENGTH_SHORT).show(); 
			}
			finish();	
			}
		
		if (OPtype==2){
			try {		
//				gbsmb.myCancel();
				if (gbsmb!=null){
					if(	gbsmb.getStatus()==AsyncTask.Status.RUNNING ||  
							gbsmb.getStatus()==AsyncTask.Status.PENDING ){
						gbsmb.cancel(true);
					}				
				}
			} catch (Exception e1) {
				Toast.makeText(getApplicationContext(), "f2...  ", Toast.LENGTH_SHORT).show(); 
				e1.printStackTrace();
			}
			
			try {
				OPtype=1;
				updateActionbar("OP "+OPtype);
				gbsmb =new GetSMBTreeAsyncTask();
				gbsmb.execute();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "f3...  ", Toast.LENGTH_SHORT).show(); 

				e.printStackTrace();
			}
			
			}
		
		//		super.onBackPressed();
	}
	
	
	private ArrayList<String> getAllSMBs(){
		ArrayList<String> intern =new ArrayList<String>();
   	String[] stra = getUserNameuserPassword(servers[positionMain].toString());
   	username=stra[0]; password=stra[1];
        NtlmPasswordAuthentication auth2 = new NtlmPasswordAuthentication("",
        		username, 	password);	      	
	    	 	     SmbFile sm = null;	
	    			 try {
	    				sm = new SmbFile(servers[positionMain].toString(),auth2);
	    			} catch (MalformedURLException e) {
	    				e.printStackTrace();
	    				}
	    			 catch (Exception exx) {
	    				 exx.printStackTrace();
	    			 }
	    			 try {
	    			 SmbFile[] ffa=sm.listFiles(); //  needs auth			
	    				for (int i=0;i<ffa.length;i++){
	    					intern.add(ffa[i].toString());
//	    	     			publishProgress(  new Integer[]{i,ffa.length}    );	 
	    				}   		
	    			} catch (SmbException e) {
	    				e.printStackTrace();
	    				}
	    			 catch (Exception exx) {
	    				 exx.printStackTrace();
	    			 }
					return intern;
	}
	     

//	     InetAddress addr = null;
//		try {
//			addr = InetAddress.getByName("192.168.1.1");
//		} catch (UnknownHostException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	     String host = addr.getHostName();

	 //////////////////////////
//	     ArrayList<String> ips=new ArrayList<String>(); 
/*
	     try {
	         

	         if (context != null) {

	           ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
	           NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
	           WifiManager wm = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);

	           WifiInfo connectionInfo = wm.getConnectionInfo();
	           int ipAddress = connectionInfo.getIpAddress();
	           String ipString = Formatter.formatIpAddress(ipAddress);


	           Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
	           Log.d(TAG, "ipString: " + String.valueOf(ipString));

	           String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
	           Log.d(TAG, "prefix: " + prefix);
	           for (int i = 100; i < 121; i++) {
	             String testIp = prefix + String.valueOf(i);

	             InetAddress address = InetAddress.getByName(testIp);
	             boolean reachable = address.isReachable(1000);
	             String hostName = address.getCanonicalHostName();
	             String hostName1 = address.getHostName();
	             
	             if (reachable){
	            	 ips.add("Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
	               Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + 
	            		   "  "+hostName1+") is reachable!");
	             }
	           }
	         }
	       } catch (Throwable t) {
	         Log.e(TAG, "Well that's not good.", t);
	       }
	     ///////////////////////////////////
	     int aaa=ips.size();
	     */

	

	public void writeALH(int pos) {
		String strA=al.get(pos);
		String strB = strA.replaceAll("/{1}", "//");
		String strC=strB.replaceFirst("////","//");
				
		  if (null == alh) {
		    alh = new ArrayList<SMBHolder1>();
		  }
		  boolean flag=false;
		  int i=0;

			  for (i=0;i<alh.size();i++){
				  if (  alh.get(i).getSmb_location_().contains(al.get(pos))     ){
					  flag=true;break;
				  }
			  }
			  if (!flag){  // not found - need to add
					 username=userName.getText().toString();
					 password=PassWord.getText().toString();
				  alh.add(new SMBHolder1(username,password,strC));
			  }
			  else {// replace
				  alh.remove(i);
					 username=userName.getText().toString();
					 password=PassWord.getText().toString();
				  alh.add(new SMBHolder1(username,password,strC));
				  
			  }
		  SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
		  Editor editor = prefs.edit();
		  try {
			  editor.putString("UserList", SMBObjectSerializer.serialize(alh));
			  editor.commit();
			  } catch (IOException e) {
			  e.printStackTrace();
				Toast.makeText(getApplicationContext(), "bad write...  ", Toast.LENGTH_SHORT).show(); 
			  }	
	
		  readALH();
			Toast.makeText(getApplicationContext(), "good write...  ", Toast.LENGTH_SHORT).show(); 
			  
		}
	
	
	
	public void backupALH() {
		  // save the task list to preference
		  SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
		  Editor editor = prefs.edit();
		  try {
			  editor.putString("BackupList", SMBObjectSerializer.serialize(alh));
			  } catch (IOException e) {
			  e.printStackTrace();
				Toast.makeText(getApplicationContext(), "bad backup...  ", Toast.LENGTH_SHORT).show(); 
			  }
		  StringBuilder sb=new StringBuilder();
		  for (int l5=0;l5<alh.size();l5++){
			  sb.append(alh.get(l5).getSmb_location_()+","+alh.get(l5).getUserName_()+","+
		  alh.get(l5).getPassword_()+"\r\n");

		  
		  }
		  SMBFileWriteString.setFileString("SMBSMBSMB", "userbackup.txt", sb.toString());

			Toast.makeText(getApplicationContext(), "good backup...  ", Toast.LENGTH_SHORT).show(); 
			  editor.commit();
		}
	
	
	
	
	
	
	private void clearALH(){
		alh.clear();
		  SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
		  Editor editor = prefs.edit();
		  try {
			  editor.putString("UserList", SMBObjectSerializer.serialize(alh));
			  editor.commit();
			  } catch (IOException e) {
			  e.printStackTrace();
				Toast.makeText(getApplicationContext(), "bad write...  ", Toast.LENGTH_SHORT).show(); 
			  }
			Toast.makeText(getApplicationContext(), "good write clr...  ", Toast.LENGTH_SHORT).show(); 
			  
	}

	private String[]  getUserNameuserPassword(String st){
		for (int i=0;i<alh.size();i++){
			if (alh.get(i).getSmb_location_().contains(st)){
//				username=alh.get(i).getUserName_();
//				password=alh.get(i).getPassword_();
				return new String[]{alh.get(i).getUserName_(),alh.get(i).getPassword_()};
			}
		}
		return new String[]{"",""};
	}
	
public void readALH (){
	alh.clear();
	// Load user List from preferences
	SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE ,Context.MODE_PRIVATE);
	try {
	  alh = (ArrayList<SMBHolder1>) SMBObjectSerializer.deserialize(prefs.getString("UserList", SMBObjectSerializer.serialize(new ArrayList<SMBHolder1>())));
	  int y=alh.size();
	} catch (IOException e) {
		alh.clear();
	      e.printStackTrace();
			Toast.makeText(getApplicationContext(), "bad reed...  "+e.getMessage(),
					Toast.LENGTH_SHORT).show(); 

	  }
}	

public void restoreALH (){
	alh.clear();
	// Load user List from preferences
	SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_FILE ,Context.MODE_PRIVATE);
	Editor editor = prefs.edit();
	try {
	  alh = (ArrayList<SMBHolder1>) SMBObjectSerializer.deserialize(prefs.getString("BackupList", SMBObjectSerializer.serialize(new ArrayList<SMBHolder1>())));
	} catch (IOException e) {
	      e.printStackTrace();
			alh.clear();
		      e.printStackTrace();
				Toast.makeText(getApplicationContext(), "bad restore...  "+e.getMessage(),
						Toast.LENGTH_SHORT).show(); 
	  }
	try {
		  editor.putString("UserList", SMBObjectSerializer.serialize(alh));
		  editor.commit();
		  } catch (IOException e) {
		  e.printStackTrace();
			Toast.makeText(getApplicationContext(), "bad write...  ", Toast.LENGTH_SHORT).show(); 
		  }
	userName.setText("");
	PassWord.setText("");
    gbsmb =new GetSMBTreeAsyncTask();
	gbsmb.execute();		
		Toast.makeText(getApplicationContext(), "good write...  ", Toast.LENGTH_SHORT).show(); 
	
}	

////////


//////////////












public void showDialog1(){
AlertDialog.Builder builderSingle = new AlertDialog.Builder(GetSMBTree.this);
builderSingle.setIcon(R.drawable.sambaicon72x72);
builderSingle.setTitle("SMB path list");
readALH();
final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(GetSMBTree.this, android.R.layout.select_dialog_singlechoice);
String a,b,c;
for (int i=0;i<alh.size();i++){
	a=alh.get(i).getSmb_location_();
	b=alh.get(i).getUserName_();
	c=alh.get(i).getPassword_();
	arrayAdapter.add("Location: "+
			a+"\nUser name: "+b+"\nPassword: "+c);
}

builderSingle.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);

                Toast.makeText(getApplicationContext(), alh.get(which).getUserName_()+"  "+
                		alh.get(which).getPassword_(),	Toast.LENGTH_SHORT).show(); 
userName.setText(alh.get(which).getUserName_());
PassWord.setText(alh.get(which).getPassword_());

username=(alh.get(which).getUserName_());
password = (alh.get(which).getPassword_());


                AlertDialog.Builder builderInner = new AlertDialog.Builder(GetSMBTree.this);
                builderInner.setMessage(strName);
                builderInner.setTitle("Your Selected Item is");
                builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,int which) {
                                dialog.dismiss();
                            }
                        });
                builderInner.show();
            }
        });
builderSingle.show();

}



public static String getIPAddress(boolean useIPv4) {
	int y=0;
    try {
        List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
        for (NetworkInterface intf : interfaces) {
            List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
            for (InetAddress addr : addrs) {
                if (!addr.isLoopbackAddress()) {
                    String sAddr = addr.getHostAddress().toUpperCase();
                    boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr); 
                    if (useIPv4) {
                        if (isIPv4) 
                            return sAddr;
                    } else {
                        if (!isIPv4) {
                            int delim = sAddr.indexOf('%'); // drop ip6 port suffix
                            return delim<0 ? sAddr : sAddr.substring(0, delim);
                        }
                    }
                }
            }
        }
    } catch (Exception ex) { } // for now eat exceptions
    return "";
}

private void hide_kbd() {
this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);
imm.hideSoftInputFromWindow(PassWord.getWindowToken(), 0);

}

private void updateActionbar(String string2){
	actionBar.setSubtitle(string2);
	actionBar.setTitle("SMBShare");
}

private class MyListAdapter extends ArrayAdapter<String> {
	ArrayList<String> alista = new ArrayList<String>();
	public MyListAdapter(ArrayList<String> bb) {
		super(GetSMBTree.this, R.layout.smbdialog1a, bb);
		this.alista=bb;		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Make sure we have a view to work with (may have been given null)
		View itemView = convertView;
		if (itemView == null) {
			itemView = getLayoutInflater().inflate(R.layout.smbdialog1a, parent, false);
		}	
		TextView line1 = (TextView) itemView.findViewById(R.id.dialog_txt111);
		TextView line2 = (TextView) itemView.findViewById(R.id.dialog_txt112);
		TextView line3 = (TextView) itemView.findViewById(R.id.dialog_txt113);
		
		line1.setText("Path: "+alista.get(position));

		if (OPtype==1){
		   	String[] stra = getUserNameuserPassword(alista.get(position));
			line2.setText("user: "+stra[0]);
			line3.setText("pass: "+stra[1]);
		}
		else {
			line2.setVisibility(View.INVISIBLE);
			line3.setVisibility(View.INVISIBLE);
		}
		return itemView;
	}				
}


//Method to show Progress bar
private void showProgressDialogWithTitle(String title,String substring) {
 pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
 //Without this user can hide loader by tapping outside screen
 pd.setCancelable(false);
 //Setting Title
 pd.setTitle(title);
 pd.setMessage(substring);
 pd.show();

}

//Method to hide/ dismiss Progress bar
private void hideProgressDialogWithTitle() {
 pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
 pd.dismiss();
}





}
