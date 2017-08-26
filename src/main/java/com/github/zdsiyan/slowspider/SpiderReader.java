package com.github.zdsiyan.slowspider;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.zdsiyan.slowspider.config.NodeConfig;
import com.github.zdsiyan.slowspider.config.GlobalConfig;
import com.github.zdsiyan.slowspider.model.Book;
import com.github.zdsiyan.slowspider.model.Chapter;
import com.github.zdsiyan.slowspider.util.SlowspiderUtil;

/**
 * @author zdsiyan
 */
public abstract class SpiderReader extends AbstractReader{
	
	public SpiderReader(GlobalConfig gc){
		super(gc);
	}
	
	public SpiderReader(NodeConfig bc){
		super(new GlobalConfig(bc));
	}
	
	protected Book init(){
		Book book = new Book();
		book.setName(gc.getName());
		return book;
	}
	
	public Book run(){
		Book book = init();
		gc.getNodeConfig().forEach(bc->readBook(bc, book));
		
		return book;
	}
	
	protected void readBook(final NodeConfig bc, final Book book){
		if(bc.hasLink()==false){
			return;
		}
		final WebClient webClient= new WebClient(getBrowser(bc));
		webClient.getOptions().setCssEnabled(getCss(bc));
		webClient.getOptions().setJavaScriptEnabled(getJs(bc));
		
		try {
			final HtmlPage page = (HtmlPage)webClient.getPage(bc.getLink());
			
			if(bc.hasLinks()){
				List<String> links = getLinks(page, bc);
				links.forEach(link->{
					if(before(link,bc,book)) try{
						HtmlPage childPage = (HtmlPage)webClient.getPage(link);
						readChapter(childPage, link, bc, book);
						
						childPage.cleanUp();
					} catch (FailingHttpStatusCodeException | IOException e) {
						e.printStackTrace();
					}
					
				});
				
			}else{
				readChapter(page, bc.getLink(), bc, book);
				
				page.cleanUp();
			}
			
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}finally{
			webClient.close();
		}
	}

	protected abstract boolean before(final String link, final NodeConfig bc, final Book book);
	
	protected abstract boolean after(final HtmlPage page, final String link, final NodeConfig bc, final Book book);
	
	protected void readChapter(final HtmlPage page, final String link, final NodeConfig bc, final Book book){
		if(StringUtils.isBlank(link) ){
			return;
		}
		
		Chapter chapter = new Chapter();
		chapter.setLink(link);
		chapter.setContent(SlowspiderUtil.getText(page, bc.getContent(), "\n\n"));
		chapter.setTitle(SlowspiderUtil.getText(page, bc.getTitle()));
		chapter.setTimeline(SlowspiderUtil.getText(page, bc.getTimeline()));
		book.addChapter(chapter);
		
		if(after(page,link,bc,book)==false){
			return;
		}
	}
	
}
