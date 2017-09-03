package com.github.zdsiyan.slowspider;

import java.io.FileOutputStream;

public class SpiderOutputFile extends AbstractOutput{

	public SpiderOutputFile(AbstractWriter writer) {
		super(writer);
	}

	@Override
	public void doit() throws Exception{
		
		FileOutputStream fos = new FileOutputStream("../"+writer.getBook().getTitle()+".html");
		writer.write(fos);
		fos.flush();
		fos.close();
	}

}
