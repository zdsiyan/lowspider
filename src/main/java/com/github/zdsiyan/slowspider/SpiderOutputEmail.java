package com.github.zdsiyan.slowspider;

import java.io.ByteArrayOutputStream;

import com.github.zdsiyan.slowspider.config.EmailConfig;
import com.github.zdsiyan.slowspider.util.SlowspiderUtil;

public class SpiderOutputEmail extends AbstractOutput {

	public SpiderOutputEmail(AbstractWriter writer) {
		super(writer);
	}
	
	

	@Override
	public void doit() throws Exception {
		if(getEmail()==null) return;
		EmailConfig emailConfig = getEmail();
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		writer.write(os);
		os.flush();
		os.close();
		
		

		SlowspiderUtil.sendMail(emailConfig, "",os.toByteArray());
	}

}
