package com.github.zdsiyan.slowspider.model;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.zdsiyan.slowspider.SpiderHtmlWriter;
import com.github.zdsiyan.slowspider.SpiderReader;
import com.github.zdsiyan.slowspider.config.GlobalConfig;
import com.github.zdsiyan.slowspider.config.NodeConfig;
import com.github.zdsiyan.slowspider.config.QueryConfig;

public class Test {
	
	private static GlobalConfig init(){
		NodeConfig nc = new NodeConfig();
		nc.setLink("http://localhost:8080/");
		nc.setName("少年医仙");
		//QueryConfig link = new QueryConfig();
		QueryConfig content = new QueryConfig();
		content.setXpath("html/body/text()");
		Map<String, String> replaceContent = new HashMap<String,String>();
		replaceContent.put("飘天文学感谢各位书友的支持，您的支持就是我们最大的动力","");
		replaceContent.put("\\{\\}","");
		content.setReplace(replaceContent);
		
		nc.setContent(content);
		
		
		QueryConfig title = new QueryConfig();
		title.setXpath("html/body/h1/text()");

		Map<String, String> replaceTitle = new HashMap<String,String>();
		replaceTitle.put("正文","");
		title.setReplace(replaceTitle);
		
		nc.setTitle(title);
		
		
		GlobalConfig gc = new GlobalConfig(nc);
		gc.setName("我的阅读日常");
		
		return gc;
	}
	
	
	private static GlobalConfig initList(){
		NodeConfig bc = new NodeConfig();
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
	
	private static void doit() throws Exception{
		GlobalConfig gc = init();
		
		
		SerializerFeature[] features = {SerializerFeature.WriteDateUseDateFormat,SerializerFeature.PrettyFormat};
		//String config = JSONObject.toJSONString(gc,features);
		//System.out.println(config);
		
		SpiderReader reader = new SpiderReader(gc){
			protected boolean before(final String link, final NodeConfig bc, final Book book){
				return true;
			}
			
			protected boolean after(final HtmlPage page, final String link, final NodeConfig bc, final Book book){
				return true;
			}
		};
		Book book = reader.run();

		System.out.println("**********************************");
		
		String result = JSONObject.toJSONString(book,features);
		//System.out.println(result);

		SpiderHtmlWriter writer = new SpiderHtmlWriter(gc, book);
		FileOutputStream fos = writer.write();
		fos.flush();
		fos.close();
		
	}
	
	
	public static void main(String[] args) throws Exception {
		doit();
		/*
		Markdown2PdfConverter
        .newConverter()
        .readFrom(new SimpleStringMarkdown2PdfReader("&#20013;&#21326;&#20154;&#27665;"))
        .writeTo(new SimpleFileMarkdown2PdfWriter(new File("abc.pdf")))
        .doIt();
		
		 * SimpleFileMarkdown2PdfWriter
		 * Markdown2PdfConverter
        .newConverter()
        .readFrom(new SimpleStringMarkdown2PdfReader(markdown))
        .writeTo(new SpiderPdfWriter(new File("abc.pdf", )))
        .doIt();*/
		
		/*String html1 = Marked.marked("***中文***");
		System.out.println(html1);*/
		
	}
}
