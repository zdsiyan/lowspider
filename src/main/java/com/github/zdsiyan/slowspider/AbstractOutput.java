package com.github.zdsiyan.slowspider;

import com.github.zdsiyan.slowspider.config.EmailConfig;
import com.github.zdsiyan.slowspider.config.GlobalConfig;

public abstract class AbstractOutput {
	protected AbstractWriter writer;
	
	public AbstractOutput(AbstractWriter writer){
		this.writer = writer;
	}
	
	protected GlobalConfig getGlobal(){
		return writer.getGc();
	}
	
	protected EmailConfig getEmail(){
		if(getGlobal()==null)
			return null;
		return writer.getGc().getEmail();
	}
	
	public abstract void doit() throws Exception;
	
}
