package com.github.zdsiyan.slowspider.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.github.zdsiyan.slowspider.SpiderHtmlWriter;
import com.github.zdsiyan.slowspider.SpiderOutputEmail;
import com.github.zdsiyan.slowspider.SpiderReader;
import com.github.zdsiyan.slowspider.config.EmailConfig;
import com.github.zdsiyan.slowspider.config.GlobalConfig;
import com.github.zdsiyan.slowspider.config.NodeConfig;
import com.github.zdsiyan.slowspider.config.QueryConfig;

public class Test {

	private static GlobalConfig init() {
		NodeConfig nc = new NodeConfig();
		nc.setLink("http://localhost:8080/");
		nc.setName("少年医仙");
		// QueryConfig link = new QueryConfig();
		QueryConfig content = new QueryConfig();
		content.setXpath("html/body/text()");
		Map<String, String> replaceContent = new HashMap<String, String>();
		replaceContent.put("飘天文学感谢各位书友的支持，您的支持就是我们最大的动力", "");
		replaceContent.put("\\{\\}", "");
		content.setReplace(replaceContent);

		nc.setContent(content);

		QueryConfig title = new QueryConfig();
		title.setXpath("html/body/h1/text()");

		Map<String, String> replaceTitle = new HashMap<String, String>();
		replaceTitle.put("正文", "");
		title.setReplace(replaceTitle);

		nc.setTitle(title);

		GlobalConfig gc = new GlobalConfig(nc);
		gc.setName("我的阅读日常");

		return gc;
	}

	private static GlobalConfig initList() {
		NodeConfig bc = new NodeConfig();
		bc.setLink("http://www.piaotian.com/html/8/8755/");
		bc.setName("少年医仙");
		bc.setSite("http://www.piaotian.com/html/8/8755/");

		QueryConfig links = new QueryConfig();
		links.setXpath("//div[@class='centent']//a");
		bc.setLinks(links);

		EmailConfig email = new EmailConfig();
		email.setHost("smtp.163.com");
		email.setUsername("account");
		email.setPassword("password");
		email.setFrom("from");
		email.setTo("target");
		

		QueryConfig content = new QueryConfig();
		content.setXpath("html/body/text()");
		Map<String, String> replaceContent = new HashMap<String, String>();
		replaceContent.put("飘天文学感谢各位书友的支持，您的支持就是我们最大的动力", "");
		replaceContent.put("\\{\\}", "");
		content.setReplace(replaceContent);

		bc.setContent(content);

		QueryConfig title = new QueryConfig();
		title.setXpath("html/body/h1/text()");

		Map<String, String> replaceTitle = new HashMap<String, String>();
		replaceTitle.put("正文", "");
		title.setReplace(replaceTitle);

		bc.setTitle(title);

		GlobalConfig gc = new GlobalConfig(bc);
		gc.setName("我的阅读日常");
		gc.setEmail(email);
		
		email.setTitle("我的阅读日常");
		email.setFilename("我的阅读日常.html");

		return gc;
	}

	private static void doit() throws Exception {
		GlobalConfig gc = initList();

		SerializerFeature[] features = { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.PrettyFormat };
		String config = JSONObject.toJSONString(gc,features);
		System.out.println(config);

		SpiderReader reader = new SpiderReader(gc) {
			int index = 0;

			protected boolean before(final String link, final NodeConfig bc, final Book book) {
				index++;
				if (index > 10) {
					return false;
				}

				return true;
			}

			protected boolean after(final HtmlPage page, final String link, final NodeConfig bc, final Book book) {
				return true;
			}
		};
		Book book = reader.run();

		System.out.println("**********************************");

		String result = JSONObject.toJSONString(book, features);
		// System.out.println(result);

		SpiderHtmlWriter writer = new SpiderHtmlWriter(gc, book);

		SpiderOutputEmail sof = new SpiderOutputEmail(writer);

		sof.doit();

	}

	public static void sendMail(String fromMail, String user, String password, String toMail, String mailTitle,
			String mailContent) throws Exception {
		Properties props = new Properties(); // 可以加载一个配置文件
		// 使用smtp：简单邮件传输协议
		props.put("mail.smtp.host", "smtp.163.com");// 存储发送邮件服务器的信息
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
		DataSource c = new DataSource() {

			@Override
			public String getContentType() {
				return "text/html";
			}

			@Override
			public InputStream getInputStream() throws IOException {
				InputStream is = new FileInputStream(new File("../2017年09月02日.html"));
				return is;
			}

			@Override
			public String getName() {
				return "abc.html";
			}

			@Override
			public OutputStream getOutputStream() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
		};
		//DataSource ds = new FileDataSource("../2017年09月02日.html");
		fileBP.setDataHandler(new DataHandler(c));
		fileBP.setFileName("abc.html");
		
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(textBP);
		mp.addBodyPart(fileBP);
		message.setContent(mp);
		
		//message.setText(mailContent); //发送 纯文本 邮件 todo
		//message.setContent(mailContent, "text/html;charset=gbk"); // 发送HTML邮件，内容样式比较丰富, 多半挂了
		message.setSentDate(new Date());// 设置发信时间
		message.saveChanges();// 存储邮件信息
		
		
		
		// 发送邮件
		// Transport transport = session.getTransport("smtp");
		Transport transport = session.getTransport();
		transport.connect(user, password);
		transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有已设好的收件人地址
		transport.close();
	}

	public static void main(String[] args) throws Exception {
		doit();
		 

	}
}
