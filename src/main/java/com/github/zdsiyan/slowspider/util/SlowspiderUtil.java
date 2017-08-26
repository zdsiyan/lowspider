package com.github.zdsiyan.slowspider.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.zdsiyan.slowspider.config.QueryConfig;
import com.github.zdsiyan.slowspider.model.Book;

import freemarker.template.Configuration;
import freemarker.template.Template;
import se.fishtank.css.selectors.Selectors;
import se.fishtank.css.selectors.dom.W3CNode;

public class SlowspiderUtil {
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getText(final HtmlPage page, final QueryConfig qc) {
		return getText(page, qc, "");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getText(final HtmlPage page, final QueryConfig qc, final String lineEnd) {
		StringBuffer sb = new StringBuffer();
		if (qc == null) {
			return sb.toString();
		}
		if (qc.hasCss()) {
			Selectors selectors = new Selectors(new W3CNode(page.getDocumentElement()));
			List<Node> nodes = selectors.querySelectorAll(qc.getCss());
			nodes.forEach(node -> {
				// node.get
			});
		} else if (qc.hasXpath()) {
			List<DomNode> nodes = (List<DomNode>) page.getByXPath(qc.getXpath());
			if (nodes != null && !nodes.isEmpty())
				for (DomNode node : nodes) {
					if (StringUtils.isNotEmpty(node.asText())) {
						sb.append(node.asText().trim()).append(lineEnd);
					}
				}
		}

		if (qc.hasReplace()) {
			return SlowspiderUtil.replace(qc, sb.toString());
		}
		return sb.toString();
	}

	/**
	 * 替换字符串
	 * 
	 * @param qc
	 * @param text
	 * @return
	 */
	public static String replace(final QueryConfig qc, final String text) {
		String result = text;
		Map<String, String> replace = qc.getReplace();
		for (Iterator<String> iter = replace.keySet().iterator(); iter.hasNext();) {
			String key = iter.next();
			result = result.replaceAll(key, replace.get(key));
		}
		return result;
	}

	/**
	 * 拼完整URL
	 * 
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
		if (domain.substring(domain.length() - 1, domain.length()).equals("/") == false) {
			result.append("/");
		}
		if (target.substring(0, 1).equals("/") == true) {
			result.append(target.substring(1));
		} else {
			String prefix = target.contains("/") ? target.substring(0, target.indexOf("/")) : "";
			// target不需要拼接的情况
			if (prefix.indexOf(".") > 0) {
				return target;
			}
			result.append(target);
		}

		return result.toString();

	}
	
	public static Template getTemplate(String templateName) {
		return getTemplate(null, templateName);
	}

	@SuppressWarnings("deprecation")
	public static Template getTemplate(String refTemplate, String templateName) {
		Configuration config = new Configuration();

		Template template = null;

		if (refTemplate == null || StringUtils.isEmpty(refTemplate)) {
			try {
				config.setClassForTemplateLoading(SlowspiderUtil.class.getClass(), "/template");
				template = config.getTemplate( "/" + templateName + ".ftl");
			} catch (IOException e) {
				throw new RuntimeException("can't get [" + templateName + "] freemarker template by " + refTemplate);
			}

		} else {
			try {
				File file = FileUtils.getFile(refTemplate);
				File directory = file.getParentFile();
				config.setDirectoryForTemplateLoading(directory);
				template = config.getTemplate(file.getName());
			} catch (IOException e) {
				throw new RuntimeException("can't get [" + templateName + "] freemarker template by " + refTemplate);
			}
		}

		return template;
	}
	
	public static String generateIndexMD(Book book){
		final StringBuffer result = new StringBuffer();
		if(book==null || book.hasChapter()==false) return result.toString();
		
		book.getChapters().forEach(chapter->{
			result.append("## "+chapter.getTitle());
		});
		System.out.println(result);
		return result.toString();
	}
	
}
