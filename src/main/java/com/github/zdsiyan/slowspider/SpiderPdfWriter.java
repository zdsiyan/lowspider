package com.github.zdsiyan.slowspider;

import java.io.FileOutputStream;
import java.io.IOException;

import com.github.zdsiyan.slowspider.config.GlobalConfig;
import com.github.zdsiyan.slowspider.model.Book;

import io.github.gitbucket.markedj.Marked;

/**
 * pdf writer.
 * 
 * @author zdsiyan
 */
public class SpiderPdfWriter extends AbstractWriter{

	public SpiderPdfWriter(GlobalConfig gc, Book book) {
		super(gc, book);
	}

	public FileOutputStream write() throws IOException{
		FileOutputStream fos = new FileOutputStream("abc.html");
		
		//book 2 html
		String result = Marked.marked("***中文***");
		
		fos.write(result.getBytes("UTF-8"));
		
		System.out.println("PDF Created!");
		return fos;
	}
}
