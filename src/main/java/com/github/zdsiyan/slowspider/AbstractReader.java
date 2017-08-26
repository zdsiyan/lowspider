package com.github.zdsiyan.slowspider;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.zdsiyan.slowspider.config.NodeConfig;
import com.github.zdsiyan.slowspider.config.GlobalConfig;

import se.fishtank.css.selectors.Selectors;
import se.fishtank.css.selectors.dom.W3CNode;

public abstract class AbstractReader {

	protected static final String HREF="href";
	
	protected final GlobalConfig gc;
	
	public AbstractReader(GlobalConfig gc){
		this.gc = gc;
	}
	
	protected BrowserVersion getBrowser(final NodeConfig bc){
		if(bc.hasBrowser()){
			//BrowserVersion.
		}
		else if(gc.hasBrowser()){
			//
		}
		return BrowserVersion.getDefault();
		
	}
	
	protected boolean getCss(final NodeConfig bc){
		if(bc.getCss()!=null){
			return bc.getCss();
		}
		else if(gc.getCss()!=null){
			return gc.getCss();
		}
		return false;
		
	}
	
	protected boolean getJs(final NodeConfig bc){
		if(bc.getJavascript()!=null){
			return bc.getJavascript();
		}
		else if(gc.getJavascript()!=null){
			return gc.getJavascript();
		}
		return false;
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List<String> getLinks(final HtmlPage page, final NodeConfig bc){
		List<String> result = new ArrayList<String>();
		if(bc.getLinks().hasCss()){
			Selectors selectors = new Selectors(new W3CNode(page.getDocumentElement()));
			List<Node>  nodes = selectors.querySelectorAll(bc.getLinks().getCss());
			nodes.forEach(node->{
				//node.get
				System.out.println(node.getTextContent());
			});

		}else if(bc.getLinks().hasXpath()){
			List<DomNode> linkNodes =  (List<DomNode>) page.getByXPath(bc.getLinks().getXpath());
			if(linkNodes!=null && !linkNodes.isEmpty()) for(DomNode node : linkNodes){
				String href = node.getAttributes().getNamedItem(HREF).getNodeValue();
				
				String target = SlowspiderUtil.calcComplateUrl(bc.getSite(), href);
				result.add(target);
			}
		}
		return result;
	}

}
