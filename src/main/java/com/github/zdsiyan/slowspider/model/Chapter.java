package com.github.zdsiyan.slowspider.model;

import java.io.Serializable;
import lombok.Data;

@Data
public class Chapter implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7087371898172263313L;
	
	protected String link;
	
	protected String title;
	
	protected String content;
	
	protected String timeline;
	
	protected boolean pushed;
	
	// timeline or index or custom str
	protected String sort;
}
