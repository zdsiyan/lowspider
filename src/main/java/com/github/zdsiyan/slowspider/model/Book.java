package com.github.zdsiyan.slowspider.model;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Book implements Serializable{
	
	private DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
	/**
	 * 
	 */
	private static final long serialVersionUID = 725423901023089063L;
	protected String title = df.format(new Date());
	protected String name;
	// markdown
	protected String index;
	
	protected List<Chapter> chapters;
	
	public void addChapter(Chapter chapter){
		if(chapters==null){
			chapters = new ArrayList<Chapter>();
		}
		chapters.add(chapter);
	}
	
	public boolean hasChapter(){
		if(chapters==null)
			return false;
		return true;
	}
}
