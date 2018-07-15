package com.lc.xljk.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * 小程序相关配置
 * @author shiqinli
 *
 */
@Component
@ConfigurationProperties(prefix="xljkapp")
@PropertySource("classpath:xljkprogram.properties")
public class XljkProgramConfig {
	private String skey;
	private String appid;
	private String appsecret;
	private String wxsessionurl;
	public String getSkey() {
		return skey;
	}
	public void setSkey(String skey) {
		this.skey = skey;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getAppsecret() {
		return appsecret;
	}
	public void setAppsecret(String appsecret) {
		this.appsecret = appsecret;
	}
	public String getWxsessionurl() {
		return wxsessionurl;
	}
	public void setWxsessionurl(String wxsessionurl) {
		this.wxsessionurl = wxsessionurl;
	}
}
