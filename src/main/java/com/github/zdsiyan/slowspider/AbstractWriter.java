package com.github.zdsiyan.slowspider;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.github.zdsiyan.slowspider.config.GlobalConfig;
import com.github.zdsiyan.slowspider.model.Book;

import lombok.Data;

@Data
public abstract class AbstractWriter{
	protected static DateFormat df = new SimpleDateFormat("yyyy-MM-dd"); 
	protected GlobalConfig gc;
	protected Book book;
	
	public AbstractWriter(GlobalConfig gc, Book book){
		this.gc = gc;
		this.book = book;
	}
	
	public abstract void write(OutputStream os) throws IOException;

}
