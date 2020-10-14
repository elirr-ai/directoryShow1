package com.example.directoryshow1;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Date;
import nl.siegmann.epublib.domain.Guide;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.TOCReference;
import nl.siegmann.epublib.domain.TableOfContents;
import nl.siegmann.epublib.epub.EpubReader;

public class EpubGetEpubContent1 {

	Book book=null;
	static int error=0;
	
	public EpubGetEpubContent1 (String d, String f){
//		path = Environment.getExternalStorageDirectory()+"/"+d+"/"+f; 
		EpubReader epubReader = new EpubReader();
		try {
			book = epubReader.readEpub(new FileInputStream(d+"/"+f));
		} catch (FileNotFoundException e1) {
			error++;
			e1.printStackTrace();
		} catch (IOException e1) {
			error++;
			e1.printStackTrace();
		}	
		catch (Exception e1) {
			error++;
			e1.printStackTrace();
		}	
		
	}

	public Book getBook1(){
		return book;
	}
	
	
	public ArrayList<String> getAuthors (){
		ArrayList<String> al1=new ArrayList<String>();		
		List<Author> al=book.getMetadata().getAuthors();
		if (al==null || al.size()==0 || al.isEmpty()) return null;
		else {
		for (int i=0;i<al.size();i++){
			al1.add(al.get(i).getFirstname()+"  "+al.get(i).getLastname());
		}	
		return al1;
		}		
	}
	
	public ArrayList<String> getTitlesA(){
	List<String> al=book.getMetadata().getTitles();
	if (al.isEmpty()) return null;
		else {			
			return convertList2ArrayList(al);
		}
	}
	

	
	public ArrayList<String> getTitles(){
	List<String> al=book.getMetadata().getTitles();
ArrayList<String> alsit =new ArrayList<String>();
	if (al.isEmpty()) return null;
		else {
			
			String[] array = new String[al.size()];
			al.toArray(array); // fill the array

			for (int i=0;i<array.length;i++){
				alsit.add(array[i]);
			}			
return alsit;			
		}
	}
	
	public ArrayList<String> getTOC(){
		ArrayList<String> al1=new ArrayList<String>();	
		
	TableOfContents al=book.getTableOfContents();
	if (al.size()==0) return null; 
	List<TOCReference> toc=   al.getTocReferences();
	if (toc.isEmpty()) return null;
	else {
		for (int i=0;i<toc.size();i++){
			al1.add(toc.get(i).getTitle());
			}
		}
	return al1;
		}
		
	
	public Bitmap getCoverImage(){
		Bitmap coverImage = null;
		try {
			coverImage = BitmapFactory.decodeStream(book.getCoverImage().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return coverImage;
	}
	
	public ArrayList<String> getContributors() {
		List<Author> ll=book.getMetadata().getContributors();
		ArrayList<String> a100 = new ArrayList<String>();
		
		for (int i=0;i<ll.size();i++){
			a100.add(ll.get(i).getFirstname()+"  "+ll.get(i).getLastname());
			}		
		return a100;
	}
	
	public ArrayList<String> getDate() {
		ArrayList<String> a100 = new ArrayList<String>();
		List<Date> llz=book.getMetadata().getDates();
		for (int i=0;i<llz.size();i++){
			a100.add(llz.get(i).getValue());
			}
		return a100;
	}
	
	public String getlanguage() {
		return book.getMetadata().getLanguage();
	}
	
	public String getTitle() {
		return book.getTitle();
	}
	
	public String getFormat(){
		return book.getMetadata().getFormat();
	}   
	
	private ArrayList<String> convertList2ArrayList(List<String> allll){
		ArrayList<String> alsitt =new ArrayList<String>();
		String[] array = new String[allll.size()];
		allll.toArray(array); // fill the array
		for (int i=0;i<array.length;i++){
			alsitt.add(array[i]);
		}						
		return alsitt;
	}
		
	
}
//  http://www.siegmann.nl/epublib/example-programs/epub-sample-simple1


