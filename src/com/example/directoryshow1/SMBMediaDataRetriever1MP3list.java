package com.example.directoryshow1;

import android.graphics.Bitmap;

public class SMBMediaDataRetriever1MP3list {

	private Bitmap b;
	private String songName;
	private String artistName;
	private String duration;
	
	public SMBMediaDataRetriever1MP3list (Bitmap b, String s, String artistName){
		this.b=b; this.songName=s; this.artistName=artistName;
	}
	
	public SMBMediaDataRetriever1MP3list (Bitmap b, String s, String artistName, String dur){
		this.b=b; this.songName=s; this.artistName=artistName;duration=dur;
	}
	
	public String getDuration(){
		return duration;
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
