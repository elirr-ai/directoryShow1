package com.example.directoryshow1;

public class EpubChaptersHolderEpub {

	String href,title;
	static String bookTitle,bookAuthor="",
	bookContributor="" ,
	bookDate="",
	bookLanguage="",
	bookFormat ="" ;
	
	
	public EpubChaptersHolderEpub(String h,String t ){
		this.href=h;
		this.title=t;	
	}
	
	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
	
	
	
}
