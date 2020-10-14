package com.example.directoryshow1;

public class SMBMP4MetaData {

	String key,value;
	
	public SMBMP4MetaData (String k, String v){
		this.key=k;
		this.value=v;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	
	
}
