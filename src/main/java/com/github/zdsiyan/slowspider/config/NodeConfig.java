package com.github.zdsiyan.slowspider.config;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zdsiyan
 */
@Data
@EqualsAndHashCode(callSuper=true)
public class NodeConfig extends BaseConfig {
	// bookname
	protected String name;
	// author
	protected String author;
	// site
	protected String site;
	// link
	protected String link;
	// links
	protected QueryConfig links;
	// title
	protected QueryConfig title;
	// content
	protected QueryConfig content;
	// timeline
	protected QueryConfig timeline;
	
	public boolean hasLink(){
		if(StringUtils.isNotBlank(link)){
			return true;
		}
		return false;
	}
	
	public boolean hasLinks(){
		if(links!=null){
			return true;
		}
		return false;
	}
	
	public boolean hasTitle(){
		if(title!=null){
			return true;
		}
		return false;
	}
	
	public boolean hasContent(){
		if(content!=null){
			return true;
		}
		return false;
	}
	
	public boolean hasTimeline(){
		if(timeline!=null){
			return true;
		}
		return false;
	}
	
}
