package com.github.zdsiyan.slowspider.config;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

@Data
public class QueryConfig {
	protected String css;
	protected String xpath;
	protected String reg;
	// replace
	protected Map<String,String> replace;
	
	public boolean hasXpath(){
		if(StringUtils.isNotBlank(xpath)){
			return true;
		}
		return false;
	}
	
	public boolean hasCss(){
		if(StringUtils.isNotBlank(css)){
			return true;
		}
		return false;
	}
	
	public boolean hasReplace(){
		if(replace!=null || replace.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean hasReg(){
		if(StringUtils.isNotBlank(reg)){
			return true;
		}
		return false;
	}
}
