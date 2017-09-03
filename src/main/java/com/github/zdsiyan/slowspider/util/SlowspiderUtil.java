package com.github.zdsiyan.slowspider.util;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.zdsiyan.slowspider.config.EmailConfig;
import com.github.zdsiyan.slowspider.config.QueryConfig;
import com.github.zdsiyan.slowspider.model.Book;

import freemarker.template.Configuration;
import freemarker.template.Template;
import se.fishtank.css.selectors.Selectors;
import se.fishtank.css.selectors.dom.W3CNode;

public class SlowspiderUtil {
	
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
	
	/**
	 * 获取模板文件, 支持定制模板
	 * @param templateName
	 * @return
	 */
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
	
	/**
	 * 生成目录
	 * @param book
	 * @return
	 */
	public static String generateIndexMD(Book book){
		final StringBuffer result = new StringBuffer();
		if(book==null || book.hasChapter()==false) return result.toString();
		
		book.getChapters().forEach(chapter->{
			result.append("## "+chapter.getTitle()+"\n");
		});
		//System.out.println(result);
		return result.toString();
	}
	
	/**
	 * 推送邮件
	 */
	public static void sendMail(String host, String fromMail, String user, String password, String toMail, String mailTitle,
			String mailContent, byte[] data, String filename) throws Exception {
		Properties props = new Properties(); // 可以加载一个配置文件
		// 使用smtp：简单邮件传输协议
		props.put("mail.smtp.host", host);// 存储发送邮件服务器的信息
		props.put("mail.smtp.auth", "true");// 同时通过验证

		Session session = Session.getInstance(props);// 根据属性新建一个邮件会话
		// session.setDebug(true); //有他会打印一些调试信息。

		MimeMessage message = new MimeMessage(session);// 由邮件会话新建一个消息对象
		message.setFrom(new InternetAddress(fromMail));// 设置发件人的地址
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(toMail));// 设置收件人,并设置其接收类型为TO
		message.setSubject(mailTitle);// 设置标题
		// 设置信件内容
		MimeBodyPart textBP = new MimeBodyPart();
		textBP.setText(mailContent);
	    
		MimeBodyPart fileBP = new MimeBodyPart();
        
		ByteArrayDataSource ds = new ByteArrayDataSource(data, "text/html");
		fileBP.setDataHandler(new DataHandler(ds));
		fileBP.setFileName(filename);
		
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(textBP);
		mp.addBodyPart(fileBP);
		message.setContent(mp);
		
		//message.setText(mailContent); //发送 纯文本 邮件 todo
		//message.setContent(mailContent, "text/html;charset=gbk"); // 发送HTML邮件，内容样式比较丰富, 有些邮箱会拒信
		message.setSentDate(new Date());// 设置发信时间
		message.saveChanges();// 存储邮件信息
		
		// 发送邮件
		// Transport transport = session.getTransport("smtp");
		Transport transport = session.getTransport();
		transport.connect(user, password);
		transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有已设好的收件人地址
		transport.close();
	}

	public static void sendMail(EmailConfig config, String text, byte[] data) throws Exception {
		Properties props = new Properties(); // 可以加载一个配置文件
		// 使用smtp：简单邮件传输协议
		props.put("mail.smtp.host", config.getHost());// 存储发送邮件服务器的信息
		props.put("mail.smtp.auth", config.getAuth());// 同时通过验证

		Session session = Session.getInstance(props);// 根据属性新建一个邮件会话
		// session.setDebug(true); //有他会打印一些调试信息。

		MimeMessage message = new MimeMessage(session);// 由邮件会话新建一个消息对象
		message.setFrom(new InternetAddress(config.getFrom()));// 设置发件人的地址
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(config.getTo()));// 设置收件人,并设置其接收类型为TO
		message.setSubject(config.getTitle());// 设置标题
		// 设置信件内容
		MimeBodyPart textBP = new MimeBodyPart();
		textBP.setText(text);
	    
		MimeBodyPart fileBP = new MimeBodyPart();
        
		ByteArrayDataSource ds = new ByteArrayDataSource(data, "text/html");
		fileBP.setDataHandler(new DataHandler(ds));
		fileBP.setFileName(config.getFilename());
		
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(textBP);
		mp.addBodyPart(fileBP);
		message.setContent(mp);
		
		//message.setText(mailContent); //发送 纯文本 邮件 todo
		//message.setContent(mailContent, "text/html;charset=gbk"); // 发送HTML邮件，内容样式比较丰富, 有些邮箱会拒信
		message.setSentDate(new Date());// 设置发信时间
		message.saveChanges();// 存储邮件信息
		
		// 发送邮件
		// Transport transport = session.getTransport("smtp");
		Transport transport = session.getTransport();
		transport.connect(config.getUsername(), config.getPassword());
		transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有已设好的收件人地址
		transport.close();
		
	}
	
}
