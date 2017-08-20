package com.github.zdsiyan.slowspider.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.zdsiyan.slowspider.SpiderPdfWriter;
import com.github.zdsiyan.slowspider.SpiderReader;
import com.github.zdsiyan.slowspider.config.BookConfig;
import com.github.zdsiyan.slowspider.config.GlobalConfig;
import com.github.zdsiyan.slowspider.config.QueryConfig;
import com.qkyrie.markdown2pdf.Markdown2PdfConverter;
import com.qkyrie.markdown2pdf.internal.exceptions.ConversionException;
import com.qkyrie.markdown2pdf.internal.exceptions.Markdown2PdfLogicException;
import com.qkyrie.markdown2pdf.internal.reading.SimpleStringMarkdown2PdfReader;
import com.qkyrie.markdown2pdf.internal.writing.SimpleFileMarkdown2PdfWriter;

public class Test {
	
	private static GlobalConfig init(){
		BookConfig bc = new BookConfig();
		bc.setLink("http://localhost:8080/");
		bc.setName("少年医仙");
		//QueryConfig link = new QueryConfig();
		QueryConfig content = new QueryConfig();
		content.setXpath("html/body/text()");
		Map<String, String> replaceContent = new HashMap<String,String>();
		replaceContent.put("飘天文学感谢各位书友的支持，您的支持就是我们最大的动力","");
		replaceContent.put("\\{\\}","");
		content.setReplace(replaceContent);
		
		bc.setContent(content);
		
		
		QueryConfig title = new QueryConfig();
		title.setXpath("html/body/h1/text()");

		Map<String, String> replaceTitle = new HashMap<String,String>();
		replaceTitle.put("正文","");
		title.setReplace(replaceTitle);
		
		bc.setTitle(title);
		
		
		GlobalConfig gc = new GlobalConfig(bc);
		gc.setName("我的阅读日常");
		
		return gc;
	}
	
	
	private static GlobalConfig initList(){
		BookConfig bc = new BookConfig();
		bc.setLink("http://localhost:8080/list");
		bc.setName("少年医仙");
		bc.setSite("http://www.piaotian.com/html/8/8755/");

		QueryConfig links = new QueryConfig();
		links.setXpath("//div[@class='centent']//a");
		bc.setLinks(links);
		
		
		QueryConfig content = new QueryConfig();
		content.setXpath("html/body/text()");
		Map<String, String> replaceContent = new HashMap<String,String>();
		replaceContent.put("飘天文学感谢各位书友的支持，您的支持就是我们最大的动力","");
		replaceContent.put("\\{\\}","");
		content.setReplace(replaceContent);
		
		bc.setContent(content);
		
		
		QueryConfig title = new QueryConfig();
		title.setXpath("html/body/h1/text()");

		Map<String, String> replaceTitle = new HashMap<String,String>();
		replaceTitle.put("正文","");
		title.setReplace(replaceTitle);
		
		bc.setTitle(title);
		
		
		GlobalConfig gc = new GlobalConfig(bc);
		gc.setName("我的阅读日常");
		
		return gc;
	}
	
	private static void read(){
		GlobalConfig gc = init();
		
		
		SerializerFeature[] features = {SerializerFeature.WriteDateUseDateFormat,SerializerFeature.PrettyFormat};
		String config = JSONObject.toJSONString(gc,features);
		
		System.out.println(config);
		
		SpiderReader reader = new SpiderReader(gc){
			protected boolean before(final String link, final BookConfig bc, final Book book){
				return true;
			}
			
			protected boolean after(final HtmlPage page, final String link, final BookConfig bc, final Book book){
				return true;
			}
		};
		Book book = reader.run();

		System.out.println("**********************************");
		
		String result = JSONObject.toJSONString(book,features);
		System.out.println(result);
		
		SpiderPdfWriter writer = new SpiderPdfWriter(gc, book);
		try {
			FileOutputStream fos = writer.write();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) throws ConversionException, Markdown2PdfLogicException {
		//read();
		
		Markdown2PdfConverter
        .newConverter()
        .readFrom(new SimpleStringMarkdown2PdfReader("***Test***"))
        .writeTo(new SimpleFileMarkdown2PdfWriter(new File("abc.pdf")))
        .doIt();
	}
}
