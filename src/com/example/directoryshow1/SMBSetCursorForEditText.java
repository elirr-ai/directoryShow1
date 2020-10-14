package com.example.directoryshow1;

//  move horizontal: -1 left, +1 right 
//  move vertical: -11 up, +11 down  


public class SMBSetCursorForEditText {
private static int indexRow;
public static String string1;
public static int numberOfLines,midCursorPosition;

	public static int setCursor(String string,
			int currectLocation , int command) {
			String lines[] = string.split("[\\r\\n]");			
			
		int[] lineLength = new int[lines.length]; // mod to arraylist later
		for (int i=0;i<lines.length;i++) {
			if (i==0) lineLength[i]=lines[i].length()+1;
			else lineLength[i]=1+lines[i].length()+lineLength[i-1];
		}
		// find in which row we have the cursor right now
		for (int i=0;i<lines.length;i++) {
			if (currectLocation<lineLength[i]) {
				indexRow=i;
				break;
			}
		}
		numberOfLines=lines.length;
		midCursorPosition=lineLength[(int)(numberOfLines/2)];
		int returnIndex = currectLocation;
		if (command==11) {  // down
			 if (indexRow<lines.length-1) {//still room 2 go down  - not at bottom
				 
				 if (lineLength[1+indexRow]-  lineLength[indexRow]             ==1){
					 
						returnIndex=0+lineLength[indexRow];
				 }
				 else{
				returnIndex=(int)(lineLength[1+indexRow]-lineLength[indexRow])/2 +0+lineLength[indexRow];
				 }
			 
			 
			 
			 } else returnIndex=currectLocation;
		}
		
				
		else if (command==-11) {  ////////////////////////// up
			if (indexRow==0 ) {//at bottom do - nothing		
				returnIndex=currectLocation;
			}
			else if (indexRow==1) {//row 1 return 0 also
				returnIndex=0+(int)lineLength[0]/2     ;
			}
			else   {//still room 2 go up  - not at bottom
				returnIndex=0+lineLength[indexRow-2]+(int)(lineLength[indexRow-1]-lineLength[indexRow-2]) /2    ;
			}
			
		}
		
		else if (command==1) {// right

				try {
					if (currectLocation<lineLength[indexRow]-1) {
						returnIndex=currectLocation+1;
					}				 
					else returnIndex=currectLocation;
				} catch (Exception e) {
					returnIndex=currectLocation;
					e.printStackTrace();
				}
		}
			
		

		else if (command==-1) {// left
			returnIndex=currectLocation;
			if (indexRow==0) {	
				if (currectLocation>0) {
					returnIndex=currectLocation-1;
						}
			}
	else if (indexRow>0) {
		if (currectLocation>lineLength[indexRow-1]) {
			returnIndex=currectLocation-1;
				}
		else returnIndex=currectLocation;
			}	 
	}			
		
		return returnIndex;
		
	}
	
	public static String removeControlCharFull(String str) {
	    return str.replaceAll("[\\r\\n\\t]", "");
			}
	
	public static String getFormattedString(String string){
		String lines[] = string.split("[\\r\\n]");			

StringBuilder sb=new StringBuilder();
		for (int i=0;i<lines.length;i++) {
		sb.append(lines[i]+"\n");			
		}
		return string;
		
	}
	
	
}
