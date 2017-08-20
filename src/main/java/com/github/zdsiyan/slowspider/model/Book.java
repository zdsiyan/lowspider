package com.github.zdsiyan.slowspider.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class Book implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 725423901023089063L;
	protected String name;
	protected String author;
	
	protected List<Chapter> chapters;
	
	public void addChapter(Chapter chapter){
		if(chapters==null){
			chapters = new ArrayList<Chapter>();
		}
		chapters.add(chapter);
	}
}
