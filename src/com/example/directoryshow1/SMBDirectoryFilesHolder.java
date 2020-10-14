
package com.example.directoryshow1;

import android.graphics.Bitmap;
import android.webkit.MimeTypeMap;


public class SMBDirectoryFilesHolder {
	public int dir_image;
	private int total_files;
	private int image_files;
	private String dir_name;
	private String date_created;
	private int position;
	public String fullFileName;
	private int Index;
	private String attrib;
	private long curMillis;
	private Bitmap bm;
	private String totalFileSizeString;
	private String dateCreatedFile;
	private String fullPathFileNameSMB;
	
	public SMBDirectoryFilesHolder(int dir_image,int total_files,int image_files,
			String dir_name,String date_created , String fullFileName,
			int Index , String attrib , long curMillis,Bitmap bm, String totalFileSizeString, String dateCreated,String fpath ) {
		super();
		this.dir_image = dir_image;
		this.total_files = total_files;
		this.image_files = image_files;
		this.dir_name = dir_name;
		this.date_created = date_created;
		this.fullFileName=fullFileName;
		this.Index=Index;
		this.attrib=attrib;
		this.curMillis=curMillis;
		this.bm=bm;
		this.totalFileSizeString=totalFileSizeString;
		this.dateCreatedFile=dateCreated;
		this.fullPathFileNameSMB=fpath;
			}


	public String getTotalFileSizeString() {
		return totalFileSizeString;
	}
	
	public String getext() {
		MimeTypeMap mime = MimeTypeMap.getSingleton();
		return mime.getMimeTypeFromExtension(fullFileName.substring(fullFileName.indexOf(".")+1)
				.toLowerCase());
	}
	
	public String getAttrib() {
		return attrib;
	}


	public void setAttrib(String attrib) {
		this.attrib = attrib;
	}


	public int getIndex() {
		return Index;
	}


	public void setIndex(int index) {
		Index = index;
	}


	public int getDir_image() {
		return dir_image;
	}

	public void setDir_image(int dir_image) {
		this.dir_image = dir_image;
	}

	public String getDir_name() {
		return dir_name;
	}

	public void setDir_name(String dir_name) {
		this.dir_name = dir_name;
	}

	
	public String getDate_created() {
		return date_created;
	}


	public void setDate_created(String date_created) {
		this.date_created = date_created;
	}

	public int getTotal_files() {
		return total_files;
	}

	public void setTotal_files(int total_files) {
		this.total_files = total_files;
	}

	public int getImage_files() {
		return image_files;
	}

	public void setImage_files(int image_files) {
		this.image_files = image_files;
	}
	
	public int getPositionAsterisk() {
		return position;
	}

	public void setPositionAsterisk(int position) {
		this.position = position;
	}
	
	
	
	//////////// fullFileName
	
	public String getFullFileName() {
		return fullFileName;
	}

	public void setFullFileName(String fullFileName) {
		this.fullFileName = fullFileName;
	}
	
		public long getParsedSeparatedDate_in_millis() {
		return curMillis;
			}
	
	
	public String getParsedSize() {
		if (attrib.contains("d")) {
			return String.valueOf(total_files)+" files" ;
		}
		if (total_files<1024) {
			return String.valueOf(total_files)+" b" ;
		}
		else if (total_files<1048576) {
			return String.valueOf(total_files/1024)+" kb"  ;
		}
		else {
			return String.valueOf(total_files/1048576)+" mb"  ;
		}
	}
	
	public void setBMP(Bitmap b) {
		this.bm=b;
	}
	public Bitmap getBMP() {
	return bm;
		}


	public String getFullPathFileNameSMB() {
		return fullPathFileNameSMB;
	}


	public void setFullPathFileNameSMB(String fullPathFileNameSMB) {
		this.fullPathFileNameSMB = fullPathFileNameSMB;
	}



	
	
	
	
}
