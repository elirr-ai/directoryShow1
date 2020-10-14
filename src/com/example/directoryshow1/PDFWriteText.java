package com.example.directoryshow1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

public class PDFWriteText {

	private String dir,fname,fileOrg,read_string;
	private File file;
	Context context;
	String dateString;
	
	public PDFWriteText (Context context, String dir, String fileOrg,String text){
		this.context=context;
		this.dir=dir;
		this.fileOrg=fileOrg;
		this.fname=fileOrg+".pdf";
		this.read_string=text;
		init();
	}


	public void MakePdfFile (){
		Document document = new Document(PageSize.A4);
		// Create Pdf Writer for Writting into New Created Document
		try {
			PdfWriter.getInstance(document, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		// Open Document for Writting into document
		document.open(); 
		addMetaData(document);  
		try {
			addTitlePage(document);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Close Document after writting all content
		document.close();
	}

	    public void addTitlePage(Document document) throws DocumentException
	    {
	  // Font Style for Document
	  Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
	  Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, 22, Font.BOLD
	  | Font.UNDERLINE, BaseColor.GRAY);
	  Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
	  Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
	   
	  // Start New Paragraph
//	  Paragraph prHead = new Paragraph();
	  // Set Font in this Paragraph
//	  prHead.setFont(titleFont);
	  // Add item into Paragraph
//	  prHead.add("RESUME – Name\n");
	   
	  // Create Table into Document with 1 Row
//	  PdfPTable myTable = new PdfPTable(1);
	  // 100.0f mean width of table is same as Document size
//	  myTable.setWidthPercentage(100.0f);
	   
	  // Create New Cell into Table
//	  PdfPCell myCell = new PdfPCell(new Paragraph(""));
//	  myCell.setBorder(Rectangle.BOTTOM);
	   
	  // Add Cell into Table
//	  myTable.addCell(myCell);
	   
//	  prHead.setFont(catFont);
//	  prHead.add("\nName1 Name2\n");
//	  prHead.setAlignment(Element.ALIGN_CENTER);
	   
	  // Add all above details into Document
//	  document.add(prHead);
//	  document.add(myTable);
//	  document.add(myTable);
	   
	  // Now Start another New Paragraph
	  Paragraph prHeaderInfo = new Paragraph();
	  prHeaderInfo.setAlignment(Element.ALIGN_CENTER);
	  prHeaderInfo.setFont(smallBold);
	  prHeaderInfo.add("\nFile: "+fname);
	  prHeaderInfo.add("\nFolder: "+dir);
	  prHeaderInfo.add("\n Original file date: "+dateString);
	  prHeaderInfo.add("\n PDF file date: "+dateString);
	  	   
	  document.add(prHeaderInfo);
//	  document.add(myTable);
//	  document.add(myTable);
	   
	  Paragraph prMain = new Paragraph();////////////////////////////
	  prMain.setFont(smallBold);
//	  prProfile.add("\n \n Profile : \n ");
	  prMain.setFont(normal);
//	  prProfile.add("\nI am Mr. XYZ. I am Android Application Developer at TAG.");
	  prMain.add("\n"+read_string);
	  
	  prMain.setFont(smallBold);
	  document.add(prMain);
	   
	  // Create new Page in PDF
	  document.newPage();
	  }
		


	private void init() {
		File Path = new File(dir);
		if(!Path.exists()) {
		    Path.mkdirs();
		}
		file = new File(dir+File.separator+fname);
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy"); 
		dateString = formatter.format(new Date(new File(Path, fileOrg).lastModified()));
Toast.makeText(context, "Alarm size= "+dateString, Toast.LENGTH_SHORT).show(); 
		
	}
    // Set PDF document Properties
    public void addMetaData(Document document)
     
    {
    document.addTitle("Directory: "+dir);
    document.addSubject("File: "+fileOrg);
    document.addKeywords("Done by DirectotyShow1.apk");
    document.addAuthor("Eli Rajchert");
    document.addCreator(dateString);
    document.addCreationDate();


	}
	
	
}
