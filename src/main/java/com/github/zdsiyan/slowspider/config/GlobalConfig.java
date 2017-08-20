package com.github.zdsiyan.slowspider.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zdsiyan
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class GlobalConfig extends BaseConfig{
	// name
	protected String name;
	protected List<BookConfig> bookConfig;
	
	public GlobalConfig(){
		bookConfig = new ArrayList<BookConfig>();
	}
	
	public GlobalConfig(BookConfig bc){
		this();
		bookConfig.add(bc);
	}
	
}
