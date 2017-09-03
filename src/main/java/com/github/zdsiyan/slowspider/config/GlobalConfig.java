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
	protected EmailConfig email;
	protected List<NodeConfig> nodeConfig;
	
	public GlobalConfig(){
		nodeConfig = new ArrayList<NodeConfig>();
	}
	
	public GlobalConfig(NodeConfig bc){
		this();
		nodeConfig.add(bc);
	}
	
}
