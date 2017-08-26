package com.github.zdsiyan.slowspider;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

import com.github.zdsiyan.slowspider.config.GlobalConfig;
import com.github.zdsiyan.slowspider.model.Book;
import com.github.zdsiyan.slowspider.util.SlowspiderUtil;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.github.gitbucket.markedj.Marked;

/**
 * pdf writer.
 * 
 * @author zdsiyan
 */
public class SpiderHtmlWriter extends AbstractWriter{

	public SpiderHtmlWriter(GlobalConfig gc, Book book) {
		super(gc, book);
	}
	
	private void parseBook(){
	}

	public FileOutputStream write() throws IOException{
		//String result = SlowspiderUtil.generateIndexMD(book);
		
		Template template = SlowspiderUtil.getTemplate("simple");
		
		FileOutputStream fos = new FileOutputStream("../"+book.getTitle()+".html");
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("book", book);
		param.put("util", new SlowspiderUtil());
		param.put("md", new Marked());

		//book 2 html
		OutputStreamWriter osw = new OutputStreamWriter(fos);
		try {
			template.process(param, osw);
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		//fos.write(result.getBytes("UTF-8"));
		
		System.out.println("html file Created!");
		return fos;
	}
}
