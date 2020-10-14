package com.example.directoryshow1;

import java.io.File;

public class DirectoryHolder1 {

	File file;
	boolean isDir;
	boolean is_Hidden;
	boolean can_Execute;
	boolean can_Read;
	boolean can_Write;
	long length;
	long lastModified;
	
	public DirectoryHolder1 (File f, boolean b){
	this.file=f;
	this.isDir=b;	
	}

	public DirectoryHolder1(File absoluteFile, boolean directory, boolean hidden, boolean canExecute, boolean canRead,
			boolean canWrite, long length, long lastModified) {
		this.file=absoluteFile;
		this.isDir=directory;
		this.is_Hidden=hidden;
		 this.can_Execute=canExecute;
		 this.can_Read=canRead;
		 this.can_Write=canWrite;
		 this.length=length;
		 this.lastModified=lastModified;
		
		
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}
	
	
	
	
}
