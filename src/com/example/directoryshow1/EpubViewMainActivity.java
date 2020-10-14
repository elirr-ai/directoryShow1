package com.example.directoryshow1;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.widget.Toast;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class EpubViewMainActivity extends Activity {

	
	private WebView wv1;
    private boolean isSplashOn = false;
    ProgressDialog pd;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
        ActionBar actionBar = getActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getIntent().getStringExtra("TITLE"));
        
			pd = new ProgressDialog(EpubViewMainActivity.this);
	        pd.setMessage("Please wait Loading...");
	        pd.show();
	        
		setContentView(R.layout.epubwebviewactivity_main);
	      wv1=(WebView)findViewById(R.id.webView);
	      wv1.getSettings().setJavaScriptEnabled(true);
//	        WebSettings Ws = wv1.getSettings();
//	        Ws.setJavaScriptEnabled(true);      	
	      wv1.getSettings().setSupportZoom(true);  
	      wv1.getSettings().setBuiltInZoomControls(true);	      

	      
	      wv1.getSettings().setLoadsImagesAutomatically(true);
	      wv1.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	      wv1.getSettings().setAppCacheEnabled(false);
	      wv1.getSettings().setLoadWithOverviewMode(true);
//	      wv1.getSettings().setPluginsEnabled(true);
//	      wv1.getSettings().setPluginState(PluginState.ON);
	      wv1.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	      wv1.setScrollbarFadingEnabled(false);
	      
	      
	      
	      
	      
	        wv1.setWebViewClient(new WebViewClient() {
	            @Override
	            public void onPageFinished(WebView view, String url) {
	                if(pd!=null && pd.isShowing())
	                {
	                    pd.dismiss();
	                }
	            }
	        });	        

//Toast.makeText(getApplicationContext(), "2nd!  "+gpp0, Toast.LENGTH_LONG).show();

wv1.loadDataWithBaseURL(null, getIntent().getStringExtra("TEXT"), "text/xml", "utf-8", null);	        
//	        wv1.setWebViewClient(new WebViewClient());
//	        wv1.loadUrl("http://www.codeplayon.com");
/////	        wv1.loadUrl("http://www.yahoo.com");
//	        wv1.loadUrl("http://www.google.com");
//	        wv1.loadUrl("https://www.azlyrics.com/lyrics/janisian/betweenthelines.html "); 
       
	        /////////
//	        AdRequest adRequest = new AdRequest.Builder().build();
	      
	      
//	      wv1.loadUrl("http://tutlane.com");
//	       this.webView = (WebView) findViewById(R.id.webview);
/*
	      wv1.setBackgroundColor(0);
	      wv1.setBackgroundResource(R.drawable.ic_launcher);
	      wv1.getSettings().setJavaScriptEnabled(true);
	      wv1.getSettings().setLoadsImagesAutomatically(true);
	      wv1.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
	      wv1.getSettings().setAppCacheEnabled(false);
	      wv1.getSettings().setLoadWithOverviewMode(true);
	 //     webView.getSettings().setPluginsEnabled(true);
	      wv1.getSettings().setPluginState(PluginState.ON);
	      wv1.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	      wv1.setScrollbarFadingEnabled(false);

	      wv1.setWebViewClient(new MyWebClient());
	   	wv1.loadUrl(urlsite);
*/
	        
	        
	      //	      wv1.setWebViewClient(new MyBrowser());


	}
	
	public class MyWebClient extends WebViewClient {
		  @Override
		  public boolean shouldOverrideUrlLoading(WebView view, String url) {
//		      view.loadUrl(urlsite);
		      return false;
		  }

		  @Override
		  public void onPageFinished(WebView view, String url) {
		      if(!isSplashOn) {               
		          wv1.setBackground(null);
		          wv1.setBackgroundColor(0);
		          isSplashOn = true;
		          pd.dismiss();
		      }

		      super.onPageFinished(view, url);
		  }
		}

	   @Override
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if(event.getAction() == KeyEvent.ACTION_DOWN){
	            switch(keyCode)
	            {
	                case KeyEvent.KEYCODE_BACK:

	                    if ((keyCode == KeyEvent.KEYCODE_BACK) && wv1.canGoBack()) {
	                        wv1.goBack();
	                        return true;
	                    }

	            }

	        }
	        return super.onKeyDown(keyCode, event);
	    }
			
			
		
		
		
		
		
		
		
	
	
	
	

	
	  private class MyBrowser extends WebViewClient {
	      @Override
	      public boolean shouldOverrideUrlLoading(WebView view, String url) {
	         view.loadUrl(url);
	         return true;
	      }
	   }
	  
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.epubwebmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
//			String gpp=FileReadString.getFileString("DILE", "AAA");
//			wv1.loadDataWithBaseURL(null, gpp, "text/xml", "utf-8", null);
//			int y=gpp.length();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
