package com.github.zdsiyan.slowspider;

import java.io.FileOutputStream;
import java.io.IOException;

import com.github.zdsiyan.slowspider.config.GlobalConfig;
import com.github.zdsiyan.slowspider.model.Book;
import com.qkyrie.markdown2pdf.Markdown2PdfConverter;

/**
 * pdf writer.
 * 
 * @author zdsiyan
 */
public class SpiderPdfWriter extends AbstractWriter {

	public SpiderPdfWriter(GlobalConfig gc, Book book) {
		super(gc, book);
	}

	public FileOutputStream write() throws IOException {
		FileOutputStream fos = new FileOutputStream("abc.pdf");
		
		Markdown2PdfConverter markdown2PdfConverter =
                Markdown2PdfConverter.newConverter();
		
		markdown2PdfConverter.readFrom(() -> "***Test***");

		System.out.println("PDF Created!");

		return fos;
	}

}
