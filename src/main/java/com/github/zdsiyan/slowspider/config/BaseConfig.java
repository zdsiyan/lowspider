package com.github.zdsiyan.slowspider.config;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;

/**
 * @author zdsiyan
 */
@Data
public class BaseConfig {
	protected String browser;
	protected Boolean javascript;
	protected Boolean css;
	
	public boolean hasBrowser(){
		if(StringUtils.isNoneBlank(browser)){
			return true;
		}
		return false;
	}
}
