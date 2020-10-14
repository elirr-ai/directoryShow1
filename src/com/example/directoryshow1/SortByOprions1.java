package com.example.directoryshow1;

import java.util.Comparator;

public class SortByOprions1 {

	Comparator<DirectoryFilesHolder> compareById = new Comparator<DirectoryFilesHolder>() {
	    @Override
	    public int compare(DirectoryFilesHolder o1, DirectoryFilesHolder o2) {
	        return o1.getFullFileName().compareTo(o2.getFullFileName());
	    }
	};
	
}
