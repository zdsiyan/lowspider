package com.github.zdsiyan.slowspider;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.zdsiyan.slowspider.config.QueryConfig;

import se.fishtank.css.selectors.Selectors;
import se.fishtank.css.selectors.dom.W3CNode;

public class SlowspiderUtil {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getText(final HtmlPage page, final QueryConfig qc){
		StringBuffer sb = new StringBuffer();
		if(qc==null){
			return sb.toString();
		}
		if(qc.hasCss()){
			Selectors selectors = new Selectors(new W3CNode(page.getDocumentElement()));
			List<Node>  nodes = selectors.querySelectorAll(qc.getCss());
			nodes.forEach(node->{
				//node.get
			});
		}else if(qc.hasXpath()){
			List<DomNode> nodes =  (List<DomNode>) page.getByXPath(qc.getXpath());
			if(nodes!=null && !nodes.isEmpty()) for(DomNode node : nodes){
				if(StringUtils.isNotEmpty(node.asText())){
					sb.append(node.asText().trim());
				}
			}
		}
		
		if(qc.hasReplace()){
			return SlowspiderUtil.replace(qc, sb.toString());
		}
		return sb.toString();
	}
	
	/**
	 * 替换字符串
	 * @param qc
	 * @param text
	 * @return
	 */
	public static String replace(final QueryConfig qc, final String text){
		String result = text;
		Map<String, String> replace = qc.getReplace();
		for(Iterator<String> iter = replace.keySet().iterator();iter.hasNext();){
			String key = iter.next();
			result = result.replaceAll(key, replace.get(key));
		}
		return result;
	}
	
	
	/**
	 * 拼完整URL
	 * @param domain
	 * @param target
	 * @return
	 */
	public static String calcComplateUrl(String domain, String target) {
		// 非空
		if (StringUtils.isBlank(target)) {
			return null;
		}
		if (StringUtils.isBlank(domain)) {
			return target;
		}
		if (target.toLowerCase().startsWith("http:") || target.toLowerCase().startsWith("https:")) {
			return target;
		}

		StringBuffer result = new StringBuffer(domain);
		if(domain.substring(domain.length()-1, domain.length()).equals("/")==false){
			result.append("/");
		}
		if(target.substring(0,1).equals("/")==true){
			result.append(target.substring(1));
		}else{
			String prefix = target.contains("/")?target.substring(0, target.indexOf("/")):"";
			// target不需要拼接的情况
			if(prefix.indexOf(".")>0){
				return target;
			}
			result.append(target);
		}
		
		return result.toString();

	}
}
