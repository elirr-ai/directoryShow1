package com.example.directoryshow1;

public class SMBCurrentDirectoryLastHolderMP3list {

	private String currentDir,Last;
	
	public SMBCurrentDirectoryLastHolderMP3list(String current, String last) {
this.currentDir=current;
this.Last=last;
	}

	public String getCurrentDir() {
		return currentDir;
	}

	public void setCurrentDir(String currentDir) {
		this.currentDir = currentDir;
	}

	public String getLast() {
		return Last;
	}

	public void setLast(String last) {
		Last = last;
	}
	
	
}
