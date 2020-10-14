package com.example.directoryshow1;

public class OpsDataBase {
String opName;
boolean needSlide;
float opScale;
boolean compueConvolution;

public OpsDataBase (String n,boolean ne,float sc,boolean c){
	this.opName=n;
	this.needSlide=ne;
	this.opScale=sc;
	this.compueConvolution=c;
}

public String getOpName() {
	return opName;
}

public void setOpName(String opName) {
	this.opName = opName;
}

public boolean isNeedSlide() {
	return needSlide;
}

public void setNeedSlide(boolean needSlide) {
	this.needSlide = needSlide;
}

public float getOpScale() {
	return opScale;
}

public void setOpScale(float opScale) {
	this.opScale = opScale;
}

public boolean isCompueConvolution() {
	return compueConvolution;
}

public void setCompueConvolution(boolean compueConvolution) {
	this.compueConvolution = compueConvolution;
}
	
	
	
}
