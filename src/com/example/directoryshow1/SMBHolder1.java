package com.example.directoryshow1;

import java.io.Serializable;

public class SMBHolder1 implements Serializable{

	private static final long serialVersionUID = 1L;
	String userName_,password_,smb_location_;
	
	public SMBHolder1 (String u, String p, String s){
		this.userName_=u;
		this.password_=p;
		this.smb_location_=s;
	}

	public String getUserName_() {
		return userName_;
	}

	public void setUserName_(String userName_) {
		this.userName_ = userName_;
	}

	public String getPassword_() {
		return password_;
	}

	public void setPassword_(String password_) {
		this.password_ = password_;
	}

	public String getSmb_location_() {
		return smb_location_;
	}

	public void setSmb_location_(String smb_location_) {
		this.smb_location_ = smb_location_;
	}
	
}
