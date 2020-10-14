package com.example.directoryshow1;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.domain.TableOfContents;

public class EpubHandel1_MainActivity extends Activity implements OnTouchListener{

	TextView tv1;
	ImageView iv1;
	Drawable drawable;
	ListView lv1;
	
	List<String> hREFr1 = new ArrayList<String>();
	
//	static final String dir="fr1";
	int width=0,height=0;
	
	SharedPreferences sharedpreferences;
	private static final String MyPREFERENCES="MYPREFERENCESEPUB";
	private static final String KEYINDEX="KEYINDEX";
	private static final String spaces="   ";
	private static final String asterisk="*  ";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.epubactivity_epub_handel1__main);
		tv1 = (TextView) findViewById(R.id.tv1);
		iv1 = (ImageView) findViewById(R.id.imageView1);
		lv1 = (ListView) findViewById(R.id.chapter_list);
		tv1.setOnTouchListener(this);
	    
		sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
		
		
		
//		EpubGetEpubContent1 gepub=new EpubGetEpubContent1(dir,"tales.epub");
//		EpubGetEpubContent1 gepubgood=new EpubGetEpubContent1(dir,"war.epub");
//		EpubGetEpubContent1 gepubgood=new EpubGetEpubContent1(dir,"wake.epub");
		EpubGetEpubContent1 gepubgood=new EpubGetEpubContent1(getIntent().getStringExtra("dir"),
				getIntent().getStringExtra("file"));
		
		if (gepubgood.getBook1()==null){	
			Toast.makeText(getApplicationContext(), "Error in EPUB file...", Toast.LENGTH_LONG).show();
			finish();	
		}
		
		else {
		
		ArrayList<String> a00 =gepubgood.getTitlesA();		
		StringBuilder sb=new StringBuilder();	
		if (a00.isEmpty()) sb.append("\n");
		else {
		for (int i=0;i<a00.size();i++){
			sb.append("Title "+"  "+a00.get(i)+"\n");
				}	
		}
		EpubChaptersHolderEpub.bookTitle=sb.toString();
		
		a00 =gepubgood.getAuthors();
		sb=new StringBuilder();	
		if (a00.isEmpty()) sb.append("\n");
		else {
		for (int i=0;i<a00.size();i++){
			sb.append("Author "+"  "+a00.get(i)+("\n"));
				}	
		}
		EpubChaptersHolderEpub.bookAuthor=sb.toString();		

		
		Bitmap b=null;
		try {
			b = gepubgood.getCoverImage();
			if (b!=null) iv1.setImageBitmap(b);
		} catch (Exception e1) {
			iv1.setBackgroundResource(R.drawable.epub80x80);
			e1.printStackTrace();
		}
		
		a00=gepubgood.getContributors();
		sb=new StringBuilder();	
		if (a00.isEmpty()) sb.append("\n");
		else {
		for (int i=0;i<a00.size();i++){
			sb.append("Contributor "+i+"  "+a00.get(i)+"\n");
				}	
		}
		EpubChaptersHolderEpub.bookContributor=sb.toString();			
		
		a00=gepubgood.getDate();
		sb=new StringBuilder();	
		for (int i=0;i<a00.size();i++){
			sb.append("date published "+a00.get(i)+"\n");
		}		
		EpubChaptersHolderEpub.bookDate=sb.toString();		
		
		EpubChaptersHolderEpub.bookLanguage="Language  "+gepubgood.getlanguage()+"\n";		

		EpubChaptersHolderEpub.bookFormat="Format  "+gepubgood.getFormat()+"\n";		
		
//Book book=gepub.book;
		Book bookgood=gepubgood.book;

//// toc
////TableOfContents tableOffContents=book.getTableOfContents();
TableOfContents tableOffContentsgood=bookgood.getTableOfContents();
////final List<TOCReference> tableOffContentsList=tableOffContents.getTocReferences();
final List<TOCReference> tableOffContentsListgood=tableOffContentsgood.getTocReferences();

////Spine spine=book.getSpine();
//spineReferences=spine.getSpineReferences();
Spine spinegood=bookgood.getSpine();
final List<SpineReference> spineReferencesgood=spinegood.getSpineReferences();
ArrayList<EpubChaptersHolderEpub> chl =new ArrayList<EpubChaptersHolderEpub>();

try {
	for (int j=0;j<tableOffContentsListgood.size();j++){
		TOCReference tocReference=tableOffContentsListgood.get(j);
		Resource resourceToc=tocReference.getResource();
		chl.add(new EpubChaptersHolderEpub(resourceToc.getHref(), tocReference.getTitle()));
			}
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}

//////////spine

//spineAL.clear();
ArrayList<EpubChaptersHolderEpub> spineAL =new ArrayList<EpubChaptersHolderEpub>();
for (int j=0;j<spineReferencesgood.size();j++){

SpineReference spineReference = spineReferencesgood.get(j);
// String h1=spineReference.getResourceId();
Resource res=spineReference.getResource();
// String title=r.getTitle();
String id=res.getId();
String href=res.getHref();
 //String inputEncoding=res.getInputEncoding();
// MediaType mediaType=res.getMediaType();
 //String mediaName =mediaType.getName();
 spineAL.add(new EpubChaptersHolderEpub(id, href));
	} 
 
//Collections.sort(chaptersXml);
hREFr1.clear();
//for (int j=0;j<chl.size();j++){
//	hREFr1.add(chl.get(j).getTitle());
//}
String st1,st2,st3 = "";

for (int j=0;j<spineAL.size();j++){
//	hREFr1.add(spineAL.get(j).getHref());
	st1=spineAL.get(j).getTitle();
	st3=st1;
		for (int k=0;k<chl.size();k++) {
			st2=chl.get(k).getHref();
//			st3=st2;
				if (st1.equals(st2)){
					st3=chl.get(k).getTitle();
					break;
				}
				
		}
		hREFr1.add(st3);

}

int chapterCounter=1;
for (int k=0;k<hREFr1.size();k++){
	String st9=hREFr1.get(k);
	if (st9.startsWith("main")){
		hREFr1.set(k, "Chapter "+chapterCounter++);
	}
	else {
		chapterCounter=1;
	}
}

	setArrayAdaptor();

String showTv=EpubChaptersHolderEpub.bookTitle+
	EpubChaptersHolderEpub.bookAuthor+
	EpubChaptersHolderEpub.bookContributor+
	EpubChaptersHolderEpub.bookDate+
	EpubChaptersHolderEpub.bookLanguage+
	EpubChaptersHolderEpub.bookFormat;

		tv1.setText(showTv);
		drawable = new BitmapDrawable(getResources(), b);
//		tv1.setBackground(d);
		
		lv1.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
               	saveIndex(String.valueOf(position));
            	SpineReference spineReference = spineReferencesgood.get(position);
            	Resource res=spineReference.getResource(); 
            	StringBuilder sbba=new StringBuilder();
            	try {
            		InputStream istream=  res.getInputStream();
            		int i;
            		while((i = istream.read())!=-1) {
            	        sbba.append((char)i);
            	     }		
            	} catch (IOException e) {
            		e.printStackTrace();
            	} 	
  
            	setArrayAdaptor();
            	
//Toast.makeText(getApplicationContext(), "String was found!  "+position+
//		"   "+hREFr.get(position), Toast.LENGTH_LONG).show();
Intent intent = new Intent(getBaseContext(), EpubViewMainActivity.class);
intent.putExtra("TITLE", EpubChaptersHolderEpub.bookTitle);
intent.putExtra("TEXT", sbba.toString());
startActivity(intent);
            }
        });
		
		}	
	}
