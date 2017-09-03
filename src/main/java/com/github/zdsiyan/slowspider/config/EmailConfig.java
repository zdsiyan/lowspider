package com.github.zdsiyan.slowspider.config;

import lombok.Data;

@Data
public class EmailConfig {
	/**
	 * 账号
	 */
	protected String host;
	protected String username;
	protected String password;
	/**
	 * 邮箱
	 */
	protected String from;
	protected String to;
	/**
	 * smtp
	 */
	protected Boolean auth=true;
	protected Boolean enable=true;
	protected Boolean required=true;
	/**
	 * 注入
	 */
	protected String title;
	protected String filename;
}
