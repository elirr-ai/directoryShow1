package com.example.directoryshow1;

import android.graphics.Bitmap;

public class MediaDataRetriever1 {

	private Bitmap b;
	private String songName;
	private String artistName;
	
	public MediaDataRetriever1 (Bitmap b, String s, String artistName){
		this.b=b; this.songName=s; this.artistName=artistName;
	}
	public Bitmap getB() {
		return b;
	}
	public void setB(Bitmap b) {
		this.b = b;
	}
	public String getSongName() {
		return songName;
	}
	public void setsongName(String name) {
		this.songName = name;
	}
	
	public String getArtistName() {
		return artistName;
	}
	
	
}