///////////////////////////
	@Override
public void onWindowFocusChanged(boolean hasFocus) {
	
		
		if (width==0 || height==0) {
			displ display =new displ();
			width=(int)display.getScreenWidth(); 
			height=(int)display.getScreenheight();
			
	ViewGroup.MarginLayoutParams p =(ViewGroup.MarginLayoutParams) tv1.getLayoutParams();
		p.setMargins(width/180,0,width/180,0);
		p.height=height/3;
		p.width=(45*width)/100;
		tv1.setTextSize(TypedValue.COMPLEX_UNIT_PX,
				getResources().getDimensionPixelSize(R.dimen.myFontSize2));
		tv1.requestLayout();	
		tv1.setBackgroundColor(Color.YELLOW);
		
		
		p =(ViewGroup.MarginLayoutParams) iv1.getLayoutParams();
		p.setMargins(width/180,0,width/180,0);
		p.height=height/3;
		p.width=(45*width)/100;
		iv1.requestLayout();		
		iv1.setScaleType(ScaleType.FIT_XY);
		
		p =(ViewGroup.MarginLayoutParams) lv1.getLayoutParams();
		p.setMargins(width/180,0,width/180,0);
		p.height=(56*height)/100;
		p.width=width;
		iv1.requestLayout();		  
}	
		
 
	
		
	super.onWindowFocusChanged(hasFocus);
}
///////////////////////////////////////////////

	
	//////////////////////////////////
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.epub_handel1__main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			String temp1=sharedpreferences.getString(KEYINDEX, "");
Toast.makeText(getApplicationContext(), "String was found!  "+temp1, Toast.LENGTH_LONG).show();
			
//			List<String> hREFr1 = new ArrayList<String>();
//			for (int j=0;j<spineAL.size();j++){
//				String s100=spineAL.get(j).getHref();
//				hREFr1.add(s100);
//			}
//			ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.epubactivity_listview, hREFr1);	
//			lv1.setAdapter(adapter);
//			adapter.notifyDataSetChanged();
			return true;
		}
		
		if (id == R.id.clearbookmark) {
			saveIndex("");
			setArrayAdaptor();
			return true;
		}
		
		
		
		
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		  switch (event.getAction() & MotionEvent.ACTION_MASK) {
	        case MotionEvent.ACTION_DOWN:

	    		tv1.setBackground(drawable);
	            break;
	        case MotionEvent.ACTION_UP:
	        	tv1.setBackgroundColor(Color.WHITE);
//	    		tv1.setText(sbb.toString());
	            break;

	        case MotionEvent.ACTION_MOVE:
////	            actionMove();
	            break;
	    }
		
		return true;
	}
	
	private void saveIndex (String s){
		SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(KEYINDEX, s).commit();
	}
	
	private void setArrayAdaptor() {
		int kk=-1;
		List<String> hREFr2 = new ArrayList<String>();hREFr2.clear();
		String temp1=sharedpreferences.getString(KEYINDEX, "");
		if (temp1!=null && temp1.length()>0 && !temp1.equals(""))  {
			kk=Integer.valueOf(sharedpreferences.getString(KEYINDEX, "")) ;

			for (int k=0;k<hREFr1.size();k++){
				if (k==kk){
					hREFr2.add(asterisk+hREFr1.get(k));
				}
				else {
					hREFr2.add(spaces+hREFr1.get(k));
				}
			}
		}
		else {
			for (int k=0;k<hREFr1.size();k++){
			hREFr2.add(hREFr1.get(k));
				}
			}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.epubactivity_listview, hREFr2);	
		lv1.setAdapter(adapter);
		adapter.notifyDataSetChanged();	
		if (kk>-1){
		lv1.smoothScrollToPositionFromTop(kk,height/10,1400);
		}
	}
	
	
}
//  http://www.siegmann.nl/epublib/example-programs/epub-sample-simple1